package com.jellyfish85.srcParser.converter

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.{RsSqlTextExpBean, RsSqlTablesExpBean}

class ConvRsSqlTextExpBean2RsSqlTablesExpBean {

  def convert(bean: RsSqlTextExpBean): RsSqlTablesExpBean = {
    val result: RsSqlTablesExpBean = new RsSqlTablesExpBean

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

  def convert(list: List[RsSqlTextExpBean]): List[RsSqlTablesExpBean] = {
    var resultSets: List[RsSqlTablesExpBean] = List()

    list.foreach {bean: RsSqlTextExpBean =>
      resultSets ::= convert(bean)
    }

    resultSets
  }
}
