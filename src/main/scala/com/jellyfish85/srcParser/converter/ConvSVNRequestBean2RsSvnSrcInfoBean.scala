package com.jellyfish85.srcParser.converter

import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.dbaccessor.src.mainte.tool.RsSvnSrcInfoBean

import java.math.BigDecimal
import org.apache.commons.io.FilenameUtils

class ConvSVNRequestBean2RsSvnSrcInfoBean {

  def convert(bean: SVNRequestBean, projectName: String): RsSvnSrcInfoBean = {
    val entry: RsSvnSrcInfoBean = new RsSvnSrcInfoBean

    entry.pathAttr.value         = bean.path
    entry.fileNameAttr.value     = bean.fileName
    entry.headRevisionAttr.value = new BigDecimal(bean.headRevision)
    entry.revisionAttr.value     = new BigDecimal(bean.revision)

    entry.projectNameAttr.value  = projectName
    entry.extensionAttr.value    = FilenameUtils.getExtension(entry.fileNameAttr.value)

    entry.authorAttr.value       = bean.author
    entry.commitYmdAttr.value    = bean.commitYmd
    entry.commitHmsAttr.value    = bean.commitHms

    entry
  }

  def convert(targetList: List[SVNRequestBean], projectName: String): List[RsSvnSrcInfoBean] = {
    var resultSets: List[RsSvnSrcInfoBean] = List()

    targetList.foreach {bean: SVNRequestBean =>

      try {
        val entry: RsSvnSrcInfoBean = convert(bean, projectName)

        resultSets ::= entry

      } catch {
        case e: Exception =>
          println(bean.path)
          e.printStackTrace()
      }
    }

    resultSets
  }
}
