package homecredit.smartdata.nifiscalaclient.api.scheme

case class Position(
                     x: String,
                     y: String
                   )
case class Destination(
                        groupId: String,
                        id: String,
                        `type`: String
                      )
case class Connections(
                        id: String,
                        parentGroupId: String,
                        backPressureDataSizeThreshold: String,
                        backPressureObjectThreshold: String,
                        destination: Destination,
                        flowFileExpiration: String,
                        labelIndex: String,
                        name: String,
                        selectedRelationships: String,
                        source: Destination,
                        zIndex: String
                      )
case class Value(
                  name: String
                )
case class Entry(
                  key: String,
                  value: Value
                )
case class Descriptors(
                        entry: List[Entry]
                      )

case class Properties(
                      topic:String,
                      `bootstrap.servers`:String
                     )
case class Config (
                   bulletinLevel: String,
                   comments: String,
                   concurrentlySchedulableTaskCount: String,
                   descriptors: Object,
                   lossTolerant: String,
                   penaltyDuration: String,
                   properties: Properties,
                   runDurationMillis: String,
                   schedulingPeriod: String,
                   schedulingStrategy: String,
                   yieldDuration: String
                 ){



def getAllComments:String={
    val c = comments
    c
  }
}

case class Relationships(
                          autoTerminate: String,
                          name: String
                        )
case class Processors(
                       id: String,
//                       parentGroupId: String,
//                       position: Position,
                       config: Config
//                       name: String,
//                       relationships: List[Relationships],
//                       style: String,
//                       `type`: String
                     )
case class Contents(
//                     connections: List[Connections],
                     processors: List[Processors]
                   )
case class ProcessGroups(
                          contents: Contents,
                          id: String,
                          parentGroupId: String,
                          position: Position,
                          comments: String,
                          name: String
                        )
case class Snippet(
                    processGroups: ProcessGroups
                  )
case class TemplatesObject(
                            templates: Option[List[Templates]] = None,
                            generated: Option[String] = None
                          )

case class Permissions(
                        canRead: Boolean,
                        canWrite: Boolean
                      )
case class Template(
                     uri: String,
                     id: String,
                     groupId: String,
                     name: String,
                     description: String,
                     timestamp: String,
                     `encoding-version`: String,
                     snippet:Snippet
                   )
case class Templates(
                      id: String,
                      permissions: Permissions,
                      template: Template
                    )

