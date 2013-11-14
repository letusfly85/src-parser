package com.jellyfish85.srcParser.eraser

class SqlRegexUtils {

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

        if (line.toUpperCase() =~ /([\\+]+)/) {
            flg = true

        } else if (line.toUpperCase() =~  /([\s]+)-\s/) {
            flg = true

        } else if (line.toUpperCase() =~ /([\s|\t]+)(WHEN|THEN|ELSE|END|\(|\))([\s+])/) {
            flg = true

        } else if (line.toUpperCase() =~ /([\s|\t]+)(WHEN|THEN|ELSE|END|\(|\))$/) {
            flg = true

        } else if (line.toUpperCase() =~ /([\s]+)-\t/) {
            flg = true


        } else if (line.toUpperCase() =~ /([\s]+)--/) {
            flg = true

        } else if (line.toUpperCase() =~ /^([\s|\t]+)\|\|/) {
            flg = true

        } else if (line.toUpperCase() =~ /\|\|([\s|\t]+)$/) {
            flg = true

        } else {
        }

        return flg
    }

    /**
     * == isMergeOpeMatched ==
     *
     * @param line
     * @return
     */
    public static Boolean isMergeOpeMatched(String line) {

        if (line.toUpperCase() =~ /([\s|\t]+)(WHEN)([\s|\t])(MATCHED)/) {
            return true

        } else {
            return false
        }

    }

}
