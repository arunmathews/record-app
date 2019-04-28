package com.compchallenge.record.businessobject

import enumeratum.{Enum, EnumEntry}
import org.joda.time.LocalDate
import com.compchallenge.record.businessobject.RecordBO.GenderType

case class RecordBO(lastName: String, firstName: String, gender: GenderType, favColor: String, dateOfBirth: LocalDate)

case object RecordBO {
  sealed trait GenderType extends EnumEntry

  object GenderType extends Enum[GenderType] {
    val values = findValues

    case object Male extends GenderType
    case object Female extends GenderType
  }
}