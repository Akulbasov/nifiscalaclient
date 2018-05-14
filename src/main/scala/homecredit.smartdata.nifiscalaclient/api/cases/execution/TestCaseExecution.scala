package homecredit.smartdata.nifiscalaclient.api.cases.execution

import homecredit.smartdata.nifiscalaclient.api.response.{ResponseStatus}


trait TestCaseExecution extends Execution{
  def executeFunction(execute:ResponseStatus*):Unit
}
