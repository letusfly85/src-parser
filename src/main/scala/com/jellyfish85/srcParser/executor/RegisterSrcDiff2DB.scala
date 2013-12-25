package com.jellyfish85.srcParser.executor

import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSvnSrcInfoDao
import java.math.BigDecimal
import com.jellyfish85.svnaccessor.bean.SVNDiffBean
import com.jellyfish85.svnaccessor.getter.{SVNGetFiles, SVNDiffGetter}
import com.jellyfish85.srcParser.utils.{ProjectNameUtils, SrcParserProp}
import com.jellyfish85.svnaccessor.manager.SVNManager
import com.jellyfish85.srcParser.converter.ConvSVNRequestBean2RsSvnSrcInfoBean
import com.jellyfish85.dbaccessor.src.mainte.tool.RsSvnSrcInfoBean

/**
 * == RegisterSrcDiff2DB ==
 *
 * when it is called by jenkins, it will compare two revision of subversion
 * and search only difference programs and register them to a database.
 *
 *
 *@example
 *    gradle run -Prunargs=com.jellyfish85.srcParser.executor.RegisterSrcDiff2DB
 *
 * @author wada shunsuke
 *
 */
class RegisterSrcDiff2DB extends ExecutorTrait with ProjectNameUtils {

  def run(args: Array[String])  {
    databaseInitialize

    val dao: RsSvnSrcInfoDao = new RsSvnSrcInfoDao
    val preHeadRevision: BigDecimal = dao.findHeadRevision(db.conn)

    val manager: SVNManager = new SVNManager
    val headPath: String = manager.repository.getLocation.toString + parserProp.app

    val getter: SVNDiffGetter = new SVNDiffGetter
    val list: List[SVNDiffBean] = getter.get(headPath, preHeadRevision.longValue())

    list.foreach {bean: SVNDiffBean =>
      println("[" + bean.modificationType.toString + "]\t" + bean.path)}

    val modifier: SVNGetFiles[SVNDiffBean] = new SVNGetFiles
    val _list: List[SVNDiffBean] = modifier.modifyAttribute2Current(list)

    val converter: ConvSVNRequestBean2RsSvnSrcInfoBean = new ConvSVNRequestBean2RsSvnSrcInfoBean
    var targetList: List[RsSvnSrcInfoBean] = List()
    _list.foreach {diff: SVNDiffBean =>
      getProjectName(diff.path) match {
        case Some(projectName) =>
          targetList ::= converter.convert(diff, projectName)

        case _ =>
      }
    }

    targetList.foreach {diff: RsSvnSrcInfoBean =>
      dao.merge(db.conn, diff)
    }
    db.jCommit

    databaseFinalize
  }

}
