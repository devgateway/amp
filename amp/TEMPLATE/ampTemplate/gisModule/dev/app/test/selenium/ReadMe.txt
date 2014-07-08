Latest attempt works with PhantomJS, but requires some custom java code after each export from IDE, and not all tests work in PhantomJS
1. Use selenium IDE firefox plugin to make test.

2. save tests.

3. export test as java file. (can use batch export plugin to quickly export entire test suite.)

4. setup eclipse with selenium following this tutorial:
http://assertselenium.com/2012/10/28/how-to-setup-a-webdriver-project-in-eclipse/

5. run selenium tests as JUnit Tests.
http://assertselenium.com/2013/03/25/getting-started-with-ghostdriver-phantomjs/


======================
!!Currently running from command line still opens a browser, haven't gotten headless working yet.

To Run
------
1. Download 'selenium-server-standalone-2.42.2.jar' from Selenium website and put it in this folder.
2. Run the sh file: sh runSeleniumTests.sh



Resources
----------

Running on Jenkins
http://www.labelmedia.co.uk/blog/setting-up-selenium-server-on-a-headless-jenkins-ci-build-machine.html

Running on PhantomJS
http://code.tutsplus.com/tutorials/headless-functional-testing-with-selenium-and-phantomjs--net-30545

http://stackoverflow.com/questions/10359586/running-selenium-ide-tests-via-selenium-grid
