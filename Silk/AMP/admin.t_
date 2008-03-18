[+] testcase Test1_1() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.EventTypeManager.Click()
	[ ] AMPEventTypes.Name.SetText("New test event")
	[ ] AMPEventTypes.Color.SetText("#CC3366")
	[ ] AMPDesktopLinks.Add1.Click ()
	[ ] 
[+] testcase Test1_2() appstate DefaultBaseState
	[ ] login("atl@amp.org","atl")
	[ ] AMPDesktopLinks.TopMenu.Calendar.Click ()
	[ ] AMPCalendar.CreateNewEvent.Click ()
	[ ] AMPShowAmpEvent.EventTitle.SetText("new event type test")
	[ ] AMPShowAmpEvent.EventType.Select ("New test event")
	[ ] DATETIME today
	[ ] STRING date1
	[ ] today = GetDateTime()
	[ ] date1 = FormatDateTime(today,"dd/mm/yyyy")
	[ ] BrowserPage.ExecLine('document.getElementById("selectedStartDate").value = "'+date1+'"', TRUE)
	[ ] BrowserPage.ExecLine('document.getElementById("selectedEndDate").value = "'+date1+'"', TRUE)
	[ ] AMPShowAmpEvent.Preview.Click ()
	[ ] AMPDesktopLinks.Save.Click ()
	[ ] AMPCalendar.ShowPublicEvents.Check ()
	[ ] AMPCalendar.Show.Click ()
	[ ] 
[+] testcase Test1_3a() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.EventTypeManager.Click()
	[ ] INTEGER i
	[ ] STRING color = "eventTypeNameColor"
	[-] for (i = 1; i<8; i++)
		[ ] color[19] = Str(i)
		[ ] STRING elem = BrowserPage.ExecLine('document.getElementById("'+color+'").value')
		[-] if (elem == "#CC3366")
			[ ] BrowserPage.ExecLine('document.getElementById("'+color+'").value = "#CC3367"')
			[ ] break
		[ ] 
	[ ] AMPEventTypes.Save.Select(i)
	[ ] AMPEventTypes.Save.Click()
	[ ] 
[+] testcase Test1_3b() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.EventTypeManager.Click()
	[ ] INTEGER i
	[ ] STRING name = "eventTypeName"
	[-] for (i = 1; i<8; i++)
		[ ] name[14] = Str(i)
		[ ] STRING elem = BrowserPage.ExecLine('document.getElementById("'+name+'").value')
		[-] if (elem == "New test event")
			[ ] BrowserPage.ExecLine('document.getElementById("'+name+'").value = "New test event 2"')
			[ ] break
		[ ] 
	[ ] AMPEventTypes.Save.Select(i)
	[ ] AMPEventTypes.Save.Click()
	[ ] 
[+] testcase Test1_4() appstate DefaultBaseState
	[ ] login("atl@amp.org","atl")
	[ ] AMPDesktopLinks.TopMenu.Calendar.Click ()
	[ ] AMPCalendar.CreateNewEvent.Click ()
	[ ] AMPShowAmpEvent.EventTitle.SetText("new event type test")
	[ ] AMPShowAmpEvent.EventType.Select ("New test event 2")
	[ ] DATETIME today
	[ ] STRING date1
	[ ] today = GetDateTime()
	[ ] date1 = FormatDateTime(today,"dd/mm/yyyy")
	[ ] BrowserPage.ExecLine('document.getElementById("selectedStartDate").value = "'+date1+'"', TRUE)
	[ ] BrowserPage.ExecLine('document.getElementById("selectedEndDate").value = "'+date1+'"', TRUE)
	[ ] AMPShowAmpEvent.Preview.Click ()
	[ ] AMPDesktopLinks.Save.Click ()
	[ ] 
[+] testcase Test1_5() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.EventTypeManager.Click()
	[ ] INTEGER i
	[ ] STRING name = "eventTypeName"
	[-] for (i = 1; i<8; i++)
		[ ] name[14] = Str(i)
		[ ] STRING elem = BrowserPage.ExecLine('document.getElementById("'+name+'").value')
		[-] if (elem == "New test event")
			[ ] BrowserPage.ExecLine('document.getElementById("'+name+'").value = "New test event 2"')
			[ ] break
		[ ] 
	[ ] AMPEventTypes.Delete.Select(i)
	[ ] AMPEventTypes.Delete.Click()
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click ()
	[ ] 
[+] testcase Test2_1() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.FeatureManager.Click ()
	[ ] AMPFeatureManager.FeatureManager1.Options.Edit1.Click ()
	[ ] AMPFeatureManager.TemplateName.HttpAmpStagingCodeRo808.Click ()
	[ ] AMPFeatureManager.TemplateName.DefaultTemplate.HttpAmpStagingCodeRo808.Click ()
	[ ] AMPFeatureManager.TemplateName.DefaultTemplate.ProjectManagement.AddProgramsButtonNational.Click ()
	[ ] AMPFeatureManager.TemplateName.DefaultTemplate.ProjectManagement.ProjectIdAndPlanning.Budget.Uncheck ()
	[ ] AMPFeatureManager.SaveChanges.Click ()
	[ ] AMPFeatureManager.Logout.Click ()
	[ ] AMPDesktopLinks.AidManagementPlatformAMP.YouHaveBeenLoggedOut.ClickHereToGoBackToTheH.Click ()
	[ ] login("atl@amp.org","atl")
	[ ] AMPMyDesktop.AddActivity.Click ()
	[ ] 
[+] testcase Test2_2() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.FeatureManager.Click ()
	[ ] AMPFeatureManager.AddANewTemplate.Click ()
	[ ] AMPFeatureManager.TemplateName1.SetText("aTest template")
	[ ] AMPFeatureManager.SaveNewTemplate.Click ()
	[ ] 
[+] testcase Test2_3() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.FeatureManager.Click ()
	[ ] AMPFeatureManager.FeatureManager1.Options.Delete1.Click ()
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click ()
	[ ] 
[+] testcase Test3_1() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.FlagUploaderSelector.Click ()
	[ ] AMPFlagUploaderSelector.Country.Select ("US Minor Outlying Islands")
	[ ] AMPFlagUploaderSelector.Browse1.Click ()
	[ ] ChooseFile.SetActive ()
	[ ] ChooseFile.ListView1.Select ("flag")
	[ ] ChooseFile.Open.Click ()
	[ ] AMPFlagUploaderSelector.SetActive( )
	[ ] AMPFlagUploaderSelector.Upload.Click ()
	[ ] 
[+] testcase Test3_2() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.FlagUploaderSelector.Click ()
	[ ] AMPFlagUploaderSelector.SetActive( )
	[ ] AMPFlagUploaderSelector.USMinorOutlyingIslands.Click ()
	[ ] 
[+] testcase Test3_3() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.FlagUploaderSelector.Click ()
	[ ] AMPFlagUploaderSelector.FirstFlag.Click ()
	[ ] 
[+] testcase Test3_4() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.FlagUploaderSelector.Click ()
	[ ] AMPFlagUploaderSelector.HttpAmpStagingCodeRo808.Click ()
	[ ] 
[+] testcase Test5_1() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.GlobalSettings.Click ()
	[ ] AMPGlobalSettings.DefaultCountry.Select (1)
	[ ] AMPGlobalSettings.Save1.Click ()
	[ ] AMPGlobalSettings.PublicView.Select ("On")
	[ ] AMPGlobalSettings.Save2.Click ()
	[ ] AMPGlobalSettings.Budget.Select ("On Budget")
	[ ] AMPGlobalSettings.Save3.Click ()
	[ ] AMPGlobalSettings.DACCRSSectorScheme.Select (2)
	[ ] AMPGlobalSettings.Save5.Click ()
	[ ] AMPGlobalSettings.DefaultTemplate1.Select (2)
	[ ] AMPGlobalSettings.Save6.Click ()
	[ ] AMPGlobalSettings.MultiSector.Select ("Off")
	[ ] AMPGlobalSettings.Save8.Click ()
	[ ] AMPGlobalSettings.Perspective.Select ("On")
	[ ] AMPGlobalSettings.Save9.Click ()
	[ ] AMPGlobalSettings.USFiscalCalendar.Select (2)
	[ ] AMPGlobalSettings.Save10.Click ()
	[ ] 
[+] testcase Test5_2() appstate DefaultBaseState
	[ ] AMPHome1.ViewPublicPortfolio.Click ()
	[ ] AMPDesktopLinks.GoToLoginPage.Click ()
	[ ] login("atl@amp.org","atl")
	[ ] AMPMyDesktop.AddActivity.Click ()
	[ ] AMPAddActivityStep1.ProjectTitle.SetText("Silk test global settings")
	[ ] AMPAddActivityStep1.StatusId.Select (2)
	[ ] AMPAddActivityStep1.Location.Click ()
	[ ] AMPAddActivityStep3.ImplementationLevel.Select (2)
	[ ] AMPAddActivityStep3.SelectTheAppropriateRegion.Select (5)
	[ ] AMPAddActivityStep3.AddLocation.Click ()
	[ ] AMPSelectLocation.SetActive( )
	[ ] AMPSelectLocation.Region.Select (2)
	[ ] AMPSelectLocation.Zone.Select (2)
	[ ] AMPSelectLocation.District.Select (2)
	[ ] AMPSelectLocation.Add.Click ()
	[ ] AMPAddActivityStep3.AddSectors.Click ()
	[ ] AMPAddSectors.SetActive( )
	[ ] AMPAddSectors.Sector.Select (2)
	[ ] AMPAddSectors.Add.Click ()
	[ ] sleep(3)
	[ ] AMPAddActivityStep3.Funding.Click ()
	[ ] AMPAddActivityStep4.AddOrganizations.Click ()
	[ ] AMPSelectOrganizations.SetActive( )
	[ ] AMPSelectOrganizations.Search.Click ()
	[ ] AMPSelectOrganizations.ResultsCheckBox1.Check ()
	[ ] AMPSelectOrganizations.Add.Click ()
	[ ] AMPAddActivityStep4.SetActive( )
	[ ] AMPAddActivityStep4.AddFunding.Click ()
	[ ] AMPAddFunding.SetActive( )
	[ ] AMPAddFunding.HtmlTable1.Commitments.AddCommitment.Click ()
	[ ] //de completat 2 randuri
	[ ] 
[+] testcase Test6_1() appstate DefaultBaseState //nu merge
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.TranslationManager.Click ()
	[ ] AMPTranslationManager.En.Check ()
	[ ] AMPTranslationManager.Export.Click ()
	[ ] BrowserFileDownload.SetActive ()
	[ ] BrowserMessage.PushButton("Save|$4427").Click ()
	[ ] sleep(3)
	[ ] SaveAs.SetActive ()
	[ ] SaveAs.Save.Click ()
	[ ] AMPTranslationManager.SetActive( )
	[ ] AMPTranslationManager.En.Uncheck ()
	[ ] AMPTranslationManager.Fr.Check ()
	[ ] AMPTranslationManager.Export.Click ()
	[ ] BrowserFileDownload.SetActive ()
	[ ] BrowserMessage.PushButton("Save|$4427").Click ()
	[ ] sleep(3)
	[ ] SaveAs.SetActive ()
	[ ] SaveAs.Save.Click ()
	[ ] OverWrite.SetActive ()
	[ ] OverWrite.Yes.Click ()
	[ ] 
[+] testcase Test6_2() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.TranslationManager.Click ()
	[ ] AMPTranslationManager.Browse2.Click ()
	[ ] ChooseFile.SetActive ()
	[ ] ChooseFile.ListView1.Select ("en")
	[ ] ChooseFile.Open.Click ()
	[ ] AMPTranslationManager.Import.Click ()
	[ ] AMPTranslationManager.TheFollowingLanguagesWhere.Select ("Update local translations")
	[ ] AMPTranslationManager.Import1.Click ()
	[ ] AMPTranslationManager.Browse2.Click ()
	[ ] ChooseFile.SetActive ()
	[ ] ChooseFile.ListView1.Select ("fr")
	[ ] ChooseFile.Open.Click ()
	[ ] AMPTranslationManager.Import.Click ()
	[ ] AMPTranslationManager.TheFollowingLanguagesWhere.Select ("Update local translations")
	[ ] AMPTranslationManager.Import1.Click ()
	[ ] 
[+] testcase Test7_1a() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.WorkspaceManager.Click ()
	[ ] AMPWorkspaceManager.AddTeams.Click ()
	[ ] AMPWorkspaceManager.HtmlTextField1.SetText("management")
	[ ] AMPWorkspaceManager.TeamCategory.Select (2)
	[ ] AMPWorkspaceManager.WorkspaceType.Select ("Management")
	[ ] AMPWorkspaceManager.Add2.Click ()
	[ ] AMPSelectChildWorkspaces.SetActive( )
	[ ] AMPSelectChildWorkspaces.TestWorkspace.Check ()
	[ ] AMPSelectChildWorkspaces.Add1.Click ()
	[ ] AMPWorkspaceManager.SetActive( )
	[ ] AMPWorkspaceManager.Save.Click ()
	[ ] 
[+] testcase Test7_1b() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.WorkspaceManager.Click ()
	[ ] AMPWorkspaceManager.AddTeams.Click ()
	[ ] AMPWorkspaceManager.HtmlTextField1.SetText("team")
	[ ] AMPWorkspaceManager.TeamCategory.Select (3)
	[ ] AMPWorkspaceManager.WorkspaceType.Select ("DONOR")
	[ ] AMPWorkspaceManager.Save.Click ()
	[ ] 
[+] testcase Test7_2() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.WorkspaceManager.Click ()
	[ ] AMPWorkspaceManager.AddRoles.Click ()
	[ ] AMPRolesManager.RoleName.SetText("test role")
	[ ] AMPRolesManager.Description.SetText("test description")
	[ ] AMPDesktopLinks.Save.Click ()
	[ ] 
[+] testcase Test7_3a() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.WorkspaceManager.Click ()
	[ ] 
	[ ] int i
	[-] for (i=1;i<30;i++)
		[ ] AMPWorkspaceManager.setTeamId(i)
		[-] if (!AMPWorkspaceManager.Teams.Members.Members2.Exists())
			[ ] break
	[ ] AMPWorkspaceManager.setTeamId(i-1)
	[ ] AMPWorkspaceManager.Teams.Members.Members2.Click()
	[ ] 
	[ ] //AMPWorkspaceManager.Teams.Members.Members1.Click ()
	[ ] AMPTeamMembers.AddTeamMember.Click ()
	[ ] AMPTeamMembers.AddMembersForTestWorkspace.Select ("translator@amp.org")
	[ ] AMPTeamMembers.Role.Select ("test role")
	[ ] AMPDesktopLinks.Save.Click ()
	[ ] 
[+] testcase Test7_3b() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.WorkspaceManager.Click ()
	[ ] 
	[ ] int i
	[-] for (i=1;i<30;i++)
		[ ] AMPWorkspaceManager.setTeamId(i)
		[-] if (!AMPWorkspaceManager.Teams.Members.Members2.Exists())
			[ ] break
	[ ] AMPWorkspaceManager.setTeamId(i-1)
	[ ] AMPWorkspaceManager.Teams.Members.Members2.Click()
	[ ] 
	[ ] //AMPWorkspaceManager.Teams.Members.Members1.Click ()
	[ ] AMPTeamMembers.MembersForTestWorkspace.Edit.Edit4.Click ()
	[ ] AMPTeamMembers.AddUpdate.Check ()
	[ ] AMPTeamMembers.Delete.Check ()
	[ ] AMPDesktopLinks.Save.Click ()
	[ ] AMPTeamMembers.MembersForTestWorkspace.Edit.Edit4.Click ()
	[ ] 
[+] testcase Test7_4() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.WorkspaceManager.Click ()
	[ ] AMPWorkspaceManager.Teams.Activities.Activities1.Click ()
	[ ] AMPTeamActivities.AssignAnActivity.Click ()
	[ ] 
[+] testcase Test7_5a() appstate DefaultBaseState //nu merge
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.WorkspaceManager.Click ()
	[ ] int i
	[-] for (i=2;i<30;i++)
		[ ] AMPWorkspaceManager.setEdit(i)
		[-] if (!AMPWorkspaceManager.Teams.Edit.Edit14.Exists())
			[ ] break
	[ ] AMPWorkspaceManager.setTeamId(i-1)
	[ ] AMPWorkspaceManager.Teams.Edit.Edit14.Click()
	[ ] 
[+] testcase Test7_6() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.WorkspaceManager.Click ()
	[ ] int i
	[-] for (i=1;i<30;i++)
		[ ] AMPWorkspaceManager.setTeamId(i)
		[-] if (!AMPWorkspaceManager.Teams.Members.Members2.Exists())
			[ ] break
	[ ] AMPWorkspaceManager.setNPD(i-1)
	[ ] AMPWorkspaceManager.Teams.NpdSettings.NpdSettings14.Click ()
	[ ] ViewSettings.SetActive( )
	[ ] ViewSettings.Width.SetText("1000")
	[ ] ViewSettings.Height.SetText("100")
	[ ] ViewSettings.Angle.SetText("50")
	[ ] ViewSettings.Save.Click ()
	[ ] 
[+] testcase Test7_8() appstate DefaultBaseState
	[ ] login("test@test.com","test")
	[ ] AMPMyDesktop.SetActive( )
	[ ] AMPMyDesktop.AddActivity.Click ()
	[ ] AMPAddActivityStep1.Save.Click ()
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click ()
	[ ] AMPAddActivityStep1.SetActive( )
	[ ] AMPAddActivityStep1.ProjectTitle.SetText ("test")
	[ ] AMPAddActivityStep1.Save.Click ()
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click ()
	[ ] AMPAddActivityStep1.SetActive( )
	[ ] AMPAddActivityStep1.StatusId.Select (2)
	[ ] AMPAddActivityStep1.Save.Click ()
	[ ] AMPAddActivityStep3.AddSectors.Click ()
	[ ] AMPAddSectors.SetActive( )
	[ ] AMPAddSectors.Sector.Select (2)
	[ ] AMPAddSectors.Add.Click ()
	[ ] AMPAddSectors.SetActive( )
	[ ] AMPAddSectors.Add.Click ()
	[ ] AMPAddActivityStep3.SetActive( )
	[ ] AMPAddActivityStep3.Save.Click ()
	[ ] AMPMyDesktop.ProjectManagementPlatformP.REPORTS.Click ()
	[ ] AMPPublicTeamReports.ReportGenerator.Click ()
	[ ] AMPReportGeneratorStep1.N5ReportDetails.Select (1)
	[ ] AMPReportGeneratorStep1.Next.Click ()
	[ ] AMPReportGeneratorStep2.SelectAll.AMP.Identification.Check ()
	[ ] AMPReportGeneratorStep2.Add.Click ()
	[ ] AMPReportGeneratorStep2.Next1.Click ()
	[ ] AMPReportGeneratorStep3.CheckBox1.Check ()
	[ ] AMPReportGeneratorStep3.CheckBox2.Check ()
	[ ] AMPReportGeneratorStep3.Add.Click ()
	[ ] AMPReportGeneratorStep3.Next.Click ()
	[ ] AMPReportGeneratorStep4.CheckBox1.Check ()
	[ ] AMPReportGeneratorStep4.CheckBox2.Check ()
	[ ] AMPReportGeneratorStep4.Add.Click ()
	[ ] AMPReportGeneratorStep4.ShowAsADrilldownInMyTabs.Check ()
	[ ] AMPReportGeneratorStep4.Next.Click ()
	[ ] AMPReportGeneratorStep5.N5ReportDetails1.ReportTitle.HtmlTextField1.SetText("activities")
	[ ] AMPReportGeneratorStep5.SaveReport.Click ()
	[ ] 
[+] testcase Test8_1() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPDesktopLinks.SetActive( )
	[ ] AMPAdmin.CategoryManager.Click ()
	[ ] AMPCategoryManager.AddNewCategory.Click ()
	[ ] AMPAddCategory.N1.SetText("silk test category")
	[ ] AMPAddCategory.N2.SetText("STC")
	[ ] AMPAddCategory.HtmlTextField4.SetText("silk test value 2")
	[ ] AMPAddCategory.N4.SetText("silk test value 1")
	[ ] AMPAddCategory.Should.Check ()
	[ ] AMPAddCategory.ShouldTheValuesBePresented.Check ()
	[ ] AMPAddCategory.Submit.Click ()
	[ ] 
[+] testcase Test8_3() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] 
	[ ] AMPDesktopLinks.SetActive( )
	[ ] AMPAdmin.CategoryManager.Click ()
	[ ] AMPCategoryManager.CategoryManager.Actions.Ordered18.EditCategory.Click ()
	[ ] AMPAddCategory.N1.SetText("silk test 1")
	[ ] AMPAddCategory.Should.Uncheck ()
	[ ] AMPAddCategory.ShouldTheValuesBePresented.Uncheck ()
	[ ] AMPAddCategory.N2.SetText("STC1")
	[ ] AMPAddCategory.HtmlTextField4.SetText("silk 11")
	[ ] AMPAddCategory.N4.SetText("silk 21")
	[ ] AMPAddCategory.Submit.Click ()
	[ ] 
[+] testcase Test8_5() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] 
	[ ] AMPDesktopLinks.SetActive( )
	[ ] AMPAdmin.CategoryManager.Click ()
	[ ] AMPCategoryManager.CategoryManager.Actions.Ordered18.DeleteCategory.Click ()
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click ()
	[ ] 
[+] testcase Test10_1a() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPDesktopLinks.SetActive( )
	[ ] AMPAdmin.UserManager.Click ()
	[ ] AMPAllUsers.Keyword.SetText("_test")
	[ ] AMPAllUsers.Keyword.TypeKeys ("<Enter>")
	[ ] AMPAllUsers.Users.Actions.EditUser.Click ()
	[ ] AMPViewEditUser.Password.SetText(Decrypt("8UVtAZk="))
	[ ] AMPViewEditUser.Confirm.SetText(Decrypt("8UVtAZnN"))
	[ ] AMPViewEditUser.ChangePassword.Click ()
	[ ] AMPDesktopLinks.Logout.Click ()
	[ ] AMPDesktopLinks.AidManagementPlatformAMP.YouHaveBeenLoggedOut.ClickHereToGoBackToTheH.Click ()
	[ ] login("_test@amp.org","test1")
	[ ] 
[+] testcase Test10_1b() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPDesktopLinks.SetActive( )
	[ ] AMPAdmin.UserManager.Click ()
	[ ] AMPAllUsers.Keyword.SetText("_test")
	[ ] AMPAllUsers.Keyword.TypeKeys ("<Enter>")
	[ ] AMPAllUsers.Users.Actions.EditUser.Click ()
	[ ] AMPViewEditUser.SetActive( )
	[ ] AMPViewEditUser.FirstName.SetText("silk 1 ")
	[ ] AMPViewEditUser.LastName.SetText("test 1")
	[ ] AMPViewEditUser.Country.Select ("US Minor Outlying Islands")
	[ ] AMPViewEditUser.MailingAddress.SetText("a@a.org")
	[ ] AMPViewEditUser.OrganizationType.Select (2)
	[ ] AMPViewEditUser.OrganizationGroup.Select (2)
	[ ] AMPViewEditUser.OrganisationName.Select (2)
	[ ] AMPViewEditUser.VerifiedAssignedOrganisation.Select (2)
	[ ] AMPViewEditUser.Save.Click ()
	[ ] 
[+] testcase Test10_2() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPDesktopLinks.SetActive( )
	[ ] AMPAdmin.UserManager.Click ()
	[ ] AMPAllUsers.Keyword.SetText("_test")
	[ ] AMPAllUsers.Keyword.TypeKeys ("<Enter>")
	[ ] AMPAllUsers.SetActive( )
	[ ] AMPAllUsers.Users.BanUser.BanUser.Click ()
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click ()
	[ ] 
[+] testcase Test_PM_3() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] 
	[ ] AMPDesktopLinks.SetActive( )
	[ ] AMPAdmin.ComponentsManager.Click ()
	[ ] AMPComponentManager.AddComponent.Click ()
	[ ] AMPAddComponent.SetActive( )
	[ ] AMPAddComponent.ComponentTitle.SetText("_silk test component")
	[ ] AMPAddComponent.ComponentCode.SetText("12345")
	[ ] AMPAddComponent.ComponentDescription.SetText("description")
	[ ] AMPAddComponent.ComponentType.SetText("type")
	[ ] AMPAddComponent.Save.Click ()
	[ ] 
	[ ] 
[+] testcase Test_PM_1() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPDesktopLinks.SetActive( )
	[ ] AMPAdmin.ComponentsManager.Click ()
	[ ] AMPComponentManager.ComponentIndicatorManager.Click ()
	[ ] AMPComponentIndicatorManager.AddAComponentIndicator.Click ()
	[ ] AMPAddComponentIndicator.SetActive( )
	[ ] AMPAddComponentIndicator.N1.SetText("silk test")
	[ ] AMPAddComponentIndicator.Description.SetText("silking")
	[ ] AMPAddComponentIndicator.N2.SetText("ST")
	[ ] AMPAddComponentIndicator.Save.Click ()
	[ ] 
	[ ] 
[+] testcase Test_PM_2() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPDesktopLinks.SetActive( )
	[ ] AMPAdmin.ComponentsManager.Click ()
	[ ] AMPComponentManager.ComponentIndicatorManager.Click ()
	[ ] AMPComponentIndicatorManager.STC.Click ()
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click ()
	[ ] 
[+] testcase Test_PM_4() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPDesktopLinks.SetActive( )
	[ ] AMPAdmin.ComponentsManager.Click ()
	[ ] AMPComponentManager.SetActive( )
	[ ] AMPComponentManager.Components.Edit.Edit602.Click ()
	[ ] AMPEditComponent.SetActive( )
	[ ] AMPEditComponent.ComponentTitle.SetText ("_silk 1")
	[ ] AMPEditComponent.ComponentCode.SetText("121")
	[ ] AMPEditComponent.ComponentType.SetText("d1")
	[ ] AMPEditComponent.ComponentDescription.SetText("desc 1")
	[ ] AMPEditComponent.Save1.Click ()
	[ ] 
[-] testcase Test_PM_5() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPDesktopLinks.SetActive( )
	[ ] AMPAdmin.ComponentsManager.Click ()
	[ ] AMPComponentManager.SetActive( )
	[ ] AMPComponentManager.Components.Delete.Delete602.Click ()
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click ()
	[ ] 
[-] testcase Test_DF_1() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.CalendarManager.Click ()
	[ ] AMPFiscalCalendarManager.SetActive( )
	[ ] AMPFiscalCalendarManager.AddAFiscalCalendar.Click ()
	[ ] AMPFiscalCalendarManager.N1.SetText("test calendar")
	[ ] AMPFiscalCalendarManager.N2.SetText("2")
	[ ] AMPFiscalCalendarManager.N3.SetText("2")
	[ ] AMPFiscalCalendarManager.N4.SetText("-2")
	[ ] AMPFiscalCalendarManager.Description.SetText("desc")
	[ ] AMPFiscalCalendarManager.Save.Click ()
	[ ] 
[-] testcase Test_DF_2() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.CalendarManager.Click ()
	[ ] AMPFiscalCalendarManager.SetActive( )
	[ ] AMPFiscalCalendarManager.FiscalCalendars.Name.TestCalendar.Click ()
	[ ] AMPFiscalCalendarManager.N1.SetText("test calendar 1")
	[ ] AMPFiscalCalendarManager.N2.SetText("3")
	[ ] AMPFiscalCalendarManager.N3.SetText("3")
	[ ] AMPFiscalCalendarManager.N4.SetText("-3")
	[ ] AMPFiscalCalendarManager.Description.TypeKeys ("description")
	[ ] AMPFiscalCalendarManager.Save.Click ()
	[ ] 
[-] testcase Test_DF_3() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.CalendarManager.Click ()
	[ ] AMPFiscalCalendarManager.SetActive( )
	[ ] AMPFiscalCalendarManager.FiscalCalendars.Name.TestCalendar.Click ()
	[ ] AMPFiscalCalendarManager.DeleteThisCalendar.Click ()
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click ()
	[ ] 
[-] testcase Test_CM_1() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.CurrencyManager.Click ()
	[ ] AMPCurrencyManager.AddANewCurrency.Click ()
	[ ] AMPCurrencyEditor.SetActive( )
	[ ] AMPCurrencyEditor.CurrencyCode.SetText("AAA")
	[ ] AMPCurrencyEditor.CurrencyName.SetText("test currency")
	[ ] AMPCurrencyEditor.Country.Select ("Romania")
	[ ] AMPCurrencyEditor.Save.Click ()
	[ ] 
[-] testcase Test_CM_2() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.CurrencyManager.Click ()
	[ ] AMPCurrencyManager.NumberOfRecordsPerPage.SetText("20")
	[ ] AMPCurrencyManager.View.Click ()
	[ ] 
[-] testcase Test_CM_3() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.CurrencyManager.Click ()
	[ ] AMPCurrencyManager.SetActive( )
	[ ] AMPCurrencyManager.AAA.Click ()
	[ ] AMPCurrencyEditor.SetActive( )
	[ ] AMPCurrencyEditor.CurrencyCode.SetText("AAB")
	[ ] AMPCurrencyEditor.CurrencyName.SetText("test currency unu")
	[ ] AMPCurrencyEditor.Save.Click ()
	[ ] 
[-] testcase Test_CM_4() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.CurrencyManager.Click ()
	[ ] AMPCurrencyManager.SetActive( )
	[ ] AMPCurrencyManager.DeleteThisCurrency1.Click ()
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click ()
	[ ] 
[-] testcase Test_CRM_1() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.CurrencyRateManager.Click ()
	[ ] AMPCurrencyRates.AddNewExchangeRate.Click ()
	[ ] AMPCurrencyRateEditor.SetActive( )
	[ ] AMPCurrencyRateEditor.CurrencyEditor.Select ("CAD")
	[ ] DATETIME today
	[ ] STRING date1
	[ ] today = GetDateTime()
	[ ] date1 = FormatDateTime(today,"dd/mm/yyyy")
	[ ] BrowserPage.ExecLine('document.getElementById("date1").value = "'+date1+'"', TRUE)
	[ ] AMPCurrencyRateEditor.ExchangeRateValueOf1USD.SetText("20")
	[ ] AMPCurrencyRateEditor.Save.Click ()
	[ ] 
[-] testcase Test_CRM_3() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.CurrencyRateManager.Click ()
	[ ] AMPCurrencyRates.NumberOfRecordsPerPage.SetText("20")
	[ ] AMPCurrencyRates.View.Click ()
	[ ] 
[-] testcase Test_CRM_4() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.CurrencyRateManager.Click ()
	[ ] AMPCurrencyRateEditor.SetActive( )
	[ ] AMPCurrencyRateEditor.ExchangeRateValueOf1USD.SetText("30")
	[ ] AMPCurrencyRateEditor.Save.Click ()
	[ ] 
[-] testcase Test_CRM_5() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.CurrencyRateManager.Click ()
	[ ] AMPCurrencyRates.SetActive( )
	[ ] AMPCurrencyRates.CAD.Check ()
	[ ] AMPCurrencyRates.DeleteSelectedRates.Click ()
	[ ] 
[-] testcase Test_RM_1() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.RegionManager.Click ()
	[ ] AMPRegionManager.AddACountry.Click ()
	[ ] AMPRegionManager.CharactersLeft1.SetText("test country")
	[ ] AMPRegionManager.ISO.SetText("TC")
	[ ] AMPRegionManager.ISO3.SetText("TCC")
	[ ] AMPRegionManager.Save.Click ()
	[ ] 
[-] testcase Test_RM_2() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.RegionManager.Click ()
	[ ] AMPRegionManager.Country.Select ("Afghanistan")
	[ ] AMPRegionManager.AddARegion.Click ()
	[ ] AMPRegionManager.CharactersLeft1.SetText("test region")
	[ ] AMPRegionManager.Save.Click ()
	[ ] 
[-] testcase Test_RM_3() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.RegionManager.Click ()
	[ ] AMPRegionManager.Country.Select ("Afghanistan")
	[ ] AMPRegionManager.Region.Select ("test region")
	[ ] AMPRegionManager.EditThisRegion.Click ()
	[ ] AMPRegionManager.CharactersLeft1.SetText("test region 1")
	[ ] AMPRegionManager.Save.Click ()
	[ ] 
[-] testcase Test_RM_4() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.RegionManager.Click ()
	[ ] AMPRegionManager.Country.Select ("Afghanistan")
	[ ] AMPRegionManager.Region.Select ("test region 1")
	[ ] AMPRegionManager.AddAZone.Click ()
	[ ] AMPRegionManager.CharactersLeft1.SetText("test zone")
	[ ] AMPRegionManager.Save.Click ()
	[ ] 
[-] testcase Test_RM_5() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.RegionManager.Click ()
	[ ] AMPRegionManager.Country.Select ("Afghanistan")
	[ ] AMPRegionManager.Region.Select ("test region 1")
	[ ] AMPRegionManager.Zone.Select ("test zone")
	[ ] AMPRegionManager.EditThisZone.Click ()
	[ ] AMPRegionManager.CharactersLeft1.SetText("test zone 1")
	[ ] AMPRegionManager.Save.Click ()
	[ ] 
[-] testcase Test_RM_6() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.RegionManager.Click ()
	[ ] AMPRegionManager.Country.Select ("Afghanistan")
	[ ] AMPRegionManager.Region.Select ("test region 1")
	[ ] AMPRegionManager.Zone.Select ("test zone")
	[ ] AMPRegionManager.AddADistrict.Click ()
	[ ] AMPRegionManager.CharactersLeft1.SetText("test district")
	[ ] AMPRegionManager.Save.Click ()
	[ ] 
[-] testcase Test_RM_7() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.RegionManager.Click ()
	[ ] AMPRegionManager.Country.Select ("Afghanistan")
	[ ] AMPRegionManager.Region.Select ("test region 1")
	[ ] AMPRegionManager.Zone.Select ("test zone")
	[ ] AMPRegionManager.District.Select ("test district")
	[ ] AMPRegionManager.EditThisDistrict.Click ()
	[ ] AMPRegionManager.CharactersLeft1.SetText("test district 1")
	[ ] AMPRegionManager.Save.Click ()
	[ ] 
[-] testcase Test_RM_8() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.GlobalSettings.Click ()
	[ ] AMPGlobalSettings.DefaultCountry.Select ("Afghanistan")
	[ ] AMPGlobalSettings.Save1.Click ()
	[ ] AMPDesktopLinks.Logout.Click ()
	[ ] AMPDesktopLinks.AidManagementPlatformAMP.YouHaveBeenLoggedOut.ClickHereToGoBackToTheH.Click ()
	[ ] login("atl@amp.org","atl")
	[ ] //to be continued
	[ ] 
[-] testcase Test_SM_1() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.SectorManager.Click ()
	[ ] AMPViewSectorScheme.AddScheme.Click ()
	[ ] AMPAddSectorScheme.SchemeName.SetText("silk test scheme")
	[ ] AMPAddSectorScheme.SchemeCode.SetText("sts")
	[ ] AMPAddSectorScheme.Save.Click ()
	[ ] 
[-] testcase Test_SM_2() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.SectorManager.Click ()
	[ ] AMPViewSectorScheme.SetActive( )
	[ ] AMPViewSectorScheme.Schemes.DACCRSSectorScheme.SilkTestScheme.Click ()
	[ ] AMPViewSectorLevel1.AddSector.Click ()
	[ ] AMPViewSectorLevel1.SectorName.SetText("silk test sector")
	[ ] AMPViewSectorLevel1.SectorCode.SetText("st1")
	[ ] AMPViewSectorLevel1.Save1.Click ()
	[ ] 
[-] testcase Test_SM_3() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.SectorManager.Click ()
	[ ] AMPAdmin.SectorManager.Click ()
	[ ] AMPViewSectorScheme.SetActive( )
	[ ] AMPViewSectorScheme.Schemes.DACCRSSectorScheme.SilkTestScheme.Click ()
	[ ] AMPViewSectorLevel1.SetActive( )
	[ ] AMPViewSectorLevel1.SilkTestSector.Click ()
	[ ] AMPViewSectorLevel2.AddSector.Click ()
	[ ] AMPViewSectorLevel2.SectorName.SetText("silk level 2")
	[ ] AMPViewSectorLevel2.SectorCode.SetText("sl2")
	[ ] AMPViewSectorLevel2.Save1.Click ()
	[ ] 
[-] testcase Test_SM_4() appstate DefaultBaseState
	[ ] login("admin@amp.org","admin")
	[ ] AMPAdmin.SectorManager.Click ()
	[ ] AMPViewSectorScheme.SetActive( )
	[ ] AMPViewSectorScheme.Schemes.DACCRSSectorScheme.SilkTestScheme.Click ()
	[ ] AMPViewSectorLevel1.SetActive( )
	[ ] AMPViewSectorLevel1.SilkTestSector.Click ()
	[ ] AMPViewSectorLevel2.SetActive( )
	[ ] AMPViewSectorLevel2.SilkLevel2.Click ()
	[ ] AMPViewSectorLevel3.AddSector1.Click ()
	[ ] AMPDesktopLinks.SectorName.SetText("silk level 3")
	[ ] AMPDesktopLinks.SectorCode.SetText("sl3")
	[ ] AMPDesktopLinks.Save1.Click ()
	[ ] 
