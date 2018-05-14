package homecredit.smartdata.nifiscalaclient.api.call
import homecredit.smartdata.nifiscalaclient.http._
import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi

import scala.util.Try

//import scalaj.http.HttpResponse

trait CallImplements extends CallBase with ScalajHttpClient{
  def apiCall(action: String, headers: Map[String,String], data: Object): String = {
    val requestUrl = NifiscalaclientApi.apiBaseURL + action
    val response = request(requestUrl, headers,data)
    response


    //    val json = parse(response)
    //    if((json \ "status").extract[String] == "OK")
    //      (json \ "result")
    //    else
    //      throw new Exception("Invalid reponse:\n" + response)
  }

  /**
    *
    * @param action  - основные строки запросов nifi
    * @param headers - заголовоки
    * @param query   - параметры если есть
    * @param data    - данные
    * @return - возвращаем response
    */
  def apiCall(action: String, query: String, headers: Map[String, String], data: Object): String = {
    val requestUrl = NifiscalaclientApi.apiBaseURL + action + query
    val response = request(requestUrl, headers,data)
    response
//    "test"
  }

  def apiCall(url:String):String={
    val response = request(url)
    response
  }
}
