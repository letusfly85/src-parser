package com.jellyfish85.srcParser.helper

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
    public String getProjectName(ArrayList<String> targetProjectNames, String path) {
        def result = ""
        targetProjectNames.each {String projectName ->
            String reg = ".*" + projectName.replace("/", "\\/") + ".*"
            if (path.matches(reg)) {
                result =  projectName
                return projectName
            }
        }

        return result
    }
}
