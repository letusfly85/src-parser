package com.jellyfish85.srcParser.helper

import com.jellyfish85.srcParser.utils.ApplicationProperties
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FilenameUtils

/**
 * == DtdFolderHelper ==
 *
 * @author wada shunsuke
 * @since  2013/11/23
 *
 */
class DtdFolderHelper {

    public static void copyDtdXml(ApplicationProperties app) {

        def constFolder    = FilenameUtils.getName(app.dtdPath())
        def constDirectory = new File(constFolder)

        if (!constDirectory.exists()) {
            FileUtils.forceMkdir(constDirectory)
        }
        FileUtils.cleanDirectory(constDirectory)

        def projectName = app._targetProjectNames().get(0)
        def folder = app.workspace() + app.ap() + projectName + app.dtdPath()
        def dir    = new File(folder)
        dir.listFiles().each {target ->
            FileUtils.copyDirectoryToDirectory(target, constDirectory)
        }

    }

}
