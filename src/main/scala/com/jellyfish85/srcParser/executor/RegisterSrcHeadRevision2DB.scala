package com.jellyfish85.srcParser.executor

import com.jellyfish85.srcParser.utils.ApplicationProperties

class RegisterSrcHeadRevision2DB {

  def run(args: Array[String]) {

    ApplicationProperties.targetProjectNames.foreach {projectName: String => println(projectName)}

  }

}