var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');

var Backbone = require('backbone');
var TitleTemplate = fs.readFileSync(__dirname + '/../templates/filter-title-template.html', 'utf8');
var ContentTemplate = fs.readFileSync(__dirname + '/../templates/filter-content-template.html', 'utf8');

require('jquery-ui/draggable'); // ?not sure if working...

// Parent base view for fitlers.
module.exports = Backbone.View.extend({

  className: 'filter-type layer-heading-container',

  titleTemplate: _.template(TitleTemplate),
  contentTemplate: _.template(ContentTemplate),

  events: {
    'click .filter-title': 'launchFilter'
  },

  initialize: function(options) {
    this.app = options.app;

  },

  renderTitle: function() {
    var self = this;
    this.app.translator.translateDOM(
      this.titleTemplate(this.model.toJSON())).then(
        function(newEl) {
          self.$el.html(newEl);
        });

    return this;
  },


  // called when user clicks on a title.
  launchFilter: function() {

    this.renderContent();
  },

  // render common box with apply button, cancel button, etc.
  renderContent: function() {
    var self = this;

    //TODO: move out of global namespace
    this.$el.append($('#filter-popup'));
    $('#filter-popup').html(this.contentTemplate(this.model.toJSON()));
    $('#filter-popup').show();
    $('#filter-popup').on('click', '.cancel', self.cancel);
    $('#filter-popup').on('click', '.apply', self.apply);

    this.renderFilters();

    //setup any popovers as needed...
    this.popovers = this.$('[data-toggle="popover"]');
    this.popovers.popover();

    // Translate
    this.app.translator.translateDOM(this.el);

    return this;
  },

  renderFilters: function() {

  },

  apply: function() {
    // TODO: consider a different name to avoid collision with javascript function.apply
    // trigger common event for applying filters.
    // this.convertTreeToJSONFilter(); //implemented by child, and if not fallback to base.
    $('#filter-popup').hide();
  },

  cancel: function() {
    $('#filter-popup').hide();
  }

});
