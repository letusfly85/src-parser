package com.jellyfish85.srcParser.executor

import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.svnaccessor.getter.SVNGetFiles
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSvnSrcInfoDao

class RegisterSrcHeadRevision2DB extends ExecutorTrait {

  def run(args: Array[String]){
    databaseInitialize

    val projectNames: List[String] = ApplicationProperties.targetProjectNames

    val getter: SVNGetFiles = new SVNGetFiles

    def simpleFilter(bean: SVNRequestBean): Boolean = {
      bean.path.matches(".*/" + ApplicationProperties.src + "/.*")
    }


    var list: List[SVNRequestBean] = List()
    projectNames.foreach {projectName: String =>
      val path = ApplicationProperties.app + projectName
      list :::= getter.getSVNInfo(path, simpleFilter)
      list.foreach (x => println(x.fileName))

      sys.exit()
    }

    val dao: RsSvnSrcInfoDao = new RsSvnSrcInfoDao
    dao.insert(db.conn, list)

  }
}