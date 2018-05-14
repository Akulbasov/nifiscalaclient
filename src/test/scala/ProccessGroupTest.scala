package homecredit.smartdata.nifiscalaclient.api
import homecredit.smartdata.nifiscalaclient.api.scheme.ProcessGroup
import org.scalatest.FlatSpec

import scala.collection.mutable.ListBuffer

class ProccessGroupsTest extends FlatSpec{

  it should "return case class ProcessGroups" in {
    NifiscalaclientApi.uploadTemplates(
      processGroupId = "4850e9d5-0158-1000-7486-e92d0d8586b6",
      fileName = "Simple.xml"
    ).isInstanceOf[ProcessGroup]
  }

  it should "length 0" in {
    NifiscalaclientApi.uploadTemplates(
      processGroupId = "4850e9d5-0158-1000-7486-e92d0d8586b6",
      fileName = "Simple.xml"
    ).isInstanceOf[ProcessGroup]
  }
//  val json_output_actual:List[String] = List("1","2")
//  val json_output_expected:List[String] = List("1","3")
//  val rwejwrw = List(json_output_actual,json_output_expected)
//
//  for(rwerjwrw <- i){
//    it shouild "input some $json"
//
//    "test with number 1 failed"
//    "test with number 1 success"
//
//  }
//    assert(1==1,"hello_world") //вложенная
}
