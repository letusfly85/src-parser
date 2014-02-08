package com.jellyfish85.srcParser.converter

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.{VChangesetsBean, TrCommitHistoryBean}
import com.jellyfish85.srcParser.utils.SrcParserProp
import java.util

import java.math.BigDecimal

class ConvVChangesetsBean2TrCommitHistoryBean {

  val srcProp: SrcParserProp = new SrcParserProp

  def convert(bean: VChangesetsBean, url1: String, url2: String): TrCommitHistoryBean = {
    val historyBean: TrCommitHistoryBean = new TrCommitHistoryBean

    historyBean.repositoryKindAttr.setValue("subversion")
    historyBean.repositoryNameAttr.setValue(srcProp.subversionRepositoryName)
    historyBean.rootUrlAttr.setValue(srcProp.subversionBaseUrl)
    historyBean.leftBaseUrlAttr.setValue(url1.replace(srcProp.subversionBaseUrl, ""))
    historyBean.rightBaseUrlAttr.setValue(url2.replace(srcProp.subversionBaseUrl, ""))
    historyBean.revisionAttr.setValue(bean.revisionAttr.value)
    historyBean.committerAttr.setValue(bean.committerAttr.value)
    historyBean.commentsAttr.setValue(bean.commentsAttr.value)
    historyBean.actionAttr.setValue(bean.actionAttr.value)
    historyBean.pathAttr.setValue(bean.pathAttr.value)
    historyBean.fileNameAttr.setValue(bean.fileNameAttr.value)
    historyBean.commitYmdAttr.setValue(bean.commitDateAttr.value)
    historyBean.commitHmsAttr.setValue(bean.commitHmsAttr.value)

    historyBean
  }

  def convert(list: util.ArrayList[VChangesetsBean], url1: String, url2: String): util.ArrayList[TrCommitHistoryBean] = {
    val results: util.ArrayList[TrCommitHistoryBean] = new util.ArrayList[TrCommitHistoryBean]()

    for (i <- 0 until list.size()) {
      val bean = convert(list.get(i), url1, url2)
      results.add(bean)
    }

    results
  }

}
