[ ] 
[-] testcase Test1My () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] AMPDesktopLinks.TopMenu.ContentRepository.Click()
	[ ] //AMPDocumentManager.DM.MyDocuments.AddFile.Click()
	[ ] AMPDocumentManager.AddFile1.Click ()
	[ ] 
	[ ] //AMPDocumentManager.Browse.Click()
	[ ] //ChooseFile.SetActive ()
	[ ] //ChooseFile.MyDocuments.Click ()
	[ ] //ChooseFile.ListView1.DoubleSelect ("test.txt")
	[ ] AMPDocumentManager.Path.SetText("C:\Documents and Settings\Administrator\My Documents\test.txt")
	[ ] AMPDocumentManager.SetActive( )
	[ ] AMPDocumentManager.Title.SetText("Test Title")
	[ ] AMPDocumentManager.Description.SetText("Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description ")
	[ ] 
	[ ] AMPDocumentManager.Submit.Click ()
	[ ] 
	[-] //AMPDocumentManager.DM.MyDocuments.TestDoc.VerifyProperties ({...})
		[ ] //""
		[-] //{...}
			[ ] //{"Exists",               TRUE}
			[ ] //{"Text",                 "test.doc"}
	[+] AMPDocumentManager.TestDoc.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "test.txt"}
	[ ] 
[-] testcase Test1Team () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] AMPDesktopLinks.TopMenu.ContentRepository.Click()
	[ ] AMPDocumentManager.Empty.TeamDocuments.AddFile.Click ()
	[ ] AMPDocumentManager.Browse.Click()
	[ ] ChooseFile.SetActive ()
	[ ] ChooseFile.MyDocuments.Click ()
	[ ] ChooseFile.ListView1.DoubleSelect ("test.doc")
	[ ] AMPDocumentManager.SetActive( )
	[ ] AMPDocumentManager.Title.SetText("Test2 Title")
	[ ] AMPDocumentManager.Description.SetText("Test2 description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description Test description ")
	[ ] 
	[+] AMPDocumentManager.Submit.Click ()
		[-] AMPDocumentManager.Empty.TeamDocuments.TestDoc.VerifyProperties ({...})
			[ ] ""
			[-] {...}
				[ ] {"Exists",               TRUE}
				[ ] {"Text",                 "test.doc"}
	[ ] 
[-] testcase Test2My () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] AMPDesktopLinks.TopMenu.ContentRepository.Click()
	[ ] AMPDocumentManager.DM.MyDocuments.Plus1.Click()
	[ ] AMPDocumentManager.Browse.Click()
	[ ] ChooseFile.SetActive ()
	[ ] ChooseFile.MyDocuments.Click ()
	[ ] ChooseFile.ListView1.DoubleSelect ("test2.doc")
	[ ] AMPDocumentManager.SetActive( )
	[ ] //AMPDocumentManager.Title.SetText("Test Title v2")
	[ ] //AMPDocumentManager.Description.SetText("This is the next installation of the version for this document")
	[ ] 
	[ ] AMPDocumentManager.Submit.Click ()
	[ ] 
	[-] AMPDocumentManager.DM.MyDocuments.Test2Doc1.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Enabled",              TRUE}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "test2.doc"}
	[ ] 
[-] testcase Test2Team () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] AMPDesktopLinks.TopMenu.ContentRepository.Click()
	[ ] AMPDocumentManager.Empty.TeamDocuments.Plus2.Click ()
	[ ] AMPDocumentManager.Browse.Click()
	[ ] ChooseFile.SetActive ()
	[ ] ChooseFile.MyDocuments.Click ()
	[ ] ChooseFile.ListView1.DoubleSelect ("test2.doc")
	[ ] AMPDocumentManager.SetActive( )
	[ ] //AMPDocumentManager.Title.SetText("Test2 Title v2")
	[ ] //AMPDocumentManager.Description.SetText("Test2 description version 2")
	[ ] 
	[ ] AMPDocumentManager.Submit.Click ()
	[ ] 
	[-] AMPDocumentManager.Empty.TeamDocuments.Test2Doc.VerifyProperties ({...})
		[ ] ""
		[-] {...}
			[ ] {"Enabled",              TRUE}
			[ ] {"Exists",               TRUE}
			[ ] {"Text",                 "test2.doc"}
	[ ] 
[-] testcase Test3My () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] AMPDesktopLinks.TopMenu.ContentRepository.Click()
	[ ] AMPDocumentManager.DM.MyDocuments.D1.Click()
	[ ] BrowserWarning.SetActive ()
	[ ] BrowserMessage.Cancel.Click ()
	[ ] 
	[ ] AMPDocumentManager.DM.MyDocuments.H1.Click()
	[ ] 
[-] testcase Test3Team () appstate DefaultBaseState
	[ ] 
[-] testcase Test4My () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] AMPDesktopLinks.TopMenu.ContentRepository.Click()
	[ ] AMPDocumentManager.DM.MyDocuments.Del1.Click()
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click()
	[ ] 
[-] testcase Test4Team () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] AMPDesktopLinks.TopMenu.ContentRepository.Click()
	[ ] AMPDocumentManager.Empty.TeamDocuments.Del2.Click()
	[ ] BrowserMessage.SetActive ()
	[ ] BrowserMessage.OK.Click()
	[ ] 
[ ] 
[ ] 
[-] testcase Test5My () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] AMPDesktopLinks.TopMenu.ContentRepository.Click()
	[ ] AMPDocumentManager.DM.MyDocuments.AddFile.Click()
	[ ] AMPDocumentManager.Submit.Click ()
	[ ] AMPDocumentManager.SetActive()
	[ ] 
	[-] do
		[ ] FuzzyVerify(AMPDocumentManager.PleaseSpecifyATitlePleas.sFullCaption,"Please specify a title ! Please specify a file path !")
	[-] except
		[ ] AppError(" Title and file requirements FAIL!")
	[ ] 
	[ ] AMPDocumentManager.Title.SetText("Test Add for Required Fields")
	[ ] AMPDocumentManager.Submit.Click ()
	[ ] 
	[ ] AMPDocumentManager.SetActive()
	[-] do
		[ ] FuzzyVerify(AMPDocumentManager.PleaseSpecifyAFile.sFullCaption,"Please specify a file path !")
	[-] except
		[ ] AppError("File requirements FAIL!")
	[ ] 
	[ ] AMPDocumentManager.Title.SetText("")
	[ ] 
	[ ] AMPDocumentManager.Browse.Click()
	[ ] ChooseFile.SetActive ()
	[ ] ChooseFile.MyDocuments.Click ()
	[ ] ChooseFile.ListView1.DoubleSelect ("test.doc")
	[ ] AMPDocumentManager.SetActive( )
	[ ] AMPDocumentManager.Submit.Click ()
	[ ] 
	[-] do
		[ ] FuzzyVerify(AMPDocumentManager.PleaseSpecifyAFile.sFullCaption,"Please specify a title !")
	[-] except
		[ ] AppError("Title requirements FAIL!")
	[ ] 
[ ] 
[-] testcase Test5Team () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] AMPDesktopLinks.TopMenu.ContentRepository.Click()
	[ ] AMPDocumentManager.Empty.TeamDocuments.AddFile.Click()
	[ ] AMPDocumentManager.Submit.Click ()
	[ ] AMPDocumentManager.SetActive()
	[ ] 
	[-] do
		[ ] FuzzyVerify(AMPDocumentManager.PleaseSpecifyATitlePleas.sFullCaption,"Please specify a title ! Please specify a file path !")
	[-] except
		[ ] AppError(" Title and file requirements FAIL!")
	[ ] 
	[ ] AMPDocumentManager.Title.SetText("Test Add for Required Fields")
	[ ] AMPDocumentManager.Submit.Click ()
	[ ] 
	[ ] AMPDocumentManager.SetActive()
	[-] do
		[ ] FuzzyVerify(AMPDocumentManager.PleaseSpecifyAFile.sFullCaption,"Please specify a file path !")
	[-] except
		[ ] AppError("File requirements FAIL!")
	[ ] 
	[ ] AMPDocumentManager.Title.SetText("")
	[ ] 
	[ ] AMPDocumentManager.Browse.Click()
	[ ] ChooseFile.SetActive ()
	[ ] ChooseFile.MyDocuments.Click ()
	[ ] ChooseFile.ListView1.DoubleSelect ("test.doc")
	[ ] AMPDocumentManager.SetActive( )
	[ ] AMPDocumentManager.Submit.Click ()
	[ ] 
	[-] do
		[ ] FuzzyVerify(AMPDocumentManager.PleaseSpecifyAFile.sFullCaption,"Please specify a title !")
	[-] except
		[ ] AppError("Title requirements FAIL!")
	[ ] 
[-] testcase Test5My2 () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] AMPDesktopLinks.TopMenu.ContentRepository.Click()
	[ ] AMPDocumentManager.DM.MyDocuments.AddFile.Click()
	[ ] AMPDocumentManager.Title.SetText("Test Add for Erroneus Fields")
	[ ] AMPDocumentManager.Path.SetText("C;;///test.doc")
	[ ] AMPDocumentManager.Submit.Click()
	[ ] 
	[-] do
		[ ] FuzzyVerify(AMPDocumentManager.PleaseSpecifyAFile.sFullCaption,"Please specify a title !")
	[-] except
		[ ] AppError("AMP-2221")
	[ ] 
[ ] 
[-] testcase Test6My () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] AMPDesktopLinks.TopMenu.ContentRepository.Click()
	[ ] AMPDocumentManager.DM.MyDocuments.Pub1.Click()
	[ ] sleep(1)
	[-] do
		[ ] FuzzyVerify(AMPDocumentManager.DM.MyDocuments.Priv1.sFullCaption,"Click here to unpublish this document")
	[-] except
		[ ] AppError("Can't make document Public!")
	[ ] 
[-] testcase Test6Team () appstate DefaultBaseState
	[ ] login("atl@amp.org", "atl")
	[ ] AMPDesktopLinks.TopMenu.ContentRepository.Click()
	[ ] AMPDocumentManager.Empty.TeamDocuments.Pub2.Click()
	[ ] sleep(1)
	[-] do
		[ ] FuzzyVerify(AMPDocumentManager.Empty.TeamDocuments.Priv2.sFullCaption,"Click here to unpublish this document")
	[-] except
		[ ] AppError("Can't make document Public!")
	[ ] 
[ ] 
[ ] 
