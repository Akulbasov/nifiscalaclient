package homecredit.smartdata.nifiscalaclient.api.call

//import scalaj.http.HttpResponse

trait CallBase{

  /**
    *
    * @param action - основные строки запросов nifi
    * @param headers - заголовоки
    * @param data - данные
    * @return - возвращаем response
    */

  def apiCall(action: String, headers: Map[String,String], data: Object):String

  /**
    *
    * @param action - основные строки запросов nifi
    * @param headers - заголовоки
    * @param query - параметры если есть
    * @param data - данные
    * @return - возвращаем response
    */

  def apiCall(action: String, query:String, headers: Map[String,String], data: Object):String

  /**
    * @param id - просто id
    */

  def apiCall(id:String):String
}
