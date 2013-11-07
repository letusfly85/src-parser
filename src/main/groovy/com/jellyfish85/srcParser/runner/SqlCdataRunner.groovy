package com.jellyfish85.srcParser.runner

import com.jellyfish85.srcParser.runner.BaseRunner

class SqlCdataRunner {

   public static void main(String[] args) {
       def _context = new BaseRunner()
       _context.databaseInitialize()

       def conn = _context.getConnection()
       println(conn)

       _context.databaseFinalize()
   }
}
