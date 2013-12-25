package com.jellyfish85.srcParser.runner

import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsPageActionIdxDao
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSvnSrcInfoDao
import com.jellyfish85.dbaccessor.manager.DatabaseManager
import com.jellyfish85.srcParser.converter.ConvRsSvnSrcInfoBean2SVNRequestBean
import com.jellyfish85.srcParser.utils.SrcParserProp

import com.jellyfish85.srcParser.downloader.DownloadSource2Workspace
import com.jellyfish85.srcParser.parser.PageActionParser

class PageActionsRunner {

    public static void main(String[] args) {
        def db = new DatabaseManager()
        db.connect()

        def app    = new SrcParserProp()
        def dl     = new DownloadSource2Workspace()
        def parser = new PageActionParser()

        def register = new RsPageActionIdxDao()
        def dao  = new RsSvnSrcInfoDao()
        def list = dao.findByExtension(db.conn(), app.page())

        def converter = new ConvRsSvnSrcInfoBean2SVNRequestBean()
        def svnList   = dao.convert(list)
        def requestList = dao.convert(converter.convert(list))


        String mode = args[0]
        if (mode == "all") {
          //download all page files and parse them
          dl.downLoadAll(app, requestList, false)

          //TODO avoid include JavaScript function source.
          def resultSets = parser.parse(app, svnList)

          // insert parse results to database
          register.deleteAll(db.conn())
          register.insert(db.conn(), resultSets)

        } else {
            //download only diff files and parse them
        }

        if (!db.conn().closed) {
            db.jCommit()
            db.jClose()
        }
    }
}