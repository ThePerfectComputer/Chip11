package util

import org.scalatest._
import flatspec._
import matchers._


class IndentationManagerTest extends AnyFlatSpec with should.Matchers {
  behavior of "IndentationManager"

  val manager = new IndentationManager

  it should "print a blank string by default" in {
    val result = manager.tabs
    assert(result == "")
  }

  it should "indent once" in {
    manager.indent()
    assert(manager.tabs == "    ")
  }
  it should "indent more than once" in {
    manager.indent(2)
    assert(manager.tabs == "            ")
  }
  it should "dedent once" in {
    manager.dedent()
    assert(manager.tabs == "        ")
  }
  it should "dedent more than once" in {
    manager.dedent(2)
    assert(manager.tabs == "")
  }
  it should "not do anything when at indentation level 0" in {
    manager.dedent()
    assert(manager.tabs == "")
    manager.indent()
    assert(manager.tabs == "    ")
  }

  it should "support variable indentation width" in {
    val manager2 = new IndentationManager(2)
    manager2.indent()
    assert(manager2.tabs == "  ")
    manager2.indent(2)
    assert(manager2.tabs == "      ")
    manager2.dedent()
    assert(manager2.tabs == "    ")
  }
}
