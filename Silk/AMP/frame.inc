[ ] const wMainWindow = AMPHome
[ ] 
[+] window BrowserChild AMPHome
	[ ]  tag "AMP -Home"
	[ ] 
	[ ]  // The URL of this page
	[ ]  const sLocation = "http://amp-staging.code.ro:8080/showLayout.do?layout=login"
	[ ] 
	[ ]  // The login user name
	[ ]  // const sUserName = ?
	[ ] 
	[ ]  // The login password
	[ ]  // const sPassword = ?
	[ ] 
	[ ]  // The size of the browser window
	[ ]  // const POINT BrowserSize = {600, 400}
	[ ] 
	[ ]  // Sets the browser font settings to the default
	[ ]  // const bDefaultFont = TRUE
[ ] 
[+] window BrowserChild AMPHome1
	[ ] tag "AMP -Home"
	[ ] parent Browser
	[-] HtmlTextField SerLogIn
		[-] multitag "&name='j_username'"
			[ ] "User Log In:"
			[ ] "$j_username"
	[-] HtmlTextField Assword
		[-] multitag "&name='j_password'"
			[ ] "Password:"
			[ ] "$j_password"
	[-] HtmlPushButton SignIn
		[-] multitag "&name='submitButton'"
			[ ] "Sign In"
			[ ] "$submitButton"
[ ] 
[+] window BrowserChild AMPShowAmpEvent  // After clicking create new event
	[ ] tag "AMP Show Amp Event"
	[ ] parent Browser
	[+] HtmlTextField EventTitle
		[-] multitag "&id='eventTitle';name='eventTitle'"
			[ ] "Event Title"
			[ ] "$eventTitle"
	[+] HtmlPopupList EventType
		[-] multitag "&name='selectedEventTypeId"
			[ ] "Event Type"
			[ ] "$selectedEventTypeId"
	[+] HtmlLink AddOrganizations
		[-] multitag "Add Organizations"
			[ ] "$?calendar?selectOrganization.do?orgSelReset=true?edit=true"
	[+] HtmlTextField fromDate
		[ ] tag "$selectedStartDate"
	[+] HtmlTextField toDate
		[ ] tag "$selectedEndDate"
	[+] HtmlPopupList startHour
		[ ] tag "&id='selectedStartHour'"
	[-] HtmlPopupList startMinute
		[ ] tag "&id='selectedStartMinute'"
	[-] HtmlPopupList endHour
		[ ] tag "&id='selectedEndHour'"
	[-] HtmlPopupList endMinute
		[ ] tag "&id='selectedEndMinute'"
	[+] HtmlPushButton Preview
		[ ] tag "Preview"
	[-] HtmlList Gregorian
		[ ] tag "Gregorian"
	[+] HtmlListBox Users
		[-] multitag "&id='usersList'"
			[ ] "Users"
	[+] HtmlPushButton AddUsers
		[ ] tag "Add Users >>"
	[+] HtmlTextField Guest
		[-] multitag "&id='guest'"
			[ ] "Guests"
			[ ] "$260"
	[+] HtmlPushButton AddGuest
		[ ] tag "Add Guest"
	[+] HtmlListBox SelectedUsers
		[-] multitag "&id='selectedUsersSel';name='selectedUsers'"
			[ ] "$selectedUsers"
	[+] HtmlPushButton Remove
		[ ] tag "Remove"
	[+] HtmlCheckBox EventIsPrivate
		[-] multitag "&name='privateEvent'"
			[ ] "Event is private"
			[ ] "$privateEvent"
	[+] HtmlPushButton Delete
		[ ] tag "Delete"
[ ] 
[+] window BrowserChild AMPCalendar
	[ ] tag "AMP View Events"
	[ ] parent Browser
	[+] HtmlLink CreateNewEvent 
		[-] multitag "Create New Event"
			[ ] "$?calendar?showCalendarEvent.do~selectedC"
	[ ] 
	[+] HtmlLink NewTestTitle
		[-] multitag "New Test Title"
			[ ] "$http:??amp-demo.code.ro?calendar?showCalendarEvent.do~ampCalend"
	[+] HtmlCheckBox Donor
		[ ] string sTag
		[ ] tag sTag
		[ ] 
		[-] void Select(int no)
			[ ] sTag =" #{no}"
			[ ] printf( "#{no}")
			[ ] 
	[+] HtmlPushButton Show
		[ ] tag "Show"
	[+] HtmlLink NewEventForTest
		[-] multitag "New Event for Test[1]"
			[ ] "$?calendar?showCalendarEvent.do"
	[+] HtmlCheckBox ShowPublicEvents
		[+] multitag "&id='showPublicEvents';name='filter.showPublicEvents'"
			[ ] "Show public events"
			[ ] "$filter.showPublicEvents"
	[+] HtmlTable View
		[+] multitag "&id='tbl'"
			[ ] "$tbl"
		[+] HtmlColumn Yearly
			[ ] tag "Yearly View"
			[-] HtmlLink View
				[-] multitag "Yearly View"
					[ ] "$http:??amp-demo.code.ro?TEMPLATE?ampTemplate?#"
		[+] HtmlColumn Monthly
			[ ] tag "Monthly View"
			[-] HtmlLink View
				[-] multitag "Monthly View"
					[ ] "$http:??amp-demo.code.ro?TEMPLATE?ampTemplate?#"
		[+] HtmlColumn Weekly
			[ ] tag "Weekly View"
			[-] HtmlLink View
				[-] multitag "Weekly View"
					[ ] "$http:??amp-demo.code.ro?TEMPLATE?ampTemplate?#"
		[+] HtmlColumn Daily
			[ ] tag "Daily View"
			[-] HtmlLink View
				[-] multitag "Daily View"
					[ ] "$http:??amp-demo.code.ro?TEMPLATE?ampTemplate?#"
		[+] HtmlColumn Custom
			[ ] tag "Custom View"
			[-] HtmlLink View
				[-] multitag "Custom View"
					[ ] "$http:??amp-demo.code.ro?TEMPLATE?ampTemplate?#"
	[+] HtmlTable Special
		[ ] tag "*2007"
		[-] HtmlColumn HtmlColumn1
			[ ] tag "#1"
			[-] HtmlTable HtmlTable1
				[ ] tag "#1"
				[-] HtmlColumn HtmlColumn2
					[ ] tag "#2"
					[-] HtmlLink NewEventForTest
						[-] multitag "New Event for Test"
							[ ] "$http:??amp-demo.code.ro?calendar?showCalendarEvent.do~ampCalend"
[ ] 
[+] window BrowserChild OrganisationPopup //Add Organization Popup
	[ ] tag "* Organisation Popup"	// should be generic
	[ ] parent Browser
	[+] HtmlPopupList OrganizationType
		[-] multitag "&name='ampOrgTypeId'"
			[ ] "Select Organization type"
			[ ] "$ampOrgTypeId"
	[+] HtmlTextField Keyword
		[-] multitag "&name='keyword'"
			[ ] "Enter a keyword"
			[ ] "$keyword"
	[+] HtmlTextField NumberOfResultsPerPage
		[-] multitag "&id='tempNumResults';name='tempNumResults'"
			[ ] "Number of results per page"
			[ ] "$tempNumResults"
	[+] HtmlPushButton Search
		[ ] tag "Search"
	[-] HtmlCheckBox ResultsCheckBox1
		[-] multitag "&id='chkBox1';name='selOrganisations'"
			[ ] "$selOrganisations[1]"
	[+] HtmlCheckBox ResultsCheckBox2
		[-] multitag "&id='chkBox2';name='selOrganisations'"
			[ ] "$selOrganisations[2]"
	[-] HtmlCheckBox ResultsCheckBox3
		[-] multitag "&id='chkBox3';name='selOrganisations'"
			[ ] "$selOrganisations[3]"
	[-] HtmlPushButton Add
		[ ] tag "Add"
[ ] 
[+] window BrowserChild AMPPreviewAmpEvent
	[ ] tag "AMP Preview Amp Event"
	[ ] parent Browser
	[-] HtmlPushButton Edit
		[ ] tag "Edit"
	[-] HtmlPushButton Save
		[ ] tag "Save"
[ ] 
[+] window BrowserChild AMPDocumentManager
	[ ] tag "AMP - Content Repository"
	[ ] parent Browser
	[+] HtmlTable DM
		[ ] tag "Document Manager"
		[-] HtmlColumn MyDocuments
			[ ] tag "My Documents"
			[+] HtmlPushButton AddFile
				[ ] tag "Add File"
			[+] HtmlText H1
				[-] multitag "&id='H*"
					[ ] "?H?[1]"
					[ ] "$H*"
			[+] HtmlText Plus1
				[-] multitag "&id='plus*"
					[ ] "?+?[1]"
					[ ] "$plus*"
			[+] HtmlText D1
				[-] multitag "&id='D*"
					[ ] "?D?[1]"
					[ ] "$D*"
			[+] HtmlText Pub1
				[-] multitag "&id='Pub*"
					[ ] "?Pub?[1]"
					[ ] "$Pub*"
			[-] HtmlText Priv1
				[-] multitag "&id='Priv*"
					[ ] "?Priv?[1]"
					[ ] "$Priv*"
			[+] HtmlText Del1
				[-] multitag "&id='a*"
					[ ] "?Del?[1]"
					[ ] "$a*"
			[+] HtmlText Test2Doc1
				[-] multitag "&id='yui-dt1-bdrow0-cell0'"
					[ ] "test2.doc"
					[ ] "$yui-dt1-bdrow0-cell0"
			[-] HtmlText TestDoc
				[-] multitag "&id='yui-dt0-bdrow0-cell0'"
					[ ] "test.doc"
					[ ] "$yui-dt0-bdrow0-cell0"
	[+] HtmlPushButton Browse
		[-] multitag "&name='fileData'"
			[ ] "Browse"
			[ ] "$fileData"
	[+] HtmlPushButton Submit
		[ ] tag "Submit"
	[+] HtmlTextField Title
		[-] multitag "&name='docTitle'"
			[ ] "Title:"
			[ ] "$docTitle"
	[+] HtmlTextField Description
		[-] multitag "&name='docDescription'"
			[ ] "Description:"
			[ ] "$docDescription"
	[+] HtmlTable ActionPanel
		[-] multitag "&id='team_table'"
			[ ] "$team_table"
		[ ] 
		[-] HtmlColumn TeamDocuments
			[ ] tag "Team Documents"
			[-] HtmlPushButton AddFile
				[ ] tag "Add File"
	[-] HtmlTable Empty
		[ ] tag "Empty"
		[-] HtmlColumn TeamDocuments
			[ ] tag "Team Documents"
			[+] HtmlText H2
				[-] multitag "&id='H*"
					[ ] "?H?[2]"
					[ ] "$H*"
			[+] HtmlText D2
				[-] multitag "&id='D*"
					[ ] "?D?[2]"
					[ ] "$D*"
			[+] HtmlText Plus2
				[-] multitag "&id='Plus*"
					[ ] "?+?[2]"
					[ ] "$Plus*"
			[+] HtmlText Pub2
				[-] multitag "&id='Pub*"
					[ ] "?Pub?[2]"
					[ ] "$Pub*"
			[-] HtmlText Priv2
				[-] multitag "&id='Priv*"
					[ ] "?Priv?[2]"
					[ ] "$Priv*"
			[+] HtmlText Del2
				[-] multitag "&id='a*"
					[ ] "?Del?[2]"
					[ ] "$a*"
			[+] HtmlText Test2Doc
				[-] multitag "&id='yui-dt0-bdrow1-cell0'"
					[ ] "test2.doc"
					[ ] "$yui-dt0-bdrow1-cell0"
			[-] HtmlPushButton AddFile
				[ ] tag "Add File"
			[-] HtmlText TestDoc
				[-] multitag "&id='yui-dt0-bdrow0-cell0'"
					[ ] "test.doc"
					[ ] "$yui-dt0-bdrow0-cell0"
	[+] HtmlText PleaseSpecifyAFile
		[-] multitag "&id='addDocumentErrorHolderDiv'"
			[ ] "Please specify a file path !"
			[ ] "$addDocumentErrorHolderDiv"
	[ ] 
	[-] HtmlText PleaseSpecifyATitlePleas
		[-] multitag "&id='addDocumentErrorHolderDiv'"
			[ ] "Please specify a title ! Please specify a file path !"
			[ ] "$addDocumentErrorHolderDiv"
	[-] HtmlTextField Path
		[-] multitag "&name='fileData'"
			[ ] "$fileData"
[ ] 
[-] window BrowserChild AMPAddActivityStep1
	[ ] tag "AMP - Add Activity - Identification*"
	[ ] parent Browser
	[+] HtmlTextField ProjectTitle
		[-] multitag "&name='title'"
			[ ] "Project Title"
			[ ] "$title"
	[-] HtmlLink Objective
		[ ] tag "$javascript:edit('aim-obj-*"
	[+] HtmlLink Description
		[ ] tag "$javascript:edit('aim-desc-*"
	[+] HtmlLink Purpose
		[ ] tag "$javascript:edit('aim-purp-*"
	[+] HtmlLink Results
		[ ] tag "$javascript:edit('aim-results-*"
	[+] HtmlLink Lesson
		[ ] tag "$javascript:edit('aim-less-*"
	[+] HtmlLink References
		[-] multitag "References"
			[ ] "$javascript:gotoStep('1_5')"
	[ ] 
	[ ] 
	[+] HtmlLink ObjAddEditObjectivelyVerifIndicators
		[ ] tag "$javascript:commentWin('objObjVerIndicators')"
	[+] HtmlLink ObjAddEditAssumption
		[ ] tag "$javascript:commentWin('objAssumption')"
	[+] HtmlLink ObjAddEditVerification
		[ ] tag "$javascript:commentWin('objVerification')"
	[ ] 
	[+] HtmlLink PurpAddEditObjectivelyVerifIndicators
		[ ] tag "$javascript:commentWin('purpObjVerIndicators')"
	[+] HtmlLink PurpAddEditAssumption
		[ ] tag "$javascript:commentWin('purpAssumption')"
	[+] HtmlLink PurpAddEditVerification
		[ ] tag "$javascript:commentWin('purpVerification')"
	[ ] 
	[+] HtmlLink ResultsAddEditObjectivelyVerifIndicators
		[ ] tag "$javascript:commentWin('resObjVerIndicators')"
	[+] HtmlLink ResultsAddEditAssumption
		[ ] tag "$javascript:commentWin('resAssumption')"
	[+] HtmlLink ResultsAddEditVerification
		[ ] tag "$javascript:commentWin('resVerification')"
	[ ] 
	[ ] 
	[+] HtmlLink Edit2
		[-] multitag "Edit[2]"
			[ ] "$javascript:edit('aim-desc-35-1196422701403')"
	[ ] 
	[+] HtmlPopupList AccessionInstrument
		[-] multitag "&name='accessionInstrument'"
			[ ] "Accession Instrument"
			[ ] "$accessionInstrument"
	[+] HtmlPopupList ACChapter
		[-] multitag "&name='acChapter'"
			[ ] "A.C. Chapter"
			[ ] "$acChapter"
	[+] HtmlCheckBox ActivityIsOnBudget
		[-] multitag "&name='budget'"
			[ ] "Activity is On Budget"
			[ ] "$budget"
	[+] HtmlTextField FY
		[-] multitag "&name='FY'"
			[ ] "FY"
			[ ] "$FY"
	[+] HtmlTextField Vote
		[-] multitag "&name='vote'"
			[ ] "Vote"
			[ ] "$vote"
	[+] HtmlTextField SubVote
		[-] multitag "&name='subVote'"
			[ ] "Sub-Vote"
			[ ] "$subVote"
	[+] HtmlTextField SubProgram
		[-] multitag "&name='subProgram'"
			[ ] "Sub-Program"
			[ ] "$subProgram"
	[+] HtmlTextField ProjectCode
		[-] multitag "&name='projectCode'"
			[ ] "Project Code"
			[ ] "$projectCode"
	[+] HtmlRadioList FinancialInstrument
		[-] multitag "&name='gbsSbs'"
			[ ] "Financial Instrument"
			[ ] "$gbsSbs"
	[+] HtmlRadioList GovApprovalProc
		[-] multitag "&name='governmentApprovalProcedures'"
			[ ] "Yes[1]"
			[ ] "$governmentApprovalProcedures"
	[+] HtmlRadioList JointCriteria
		[-] multitag "&name='jointCriteria'"
			[ ] "Yes[2]"
			[ ] "$jointCriteria"
	[+] HtmlPushButton AddOrganizations
		[-] multitag "&name='addOrgs'"
			[ ] "Add Organizations"
			[ ] "$addOrgs"
	[ ] 
	[+] HtmlTextField Org1Id
		[-] multitag "&name='selectedOrganizations?0?.projectId'"
			[ ] "$selectedOrganizations?0?.projectId"
	[+] HtmlPopupList LineMinistryRank
		[-] multitag "&name='lineMinRank'"
			[ ] "Line Ministry Rank"
			[ ] "$lineMinRank"
	[+] HtmlPopupList MinistryOfPlanningRank
		[-] multitag "&name='planMinRank'"
			[ ] "Ministry of Planning Rank"
			[ ] "$planMinRank"
	[+] HtmlCheckBox SameAsProposedApprovalDate
		[-] multitag "&name='sameAs1'"
			[ ] "Same as Proposed Approval Date"
			[ ] "$sameAs1"
	[+] HtmlTextField ActualApprovalDate
		[-] multitag "&id='revisedAppDate';name='revisedAppDate'"
			[ ] "Actual Approval Date"
			[ ] "$revisedAppDate"
	[+] HtmlTable Today
		[ ] tag "Today"
		[+] HtmlColumn Wed
			[ ] tag "Wed"
			[+] HtmlText N12
				[ ] tag "12"
			[-] HtmlText N9
				[ ] tag "9"
		[+] HtmlColumn Tue
			[ ] tag "Tue"
			[+] HtmlText N4
				[ ] tag "4"
			[-] HtmlText N1
				[ ] tag "1"
		[+] HtmlColumn Thu
			[ ] tag "Thu"
			[-] HtmlText N10
				[ ] tag "10"
	[+] HtmlCheckBox SameAsProposedStartDate
		[-] multitag "&name='sameAs2'"
			[ ] "Same as Proposed Start Date"
			[ ] "$sameAs2"
	[+] HtmlPushButton Comment1
		[ ] tag "Comment[1]"
	[+] HtmlTextField ProposedStartDate
		[-] multitag "&id='originalStartDate';name='originalStartDate'"
			[ ] "Proposed Start Date"
			[ ] "$originalStartDate"
	[+] HtmlImage ClickToViewCalendar7
		[-] multitag "Click to View Calendar[7]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[7]"
	[+] HtmlImage ClickToViewCalendar8
		[-] multitag "Click to View Calendar[8]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[8]"
	[+] HtmlPushButton Comment2
		[ ] tag "Comment[2]"
	[+] HtmlTextField IfThereHaveBeenSomeChange
		[-] multitag "&name='statusReason'"
			[ ] "If there have been some changes in the status, explain below the reasons :"
			[ ] "$statusReason"
	[+] HtmlPopupList StatusId
		[-] multitag "&name='statusId'"
			[ ] "*"
			[ ] "$statusId"
	[ ] 
	[+] HtmlLink Location
		[-] multitag "Location"
			[ ] "$javascript:gotoStep(2)[1]"
	[+] HtmlPushButton Save
		[-] multitag "&name='saveButton'"
			[ ] "Save"
			[ ] "$saveButton"
	[+] HtmlTextField ProjectTitle1
		[-] multitag "&name='title'"
			[ ] "Project Title"
			[ ] "$title"
	[+] HtmlPopupList IfThereHaveBeenSomeChange1
		[-] multitag "&name='statusId'"
			[ ] "If there have been some changes in the status, explain below the reasons :"
			[ ] "$statusId"
	[+] HtmlImage ClickToViewCalendar1
		[-] multitag "Click to View Calendar[1]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[1]"
	[+] HtmlImage ClickToViewCalendar2
		[-] multitag "Click to View Calendar[2]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[2]"
	[+] HtmlImage ClickToViewCalendar3
		[-] multitag "Click to View Calendar[3]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[3]"
	[+] HtmlImage ClickToViewCalendar4
		[-] multitag "Click to View Calendar[4]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[4]"
	[+] HtmlImage ClickToViewCalendar5
		[-] multitag "Click to View Calendar[5]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[5]"
	[-] HtmlImage ClickToViewCalendar6
		[-] multitag "Click to View Calendar[6]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[6]"
[ ] 
[+] window BrowserChild AMPMyDesktop
	[ ] tag "AMP- My Desktop" //
	[ ] parent Browser
	[+] HtmlPushButton AddActivity
		[-] multitag "&name='addActivity'"
			[ ] "Add Activity"
			[ ] "$addActivity[1]"
	[-] HtmlTable CurrentlySelectedFiltersU
		[-] multitag "&id='reportTable'"
			[ ] "Currently Selected Filters:  USD  false"
			[ ] "$reportTable"
		[+] HtmlColumn Status
			[ ] tag "Status"
			[-] HtmlText Ongoing13
				[ ] tag "Ongoing[13]"
[ ] 
[+] window BrowserChild AMPAddActivityStep2
	[ ] tag "AMP - Add Activity - References"
	[ ] parent Browser
	[-] HtmlLink Location
		[-] multitag "Location"
			[ ] "$javascript:gotoStep(3)"
	[ ] 
[ ] 
[+] window BrowserChild AMPSelectOrganizations
	[ ] tag "AMP - Select Organization"
	[ ] parent Browser
	[+] HtmlPushButton Search
		[-] multitag "&name='submitButton'"
			[ ] "Search"
			[ ] "$submitButton[1]"
	[ ] 
	[+] HtmlCheckBox ResultsCheckBox1
		[-] multitag "&id='chkBox1';name='selOrganisations'"
			[ ] "$selOrganisations[1]"
	[ ] 
	[+] HtmlPushButton Add
		[-] multitag "&name='submitButton'"
			[ ] "Add"
		[ ] 
	[ ] 
[ ] 
[+] window BrowserChild AMPAddActivityStep3
	[ ] tag "AMP - Add Activity - Location*"
	[ ] parent Browser
	[+] HtmlLink References
		[-] multitag "References"
			[ ] "$javascript:gotoStep('1_5')"
	[+] HtmlLink Location
		[-] multitag "Location"
			[ ] "$javascript:gotoStep(2)[1]"
	[+] HtmlPopupList ImplementationLevel
		[-] multitag "&name='levelId'"
			[ ] "Implementation Level"
			[ ] "$levelId"
	[+] HtmlPopupList SelectTheAppropriateRegion
		[-] multitag "&name='implemLocationLevel'"
			[ ] "Select the appropriate Region, Zone or Woreda as needed."
			[ ] "$implemLocationLevel"
	[+] HtmlPushButton AddLocation
		[-] multitag "&name='submitButton'"
			[ ] "Add Location"
			[ ] "$submitButton"
	[+] HtmlPushButton Save
		[-] multitag "&name='saveButton'"
			[ ] "Save"
			[ ] "$saveButton"
	[+] HtmlPopupList Region
		[-] multitag "&name='impRegion'"
			[ ] "*"
			[ ] "$impRegion"
	[+] HtmlPopupList Zone
		[-] multitag "&name='impZone'"
			[ ] "*"
			[ ] "$impZone"
	[+] HtmlListBox District
		[-] multitag "&name='impMultiWoreda'"
			[ ] "*"
			[ ] "$impMultiWoreda"
	[+] HtmlPushButton Add
		[ ] tag "Add"
	[+] HtmlPushButton AddSectors
		[ ] tag "Add Sectors"
	[+] HtmlPopupList SelectSectors
		[-] multitag "&name='sectorScheme'"
			[ ] "Sector Scheme"
			[ ] "$sectorScheme"
	[+] HtmlPopupList Sector
		[-] multitag "&name='sectorScheme'"
			[ ] "Sector"
			[ ] "$sector"
	[+] HtmlPopupList SubSectorLevel1
		[-] multitag "&name='subsectorLevel1'"
			[ ] "Sub-Sector Level 1"
			[ ] "$subsectorLevel1"
	[+] HtmlTextField Description
		[-] multitag "&name='programDescription'"
			[ ] "Description"
			[ ] "$programDescription"
	[+] HtmlTable CrossCuttingIssues
		[ ] tag "Cross Cutting Issues"
		[-] HtmlColumn Edit
			[ ] tag "Edit"
			[+] HtmlLink Edit1
				[-] multitag "Edit[1]"
					[ ] "$javascript:edit('aim-eo-58-1197469370844')"
			[+] HtmlLink Edit2
				[-] multitag "Edit[2]"
					[ ] "$javascript:edit('aim-env-58-1197469370844')"
			[+] HtmlLink Edit3
				[-] multitag "Edit[3]"
					[ ] "$javascript:edit('aim-min-58-1197469370844')"
			[ ] 
	[+] HtmlLink Funding
		[-] multitag "Funding"
			[ ] "$javascript:gotoStep(3)"
	[ ] 
[ ] 
[+] window BrowserChild AMPSelectLocation
	[ ] tag "AMP - Select Location"
	[ ] parent Browser
	[+] HtmlPopupList Region
		[-] multitag "&name='impRegion'"
			[ ] "Region"
			[ ] "$impRegion"
	[+] HtmlPopupList Zone
		[-] multitag "&name='impZone'"
			[ ] "*"
			[ ] "$impZone"
	[+] HtmlListBox District
		[-] multitag "&name='impMultiWoreda'"
			[ ] "*"
			[ ] "$impMultiWoreda"
	[+] HtmlPushButton Add
		[ ] tag "Add"
	[+] HtmlListBox Region2
		[-] multitag "&name='impMultiRegion'"
			[ ] "Region"
			[ ] "$impMultiRegion"
	[ ] 
	[ ] 
[ ] 
[+] window BrowserChild AMPAddActor
	[ ] tag "AMP - Add Actor"
	[ ] parent Browser
	[+] HtmlList TopMenu
		[ ] tag "Aid Management Platform (AMP)"
		[+] HtmlLink Calendar
			[-] multitag "::: CALENDAR"
				[ ] "$?calendar?showCalendarView.do"
		[+] HtmlLink Reports
			[ ] 
		[+] HtmlLink Documents
			[ ] 
		[+] HtmlLink ContentRepository
			[-] multitag "::: CONTENT REPOSITORY"
				[ ] "$?contentrepository?documentManager.do"
		[+] HtmlLink MEDashboard
			[ ] 
		[+] HtmlLink Help
			[ ] 
	[+] HtmlPushButton Add1
		[ ] tag "Add"
	[+] HtmlTextField Actor
		[-] multitag "&name='actor'"
			[ ] "Actor"
			[ ] "$actor"
	[ ] 
[ ] 
[+] window BrowserChild AMPAddSectors
	[ ] tag "AMP - Select Sector"
	[ ] parent Browser
	[+] HtmlPopupList SelectSectors
		[-] multitag "&name='sectorScheme'"
			[ ] "Sector Scheme"
			[ ] "$sectorScheme"
	[+] HtmlPopupList Sector
		[-] multitag "&name='sector'"
			[ ] "Sector"
			[ ] "$sector"
	[+] HtmlPopupList SubSectorLevel1
		[-] multitag "&name='subsectorLevel1'"
			[ ] "Sub-Sector Level 1"
			[ ] "$subsectorLevel1"
	[+] HtmlPushButton Add
		[ ] tag "Add"
	[ ] 
	[+] HtmlPopupList SelectSectors1
		[-] multitag "&name='sectorScheme'"
			[ ] "Select Sectors"
			[ ] "$sectorScheme"
	[-] HtmlPopupList Sector1
		[-] multitag "&name='sector'"
			[ ] "Sector"
			[ ] "$sector"
[ ] 
[+] window BrowserChild AMPEditActivity
	[ ] tag "AMP - Edit Activity"
	[ ] parent Browser
	[+] HtmlText CanadianDollarCFAFrancBCEA
		[ ] tag "Canadian Dollar CFA Franc BCEAO Ethiopian Birr Euro French Franc Indian Rupee Japanese Yen Pound Sterling Unit of Account(UA) *"
	[-] HtmlTable Today
		[ ] tag "Today"
		[+] HtmlColumn Sat
			[ ] tag "Sat"
			[+] HtmlText N1
				[ ] tag "1"
			[+] HtmlText N8
				[ ] tag "8"
			[+] HtmlText N19
				[ ] tag "19"
			[+] HtmlText N12
				[ ] tag "12"
			[-] HtmlText N5
				[ ] tag "5"
		[+] HtmlColumn Fri
			[ ] tag "Fri"
			[-] HtmlText N21
				[ ] tag "21"
		[+] HtmlColumn Sun
			[ ] tag "Sun"
			[+] HtmlText N2
				[ ] tag "2"
			[-] HtmlText N20
				[ ] tag "20"
		[+] HtmlColumn Wed
			[ ] tag "Wed"
			[+] HtmlText N9
				[ ] tag "9"
			[+] HtmlText N23
				[ ] tag "23"
			[-] HtmlText N16
				[ ] tag "16"
		[+] HtmlColumn Thu
			[ ] tag "Thu"
			[+] HtmlText N17
				[ ] tag "17"
			[+] HtmlText N10
				[ ] tag "10"
			[-] HtmlText N3
				[ ] tag "3"
	[+] HtmlCheckBox Currency2
		[-] multitag "&name='deleteContrib'"
			[ ] "Currency[2]"
			[ ] "$deleteContrib[2]"
	[-] HtmlPushButton DeleteSelected
		[-] multitag "&name='removeFields'"
			[ ] "Delete Selected"
			[ ] "$removeFields"
[ ] 
[+] window BrowserChild AMPAddActivityStep4
	[ ] tag "AMP - Add Activity - Funding"
	[ ] parent Browser
	[+] HtmlPushButton AddOrganizations
		[-] multitag "&name='submitButton'"
			[ ] "Add Organizations"
			[ ] "$submitButton"
	[+] HtmlPushButton AddFunding
		[ ] tag "Add Funding"
	[+] HtmlPopupList AssistanceType
		[-] multitag "&name='assistanceType'"
			[ ] "*"
			[ ] "$assistanceType"
	[+] HtmlPopupList Modality
		[-] multitag "&name='modality'"
			[ ] "*"
			[ ] "$modality"
	[+] HtmlTextField FundingOrganizationId
		[-] multitag "&name='orgFundingId'"
			[ ] "Funding Organization Id"
			[ ] "$orgFundingId"
	[+] HtmlPushButton AddProjection
		[-] multitag "&name='addMTEFProj'"
			[ ] "Add Projection"
			[ ] "$addMTEFProj"
	[+] HtmlTextField Pipeline1
		[-] multitag "&name='mtefProjection?0?.amount'"
			[ ] "pipeline[1]"
			[ ] "$mtefProjection?0?.amount"
	[+] HtmlPopupList ProjectedPipeline1
		[+] multitag "Projected?Pipeline[1]"
			[ ] "$mtefProjection?0?.projected"
			[ ] "&name='mtefProjection?0?.projected'"
	[+] HtmlPopupList Currency
		[-] multitag "&name='mtefProjection?0?.currencyCode'"
			[ ] "*"
			[ ] "$mtefProjection?0?.currencyCode"
	[+] HtmlLink Components
		[-] multitag "Components"
			[ ] "$javascript:gotoStep(5)"
	[ ] 
	[ ] 
	[ ] 
	[+] HtmlPushButton AddFundings
		[ ] tag "Add Fundings"
	[+] HtmlLink RegionalFunding
		[-] multitag "Regional Funding"
			[ ] "$javascript:gotoStep(4)"
	[-] HtmlPushButton Save
		[-] multitag "&name='saveButton'"
			[ ] "Save"
			[ ] "$saveButton"
[ ] 
[+] window BrowserChild AMPAddActivityStep5
	[ ] tag "AMP - Add Activity - Components"
	[ ] parent Browser
	[+] HtmlPushButton AddComponent
		[-] multitag "&name='submitButton'"
			[ ] "Add Component"
			[ ] "$submitButton[1]"
	[ ] 
	[+] HtmlLink AddPhysicalProgress
		[-] multitag "Add Physical Progress"
			[ ] "$javascript:addPhyProgess(-1,1197642794828)"
	[+] HtmlPushButton AddIssues
		[-] multitag "&name='submitButton'"
			[ ] "Add Issues"
			[ ] "$submitButton[3]"
	[+] HtmlLink AddMeasures
		[-] multitag "Add Measures"
			[ ] "$javascript:addMeasures('1197643686688')"
	[+] HtmlLink RelatedDocuments
		[-] multitag "Related Documents"
			[ ] "$javascript:gotoStep(6)"
	[+] HtmlLink Edit
		[-] multitag "Edit"
			[ ] "$javascript:editFunding('1198085054850')"
	[+] HtmlLink AddActors
		[-] multitag "Add Actors"
			[ ] "$javascript:addActors('1198264844266','1198264883141')"
	[-] HtmlLink ME
		[-] multitag "M  E"
			[ ] "$javascript:gotoStep(10)"
[ ] 
[+] window BrowserChild AMPAddPhysicalProgress
	[ ] tag "AMP - Add Physical Progress"
	[ ] parent Browser
	[+] HtmlTextField Title
		[-] multitag "&name='phyProgTitle'"
			[ ] "Title"
			[ ] "$phyProgTitle"
	[+] HtmlTextField Description
		[-] multitag "&name='phyProgDesc'"
			[ ] "Description"
			[ ] "$phyProgDesc"
	[+] HtmlPushButton Add
		[ ] tag "Add"
	[ ] 
	[+] HtmlImage ClickToViewCalendar
		[-] multitag "Click to View Calendar"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho"
	[-] HtmlTable Today
		[ ] tag "Today"
		[+] HtmlColumn Tue
			[ ] tag "Tue"
			[-] HtmlText N1
				[ ] tag "1"
		[+] HtmlColumn Wed
			[ ] tag "Wed"
			[+] HtmlText N2
				[ ] tag "2"
			[-] HtmlText N9
				[ ] tag "9"
		[+] HtmlColumn Thu
			[ ] tag "Thu"
			[-] HtmlText N10
				[ ] tag "10"
[ ] 
[+] window BrowserChild AMPAddComponents
	[ ] tag "AMP - Add Components"
	[ ] parent Browser
	[+] HtmlPopupList Component
		[-] multitag "&name='componentTitle'"
			[ ] "Add Component"
			[ ] "$componentTitle"
	[-] HtmlLink AddCommitments
		[-] multitag "Add[1]"
			[ ] "$javascript:addCommitments()"
	[-] HtmlLink AddExpenditure
		[-] multitag "Add*"
			[ ] "$javascript:addExpenditure()"
	[-] HtmlLink AddDisbursement
		[-] multitag "Add*"
			[ ] "$javascript:addDisbursement()"
	[+] HtmlPopupList PlannedActual1
		[-] multitag "&name='comm_0_1'"
			[ ] "Planned?Actual*"
			[ ] "$comm_0_1"
	[+] HtmlTextField Planned
		[-] multitag "&name='comm_0_2'"
			[ ] "Planned[1]"
			[ ] "$comm_0_2"
	[+] HtmlPopupList Currency
		[-] multitag "&name='comm_0_3'"
			[ ] "Commitments - (Total Actual Allocation 0.0 null )"
			[ ] "$comm_0_3"
	[+] HtmlPopupList PlannedActual2
		[-] multitag "&name='disb_0_1'"
			[ ] "Planned?Actual*"
			[ ] "$disb_0_1"
	[+] HtmlTextField Planned2
		[-] multitag "&name='disb_0_2'"
			[ ] "Planned[2]"
			[ ] "$disb_0_2"
	[+] HtmlPopupList Currency2
		[-] multitag "&name='disb_0_3'"
			[ ] "Disbursement - (Total Actual Allocation 0.0 null )"
			[ ] "$disb_0_3"
	[+] HtmlPopupList PlannedActual3
		[-] multitag "&name='expn_0_1'"
			[ ] "Planned?Actual*"
			[ ] "$expn_0_1"
	[+] HtmlTextField Planned3
		[-] multitag "&name='expn_0_2'"
			[ ] "Planned[3]"
			[ ] "$expn_0_2"
	[+] HtmlPopupList Currency3
		[-] multitag "&name='expn_0_3'"
			[ ] "Expenditure - (Total Actual Allocation 0.0 null )"
			[ ] "$expn_0_3"
	[+] HtmlPushButton Save
		[ ] tag "Save"
	[ ] 
	[ ] 
	[+] HtmlImage ClickToViewCalendar
		[-] multitag "Click to View Calendar"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho"
	[-] HtmlTable Add
		[ ] tag "Add"
		[+] HtmlColumn Fri
			[ ] tag "Fri"
			[+] HtmlText N7
				[ ] tag "7"
			[+] HtmlText N14
				[ ] tag "14"
			[-] HtmlText N4
				[ ] tag "4"
		[+] HtmlColumn Sun
			[ ] tag "Sun"
			[-] HtmlText N30
				[ ] tag "30"
		[+] HtmlColumn Sat
			[ ] tag "Sat"
			[-] HtmlText N22
				[ ] tag "22"
		[+] HtmlColumn Thu
			[ ] tag "Thu"
			[-] HtmlText N10
				[ ] tag "10"
	[+] HtmlImage ClickToViewCalendar2
		[-] multitag "Click to View Calendar[2]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[2]"
	[-] HtmlImage ClickToViewCalendar3
		[-] multitag "Click to View Calendar[3]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[3]"
[ ] 
[+] window BrowserChild AMPAddIssue
	[ ] tag "AMP - Add Issue"
	[ ] parent Browser
	[+] HtmlTextField Issue
		[-] multitag "&name='issue'"
			[ ] "Issue"
			[ ] "$issue"
	[+] HtmlPushButton Add
		[ ] tag "Add"
	[ ] 
[ ] 
[+] window BrowserChild AMPAddMeasure
	[ ] tag "AMP - Add Measure"
	[ ] parent Browser
	[+] HtmlTextField Measure
		[-] multitag "&name='measure'"
			[ ] "Measure"
			[ ] "$measure"
	[+] HtmlPushButton Add
		[ ] tag "Add"
	[ ] 
[ ] 
[+] window BrowserChild AMPAddFunding
	[ ] tag "AMP - Add Funding"
	[ ] parent Browser
	[+] HtmlPopupList AssistanceType
		[-] multitag "&name='assistanceType'"
			[ ] "11111111"
			[ ] "$assistanceType"
	[+] HtmlPopupList Modality
		[-] multitag "&name='modality'"
			[ ] "? Financing Instrument*"
			[ ] "$modality"
	[+] HtmlTextField FundingOrganizationId
		[-] multitag "&name='orgFundingId'"
			[ ] "Funding Organization Id"
			[ ] "$orgFundingId"
	[+] HtmlPushButton AddProjection
		[-] multitag "&name='addMTEFProj'"
			[ ] "Add Projection"
			[ ] "$addMTEFProj"
	[+] HtmlTextField Pipeline1
		[-] multitag "&name='mtefProjection?0?.amount'"
			[ ] "pipeline[1]"
			[ ] "$mtefProjection?0?.amount"
	[+] HtmlPopupList Currency
		[-] multitag "&name='mtefProjection?0?.currencyCode'"
			[ ] "*"
			[ ] "$mtefProjection?0?.currencyCode"
	[+] HtmlPopupList ProjectedPipeline
		[-] multitag "&name='mtefProjection?0?.projected'"
			[ ] "Projected?Pipeline[1]"
			[ ] "$mtefProjection?0?.projected"
	[+] HtmlPushButton AddCommitment
		[-] multitag "&name='xx1'"
			[ ] "Add Commitment"
			[ ] "$xx1"
	[+] HtmlPopupList PlannedActual
		[-] multitag "&name='fundingDetail?0?.adjustmentType'"
			[ ] "Planned?Actual*"
			[ ] "$fundingDetail?0?.adjustmentType"
	[-] HtmlTextField Planned
		[-] multitag "&name='fundingDetail?0?.transactionAmount'"
			[ ] "Planned"
			[ ] "$fundingDetail?0?.transactionAmount"
	[+] HtmlPopupList Currency2
		[-] multitag "&name='fundingDetail?0?.currencyCode'"
			[ ] "Currency"
			[ ] "$fundingDetail?0?.currencyCode"
	[+] HtmlImage ClickToViewCalendar
		[-] multitag "Click to View Calendar"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho"
	[+] HtmlPushButton AddDisbursement
		[-] multitag "&name='xx1'"
			[ ] "Add Disbursement"
			[ ] "$xx1"
	[-] HtmlTextField Planned2
		[-] multitag "&name='fundingDetail?1?.transactionAmount'"
			[ ] "Planned"
			[ ] "$fundingDetail?1?.transactionAmount"
	[+] HtmlPopupList PlannedActual2
		[-] multitag "&name='fundingDetail?1?.adjustmentType'"
			[ ] "Planned?Actual*"
			[ ] "$fundingDetail?1?.adjustmentType"
	[+] HtmlPopupList Currency3
		[-] multitag "&name='fundingDetail?1?.currencyCode'"
			[ ] "Currency"
			[ ] "$fundingDetail?1?.currencyCode"
	[+] HtmlPushButton AddExpenditure
		[-] multitag "&name='xx1'"
			[ ] "Add Expenditure"
			[ ] "$xx1"
	[+] HtmlPopupList PlannedActual3
		[-] multitag "&name='fundingDetail?2?.adjustmentType'"
			[ ] "Planned?Actual*"
			[ ] "$fundingDetail?2?.adjustmentType"
	[+] HtmlTextField Planned3
		[-] multitag "&name='fundingDetail?2?.transactionAmount'"
			[ ] "Planned"
			[ ] "$fundingDetail?2?.transactionAmount"
	[+] HtmlPopupList Currency4
		[-] multitag "&name='fundingDetail?2?.currencyCode'"
			[ ] "Currency"
			[ ] "$fundingDetail?3?.currencyCode"
	[+] HtmlTextField Classification
		[-] multitag "&name='fundingDetail?*?.classification'"
			[ ] "Classification"
			[ ] "$fundingDetail?*?.classification"
	[+] HtmlTextField Conditions
		[-] multitag "&name='fundingConditions'"
			[ ] "Conditions"
			[ ] "$fundingConditions"
	[+] HtmlPushButton Save
		[ ] tag "Save"
	[+] HtmlTable HtmlTable1
		[ ] tag "#1"
		[+] HtmlColumn Commitments
			[ ] tag "Commitments"
			[+] HtmlPushButton AddCommitment
				[-] multitag "&name='xx1'"
					[ ] "Add Commitment"
					[ ] "$xx1"
			[+] HtmlTextField Planned
				[-] multitag "&name='fundingDetail?0?.transactionAmount'"
					[ ] "Planned"
					[ ] "$fundingDetail?0?.transactionAmount"
			[+] HtmlPopupList Currency
				[-] multitag "&name='fundingDetail?0?.currencyCode'"
					[ ] "Currency"
					[ ] "$fundingDetail?0?.currencyCode"
			[-] HtmlImage ClickToViewCalendar
				[-] multitag "Click to View Calendar"
					[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho"
	[+] HtmlTable Today
		[ ] tag "Today"
		[+] HtmlColumn Fri
			[ ] tag "Fri"
			[+] HtmlText N7
				[ ] tag "7"
			[+] HtmlText N21
				[ ] tag "21"
			[+] HtmlText N4
				[ ] tag "4"
			[+] HtmlText N11
				[ ] tag "11"
			[-] HtmlText N18
				[ ] tag "18"
		[+] HtmlColumn Thu
			[ ] tag "Thu"
			[+] HtmlText N3
				[ ] tag "3"
			[+] HtmlText N10
				[ ] tag "10"
			[-] HtmlText N17
				[ ] tag "17"
		[+] HtmlColumn Tue
			[ ] tag "Tue"
			[+] HtmlText N1
				[ ] tag "1"
			[-] HtmlText N29
				[ ] tag "29"
		[+] HtmlColumn Wed
			[ ] tag "Wed"
			[+] HtmlText N16
				[ ] tag "16"
			[+] HtmlText N9
				[ ] tag "9"
			[-] HtmlText N2
				[ ] tag "2"
	[+] HtmlTable HtmlTable2
		[ ] tag "#2"
		[+] HtmlColumn Expenditures
			[ ] tag "Expenditures"
			[+] HtmlPushButton AddExpenditure
				[-] multitag "&name='xx1'"
					[ ] "Add Expenditure"
					[ ] "$xx1"
			[+] HtmlTextField Planned
				[-] multitag "&name='fundingDetail?2?.transactionAmount'"
					[ ] "Planned"
					[ ] "$fundingDetail?2?.transactionAmount"
			[+] HtmlPopupList Currency
				[-] multitag "&name='fundingDetail?2?.currencyCode'"
					[ ] "Currency"
					[ ] "$fundingDetail?2?.currencyCode"
			[-] HtmlImage ClickToViewCalendar
				[-] multitag "Click to View Calendar"
					[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho"
[ ] 
[+] window BrowserChild AMPRegionalFunding
	[ ] tag "AMP - Add Activity - Regional Funding"
	[ ] parent Browser
	[+] HtmlPushButton AddFundings
		[-] multitag "&name='submitButton'"
			[ ] "Add Fundings"
			[ ] "$submitButton"
	[+] HtmlLink EditThisFunding
		[-] multitag "Edit this funding"
			[ ] "$javascript:editFunding('3')"
	[-] HtmlLink Components
		[-] multitag "Components"
			[ ] "$javascript:gotoStep(5)[1]"
[ ] 
[+] window BrowserChild AMPAddRegionalFunding
	[ ] tag "AMP - Add Regional Funding"
	[ ] parent Browser
	[+] HtmlPopupList Region
		[-] multitag "&name='fundingRegionId'"
			[ ] "Region"
			[ ] "$fundingRegionId"
	[+] HtmlLink Add1
		[-] multitag "Add[1]"
			[ ] "$javascript:addCommitments()"
	[+] HtmlPopupList PlannedActualAmount
		[-] multitag "&name='comm_0_1'"
			[ ] "Planned?Actual    Amount                 Currency                        Date"
			[ ] "$comm_0_1"
	[+] HtmlTextField Planned
		[-] multitag "&name='comm_0_2'"
			[ ] "Planned"
			[ ] "$comm_0_2"
	[+] HtmlPopupList CommitmentsTotalActualA
		[-] multitag "&name='comm_0_3'"
			[ ] "Commitments - ( Total Actual Allocation 0.0 null )"
			[ ] "$comm_0_3"
	[+] HtmlImage ClickToViewCalendar
		[-] multitag "Click to View Calendar"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho"
	[+] HtmlTable Add
		[ ] tag "Add"
		[+] HtmlColumn Fri
			[ ] tag "Fri"
			[+] HtmlText N7
				[ ] tag "7"
			[+] HtmlText N14
				[ ] tag "14"
			[-] HtmlText N21
				[ ] tag "21"
		[+] HtmlColumn Thu
			[ ] tag "Thu"
			[+] HtmlText N13
				[ ] tag "13"
			[+] HtmlText N20
				[ ] tag "20"
			[+] HtmlText N6
				[ ] tag "6"
			[+] HtmlText N17
				[ ] tag "17"
			[-] HtmlText N10
				[ ] tag "10"
		[+] HtmlColumn Sat
			[ ] tag "Sat"
			[-] HtmlText N22
				[ ] tag "22"
		[+] HtmlColumn Wed
			[ ] tag "Wed"
			[+] HtmlText N16
				[ ] tag "16"
			[+] HtmlText N9
				[ ] tag "9"
			[-] HtmlText N2
				[ ] tag "2"
	[+] HtmlPushButton Save
		[ ] tag "Save"
	[+] HtmlLink Add2
		[-] multitag "Add[2]"
			[ ] "$javascript:addDisbursement()"
	[+] HtmlPopupList PlannedActualAmount1
		[-] multitag "&name='disb_0_1'"
			[ ] "Planned?Actual    Amount                 Currency                        Date[2]"
			[ ] "$disb_0_1"
	[+] HtmlTextField Planned2
		[-] multitag "&name='disb_0_2'"
			[ ] "Planned[2]"
			[ ] "$disb_0_2"
	[+] HtmlPopupList DisbursementTotalActual
		[-] multitag "&name='disb_0_3'"
			[ ] "Disbursement - ( Total actual to date 0.0 null)"
			[ ] "$disb_0_3"
	[+] HtmlImage ClickToViewCalendar2
		[-] multitag "Click to View Calendar[2]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[2]"
	[+] HtmlLink Add3
		[-] multitag "Add[3]"
			[ ] "$javascript:addExpenditure()"
	[+] HtmlPopupList PlannedActualAmount2
		[-] multitag "&name='expn_0_1'"
			[ ] "Planned?Actual    Amount                 Currency                        Date[3]"
			[ ] "$expn_0_1"
	[+] HtmlTextField Planned3
		[-] multitag "&name='expn_0_2'"
			[ ] "Planned[3]"
			[ ] "$expn_0_2"
	[+] HtmlPopupList ExpenditureTotalActualT
		[-] multitag "&name='expn_0_3'"
			[ ] "Expenditure - ( Total actual to date 0.0 null )"
			[ ] "$expn_0_3"
	[+] HtmlImage ClickToViewCalendar3
		[-] multitag "Click to View Calendar[3]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[3]"
[ ] 
[+] window BrowserChild AMPAddActivityStep6
	[ ] tag "AMP - Add Activity - Related Documents"
	[ ] parent Browser
	[+] HtmlPushButton AddDocumentsFromRepository
		[-] multitag "&name='submitButton'"
			[ ] "Add Documents From Repository"
			[ ] "$submitButton[1]"
	[+] HtmlPushButton AddDocuments
		[-] multitag "&name='submitButton'"
			[ ] "Add Documents"
			[ ] "$submitButton[2]"
	[+] HtmlPushButton AddWebResources
		[-] multitag "&name='submitButton'"
			[ ] "Add Web Resources"
			[ ] "$submitButton[4]"
	[+] HtmlLink RelatedOrganizations
		[-] multitag "Related Organizations"
			[ ] "$javascript:gotoStep(7)"
	[ ] 
	[-] HtmlLink ME
		[-] multitag "M  E"
			[ ] "$javascript:gotoStep(10)"
[ ] 
[+] window BrowserChild AMPAddDocuments
	[ ] tag "AMP - Add Documents and Links"
	[ ] parent Browser
	[+] HtmlTextField Title
		[-] multitag "&name='docTitle'"
			[ ] "Title"
			[ ] "$docTitle"
	[+] HtmlTextField Description
		[-] multitag "&name='docDescription'"
			[ ] "Description"
			[ ] "$docDescription"
	[+] HtmlTextField Comments
		[-] multitag "&name='docComment'"
			[ ] "Comments"
			[ ] "$docComment"
	[+] HtmlImage ClickToViewCalendar
		[-] multitag "Click to View Calendar"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho"
	[+] HtmlTable Today
		[ ] tag "Today"
		[+] HtmlColumn Tue
			[ ] tag "Tue"
			[-] HtmlText N11
				[ ] tag "11"
		[+] HtmlColumn Wed
			[ ] tag "Wed"
			[+] HtmlText N12
				[ ] tag "12"
			[-] HtmlText N5
				[ ] tag "5"
		[+] HtmlColumn Fri
			[ ] tag "Fri"
			[+] HtmlText N7
				[ ] tag "7"
			[-] HtmlText N14
				[ ] tag "14"
		[+] HtmlColumn Sat
			[ ] tag "Sat"
			[-] HtmlText N15
				[ ] tag "15"
	[+] HtmlPushButton Add
		[ ] tag "Add"
	[+] HtmlPushButton Browse
		[-] multitag "&name='&id='docFile';name='docFile''"
			[ ] "Browse*"
			[ ] "$docFile"
	[+] HtmlTextField WebResources
		[-] multitag "&name='docWebResource'"
			[ ] "Web resources"
			[ ] "$docWebResource"
	[+] HtmlTextField File2
		[-] multitag "&id='docFile';name='docFile'"
			[ ] "File[2]"
			[ ] "$docFile"
[ ] 
[+] window BrowserChild AMPAddDocumentsFromRepository
	[ ] tag "Popup"
	[ ] parent Browser
	[+] HtmlTable HtmlTable1
		[ ] tag "&name='framingTable'"
		[+] HtmlColumn SelectSelectDocumen
			[ ] tag "Select          Select Documents"
			[+] HtmlPushButton Select
				[-] multitag "&id='otherDocumentsMenu0';name='otherDocumentsButton'"
					[ ] "Select"
					[ ] "$otherDocumentsButton"
			[+] HtmlText Examen_krDoc
				[-] multitag "&id='yui-dt0-bdrow0-cell0'"
					[ ] "*"
					[ ] "$yui-dt0-bdrow0-cell0"
	[+] HtmlList Empty
		[ ] tag "Empty"
		[-] HtmlLink TeamDocuments
			[+] multitag "Team Documents"
				[ ] "$?TEMPLATE?ampTemplate?#[2]"
	[+] HtmlPushButton Submit
		[ ] tag "Submit"
	[ ] 
[ ] 
[+] window BrowserChild AMPAddActivityStep7
	[ ] tag "AMP - Add Activity - Related Organizations"
	[ ] parent Browser
	[+] HtmlPushButton AddOrganizations1
		[-] multitag "&name='submitButton'"
			[ ] "Add Organizations[1]"
			[ ] "$submitButton[1]"
	[+] HtmlPushButton AddOrganizations2
		[-] multitag "&name='submitButton'"
			[ ] "Add Organizations[2]"
			[ ] "$submitButton[3]"
	[+] HtmlPushButton AddOrganizations3
		[-] multitag "&name='submitButton'"
			[ ] "Add Organizations[3]"
			[ ] "$submitButton[5]"
	[+] HtmlPushButton AddOrganizations4
		[-] multitag "&name='submitButton'"
			[ ] "Add Organizations[4]"
			[ ] "$submitButton[7]"
	[+] HtmlPushButton AddOrganizations5
		[-] multitag "&name='submitButton'"
			[ ] "Add Organizations[5]"
			[ ] "$submitButton[9]"
	[+] HtmlPushButton AddOrganizations6
		[-] multitag "&name='submitButton'"
			[ ] "Add Organizations[6]"
			[ ] "$submitButton[11]"
	[+] HtmlLink RelatedOrganizations
		[-] multitag "Contact Information"
			[ ] "$javascript:gotoStep(8)"
	[ ] 
[ ] 
[+] window BrowserChild AMPAddActivityStep9
	[ ] tag "AMP - Add Activity - Contact Details*"
	[ ] parent Browser
	[+] HtmlTextField FirstName1
		[-] multitag "&name='dnrCntFirstName'"
			[ ] "First Name[1]"
			[ ] "$dnrCntFirstName"
	[+] HtmlTextField LastName1
		[-] multitag "&name='dnrCntLastName'"
			[ ] "Last Name[1]"
			[ ] "$dnrCntLastName"
	[+] HtmlTextField Email1
		[-] multitag "&name='dnrCntEmail'"
			[ ] "Email[1]"
			[ ] "$dnrCntEmail"
	[+] HtmlTextField Title1
		[-] multitag "&name='dnrCntTitle'"
			[ ] "Title[1]"
			[ ] "$dnrCntTitle"
	[+] HtmlTextField Organization1
		[-] multitag "&name='dnrCntOrganization'"
			[ ] "Organization[1]"
			[ ] "$dnrCntOrganization"
	[+] HtmlTextField PhoneNumber1
		[-] multitag "&name='dnrCntPhoneNumber'"
			[ ] "Phone Number[1]"
			[ ] "$dnrCntPhoneNumber"
	[+] HtmlTextField FaxNumber1
		[-] multitag "&name='dnrCntFaxNumber'"
			[ ] "Fax Number[1]"
			[ ] "$dnrCntFaxNumber"
	[+] HtmlTextField FirstName2
		[-] multitag "&name='mfdCntFirstName'"
			[ ] "First Name[2]"
			[ ] "$mfdCntFirstName"
	[+] HtmlTextField LastName2
		[-] multitag "&name='mfdCntLastName'"
			[ ] "Last Name[2]"
			[ ] "$mfdCntLastName"
	[+] HtmlTextField Email2
		[-] multitag "&name='mfdCntEmail'"
			[ ] "Email[2]"
			[ ] "$mfdCntEmail"
	[+] HtmlTextField Title2
		[-] multitag "&name='mfdCntTitle'"
			[ ] "Title[2]"
			[ ] "$mfdCntTitle"
	[+] HtmlTextField Organization2
		[-] multitag "&name='mfdCntOrganization'"
			[ ] "Organization[2]"
			[ ] "$mfdCntOrganization"
	[+] HtmlTextField PhoneNumber2
		[-] multitag "&name='mfdCntPhoneNumber'"
			[ ] "Phone Number[2]"
			[ ] "$mfdCntPhoneNumber"
	[+] HtmlTextField FaxNumber2
		[-] multitag "&name='mfdCntFaxNumber'"
			[ ] "Fax Number[2]"
			[ ] "$mfdCntFaxNumber"
	[+] HtmlLink ME
		[-] multitag "M & E"
			[ ] "$javascript:gotoStep(10)"
	[ ] 
[ ] 
[+] window BrowserChild AMPAddActivityStep10
	[ ] tag "AMP - Add Activity - Monitoring and Evaluation"
	[ ] parent Browser
	[+] HtmlImage IND004
		[-] multitag "- IND004"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?arr[5]"
	[+] HtmlTextField BaseValue
		[-] multitag "&name='baseVal'"
			[ ] "?[1]"
			[ ] "$baseVal"
	[+] HtmlTextField Comments1
		[-] multitag "&name='baseValComments'"
			[ ] "Comments[1]"
			[ ] "$baseValComments"
	[+] HtmlTextField TargetVal
		[-] multitag "&name='targetVal'"
			[ ] "?[3]"
			[ ] "$targetVal"
	[+] HtmlTextField Comments2
		[-] multitag "&name='targetValComments'"
			[ ] "Comments[2]"
			[ ] "$targetValComments"
	[+] HtmlTextField CurrentValue
		[-] multitag "&name='currentVal'"
			[ ] "Current Value"
			[ ] "$currentVal"
	[+] HtmlTextField Comments3
		[-] multitag "&name='currentValComments'"
			[ ] "Comments[3]"
			[ ] "$currentValComments"
	[+] HtmlPushButton SetValues
		[ ] tag "Set Values"
	[+] HtmlLink Costing
		[-] multitag "Costing"
			[ ] "$javascript:gotoStep(11)"
	[ ] 
	[+] HtmlText IND0041
		[ ] tag "- IND004"
	[+] HtmlImage ClickToViewCalendar1
		[-] multitag "Click to View Calendar[1]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[1]"
	[+] HtmlTable HtmlTable1
		[ ] tag "#1"
		[+] HtmlColumn Mon
			[ ] tag "Mon"
			[-] HtmlText N7
				[ ] tag "7"
		[+] HtmlColumn Wed
			[ ] tag "Wed"
			[-] HtmlText N9
				[ ] tag "9"
	[+] HtmlImage ClickToViewCalendar2
		[-] multitag "Click to View Calendar[2]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[2]"
	[-] HtmlTable Today
		[ ] tag "Today"
		[+] HtmlColumn Wed
			[ ] tag "Wed"
			[-] HtmlText N9
				[ ] tag "9"
		[+] HtmlColumn Tue
			[ ] tag "Tue"
			[-] HtmlText N8
				[ ] tag "8"
	[-] HtmlImage ClickToViewCalendar3
		[-] multitag "Click to View Calendar[3]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[3]"
[ ] 
[+] window BrowserChild AMPAddActivityStep11
	[ ] tag "AMP - Add Activity - Project Costs"
	[ ] parent Browser
	[+] HtmlPushButton AddActivity
		[ ] tag "Add Activity"
[ ] 
[+] window BrowserChild AMPAddEditActivity
	[ ] tag "AMP - Edit Activity"
	[ ] parent Browser
	[+] HtmlTextField ActivityName
		[-] multitag "&name='name'"
			[ ] "Activity name:"
			[ ] "$name"
	[+] HtmlTextField ActivityID
		[-] multitag "&name='textId'"
			[ ] "Activity ID:"
			[ ] "$textId"
	[+] HtmlTextField Inputs
		[-] multitag "&name='inputs'"
			[ ] "Inputs:"
			[ ] "$inputs"
	[+] HtmlTextField TotalCost
		[-] multitag "&name='totalCost'"
			[ ] "Total Cost:"
			[ ] "$totalCost"
	[+] HtmlPopupList ActivityID1
		[-] multitag "&name='totalCostCurrencyId'"
			[ ] "Activity ID:"
			[ ] "$totalCostCurrencyId"
	[+] HtmlTextField Contributions
		[-] multitag "&name='contrAmount'"
			[ ] "Contributions"
			[ ] "$contrAmount"
	[+] HtmlPopupList Pop1
		[-] multitag "&name='contrCurrId'"
			[ ] "*"
			[ ] "$contrCurrId"
	[+] HtmlPopupList Pop2
		[-] multitag "&name='contrFinInstrId'"
			[ ] "*"
			[ ] "$contrFinInstrId"
	[+] HtmlPopupList Pop3
		[-] multitag "&name='contrDonorId'"
			[ ] "*"
			[ ] "$contrDonorId"
	[+] HtmlPopupList Pop4
		[-] multitag "&name='contrFinTypeId'"
			[ ] "*"
			[ ] "$contrFinTypeId"
	[+] HtmlPushButton AddContribution
		[-] multitag "&name='addFields'"
			[ ] "Add Contribution"
			[ ] "$addFields"
	[+] HtmlTextField Assumptions
		[-] multitag "&name='assumptions'"
			[ ] "Assumptions:"
			[ ] "$assumptions"
	[+] HtmlTextField DueDate
		[-] multitag "&name='dueDate'"
			[ ] "Due Date*"
			[ ] "$dueDate"
	[ ] 
	[+] HtmlTextField Progress
		[-] multitag "&name='progress'"
			[ ] "Progress:"
			[ ] "$progress"
	[+] HtmlPushButton PickDate
		[ ] tag "Pick date"
	[+] HtmlPushButton OK
		[-] multitag "&name='save'"
			[ ] "OK"
			[ ] "$save"
[ ] 
[+] window DialogBox ChooseFile
	[ ] tag "Choose file"
	[ ] parent Browser
	[+] PushButton MyDocuments
		[-] multitag "My Documents"
			[ ] "$41062"
	[+] ListView ListView1
		[-] multitag "#1"
			[ ] "$1"
	[+] PushButton Open
		[-] multitag "Open"
			[ ] "$1"
	[-] PushButton Desktop
		[-] multitag "Desktop"
			[ ] "$41061"
[+] window BrowserChild EditData
	[ ] tag "Edit Data"
	[ ] parent Browser
	[ ] 
	[-] HtmlTextField Text
		[ ] tag "$content"
	[ ] 
	[-] HtmlPushButton SaveNow
		[ ] tag "Save now"
	[ ] 
	[ ] 
	[-] HtmlTextField Title11
		[-] multitag "Title:[1]"
			[ ] "$title"
			[ ] "&name='title'"
	[-] HtmlTextField Title21
		[-] multitag "Title:[2]"
			[ ] "$content"
			[ ] "&name='content'"
	[ ] 
	[-] BrowserChild Bla
		[-] multitag "Edit Data - Windows Internet Explorer"
			[ ] "&id='_content_editor'"
		[ ] 
		[-] HtmlTextField test
			[ ] tag "#1"
		[ ] 
	[ ] 
	[ ] 
	[-] BrowserChild Title
		[+] multitag "&id='_content_editor'"
			[ ] "Title:"
		[-] HtmlTextField Title21
			[-] multitag "Title:[2]"
				[ ] "$content"
				[ ] "&name='content'"
			[ ] 
		[ ] 
		[ ] 
[+] window BrowserChild CommentDetail
	[ ] tag "- Comment Detail"
	[ ] parent Browser
	[-] HtmlTextField AddYourCommentHere
		[-] multitag "&id='commentText';name='commentText'"
			[ ] "Add your comment here"
			[ ] "$commentText"
	[-] HtmlPushButton Save
		[ ] tag "Save"
	[-] HtmlPushButton Close
		[ ] tag "Close"
		[ ] 
[+] window DialogBox MozillaFirefox
	[ ] tag "Mozilla Firefox"
[+] window BrowserChild Popup
	[ ] tag "Popup"
	[ ] parent Browser
	[-] HtmlTable HtmlTable1
		[ ] tag "&name='framingTable'"
		[+] HtmlColumn SelectSelectDocumen
			[ ] tag "Select          Select Documents"
			[+] HtmlPushButton Select
				[-] multitag "&id='otherDocumentsMenu0';name='otherDocumentsButton'"
					[ ] "Select"
					[ ] "$otherDocumentsButton"
			[-] HtmlText Examen_krDoc
				[-] multitag "&id='yui-dt0-bdrow0-cell0'"
					[ ] "examen_kr.doc"
					[ ] "$yui-dt0-bdrow0-cell0"
	[-] HtmlList PleaseWaitAMoment
		[ ] tag "Please wait a moment"
	[+] HtmlPushButton Submit
		[ ] tag "Submit"
[+] window BrowserChild ProposedProjectCost
	[ ] tag "Proposed Project Cost"
	[ ] parent Browser
	[+] HtmlTextField Planned
		[-] multitag "&id='funAmount';name='proProjCost.funAmount'"
			[ ] "Planned"
			[ ] "$proProjCost.funAmount"
	[+] HtmlPopupList Currency
		[-] multitag "&name='proProjCost.currencyCode'"
			[ ] "Currency"
			[ ] "$proProjCost.currencyCode"
	[+] HtmlImage ClickToViewCalendar
		[-] multitag "Click to View Calendar"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho"
	[-] HtmlTable Today
		[ ] tag "Today"
		[+] HtmlColumn Sun
			[ ] tag "Sun"
			[+] HtmlText N2
				[ ] tag "2"
			[-] HtmlText N13
				[ ] tag "13"
		[+] HtmlColumn Sat
			[ ] tag "Sat"
			[+] HtmlText N1
				[ ] tag "1"
			[+] HtmlText N15
				[ ] tag "15"
			[-] HtmlText N12
				[ ] tag "12"
		[+] HtmlColumn Wed
			[ ] tag "Wed"
			[+] HtmlText N26
				[ ] tag "26"
			[+] HtmlText N19
				[ ] tag "19"
			[+] HtmlText N16
				[ ] tag "16"
			[-] HtmlText N9
				[ ] tag "9"
		[+] HtmlColumn Fri
			[ ] tag "Fri"
			[-] HtmlText N21
				[ ] tag "21"
		[+] HtmlColumn Thu
			[ ] tag "Thu"
			[+] HtmlText N27
				[ ] tag "27"
			[-] HtmlText N20
				[ ] tag "20"
	[-] HtmlPushButton Save
		[ ] tag "Save"
[+] window DialogBox StartMenu
	[ ] tag "Start Menu"
	[ ] parent Taskbar
[+] window DialogBox MyDocuments
	[ ] tag "My Documents"
	[+] ListView ListView1
		[-] multitag "#1"
			[ ] "$1"
		[-] TextField TextField1
			[-] multitag "#1"
				[ ] "$1"
[+] window BrowserChild AMPDesktopLinks
	[ ] tag "AMP*" //should be generized
	[ ] parent Browser
	[+] HtmlList TopMenu
		[ ] tag "Aid Management Platform (AMP)"
		[+] HtmlLink Calendar
			[-] multitag "::: CALENDAR"
				[ ] "$?calendar?showCalendarView.do"
		[+] HtmlLink Reports
			[ ] 
		[+] HtmlLink Documents
			[ ] 
		[+] HtmlLink ContentRepository
			[-] multitag "::: CONTENT REPOSITORY"
				[ ] "$?contentrepository?documentManager.do"
		[+] HtmlLink MEDashboard
			[ ] 
		[+] HtmlLink Help
			[ ] 
	[+] HtmlPushButton Add1
		[ ] tag "Add"
	[+] HtmlTextField Actor
		[-] multitag "&name='actor'"
			[ ] "Actor"
			[ ] "$actor"
	[+] HtmlPushButton Save
		[-] multitag "&name='saveButton'"
			[ ] "Save"
			[ ] "$saveButton"
	[+] HtmlLink Costing
		[-] multitag "Costing"
			[ ] "$javascript:gotoStep(11)"
	[ ] 
	[ ] 
[ ] 
