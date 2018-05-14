package homecredit.smartdata.nifiscalaclient.api.cases.execution

import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi
import homecredit.smartdata.nifiscalaclient.api.response.ResponseStatus

trait Execution {
  def executeFunction(execute:ResponseStatus*):Unit
}