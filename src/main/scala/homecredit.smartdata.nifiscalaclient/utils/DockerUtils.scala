package homecredit.smartdata.nifiscalaclient.utils
import sys.process._

object DockerUtils extends FileUtils {

  val docComposePath = currentProjPath + "/nifi-integration-tests/docker-compose.yml"

  lazy val dockerGetNifiContainerId:Option[String] = {
    Seq(
      "/bin/sh","-c",
      "docker ps -aq --filter name=nifi-cp"
    ).!.toString
    match {
      case "0" => None
      case v : String => Some(v)
    }
  }

  lazy val dockerDown: this.type = {
    /*
    After delete, this is only for user expirience
     */
    ("docker-compose " +
      s"--file ${docComposePath}" +
      " down").!

    this
  }
  lazy val dockerInstance: this.type = {
    /*
    After delete, this is only for user expirience
     */
    ("docker-compose " +
      s"--file ${docComposePath}" +
      " up -d nifi-cp").!
    this
  }
  lazy val dockerCheckContainerLength: this.type = {
    /*
      After delete, this is only for user expirience
       */

    ("docker-compose "+
      s"--file ${docComposePath}"
      #| Seq("awk","{print split($1,a,\" \")}")).!

    this
  }
  lazy val dockerRemoveProvenance: this.type = {
    /*
      After delete, this is only for user experience
    */

    val dkrGetNifiContainerId = Seq(
      "/bin/sh","-c",
      "docker ps -aq --filter name=nifi-cp"
    )
    val dkrEnterInContainerAndRemoveGzFile = Seq(
      "/bin/sh","-c","xargs -I '{}' docker exec '{}' " +
        "find /opt/nifi/provenance_repository/ -name '*.gz' -delete"
    )

    (
      dkrGetNifiContainerId #|
        dkrEnterInContainerAndRemoveGzFile
      ).!
    this
  }
}
