import homecredit.smartdata.nifiscalaclient.api.NifiscalaclientApi
import homecredit.smartdata.nifiscalaclient.utils.DockerUtils
import org.scalatest.{FeatureSpec, GivenWhenThen}

class DockerTest extends FeatureSpec with GivenWhenThen {
  feature("DockerTest") {
    scenario("dockerGetNifiContainerId\n" +
      "checklengthofcontainer"){
      info(DockerUtils.dockerGetNifiContainerId.getOrElse("0"))
//      assert(NifiscalaclientApi.dockerGetNifiContainerId.length>0)
    }
//    scenario("dockerGetNifiContainerId\n" +
//      "checktypofcontainer"){
//      assert(NifiscalaclientApi.dockerGetNifiContainerId.isInstanceOf[String])
//    }
//    scenario("dockerGetNifiContainerId\n" +
//      "OffNifi"){
//      NifiscalaclientApi.dockerDown
//      info(NifiscalaclientApi.dockerGetNifiContainerId)
////      assert(NifiscalaclientApi.dockerGetNifiContainerId.isInstanceOf[String])
//    }
  }
}
