var fs = require('fs');
var $ = require('jquery');
var _ = require('underscore');
var Backbone = require('backbone');
var ZeroClipboard = require('zeroclipboard');

var Template = fs.readFileSync(__dirname + '/../templates/share-link-template.html', 'utf-8');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  events: {
    'focus #amp-gis-share-link': 'selectLink'
  },

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this.model, 'sync', this.showLink);
  },

  render: function() {
    var self = this;
    this.app.translator.translateDOM(this.template()).then(
        function(sharelinktemplate) {
          self.$el.html(sharelinktemplate);
        }
      );

    // set up and render the zeroclipboard copy thing
    var button = this.$('[data-toggle="tooltip"]');
    button.tooltip({container: 'body'});
    new ZeroClipboard(button)
      .on('error', function() {  // if flash is unavailable
        console.warn('Flash player not detected, copy-to-clipboard button won\'t work');
        this.destroy();
        button.unwrap().remove();
      });

    return this;
  },

  showLink: function() {
    this.app.url.hash(this.app.state.toHash(this.model.id), {silent: true});
    var fullLink = this.app.url.full();
    this.$('.share-url')
      .val(fullLink)
      .attr('data-clipboard-text', fullLink);
    $('#map-loading').hide();
  },

  selectLink: function(e) {
    _.defer(function() { e.currentTarget.select(); });  // after the cursor has set
  }

});
