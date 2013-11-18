package com.jellyfish85.srcParser.parser

import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith

import java.math.BigDecimal
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTablesBean
import com.jellyfish85.srcParser.utils.Stream2String

@RunWith(classOf[JUnitRunner])
class TableParserTest extends Specification with Stream2String {

  val parser: TableParser[RsSqlTablesBean] = new TableParser[RsSqlTablesBean]

  "return true" should {

    val selectStream = getClass.getResourceAsStream("/query/SELECT_DUAL.sql")
    val sqlSelect: String = stream2String(selectStream)
    val tree01 = parser.getCommonTree(sqlSelect)
    val entry00: RsSqlTablesBean = new RsSqlTablesBean
    entry00.fileNameAttr.setValue("my.file")

    val entrySelect: RsSqlTablesBean = new RsSqlTablesBean
    entrySelect.crudTypeAttr.value = "SELECT"

    "return SELECT for SQL 'SELECT SYSDATE FROM DUAL'" in {
      parser.getInitCrud(tree01, entrySelect, sqlSelect).crudTypeAttr.value must beEqualTo(entrySelect.crudTypeAttr.value)
    }


    val entryDelete: RsSqlTablesBean = new RsSqlTablesBean
    entryDelete.crudTypeAttr.value = "DELETE"

    val sqlDelete  = "DELETE FROM DUAL"
    val tree02 = parser.getCommonTree(sqlDelete)
    "return DELETE for SQL 'DELETE FROM DUAL'" in {
      parser.getInitCrud(tree02, entryDelete, sqlDelete).crudTypeAttr.value must beEqualTo(entryDelete.crudTypeAttr.value)
    }

  }

}
