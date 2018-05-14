package homecredit.smartdata.nifiscalaclient.api.cases

import homecredit.smartdata.nifiscalaclient.api.logging.nfclogging
import homecredit.smartdata.nifiscalaclient.api.response.{NOTOK, ResponseStatus}
import homecredit.smartdata.nifiscalaclient.api.scheme.{Case, Events, FlowProcessors}
import homecredit.smartdata.nifiscalaclient.utils.DockerUtils

trait TestCases {

    /**

      Execution flow testing and controlling clearing state
      when executed exception and logging

    **/

  def execution(execute:ResponseStatus*): Unit =
    for(e <- execute)
      nfclogging
        .filterByNOT(nfclogging(e))
        .foreach({
          g => {
            g match {
              case NOTOK("clearNifiState") => DockerUtils.dockerDown.dockerInstance
              case _ => throw new Exception(g.toString)
            }

          }
        })

    /**

      Emulation data template, for start pushing
      Logging all emulation process in log4j
      and after exception if trouble

    **/

  def emulation[Data](data:Data, emulate:(Data)=>ResponseStatus): Unit =
      nfclogging
        .filterByNOT(nfclogging(emulate(data)))
          .foreach(g => throw new Exception(g.toString))


    /**

      Validation data in every provenance events by
      expected case value in comments
      Logging all validation process in log4j

    **/

  def validation(eventsData:List[Events],
                 caseData:Case,
                 processorData:FlowProcessors,
                 validate:(List[Events],Case,FlowProcessors) => List[ResponseStatus]
                ):List[ResponseStatus] = {
                    validate(eventsData,caseData,processorData)
                      .map(nfclogging(_))
                }



    /**

      Validation case schema in processwithComments
      Logging all validation process in log4j
      and after exception if trouble

      **/

  def validation[T](processwithComments:T, validate:(T) => List[ResponseStatus]):Unit = {
    val hasNOT = nfclogging
      .filterByNOT(validate(processwithComments).map(nfclogging(_)))

    hasNOT
      .foreach(
        g => throw new Exception(g.toString)
      )
  }
}
