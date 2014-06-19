define(
  [
    'underscore',
    'backbone',
    APP_ROOT + '/amp/sidebar/filters/views/base-filter-view.js',
    APP_ROOT + '/amp/sidebar/filters/models/years-filter-model.js'
  ],
  function (_, Backbone, BaseFilterView, YearsFilterModel) {
    'use strict';

    var View = BaseFilterView.extend({

      initialize: function() {
        BaseFilterView.prototype.initialize.apply(this);

        this.model = new YearsFilterModel();
      },

      renderTitle: function() {
        BaseFilterView.prototype.renderTitle.apply(this);


      },

      // Called when user clicks on a title.
      renderContent: function() {
        BaseFilterView.prototype.renderContent.apply(this);


      },

    });

    return View;
  }
);
