package com.compchallenge.record.json

import enumeratum.Json4s
import org.json4s.{DefaultFormats, Formats}
import com.compchallenge.record.businessobject.RecordBO.GenderType
import com.compchallenge.record.datetime.DateSerializer
import com.compchallenge.record.validation.BOValidationFailureCode

object JsonFormats {
  val jsonFormat: Formats = DefaultFormats.withBigDecimal + 
    Json4s.serializer(GenderType) + 
    Json4s.serializer(BOValidationFailureCode) + 
    DateSerializer
}