Selenium info on confluence:
https://wiki.dgfoundation.org/display/ITRI/Automated+Browser+Testing#AutomatedBrowserTesting-Selenium:BrowserMacros


To Run in browser or create tests
---------------------------------

1. Download and instal Selenium plugin from Selenium website: http://www.seleniumhq.org/download/
2. Open the desired test-suite from the tests folder.
3. Run it!



To Run in browser from command line
-----------------------------------

1. Download 'selenium-server-standalone-2.44.0.jar' from Selenium website: http://www.seleniumhq.org/download/
2. Put the jar it in this folder.
3. Update the config variables in runSeleniumTests.sh with the correct AMP url and test suite name.
4. Run the sh file: sh runSeleniumTests.sh
5. This will open firefox and run the test suite for you and save the results.
6. You may also just run:
    java -jar selenium-server-standalone-2.44.0.jar -htmlSuite "*firefox" "http://localhost:8080"  "tests/suite-gis.html"  "tests/suite-gis-results.html"



To Run tests from command line without a browser
------------------------------------------------

Latest attempt works with PhantomJS, but requires some custom java code after each export from IDE, 
and not all tests work in PhantomJS
1. Use selenium IDE firefox plugin to make test.

2. save tests.

3. export test as java file. (can use batch export plugin to quickly export entire test suite.)

4. setup eclipse with selenium following this tutorial:
http://assertselenium.com/2012/10/28/how-to-setup-a-webdriver-project-in-eclipse/

5. run selenium tests as JUnit Tests.
http://assertselenium.com/2013/03/25/getting-started-with-ghostdriver-phantomjs/




Resources
----------

Running on Jenkins
http://www.labelmedia.co.uk/blog/setting-up-selenium-server-on-a-headless-jenkins-ci-build-machine.html

Running on PhantomJS
http://code.tutsplus.com/tutorials/headless-functional-testing-with-selenium-and-phantomjs--net-30545

http://stackoverflow.com/questions/10359586/running-selenium-ide-tests-via-selenium-grid

My StackOverflow question:
http://stackoverflow.com/questions/24636413/selenium-ide-and-headless-browser-testing
