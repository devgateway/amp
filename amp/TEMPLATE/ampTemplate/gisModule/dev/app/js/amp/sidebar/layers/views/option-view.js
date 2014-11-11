var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/../templates/radio-option-template.html', 'utf8');


module.exports = Backbone.View.extend({

  app: null,
  template: _.template(Template),

  events: {
    'change input': 'toggleSelect'
  },

  initialize: function(options) {
    this.app = options.app;
    this.listenTo(this.model, 'change:selected', this.render);
  },

  render: function() {
    this.$el.html(this.template(this.model.attributes));
    this.app.translator.translateDOM(this.el); /* After to catch disabled */

    return this;
  },

  toggleSelect: function() {
    this.model.trigger('toggleSelect', this.model);
    this.app.translator.translateDOM(this.el);
  }

});
