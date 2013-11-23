package com.jellyfish85.srcParser.helper

import com.jellyfish85.srcParser.utils.ApplicationProperties

/**
 * == ProjectNameHelper ==
 *
 * @author wada shunsuke
 * @since  2013/11/23
 *
 */
class ProjectNameHelper {

    /**
     * == getProjectName ==
     *
     *
     * @param app
     * @param path
     * @return
     */
    public String getProjectName(ApplicationProperties app, String path) {
        def targetProjectNames = app._targetProjectNames()

        def result = ""
        targetProjectNames.each {String projectName ->
            def reg = ".*" + projectName + ".*"
            if (path =~ /${reg}/) {
                result = projectName
            }
        }

        return result
    }
}
