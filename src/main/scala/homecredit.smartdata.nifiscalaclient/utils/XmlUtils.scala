package homecredit.smartdata.nifiscalaclient.utils

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import scala.reflect.ClassTag


object XmlUtils{
  @JsonIgnoreProperties(ignoreUnknown = true)
  def fromXml[T](xml: String)(implicit ct:ClassTag[T]): T = {
    val xmlMapper = new XmlMapper()
    xmlMapper.registerModule(DefaultScalaModule)
    xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    xmlMapper.readValue[T](xml, ct.runtimeClass.asInstanceOf[Class[T]])
  }
}




