package com.jellyfish85.srcParser.runner

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlCdataBean
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSqlCdataDao
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSqlTextDao
import com.jellyfish85.srcParser.eraser.SqlFwEraser
import com.jellyfish85.srcParser.helper.SqlCdata2SqlTextHelper
import org.apache.commons.io.FilenameUtils

class CleanSqlTextRunner {

    public static void  main(String[] args) {
        def _context = new BaseRunner()
        _context.databaseInitialize()

        def dao = new RsSqlCdataDao()
        def register = new RsSqlTextDao()

        def eraser = new SqlFwEraser()


        def _targetList = dao.findSummary(_context.getConnection())
        def targetList = dao.convert(_targetList)

        targetList.each {RsSqlCdataBean target ->
            println(target.pathAttr().value())

            def _list = dao.find(_context.getConnection(), target)
            def list  = dao.convert(_list)
            def query = eraser.getErasedSqlText(list)

            def helper = new SqlCdata2SqlTextHelper()
            def entries = helper.query2RsSqlTextBeanList(query, list[0])
            register.delete(_context.getConnection(), entries[0])
            register.insert(_context.getConnection(), entries)

            _context.manager.jCommit()
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
