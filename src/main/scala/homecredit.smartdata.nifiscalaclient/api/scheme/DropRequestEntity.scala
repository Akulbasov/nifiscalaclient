package homecredit.smartdata.nifiscalaclient.api.scheme

case class DropRequestEntity (
              id: String,
              uri: String,
              submissionTime: String,
              lastUpdated: String,
              percentCompleted: Integer,
              finished: Boolean,
              failureReason: String,
              currentCount: Integer,
              currentSize: Integer,
              current: String,
              originalCount: Integer,
              originalSize: Integer,
              original: String,
              droppedCount: Integer,
              droppedSize: Integer,
              dropped: String,
              state: String
                        )
