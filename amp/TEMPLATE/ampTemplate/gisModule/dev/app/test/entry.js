var QUnit = require('qunitjs');
var runner$ = require('jquery');
var frame = require('./util/frame.js');

// Frame code currently not working sinze switch from dojo. 
// Probably not needed for unit testing.
  // register all of the available test iframes
  var $iFrames = runner$('.test-frame');
  $iFrames.each(function(i, frameEl) {
    var $iFrame = runner$(frameEl);
    frame.registerIFrame($iFrame);
  });

  // // register the progress indicators
  var table = runner$('#test-frames-status');
  frame.registerStatus(function(statusObj) {
    runner$('.tests-queued', table).text(statusObj.queued);
    runner$('.tests-running', table).text(statusObj.running);
    runner$('.tests-finished', table).text(statusObj.finished);
  });



//Run tests:
//require('./scripts/basemap.js');
require('./scripts/map.js');

// start testing!
 frame.runTests();
