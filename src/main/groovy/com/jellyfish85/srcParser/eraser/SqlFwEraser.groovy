package com.jellyfish85.srcParser.eraser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlCdataBean

class SqlFwEraser {

    public String getErasedSqlText(ArrayList<RsSqlCdataBean> list) {
        def result = ""

        list.each {RsSqlCdataBean bean ->
            def line = bean.textAttr().value()
            //println(line)

            result += replaceFrameworkVariant(line)
        }

        return result
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

        result = result.replace("null", "NULL")

        result = result.replaceFirst(/--%([a-z]+)/, "/* fw_flg */")

        result = result.replaceAll(/\$:_([A-Za-z0-9.\_-]+)\$/,  ":variance")
        result = result.replaceAll(/\$([A-Za-z0-9.\_-]+)\$/,    ":variance")
        result = result.replaceAll(/:_a([a-z]{1})/,             ":variance")

        return (result + "\n")
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
