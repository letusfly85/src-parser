package com.jellyfish85.srcParser.utils

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTextBean

trait QueryBuilder {

  var query: String = _

  def queryBuild(list: List[RsSqlTextBean]): String =  {
    query = ""

    list.foreach {bean: RsSqlTextBean => query += bean.textAttr.value + "\n"}

    query
  }

}
