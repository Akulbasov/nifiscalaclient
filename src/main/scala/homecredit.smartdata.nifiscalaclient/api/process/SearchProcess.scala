package homecredit.smartdata.nifiscalaclient.api.process
import scala.collection.mutable.ListBuffer
trait SearchProcess {
  def searchProcessId(file:String):String
  implicit val searchProcessId:ListBuffer[String]
}
