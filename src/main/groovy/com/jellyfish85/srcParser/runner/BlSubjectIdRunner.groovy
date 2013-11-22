package com.jellyfish85.srcParser.runner

import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSubjectidBlpathIdxDao
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSvnSrcInfoDao
import com.jellyfish85.srcParser.converter.ConvRsSvnSrcInfoBean2SVNRequestBean
import com.jellyfish85.srcParser.downloader.DownloadSource2Workspace
import com.jellyfish85.srcParser.parser.BlSubjectIdParser

/**
 * == BlSubjectIdRunner ==
 *
 * @author wada shunsuke
 * @since  2013/11/22
 *
 */
class BlSubjectIdRunner {

    /**
     * == main ==
     *
     * @author wada shunsuke
     * @since  2013/11/22
     * @param  args
     */
    public static void  main(String[] args) {
        def _context = new BaseRunner()
        _context.databaseInitialize()

        def conn = _context.getConnection()
        def app  = _context.app

        def dl   = new DownloadSource2Workspace()

        def dao  = new RsSvnSrcInfoDao()
        def list = dao.findByExtension(conn, app.al())

        def parser = new BlSubjectIdParser()

        def converter = new ConvRsSvnSrcInfoBean2SVNRequestBean()
        def _list     = dao.convert(converter.convert(list))

        dl.downLoadAll(app, _list)

        //todo
        //def sets = parser.parse(_list)

        def register = new RsSubjectidBlpathIdxDao()


        _context.databaseFinalize()
    }

}