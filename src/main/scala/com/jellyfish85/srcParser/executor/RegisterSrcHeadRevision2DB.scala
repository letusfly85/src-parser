package com.jellyfish85.srcParser.executor

import com.jellyfish85.srcParser.utils.SrcParserProp
import com.jellyfish85.svnaccessor.getter.SVNGetFiles
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSvnSrcInfoDao
import com.jellyfish85.srcParser.converter.ConvSVNRequestBean2RsSvnSrcInfoBean
import com.jellyfish85.dbaccessor.src.mainte.tool.RsSvnSrcInfoBean

/**
 * == RegisterSrcHeadRevision2DB ==
 *
 * This class will be called when the beginning of a day.
 * After then, the diff getter class will gather information from subversion by jenkins polling.
 *
 * @author wada shunsuke
 *
 * @example
 *   gradle run -Prunargs=com.jellyfish85.srcParser.executor.RegisterSrcHeadRevision2DB
 *
 */
class RegisterSrcHeadRevision2DB extends ExecutorTrait {

  def run(args: Array[String]){
    databaseInitialize

    val projectNames: List[String] = parserProp.targetProjectNames

    val getter: SVNGetFiles[SVNRequestBean] = new SVNGetFiles

    val src: String =  parserProp.src
    def simpleFilter(bean: SVNRequestBean): Boolean = {
      bean.path.matches(".*" + src + "/.*")
    }

    val dao: RsSvnSrcInfoDao = new RsSvnSrcInfoDao
    dao.deleteAll(db.conn)
    db.jCommit

    val converter: ConvSVNRequestBean2RsSvnSrcInfoBean =
      new ConvSVNRequestBean2RsSvnSrcInfoBean

    var list: List[SVNRequestBean] = List()
    projectNames.foreach {projectName: String =>
      list = List()

      val path = parserProp.app + projectName
      println("getting info " + path + " .....")

      list = getter.getSVNInfo(path, simpleFilter)

      val targetList: List[RsSvnSrcInfoBean] = converter.convert(list, projectName)
      if (!targetList.isEmpty) {dao.insert(db.conn, targetList)}
      db.jCommit

    }

    databaseFinalize
  }
}