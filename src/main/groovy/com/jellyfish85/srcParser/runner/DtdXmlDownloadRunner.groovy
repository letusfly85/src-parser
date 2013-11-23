package com.jellyfish85.srcParser.runner

import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSvnSrcInfoDao
import com.jellyfish85.srcParser.converter.ConvRsSvnSrcInfoBean2SVNRequestBean
import com.jellyfish85.srcParser.downloader.DownloadSource2Workspace
import com.jellyfish85.srcParser.helper.DtdFolderHelper

/**
 * == DtdXmlDownloadRunner ==
 *
 * @author wada shunsuke
 * @since  2013/11/23
 *
 */
class DtdXmlDownloadRunner {

    public static void main(String[] args) {
        def _context = new BaseRunner()
        _context.databaseInitialize()

        def conn = _context.getConnection()

        def app  = _context.app

        def dao  = new RsSvnSrcInfoDao()
        def dl   = new DownloadSource2Workspace()
        def converter = new ConvRsSvnSrcInfoBean2SVNRequestBean()


        def dtdList = dao.findByLikePath(conn, "%" + app.dtdPath() + "%")
        def _dtdList = dao.convert(converter.convert(dtdList))
        dl.downLoadAll(app, _dtdList, true)

        def helper = new DtdFolderHelper()
        helper.copyDtdXml(app)

        _context.databaseFinalize()
    }
}
