package com.jellyfish85.srcParser.runner

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSubjectidBlpathIdxBean
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


        def dao  = new RsSvnSrcInfoDao()
        def dl   = new DownloadSource2Workspace()
        def converter = new ConvRsSvnSrcInfoBean2SVNRequestBean()

        def list = dao.findByExtension(conn, app.al())
        def _list     = dao.convert(converter.convert(list))
        dl.downLoadAll(app, _list, false)

        def parser = new BlSubjectIdParser()
        def sets = parser.parse(app, _list)
        def _sets = new ArrayList<RsSubjectidBlpathIdxBean>()

        sets.each {RsSubjectidBlpathIdxBean bean ->
            if (bean.subjectGroupIdAttr().value() != null && bean.subjectGroupIdAttr().value() != "") {
                _sets.add(bean)
            } else {
                println("[WARN]" + bean.pathAttr().value())
            }
        }

        def register = new RsSubjectidBlpathIdxDao()
        register.deleteAll(conn)
        register.insert(conn, _sets)

        _context.databaseFinalize()
    }
}