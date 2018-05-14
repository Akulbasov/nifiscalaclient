package homecredit.smartdata.nifiscalaclient.api.cases.emulation

import java.util
import java.util.Properties

import homecredit.smartdata.nifiscalaclient.api.response._
import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi._
import homecredit.smartdata.nifiscalaclient.api.logging.nfclogging
import homecredit.smartdata.nifiscalaclient.api.ping._
import org.apache.kafka.clients.producer._
import org.apache.kafka.clients.consumer._

import scala.collection.JavaConverters._
import scala.util.{Failure, Success, Try}


trait TestCaseEmulation extends Emulation{
  implicit val props = new Properties()
  props.put("bootstrap.servers", broker)

  def isSerOrDeszationObject(isSerialization:Boolean):Properties= {
    if(isSerialization) {
      props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
      props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    }
    else {
      props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
      props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    }
    props
  }


  def configPropertiesForKafka(serialize:Boolean=true,groupId:String="test_in_0"):Properties = {
    val isSerialization = serialize
    val gid = groupId
    val pr = isSerOrDeszationObject(isSerialization)
    if(!gid.isEmpty)
      pr.put("group.id", "test_in_0")
    pr
  }

  def sendData(data:String = "Test"): ResponseStatus = {
    val producer = new KafkaProducer[String, String](configPropertiesForKafka())
    val record = new ProducerRecord(topic, "key", data)
    val rStatus = Try(producer.send(record)) match {
      case Success(_)=>
        OK(getCurrentMethodName,data,topic)
      case Failure(e) => {
        NOTOK(getCurrentMethodName,data,topic,e)
      }
    }
    producer.close()
    rStatus
  }
}