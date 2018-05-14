package homecredit.smartdata.nifiscalaclient.api.scheme

case class Case(caseId:Integer,caseValue:Object)
case class TestCasesSchema(cases:List[Case])