//package homecredit.smartdata.nifiscalaclient.api

import homecredit.smartdata.nifiscalaclient.NifiFlowTesting
import org.scalatest.FlatSpec

import scala.util.Try

class UpperLevelTest extends FlatSpec {
  it should "success testnifi 1 times" in {
      assert(Try(NifiFlowTesting.startNifiTest()).isSuccess,true)
  }
}
