define(['qunit'], function(QUnit) {

  var testPages = [];
  var ranPages = [];
  var testFrames = [];


  function registerIFrame($iFrame) {
    testFrames.push($iFrame);
  }


  function registerTestPage(path, pageTestFn) {
    testPages.push({path: path, pageTestFn: pageTestFn});
  }


  function activateFrame($iframe) {

    function runNextTask() {

      // grab the next available task
      var task = testPages.shift();
      if (! task) { // we are done; kill this task runner
        return;
      }

      // move this task to the queue of run tests
      ranPages.push(task);

      // set up our frame for the next test
      $iframe.attr('src', task.path);

      // record that this test tried to start
      task.started = true;
      task.completed = false;

      // when ready, run the test
      $iframe.on('load', function() {
        // grab jQuery from the test page and configure it for testing
        var frame$ = $iframe[0].contentWindow.jQuery;
        frame$.support.cssTransitions = false;
        frame$.fx.off = true;
        // run the test!
        task.pageTestFn(QUnit, frame$, finishTask);
      })

      function finishTask() {
        $iframe.off('load');
        task.completed = true;
        // start the next available task in this frame
        runNextTask();
      }
    }

    // start it up!
    runNextTask();
  }


  function runTests() {
    QUnit.start();
    testFrames.forEach(function(testFrame) {
      activateFrame(testFrame);
    });
  }


  return {
    registerIFrame: registerIFrame,
    testPage: registerTestPage,
    runTests: runTests
  }
});
