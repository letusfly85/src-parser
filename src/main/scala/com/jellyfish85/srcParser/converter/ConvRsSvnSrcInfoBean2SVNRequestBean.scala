package com.jellyfish85.srcParser.converter

import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.dbaccessor.src.mainte.tool.RsSvnSrcInfoBean

class ConvRsSvnSrcInfoBean2SVNRequestBean {

  def convert(bean: RsSvnSrcInfoBean): SVNRequestBean = {
    val entry: SVNRequestBean = new SVNRequestBean

    entry.path                   = bean.pathAttr.value
    entry.fileName               = bean.fileNameAttr.value
    entry.headRevision           = bean.headRevisionAttr.value.longValue()
    entry.revision               = bean.revisionAttr.value.longValue()
    entry.author                 = bean.authorAttr.value
    entry.commitYmd              = bean.commitYmdAttr.value
    entry.commitHms              = bean.commitHmsAttr.value

    entry
  }

  def convert(targetList: List[RsSvnSrcInfoBean]): List[SVNRequestBean] = {
    var resultSets: List[SVNRequestBean] = List()

    targetList.foreach {bean: RsSvnSrcInfoBean =>

      try {
        val entry: SVNRequestBean = convert(bean)

        resultSets ::= entry

      } catch {
        case e: Exception =>
          println(bean.pathAttr.value)
          e.printStackTrace()
      }
    }

    resultSets
  }
}