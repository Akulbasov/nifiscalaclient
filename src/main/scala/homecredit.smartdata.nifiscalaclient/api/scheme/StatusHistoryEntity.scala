package homecredit.smartdata.nifiscalaclient.api.scheme

case class StatusHistoryEntityRoot(statusHistory:StatusHistoryEntity)
case class StatusHistoryEntity (
                                  generated:String,
                                  componentDetails: Object,
                                  fieldDescriptors: List[Object],
                                  aggregateSnapshots: List[Object],
                                  nodeSnapshots: List[Object]
                               )

