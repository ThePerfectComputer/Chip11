package util

object IndentationManager {
  def getIndent(spacesPerLevel: Int=4, levels: Int=1)(body: IndentationManager => Unit) : Unit = {
    val manager = new IndentationManager(spacesPerLevel)
    manager.getIndent(levels)(body)
  }
}
class IndentationManager(val spacesPerLevel: Int=4){
  val tabs = " " * spacesPerLevel

  def getIndent(levels:Int=1)(body: IndentationManager=> Unit) : Unit ={
    val newManager = new IndentationManager(spacesPerLevel)
    val stream = new java.io.ByteArrayOutputStream()
    Console.withOut(stream) {
      body(newManager)
    }
    val lines = stream.toString().split("\n")
    for(line <- lines){
      println(s"$tabs$line")
    }
  }
}

