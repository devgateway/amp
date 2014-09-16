var fs = require('fs');
var _ = require('underscore');

var PopupFilterView = require('../views/popup-filters-view');
var BaseControlView = require('../../base-control/base-control-view');
var Template = fs.readFileSync(__dirname + '/../templates/filters-sidebar-template.html', 'utf8');


module.exports = BaseControlView.extend({
  id: 'tool-filters',
  title: 'Filters',
  iconClass: 'ampicon-filters',
  description: 'Apply filters to the map.',
  apiURL: '/rest/filters',

  events:{
    'click .accordion-heading': 'newlaunchFilter'
  },

  // collection of child views..
  filterViewsInstances:[],

  template: _.template(Template),

  initialize:function(options) {
    this.app = options.app;
    BaseControlView.prototype.initialize.apply(this, arguments);
  },


  //==========================
  // GIS sidebar specific code:
  //==========================
  render:function() {
    var self = this;
    BaseControlView.prototype.render.apply(this);

    // add content
    this.$('.content').html(this.template({title: this.title}));

    _.each(this.filterViewsInstances, function(filterView) {
      self.$('.filter-list').append(filterView.renderTitle().el);
    });

    this.popupFilterView = new PopupFilterView({app:this.app, el:this.$('#filter-popup')});

    return this;
  },

  //==========================
  // Generic Filter code
  //==========================
  newlaunchFilter:function() {
    this.popupFilterView.render();
  }
});

