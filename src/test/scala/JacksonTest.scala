package homecredit.smartdata.nifiscalaclient.json

import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi
import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi._
import homecredit.smartdata.nifiscalaclient.utils.JsonUtils
import org.scalatest.{FeatureSpec, GivenWhenThen}

import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

class JacksonTest extends FeatureSpec with GivenWhenThen {
  feature("JacksonTest#getAsJsonByEmptyField") {
    val example_of_string =
      """{"cases":[
        |              {"caseValue":"Error JSON","caseId":0},
        |              {"caseValue":{"cd2":"Artem","z":"1833971301"},"caseId":1},
        |              {"caseValue":{"cd2":"Anya","z":"18339713wq"},"caseId":2}
        |              ]}""".stripMargin



    def getAsJson[T](data:String,failOnUknownProperties:Boolean=false)(implicit ct:ClassTag[T]): T = {
      new JsonUtils(failOnUknownProperties).fromJson[T](data)
    }
    getAsJson[TestCasesSchema1](example_of_string,true).cases.foreach( e => info(e.asInstanceOf[String]))

  }
}
case class Case(caseId:Integer,caseValue:Object)
case class TestCasesSchema1(cases:List[Case])
