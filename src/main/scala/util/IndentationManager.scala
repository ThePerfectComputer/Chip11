package util

class IndentationManager(val spacesPerLevel: Int=4){
  var indentationLevel:Int = 0

  def tabs : String = {
    " ".repeat(indentationLevel*spacesPerLevel)
  }
  def indent(levels:Int=1){
    indentationLevel += levels
  }
  def dedent(levels:Int=1){
    indentationLevel -= levels
    if(indentationLevel < 0) indentationLevel = 0
  }
}
