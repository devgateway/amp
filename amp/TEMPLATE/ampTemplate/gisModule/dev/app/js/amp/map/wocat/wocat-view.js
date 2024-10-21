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
    console.log("APp", this.app);
    var wocatInitiativesEnabled = this.app.data.generalSettings.attribues['wocat-initiatives-enabled'];
    console.log("WocatInitiatives",wocatInitiativesEnabled);
    if (wocatInitiativesEnabled===true) {
      _.bindAll(this, 'render');
    }
  },

  render: function() {
    var self = this;

    self.$el.html(self.template());
    var content = new WocatTable({
        app: this.app
      }).render().el;

    self.$('.wocat-content', self.$el).html(content);

      this.$el.append(this.wocatView.render().el);
    /* TODO Reintroduce the "Loading" image in the table template */
    return this;
  },

  toggleWocat: function() {
    this.$el.toggleClass('expanded');

    if (this.$el.hasClass('expanded')) {
      this.$el.css({
        'display': 'block',
        'width': '500px',
        'max-height': '100vh',
        'overflow-y': 'auto',
        'position': 'fixed',
        'z-index': '1000',
        'transition': 'all 0.3s ease-in-out'
      });

      this.centerElement();
    } else {
      // Add transition when collapsing
      this.$el.css({
        'transition': 'all 0.3s ease-in-out',
        'width': '0',
        'height': '0'
      });

      // Remove style attributes after the collapse animation completes
      setTimeout(() => {
        this.$el.removeAttr('style');
      }, 300);
    }

    return false;
  },

  centerElement: function() {
    const element = this.$el;

    setTimeout(function() {
      const windowWidth = $(window).width();
      const windowHeight = $(window).height();
      const elementWidth = element.outerWidth();
      const elementHeight = element.outerHeight();

      var left = (windowWidth - elementWidth) / 2;
      const top = (windowHeight - elementHeight) / 2;
      // left += 60;

      element.css({
        'left': 800 + 'px',
        'top': top + 'px',
        'position': 'fixed',
        'transition': 'all 0.3s ease-in-out'
      });
    }, 100);
  }


});
