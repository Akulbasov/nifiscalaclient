package homecredit.smartdata.nifiscalaclient.utils
import sys.process._

object DockerUtils {
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
    After delete,  this is only for user expirience
     */
    ("docker-compose " +
      "--file /home/akulbasov/Downloads/nifi-integration-tests/docker-compose.yml" +
      " down").!

    this
  }
  lazy val dockerInstance: this.type = {
    /*
    After delete, this is only for user expirience
     */
    ("docker-compose " +
      "--file /home/akulbasov/Downloads/nifi-integration-tests/docker-compose.yml" +
      " up -d nifi-cp").!
    this
  }
  lazy val dockerCheckContainerLength: this.type = {
    /*
      After delete, this is only for user expirience
       */

    ("docker-compose "+
      "--file /home/akulbasov/Downloads/nifi-integration-tests/docker-compose.yml"
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
