package homecredit.smartdata.nifiscalaclient.utils

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.reflect.ClassTag


/**
  * JsonUtils
  *
  * Provides JSON conversion methods
  *
  *
  * ClassTag + Jackson XML for json (rewrite)
  */

class JsonUtils(failOnUknownProperties:Boolean=false){
  val mapper = new ObjectMapper()
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUknownProperties)
  mapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, failOnUknownProperties)
  mapper.configure(DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES, failOnUknownProperties)

  def toJson(value: Object): String = {
    mapper.writeValueAsString(value)
  }

  def toMap[V](json:String)(implicit m: Manifest[V]) = fromJson[Map[String,V]](json)

  def fromJson[T](json: String)(implicit ct:ClassTag[T]): T = {
    mapper.readValue[T](json, ct.runtimeClass.asInstanceOf[Class[T]])
  }


}