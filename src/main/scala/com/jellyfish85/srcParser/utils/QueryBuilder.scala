package com.jellyfish85.srcParser.utils

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTextBeanTrait

trait QueryBuilder[A <: RsSqlTextBeanTrait] {

  var query: String = _

  def queryBuild(list: List[A]): String =  {
    query = ""

    list.foreach {bean: A => query += bean.textAttr.value + "\n"}

    query
  }

}
