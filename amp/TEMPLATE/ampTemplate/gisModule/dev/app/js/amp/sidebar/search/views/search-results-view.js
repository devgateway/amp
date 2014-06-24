define(
  [
    'underscore',
    'backbone',
    'text!' + APP_ROOT + '/amp/sidebar/search/templates/search-control-widget.html',
    'text!' + APP_ROOT + '/amp/sidebar/search/templates/search-results-template.html'
  ],
  function (_, Backbone, SearchWidget, Template) {
    'use strict';

    var searchWidget = _.template(SearchWidget);

    var View = Backbone.View.extend({

      template: _.template(Template),

      render: function() {

        // add content
        this.$el.html(this.template({
          searchWidget: searchWidget
          // with results!
        }));
      },
    });

    return View;
  }
);
