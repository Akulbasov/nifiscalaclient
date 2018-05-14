package homecredit.smartdata.nifiscalaclient.api.ping

import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi

import scala.util.Try
import scala.io.Source

trait Ping{
  def ping(URL:String): Boolean ={
    Try(Source.fromURL(URL)).isSuccess
  }
}

