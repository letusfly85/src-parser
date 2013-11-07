package com.jellyfish85.srcParser.eraser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlCdataBean

class SqlFwEraser {

    public static String getErasedSqlText(ArrayList<RsSqlCdataBean> list , String sqlText) {

        list.each {RsSqlCdataBean bean ->
            def line = bean.textAttr().value()
        }


        if (sqlText =~ /abc/) {
            return sqlText

        } else {
            return "abc"
        }
    }

    public static void main(String[] args) {

        println(getErasedSqlText("abcdefg"))

    }

    /**
     * == replaceFrameworkVariant ==
     *
     * @param line
     * @return
     * @todo
     *
     */
    public static String replaceFrameworkVariant(String line) {
        def result = line

        return result
    }

    /**
     * == isOperator ==
     *
     *
     *
     * @param line
     * @return
     *
     */
    public static Boolean isOperator(String line) {
        def flg = false

        if (/\+/ =~ line) {
            flg = true

        } else if (/([\s]+)-\s/ =~ line) {
            flg = true

        } else if (/([\s|\t]+)(WHEN|THEN|ELSE|END|\(|\))([\s+])/ =~ line.toUpperCase()) {
            flg = true

        } else if (/([\s|\t]+)(WHEN|THEN|ELSE|END|\(|\))$/ =~ line.toUpperCase()) {
            flg = true

        } else if (/([\s]+)-\t/ =~ line) {
            flg = true

        } else if (/^([\s|\t]+)\|\|/ =~ line) {
            flg = true

        } else if (/\|\|([\s|\t]+)$/ =~ line) {
            flg = true

        } else {
        }

        return flg
    }

}
