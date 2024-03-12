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
    var saving = app.translator.translateSync("amp.dashboard:saving-state","Saving dashboard state, please wait...");
	  
    this.$('#dash-share-url')
      .attr('disabled', 'disabled')
      .val(saving);

    var stateBlob = this.app.state.freeze();

    this.listenToOnce(this.app.savedDashes, 'request', function(model, xhr) {
      // this has to be set up before .create, so we don't miss it
      xhr
        .done(_(function() {
          var id = model.get('id');
          this.app.url.hash(this.app.state.toHash(id), { silent: true });
          this.$('#dash-share-url')
            .removeAttr('disabled')
            .val(this.app.url.full());
        }).bind(this))
        .fail(_(function() {
          this.$('#dash-share-url').val('Error: could not save dashboard for sharing.');
        }).bind(this));
    });

    this.app.savedDashes.create({  // create does POST
      title: 'Dashboard',
      description: 'Saved dashboard',
      stateBlob: stateBlob
    }, { app: this.app });

    this.$('.dash-share-modal').modal();
  }

});
