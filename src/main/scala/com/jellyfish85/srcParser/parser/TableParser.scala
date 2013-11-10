package com.jellyfish85.srcParser.parser

import br.com.porcelli.parser._

import org.antlr.runtime.{CommonTokenStream, ANTLRStringStream}
import org.antlr.runtime.tree.CommonTree

class TableParser {

  def parse(sql: String) {

    val stream: ANTLRStringStream = new ANTLRStringStream(sql.toLowerCase)

    val lexer 	= new PLSQLLexer(stream)

    val tokens	= new CommonTokenStream(lexer)

    val parser = new PLSQLParser(tokens)

    val result = parser.sql_statement

    val tree = result.getTree.asInstanceOf[CommonTree]

    println(tree)

  }

  def walkTree(tree: CommonTree) {
    println(tree.getText)

    if (tree.getChildCount > 0) {
      for (i <- 0 to tree.getChildCount -1) {
        val child: CommonTree = tree.getChild(i).asInstanceOf[CommonTree]

        walkTree(child)
      }
    }

  }

}
