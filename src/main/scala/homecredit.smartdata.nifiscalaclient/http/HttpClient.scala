package homecredit.smartdata.nifiscalaclient.http

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scalaj.http.HttpRequest
import homecredit.smartdata.nifiscalaclient.api.response.ResponseStatus

/**
  * HttpClient
  *
  * Provides HTTP request methods (to be backed by Scalaj-Http, Dispatch or mock objects for testing)
  */
trait HttpClient{
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
//    * @param port rewr
    * @return
    */
//    def checkStatus(httpRequest: String):ResponseStatus
    def request(id:String): String
    def request(requestUrl: String,headers:Map[String,String],data:Object = None): String
//    def request(requestUrl: String,headers:Map[String,String],data:Object = None): String
//    def asyncRequest(requestUrl: String,headers:Map[String,String],data:Option[Map[String,String]]=None): Future[String]
}