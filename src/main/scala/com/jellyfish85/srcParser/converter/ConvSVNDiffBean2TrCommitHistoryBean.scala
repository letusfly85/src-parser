package com.jellyfish85.srcParser.converter

import com.jellyfish85.svnaccessor.bean.SVNDiffBean
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.TrCommitHistoryBean
import com.jellyfish85.srcParser.utils.SrcParserProp
import java.util

import java.math.BigDecimal

class ConvSVNDiffBean2TrCommitHistoryBean {

  val srcProp: SrcParserProp = new SrcParserProp

  def convert(bean: SVNDiffBean, url1: String, url2: String): TrCommitHistoryBean = {
    val historyBean: TrCommitHistoryBean = new TrCommitHistoryBean

    historyBean.repositoryKindAttr.setValue("subversion")
    historyBean.repositoryNameAttr.setValue(srcProp.subversionRepositoryName)
    historyBean.rootUrlAttr.setValue(srcProp.subversionBaseUrl)
    historyBean.leftBaseUrlAttr.setValue(url1)
    historyBean.rightBaseUrlAttr.setValue(url2)
    historyBean.revisionAttr.setValue(new BigDecimal(bean.revision))
    //historyBean.committerAttr.setValue(bean.comm)
    historyBean.commentsAttr.setValue(bean.comment)
    historyBean.actionAttr.setValue(bean.modificationType.toString)
    historyBean.pathAttr.setValue(bean.path)
    historyBean.fileNameAttr.setValue(bean.fileName)
    historyBean.commitYmdAttr.setValue(bean.commitYmd)
    historyBean.commitHmsAttr.setValue(bean.commitHms)

    historyBean
  }

  def convert(list: util.ArrayList[SVNDiffBean], url1: String, url2: String): util.ArrayList[TrCommitHistoryBean] = {
    val results: util.ArrayList[TrCommitHistoryBean] = new util.ArrayList[TrCommitHistoryBean]()

    for (i <- 0 until list.size()) {
      val bean = convert(list.get(i), url1, url2)
      results.add(bean)
    }

    results
  }

}
