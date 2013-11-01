package com.jellyfish85.srcParser.facade

object SrcParserFacade {

  def main(args: Array[String]) {

    val className: String = args(0)
    val obj = Class.forName(className).newInstance().asInstanceOf[{
    def run(args: Array[String])
    }]

    if (args.length >= 2) {
    obj.run(args.tail)

    } else {
    val ary = Array("")
    obj.run(ary)
    }
  }
}