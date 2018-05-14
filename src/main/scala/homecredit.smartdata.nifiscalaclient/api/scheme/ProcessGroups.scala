package homecredit.smartdata.nifiscalaclient.api.scheme

/**
  * Contest
  *
  * Represents a contest on Codeforces.
  *
  */

case class ProcessGroup(
                          template: Option[Object],
                          status:Int,
                          statusText:String
                        )


case class ProcessorStatusSnapshot(
                                  id:String,
                                  name:String,
                                  groupId:String,
                                  runStatus:String
                                  )

case class StatusSnapshot(
                          id:String,
                          name:String,
                          processorStatusSnapshots: List[ProcessorStatusSnapshot]
                         )

case class ProcessGroupsStatus(
                              id: String,
                              name:String,
                              nodeSnapshots:List[StatusSnapshot]
                              )
case class ProcessGroupFlow(
                           id:String,
                           uri:String,
                           parentGroupId: String,
                            breadcrumb: Object,
                            flow: Flow,
                            lastRefreshed: String
                           )

case class ProcessGroupFlowEntity(
                                 permissions: Object,
                                 processGroupFlow: ProcessGroupFlow
                                 )