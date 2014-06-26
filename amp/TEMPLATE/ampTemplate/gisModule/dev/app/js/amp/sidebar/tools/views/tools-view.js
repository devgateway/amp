define(
  [
    'underscore',
    'backbone',
    'amp/sidebar/base-control/base-control-view',
    'text!amp/sidebar/tools/templates/tools-template.html'
  ],
  function (_, Backbone, BaseToolView, Template) {
    'use strict';

    var View = BaseToolView.extend({

      title:  'Tools',
      iconClass:  'ampicon-tools',
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
