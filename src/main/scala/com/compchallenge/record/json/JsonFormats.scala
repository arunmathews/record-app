package com.compchallenge.record.json

import enumeratum.Json4s
import org.json4s.{DefaultFormats, Formats}

/**
  *
  */
object JsonFormats {
  val jsonFormat: Formats = DefaultFormats.withBigDecimal
}