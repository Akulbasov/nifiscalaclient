package homecredit.smartdata.nifiscalaclient.api
import homecredit.smartdata.nifiscalaclient.api.scheme.Template
import homecredit.smartdata.nifiscalaclient.utils.{FileUtils, XmlUtils}

import scala.collection.mutable.ListBuffer


trait SearchProcessId extends FileUtils {
  def searchProcessId(file:String):String = {
    XmlUtils.fromXml[Template](readFileFromClasspath(file)).groupId
  }
  implicit val searchProcessId = new ListBuffer[String]()
}
