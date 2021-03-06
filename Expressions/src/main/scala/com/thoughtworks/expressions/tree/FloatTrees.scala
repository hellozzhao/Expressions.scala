package com.thoughtworks.expressions.tree

import java.util.IdentityHashMap

import com.thoughtworks.expressions.api.Floats
import com.thoughtworks.feature.Factory.{Factory1, inject}
import scala.collection.JavaConverters._
import scala.language.higherKinds

/**
  * @author 杨博 (Yang Bo)
  */
trait FloatTrees extends Floats with ValueTrees {

  protected trait FloatTermApi extends super.FloatTermApi with ValueTermApi with FloatExpressionApi {
    thisFloat: FloatTerm =>

    def factory: Factory1[TreeApi { type TermIn[C <: Category] = C#FloatTerm }, ThisTerm] = {
      float.factory
    }
  }

  override type FloatTerm <: (ValueTerm with Any) with FloatTermApi

  final case class FloatParameter(id: Any) extends TreeApi with Parameter { thisParameter =>
    type TermIn[C <: Category] = C#FloatTerm

    def export(foreignCategory: Category, map: ExportContext): foreignCategory.FloatTerm = {
      map.asScala
        .getOrElseUpdate(this, foreignCategory.float.parameter(id))
        .asInstanceOf[foreignCategory.FloatTerm]
    }

    def alphaConversion(context: AlphaConversionContext): TreeApi = {
      val newId = new AnyRef {
        override def toString: String = raw"""α-converted(${thisParameter.toString})"""
      }
      context.asScala.getOrElseUpdate(this, FloatParameter(newId))
    }

  }

  final case class FloatLiteral(value: Float) extends TreeApi with Operator {

    def alphaConversion(context: AlphaConversionContext): TreeApi = this

    type TermIn[C <: Category] = C#FloatTerm

    def export(foreignCategory: Category, map: ExportContext): foreignCategory.FloatTerm = {
      map.asScala
        .getOrElseUpdate(this, foreignCategory.float.literal(value))
        .asInstanceOf[foreignCategory.FloatTerm]
    }
  }

  protected trait FloatTypeApi extends super.FloatTypeApi with FloatExpressionApi {
    def in(foreignCategory: Category): TypeIn[foreignCategory.type] = {
      foreignCategory.float
    }

    def literal(value: Float): FloatTerm = {
      factory.newInstance(FloatLiteral(value))
    }

    def parameter(id: Any): FloatTerm = {
      factory.newInstance(FloatParameter(id))
    }

    @inject
    def factory: Factory1[TreeApi { type TermIn[C <: Category] = ThisTerm#TermIn[C] }, ThisTerm]
  }

  type FloatType <: (ValueType with Any) with FloatTypeApi

}
