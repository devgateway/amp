/*
* Load the site javascript
*
* 'gis' is the only module we actually bind to, the others are just required so
* they have a chance to be evaluated.
**/
define.amd.jQuery = true;
require(['jquery', "dojo/domReady!"], function() {
  require(["gis", "bootstrap", "js/log-safety.js"], function (GISView) {
    var gisView = new GISView();
    gisView.render();
  });
});
