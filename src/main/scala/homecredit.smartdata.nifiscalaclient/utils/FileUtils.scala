package homecredit.smartdata.nifiscalaclient.utils

import java.io.{File, IOException}
import java.net.URISyntaxException
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

trait FileUtils {

  val currentProjPath = System.getProperty("user.dir")




  def filesExist(str:String): Option[String] ={
    Option(str)
      .getOrElse(return None)
      new File(str).exists match {
       case true => Some(str)
       case false => None
      }
  }

  @throws[IOException]
  @throws[URISyntaxException]
  def readFileFromClasspath(fileName: String): String = {
    new String(Files.readAllBytes(Paths.get(getClass.getClassLoader.getResource(fileName).toURI)), StandardCharsets.UTF_8).mkString("").replaceAll(">  <", "><")
  }

  @throws[IOException]
  @throws[URISyntaxException]
  @throws[ArrayIndexOutOfBoundsException]
  def getListOfAllFilesInResources: Option[Array[File]] = {
    val f = for (file <- new File(getClass.getResource("/").toURI).listFiles;
         if file.isFile;
         if file.toString.endsWith(".xml")
    ) yield {
      file
    }
    if(f.isEmpty)
      None
    else
      Some(f)
  }
}
