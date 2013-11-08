package com.jellyfish85.srcParser.bean

class SqlText {

  var line:          String  = _

  var whereOpeCounter: Int = _

  var isWhereStartLine:  Boolean = _

  var isInWhereScope:    Boolean = false

  var isWhereEndLine:    Boolean = _

  var selectOpeCounter:  Int     = 0

  var inSelectStartLine: Boolean = _

  var inSelectEndLine:   Boolean = _

  var inFromScope:   Boolean = _

  var cursor:        Int  = _

  var fwFlg:         Boolean = false

  def setLine(_line: String)  {line   = _line}

  def countUpWhereOpe {this.whereOpeCounter += 1}

  def setIsWhereStartLine(_isWhereStartLine: Boolean)    {isWhereStartLine = _isWhereStartLine}

  def setIsInWhereScope(_isInWhereScope: Boolean)        {isInWhereScope = _isInWhereScope}

  def setIsWhereEndLine(_isWhereEndLine: Boolean)        {isWhereEndLine = _isWhereEndLine}

  def countUpSelectOpe {this.selectOpeCounter += 1}

  def setInSelectStartLine(_inSelectStartLine: Boolean)  {inSelectStartLine = _inSelectStartLine}

  def setInSelectEndLine(_inSelectEndLine: Boolean)      {inSelectEndLine = _inSelectEndLine}

  def setCursor(_cursor: Int) {cursor = _cursor}

  def setFwFlg(_fwFlg: Boolean) {fwFlg = _fwFlg}

}
