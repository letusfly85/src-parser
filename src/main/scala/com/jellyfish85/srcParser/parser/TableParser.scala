package com.jellyfish85.srcParser.parser

import br.com.porcelli.parser._

import org.antlr.runtime.{CommonTokenStream, ANTLRStringStream}
import org.antlr.runtime.tree.CommonTree

import com.jellyfish85.dbaccessor.bean.src.mainte.tool.RsSqlTablesBeanTrait

import java.math.BigDecimal

/**
 *
 * @todo  1.refactor, 2. add subversion attributes to bean
 * @param m
 * @tparam A
 */
class TableParser[A <: RsSqlTablesBeanTrait](implicit m:ClassManifest[A]) {

  /**
   * == getCommonTree ==
   *
   * @param sql
   * @return
   *
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
  def getInitCrud(tree: CommonTree, entry: A, sql: String): A = {
    val result: A = entry

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
   * @param attr
   * @param level
   * @param sql
   * @return
   */
  def getCrudRecursive(attr: A, level: Int, sql: String) :List[A]= {
    val anyRef = classManifest[A].erasure
    var newAttr: A = anyRef.newInstance.asInstanceOf[A]
    attr.copyAttr(newAttr)

    val tree = getCommonTree(sql)
    newAttr = getInitCrud(tree, newAttr, sql)

    var list :List[A] = List()
    newAttr.crudTypeAttr.value match {
      case "SELECT" =>
        list :::= specifySelectSQLTable(tree, newAttr, level, sql)

      case "DELETE" =>
        list :::= specifyDeleteSQLTable(tree, newAttr, level, sql)

      case "INSERT" =>
        list :::= specifyInsertSQLTable(tree, newAttr, level, sql)

      case "UPDATE" =>
        list :::= specifyUpdateSQLTable(tree, newAttr, level, sql)

      case "MERGE"  =>
        list ::= specifyMergeSQLTable(tree, newAttr, level, sql)

      case _ =>
    }

    list.foreach {bean: A =>
      bean.fileNameAttr.value      = attr.fileNameAttr.value
      bean.persisterNameAttr.value = attr.persisterNameAttr.value
      bean.callTypeAttr.value      = "SQL"
    }

    list
  }


  /**
   * search table name and its alias
   *
   * @param tree
   * @param attr
   * @return
   */
  def specifyTable(tree: CommonTree, attr: A, level: Int) :List[A] = {
    //println("------" + tree.getText + "\t" + level.toString)
    var attrList :List[A] = List()

    tree.getText match {
      case "ALIAS" =>
        attr.tableAliasAttr.value = tree.getChild(0).getText

      case "TABLEVIEW_NAME" =>
        val anyRef = classManifest[A].erasure
        val newAttr: A = anyRef.newInstance.asInstanceOf[A]

        attr.copyAttr(newAttr)
        newAttr.tableNameAttr.value  = tree.getChild(0).getText
        newAttr.depthAttr.value      = new BigDecimal(level)
        newAttr.crudTypeAttr.value = "SELECT"
        newAttr.callTypeAttr.value = "SQL"

        attrList ::= newAttr

      case "SELECT_MODE"    =>
        val anyRef = classManifest[A].erasure
        val newAttr: A = anyRef.newInstance.asInstanceOf[A]

        attr.copyAttr(newAttr)
        newAttr.tableNameAttr.value  = "INLINE VIEW"
        newAttr.depthAttr.value      = new BigDecimal(level)
        newAttr.crudTypeAttr.value = "SELECT"
        newAttr.callTypeAttr.value = "SQL"

        var newAttrList: List[A] = List(newAttr)
        for (i <- 0 until tree.getChildCount) {
          newAttrList :::= specifyTable(tree.getChild(i).asInstanceOf[CommonTree], attr, level + 1)
        }
        return newAttrList

      case _ =>
        var newAttrList: List[A] = List()
        for (i <- 0 until tree.getChildCount) {
          newAttrList :::= specifyTable(tree.getChild(i).asInstanceOf[CommonTree], attr, level + 1)
        }
        return newAttrList
    }

    return attrList
  }

  /**
   * search table name and its alias that select statement calls
   *
   * @param tree
   * @param attr
   * @return
   */
  def specifySelectSQLTable(tree :CommonTree, attr :A,
                            level: Int, sql: String) :List[A] = {

    var attrList :List[A] = List()

    tree.getText match {
      case "TABLE_REF" =>
        val _list = specifyTable(tree.getChild(0).asInstanceOf[CommonTree], attr, (level+1))

        attrList :::= _list

      case _ =>
        Option(tree.getChildren) match {
          case None =>

          case Some(children) =>

            for (i <- 0 until children.size()) {

              val nextTree = children.get(i).asInstanceOf[CommonTree]

              val anyRef = classManifest[A].erasure
              val newAttr: A = anyRef.newInstance.asInstanceOf[A]
              attr.copyAttr(newAttr)

              val newAttrList: List[A] = specifySelectSQLTable(nextTree, newAttr, level, sql)
              if (newAttrList.size > 0) {
                attrList :::= newAttrList
              }
            }

        }
    }

    attrList
  }

  /**
   * search table name and its alias that delete statement call
   *
   * @param tree
   * @param attr
   * @return
   */
  def specifyDeleteSQLTable(tree :CommonTree, attr :A,
                            level: Int, sql: String) :List[A] = {

    val attrList :List[A] = List()

    tree.getText match {
      case "TABLE_REF" =>
        val newAttrList = specifyTable(tree.getChild(0).asInstanceOf[CommonTree], attr, (level+1))

        return newAttrList

      case _ =>
        Option(tree.getChildren) match {
          case None =>
            return (attr :: attrList)

          case Some(children) =>
            for (i <- 0 to children.size() -1) {
              val nextTree = children.get(i).asInstanceOf[CommonTree]
              val newAttrList: List[A] = specifyDeleteSQLTable(nextTree, attr, level, sql)

              if (newAttrList.size > 0) {
                return newAttrList
              }
            }
        }
    }

    attrList
  }

  /**
   * search table name and its alias that insert statement call
   *
   * @param tree
   * @param attr
   * @return
   */
  def specifyInsertSQLTable(tree :CommonTree, attr :A,
                            level: Int, sql: String) :List[A] = {

    var attrList :List[A] = List()

    tree.getText match {
      case "TABLEVIEW_NAME" =>
        attr.tableNameAttr.value = tree.getChild(0).getText
        attrList ::= attr

      case "SELECT_STATEMENT" =>
        val newAttrList: List[A] = specifyTable(tree.getChild(0).asInstanceOf[CommonTree], attr, (level+1))
        return newAttrList

      //todo specify insert all statement
      case "MULTI_TABLE_MODE" =>
        attr.tableAliasAttr.value  = "INSERT ALL"
        return (attr :: attrList)

      case _ =>
        Option(tree.getChildren) match {
          case None =>
            return attrList
          case Some(children) =>
            for (i <- 0 until children.size()) {
              val nextTree = children.get(i).asInstanceOf[CommonTree]
              val newAttrList: List[A] = specifyInsertSQLTable(nextTree, attr, level, sql)

              if (newAttrList.size >0) {
                attrList :::= newAttrList
              }
            }
        }
    }
    attrList
  }

  /**
   * search table name and its alias that update statement call
   *
   * @param tree
   * @param attr
   * @return
   */
  def specifyUpdateSQLTable(tree :CommonTree, attr :A,
                            level: Int, sql: String) :List[A] = {

    var attrList :List[A] = List()

    tree.getText match {
      case "TABLEVIEW_NAME" =>
        attr.tableNameAttr.value = tree.getChild(0).getText
        attrList ::= attr

      case "select" =>
        val tabAttrList: List[A] = specifyTable(tree.getChild(0).asInstanceOf[CommonTree], attr, (level+1))
        attrList :::= tabAttrList

      case _ =>
        Option(tree.getChildren) match {
          case None =>
            return attrList
          case Some(children) =>
            for (i <- 0 until children.size()) {
              val nextTree = children.get(i).asInstanceOf[CommonTree]
              val newAttrList: List[A] = specifyUpdateSQLTable(nextTree, attr, level, sql)

              if (newAttrList.size > 0) {
                attrList :::= newAttrList
              }
            }
        }
    }

    attrList
  }

  /**
   * search table name and its alias that merge statement call
   *
   * @param tree
   * @param attr
   * @return
   */
  def specifyMergeSQLTable(tree :CommonTree, attr :A,
                           level: Int, sql: String) :A = {

    val nextTree = tree.getChildren
    if (nextTree.size() != 1) {  //TODO
      attr.tableAliasAttr.value = tree.getChild(0).getChild(0).toString
      attr.tableNameAttr.value  = tree.getChild(1).getChild(0).toString
      attr.depthAttr.value      = new BigDecimal(level)

    } else {
      attr.tableAliasAttr.value = "NONE"
      attr.tableNameAttr.value  = tree.getChild(0).getChild(0).toString
      attr.depthAttr.value      = new BigDecimal(level)
    }

    attr
  }

  def simpleWalk(tree: CommonTree) {
    println(tree.getText)

    val children = tree.getChildren
    if (children != null && children.size() > 0){
      for (i <- 0 until children.size()) {
        simpleWalk(children.get(i).asInstanceOf[CommonTree])
      }
    }
  }

  /**
   * == isTruncateSql ==
   *
   *
   * @param sql
   * @return
   */
  def isTruncateSql(sql: String): Boolean = {
    val firstLine: String = sql.split("\n").head
    if (firstLine.toUpperCase().matches(".*(TRUNCATE).*")) {
      return true

    } else {
      return false
    }
  }

  /**
   * == specifyTruncateSQLTable ==
   *
   *
   * @param queryFirstLine
   * @param attr
   * @return
   */
  def specifyTruncateSQLTable(queryFirstLine: String, attr :A) :A = {
    val tableName: String = queryFirstLine.toUpperCase().
      replaceAll("""([\s|\t]+)(TRUNCATE)([\s|\t]+)(TABLE)([\s|\t]+)""", "")

    val anyRef = classManifest[A].erasure
    val newAttr: A = anyRef.newInstance.asInstanceOf[A]

    attr.copyAttr(newAttr)
    newAttr.depthAttr.setValue(new BigDecimal(0))
    newAttr.tableNameAttr.setValue(tableName)
    newAttr.crudTypeAttr.setValue("TRUNCATE")
    newAttr.callTypeAttr.setValue("SQL")

    newAttr
  }
}