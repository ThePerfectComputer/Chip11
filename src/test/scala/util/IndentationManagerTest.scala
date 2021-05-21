package util

import org.scalatest._
import flatspec._
import matchers._

class IndentationManagerTest extends AnyFlatSpec with should.Matchers {
  behavior of "IndentationManager"

  it should "support context manager" in {

    val stream = new java.io.ByteArrayOutputStream()
    Console.withOut(stream) {
      val manager = new IndentationManager
      println("No Indent")
      manager.getIndent() { manager =>
        println("More Indent")
        manager.getIndent() { manager =>
          println("Most Indent")
        }
      }
    }
    assert(stream.toString() =="""No Indent
    More Indent
        Most Indent
""")


  }
  it should "Support custom tab levels" in {
    val stream = new java.io.ByteArrayOutputStream()
    Console.withOut(stream) {
      IndentationManager.getIndent(spacesPerLevel=2){ manager =>
        println("Some Indent")
        manager.getIndent(){ manager =>
          println("More Indent")
        }
      }
    }
    assert(stream.toString() =="""  Some Indent
    More Indent
""")

  }
}
