package com.jellyfish85.srcParser.utils

import java.io.{InputStreamReader, BufferedReader, InputStream}
import java.util

class Stream2StringUtils extends Stream2String {}

trait Stream2String {
  var query: String = _
  var ary:   util.ArrayList[String] = _

  def stream2String(inputStream: InputStream): String = {

    try {
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
          buf.append(content)
          buf.append("\n")
        }
      }

      query = buf.toString()

    } catch {
      case e: NullPointerException =>
        e.printStackTrace()

    } finally {
      if (inputStream != null) {
        inputStream.close()
      }
    }

    query
  }

  def stream2StringAry(inputStream: InputStream): util.ArrayList[String] = {
    ary = new util.ArrayList[String]()

    try {
      val reader: BufferedReader =
        new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))

      var switch: Boolean = true
      var content: String = ""
      while (switch) {
        content = reader.readLine()
        if (content.eq(null)) {
          switch = false

        } else {
          ary.add(content)
        }
      }

    } catch {
      case e: NullPointerException =>
        e.printStackTrace()

    } finally {
      if (inputStream != null) {
        inputStream.close()
      }
    }

    ary
  }

}