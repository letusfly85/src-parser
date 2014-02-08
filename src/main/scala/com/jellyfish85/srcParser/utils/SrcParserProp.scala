package com.jellyfish85.srcParser.utils

import org.apache.commons.configuration.{PropertiesConfiguration, Configuration}
import java.io.{InputStreamReader, BufferedReader, InputStream}
import java.util

class SrcParserProp {

  val configuration: Configuration =
    new PropertiesConfiguration("workspace.properties")

  
  val workspace:  String = configuration.getString("workspace")

  val trunk:      String = configuration.getString("trunk")

  val src:        String = configuration.getString("src")  

  val app:        String = configuration.getString("app")  

  val ap :        String = configuration.getString("app")  

  val page:       String = configuration.getString("page")

  val uql:        String = configuration.getString("uql")

  val al:         String = configuration.getString("al")

  val boj:        String = configuration.getString("boj")

  val sql:        String = configuration.getString("sql")

  val expSqlPath: String = configuration.getString("expSqlPath")

  val dtdPath:    String = configuration.getString("dtdPath")

  val output:     String = configuration.getString("output")

  val outputProp: String = configuration.getString("outputProp")

  val bpp:        String = configuration.getString("bpp")

  val logicPath:  String = configuration.getString("logicPath")


  /**
   *
   * @since  2013/11/02
   * @author wada.shunsuke
   * @return project names
   */
  def targetProjectNames: List[String] = {
    var list: List[String] = List()

    var inputStream: InputStream = null
    try {
      inputStream = getClass().getResourceAsStream("/projectName.lst")

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

    } finally {
      if (inputStream != null) inputStream.close()
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


  /*******************************************************************
   *
   *
   *
   *
   *
   *******************************************************************/
  val parserConfiguration: Configuration =
    new PropertiesConfiguration("properties/src-parser.properties")

  val subversionBaseUrl: String =
              parserConfiguration.getString("subversion.url.base")

  val subversionBranchProduct: String =
              parserConfiguration.getString("subversion.branch.product")

  val subversionBranchDevelop: String =
              parserConfiguration.getString("subversion.branch.develop")

  val subversionRepositoryName: String =
              parserConfiguration.getString("subversion.repository.name")
}
