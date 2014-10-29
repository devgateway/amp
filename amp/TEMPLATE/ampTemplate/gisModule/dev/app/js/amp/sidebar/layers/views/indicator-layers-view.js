var fs = require('fs');
var _ = require('underscore');
var RadioListCollection = require('../collections/radio-list-collection');
var BaseControlView = require('../../base-control/base-control-view');
var OptionView = require('./option-view');
var Template = fs.readFileSync(__dirname + '/../templates/layers-template.html', 'utf8');


module.exports = BaseControlView.extend({
  id: 'tool-layers-sd',
  title: 'Statistical Data',
  iconClass: 'ampicon-layers',
  description: 'View indicators on sub-national needs.',

  template: _.template(Template),

  initialize: function() {
    BaseControlView.prototype.initialize.apply(this, arguments);  // sets this.app and this.baseTemplate
    this.collection = new RadioListCollection(this.app.data.indicators.models);
    this.listenTo(this.app.data.indicators, 'add', this.render);
  },

  render: function() {
    // TODO: find a better way to keep our proxy collection up to date
    // Thad do you know a good pattern for this?
    this.collection.reset(this.app.data.indicators.models);
    BaseControlView.prototype.render.apply(this);

    this.$('.content').html(this.template({title: this.title}));

    this.$('.layer-selector').html(this.collection.map(function(indicator) {
      return (new OptionView({ model: indicator })).render().el;
    }));

    return this;
  }

});
