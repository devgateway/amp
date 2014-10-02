var fs = require('fs');
var _ = require('underscore');

var Backbone = require('backbone');
var TitleTemplate = fs.readFileSync(__dirname + '/../templates/filter-title-template.html', 'utf8');
var ContentTemplate = fs.readFileSync(__dirname + '/../templates/filters-top-level-template.html', 'utf8');

var GenericFilterView = require('../views/generic-filter-view');
var GenericNestedFilterView = require('../views/generic-nested-filter-view');
var YearsFilterView = require('../views/years-filter-view');

var YearsFilterModel = require('../models/years-filter-model');

// Parent base view for fitlers.
module.exports = Backbone.View.extend({
  className:  'tab-pane',
  titleTemplate: _.template(TitleTemplate),
  contentTemplate: _.template(ContentTemplate),
  filterCollection: null,
  viewList:[],
  title: 'tbd',

  initialize:function(options) {
    this.title = options.title;
    this.filterCollection = new Backbone.Collection();
  },

  renderFilters: function() {
    var self = this;
    var view = null;
    var first = true;
    this.$el.attr('id', 'filter-pane-' + this.title);
    this.$el.html(this.contentTemplate());
    this.filterCollection.each(function(filter) {

      if (filter instanceof YearsFilterModel) {
        view = new YearsFilterView({
          model:filter,
          el: self.$('.sub-filters-content')
        });
        self.viewList.push(view);

      } else if (filter.get('title') === 'Organizations') {
        view = new GenericNestedFilterView({
          model:filter,
          el: self.$('.sub-filters-content')
        });
        self.viewList.push(view);

      } else {

        view = new GenericFilterView({
          model:filter,
          el: self.$('.sub-filters-content')
        });
        self.viewList.push(view);
      }

      self.$('.sub-filters-titles').append(view.renderTitle().$titleEl);

      // hacky way to open first one for now.
      if (first) {
        first = false;
        view.renderFilters();
        self.$('.sub-filters-titles li:first').addClass('active');
      }
    });

    return this;
  },

  renderTitle: function() {
    this.titleEl = this.titleTemplate({title: this.title});
    return this;
  }
});
