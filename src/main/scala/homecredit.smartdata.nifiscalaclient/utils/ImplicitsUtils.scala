package homecredit.smartdata.nifiscalaclient.utils

trait ImplicitsUtils {

  implicit var topic:String="test"
  implicit var broker:String="localhost:9092"

  implicit val uUID: Option[String] = createuUID



  implicit def createuUID:Option[String] = {
    /*
    Some uid method initialize
     */
    None
  }
}

