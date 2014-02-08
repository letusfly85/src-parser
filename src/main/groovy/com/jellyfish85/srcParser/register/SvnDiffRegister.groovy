package com.jellyfish85.srcParser.register

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.TrCommitHistoryBean
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.VChangesetsBean
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.TrCommitHistoryDao
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.VChangesetsDao
import com.jellyfish85.dbaccessor.manager.DatabaseManager
import com.jellyfish85.srcParser.BaseContext
import com.jellyfish85.srcParser.converter.ConvVChangesetsBean2TrCommitHistoryBean
import com.jellyfish85.srcParser.utils.SrcParserProp
import com.jellyfish85.svnaccessor.bean.SVNDiffBean
import com.jellyfish85.svnaccessor.getter.SVNDiffGetter
import com.jellyfish85.svnaccessor.manager.SVNManager

import java.sql.Connection

class SvnDiffRegister {

    private String url1      = ""

    private String url2      = ""

    private Long   revision1 = null

    private Long   revision2 = null

    private SVNManager    manager = null

    private SVNDiffGetter getter  = null

    private SrcParserProp srcProp = null

    private VChangesetsDao dao    = new VChangesetsDao()

    private DatabaseManager db    = new DatabaseManager()

    private Connection      conn  = null

    public SvnDiffRegister(BaseContext _context) {
        manager = new SVNManager()

        getter  = new SVNDiffGetter()

        srcProp = new SrcParserProp()

        db.myConnect()
        conn = db.conn()
    }

    public void setUrl1(String _url1) {
        this.url1 = _url1
    }

    public void setUrl2(String _url2) {
        this.url2 = _url2
    }

    public void setRevision1(Long _revision1) {
        this.revision1 = _revision1
    }

    public void setRevision2(Long _revision2) {
        this.revision2 = _revision2
    }

    public ArrayList<SVNDiffBean> getLeftDiffs() {
        def ary = getter._getDiffSummary(this.url1, this.revision1, this.url2, this.revision2)

        return ary
    }

    public ArrayList<SVNDiffBean> getRightDiffs() {
        def ary = getter._getDiffSummary(this.url2, this.revision2, this.url1, this.revision1)

        return ary
    }

    public void register() {
        String productUrl = this.srcProp.subversionBaseUrl() + this.srcProp.subversionBranchProduct()

        Long   productRevision = manager.getRepository(productUrl).getLatestRevision()

        String developUrl = this.srcProp.subversionBaseUrl() + this.srcProp.subversionBranchDevelop()

        Long   developRevision = manager.getRepository(developUrl).getLatestRevision()

        setUrl1(productUrl)
        setRevision1(productRevision)

        setUrl2(developUrl)
        setRevision2(developRevision)

        ArrayList<SVNDiffBean> rightAry  = getRightDiffs()
        ArrayList<VChangesetsBean> _rightAry = new ArrayList<>()
        rightAry.each {bean ->
            println(bean.path() + "\t" + bean.revision().toString())
            bean.setPath(bean.path().replace(srcProp.subversionBaseUrl(), ""))
            _rightAry.add(dao.findUnique(conn, new BigDecimal(bean.revision()), bean.path()))
        }

        _rightAry.each {VChangesetsBean bean ->
            println(bean.fileNameAttr().value() + "\t" + bean.revisionAttr().value().toString() +
                    bean.commentsAttr().value() + "\t" + bean.commentsAttr().value()
                   )
        }

        ConvVChangesetsBean2TrCommitHistoryBean converter = new ConvVChangesetsBean2TrCommitHistoryBean()
        ArrayList<TrCommitHistoryBean> historyBeans = converter.convert(_rightAry, developUrl, productUrl)

        TrCommitHistoryDao historyDao = new TrCommitHistoryDao()
        db.connect()
        historyDao.insert(db.conn(), historyBeans)
        db.jCommit()

        //System.exit(0)

        /*
        ArrayList<SVNDiffBean> leftAry   = getLeftDiffs()
        ArrayList<SVNDiffBean> _leftAry  = getLeftDiffs()
        leftAry.each {bean ->
            println(bean.path() + "\t" + bean.revision().toString())
            bean.setPath(bean.path().replace(srcProp.subversionBaseUrl(), ""))
            _leftAry.add(dao.findUnique(conn, new BigDecimal(bean.revision()), bean.path()))
        }

        _leftAry.each {VChangesetsBean bean ->
            println(bean.fileNameAttr().value() + "\t" + bean.revisionAttr().value().toString() +
                    bean.commentsAttr().value() + "\t" + bean.commentsAttr().value()
            )
        }
        */

    }


}
