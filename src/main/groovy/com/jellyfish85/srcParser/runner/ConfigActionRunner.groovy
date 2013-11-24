package com.jellyfish85.srcParser.runner

import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsConfigAttributesDao
import com.jellyfish85.dbaccessor.manager.DatabaseManager
import com.jellyfish85.srcParser.utils.ApplicationProperties

import com.jellyfish85.srcParser.downloader.DownloadSource2Workspace
import com.jellyfish85.srcParser.parser.ConfigActionParser
import com.jellyfish85.svnaccessor.bean.SVNRequestBean
import com.jellyfish85.svnaccessor.getter.SVNGetFiles
import org.apache.commons.io.FilenameUtils

class ConfigActionRunner {

    public static void main(String[] args) {
        def db = new DatabaseManager()
        db.connect()

        def app    = new ApplicationProperties()
        def dl     = new DownloadSource2Workspace()
        def parser = new ConfigActionParser()

        def register = new RsConfigAttributesDao()
        def list     = getConfigFileNames()

        //download all page files and parse them
        dl.downLoadAll(app, list, false)

        def modifier = new SVNGetFiles()
        def _list = modifier.modifyAttribute2Current(list)

        def resultSets = parser.parse(app, _list)

        // insert parse results to database
        register.deleteAll(db.conn())
        register.insert(db.conn(), resultSets)

        if (!db.conn().closed) {
            db.jCommit()
            db.jClose()
        }
    }

    public static ArrayList<SVNRequestBean> getConfigFileNames() {
        def list = new ArrayList<SVNRequestBean>()

        try {
            def inputStream = getClass().getResourceAsStream("/configFileName.lst")

            def reader =
                new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))

            Boolean _switch = true
            def content = ""
            while (_switch) {
                content = reader.readLine()
                if (content.equals(null)) {
                    _switch = false

                } else {
                    def bean = new SVNRequestBean()
                    bean.path = content
                    bean.fileName = FilenameUtils.getName(content)

                    list.add(bean)
                }
            }

        } catch (Exception e) {
            e.printStackTrace()
        }

        return list
    }

}
