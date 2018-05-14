package homecredit.smartdata.nifiscalaclient.api.logging

import java.io.File

import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi
import homecredit.smartdata.nifiscalaclient.api.response.{NOTOK, OK, ResponseStatus}
import homecredit.smartdata.nifiscalaclient.api.scheme.{Case, Events, FlowProcessors}
import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi.toJson
import homecredit.smartdata.nifiscalaclient.utils.ImplicitsUtils
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.log4j.{Level, Logger}


trait loggingHelper extends ImplicitsUtils {
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("kafka").setLevel(Level.OFF)

  lazy val log = Logger.getLogger(this.getClass.getName)


  def filterByNOT(listRs:List[ResponseStatus]):List[ResponseStatus] = {
      listRs.filter(_.isInstanceOf[NOTOK])
  }

  def filterByNOT(rs:ResponseStatus):List[ResponseStatus] = {
      List(rs).filter(_.isInstanceOf[NOTOK])
  }

}
object nfclogging extends loggingHelper {

  def apply(r : ResponseStatus):ResponseStatus = {
    r match {
      case OK("clearNifiState")  => {
        log.info(
          s"Successfully clearNifiState with \n" +
            s".dockerRemoveProvenance"+
            ".stopProcessGroup"+
            ".deleteProvenanceRequest()"+
            ".emptyAllQueues"+
            ".deleteProcessGroupsById()"+
            ".deleteTemplatesById()"
        )
        OK()
      }

      case NOTOK("clearNifiState",e:Throwable)  => {
        log.error(
          s"Clear nifi state not executed with error:\n ${e}"
        )
        NOTOK("clearNifiState")
      }

      case OK("tryNifiPing")  => {
        log.info(
          s"Ping to nifi correctly executed"
        )
        OK()
      }

      case NOTOK("tryNifiPing",e:Throwable)  => {
        log.error(
          s"Ping not executed, with ${e}"
        )
        NOTOK("tryNifiPing")
      }

      case OK("runNifiState")  => {
        log.info(
          s"Nifi is running state with \n" +
            s".deployTemplatesOnWorkFlow.runProcessGroup.emulateKafkaConsumer.submitProvenanceRequest"
        )
        OK()
      }

      case NOTOK("runNifiState",e:Throwable)  => {
        log.error(
          s"Nifi can't run state, something wrong, with \n${e}"
        )
        NOTOK("runNifiState")
      }

      case OK("localhost",8080,0)  => {
        log.info(
          s"Nifi is ready to host localhost and port 8080"
        )
        OK()
      }

      case NOTOK("localhost",8080,0,e:Throwable)  => {
        log.error(
          s"Nifi not ready to host localhost and port 8080"
        )
        NOTOK()
      }

      case OK(p:Events)  => {
        log.info(
          s"I get provenance events by process ${p.componentId}\n"+
          s"with provenance events type ${p.eventType}\n"+
            s"with provenance content ${p.eventContent}\n" +
            s"with inputContentAvailable ${p.inputContentAvailable}\n" +
            s"with outputContentAvailable ${p.outputContentAvailable}\n"
        )
        OK()
      }

      case NOTOK(p:Events,e:Throwable)  => {
        log.error(
          s"I don't get provenance events by process ${p.componentId}\n"+
            s"with provenance events type ${p.eventType}\n"+
            s"with provenance content ${p.eventContent}\n" +
            s"with inputContentAvailable ${p.inputContentAvailable}\n" +
            s"with outputContentAvailable ${p.outputContentAvailable}\n"
        )
        NOTOK()
      }

      case OK(
          "sendData",
          data:String,
          topic:String
      )  => {
        log.info(
          s"Send record ${data} to topic ${topic}"
        )
        OK()
      }
      case NOTOK(
        "sendData",
        data:String,
        topic:String,
        e:Throwable
      )  => {
        log.error(
            s"Send record ${data} to topic ${topic} with exception ${e.getMessage}"
        )
        NOTOK()
      }
      case OK(
        eventsData:(String,Boolean,Boolean),
        caseData:Case,
        componentsData:FlowProcessors,
        true
      ) => {
        log.info(
          s"Match in process with " +
            s"id ${componentsData.component.id}" +
            s"\n and type ${componentsData.component.`type`}" +
            s"\n and case id ${caseData.caseId}" +
            s"\n and case event data ${toJson(caseData.caseValue)}" +
            s"\n and process event provenance type ${eventsData._1}" +
            s"\n and process event provenance input ${eventsData._2}" +
            s"\n and process event provenance output ${eventsData._3}"
        )
        OK(componentsData.component.id)
      }
      case NOTOK(
        eventsData:(String,Boolean,Boolean),
        caseData:Case,
        componentsData:FlowProcessors,
        false
      )  => {
        log.error(
          s"Not match in process with " +
            s"id ${componentsData.component.id}" +
            s"\n and type ${componentsData.component.`type`}" +
            s"\n and case id ${caseData.caseId}" +
            s"\n and case event data ${toJson(caseData.caseValue)}" +
            s"\n and process event provenance type ${eventsData._1}" +
            s"\n and process event provenance input ${eventsData._2}" +
            s"\n and process event provenance output ${eventsData._3}"
        )
        NOTOK(componentsData.component.id)
      }
      case OK("file",x:Array[File])  => {
        if(x.length==1)
          log.info(
            s"File is found with name ${x.toString} "
          )
        else
          log.info(
            s"I found multiply file with .xml , i get zero file(0) ${x.toString} "
          )

        OK()
      }
      case NOTOK("file")  => {
        log.error(
          "Not found file with extensions .xml"
        )
        NOTOK("file")
      }

      case NOTOK(
          "instanceTemplates",
          processGroupId:Some[String],
          idTemplate:String,
          e:Throwable
      )  => {
        log.error(
          "Not instance template successfully "+
            s"with processGroupId ${processGroupId} "+
            s"with idTemplate ${idTemplate} "+
            s"and error ${e.getMessage} "
        )
        NOTOK()
      }

      case OK("instanceTemplates",processGroupId:Some[String],idTemplate:String)  => {
        log.info(
          "Instance template successfully "+
            s"with processGroupId ${processGroupId.get} "+
            s"with idTemplate ${idTemplate}"
        )
        OK(processGroupId)
      }

      case NOTOK("uploadTemplates",processGroupId:String,fileName:String,e:Throwable)  => {
        log.error(
          "Template not upload successfully "+
            s"with processGroupId ${processGroupId} "+
            s"with fileName ${fileName} "+
            s"and error ${e.getMessage} "
        )
        NOTOK()
      }

      case OK("uploadTemplates",processGroupId:String,fileName:String)  => {
        log.info(
          "Template upload successfully "+
          s"with processGroupId ${processGroupId} "+
          s"with fileName ${fileName}"
        )
        OK()
      }

      case OK("validateCaseSchema",sizecases:Int) => {
        log.info(
          s"Comment's size is ${sizecases}, it's correct for program, even size"
        )
        OK()
      }
      case NOTOK("validateCaseSchema",sizecases:Int) => {
        log.error(
          s"Comment's size is ${sizecases}, it's not correct for program, must be even"
        )
        NOTOK()
      }

      case OK(_:List[FlowProcessors]) => {
        log.info(
          s"I found some process with comments"
        )
        OK()
      }
      case NOTOK(mN:String,e:Throwable) => {
          log.error(e)
          NOTOK()
      }

      case NOTOK(_:List[FlowProcessors],sizepr:Int) => {
          log.error(
            s"I not found some flow process with comments, " +
              s"size of this flow process == ${sizepr}"
          )
        NOTOK()
      }

      case NOTOK(pr:FlowProcessors,e:Throwable) => {
          log.error(
            s"Not matching comment in process " +
            s"${pr.component.id} with case schema " +
            s"with exception body ${e.getMessage}"
          )
          NOTOK()
      }



      case OK(pr:FlowProcessors,c:List[Case]) => {
          log.info(
            s"Matching comment in process ${pr.component.id} " +
              s"with " +
              s"caseId : ${c map(z => z.caseId.toString)} and " +
              s"caseValue : ${c map(z => z.caseValue.toString)}"
          )
          OK()
      }

      case _ => {
        log.warn(s"Not matching nothing with ${r}")
        NOTOK()
      }
    }
  }
}


//case object STATUS extends ResponseStatus
