package homecredit.smartdata.nifiscalaclient.api.scheme

case class FlowObject(flow: Flow)

case class RevisionObject (version:Integer)

case class ProcessGroupsObject(
                  revision: RevisionObject ,
                  id: String,
                  uri: String,
                  position: Object,
                  permissions: Object,
                  bulletins:List[Bulletins],
                  component:Object,
                  status:Object,
                  versionedFlowSnapshot:Object,
                  runningCount: Integer,
                  stoppedCount: Integer,
                  invalidCount: Integer,
                  inputPortCount: Integer,
                  outputPortCount: Integer
                              )


case class Bulletins(
                    id:Integer,
                    groupId:String,
                    timeStamp:String,
                    bulletin: Bulletin
                    )

case class Bulletin(
                   level:Option[String],
                   message:String,
                   groupId:String
                   )

case class FlowProcessors(
                        bulletins:Option[List[Bulletin]],
                        component: Components
                     )
case class Flow(
                 processGroups: List[ProcessGroupsObject],
                 remoteProcessGroups: List[Object],
                 processors: List[FlowProcessors],
                 inputPorts: List[Object],
                 outputPorts: List[Object],
                 connections: List[Connection],
                 labels: List[Object],
                 funnels: List[Object]
               )
case class Connection(id:String)



