package com.jellyfish85.srcParser.helper

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlCdataBean

class SqlCdata2SqlTextHelper {

    public static String cdata2Text(ArrayList<RsSqlCdataBean> list) {
        def query = ""
        list.each {RsSqlCdataBean bean -> query += bean.textAttr().value()}

        return query
    }

}
