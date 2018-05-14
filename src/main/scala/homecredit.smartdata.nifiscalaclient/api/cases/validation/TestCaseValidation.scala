package homecredit.smartdata.nifiscalaclient.api.cases.validation

import homecredit.smartdata.nifiscalaclient.api.{NifiscalaclientApi, response}
import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi.{getAsJson, toJson}
import homecredit.smartdata.nifiscalaclient.api.logging.nfclogging
import homecredit.smartdata.nifiscalaclient.api.scheme.{Case, Events, FlowProcessors, TestCasesSchema}
import homecredit.smartdata.nifiscalaclient.api.response._

import scala.util.{Failure, Success, Try}

trait TestCaseValidation extends Validation {

  def validateCaseValue(
                         eventsData:List[Events],
                         caseData:Case,
                         componentsData:FlowProcessors
                       ): List[ResponseStatus] = {
    eventsData
      .filter(
        pE => {pE.componentId==componentsData.component.id}
      )
      .groupBy(
        pE=>(
          pE.eventType,pE.inputContentAvailable,pE.outputContentAvailable
        )
      ).map(
        pEType => {
          val isMatch = pEType._2.map(pE => pE.eventContent.toString)
            .exists(toJson(caseData.caseValue).contains)
          Try(assert(isMatch)) match {
            case Success(_) =>
                OK(pEType._1,caseData,componentsData,isMatch)
            case Failure(e) =>
                NOTOK(pEType._1,caseData,componentsData,isMatch)
          }
        }
    ).toList
  }

  def validateCaseSchema(
                          processwithComments: List[FlowProcessors]
                        ): List[ResponseStatus] = {
      def countAllCasesInCommentsProcessors:Int = {
        var count:Integer=0
        processwithComments.map(
          pr=>getAsJson[TestCasesSchema](pr.component.config.getAllComments).cases.map({
            c => count += 1
          })
        )
        count
      }
      def checkCaseSchema = {
        for {
          pr <- processwithComments
        } yield {
          Try(
            NifiscalaclientApi.getAsJson[TestCasesSchema]
              (pr.component.config.getAllComments,true).cases
          ) match {
            case Failure(e) =>{
                NOTOK(pr,e)
            }
            case Success(s) => {
                OK(pr, s)
            }
          }
        }
      }
      Try(processwithComments) match {
        case Failure(e) => List(
            NOTOK(NifiscalaclientApi.getCurrentMethodName,e)
        )
        case Success(s) => {
          Try(assert(processwithComments.nonEmpty)) match {
            case Success(_) => {
              Try(assert(countAllCasesInCommentsProcessors % 2 == 0)) match {
                case Success(_) => {
                    OK(
                      NifiscalaclientApi.getCurrentMethodName,
                      countAllCasesInCommentsProcessors
                    )
                  checkCaseSchema
                }
                case Failure(_) =>
                    List(
                        NOTOK(
                          NifiscalaclientApi.getCurrentMethodName,
                          countAllCasesInCommentsProcessors
                        )
                    )
              }
            }
            case Failure(_) =>
                  List(
                      NOTOK(
                        processwithComments,processwithComments.length
                    )
                  )
          }
        }
      }

  }
}
