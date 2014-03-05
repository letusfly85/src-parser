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
import java.sql.SQLException

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
        ConvVChangesetsBean2TrCommitHistoryBean converter = new ConvVChangesetsBean2TrCommitHistoryBean()
        ArrayList<TrCommitHistoryBean> historyBeans = new ArrayList<>()

        TrCommitHistoryDao historyDao = new TrCommitHistoryDao()
        db.connect()
        historyDao.deleteAll(db.conn())
        db.jCommit()


        String productUrl = this.srcProp.subversionBaseUrl() + this.srcProp.subversionBranchProduct()

        Long   productRevision = manager.getRepository(productUrl).getLatestRevision()

        String developUrl = this.srcProp.subversionBaseUrl() + this.srcProp.subversionBranchDevelop()

        Long   developRevision = manager.getRepository(developUrl).getLatestRevision()

        setUrl1(productUrl)
        setRevision1(productRevision)

        setUrl2(developUrl)
        setRevision2(developRevision)

        ArrayList<SVNDiffBean> rightAry      = getRightDiffs()
        ArrayList<VChangesetsBean> _rightAry = new ArrayList<>()
        rightAry.each {bean ->
            try {
                BigDecimal _revision = null
                    if (bean.revision() == null) {
                        _revision = new BigDecimal(bean.headRevision())

                    } else {
                        _revision = new BigDecimal(bean.revision())
                }
                _rightAry.add(dao.findUnique(conn, _revision, bean.path()))

            } catch(SQLException e) {
                VChangesetsBean _bean = new VChangesetsBean()
                _bean.actionAttr().setValue("N")
                _bean.pathAttr().setValue(bean.path())
                _bean.fileNameAttr().setValue(bean.fileName())
                _rightAry.add(_bean)

                println("[WARN]cannot get info: " + bean.path())
            }
        }

        historyBeans = converter.convert(_rightAry, developUrl, productUrl)
        historyDao.insert(db.conn(), historyBeans)
        db.jCommit()

        ArrayList<SVNDiffBean> leftAry       = getLeftDiffs()
        ArrayList<VChangesetsBean> _leftAry  = new ArrayList<>()
        leftAry.each {bean ->
            try {
                BigDecimal _revision = null
                if (bean.revision() == null) {
                    _revision = new BigDecimal(bean.headRevision())

                } else {
                    _revision = new BigDecimal(bean.revision())
                }
                _leftAry.add(dao.findUnique(conn, _revision, bean.path()))

            } catch(SQLException e) {
                VChangesetsBean _bean = new VChangesetsBean()
                _bean.actionAttr().setValue("N")
                _bean.pathAttr().setValue(bean.path())
                _bean.fileNameAttr().setValue(bean.fileName())
                _leftAry.add(_bean)

                println("[WARN]cannot get info: " + bean.path())
            }
        }

        historyBeans = converter.convert(_leftAry, productUrl, developUrl)
        historyDao.insert(db.conn(), historyBeans)
        db.jCommit()

    }
}