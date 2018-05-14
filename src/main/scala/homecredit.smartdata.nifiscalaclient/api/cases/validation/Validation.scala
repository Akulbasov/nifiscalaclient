package homecredit.smartdata.nifiscalaclient.api.cases.validation

import homecredit.smartdata.nifiscalaclient.api.scheme.{Case, Events, FlowProcessors}
import homecredit.smartdata.nifiscalaclient.api.response.ResponseStatus
import homecredit.smartdata.nifiscalaclient.utils.ImplicitsUtils

/**
  * HttpClient
  *
  * Provides HTTP request methods (to be backed by Scalaj-Http, Dispatch or mock objects for testing)
  */
//trait Validation extends FeatureSpec with GivenWhenThen  {
trait Validation extends ImplicitsUtils{


    /**
    * request
    *
    * All of the complexity of the whole query system resides here.
    *
    * The parameters passed in [options] are handled as follows:
    *   (id, file: File)       file content is injected as multipart form data.
    *   (id, None)             is ignored (= the parameter is absent)
    *   (id, Some(x))          (given optional parameter) passed as parameter id=x.toString
    *   (id, x)                (anything else) passed as parameter id=x.toString
    */
    /**
      *
  //    * @param requestUrl rew
  //    * @param port rewr
      * @return
      */

  def validateCaseValue(
                         eventsData:List[Events],
                         caseData:Case,
                         componentsData:FlowProcessors
                       ):List[ResponseStatus]
  def validateCaseSchema(processwithComments:List[FlowProcessors]):List[ResponseStatus]
}


