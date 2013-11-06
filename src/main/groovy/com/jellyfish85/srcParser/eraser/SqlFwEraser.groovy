package com.jellyfish85.srcParser.eraser

class SqlFwEraser {

    public static String getErasedSqlText(String sqlText) {

        if (sqlText =~ /abc/) {
            return sqlText

        } else {
            return "abc"
        }
    }

    public static void main(String[] args) {

        println(getErasedSqlText("abcdefg"))

    }

}
