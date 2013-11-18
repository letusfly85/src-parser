package com.jellyfish85.srcParser.parser

import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith

import java.math.BigDecimal
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTablesBean

@RunWith(classOf[JUnitRunner])
class TableParserTest extends Specification {

  val parser: TableParser[RsSqlTablesBean] = new TableParser[RsSqlTablesBean]

  "return true" should {
    val sql  = "SELECT SYSDATE FROM DUAL"
    val tree = parser.getCommonTree(sql)
    val entry00: RsSqlTablesBean = new RsSqlTablesBean
    entry00.fileNameAttr.setValue("my.file")

    val entry01: RsSqlTablesBean = new RsSqlTablesBean
    entry01.crudTypeAttr.value = "SELECT"

    "return SELECT for SQL 'SELECT SYSDATE FROM DUAL'" in {
      parser.getInitCrud(tree, entry00, sql).crudTypeAttr.value must beEqualTo(entry01.crudTypeAttr.value)
    }

  }

}
