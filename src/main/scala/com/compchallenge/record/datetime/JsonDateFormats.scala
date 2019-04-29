package com.compchallenge.record.datetime

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.json4s.CustomSerializer
import org.json4s.JsonAST.{JNull, JString}

/**
  * Custom json date format for joda time
  */
object JsonDateFormats {
  val dateFormatter = DateTimeFormat.forPattern("MM/dd/YYYY")
  val dateTimeFormatter = DateTimeFormat.forPattern("HHmmss'T'ddMMYYYY")
}

object DateSerializer extends CustomSerializer[LocalDate] (format =>
  (
    {
      case JString(s) =>
        JsonDateFormats.dateFormatter.parseLocalDate(s)
      case JNull => null
    },
    {
      case x: LocalDate =>
        JString(JsonDateFormats.dateFormatter.print(x))
    }
    )
)

