package com.thoughtworks.expressions

import com.thoughtworks.expressions.tree.AllTrees
import com.thoughtworks.feature.Factory
import org.scalatest.{FreeSpec, Matchers}

/**
  * @author 杨博 (Yang Bo)
  */
class OpenCLExpressionsSpec extends FreeSpec with Matchers {

  "id" in {
    val category1 = {
      Factory[AllTrees].newInstance()
    }

    val category2 = {
      Factory[AllTrees].newInstance()
    }

    def foo(e1: category1.FloatTerm): category2.FloatTerm = {
      e1.in(category2)
    }

    def bar(e1: category1.ArrayTerm { type Element = category1.FloatTerm })
      : category2.ArrayTerm { type Element = category2.FloatTerm } = {
      e1.in(category2)
    }

  }
}