package com.jellyfish85.srcParser.utils

trait ProjectNameUtils {

  def getProjectName(path: String): Option[String] = {
    val parserProp: SrcParserProp = new SrcParserProp

    parserProp.targetProjectNames.foreach {projectName: String =>
      val reg = ".*" +  parserProp.app + projectName + ".*"
      if (path.matches(reg)) {
        return Some(projectName)
      }
    }

    None
  }
}
