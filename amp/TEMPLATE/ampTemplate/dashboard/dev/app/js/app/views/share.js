var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/share.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  events: {
    'click .dash-share-button': 'share'
  },

  initialize: function(options) {
    this.app = options.app;
  },

  render: function() {
    this.$el.html(template({ details: {} }));
    return this;
  },

  share: function() {
    this.app.state.freeze({ toURL: true });
    this.$('#dash-share-url').val(this.app.url.full());
    this.$('.dash-share-modal').modal();
  }

});
