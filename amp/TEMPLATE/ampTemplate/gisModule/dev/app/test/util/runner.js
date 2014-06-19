require(
  [
    'jquery',
    'qunit',
    '/test/util/frame.js'
  ],
  function(runner$, QUnit, frame) {

    // register all of the available test iframes
    var $iFrames = runner$('.test-frame');
    $iFrames.each(function(i, frameEl) {
      $iFrame = runner$(frameEl);
      frame.registerIFrame($iFrame);
    });

    // load all the tests
    require([
      '/test/scripts/load-dom-stuff.js',
      '/test/scripts/sidebar-accordion.js'
    ], function() {
      // start testing!
      frame.runTests();
    });
  }
);
