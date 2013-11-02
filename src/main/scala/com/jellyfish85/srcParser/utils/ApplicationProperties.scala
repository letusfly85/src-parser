package com.jellyfish85.srcParser.utils

import java.util.Properties
import java.io._

object ApplicationProperties {

  val property: Properties = new Properties()

  def load {
    property.load(getClass().getResourceAsStream("/workspace.properties"))
  }

  def trunk: String = {load; property.getProperty("trunk")}

  def src:   String = {load; property.getProperty("src")  }

  def app:   String = {load; property.getProperty("app")  }

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

      val buf: StringBuffer = new StringBuffer()
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



