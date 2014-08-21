/*https://gist.github.com/jonnyreeves/2474026*/
define(function() {

	"use strict";

	function TabEvents() {
		// Constructor
    function TabEvents() {}
	}

  // Some private method.
	function privatestub() {
	}

  // "Class" methods definition here.
	TabEvents.prototype = {
		constructor: TabEvents,
		onCreateTab: function(event, ui) {
			console.log('create tab');
      this.onActivateTab(event, ui);
		},
		onActivateTab: function(event, ui) {
			console.log('activate tab');
      console.log(event);            
      console.log(ui);
      
      //TODO: move this logic elsewhere.
      var panel = null;
      var selectedTabIndex = 0;
      if(ui.panel != undefined) {
        panel = ui.panel;
        selectedTabIndex = ui.tab.index();
      } else {
        panel = ui.newPanel;
        selectedTabIndex = ui.newTab.index();      
      }
      // Put loading animation.
      $(panel).html('<span><img src="/images/ajax-loader.gif"/></span>');
      // Simulate time consuming content.
      setTimeout(function(){
        console.log(app.tabs);
        var content = app.tabs.children.toArray()[selectedTabIndex].model.attributes.content;
        $(panel).html('<p>' + content + '</p>');
      }, 1000);                   
		}
	};

	return TabEvents;
});