var fs = require('fs');
var $ = require('jquery');
var Backbone = require('backbone');
var _ = require('underscore');
var Template = fs.readFileSync(__dirname + '/../templates/about-template.html', 'utf8');

module.exports = Backbone.View.extend({
  template: _.template(Template),
  id: 'aboutModal',  
  initialize: function(options) {
	  this.app = options.app;
    _.bindAll(this, 'render');

  },

  render: function() {
	var self = this;
	_.defaults(window, {ampVersion: "0", buildDate: "0"});
    this.$el.html(self.template({
      ampVersion: window.ampVersion,
      buildDate: window.buildDate
    }));
    return this;
  }
  
});

