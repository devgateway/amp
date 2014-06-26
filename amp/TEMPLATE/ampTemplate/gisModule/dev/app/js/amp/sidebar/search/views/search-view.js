define(
  [
    'underscore',
    'amp/sidebar/base-control/base-control-view',
    'amp/sidebar/search/views/search-results-view',
    'text!amp/sidebar/search/templates/search-control-widget.html',
    'text!amp/sidebar/search/templates/search-template.html'
  ],
  function (_, BaseToolView, ResultsView, SearchWidget, Template, ResultsTemplate) {
    'use strict';

    var searchWidget = _.template(SearchWidget);

    var View = BaseToolView.extend({

      id: 'tool-search',
      title: 'Keyword Search',
      iconClass: 'ampicon-search',
      description: '',

      template: _.template(Template),

      events: {
        'submit .search-form': 'renderResults'
      },

      render: function() {
        BaseToolView.prototype.render.apply(this);

        var self = this;

        // add content
        this.$('.content').html(this.template({
          title: this.title,
          searchWidget: searchWidget,
        }));

        return this;
      },

      renderResults: function() {
        var resultsView = new ResultsView();
        this.$('.results-placeholder').html(resultsView.render().el);
        this.$('.results-placeholder .modal').modal({show: true, backdrop: false});
      }
    });

    return View;
  }
);


