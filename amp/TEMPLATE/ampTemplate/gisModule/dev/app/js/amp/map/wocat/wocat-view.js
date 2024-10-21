var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var WocatTable = require('./wocat-table-view');
const $ = require("jquery");
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
    self.app.data.generalSettings.load().then(function () {
      $.when(self.app.data.generalSettings.loaded).then(function () {
        console.log("App loaded",self.app);
        var wocatInitiativesEnabled = self.app.data.generalSettings.get('wocat-initiatives-enabled');
        console.log("Enabled", wocatInitiativesEnabled);
        if (!wocatInitiativesEnabled) {
          self.$el.css({
            'display': 'none'
          });
        }
        else
        {
          self.$el.css({
            'display': 'block'
          });

        }
      });
    });
    return self;
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
      left += 60;

      element.css({
        'left': left + 'px',
        'top': top + 'px',
        'position': 'fixed',
        'transition': 'all 0.3s ease-in-out'
      });
    }, 100);
  }


});
