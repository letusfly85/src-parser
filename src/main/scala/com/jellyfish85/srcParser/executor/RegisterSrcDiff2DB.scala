package com.jellyfish85.srcParser.executor

import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSvnSrcInfoDao
import java.math.BigDecimal
import com.jellyfish85.svnaccessor.bean.SVNDiffBean
import com.jellyfish85.svnaccessor.getter.SVNDiffGetter
import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.svnaccessor.manager.SVNManager

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
class RegisterSrcDiff2DB extends ExecutorTrait {

  def run(args: Array[String])  {
    databaseInitialize

    val dao: RsSvnSrcInfoDao = new RsSvnSrcInfoDao
    val preHeadRevision: BigDecimal = dao.findHeadRevision(db.conn)

    val manager: SVNManager = new SVNManager
    val headPath: String = manager.repository.getLocation.toString + ApplicationProperties.app

    val getter: SVNDiffGetter = new SVNDiffGetter
    val list: List[SVNDiffBean] = getter.get(headPath, preHeadRevision.longValue())


    //TODO get detail of the source and register database
    list.foreach {bean: SVNDiffBean =>
      println(bean.fileName)
    }
  }

}
