package com.jellyfish85.srcParser.executor

import com.jellyfish85.srcParser.utils.QueryBuilder
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.{RsSqlTablesExpDao, RsSqlTextExpDao}
import com.jellyfish85.srcParser.parser.TableParser
import com.jellyfish85.srcParser.converter.{ConvRsSqlTextExpBean2RsSqlTablesExpBean}
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.{RsSqlTablesExpBean, RsSqlTablesBean, RsSqlTextExpBean}

class ParseExpTableAttribute extends ExecutorTrait with QueryBuilder[RsSqlTextExpBean] {

  def run(args: Array[String]) {

    databaseInitialize()

    val dao:       RsSqlTextExpDao  = new RsSqlTextExpDao
    val parser:    TableParser[RsSqlTextExpBean, RsSqlTablesExpBean]   =
                                      new TableParser[RsSqlTextExpBean, RsSqlTablesExpBean]
    val converter: ConvRsSqlTextExpBean2RsSqlTablesExpBean =
      new ConvRsSqlTextExpBean2RsSqlTablesExpBean
    def register: RsSqlTablesExpDao = new RsSqlTablesExpDao

    /*
    val _bean: RsSqlTextBean = new RsSqlTextBean
    _bean.pathAttr.value          = args(0)
    _bean.fileNameAttr.value      = FilenameUtils.getName(args(0))
    _bean.persisterNameAttr.value = args(1)

    val list: List[RsSqlTextBean]  = dao.find(db.conn, _bean)
    val sql:  String = queryBuild(list)


    val entry = new RsSqlTablesBean
    entry.fileNameAttr.value      = _bean.fileNameAttr.value
    entry.pathAttr.value          = _bean.pathAttr.value
    entry.persisterNameAttr.value = _bean.persisterNameAttr.value

    val resultSets: List[RsSqlTablesBean] = parser.getCrudRecursive(entry, 0, sql)


    register.delete(db.conn, entry)
    register.insert(db.conn, resultSets)
    */

    val list: List[RsSqlTextExpBean] = dao.findSummary(db.conn)
    list.foreach {bean: RsSqlTextExpBean =>

      println("[TARGET]\t" + bean.pathAttr.value)
      try {
        val _list = dao.find(db.conn, bean)

        val sql: String = queryBuild(_list)
        val entry: RsSqlTablesExpBean = converter.convert(_list.head)

        val resultSets: List[RsSqlTablesExpBean] = parser.getCrudRecursive(entry, 0, sql)

        register.delete(db.conn, entry)
        register.insert(db.conn, resultSets)

        db.jCommit

      } catch {
        case e: Exception =>
          db.jRollback
          println("[ERROR]\t" + bean.persisterNameAttr.value + "\t" + bean.pathAttr.value)
          e.printStackTrace()
      }
    }

    databaseFinalize()
  }
}
