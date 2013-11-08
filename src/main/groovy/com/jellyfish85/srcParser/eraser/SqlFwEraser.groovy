package com.jellyfish85.srcParser.eraser

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlCdataBean
import com.jellyfish85.srcParser.bean.SqlText

class SqlFwEraser {

    public String getErasedSqlText(ArrayList<RsSqlCdataBean> list) {
        def result = ""

        list.each {RsSqlCdataBean bean ->
            def line = bean.textAttr().value()
            //println(line)

            result += replaceFrameworkVariant(line)

        }

        result = replaceFrameworkOperator(result)
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
        result = result.replaceAll(/\$([A-Za-z0-9.\_-]+)/,      ":variance")
        result = result.replaceAll(/\$\{([A-Za-z0-9.\_-]+)\}/,  ":variance")
        result = result.replaceAll(/:_a([a-z]{1})/,             ":variance")
        result = result.replaceAll(/:_([A-Za-z\_]+)/,           ":variance")

        return (result + "\n")
    }

    public static String replaceFrameworkOperator(String query) {
        def result = ""

        def firstSqlText  = new SqlText()
        def secondSqlText = new SqlText()
        def thirdSqlText  = new SqlText()

        def cursor = 0
        query.split("\n").each {line ->

            firstSqlText.setCursor(cursor)
            firstSqlText.setLine(line)
            firstSqlText.setFwFlg(false)
            firstSqlText.setIsWhereStartLine(false)

            if (line.toUpperCase() =~ /.*SELECT.*/) {
                firstSqlText.setInSelectStartLine(true)
                firstSqlText.countUpSelectOpe()
            }

            if (line.toUpperCase() =~ /.*FROM.*/) {
                if (firstSqlText.countUpSelectOpe() == 0) {
                    firstSqlText.setInSelectEndLine(true)
                }
            }

            if (line.toUpperCase() =~ /.*WHERE.*/) {
                firstSqlText.setIsWhereStartLine(true)
                firstSqlText.setIsInWhereScope(true)
            }

            if (line.toUpperCase() =~ /.*ORDER.*/) {
                firstSqlText.setIsInWhereScope(false)
                firstSqlText.setIsWhereEndLine(false)
            }

            if (line.matches(/.*\/\* fw_flg \*\/.*/)) {
                firstSqlText.setFwFlg(true)
            }

            cursor+=1

            thirdSqlText  = secondSqlText
            secondSqlText = firstSqlText

            if (secondSqlText.fwFlg()) {


            } else {

                if (secondSqlText.isInWhereScope()){
                    secondSqlText.countUpWhereOpe()
                    println(secondSqlText.whereOpeCounter())
                }

                if (secondSqlText.isInWhereScope()       &&
                    secondSqlText.whereOpeCounter() >  2 &&
                    secondSqlText.whereOpeCounter() <= 3
                ){

                    if (!secondSqlText.line().matches(/.*AND.*/) &&
                        !secondSqlText.line().matches(/.*WHERE.*/)) {

                        println((String)secondSqlText.whereOpeCounter() + "\tAND\t" + secondSqlText.line() + "\n")
                        result += "\tAND\t" + secondSqlText.line() + "\n"

                    } else {
                        result += secondSqlText.line() + "\n"
                    }

                } else {
                    result += secondSqlText.line() + "\n"
                }
            }

        }
        println("==== " + result)
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
