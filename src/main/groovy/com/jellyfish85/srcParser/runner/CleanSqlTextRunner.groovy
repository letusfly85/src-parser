package com.jellyfish85.srcParser.runner

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlCdataBean
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSqlCdataDao
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSqlTextDao
import com.jellyfish85.srcParser.eraser.SqlFwEraser
import com.jellyfish85.srcParser.helper.SqlCdata2SqlTextHelper

class CleanSqlTextRunner {

    public static void  main(String[] args) {
        def _context = new BaseRunner()
        _context.databaseInitialize()

        def dao = new RsSqlCdataDao()
        def register = new RsSqlTextDao()

        //TODO get all bean from database after scratch
        def bean = new RsSqlCdataBean()
        println(args[0])
        bean.pathAttr().setValue(args[0])
        bean.persisterNameAttr().setValue(args[1])
        bean.fileNameAttr().setValue(args[2])
        def list = dao.find(_context.getConnection(), bean)

        def eraser = new SqlFwEraser()
        def query = eraser.getErasedSqlText(dao.convert(list))

        println(query)

        def helper = new SqlCdata2SqlTextHelper()
        def entries = helper.query2RsSqlTextBeanList(query, bean)
        register.delete(_context.getConnection(), entries[0])
        register.insert(_context.getConnection(), entries)

        _context.databaseFinalize()
    }

}
