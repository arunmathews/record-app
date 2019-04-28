package com.compchallenge.record.service

import com.compchallenge.record.json.JsonFormats
import org.json4s
import org.json4s.{Formats, JField, JValue}
import org.json4s.JsonAST.{JField, JObject, JString, JValue}
import org.scalatra.json.JacksonJsonSupport

/**
 * Mixin json specific logic to the servlet
 */
trait JsonSupportMixin
  extends JacksonJsonSupport {

  before() {
    contentType = formats("json")
  }

  protected implicit lazy val jsonFormats: Formats = JsonFormats.jsonFormat
}