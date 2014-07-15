var QUnit = require('qunitjs');

var testPages = [],
    ranPages = [],
    testFrames = [],
    setStatus = function() {};


function registerIFrame($iFrame) {
  testFrames.push($iFrame);
}


function registerTestPage(path, pageTestFn) {
  testPages.push({path: path, pageTestFn: pageTestFn});
}


function registerStatus(statusFn) {
  setStatus = statusFn;
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
    task.finished = false;

    // when ready, run the test
    $iframe.on('load', function() {
      // grab jQuery from the test page and configure it for testing
      var frame$ = $iframe[0].contentWindow.jQuery;
      frame$.support.cssTransitions = false;
      frame$.fx.off = true;

      // grab AMD stuff from the frame
      var frameRequire = $iframe[0].contentWindow.require;
      // run the test (after loading backbone stuff)!
      frameRequire(['amp'], function() {
        // This seems to still race with other stuff
        // being loaded. Adding a little extra timeout
        // helps keep the tests results consistent.
        window.setTimeout(function() {
          task.pageTestFn(QUnit, frame$, finishTask);
        }, 50);
      });
    });

    function finishTask() {
      $iframe.off('load');
      task.finished = true;
      // start the next available task in this frame
      runNextTask();
      // update frame status
      updateStatus();
    }
  }

  // start it up!
  runNextTask();
}


function updateStatus() {
  var queued = testPages.length;
  var finished = ranPages.filter(function(task) {
    return task.finished;
  }).length;
  var running = ranPages.length - finished;
  setStatus({
    queued: queued,
    finished: finished,
    running: running
  });
}


function runTests() {
  QUnit.start();
  updateStatus();
  testFrames.forEach(function(testFrame) {
    activateFrame(testFrame);
  });
}



module.exports = {
  registerIFrame: registerIFrame,
  testPage: registerTestPage,
  registerStatus: registerStatus,
  runTests: runTests
};
