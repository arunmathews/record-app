package com.compchallenge.record.datetime

import java.sql.{Date => SDate}
import org.joda.time.{DateTimeZone, LocalDate}

/**
  * Some methods to deal with different date formats
  */
object DateTimeFactory {
  private val pacificZone = DateTimeZone.forID("US/Pacific")

  def toSqlDate(localDate: LocalDate): SDate = new SDate(localDate.toDate.getTime)

  def toLocalDate(date: SDate) = LocalDate.fromDateFields(date)
}
