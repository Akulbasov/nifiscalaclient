package homecredit.smartdata.nifiscalaclient.api.scheme

case class Name(
                 clientId: String,
                 version: Double,
                 lastModifier: String
               )
case class Components(
                          name: String,
                          id:String,
                         `type`:String,
                          config: Config
                     )
case class ScheduleComponentsEntityRoot(
                           id: String,
                           state: String,
                           components: Components
                         )

