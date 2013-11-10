package com.jellyfish85.srcParser.executor

import org.apache.commons.io.FilenameUtils

import com.jellyfish85.srcParser.utils.QueryBuilder
import com.jellyfish85.srcParser.parser.TableParser

import com.jellyfish85.dbaccessor.dao.src.mainte.tool.RsSqlTextDao
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.{RsSqlTablesBean, RsSqlTextBean}


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
class ParseTableAttribute extends ExecutorTrait with QueryBuilder {

  def run(args: Array[String]) {

    databaseInitialize()

    val dao:  RsSqlTextDao  = new RsSqlTextDao

    val _bean: RsSqlTextBean = new RsSqlTextBean
    _bean.pathAttr.value          = args(0)
    _bean.fileNameAttr.value      = FilenameUtils.getName(args(0))
    _bean.persisterNameAttr.value = args(1)

    val list: List[RsSqlTextBean]  = dao.find(db.conn, _bean)
    val sql:  String = queryBuild(list)

    val parser: TableParser = new TableParser

    val entry = new RsSqlTablesBean
    entry.fileNameAttr.value      = _bean.fileNameAttr.value
    entry.pathAttr.value          = _bean.pathAttr.value
    entry.persisterNameAttr.value = _bean.persisterNameAttr.value

    val resultSets: List[RsSqlTablesBean] = parser.getCrudRecursive(entry, 0, sql)
    resultSets.foreach {result: RsSqlTablesBean =>
      println(result.tableNameAttr.value)
    }
  }

}
