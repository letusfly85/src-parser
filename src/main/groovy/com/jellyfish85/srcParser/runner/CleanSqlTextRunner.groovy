package com.jellyfish85.srcParser.runner

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlCdataBean
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSqlCdataDao
import com.jellyfish85.srcParser.eraser.SqlFwEraser

class CleanSqlTextRunner {

    public static void  main(String[] args) {
        def _context = new BaseRunner()
        _context.databaseInitialize()

        def dao = new RsSqlCdataDao()

        //TODO get all bean from database after scratch
        def bean = new RsSqlCdataBean()
        println(args[0])
        bean.pathAttr().setValue(args[0])
        bean.persisterNameAttr().setValue(args[1])
        def list = dao.find(_context.getConnection(), bean)

        def eraser = new SqlFwEraser()
        def query = eraser.getErasedSqlText(dao.convert(list))

        println(query)

        _context.databaseFinalize()
    }

}
