import homecredit.smartdata.nifiscalaclient.utils.DockerUtils.currentProjPath
import org.scalatest.FlatSpec
import homecredit.smartdata.nifiscalaclient.utils.FileUtils

class FilesTestSpec extends FlatSpec{
    it should "return some project path" in {
      object FileObj extends FileUtils {
        assert(currentProjPath=="/home/akulbasov/Downloads/IdeaProjects/NewsAggregator")
      }
    }

  it should "return string of path if find file docker-compose.yml for nifi integration test" in {
    object FileObj extends FileUtils {
      val docComposePath = currentProjPath + "/nifi-integration-tests/docker-compose.yml"
      assert(filesExist(currentProjPath).get.isInstanceOf[String])
    }
  }

  it should "return none value if dont find file docker-compose.yml for nifi integration test" in {
    object FileObj extends FileUtils {
      val docComposePath = currentProjPath + "someerror/nifi-integration-tests/docker-compose.yml"
      assert(filesExist(currentProjPath).getOrElse("I not find dc") == "I not find dc")
    }
  }

}
