define(
  [
    'underscore',
    'backbone',
    APP_ROOT + '/amp/sidebar/base-control/base-control-view.js',
    'text!' + APP_ROOT + '/amp/sidebar/data-sources/templates/data-sources-template.html'
  ],
  function (_, Backbone, BaseToolView, Template) {
    'use strict';

    var View = BaseToolView.extend({

      title:  'Data Source',
      iconClass:  'ampicon-data-sources',
      description:  '',

      template:  _.template(Template),

      initialize: function() {
        BaseToolView.prototype.initialize.apply(this);
      },

      render: function() {
        BaseToolView.prototype.render.apply(this);

        // add content
        this.$('.content').html(this.template({title: this.title}));
      }
    });

    return View;
  }
);
