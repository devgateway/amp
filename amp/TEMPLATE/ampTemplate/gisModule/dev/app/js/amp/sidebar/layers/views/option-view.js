var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/../templates/radio-option-template.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  events: {
    'change input': 'toggleSelect'
  },

  initialize: function() {
    this.listenTo(this.model, 'change:selected', this.render);
  },

  render: function() {
    this.$el.html(this.template(this.model.attributes));
    return this;
  },

  toggleSelect: function() {
    this.model.collection.toggleSelect(this.model);
  }

});
