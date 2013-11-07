package com.jellyfish85.srcParser.runner

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlCdataBean
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSqlCdataDao
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSvnSrcInfoDao
import com.jellyfish85.srcParser.converter.ConvRsSvnSrcInfoBean2SVNRequestBean
import com.jellyfish85.srcParser.runner.BaseRunner
import com.jellyfish85.srcParser.parser.SqlCdataParser
import com.jellyfish85.srcParser.downloader.DownloadSource2Workspace
import com.jellyfish85.svnaccessor.bean.SVNRequestBean

class SqlCdataRunner {

   public static void main(String[] args) {
       def _context = new BaseRunner()
       _context.databaseInitialize()

       def conn = _context.getConnection()
       def app  = _context.app

       def parser = new SqlCdataParser()

       def dao  = new RsSvnSrcInfoDao()
       def list = dao.findByExtension(conn, app.al())

       def converter = new ConvRsSvnSrcInfoBean2SVNRequestBean()
       def requestList = dao.convert(converter.convert(list))

       def dl     = new DownloadSource2Workspace()
       dl.downLoadAll(app, requestList)

       def register = new RsSqlCdataDao()
       register.truncate(conn)

       requestList.each {SVNRequestBean target ->
           def sets = parser.parse(target, app)
           register.insert(conn, sets)

           _context.manager.Jcommit()
       }

       _context.databaseFinalize()
   }
}
