package homecredit.smartdata.nifiscalaclient.api.cases.emulation

import homecredit.smartdata.nifiscalaclient.api.response.{ResponseStatus}

/**
  * HttpClient
  *
  * Provides HTTP request methods (to be backed by Scalaj-Http, Dispatch or mock objects for testing)
  */
trait Emulation{
  /**
    * request
    *
    * All of the complexity of the whole query system resides here.
    *
    * The parameters passed in [options] are handled as follows:
    *   (id, file: File)       file content is injected as multipart form data.
    *   (id, None)             is ignored (= the parameter is absent)
    *   (id, Some(x))          (given optional parameter) passed as parameter id=x.toString
    *   (id, x)                (anything else) passed as parameter id=x.toString
    */
  /**
    *
    * @param requestUrl rew
//    * @param port rewr
    * @return
    */

  def sendData(data:String): ResponseStatus
//  def consumer():String
}


