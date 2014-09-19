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


  render:function() {
    var self = this;
    BaseControlView.prototype.render.apply(this);

    // add content
    this.$('.content').html(this.template({title: this.title}));

    _.each(this.filterViewsInstances, function(filterView) {
      self.$('.filter-list').append(filterView.renderTitle().titleEl);
    });

    this.popupFilterView = new PopupFilterView({app:this.app, el:this.$('#filter-popup')});

    return this;
  },


  newlaunchFilter:function() {
    var self = this;
    this.popupFilterView.render();
    this.popupFilterView.$el.show();

    // need to do better, but must close accordion or get weird states from bootstrap and manual filter showing....
    this.popupFilterView.on('close', function() {
      self.$('.accordion-body').collapse('hide');
    });
  }
});

