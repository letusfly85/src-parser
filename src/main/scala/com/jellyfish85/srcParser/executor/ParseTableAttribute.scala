package com.jellyfish85.srcParser.executor

import org.apache.commons.io.FilenameUtils

import com.jellyfish85.srcParser.utils.QueryBuilder
import com.jellyfish85.srcParser.parser.TableParser

import com.jellyfish85.dbaccessor.dao.src.mainte.tool.{RsSqlTablesDao, RsSqlTextDao}
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.{RsSqlTablesBean, RsSqlTextBean}
import com.jellyfish85.srcParser.converter.ConvRsSqlTextBean2RsSqlTablesBean


/**
 * == ParseTableAttribute ==
 *
 *
 *
 *@example
 *    gradle run -Prunargs=com.jellyfish85.srcParser.executor.ParseTableAttribute
 *
 * @author wada shunsuke
 *
 */
class ParseTableAttribute extends ExecutorTrait with QueryBuilder[RsSqlTextBean] {

  def run(args: Array[String]) {

    databaseInitialize()

    val dao:       RsSqlTextDao  = new RsSqlTextDao
    val parser:    TableParser[RsSqlTablesBean]  =
                                   new TableParser[RsSqlTablesBean]
    val converter: ConvRsSqlTextBean2RsSqlTablesBean =
                                   new ConvRsSqlTextBean2RsSqlTablesBean
    def register: RsSqlTablesDao = new RsSqlTablesDao

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

    val list: List[RsSqlTextBean] = dao.findSummary(db.conn)
    list.foreach {bean: RsSqlTextBean =>

      println("[TARGET]\t" + bean.pathAttr.value)
      try {
        val _list = dao.find(db.conn, bean)

        val sql: String = queryBuild(_list)
        val entry: RsSqlTablesBean = converter.convert(_list.head)

        val resultSets: List[RsSqlTablesBean] = parser.getCrudRecursive(entry, 0, sql)

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
