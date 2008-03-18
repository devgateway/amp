[ ] 
[-] testcase Test1 () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] 
	[ ] //AMPDesktopLinks.TopMenu.Calendar.Click()
	[ ] //AMPMyDesktop.ProjectManagementPlatformP.CALENDAR.Click ()
	[ ] startConditions()
	[ ] calendarCheckEventTypes()
	[ ] calendarCheckDonors()
	[ ] AMPCalendar.CreateNewEvent.Click()
	[ ] AMPShowAmpEvent.EventTitle.SetText("New Event for Test")
	[ ] INTEGER eventNo
	[ ] eventNo = AMPShowAmpEvent.EventType.GetItemCount()
	[ ] AMPShowAmpEvent.EventType.Select(1) //first Item
	[ ] AMPShowAmpEvent.AddOrganizations.Click()
	[ ] OrganisationPopup.SetActive()
	[ ] OrganisationPopup.OrganizationType.Select(1)
	[ ] OrganisationPopup.Search.Click()
	[ ] OrganisationPopup.ResultsCheckBox1.Check()
	[ ] OrganisationPopup.ResultsCheckBox2.Check()
	[ ] OrganisationPopup.Add.Click()
	[ ] DATETIME today 
	[ ] DATETIME new
	[ ] STRING date1
	[ ] STRING date2
	[ ] today = GetDateTime()
	[ ] new = AddDateTime(today,5)
	[ ] date1 = FormatDateTime (new, "dd/mm/yyyy")
	[ ] date2 = FormatDateTime (today, "dd/mm/yyyy")
	[ ] Agent.SetOption(OPT_VERIFY_ENABLED, false)
	[ ] BrowserPage.ExecLine ('document.getElementById("selectedStartDate").value = "'+date1+'"', TRUE)
	[ ] AMPShowAmpEvent.startHour.Select("18")
	[ ] AMPShowAmpEvent.startMinute.Select("10")
	[ ] BrowserPage.ExecLine ('document.getElementById("selectedEndDate").value = "'+date2+'"', TRUE)
	[ ] AMPShowAmpEvent.endHour.Select("20")
	[ ] AMPShowAmpEvent.endMinute.Select("10")
	[ ] Agent.SetOption(OPT_VERIFY_ENABLED,  true)
	[ ] AMPShowAmpEvent.Preview.Click()
	[-] //AMPShowAmpEvent.Gregorian.VerifyProperties({...})
		[ ] //""
		[-] //{...}
			[-] //{"$Contents",            [LIST OF STRING] {...}}
				[ ] //"Start Date Should Be Less Or Equal To End Date"
	[ ] new = AddDateTime(today,10)
	[ ] date2 = FormatDateTime(new, "dd/mm/yyyy")
	[ ] BrowserPage.ExecLine ('document.getElementById("selectedEndDate").value = "'+date2+'"', TRUE)
	[ ] 
	[ ] AMPShowAmpEvent.Users.Select(1)
	[ ] AMPShowAmpEvent.Users.ExtendSelect(2)
	[ ] AMPShowAmpEvent.AddUsers.Click()
	[ ] AMPShowAmpEvent.Guest.SetText("Tester McTester")
	[ ] AMPShowAmpEvent.AddGuest.Click()
	[ ] AMPShowAmpEvent.SelectedUsers.Select(2)
	[ ] AMPShowAmpEvent.Remove.Click()
	[ ] //should be removed BUG
	[ ] AMPShowAmpEvent.EventTitle.SetText("New Event for Test")
	[ ] AMPShowAmpEvent.AddOrganizations.Click()
	[ ] OrganisationPopup.SetActive()
	[ ] OrganisationPopup.OrganizationType.Select(1)
	[ ] OrganisationPopup.Search.Click()
	[ ] OrganisationPopup.ResultsCheckBox1.Check()
	[ ] OrganisationPopup.ResultsCheckBox2.Check()
	[ ] OrganisationPopup.Add.Click()
	[ ] //end remove
	[ ] 
	[ ] 
	[ ] AMPShowAmpEvent.Preview.Click()
	[ ] AMPPreviewAmpEvent.Edit.Click ()
	[ ] AMPShowAmpEvent.Guest.SetText("Harry McTester")
	[ ] AMPShowAmpEvent.AddGuest.Click()
	[ ] AMPShowAmpEvent.Preview.Click()
	[ ] 
	[ ] AMPPreviewAmpEvent.Save.Click()
	[ ] 
	[ ] AMPCalendar.ShowPublicEvents.Check()
	[ ] AMPCalendar.Show.Click ()
	[ ] 
	[ ] 
	[+] AMPCalendar.CreateNewEvent.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Enabled",              TRUE}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "Create New Event"}
	[+] AMPCalendar.NewEventForTest.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "New Event for Test"}
	[ ] 
	[ ] printf("MERGE!")
[-] testcase Test2scenario1 () appstate DefaultBaseState
	[ ] calendarTest2Preconditions()
	[ ] 
	[ ] AMPCalendar.View.Yearly.View.Click()
	[ ] AMPCalendar.NewEventForTest.Click()
	[ ] sleep(3)
	[ ] AMPDesktopLinks.TopMenu.Calendar.Click()
	[ ] 
[-] testcase Test2scenario2 () appstate DefaultBaseState
	[ ] calendarTest2Preconditions()
	[ ] 
	[ ] AMPCalendar.View.Monthly.View.Click()
	[ ] AMPCalendar.NewEventForTest.Click()
	[ ] sleep(3)
	[ ] AMPDesktopLinks.TopMenu.Calendar.Click()
	[ ] 
[-] testcase Test2scenario3 () appstate DefaultBaseState
	[ ] calendarTest2Preconditions()
	[ ] 
	[ ] AMPCalendar.View.Weekly.View.Click()
	[ ] AMPCalendar.NewEventForTest.Click()
	[ ] sleep(3)
	[ ] AMPDesktopLinks.TopMenu.Calendar.Click()
	[ ] 
[-] testcase Test2scenario4 () appstate DefaultBaseState
	[ ] calendarTest2Preconditions()
	[ ] 
	[ ] AMPCalendar.View.Daily.View.Click()
	[ ] AMPCalendar.Special.HtmlColumn1.HtmlTable1.HtmlColumn2.NewEventForTest.Click ()
	[ ] sleep(3)
	[ ] AMPDesktopLinks.TopMenu.Calendar.Click()
	[ ] 
[-] testcase Test2scenario5 () appstate DefaultBaseState
	[ ] calendarTest2Preconditions()
	[ ] 
	[ ] AMPCalendar.View.Custom.View.Click()
	[ ] 
	[ ] DATETIME today 
	[ ] DATETIME new
	[ ] STRING date1
	[ ] STRING date2
	[ ] today = GetDateTime()
	[ ] new = AddDateTime(today,5)
	[ ] date1 = FormatDateTime (new, "dd/mm/yyyy")
	[ ] date2 = FormatDateTime (today, "dd/mm/yyyy")
	[ ] Agent.SetOption(OPT_VERIFY_ENABLED, false)
	[ ] BrowserPage.ExecLine ('document.getElementById("customViewStartDate").value = "'+date1+'"', TRUE)
	[ ] new = AddDateTime(today,10)
	[ ] date2 = FormatDateTime(new, "dd/mm/yyyy")
	[ ] BrowserPage.ExecLine ('document.getElementById("customViewEndDate").value = "'+date2+'"', TRUE)
	[ ] 
	[ ] AMPCalendar.Show.Click()
	[ ] AMPCalendar.NewEventForTest.Click()
	[ ] sleep(3)
	[ ] AMPDesktopLinks.TopMenu.Calendar.Click()
	[ ] 
[ ] 
[-] testcase Test3 () appstate DefaultBaseState
	[ ] calendarTest2Preconditions()
	[ ] 
	[ ] AMPCalendar.View.Yearly.View.Click()
	[ ] AMPCalendar.NewEventForTest.Click()
	[ ] sleep(3)
	[ ] AMPPreviewAmpEvent.Edit.Click()
	[ ] 
	[ ] AMPShowAmpEvent.EventType.Select(2)
	[ ] AMPShowAmpEvent.AddOrganizations.Click()
	[ ] OrganisationPopup.SetActive()
	[ ] OrganisationPopup.OrganizationType.Select(1)
	[ ] OrganisationPopup.Search.Click()
	[ ] OrganisationPopup.ResultsCheckBox3.Check()
	[ ] OrganisationPopup.Add.Click()
	[ ] AMPShowAmpEvent.Guest.SetText("Matt McTester")
	[ ] AMPShowAmpEvent.AddGuest.Click()
	[ ] AMPShowAmpEvent.Preview.Click()
	[ ] AMPPreviewAmpEvent.Save.Click()
	[ ] 
	[ ] 
	[ ] 
	[ ] 
[-] testcase Test4 () appstate DefaultBaseState
	[ ] calendarTest2Preconditions()
	[ ] 
	[ ] AMPCalendar.View.Yearly.View.Click()
	[ ] AMPCalendar.NewEventForTest.Click()
	[ ] sleep(3)
	[ ] AMPPreviewAmpEvent.Edit.Click()
	[ ] AMPShowAmpEvent.Delete.Click ()
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click()
	[ ] sleep(3)
	[+] AMPCalendar.CreateNewEvent.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Enabled",              TRUE}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "Create New Event"}
	[ ] sleep(1)
	[-] AMPCalendar.NewEventForTest.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Exists",               FALSE}
	[ ] 
[ ] 
[-] testcase Test5 () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] startConditions()
	[ ] //AMPDesktopLinks.TopMenu.Calendar.Click()
	[ ] calendarCheckEventTypes()
	[ ] calendarCheckDonors()
	[ ] AMPCalendar.CreateNewEvent.Click()
	[ ] AMPShowAmpEvent.EventType.Select(1) //first Item
	[ ] AMPShowAmpEvent.AddOrganizations.Click()
	[ ] OrganisationPopup.SetActive()
	[ ] OrganisationPopup.OrganizationType.Select(1)
	[ ] OrganisationPopup.Search.Click()
	[ ] OrganisationPopup.ResultsCheckBox1.Check()
	[ ] OrganisationPopup.ResultsCheckBox2.Check()
	[ ] OrganisationPopup.Add.Click()
	[ ] DATETIME today 
	[ ] DATETIME new
	[ ] STRING date1
	[ ] STRING date2
	[ ] today = GetDateTime()
	[ ] date1 = FormatDateTime (AddDateTime(today,30), "dd/mm/yyyy") //almost next month
	[ ] date2 = FormatDateTime (AddDateTime(today,60), "dd/mm/yyyy")
	[ ] Agent.SetOption(OPT_VERIFY_ENABLED, false)
	[ ] BrowserPage.ExecLine ('document.getElementById("selectedStartDate").value = "'+date1+'"', TRUE)
	[ ] AMPShowAmpEvent.startHour.Select("18")
	[ ] AMPShowAmpEvent.startMinute.Select("10")
	[ ] BrowserPage.ExecLine ('document.getElementById("selectedEndDate").value = "'+date2+'"', TRUE)
	[ ] AMPShowAmpEvent.endHour.Select("20")
	[ ] AMPShowAmpEvent.endMinute.Select("10")
	[ ] Agent.SetOption(OPT_VERIFY_ENABLED,  true)
	[ ] 
	[ ] AMPShowAmpEvent.Users.Select(1)
	[ ] AMPShowAmpEvent.Users.ExtendSelect(2)
	[ ] AMPShowAmpEvent.AddUsers.Click()
	[ ] AMPShowAmpEvent.Guest.SetText("Tester McTester")
	[ ] AMPShowAmpEvent.AddGuest.Click()
	[ ] AMPShowAmpEvent.SelectedUsers.Select(2)
	[ ] AMPShowAmpEvent.Remove.Click()
	[ ] AMPShowAmpEvent.Preview.Click()
	[ ] 
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click ()
	[ ] 
	[ ] AMPShowAmpEvent.EventTitle.SetText("New Event for Test")
	[ ] AMPShowAmpEvent.Preview.Click()
	[ ] 
	[ ] AMPPreviewAmpEvent.Edit.Click ()
	[ ] AMPShowAmpEvent.Guest.SetText("Harry McTester")
	[ ] AMPShowAmpEvent.AddGuest.Click()
	[ ] AMPShowAmpEvent.Preview.Click()
	[ ] sleep(1)
	[ ] AMPPreviewAmpEvent.Save.Click()
	[ ] 
	[ ] AMPCalendar.ShowPublicEvents.Check()
	[ ] AMPCalendar.Show.Click ()
	[ ] 
	[ ] 
	[-] AMPCalendar.CreateNewEvent.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Enabled",              TRUE}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "Create New Event"}
	[-] AMPCalendar.NewEventForTest.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "New Event for Test"}
	[ ] printf("MERGE!")
[ ] 
[-] testcase Test6 () appstate DefaultBaseState //make event private
	[ ] login("atl@amp.org", "atl")
	[ ] startConditions()
	[ ] //AMPDesktopLinks.TopMenu.Calendar.Click()
	[ ] calendarCheckEventTypes()
	[ ] calendarCheckDonors()
	[ ] 
	[ ] AMPCalendar.ShowPublicEvents.Check()
	[ ] AMPCalendar.Show.Click()
	[ ] 
	[-] AMPCalendar.NewEventForTest.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "New Event for Test"}
	[ ] //end preconditions	
	[ ] AMPCalendar.NewEventForTest.Click()
	[ ] AMPPreviewAmpEvent.Edit.Click ()
	[ ] AMPShowAmpEvent.EventIsPrivate.Check ()
	[ ] AMPShowAmpEvent.Preview.Click()
	[ ] AMPPreviewAmpEvent.Save.Click()
	[ ] 
	[ ] AMPCalendar.ShowPublicEvents.Uncheck()
	[ ] AMPCalendar.Show.Click()
	[ ] 
	[-] AMPCalendar.CreateNewEvent.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Enabled",              TRUE}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "Create New Event"}
	[-] AMPCalendar.NewEventForTest.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "New Event for Test"}
	[ ] printf("MERGE!")
	[ ] 
[ ] 
[-] testcase Test7 () appstate DefaultBaseState //make event public
	[ ] login("atl@amp.org", "atl")
	[ ] startConditions()
	[ ] //AMPDesktopLinks.TopMenu.Calendar.Click()
	[ ] calendarCheckEventTypes()
	[ ] calendarCheckDonors()
	[ ] 
	[ ] AMPCalendar.ShowPublicEvents.Uncheck()
	[ ] AMPCalendar.Show.Click()
	[ ] 
	[-] AMPCalendar.NewEventForTest.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "New Event for Test"}
	[ ] //end preconditions	
	[ ] AMPCalendar.NewEventForTest.Click()
	[ ] AMPPreviewAmpEvent.Edit.Click ()
	[ ] AMPShowAmpEvent.EventIsPrivate.UnCheck()
	[ ] AMPShowAmpEvent.Preview.Click()
	[ ] AMPPreviewAmpEvent.Save.Click()
	[ ] 
	[ ] AMPCalendar.ShowPublicEvents.Check()
	[ ] AMPCalendar.Show.Click()
	[ ] 
	[-] AMPCalendar.CreateNewEvent.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Enabled",              TRUE}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "Create New Event"}
	[-] AMPCalendar.NewEventForTest.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "New Event for Test"}
	[ ] printf("MERGE!")
	[ ] 
[ ] 
[-] testcase Test8 () appstate DefaultBaseState 
	[ ] login("atl@amp.org", "atl")
	[ ] startConditions()
	[ ] //AMPDesktopLinks.TopMenu.Calendar.Click()
	[ ] calendarCheckEventTypes()
	[ ] calendarCheckDonors()
	[ ] 
	[ ] AMPCalendar.ShowPublicEvents.Uncheck()
	[ ] AMPCalendar.Show.Click()
	[ ] 
	[+] AMPCalendar.NewEventForTest.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "New Event for Test"}
	[ ] //end preconditions	
	[ ] //can't be done dynamically yet
	[ ] //TO DO
