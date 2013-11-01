package com.jellyfish85.srcParser.utils

import java.util.Properties
import java.io.{IOException, InputStreamReader, FileInputStream}

object ApplicationProperties {

  val property: Properties = new Properties()

  def load {
    property.load(getClass().getResourceAsStream("/workspace.properties"))
  }

  def trunk: String = {load; property.getProperty("trunk")}

  def src:   String = {load; property.getProperty("src")  }

  def targetProjectNames: List[String] = {
    var list: List[String] = List()

    try {
      val is: FileInputStream   = (getClass().getResourceAsStream("/projectName.lst")).asInstanceOf[FileInputStream]
      val in: InputStreamReader = new InputStreamReader(is, "UTF8")
      var ch: Int = 0
      while ((ch = in.read()) != -1) {
        list ::= Integer.toHexString(ch)
      }
      in.close()
    } catch  {
      case e:IOException =>
        e.printStackTrace()
    }

    list
  }
}



