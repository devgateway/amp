var BackboneDash = require('../backbone-dash');
var boilerplate = require('amp-boilerplate');


module.exports = BackboneDash.View.extend({

  className: 'row',

  initialize: function(options) {
    this.app = options.app;
  },

  render: function() {
	var headerWidget = new boilerplate.layout(
      {
        caller: 'DASHBOARD'
	  });
	window.boilerh = $header;
    this.$el.html(headerWidget.headerFooter.render().el);
    return this;
  }

});
