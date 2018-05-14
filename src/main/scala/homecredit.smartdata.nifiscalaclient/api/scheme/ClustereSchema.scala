package homecredit.smartdata.nifiscalaclient.api.scheme

case class ClustereSchema (
                            cluster:Cluster
                          )

case class Cluster(
                    nodes:List[Node]
                  )

case class Node(nodeId:String)
