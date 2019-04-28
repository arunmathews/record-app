package com.compchallenge.record.validation

import enumeratum.EnumEntry
import enumeratum.Enum
import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JNull, JString}

sealed trait BOValidationFailureCode extends EnumEntry

object BOValidationFailureCode extends Enum[BOValidationFailureCode]{
  val values = findValues

  case object Invalid extends BOValidationFailureCode
  case object Missing extends BOValidationFailureCode
}