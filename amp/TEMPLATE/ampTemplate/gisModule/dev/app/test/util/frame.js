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

        // grab AMD stuff from the frame
        var frameRequire = $iframe[0].contentWindow.require;
        // run the test (after loading backbone stuff)!
        frameRequire(['/js/main.js'], function() {
          // This seems to still race with other stuff
          // being loaded. Adding a little extra timeout
          // helps keep the tests results consistent.
          window.setTimeout(function() {
            task.pageTestFn(QUnit, frame$, finishTask);
          }, 50);
        });
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
