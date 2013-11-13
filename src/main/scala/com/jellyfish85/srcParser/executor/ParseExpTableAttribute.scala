package com.jellyfish85.srcParser.executor

import com.jellyfish85.srcParser.utils.QueryBuilder
import com.jellyfish85.dbaccessor.dao.src.mainte.tool.{RsSqlTablesExpDao, RsSqlTextExpDao}
import com.jellyfish85.srcParser.parser.TableParser
import com.jellyfish85.srcParser.converter.{ConvRsSqlTextExpBean2RsSqlTablesExpBean}
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.{RsSqlTablesExpBean, RsSqlTextExpBean}
import org.apache.commons.io.FilenameUtils

import java.math.BigDecimal

/**
 * == ParseTableAttribute ==
 *
 *
 *
 *@example
 *    gradle run -Prunargs=com.jellyfish85.srcParser.executor.ParseExpTableAttribute
 *
 * @author wada shunsuke
 *
 */
class ParseExpTableAttribute extends ExecutorTrait with QueryBuilder[RsSqlTextExpBean] {

  def run(args: Array[String]) {

    databaseInitialize()

    val dao:       RsSqlTextExpDao  = new RsSqlTextExpDao
    val parser:    TableParser[RsSqlTablesExpBean]   =
                                      new TableParser[RsSqlTablesExpBean]
    val converter: ConvRsSqlTextExpBean2RsSqlTablesExpBean =
      new ConvRsSqlTextExpBean2RsSqlTablesExpBean
    def register: RsSqlTablesExpDao = new RsSqlTablesExpDao

    /*
    val _bean: RsSqlTextExpBean = new RsSqlTextExpBean
    _bean.pathAttr.value          = args(0)
    _bean.subLineAttr.value       = new BigDecimal(1)
    _bean.fileNameAttr.value      = FilenameUtils.getName(args(0))
    _bean.persisterNameAttr.value = args(1)

    val list: List[RsSqlTextExpBean]  = dao.find(db.conn, _bean)
    val entry = new RsSqlTablesExpBean
    entry.fileNameAttr.value      = _bean.fileNameAttr.value
    entry.pathAttr.value          = _bean.pathAttr.value
    entry.persisterNameAttr.value = _bean.persisterNameAttr.value

    val sql:  String = queryBuild(list)

    var resultSets: List[RsSqlTablesExpBean] = List()
    if (parser.isTruncateSql(sql)) {
      resultSets ::= parser.specifyTruncateSQLTable(sql.split("\n").head, entry)

    } else {
      resultSets = parser.getCrudRecursive(entry, 0, sql)
    }

    register.delete(db.conn, entry)
    register.insert(db.conn, resultSets)
    */

    register.truncate(db.conn)
    val list: List[RsSqlTextExpBean] = dao.findSummary(db.conn)
    list.foreach {bean: RsSqlTextExpBean =>

      println("[TARGET]\t" + bean.pathAttr.value)
      try {
        val _list = dao.find(db.conn, bean)

        val sql: String = queryBuild(_list)
        val entry: RsSqlTablesExpBean = converter.convert(_list.head)

        var resultSets: List[RsSqlTablesExpBean] = List()

        if (parser.isTruncateSql(sql)) {
          resultSets ::= parser.specifyTruncateSQLTable(sql.split("\n").head, entry)

        } else {
          resultSets =  parser.getCrudRecursive(entry, 0, sql)
        }

        resultSets.map{bean: RsSqlTablesExpBean => bean.subLineAttr.setValue(entry.subLineAttr.value)}

        register.delete(db.conn, entry)
        register.insert(db.conn, resultSets)

        db.jCommit

      } catch {
        case e: Exception =>
          db.jRollback
          println("[ERROR]\t" + bean.subLineAttr.value.toString + "\t" + bean.pathAttr.value)
          e.printStackTrace()
      }
    }

    databaseFinalize()
  }
}