package homecredit.smartdata.nifiscalaclient

import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi
import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi.{matchingCaseWithContentByProvenanceEventsId, nifiBaseURL, ping}
import homecredit.smartdata.nifiscalaclient.api.cases.Case
import homecredit.smartdata.nifiscalaclient.api.logging.nfclogging
import homecredit.smartdata.nifiscalaclient.api.response.{NOTOK, OK, ResponseStatus}
import homecredit.smartdata.nifiscalaclient.utils.DockerUtils

import scala.util.{Failure, Success, Try}

object NifiFlowTesting extends App {


    /**

      Execution and start nifi testing flow.
      If find exception restart docker with nifi

    **/

  def startNifiTest():Unit = {
    Case.execution(tryNifiPing, runNifiState())
      matchingCaseWithContentByProvenanceEventsId(clearNifiState).foreach(
        groupProcess => nfclogging.filterByNOT(groupProcess)
          .foreach(
            g => throw new Exception(g.toString)
          )
      )

  }

  private def runNifiState() = {
    Try(
      NifiscalaclientApi
        .deployTemplatesOnWorkFlow
        .runProcessGroup
        .emulateKafkaConsumer
        .submitProvenanceRequest
    ) match {
      case Success(_) => OK(NifiscalaclientApi.getCurrentMethodName)
      case Failure(e) =>
        nfclogging(clearNifiState())
        NOTOK(NifiscalaclientApi.getCurrentMethodName,e)
    }
  }


  private def tryNifiPing:ResponseStatus = {
    Try(
      if(!ping(nifiBaseURL)){
        DockerUtils.dockerInstance
      do {
        Thread.sleep( 500 )
      }while(!ping(nifiBaseURL))
    }) match {
      case Success(_) => OK(NifiscalaclientApi.getCurrentMethodName)
      case Failure(e) => NOTOK(NifiscalaclientApi.getCurrentMethodName,e)
    }
  }

  private def clearNifiState() = {
    Try({
        DockerUtils.dockerRemoveProvenance
        NifiscalaclientApi
          .deleteTemplatesById()
          .stopProcessGroup
          .deleteProcessGroupsById()
          .deleteProvenanceRequest()
          .emptyAllQueues
    }) match {
      case Success(_) => OK(NifiscalaclientApi.getCurrentMethodName)
      case Failure(e) => NOTOK(NifiscalaclientApi.getCurrentMethodName,e)
    }
  }
}
