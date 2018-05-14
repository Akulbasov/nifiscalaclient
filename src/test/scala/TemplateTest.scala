import java.io.File

import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi
import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi.{getFlowByProcessGroupId, getListOfAllFilesInResources}
import homecredit.smartdata.nifiscalaclient.api.scheme.Case
import homecredit.smartdata.nifiscalaclient.utils.DockerUtils
import org.scalatest.{FeatureSpec, GivenWhenThen}

import scala.util.Try
class TemplateTest extends FeatureSpec with GivenWhenThen {
//  NifiscalaclientApi.instanceTemplates()
  feature("TemplateTest") {
    def getListOfAllFilesInResources: Array[File] = {
      for (file <- new File(getClass.getResource("/").toURI).listFiles;
           if file.isFile;
           if file.toString.endsWith(".xml")
      ) yield {
        file
      }
    }
    scenario("getClass.getResource(\"/\")"){
      info(getClass.getResource("/").toString)
    }

    scenario("getFlowByProcessGroupId"){
      info("Running method getFlowByProcessGroupId without pgid execute exception with docker down")
      DockerUtils.dockerDown
      intercept[java.lang.Exception]{
        NifiscalaclientApi.getFlowByProcessGroupId.toString
      }
    }

    scenario("getFlowByProcessGroupId with deployTemplatesOnWorkFlow with docker Down"){
      info("Running method getFlowByProcessGroupIdwith deploy deployTemplatesOnWorkFlow")
      DockerUtils.dockerDown
      intercept[java.lang.Exception]{
        NifiscalaclientApi.deployTemplatesOnWorkFlow.getFlowByProcessGroupId.toString
      }
    }

    scenario("NifiscalaclientApi.processwithComments"){
      info("Running method processwithComments without pgid execute exception with dockerdown")
      DockerUtils.dockerDown
      intercept[java.lang.Exception] {
        assert(NifiscalaclientApi.processwithComments.length != 0, true)
      }
    }

    //
    scenario("getFlowByProcessGroupId without deployTemplatesOnWorkFlow with dockerInstance"){
      info("Running method getFlowByProcessGroupId without pgid execute exception with dockerinstance")
      DockerUtils.dockerInstance
      intercept[java.lang.Exception]{
        NifiscalaclientApi.getFlowByProcessGroupId.toString
      }
    }

    scenario("getFlowByProcessGroupId with deployTemplatesOnWorkFlow with dockerInstance"){
      info("Running method getFlowByProcessGroupIdwith deploy deployTemplatesOnWorkFlow")
      DockerUtils.dockerInstance
      intercept[java.lang.Exception]{
        NifiscalaclientApi.deployTemplatesOnWorkFlow.getFlowByProcessGroupId.toString
      }
    }

    scenario("NifiscalaclientApi.processwithComments with deployTemplatesOnWorkFlow with dockerInstance"){
      info("Running method processwithComments without pgid execute exception with dockerdown")
      DockerUtils.dockerInstance
      intercept[java.lang.Exception] {
        assert(NifiscalaclientApi.deployTemplatesOnWorkFlow.processwithComments.length != 0, true)
      }
    }
    //
    scenario("getListOfAllFilesInResources.length!=0"){
      assert(getListOfAllFilesInResources.length!=0)
    }
    scenario("getListOfAllFilesInResources(0)"){
      assert(getListOfAllFilesInResources(0).isInstanceOf[File],true)
    }
    scenario("getListOfAllFilesInResources(0).toString.split(\"/\").last!=0"){
      info(getListOfAllFilesInResources(0).toString.split("/").last)
      assert(getListOfAllFilesInResources(0).toString.split("/").last!=0,true)
    }
    scenario("assert(getListOfAllFilesInResources.isInstanceOf[Array[File]],true)"){
      assert(getListOfAllFilesInResources.isInstanceOf[Array[File]],true)
    }

    scenario("assert(NifiscalaclientApi.file.isInstanceOf[String],true)"){
      assert(NifiscalaclientApi.file.isInstanceOf[String],true)
    }


  }
}
