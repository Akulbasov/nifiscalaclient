package homecredit.smartdata.nifiscalaclient.api

import homecredit.smartdata.nifiscalaclient.api.call.CallImplements
import homecredit.smartdata.nifiscalaclient.api.ping.Ping
import homecredit.smartdata.nifiscalaclient.api.cases.validation.TestCaseValidation
import homecredit.smartdata.nifiscalaclient.api.cases.emulation.TestCaseEmulation
import homecredit.smartdata.nifiscalaclient.http.ScalajHttpClient
import homecredit.smartdata.nifiscalaclient.api.cases.Case
import homecredit.smartdata.nifiscalaclient.api.logging._

import scala.reflect.ClassTag
import scala.collection.mutable.ListBuffer
import homecredit.smartdata.nifiscalaclient.api.response._
import homecredit.smartdata.nifiscalaclient.api.scheme._
import homecredit.smartdata.nifiscalaclient.utils.{ImplicitsUtils, JsonUtils, XmlUtils}

import scala.util.{Failure, Success, Try}



/**
  * NifiScalaClientApi
  *
  * Anonymous (non-authenticated) homecredit.api API
  */


object NifiscalaclientApi extends ScalajHttpClient
  with Ping
  with ImplicitsUtils
  with TestCaseEmulation
  with TestCaseValidation
  with SearchProcessId
  with CallImplements
{


  private val currentHost:String = java.net.InetAddress.getLocalHost.getHostName

  private val nifiHost:String = "nifi"
  private val nifiApiUri:String = s"$nifiHost-api"
  def getCurrentMethodName : String = Thread.currentThread.getStackTrace()(2).getMethodName

  private val port:String = ":8080"
  val apiBaseURL:String = s"http://$currentHost$port/$nifiApiUri/"
  val nifiBaseURL:String = s"http://$currentHost$port/$nifiHost/"
  protected var pgid:ListBuffer[String] = ListBuffer[String]()
  protected var nodesid:ListBuffer[String] = ListBuffer[String]()
  protected var submitProvenanceRequestIdValue:String = ""

  lazy val processwithComments:List[FlowProcessors] =
    getFlowByProcessGroupId.processors.filter(
    pr=>{
      pr.component.config.comments.length != 0
    }
  )

  def file:String = {
    getListOfAllFilesInResources match {
      case Some(x) =>
          nfclogging(OK(getCurrentMethodName,x))
          x(0).toString.split("/").last
      case None =>
        Some(nfclogging(NOTOK(getCurrentMethodName)))
        "file not found"
    }
  }


  def matchingCaseWithContentByProvenanceEventsId(clearNifiState: () => Unit): List[List[ResponseStatus]] = {
      val checkEquality = for {
        pr <- NifiscalaclientApi.processwithComments
      } yield
        //Cycle by process
        for {
          //Cycle by case in process
          componentCase <-
            getAsJson[TestCasesSchema](
              pr.component.config.getAllComments
            ).cases
        } yield {
          Case.validation(
            NifiscalaclientApi.getContentByProvenanceEventsId,
            componentCase,
            pr,
            validateCaseValue
          )
        }
    clearNifiState()
    val checkEqualityInGroup = checkEquality.flatten.flatten.groupBy(p=>p).values.toList
    checkEqualityInGroup
  }



  def getCurrentRootProcessGroupdId: ProcessGroupsObject = {
    getAsJson[ProcessGroupsObject](apiCall(List(
      uUID,Some("process-groups"),Some("root")
    ).flatten.mkString("/"),Map("method"->"GET"),None))
  }

  def getProcessGroupsObject(pgId:String):ProcessGroupsObject = {
    getAsJson[ProcessGroupsObject](apiCall(List(
      uUID,Some("process-groups"),Some(pgId)
    ).flatten.mkString("/"),Map("method"->"GET"),None))
  }



  def emulateKafkaConsumer: this.type = {
    Thread.sleep(6000)
      getFlowByProcessGroupId.processors.filter(
        pr => (pr.component.`type` == "org.apache.nifi.processors.kafka.pubsub.ConsumeKafka"
          && pr.component.config.comments.length != 0)
      ).map(
        pr => {
          topic = pr.component.config.properties.topic
          val comments = pr.component.config.getAllComments
          val testCasesObject = getAsJson[TestCasesSchema](comments)
          testCasesObject.cases.map(
            c => {
              val caseObject = {
                if(toJson(c.caseValue).startsWith("\"")
                  && toJson(c.caseValue).endsWith("\""))
                  // TODO SuperHardCode, try to replace this implementaion ......or not
                  toJson(c.caseValue).replaceAll("\"", "")
                else
                  toJson(c.caseValue)
              }
                Case.emulation[String](caseObject, sendData)
            }
          )
        }
      )
    this
  }


  def emptyAllQueues:this.type = {
    getFlowByProcessGroupId.connections.map {
      idConnection => {
        getAsJson[DropRequestEntity](apiCall(
        List(
        Some("flowfile-queues"),
        Some(idConnection.id),
        Some("drop-requests")
        ).flatten.mkString("/"),Map(
        "method"->"POST"
        ),None
        ))
      }
    }
    println("Successfully deleted all queue")
    this
  }


  def getFlowByProcessGroupId: Flow = {
    if(this.pgid.isEmpty){
      throw new Exception("I did not receive a key " +
        "for a group of processors, " +
        "first of all instance templates"
      )
    }
    val a = getAsJson[ProcessGroupFlowEntity](apiCall(
    List(
      Some("flow"),
      Some("process-groups"),
      Some(this.pgid.head)
    ).flatten.mkString("/"),
    Map(
      "method" -> "GET"
    ),None
    ))
    a.processGroupFlow.flow
  }


  def runProcessGroup: this.type = {
      getAsJson[ProcessGroupsObject](apiCall(
       List(
       Some("flow"),
       Some("process-groups"),
       Some(this.pgid.head)
      ).flatten.mkString("/"),
       Map(
       "method" -> "PUT",
       "mime" -> "application/json"
      ), Some(Map(
          "id" -> this.pgid.head,
          "state" -> "RUNNING"
          )
        )
      ))
    this
  }

  def stopProcessGroup: this.type = {
      Thread.sleep(10000)
      getAsJson[ScheduleComponentsEntityRoot](apiCall(
      List(
        Some("flow"),
        Some("process-groups"),
        Some(this.pgid.head)
      ).flatten.mkString("/"),
      Map(
        "method" -> "PUT",
        "mime" -> "application/json"
      ), Some(Map(
        "id" -> this.pgid.head,
        "state" -> "STOPPED"
      )))
      )

    this
  }


  def deployTemplatesOnWorkFlow: this.type = {
    val f = file
    this.uploadTemplates(
      processGroupId = getCurrentRootProcessGroupdId.id,
      fileName = f
    )
      .instanceTemplates(
        processGroupId = Some(getCurrentRootProcessGroupdId.id)
      )
    Case.validation(processwithComments,validateCaseSchema)
    this
  }


  def instanceTemplates(processGroupId: Option[String] = None): ListBuffer[String] = {
      val cmn = getCurrentMethodName
      getCurrentTemplatesIdList.map {
        idTemplate => {
          Try(
          getAsJson[FlowObject](apiCall(
          List(
          uUID,
          Some("process-groups"),
          processGroupId,
          Some("template-instance")
        ).flatten.mkString("/"),
          Map(
          "method" -> "POST",
          "mime" -> "application/json"
        ), Some(Map(
          "templateId" -> idTemplate,
          "originX" -> "1",
          "originY" -> "1"
        )))
        ).flow.processGroups.foreach {
            pg => {
              setCurrentProcessGroupsId(pg)
            }
          }) match {
            case Success(_) =>
              nfclogging(OK(cmn,processGroupId,idTemplate))
            case Failure(e) =>
                nfclogging(NOTOK(cmn,processGroupId,idTemplate,e))
          }

        }
      }
    this.pgid
  }


  private def setCurrentProcessGroupsId(pg:ProcessGroupsObject): Unit = {
    this.pgid += pg.id
  }



  def getAllIdByTemplates: List[String] ={
    getCurrentTemplatesIdList ++ getCurrentProcessGroupsId
  }

  def getCurrentTemplatesIdList:List[String] = {
    for {t <- getAsCurrentTemplatesClass.templates.get} yield {t.template.id}
  }


  def getCurrentProcessGroupsId:List[String] = {
    for {t <- getAsCurrentTemplatesClass.templates.get} yield {t.template.groupId}
  }

  def getAsCurrentTemplatesClass: TemplatesObject = {
    getAsJson[TemplatesObject](apiCall(List(
      uUID,Some("flow"),Some("templates")
    ).flatten.mkString("/"),Map("method"->"GET"),None))
  }


  def deleteProcessGroupsById():this.type = {
      Thread.sleep(2000)
      this.pgid.map {
        idProcessGroup =>
          getAsJson[ProcessGroupsObject](apiCall(
            (uUID :: Some("process-groups") :: Some(idProcessGroup) :: Nil).flatten.mkString("/"),
            List(
              Some(List(Some("?version"),Some(0)).flatten.mkString("=")),
              Some(List(Some("clientId"),Some("smartdata")).flatten.mkString("="))
            ).flatten.mkString("&"),
            Map(
              "method" -> "DELETE"
            ),None)
          )
      }
    this
  }


  def deleteTemplatesById():this.type = {
      getCurrentTemplatesIdList.map {
        idTemplate =>
          println("This"+idTemplate)
          getAsJson[Templates](apiCall(
              List(
                uUID,
                Some("templates"),
                Some(idTemplate)
              ).flatten.mkString("/"),
              Map(
                "method" -> "DELETE"
              ),None
            )
          )
      }
    this
  }


  def uploadTemplates(processGroupId: String, fileName:String): this.type = {
          getAsXml[ProcessGroup](apiCall(
              List(
                uUID,
                Some("process-groups"),
                Some(processGroupId),
                Some("templates"),
                Some("upload")
              ).flatten.mkString("/"),
              Map(
                "method"->"POST",
                "name"->"template",
                "fileName"->fileName,
                "mime"->"multipart/form-data"
              ),None
            )
          )
        this
      }

  def submitProvenanceRequest:this.type = {
    Thread.sleep(10000)
    val provenanceObject = getAsJson[ProvenanceSchema](
      apiCall(
        List(Some("provenance")).flatten.mkString("/"),
        Map("method"->"POST","mime"->"application/json","stream"->"true"),
        Some(
          Map(
          "provenance" ->
            Map(
            "request" ->
              Map(
                "maxResults"->"1000"
              )
            )
          )
        )
      )
    )
    this.submitProvenanceRequestIdValue = provenanceObject.provenance.id
    this
  }



  def getContentByProvenanceEventsId:List[Events] = {
    val allEvents = for {
        pE <- getProvenanceEvents.provenance.results.provenanceEvents
    } yield {
      Try (
          if(pE.eventType!="DOWNLOAD") {
            if (pE.inputContentAvailable)
              pE.eventContent =
                apiCall(
                  List(
                    Some("provenance-events"),
                    Some(pE.eventId),
                    Some("content"),
                    Some("input")
                  ).flatten.mkString("/"),
                  Map("method" -> "GET"),
                  None
                )
            if (pE.outputContentAvailable)
              pE.eventContent =
                apiCall(
                  List(
                    Some("provenance-events"),
                    Some(pE.eventId),
                    Some("content"),
                    Some("output")
                  ).flatten.mkString("/"),
                  Map("method" -> "GET"),
                  None
                )
          }
      ) match {
        case Success(_) => OK(pE)
        case Failure(e) => NOTOK(pE,e)
      }
      pE
    }
    allEvents
  }


  def getProvenanceEvents:ProvenanceSchema = {
    getAsJson[ProvenanceSchema](
      apiCall(
        List(Some("provenance"),
          Some(this.submitProvenanceRequestIdValue)).flatten.mkString("/"),
        Map("method"->"GET"),
        None
      )
    )
  }

  def deleteProvenanceRequest():this.type={
    getAsJson[Object](
      apiCall(
        List(Some("provenance"),Some(this.submitProvenanceRequestIdValue)).flatten.mkString("/"),
        Map("method"->"DELETE","mime"->"application/json"),
        None
      )
    )
    this
  }

  def toJson(data:Object):String={
    val dataJson = new JsonUtils().toJson(data)
    val Object = {
      if(dataJson.startsWith("\"") && dataJson.endsWith("\""))
      // TODO SuperHardCode, try to replace this implementaion ......or not
        dataJson.replaceAll("\"", "")
      else
        dataJson
    }
    Object
  }


  def getAsXml[T](data:String)(implicit ct:ClassTag[T]): T = {
    XmlUtils.fromXml[T](data)
  }
  def getAsJson[T](data:String,failOnUknownProperties:Boolean=false)(implicit ct:ClassTag[T]): T = {
    new JsonUtils(failOnUknownProperties).fromJson[T](data)
  }

}
