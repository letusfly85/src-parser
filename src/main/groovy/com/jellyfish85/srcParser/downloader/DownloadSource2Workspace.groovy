package com.jellyfish85.srcParser.downloader

import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.svnaccessor.getter.SVNGetFiles
import org.apache.commons.io.FileUtils

public class DownloadSource2Workspace {

    public DownloadSource2Workspace() {}

    /**
     * == downLoadAll ==
     *
     * @return
     */
    public static void downLoadAll(
            ApplicationProperties app,
            ArrayList<SVNRequestBean> requestList
    ) {

        def workspace = new File(app.workspace())
        if (!workspace.exists()) {
            FileUtils.forceMkdir(workspace)
        }

        def getter = new SVNGetFiles()
        getter.simpleGetFilesWithDirectory(requestList, workspace)

    }
}