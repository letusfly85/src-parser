package com.jellyfish85.srcParser.register

import com.jellyfish85.svnaccessor.bean.SVNDiffBean
import com.jellyfish85.svnaccessor.getter.SVNDiffGetter
import com.jellyfish85.svnaccessor.manager.SVNManager

class SvnDiffRegister {

    private String url1      = ""

    private String url2      = ""

    private Long   revision1 = null

    private Long   revision2 = null

    private SVNManager    manager = null

    private SVNDiffGetter getter  = null

    public SvnDiffRegister() {
        manager = new SVNManager()

        getter  = new SVNDiffGetter()
    }

    public void setUrl1(String _url1) {
        this.url1 = _url1
    }

    public void setUrl2(String _url2) {
        this.url2 = _url2
    }

    public void setRevision1(String _revision1) {
        this.revision1 = _revision1
    }

    public void setRevision2(String _revision2) {
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

    }


}
