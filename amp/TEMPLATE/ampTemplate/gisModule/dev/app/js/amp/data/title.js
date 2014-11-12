var _ = require('underscore');
var Backbone = require('backbone');


var Title = function() {
  this.initialize.apply(this, arguments);
};

_.extend(Title.prototype, Backbone.Events, {

  current: '',

  initialize: function(options) {
    this.data = options.data;

    this.listenTo(this.data, 'show hide', this.updateTitle);
  },

  updateTitle: function() {
    // currently this just joins all the titles of the layers on ", "
    // ... but it should probably be smarter.
    var titles = this.data.getAllVisibleLayers().map(function(layer) {
      return layer.get('title');
    }).value();  // getLayers() returns a underscore chain()

    this.current = titles.join(', ');
    this.trigger('update', this.current);
  }

});


module.exports = Title;
