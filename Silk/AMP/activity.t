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
	[ ] 
	[ ] 
[-] testcase Bla2 () appstate none
	[ ] CommentDetail.SetActive()
	[ ] 
[-] testcase Bla () appstate none
	[ ] EditData.SetActive()
	[ ] 
	[ ] //Agent.SetOption(OPT_VERIFY_ENABLED,  true)
	[ ] 
	[ ] //EditData.Bla.Title21.SetText("AAAAAAAAAAAAAAAAA")
	[ ] //EditData.Text.lsValue = {"<P>BBBB</P>", "<P> MATA </P>"}
	[ ] EditData.Text.lsValue = {"<P>Test Objective</P>"}
	[ ] 
	[ ] //print(BrowserPage.ExecLine ('document.getElementsByName("content")[0].value'))
	[ ] 
	[ ] //print(BrowserPage.ExecLine ('document.getElementsByName("content")[0].value = "<P>Fsfss</P>";'))
	[ ] //print(BrowserPage.ExecLine ('document.getElementsByName("content")[0].focus()'))
	[ ] //EditData.SaveNow.Click ()
