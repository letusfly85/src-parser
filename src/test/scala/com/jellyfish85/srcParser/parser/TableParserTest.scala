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
    val selectSql: String = stream2String(selectStream)

    val tree01 = parser.getCommonTree(selectSql)
    val selectEntry: RsSqlTablesBean = new RsSqlTablesBean
    selectEntry.fileNameAttr.setValue("my.file")

    val entrySelect: RsSqlTablesBean = new RsSqlTablesBean
    entrySelect.crudTypeAttr.value = "SELECT"

    "return SELECT for SQL 'SELECT SYSDATE FROM DUAL'" in {
      parser.getInitCrud(tree01, entrySelect, selectSql).crudTypeAttr.value must beEqualTo(entrySelect.crudTypeAttr.value)
    }


    val deleteStream = getClass.getResourceAsStream("/query/DELETE_USERS.sql")
    val deleteSql: String = stream2String(deleteStream)
    val tree02 = parser.getCommonTree(deleteSql)

    val deleteEntry: RsSqlTablesBean = new RsSqlTablesBean
    deleteEntry.crudTypeAttr.value = "DELETE"

    "return DELETE for SQL 'DELETE FROM USERS'" in {
      parser.getInitCrud(tree02, deleteEntry, deleteSql).crudTypeAttr.value must beEqualTo(deleteEntry.crudTypeAttr.value)
    }

    val updateStream = getClass.getResourceAsStream("/query/UPDATE_EMPLOYEE_MST.sql")
    val updateSql: String = stream2String(updateStream)
    val tree03 = parser.getCommonTree(updateSql)

    val updateEntry: RsSqlTablesBean = new RsSqlTablesBean
    updateEntry.crudTypeAttr.value = "UPDATE"

    "return UPDATE for SQL 'UPDATE EMPLOYEE MST'" in {
      parser.getInitCrud(tree03, updateEntry, updateSql).crudTypeAttr.value must beEqualTo(updateEntry.crudTypeAttr.value)
    }

  }

}
