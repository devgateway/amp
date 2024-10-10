var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var WocatTable = require('./wocat-table-view');
var Template = fs.readFileSync(__dirname + '/wocat-template.html', 'utf8');

module.exports = Backbone.View.extend({

  className: 'wocat',

  template: _.template(Template),

  events: {
    'click a[href="#toggle-wocat-collapse"]': 'toggleWocat'
  },

  initialize: function(options) {
    this.app = options.app;
    _.bindAll(this, 'render');
  },

  render: function() {
    var self = this;

    self.$el.html(self.template());
    var content = new WocatTable({
        app: this.app
      }).render().el;

    self.$('.wocat-content', self.$el).html(content);
    /* TODO Reintroduce the "Loading" image in the table template */
    return this;
  },

  toggleWocat: function() {
    this.$el.toggleClass('expanded');  // Toggle the expanded class
    if (this.$el.hasClass('expanded')) {
      this.centerElement();  // Center the element if it is expanded
    }
    return false;  // Prevent default URL behavior
  },

// Center the element in the middle of the page
  centerElement: function() {
    const element = this.$el;

    // Get the dimensions of the window and the element
    const windowWidth = $(window).width();
    const windowHeight = $(window).height();
    const elementWidth = element.outerWidth();
    const elementHeight = element.outerHeight();

    // Calculate centered position
    const left = (windowWidth - elementWidth) / 2;
    const top = (windowHeight - elementHeight) / 2;

    // Apply the centered position styles
    element.css({
      position: 'fixed',  // Make the element position relative to the viewport
      left: left + 'px',
      top: top + 'px',
      zIndex: 1000  // Adjust z-index to ensure it appears above other elements
    });
  }


});
