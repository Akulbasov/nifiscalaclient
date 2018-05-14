package homecredit.smartdata.nifiscalaclient.http


import java.io.File
import java.nio.file.{Files, Paths}

import com.sun.net.httpserver.Headers
import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi
import homecredit.smartdata.nifiscalaclient.utils.{JsonUtils, XmlUtils}

import scala.io.Source
import scala.util.Try
import scalaj.http.{BaseHttp, Http, MultiPart}
import scala.util.Try
import scala.concurrent._
import scala.concurrent.duration.Duration
import ExecutionContext.Implicits.global



/**
  * Created by mukel on 8/5/15.
  */
trait ScalajHttpClient extends HttpClient {

  import java.io.IOException
  import java.net.URISyntaxException
  import java.nio.file.Files
  import java.nio.file.Paths

  @throws[IOException]
  @throws[URISyntaxException]
  private def readFileFromClasspath(fileName: String) : Array[Byte] = {
    Files.readAllBytes(Paths.get(getClass.getClassLoader.getResource(fileName).toURI))
  }


  def request(url:String):String = {
    val query = Http(url)
//    println(query)
    val response = query.asString
    if (response.isSuccess){
      response.body
    }
    else
      throw new Exception("HTTP request error " + response.code + ": " + response.statusLine + response.headers)
  }
//  Http("http://httpbin.org/delete").postData("hello there").method("DELETE").header("content-type","text/text").asString
  def request(requestUrl: String,headers:Map[String,String],data:Object=None): String = {
    val query = headers.to[List] match {
        case List(("method","DELETE")) => Http(requestUrl).method(headers("method"))
        case List(("method","POST"),_,_,("mime","multipart/form-data")) =>
          Http(requestUrl).postMulti(MultiPart(
            headers("name"),
            headers("fileName"),
            headers("mime"),
            readFileFromClasspath(headers("fileName"))
          ))
        case List(("method",_),("mime","application/json"),("stream","true")) => {
          Http(requestUrl).timeout(connTimeoutMs = 1000, readTimeoutMs = 1000)
            .postData(new JsonUtils().toJson(data))
            .method(headers("method"))
            .header("Content-Type", headers("mime"))
//            .execute(is=>{
//              scala.io.Source.fromInputStream(is).getLines().foreach(println)
//            })

        }
        case List(("method",_),("mime","application/json")) => {
          Http(requestUrl)
            .postData(new JsonUtils().toJson(data))
            .method(headers("method"))
            .header("Content-Type", headers("mime"))
        }
//        case List(("method","GET"),("timeout","true")) => Http(requestUrl).timeout(1000,5000)
        case List(("method","GET")) => Http(requestUrl)
        case List(("method","POST")) => Http(requestUrl).method(headers("method"))
        case Nil => throw new Exception("Headers not found")
        case _ => throw new Exception("I don't understand")
      }
        val response = query.asString
//        println(requestUrl,headers,data,"this response is ----------->>>>>>>>>",response)

        if (response.code.isInstanceOf[Int]){
          response.body
        }
        else {
          throw new Exception("HTTP request error " + response.code + ": " + response.statusLine + response.headers)
        }
  }

  def blockingRequest(requestUrl: String): Future[Unit] = {
    import scala.concurrent.duration._
    val f = Future {
      println("Wait 70 sec for ready nifi workflow")
      Thread.sleep(70000)
    }
    try{ f.onComplete{_ => Source.fromURL(requestUrl).mkString} } catch {
      case e: Exception => println("Nifi workflow not ready, wait..." + e.getMessage)
    }
    Await.ready(f, 60.seconds)
  }

  def asyncRequest(requestUrl: String,headers:Map[String,String],data:Option[Map[String,String]]=None): Future[String] = {
    val p = Promise[String]()
    Future {
      p.success(request(requestUrl,headers,data))
    }
    p.future
  }



}