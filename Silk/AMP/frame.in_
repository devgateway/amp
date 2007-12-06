[ ] const wMainWindow = AMPHome
[ ] 
[+] window BrowserChild AMPHome
	[ ]  tag "AMP -Home"
	[ ] 
	[ ]  // The URL of this page
	[ ]  const sLocation = "http://amp-staging.code.ro/showLayout.do?layout=login"
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
[-] window BrowserChild AMPShowAmpEvent  // After clicking create new event
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
	[-] HtmlLink Objective
		[-] multitag "Edit[1]"
			[ ] "$javascript:edit('aim-obj-*"
	[-] HtmlLink Description
		[-] multitag "Edit[1]"
			[ ] "$javascript:edit('aim-desc-*"
	[-] HtmlLink ObjAddEditObjectivelyVerifIndicators
		[-] multitag "Add?Edit Objectively Verifiable Indicators[1]"
			[ ] "$javascript:commentWin('objObjVerIndicators')"
	[-] HtmlLink ObjAddEditAssumption
		[-] multitag "Add?Edit Assumption[1]"
			[ ] "$javascript:commentWin('objAssumption')"
	[-] HtmlLink ObjAddEditVerification
		[-] multitag "Add?Edit Verification[1]"
			[ ] "$javascript:commentWin('objVerification')"
	[+] HtmlLink Edit2
		[-] multitag "Edit[2]"
			[ ] "$javascript:edit('aim-desc-35-1196422701403')"
	[ ] 
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
[ ] 
[+] window BrowserChild AMPDesktopLinks
	[ ] tag "AMP*" //should be generized
	[ ] parent Browser
	[+] HtmlList TopMenu
		[ ] tag "Aid Management Platform (AMP)"
		[-] HtmlLink Calendar
			[-] multitag "::: CALENDAR"
				[ ] "$?calendar?showCalendarView.do"
		[-] HtmlLink Reports
			[ ] 
		[-] HtmlLink Documents
			[ ] 
		[-] HtmlLink ContentRepository
			[-] multitag "::: CONTENT REPOSITORY"
				[ ] "$?contentrepository?documentManager.do"
		[+] HtmlLink MEDashboard
			[ ] 
		[+] HtmlLink Help
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
