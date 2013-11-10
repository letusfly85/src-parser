package com.jellyfish85.srcParser.converter

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.{RsSqlTextBean, RsSqlTablesBean}

class ConvRsSqlTextBean2RsSqlTablesBean {

  def convert(bean: RsSqlTextBean): RsSqlTablesBean = {
    val result: RsSqlTablesBean = new RsSqlTablesBean

    result.authorAttr.setValue(bean.authorAttr.value)
    result.commitHmsAttr.setValue(bean.commitHmsAttr.value)
    result.commitYmdAttr.setValue(bean.commitYmdAttr.value)
    result.extensionAttr.setValue(bean.extensionAttr.value)
    result.fileNameAttr.setValue(bean.fileNameAttr.value)
    result.headRevisionAttr.setValue(bean.headRevisionAttr.value)
    result.pathAttr.setValue(bean.pathAttr.value)
    result.persisterNameAttr.setValue(bean.persisterNameAttr.value)
    result.projectNameAttr.setValue(bean.projectNameAttr.value)
    result.revisionAttr.setValue(bean.revisionAttr.value)

    return result
  }

  def convert(list: List[RsSqlTextBean]): List[RsSqlTablesBean] = {
    var resultSets: List[RsSqlTablesBean] = List()

    list.foreach {bean: RsSqlTextBean =>
      resultSets ::= convert(bean)
    }

    resultSets
  }
}
