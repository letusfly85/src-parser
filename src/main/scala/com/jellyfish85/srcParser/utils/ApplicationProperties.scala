package com.jellyfish85.srcParser.utils

import java.util.Properties
import java.io._
import java.util

object ApplicationProperties {

  val property: Properties = new Properties()

  def load = property.load(getClass().getResourceAsStream("/workspace.properties"))

  def workspace:  String = {load; property.getProperty("workspace")}

  def trunk:      String = {load; property.getProperty("trunk")}

  def src:        String = {load; property.getProperty("src")  }

  def app:        String = {load; property.getProperty("app")  }

  def ap :        String = {load; property.getProperty("app")  }

  def page:       String = {load; property.getProperty("page")}

  def uql:        String = {load; property.getProperty("uql")}

  def al:         String = {load; property.getProperty("al")}

  def boj:        String = {load; property.getProperty("boj")}

  def sql:        String = {load; property.getProperty("sql")}

  def expSqlPath: String = {load; property.getProperty("expSqlPath")}

  def dtdPath:    String = {load; property.getProperty("dtdPath")}

  def output:     String = {load; property.getProperty("output")}

  def outputProp: String = {load; property.getProperty("outputProp")}

  def bpp:        String = {load; property.getProperty("bpp")}

  def logicPath:  String = {load; property.getProperty("logicPath")}


  /**
   *
   * @since  2013/11/02
   * @author wada.shunsuke
   * @return project names
   */
  def targetProjectNames: List[String] = {
    var list: List[String] = List()

    try {
      val inputStream: InputStream = getClass().getResourceAsStream("/projectName.lst")

      val reader: BufferedReader =
        new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))

      var switch: Boolean = true
      var content: String = ""
      while (switch) {
        content = reader.readLine()
        if (content.eq(null)) {
          switch = false

        } else {
          list ::= content
        }
      }
    }

    list
  }

  def _targetProjectNames: util.ArrayList[String] = {
    val list: util.ArrayList[String] = new util.ArrayList[String]()
    targetProjectNames.foreach {projectName: String => list.add(projectName)}
    list
  }

  def configFileNames: List[String] = {
    var list: List[String] = List()

    try {
      val inputStream: InputStream = getClass().getResourceAsStream("/configFileName.lst")

      val reader: BufferedReader =
        new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))

      var switch: Boolean = true
      var content: String = ""
      while (switch) {
        content = reader.readLine()
        if (content.eq(null)) {
          switch = false

        } else {
          list ::= content
        }
      }
    }

    list
  }
}

class ApplicationProperties {
  val app = ApplicationProperties
}



