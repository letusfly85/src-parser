package com.jellyfish85.srcParser.utils

import java.io.{InputStreamReader, BufferedReader, InputStream}

trait Stream2String {
  var query: String = _

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
      query

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
}
