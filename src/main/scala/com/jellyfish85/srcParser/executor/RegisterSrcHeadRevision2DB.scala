package com.jellyfish85.srcParser.executor

import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.svnaccessor.getter.SVNGetFiles
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSvnSrcInfoDao
import com.jellyfish85.srcParser.converter.ConvSVNRequestBean2RsSvnSrcInfoBean
import com.jellyfish85.dbaccessor.src.mainte.tool.RsSvnSrcInfoBean

class RegisterSrcHeadRevision2DB extends ExecutorTrait {

  def run(args: Array[String]){
    databaseInitialize

    val projectNames: List[String] = ApplicationProperties.targetProjectNames

    val getter: SVNGetFiles = new SVNGetFiles

    def simpleFilter(bean: SVNRequestBean): Boolean = {
      bean.path.matches(".*" + ApplicationProperties.src + "/.*")
    }

    val dao: RsSvnSrcInfoDao = new RsSvnSrcInfoDao
    val converter: ConvSVNRequestBean2RsSvnSrcInfoBean = new ConvSVNRequestBean2RsSvnSrcInfoBean

    var list: List[SVNRequestBean] = List()
    projectNames.foreach {projectName: String =>
      val path = ApplicationProperties.app + projectName
      println("getting info " + path + " .....")

      list :::= getter.getSVNInfo(path, simpleFilter)
      list.foreach (x => println(x.fileName))

      val targetList: List[RsSvnSrcInfoBean] = converter.convert(list, projectName)

      if (!targetList.isEmpty) {dao.insert(db.conn, targetList)}
      db.jCommit

      sys.exit()
    }

    databaseFinalize
  }
}