package homecredit.smartdata.nifiscalaclient.api.response

import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi

sealed trait ResponseStatus
case class OK(data:Any*) extends ResponseStatus
case class NOTOK(data:Any*) extends ResponseStatus


