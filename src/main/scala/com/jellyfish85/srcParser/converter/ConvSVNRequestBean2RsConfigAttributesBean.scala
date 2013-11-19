package com.jellyfish85.srcParser.converter

import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsConfigAttributesBean
import org.apache.commons.io.FilenameUtils

import java.math.BigDecimal

/**
 * == ConvSVNRequestBean2RsConfigAttributesBean ==
 *
 *
 *
 * @author wada shunsuke
 * @since  2013/11/19
 *
 */
class ConvSVNRequestBean2RsConfigAttributesBean {

  def convert(bean: SVNRequestBean): RsConfigAttributesBean = {
    val result: RsConfigAttributesBean = new RsConfigAttributesBean

    result.headRevisionAttr.setValue(new BigDecimal(bean.headRevision))
    result.fileNameAttr.setValue(bean.fileName)
    result.pathAttr.setValue(bean.path)
    result.revisionAttr.setValue(new BigDecimal(bean.revision))
    result.authorAttr.setValue(bean.author)
    result.commitYmdAttr.setValue(bean.commitYmd)
    result.commitHmsAttr.setValue(bean.commitHms)
    result.extensionAttr.setValue(FilenameUtils.getExtension(bean.path))

    return result
  }

}
