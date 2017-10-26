var fs = require('fs');
var jQuery = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
//loading hacks

require('jquery-ui/draggable');
Backbone.$ = jQuery;
// boostrap looks for jquery in the global namespace, so put it there.
window.jQuery = jQuery;


var bootstrap_enabled = (typeof $().modal == 'function');
if (bootstrap_enabled) {
  require('bootstrap/dist/js/bootstrap');
}

var MainView = require('./views/main-view');

function Widget() {
	  this.initialize.apply(this, arguments);
}

_.extend(Widget.prototype, Backbone.Events, {

	  initialize: function(options) {
		  options = _.defaults(options, { draggable: true });
		  this.view = new MainView(options);
		  
		  this.listenTo(this.view, 'all', function() {
		      this.trigger.apply(this, arguments);
		    });
	  },
	  show: function() {
		 this.view.show();
	  },
	  
	  setElement: function() {
		    this.view.setElement(arguments);
		},

});
module.exports = Widget;
window.gisLayersManager = Widget;