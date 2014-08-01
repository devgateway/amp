var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');

var BaseControlView = require('../../base-control/base-control-view');
var OptionView = require('./option-view');
var Template = fs.readFileSync(__dirname + '/../templates/layers-template.html', 'utf8');


module.exports = BaseControlView.extend({
  id: 'tool-layers',
  title: 'Statistical Data',
  iconClass: 'ampicon-layers',
  description: 'Country indicators.',

  template: _.template(Template),

  initialize: function() {
    BaseControlView.prototype.initialize.apply(this, arguments);  // sets this.app and this.baseTemplate
    this.listenTo(this.app.data.indicators, 'add', this.render);
  },

  render: function() {
    BaseControlView.prototype.render.apply(this);

    this.$('.content').html(this.template({title: this.title}));

    this.$('.layer-selector').html(this.app.data.indicators.map(function(indicator) {
      return (new OptionView({ model: indicator })).render().el;
    }));

    return this;
  }

});
