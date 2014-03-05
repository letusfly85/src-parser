package com.jellyfish85.srcParser.generator

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.TrCommitHistoryBean
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.TrCommitHistoryDao
import com.jellyfish85.dbaccessor.manager.DatabaseManager
import com.jellyfish85.srcParser.downloader.DownloadSource2Workspace
import com.jellyfish85.srcParser.helper.CheckSumHelper
import com.jellyfish85.srcParser.utils.SrcParserProp
import com.jellyfish85.svnaccessor.bean.SVNRequestBean

import java.sql.Connection

class SVNDiffCheckSumGenerator {

    private DatabaseManager      manager   = new DatabaseManager()

    private Connection           conn      = null

    private TrCommitHistoryDao   dao       = new TrCommitHistoryDao()

    private CheckSumHelper       helper    = new CheckSumHelper()

    private SrcParserProp        srcProp   = new SrcParserProp()

    public SVNDiffCheckSumGenerator() {
        manager.connect()
        conn = manager.conn()
    }

    public void generate() {
        String developUrl = this.srcProp.subversionBranchDevelop()
        def _beans = dao.findModified(conn, developUrl)
        ArrayList<TrCommitHistoryBean> beans = dao.convert(_beans)

        beans.each {x ->
            println(x.pathAttr().value())
            applyCheckSumDiff(x)
        }

        beans.each {x ->
            if (x.checkSumLeft() != x.checkSumRight()) {
                println(x.pathAttr().value())
            }
        }
        /*
        TrCommitHistoryBean _bean = beans.head()
        applyCheckSumDiff(_bean)
        ArrayList<TrCommitHistoryBean> _b = new ArrayList<>()
        _b.add(_bean)
        dao.updateByRelativePath(conn, _b)
        conn.commit()
        */


        dao.updateByRelativePath(conn, beans)
        conn.commit()
    }

    public void applyCheckSumDiff(TrCommitHistoryBean bean) {
        String path = bean.pathAttr().value()

        String path1 = path.replace(bean.leftBaseUrlAttr().value(), bean.rightBaseUrlAttr().value())
        String path2 = path

        SVNRequestBean svnRequestBean1 = new SVNRequestBean()
        svnRequestBean1.setPath(path1)
        DownloadSource2Workspace.simpleDownload(svnRequestBean1, "branch")

        SVNRequestBean svnRequestBean2 = new SVNRequestBean()
        svnRequestBean2.setPath(path2)
        DownloadSource2Workspace.simpleDownload(svnRequestBean2, "trunk")

        String _path1 = "output/branch" + path1
        String _path2 = "output/trunk"  + path2

        helper.applyCheckSum(bean, _path1, _path2)
    }
}
