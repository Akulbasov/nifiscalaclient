package homecredit.smartdata.nifiscalaclient.api.scheme

case class Events(
                   var eventCount:Int,
                   componentId:String,
                   componentName:String,
                   eventId:String,
                   eventType:String,
                   var eventContent:Object,
                   inputContentAvailable:Boolean,
                   outputContentAvailable:Boolean,
                   flowFileUuid:String
                 )


case class ProvenanceEvents(provenanceEvents: List[Events])
case class Provenance(id:String,results:ProvenanceEvents)
case class ProvenanceSchema(provenance:Provenance)
