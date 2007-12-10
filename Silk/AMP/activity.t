[ ] 
[-] testcase Test1 () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] AMPMyDesktop.AddActivity.Click()
	[ ] AMPAddActivityStep1.ProjectTitle.SetText("Full Activity Silk Test")
	[ ] AMPAddActivityStep1.Objective.Click()
	[ ] 
	[ ] EditData.Click(1,100,100)
	[ ] EditData.TypeKeys("Test Objective")
	[ ] EditData.SaveNow.Click()
	[ ] 
	[ ] AMPAddActivityStep1.ObjAddEditObjectivelyVerifIndicators.Click ()
	[ ] CommentDetail.AddYourCommentHere.SetText("Obj Verf Indicators")
	[ ] CommentDetail.Save.Click()
	[ ] Sleep(1)
	[ ] CommentDetail.Close.Click()
	[ ] AMPAddActivityStep1.ObjAddEditAssumption.Click()
	[ ] CommentDetail.AddYourCommentHere.SetText("Assumption")
	[ ] CommentDetail.Save.Click()
	[ ] Sleep(1)
	[ ] CommentDetail.Close.Click()
	[ ] 
	[ ] AMPAddActivityStep1.ObjAddEditVerification.Click()
	[ ] CommentDetail.AddYourCommentHere.SetText("Verification")
	[ ] CommentDetail.Save.Click()
	[ ] Sleep(1)
	[ ] CommentDetail.Close.Click()
	[ ] 
	[ ] AMPAddActivityStep1.Description.Click()
	[ ] EditData.Click(1,100,100)
	[ ] EditData.TypeKeys("Test Description")
	[ ] EditData.SaveNow.Click()
	[ ] 
	[ ] AMPAddActivityStep1.Purpose.Click()
	[ ] EditData.Click(1,100,100)
	[ ] EditData.TypeKeys("Test Purpose")
	[ ] EditData.SaveNow.Click()
	[ ] 
	[ ] AMPAddActivityStep1.PurpAddEditObjectivelyVerifIndicators.Click ()
	[ ] CommentDetail.AddYourCommentHere.SetText("Purp Verf Indicators")
	[ ] CommentDetail.Save.Click()
	[ ] Sleep(1)
	[ ] CommentDetail.Close.Click()
	[ ] 
	[ ] AMPAddActivityStep1.PurpAddEditAssumption.Click()
	[ ] CommentDetail.AddYourCommentHere.SetText("Assumption")
	[ ] CommentDetail.Save.Click()
	[ ] Sleep(1)
	[ ] CommentDetail.Close.Click()
	[ ] 
	[ ] AMPAddActivityStep1.PurpAddEditVerification.Click()
	[ ] CommentDetail.AddYourCommentHere.SetText("Verification")
	[ ] CommentDetail.Save.Click()
	[ ] Sleep(1)
	[ ] CommentDetail.Close.Click()
	[ ] 
	[ ] AMPAddActivityStep1.Results.Click()
	[ ] EditData.Click(1,100,100)
	[ ] EditData.TypeKeys("Test Results")
	[ ] EditData.SaveNow.Click()
	[ ] 
	[ ] AMPAddActivityStep1.ResultsAddEditObjectivelyVerifIndicators.Click ()
	[ ] CommentDetail.AddYourCommentHere.SetText("Purp Verf Indicators")
	[ ] CommentDetail.Save.Click()
	[ ] Sleep(1)
	[ ] CommentDetail.Close.Click()
	[ ] 
	[ ] AMPAddActivityStep1.ResultsAddEditAssumption.Click()
	[ ] CommentDetail.AddYourCommentHere.SetText("Assumption")
	[ ] CommentDetail.Save.Click()
	[ ] Sleep(1)
	[ ] CommentDetail.Close.Click()
	[ ] 
	[ ] AMPAddActivityStep1.ResultsAddEditVerification.Click()
	[ ] CommentDetail.AddYourCommentHere.SetText("Verification")
	[ ] CommentDetail.Save.Click()
	[ ] Sleep(1)
	[ ] CommentDetail.Close.Click()
	[ ] 
	[ ] AMPAddActivityStep1.Lesson.Click()
	[ ] EditData.Click(1,100,100)
	[ ] EditData.TypeKeys("Test Lesson")
	[ ] EditData.SaveNow.Click()
	[ ] 
	[ ] AMPAddActivityStep1.AccessionInstrument.Select (2)
	[ ] AMPAddActivityStep1.ACChapter.Select (2)
	[ ] AMPAddActivityStep1.ActivityIsOnBudget.Check ()
	[ ] 
	[ ] AMPAddActivityStep1.FY.SetText("1")
	[ ] AMPAddActivityStep1.Vote.SetText("2")
	[ ] AMPAddActivityStep1.SubVote.SetText("3")
	[ ] AMPAddActivityStep1.SubProgram.SetText("4")
	[ ] AMPAddActivityStep1.ProjectCode.SetText("5")
	[ ] AMPAddActivityStep1.FinancialInstrument.Select (1)
	[ ] AMPAddActivityStep1.GovApprovalProc.Select(1)
	[ ] AMPAddActivityStep1.JointCriteria.Select(1)
	[ ] 
	[ ] AMPAddActivityStep1.AddOrganizations.Click ()
	[ ] 
	[ ] 
[-] testcase Test2 () appstate none
	[-] recording
		[ ] AMPAddActivityStep1.SetActive( )
		[ ] AMPDesktopLinks.SetActive( )
		[ ] AMPDesktopLinks.Search.Click ()
		[ ] AMPDesktopLinks.N111111111.Check ()
		[ ] AMPDesktopLinks.Add.Click ()
		[ ] AMPAddActivityStep1.SetActive( )
		[ ] AMPAddActivityStep1.N11111111.SetText("1234")
		[ ] AMPAddActivityStep1.LineMinistryRank.Select ("1")
		[ ] AMPAddActivityStep1.MinistryOfPlanningRank.Select ("1")
