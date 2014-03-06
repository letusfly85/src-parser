package com.jellyfish85.srcParser.generator

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.TrCommitHistoryBean
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.TrCommitHistoryDao
import com.jellyfish85.dbaccessor.manager.DatabaseManager
import com.jellyfish85.srcParser.utils.SrcParserProp
import groovy.text.SimpleTemplateEngine

import java.sql.Connection

class SVNDiffViewGenerator {

    private DatabaseManager manager = new DatabaseManager()

    private Connection      conn    = null

    private TrCommitHistoryDao  dao = new TrCommitHistoryDao()

    private SrcParserProp srcProp   = new SrcParserProp()

    public SVNDiffViewGenerator() {
        manager.connect()
        conn = manager.conn()
    }

    public void generate() {
        def _beansDiffs = dao.findDiffs(conn, this.srcProp.subversionBranchDevelop())
        ArrayList<TrCommitHistoryBean> beansDiffs = dao.convert(_beansDiffs)
        beansDiffs.collect {x ->
            x.pathAttr().setValue(x.pathAttr().value().replace(("/" + srcProp.unusedName() + "/"), ""))
        }

        def _beansLeftNotExists = dao.findNotExists(conn, this.srcProp.subversionBranchDevelop())
        ArrayList<TrCommitHistoryBean> beansLeftNotExists = dao.convert(_beansLeftNotExists)
        beansLeftNotExists.collect {x ->
            x.pathAttr().setValue(x.pathAttr().value().replace(("/" + srcProp.unusedName() + "/"), ""))
        }

        def _beansRightNotExists = dao.findNotExists(conn, this.srcProp.subversionBranchProduct())
        ArrayList<TrCommitHistoryBean> beansRightNotExists = dao.convert(_beansRightNotExists)
        beansRightNotExists.collect {x ->
            x.pathAttr().setValue(x.pathAttr().value().replace(("/" + srcProp.unusedName() + "/"), ""))
        }


        //String path = "/template/commits_only2trunk.template"
        String path = "/template/commit_history.template.html"

        File template     = new File(getClass().getResource(path).toURI())
        String hrefHeader = srcProp.hrefHeader()
        Map map = [
             hrefHeader: hrefHeader,
                beansDiffs          : beansDiffs,
                beansLeftNotExists  : beansLeftNotExists,
                beansRightNotExists : beansRightNotExists
        ]

         SimpleTemplateEngine engine  = new SimpleTemplateEngine()
        String query =
                engine.createTemplate(template).make(map).toString()

        File file = new File("output/commits_only2trunk.html")

        PrintWriter pw =
                new PrintWriter(new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(file),"UTF-8")))

        pw.write(query)
        pw.close()
    }

}
