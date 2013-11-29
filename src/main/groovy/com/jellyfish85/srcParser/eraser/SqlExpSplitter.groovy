package com.jellyfish85.srcParser.eraser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlCdataBean
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTextExpBean
import com.jellyfish85.srcParser.helper.SqlRegexHelper

class SqlExpSplitter extends SqlRegexHelper  {

    public ArrayList<RsSqlTextExpBean> split(ArrayList<RsSqlCdataBean> list) {
        def resultSets = new ArrayList<RsSqlTextExpBean>()

        def _switch   = false
        def _scratch  = false
        def _mergeOpe = false

        def num = 0
        def idx = 0
        list.each {RsSqlCdataBean bean ->
          def text = bean.textAttr().value()

          if (isMergeOpeMatched(text)) {
              _mergeOpe = true
          }

          if (text.toUpperCase() =~ /.*([\s|\t]*)INSERT([\s|\t]+).*/ ||
              text.toUpperCase() =~ /.*([\s|\t]*)MERGE([\s|\t]+).*/  ||
              text.toUpperCase() =~ /.*([\s|\t]*)TRUNCATE([\s|\t]+).*/) {

              if (!_mergeOpe) {
                _switch  = true
                num += 1
                idx = 0
              }

          } else if (_switch && text =~ /.*;.*/) {
              _switch  = false
              _scratch = true
          }

          if (_switch || _scratch){
              _scratch = false
              idx += 1
              def entry = new RsSqlTextExpBean()

              def _text = text.replaceAll(/([\s|\t]+)(--).*/, "")
              _text     = _text.replaceAll(/(\/\*).*(\*\/)/, "")
              _text     = _text.replaceAll(/\/\*([\s|\t]{1}).*([\s|\t]+)\/\*.*/, "")
              _text     = _text.replaceAll(/([^\x01-\x7E]+)/, "var")
              _text     = _text.replace(";" ,"")

              entry.authorAttr().setValue(bean.authorAttr().value())
              entry.pathAttr().setValue(bean.pathAttr().value())
              entry.fileNameAttr().setValue(bean.fileNameAttr().value())
              entry.lineAttr().setValue(new BigDecimal(idx))
              entry.textAttr().setValue(_text)
              entry.headRevisionAttr().setValue(new BigDecimal(bean.headRevisionAttr().value()))
              entry.revisionAttr().setValue(new BigDecimal(bean.revisionAttr().value()))
              entry.commitYmdAttr().setValue(bean.commitYmdAttr().value())
              entry.commitHmsAttr().setValue(bean.commitHmsAttr().value())
              entry.extensionAttr().setValue(bean.extensionAttr().value())
              entry.subLineAttr().setValue(new BigDecimal(num))

              resultSets.add(entry)
          }

        }

        return resultSets
    }
}
