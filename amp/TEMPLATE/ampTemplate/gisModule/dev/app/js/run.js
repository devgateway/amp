(function helloWorld() {
  define.amd.jQuery = true;

  // configures package locaitons
  require(['/js/config.js'], function() {

    // now that we have package locations, grab jq
    require(['jquery'], function() {

      // bootstrap looks for jq global...
      require(['bootstrap', '/js/log-safety.js', 'domReady!'], function() {

        // showtime
        require(['amp']);

      });
    });

  });

})();
