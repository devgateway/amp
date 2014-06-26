/*
* Load the site javascript
*
* 'gis' is the only module we actually bind to, the others are just required so
* they have a chance to be evaluated.
**/
define.amd.jQuery = true;
require(['jquery', 'domReady!'], function() {
  require(['amp/gis/views/gis-view', 'bootstrap', 'js/log-safety'], function (GISView) {
    var gisView = new GISView();
    gisView.render();
  });
});
