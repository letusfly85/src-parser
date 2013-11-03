package com.jellyfish85.srcParser.converter

import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.dbaccessor.src.mainte.tool.RsSvnSrcInfoBean

import java.math.BigDecimal
import org.apache.commons.io.{FileUtils, FilenameUtils}

class ConvSVNRequestBean2RsSvnSrcInfoBean {

  def convert(targetList: List[SVNRequestBean], projectName: String): List[RsSvnSrcInfoBean] = {
    var resultSets: List[RsSvnSrcInfoBean] = List()

    targetList.foreach {bean: SVNRequestBean =>

      val entry: RsSvnSrcInfoBean = new RsSvnSrcInfoBean

      entry.pathAttr.value         = bean.path
      entry.fileNameAttr.value     = bean.fileName
      entry.headRevisionAttr.value = bean.headRevision.asInstanceOf[BigDecimal]
      entry.revisionAttr.value     = bean.revision.asInstanceOf[BigDecimal]

      entry.projectNameAttr.value  = projectName
      entry.extensionAttr.value    = FilenameUtils.getExtension(entry.fileNameAttr.value)

      resultSets ::= entry
    }

    resultSets
  }

}
