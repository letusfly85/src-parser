package com.jellyfish85.srcParser.executor

import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.svnaccessor.getter.SVNGetFiles
import com.jellyfish85.svnaccessor.bean.SVNRequestBean

class RegisterSrcHeadRevision2DB {

  def run(args: Array[String]) {

    val projectNames: List[String] = ApplicationProperties.targetProjectNames

    val getter: SVNGetFiles = new SVNGetFiles

    def simpleFilter(bean: SVNRequestBean): Boolean = {
      bean.path.matches(".*/" + ApplicationProperties.src + "/.*")
    }
    projectNames.foreach {projectName: String =>
      val path = ApplicationProperties.app + projectName
      getter.getSVNInfo(path, simpleFilter)
    }
  }
}