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
    this.$el.toggleClass('expanded');  // Toggle the 'expanded' class

    // Apply dynamic styles for centering the element
    if (this.$el.hasClass('expanded')) {
      // Dynamically set styles when expanded
      this.$el.css({
        'display': 'block',
        'width': '500px',
        'max-height': '100vh',
        'overflow-y': 'auto',
        'position': 'fixed',
        'z-index': '1000'
      });

      // Center the element in the viewport
      this.centerElement();
    }
    else
    {
      this.$el.removeAttr('style');
    }

    return false; // Stops it from updating the URL
  },

  centerElement: function() {
    const element = this.$el;

    // Delay to ensure height is recalculated after the element is expanded
    setTimeout(function() {
      const windowWidth = $(window).width();
      const windowHeight = $(window).height();
      const elementWidth = element.outerWidth();
      const elementHeight = element.outerHeight();

      var left = (windowWidth - elementWidth) / 2;
      const top = (windowHeight - elementHeight) / 2;
      left+=60;
      // Apply the centered position styles
      element.css({
        'left': left + 'px',
        'top': top + 'px'
      });
    }, 100);  // Small delay to recalculate the height after expansion
  }


});
