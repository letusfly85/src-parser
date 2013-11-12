package com.jellyfish85.srcParser.runner

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlCdataBean
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSqlCdataDao
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSqlTextExpDao
import com.jellyfish85.srcParser.eraser.SqlExpSplitter

class CleanSqlTextExpRunner {

    public static void  main(String[] args) {
        def _context = new BaseRunner()
        _context.databaseInitialize()

        def dao = new RsSqlCdataDao()
        def register = new RsSqlTextExpDao()

        def splitter = new SqlExpSplitter()


        def _targetList = dao.findSummaryByExtension(_context.getConnection(), _context.app.sql())
        def targetList = dao.convert(_targetList)

        targetList.each {RsSqlCdataBean target ->
            println(target.pathAttr().value())

            def _list = dao.find(_context.getConnection(), target)
            def list  = dao.convert(_list)
            def sets = splitter.split(list)

            if (sets.size() > 0) {
                register.delete(_context.getConnection(), sets[0])
                register.insert(_context.getConnection(), sets)

                _context.manager.jCommit()
            }
        }

        /*
        def bean = new RsSqlCdataBean()
        println(args[0])
        bean.pathAttr().setValue(args[0])
        bean.persisterNameAttr().setValue(args[1])
        bean.fileNameAttr().setValue(FilenameUtils.getName(args[0]))
        def list = dao.find(_context.getConnection(), bean)
        def query = eraser.getErasedSqlText(dao.convert(list))

        println(query)

        def helper = new SqlCdata2SqlTextHelper()
        def entries = helper.query2RsSqlTextBeanList(query, bean)
        register.delete(_context.getConnection(), entries[0])
        register.insert(_context.getConnection(), entries)
        */

        _context.databaseFinalize()
    }

}
