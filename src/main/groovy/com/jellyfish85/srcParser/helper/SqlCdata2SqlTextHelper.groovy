package com.jellyfish85.srcParser.helper

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlCdataBean
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTextBean

class SqlCdata2SqlTextHelper {

    public static String cdata2Text(ArrayList<RsSqlCdataBean> list) {
        def query = ""
        list.each {RsSqlCdataBean bean -> query += bean.textAttr().value()}

        return query
    }

    public static ArrayList<RsSqlTextBean> query2RsSqlTextBeanList(String query, RsSqlCdataBean bean) {
        def list = new ArrayList<RsSqlTextBean>()
        def idx  = 0

        query.split("\n").each {String text ->
            def entry = new RsSqlTextBean()

            entry.headRevisionAttr().setValue(bean.headRevisionAttr().value())
            entry.projectNameAttr().setValue(bean.projectNameAttr().value())
            entry.fileNameAttr().setValue(bean.fileNameAttr().value())
            entry.persisterNameAttr().setValue(bean.persisterNameAttr().value())
            entry.pathAttr().setValue(bean.pathAttr().value())
            entry.lineAttr().setValue(new BigDecimal(idx))
            entry.textAttr().setValue(text)
            entry.revisionAttr().setValue(bean.revisionAttr().value())
            entry.authorAttr().setValue(bean.authorAttr().value())
            entry.commitYmdAttr().setValue(bean.commitYmdAttr().value())
            entry.commitHmsAttr().setValue(bean.commitHmsAttr().value())
            entry.extensionAttr().setValue(bean.extensionAttr().value())

            list.add(entry)
            idx += 1
        }

        return list
    }

}
