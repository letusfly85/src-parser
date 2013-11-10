package com.jellyfish85.srcParser.parser

import br.com.porcelli.parser._

import org.antlr.runtime.{CommonTokenStream, ANTLRStringStream}
import org.antlr.runtime.tree.CommonTree
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTablesBean

class TableParser {

  /**
   * == getCommonTree ==
   *
   * @param sql
   * @return
   */
  def getCommonTree(sql: String): CommonTree = {

    val stream: ANTLRStringStream = new ANTLRStringStream(sql.toLowerCase)

    val lexer 	= new PLSQLLexer(stream)

    val tokens	= new CommonTokenStream(lexer)

    val parser = new PLSQLParser(tokens)

    val result = parser.sql_statement

    val tree = result.getTree.asInstanceOf[CommonTree]

    return tree
  }

  /**
   * == getInitCrud ==
   *
   * @param tree
   * @param entry
   * @param sql
   * @return
   */
  def getInitCrud(tree: CommonTree, entry: RsSqlTablesBean, sql: String): RsSqlTablesBean = {
    val result: RsSqlTablesBean = entry

    tree.getText match {
      case "SELECT_STATEMENT" =>
        result.crudTypeAttr.value = "SELECT"
        return result
      case "insert" =>
        result.crudTypeAttr.value = "INSERT"
        return result
      case "merge" =>
        result.crudTypeAttr.value = "MERGE"
        return result
      case "update" =>
        result.crudTypeAttr.value = "UPDATE"
        return result
      case "delete" =>
        result.crudTypeAttr.value = "DELETE"
        return result
      case x =>
        result.crudTypeAttr.value = "UNKNOWN"
        Option(tree.getChildren) match {
          case None =>
            return result

          case Some(children) =>
            for (i <- 0 until children.size())  {
              val nextTree = children.get(i).asInstanceOf[CommonTree]
              val _result  = getInitCrud(nextTree, result, sql)

              if (_result.crudTypeAttr.value != "UNKNOWN") {
                return _result
              }
            }
        }
    }

    result
  }

  /**
   * == getCrudRecursive ==
   *
   * @param target
   * @param level
   * @param sql
   * @return
   */
  def getCrudRecursive(target: RsSqlTablesBean, level: Int, sql: String) :List[RsSqlTablesBean]= {
    var entry = new RsSqlTablesBean()
    entry.fileNameAttr.value = target.fileNameAttr.value

    val tree = getCommonTree(sql)
    entry = getInitCrud(tree, entry, sql)

    var list :List[RsSqlTablesBean] = List()
    /*
    tableAttribute.crudType match {
      case "SELECT" =>
        list :::= specifySelectSQLTable(tree,sqlAttribute,tableAttribute,level)
      case "DELETE" =>
        list :::= specifyDeleteSQLTable(tree,sqlAttribute,tableAttribute,level)
      case "INSERT" =>
        list :::= specifyInsertSQLTable(tree,sqlAttribute,tableAttribute,level)
      case "UPDATE" =>
        list :::= specifyUpdateSQLTable(tree,sqlAttribute,tableAttribute,level)
      case "MERGE"  =>
        list ::= specifyMergeSQLTable(tree,sqlAttribute,tableAttribute,level)
      case _ =>
    }
   */

    list.foreach {bean =>
      bean.fileNameAttr.value      = target.fileNameAttr.value
      bean.persisterNameAttr.value = target.persisterNameAttr.value
      bean.callTypeAttr.value      = "SQL"
    }

    list
  }

  /**
   * search table name and its alias that select statement calls
   *
   * @param tree
   * @param sqlAttribute
   * @param tableAttribute
   * @return
   */
  def specifySelectSQLTable(tree :CommonTree, sqlAttribute :RsSqlTablesBean, tableAttribute: RsSqlTablesBean,
                            level: Int, sql: String) :List[RsSqlTablesBean] = {

    var tableAttributeList :List[RsSqlTablesBean] = List()

    tree.getText match {
      case "TABLE_REF" =>
        val tabAttr = getCrudRecursive(sqlAttribute, level + 1, sql)
        tableAttributeList :::= tabAttr

      case _ =>
        Option(tree.getChildren) match {
          case None =>
          case Some(children) =>
            for (i <- 0 until children.size()) {
              val nextTree = children.get(i).asInstanceOf[CommonTree]
              val tabAttr = new RsSqlTablesBean()
              tabAttr.fileNameAttr.value = sqlAttribute.fileNameAttr.value
              tabAttr.crudTypeAttr.value = tableAttribute.crudTypeAttr.value
              val tabAttrList = specifySelectSQLTable(nextTree, sqlAttribute, tabAttr, level, sql)

              if (tabAttrList.size > 0) {
                tableAttributeList :::= tabAttrList
              }
            }
        }
    }

    tableAttributeList
  }

}
