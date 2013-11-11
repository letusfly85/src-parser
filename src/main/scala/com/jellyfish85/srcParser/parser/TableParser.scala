package com.jellyfish85.srcParser.parser

import br.com.porcelli.parser._

import org.antlr.runtime.{CommonTokenStream, ANTLRStringStream}
import org.antlr.runtime.tree.CommonTree
import com.jellyfish85.dbaccessor.bean.src.mainte.tool.{RsSqlTablesBeanTrait, RsSqlTextBeanTrait}

import java.math.BigDecimal

class TableParser[A <: RsSqlTextBeanTrait, B <: RsSqlTablesBeanTrait] {

  /**
   * == getCommonTree ==
   *
   * @param sql
   * @return
   */
  def getCommonTree(sql: String): CommonTree = {

    var tree: CommonTree = null
    try {
      val stream: ANTLRStringStream = new ANTLRStringStream(sql.toLowerCase)

      val lexer   = new PLSQLLexer(stream)

      val tokens  = new CommonTokenStream(lexer)

      val parser  = new PLSQLParser(tokens)

      val result  = parser.sql_statement

      tree    = result.getTree.asInstanceOf[CommonTree]

    } catch {
      case e: Exception =>
        e.printStackTrace()
    }

    tree
  }

  /**
   * == getInitCrud ==
   *
   * @param tree
   * @param entry
   * @param sql
   * @return
   */
  def getInitCrud(tree: CommonTree, entry: B, sql: String): B = {
    val result: B = entry

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
  def getCrudRecursive(target: B, level: Int, sql: String) :List[B]= {
    //var entry = new RsSqlTablesBean()
    var entry: B = target

    val tree = getCommonTree(sql)
    entry = getInitCrud(tree, entry, sql)

    var list :List[B] = List()
    entry.crudTypeAttr.value match {
      case "SELECT" =>
        list :::= specifySelectSQLTable(tree, entry, level, sql)

      case "DELETE" =>
        list :::= specifyDeleteSQLTable(tree, entry, level, sql)

      case "INSERT" =>
        list :::= specifyInsertSQLTable(tree, entry, level, sql)

      case "UPDATE" =>
        list :::= specifyUpdateSQLTable(tree, entry, level, sql)

      case "MERGE"  =>
        list ::= specifyMergeSQLTable(tree, entry, level, sql)

      case _ =>
    }

    list.foreach {bean: B =>
      bean.fileNameAttr.value      = target.fileNameAttr.value
      bean.persisterNameAttr.value = target.persisterNameAttr.value
      bean.callTypeAttr.value      = "SQL"
    }

    list
  }


  /**
   * table名称とtableエイリアスを返却する
   *
   * @param tree
   * @param tableAttribute
   * @return
   */
  def specifyTable(tree: CommonTree, tableAttribute: B, level: Int) :List[B] = {
    //println("------" + tree.getText + "\t" + level.toString)
    var tableAttributeList :List[B] = List()

    tree.getText match {
      case "ALIAS" =>
        tableAttribute.tableAliasAttr.value = tree.getChild(0).getText

      case "TABLEVIEW_NAME" =>
        //var tabAttr = tableAttribute
        val tabAttr: B = (new Object).asInstanceOf[B]
        tabAttr.pathAttr.setValue(tableAttribute.pathAttr.value)
        tabAttr.projectNameAttr.setValue(tableAttribute.projectNameAttr.value)
        tabAttr.headRevisionAttr.setValue(tableAttribute.headRevisionAttr.value)
        tabAttr.revisionAttr.setValue(tableAttribute.revisionAttr.value)

        tabAttr.tableNameAttr.value  = tree.getChild(0).getText
        tabAttr.tableAliasAttr.value = tableAttribute.tableAliasAttr.value
        tabAttr.depthAttr.value      = new BigDecimal(level)

        tabAttr.fileNameAttr.value = tableAttribute.fileNameAttr.value
        tabAttr.pathAttr.value     = tableAttribute.pathAttr.value
        tabAttr.crudTypeAttr.value = "SELECT"
        tabAttr.callTypeAttr.value = "SQL"
        tableAttributeList ::= tabAttr

      case "SELECT_MODE"    =>
        val tabAttr: B = (new Object).asInstanceOf[B]
        tabAttr.pathAttr.setValue(tableAttribute.pathAttr.value)
        tabAttr.projectNameAttr.setValue(tableAttribute.projectNameAttr.value)
        tabAttr.headRevisionAttr.setValue(tableAttribute.headRevisionAttr.value)
        tabAttr.revisionAttr.setValue(tableAttribute.revisionAttr.value)

        tabAttr.tableNameAttr.value  = "INLINE VIEW"
        tabAttr.tableAliasAttr.value = tableAttribute.tableAliasAttr.value
        tabAttr.depthAttr.value      = new BigDecimal(level)

        tabAttr.fileNameAttr.value = tableAttribute.fileNameAttr.value
        tabAttr.pathAttr.value     = tableAttribute.pathAttr.value
        tabAttr.crudTypeAttr.value = "SELECT"
        tabAttr.callTypeAttr.value = "SQL"

        var tabAttrList: List[B] = List(tabAttr)
        for (i <- 0 until tree.getChildCount) {
          tabAttrList :::= specifyTable(tree.getChild(i).asInstanceOf[CommonTree], tableAttribute, level + 1)
        }
        return tabAttrList

      case _ =>
        var tabAttrList: List[B] = List()
        for (i <- 0 until tree.getChildCount) {
          tabAttrList :::= specifyTable(tree.getChild(i).asInstanceOf[CommonTree], tableAttribute, level + 1)
        }
        return tabAttrList
    }

    return tableAttributeList
  }

  /**
   * search table name and its alias that select statement calls
   *
   * @param tree
   * @param sqlAttribute
   * @return
   */
  def specifySelectSQLTable(tree :CommonTree, sqlAttribute :B,
                            level: Int, sql: String) :List[B] = {

    var tableAttributeList :List[B] = List()

    tree.getText match {
      case "TABLE_REF" =>
        val _list = specifyTable(tree.getChild(0).asInstanceOf[CommonTree], sqlAttribute, (level+1))

        tableAttributeList :::= _list

      case _ =>
        Option(tree.getChildren) match {
          case None =>

          case Some(children) =>

            for (i <- 0 until children.size()) {

              val nextTree = children.get(i).asInstanceOf[CommonTree]
              val tabAttr: B = sqlAttribute

              tabAttr.fileNameAttr.value = sqlAttribute.fileNameAttr.value
              tabAttr.crudTypeAttr.value = sqlAttribute.crudTypeAttr.value

              val tabAttrList: List[B] = specifySelectSQLTable(nextTree, tabAttr, level, sql)
              if (tabAttrList.size > 0) {
                tableAttributeList :::= tabAttrList
              }
            }

        }
    }

    tableAttributeList
  }

  /**
   * search table name and its alias that delete statement call
   *
   * @param tree
   * @param sqlAttribute
   * @return
   */
  def specifyDeleteSQLTable(tree :CommonTree, sqlAttribute :B,
                            level: Int, sql: String) :List[B] = {

    val tableAttributeList :List[B] = List()

    tree.getText match {
      case "TABLE_REF" =>
        val tabAttrList = specifyTable(tree.getChild(0).asInstanceOf[CommonTree], sqlAttribute, (level+1))

        return tabAttrList

      case _ =>
        Option(tree.getChildren) match {
          case None =>
            return (sqlAttribute :: tableAttributeList)

          case Some(children) =>
            for (i <- 0 to children.size() -1) {
              val nextTree = children.get(i).asInstanceOf[CommonTree]
              val tabAttrList: List[B] = specifyDeleteSQLTable(nextTree, sqlAttribute, level, sql)

              if (tabAttrList.size > 0) {
                return tabAttrList
              }
            }
        }
    }

    tableAttributeList
  }

  /**
   * search table name and its alias that insert statement call
   *
   * @param tree
   * @param sqlAttribute
   * @return
   */
  def specifyInsertSQLTable(tree :CommonTree, sqlAttribute :B,
                            level: Int, sql: String) :List[B] = {

    var tableAttributeList :List[B] = List()

    tree.getText match {
      case "TABLEVIEW_NAME" =>
        sqlAttribute.tableNameAttr.value = tree.getChild(0).getText
        tableAttributeList ::= sqlAttribute

      case "SELECT_STATEMENT" =>
        val tabAttrList: List[B] = specifyTable(tree.getChild(0).asInstanceOf[CommonTree], sqlAttribute, (level+1))
        return tabAttrList

      //todo specify insert all statement
      case "MULTI_TABLE_MODE" =>
        sqlAttribute.tableAliasAttr.value  = "INSERT ALL"
        return (sqlAttribute :: tableAttributeList)

      case _ =>
        Option(tree.getChildren) match {
          case None =>
            return tableAttributeList
          case Some(children) =>
            for (i <- 0 until children.size()) {
              val nextTree = children.get(i).asInstanceOf[CommonTree]
              val tabAttrList: List[B] = specifyInsertSQLTable(nextTree, sqlAttribute, level, sql)

              if (tabAttrList.size >0) {
                tableAttributeList :::= tabAttrList
              }
            }
        }
    }
    tableAttributeList
  }

  /**
   * search table name and its alias that update statement call
   *
   * @param tree
   * @param sqlAttribute
   * @return
   */
  def specifyUpdateSQLTable(tree :CommonTree, sqlAttribute :B,
                            level: Int, sql: String) :List[B] = {

    var tableAttributeList :List[B] = List()

    tree.getText match {
      case "TABLEVIEW_NAME" =>
        sqlAttribute.tableNameAttr.value = tree.getChild(0).getText
        tableAttributeList ::= sqlAttribute

      case "select" =>
        val tabAttrList: List[B] = specifyTable(tree.getChild(0).asInstanceOf[CommonTree], sqlAttribute, (level+1))
        tableAttributeList :::= tabAttrList

      case _ =>
        Option(tree.getChildren) match {
          case None =>
            return tableAttributeList
          case Some(children) =>
            for (i <- 0 until children.size()) {
              val nextTree = children.get(i).asInstanceOf[CommonTree]
              val tabAttrList: List[B] = specifyUpdateSQLTable(nextTree, sqlAttribute, level, sql)

              if (tabAttrList.size > 0) {
                tableAttributeList :::= tabAttrList
              }
            }
        }
    }

    tableAttributeList
  }

  /**
   * search table name and its alias that merge statement call
   *
   * @param tree
   * @param sqlAttribute
   * @return
   */
  def specifyMergeSQLTable(tree :CommonTree, sqlAttribute :B,
                           level: Int, sql: String) :B = {

    val nextTree = tree.getChildren
    if (nextTree.size() != 1) {  //TODO
      sqlAttribute.tableAliasAttr.value = tree.getChild(0).getChild(0).toString
      sqlAttribute.tableNameAttr.value  = tree.getChild(1).getChild(0).toString
      sqlAttribute.depthAttr.value      = new BigDecimal(level)

    } else {
      sqlAttribute.tableAliasAttr.value = "NONE"
      sqlAttribute.tableNameAttr.value  = tree.getChild(0).getChild(0).toString
      sqlAttribute.depthAttr.value      = new BigDecimal(level)
    }

    sqlAttribute
  }
}