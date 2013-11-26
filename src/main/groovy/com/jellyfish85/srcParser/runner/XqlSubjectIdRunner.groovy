package com.jellyfish85.srcParser.runner

import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSubjectidXqlpathIdxDao
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSvnSrcInfoDao
import com.jellyfish85.srcParser.converter.ConvRsSvnSrcInfoBean2SVNRequestBean
import com.jellyfish85.srcParser.downloader.DownloadSource2Workspace
import com.jellyfish85.srcParser.parser.XqlSubjectIdParser

/**
 * == XqlSubjectIdRunner ==
 *
 *
 * @author wada shunsuke
 * @since  2013/11/26
 *
 */
class XqlSubjectIdRunner {

    public static void main(String[] args) {

        def _context = new BaseRunner()
        _context.databaseInitialize()

        def conn     = _context.connection
        def app      = _context.app

        def dao  = new RsSvnSrcInfoDao()
        def dl   = new DownloadSource2Workspace()
        def converter = new ConvRsSvnSrcInfoBean2SVNRequestBean()

        def list = dao.findByExtension(conn, app.al())
        def _list     = dao.convert(converter.convert(list))
        dl.downLoadAll(app, _list, false)

        def parser = new XqlSubjectIdParser()
        def sets = parser.parse(app, _list)

        def register = new RsSubjectidXqlpathIdxDao()
        register.deleteAll(conn)
        register.insert(conn, sets)

        _context.databaseFinalize()

    }
}
