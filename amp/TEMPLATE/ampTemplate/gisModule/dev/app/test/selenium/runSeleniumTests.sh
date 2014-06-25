# Start Selenium service
# setsid java -jar selenium-server-standalone-2.42.2.jar &
# PATH="/usr/local/lib/node_modules/phantomjs/bin:$PATH" java -jar selenium-server-standalone-2.42.2.jar

# java -jar selenium-server-standalone-2.42.2.jar -Dphantomjs.binary.path=/usr/local/lib/node_modules/phantomjs/bin

echo "Run Test Suite";
java -jar selenium-server-standalone-2.42.2.jar -htmlSuite "*firefox" "http://localhost:3000" "tests/suite-sidebar.html" "sidebar-results.html"

#java -jar selenium-server-standalone-2.42.2.jar -htmlSuite "*custom /usr/local/lib/node_modules/phantomjs/lib/phantom/bin/phantomjs" "http://localhost:3000" "tests/suite-sidebar.html" "sidebar-results.html"



# I couldn't get this to run tests from the Selenium IDE
# Launch the grid server, which listens on 4444 by default: 
#java -jar selenium-server-standalone-2.42.2.jar -role hub
# Register with the hub: 
#phantomjs --webdriver=8080 --webdriver-selenium-grid-hub=http://127.0.0.1:4444

# Now you can use your normal webdriver client with http://127.0.0.1:4444 and just request browserName: 
#phantomjs
