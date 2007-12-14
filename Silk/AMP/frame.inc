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
[+] window BrowserChild AMPAddActivityStep1
	[ ] tag "AMP - Add Activity - Identification*"
	[ ] parent Browser
	[+] HtmlTextField ProjectTitle
		[-] multitag "&name='title'"
			[ ] "Project Title"
			[ ] "$title"
	[+] HtmlLink Objective
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
			[-] HtmlText N12
				[ ] tag "12"
		[+] HtmlColumn Tue
			[ ] tag "Tue"
			[-] HtmlText N4
				[ ] tag "4"
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
[ ] 
[+] window BrowserChild AMPMyDesktop
	[ ] tag "AMP- My Desktop" //
	[ ] parent Browser
	[+] HtmlPushButton AddActivity
		[-] multitag "&name='addActivity'"
			[ ] "Add Activity"
			[ ] "$addActivity[1]"
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
	[ ] 
[ ] 
[+] window BrowserChild AMPAddSectors
	[ ] tag "AMP - Select Sector"
	[ ] parent Browser
	[+] HtmlPopupList SelectSectors
		[-] multitag "&name='sectorScheme'"
			[ ] "Sector Scheme"
			[ ] "$sectorScheme"
	[-] HtmlPopupList Sector
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
[ ] 
[-] window BrowserChild AMPAddActivityStep5
	[ ] tag "AMP - Add Activity - Components"
	[ ] parent Browser
	[+] HtmlPushButton AddComponent
		[-] multitag "&name='submitButton'"
			[ ] "Add Component"
			[ ] "$submitButton[1]"
	[ ] 
[ ] 
[-] window BrowserChild AMPAddComponents
	[ ] tag "AMP - Add Components"
	[ ] parent Browser
	[+] HtmlPopupList Component
		[-] multitag "&name='componentTitle'"
			[ ] "Add Component"
			[ ] "$componentTitle"
	[+] HtmlLink AddCommitments
		[-] multitag "Add[1]"
			[ ] "$javascript:addCommitments()"
	[+] HtmlLink AddExpenditure
		[-] multitag "Add[2]"
			[ ] "$javascript:addExpenditure()"
	[+] HtmlLink AddDisbursement
		[-] multitag "Add[2]"
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
	[+] HtmlLink References
		[-] multitag "References"
			[ ] "$javascript:gotoStep('1_5')"
	[+] HtmlLink Location
		[-] multitag "Location"
			[ ] "$javascript:gotoStep(2)[1]"
	[+] HtmlPopupList SelectTheAppropriateRegion
		[-] multitag "&name='implemLocationLevel'"
			[ ] "Select the appropriate Region, Zone or Woreda as needed."
			[ ] "$implemLocationLevel"
	[+] HtmlPushButton Close
		[ ] tag "Close"
	[+] HtmlPopupList ImplementationLevel
		[-] multitag "&name='levelId'"
			[ ] "Implementation Level"
			[ ] "$levelId"
	[+] HtmlPushButton AddLocation
		[-] multitag "&name='submitButton'"
			[ ] "Add Location"
			[ ] "$submitButton"
	[+] HtmlPopupList Bolivia
		[-] multitag "&name='impRegion'"
			[ ] "Bolivia"
			[ ] "$impRegion"
	[+] HtmlListBox District
		[-] multitag "&name='impMultiWoreda'"
			[ ] "District"
			[ ] "$impMultiWoreda"
	[+] HtmlPopupList Region
		[-] multitag "&name='impRegion'"
			[ ] "askldalsd"
			[ ] "$impRegion"
	[+] HtmlPushButton Add
		[ ] tag "Add"
	[+] HtmlPushButton AddSectors
		[ ] tag "Add Sectors"
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
	[+] HtmlPushButton AddFunding
		[ ] tag "Add Funding"
	[+] HtmlPushButton AddOrganizations
		[-] multitag "&name='submitButton'"
			[ ] "Add Organizations"
			[ ] "$submitButton[1]"
	[+] HtmlPushButton AddFunding2
		[ ] tag "Add Funding[2]"
	[+] HtmlCheckBox DelegatedPartner2
		[-] multitag "&name='fundingOrganization?1?.delegatedCooperation'"
			[ ] "Delegated Partner[2]"
			[ ] "$fundingOrganization?1?.delegatedCooperation"
	[+] HtmlCheckBox Identification
		[-] multitag "&name='fundingOrganization?1?.delegatedPartner'"
			[ ] "Identification"
			[ ] "$fundingOrganization?1?.delegatedPartner"
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
			[+] HtmlImage ClickToViewCalendar
				[-] multitag "Click to View Calendar"
					[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho"
		[+] HtmlColumn Fri
			[ ] tag "Fri"
			[-] HtmlText N7
				[ ] tag "7"
		[+] HtmlColumn Thu
			[ ] tag "Thu"
			[+] HtmlText N6
				[ ] tag "6"
			[-] HtmlText N13
				[ ] tag "13"
		[+] HtmlColumn Wed
			[ ] tag "Wed"
			[+] HtmlText N5
				[ ] tag "5"
			[-] HtmlText N12
				[ ] tag "12"
	[+] HtmlTable Today
		[ ] tag "Today"
		[+] HtmlColumn Thu
			[ ] tag "Thu"
			[+] HtmlText N6
				[ ] tag "6"
			[+] HtmlText N20
				[ ] tag "20"
			[-] HtmlText N13
				[ ] tag "13"
		[+] HtmlColumn Wed
			[ ] tag "Wed"
			[-] HtmlText N12
				[ ] tag "12"
		[+] HtmlColumn Fri
			[ ] tag "Fri"
			[-] HtmlText N7
				[ ] tag "7"
		[+] HtmlColumn Tue
			[ ] tag "Tue"
			[+] HtmlText N11
				[ ] tag "11"
			[-] HtmlText N4
				[ ] tag "4"
		[+] HtmlColumn Sat
			[ ] tag "Sat"
			[-] HtmlText N8
				[ ] tag "8"
		[+] HtmlColumn Sun
			[ ] tag "Sun"
			[-] HtmlText N16
				[ ] tag "16"
	[+] HtmlPushButton AddDisbursement
		[-] multitag "&name='xx1'"
			[ ] "Add Disbursement"
			[ ] "$xx1"
	[+] HtmlTextField Planned
		[-] multitag "&name='fundingDetail?1?.transactionAmount'"
			[ ] "Planned"
			[ ] "$fundingDetail?1?.transactionAmount"
	[+] HtmlImage ClickToViewCalendar
		[-] multitag "Click to View Calendar"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho"
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
		[+] HtmlColumn Fri
			[ ] tag "Fri"
			[-] HtmlText N7
				[ ] tag "7"
		[+] HtmlColumn Thu
			[ ] tag "Thu"
			[-] HtmlText N13
				[ ] tag "13"
	[+] HtmlTable December
		[ ] tag "December"
		[+] HtmlColumn Expenditures
			[ ] tag "Expenditures"
			[-] HtmlTextField Classification
				[-] multitag "&name='fundingDetail?2?.classification'"
					[ ] "Classification"
					[ ] "$fundingDetail?2?.classification"
	[+] HtmlTextField Conditions
		[-] multitag "&name='fundingConditions'"
			[ ] "Conditions"
			[ ] "$fundingConditions"
	[+] HtmlPushButton Save
		[ ] tag "Save"
	[+] HtmlLink RegionalFunding
		[-] multitag "Regional Funding"
			[ ] "$javascript:gotoStep(4)"
	[+] HtmlText NoRegionsSelected
		[ ] tag "No regions selected"
	[+] HtmlLink Add1
		[-] multitag "Add[1]"
			[ ] "$javascript:addCommitments()"
	[+] HtmlLink Add3
		[-] multitag "Add[3]"
			[ ] "$javascript:addDisbursement()"
	[+] HtmlTextField Planned2
		[-] multitag "&name='disb_0_2'"
			[ ] "Planned[2]"
			[ ] "$disb_0_2"
	[+] HtmlImage ClickToViewCalendar2
		[-] multitag "Click to View Calendar[2]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[2]"
	[+] HtmlLink Add4
		[-] multitag "Add[4]"
			[ ] "$javascript:addExpenditure()"
	[+] HtmlTextField Planned3
		[-] multitag "&name='expn_0_2'"
			[ ] "Planned[3]"
			[ ] "$expn_0_2"
	[+] HtmlImage ClickToViewCalendar3
		[-] multitag "Click to View Calendar[3]"
			[ ] "$http:??amp-staging.code.ro:8080?TEMPLATE?ampTemplate?images?sho[3]"
	[+] HtmlLink AddPhysicalProgress
		[-] multitag "Add Physical Progress"
			[ ] "$javascript:addPhyProgess(-1,1197564805094)"
	[+] HtmlTextField Title
		[-] multitag "&name='phyProgTitle'"
			[ ] "Title"
			[ ] "$phyProgTitle"
	[+] HtmlPushButton AddIssues
		[-] multitag "&name='submitButton'"
			[ ] "Add Issues"
			[ ] "$submitButton[3]"
	[+] HtmlTextField Issue
		[-] multitag "&name='issue'"
			[ ] "Issue"
			[ ] "$issue"
	[+] HtmlPopupList PlannedActualPlannedActu
		[-] multitag "&name='comm_0_1'"
			[ ] "Planned?Actual    Planned?Actual    Amount                 Currency                        Date"
			[ ] "$comm_0_1"
	[+] HtmlPopupList CommitmentsTotalActualAl
		[-] multitag "&name='comm_0_3'"
			[ ] "Commitments - (Total Actual Allocation 0.0 null )"
			[ ] "$comm_0_3"
[ ] 
[+] window BrowserChild AMPAddFunding
	[ ] tag "AMP - Add Funding"
	[ ] parent Browser
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
	[+] HtmlPopupList Currency
		[-] multitag "&name='mtefProjection?0?.currencyCode'"
			[ ] "*"
			[ ] "$mtefProjection?0?.currencyCode"
	[+] HtmlPushButton AddCommitment
		[-] multitag "&name='xx1'"
			[ ] "Add Commitment"
			[ ] "$xx1"
	[+] HtmlPopupList PlannedActual
		[-] multitag "&name='fundingDetail?0?.adjustmentType'"
			[ ] "Planned?Actual*"
			[ ] "$fundingDetail?0?.adjustmentType"
	[+] HtmlTextField Planned
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
	[+] HtmlTextField Planned2
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
	[ ] 
	[ ] 
[ ] 
[+] window DialogBox ChooseFile
	[ ] tag "Choose file"
	[ ] parent Browser
	[-] PushButton MyDocuments
		[-] multitag "My Documents"
			[ ] "$41062"
	[-] ListView ListView1
		[-] multitag "#1"
			[ ] "$1"
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
