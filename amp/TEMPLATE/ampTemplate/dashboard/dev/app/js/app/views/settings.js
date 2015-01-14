var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');
var ModalView = require('./settings-modal');
var template = _.template(fs.readFileSync(
  __dirname + '/../templates/settings.html', 'UTF-8'));


module.exports = BackboneDash.View.extend({

  events: {
    'click .dash-settings-button': 'editSettings'
  },

  initialize: function(options) {
    this.app = options.app;
    this.modalView = new ModalView({ app: this.app, collection: this.collection });

    this.app.settings.load().done(_(function() {
      this.app.state.register(this, 'settings', {
        get: this.app.settings.toAPI,
        set: this.app.settings.fromState
      });
    }).bind(this));
  },

  render: function() {
    this.$el.html(template());
    return this;
  },

  editSettings: function() {
    this.app.modal('Settings', {  // TODO: translate "Settings"
      specialClass: 'dash-settings-modal',
      bodyEl: this.modalView.render().el
    });
  }

});
