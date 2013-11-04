package com.jellyfish85.srcParser.utils

//case class ProjectName(projectName: String)

trait ProjectNameUtils {
  val projectNameList: List[String] = ApplicationProperties.targetProjectNames

  def getProjectName(path: String): Option[String] = {

    projectNameList.foreach {projectName: String =>
      val reg = ".*" +  ApplicationProperties.app + projectName + ".*"
      if (path.matches(reg)) {
        return Some(projectName)
      }
    }

    None
  }
}
