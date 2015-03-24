var Backbone = require('backbone');
var boilerplate = require('amp-boilerplate');


module.exports = Backbone.View.extend({

  className: 'row',

  initialize: function(options) {
    this.options = options;
  },

  render: function() {
    var $header = $('#amp-header');
    window.boilerh = $header;
	var headerWidget = new boilerplate.layout(
      {
        caller: 'GIS'
	  });
	window.boilerh = $header;
    this.$el.html(headerWidget.headerFooter.render().el);
    $header.html(headerWidget.menu.render().el);
    return this;
  }

});