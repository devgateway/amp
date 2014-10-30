var fs = require('fs');
var _ = require('underscore');
var RadioListCollection = require('../collections/radio-list-collection');
var Backbone = require('backbone');
var OptionView = require('./option-view');
var Template = fs.readFileSync(__dirname + '/../templates/layers-template.html', 'utf8');


module.exports = Backbone.View.extend({
  id: 'tool-layers-sd-fund',
  title: 'Funding Data',
  iconClass: 'ampicon-layers',

  template: _.template(Template),

  initialize: function(options) {
    /* Remember that this should be mutually exclusive to the other things in this multisection*/
    this.app = options.app;

    /* For mutual exclusion, we use this reference to the parent */
    this.parentMultisectionControl = options.parent;

    this.collection = new RadioListCollection(this.app.data.admClustersTemp.models, {
      siblingGroupList: this.parentMultisectionControl.radioButtonGroup
    });

    this.listenTo(this.app.data.admClustersTemp, 'add', this.render);
  },

  render: function() {
    // TODO: find a better way to keep our proxy collection up to date
    // Thad do you know a good pattern for this?
    this.collection.reset(this.app.data.admClustersTemp.models);

    this.$el.html(this.template({title: this.title}));

    //this.$(this.el,this).html(this.template({title: this.title}));

    this.$('.layer-selector', this).html(this.collection.map(function(indicator) {
      return (new OptionView({ model: indicator })).render().el;
    }));

    return this;
  }

});
