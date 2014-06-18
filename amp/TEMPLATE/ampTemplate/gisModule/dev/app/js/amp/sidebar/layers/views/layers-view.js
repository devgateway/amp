define(
  [
    'underscore',
    'backbone',
    APP_ROOT + '/amp/sidebar/base-control/base-control-view.js',
    'text!' + APP_ROOT + '/amp/sidebar/layers/templates/layers-template.html'
  ],
  function (_, Backbone, BaseToolView, Template) {
    'use strict';

    var LayersView = BaseToolView.extend({

      title: 'Layers',
      iconClass: 'ampicon-layers',
      description: 'Tool desc, remove if possible.',

      template: _.template(Template),

      initialize: function() {
        BaseToolView.prototype.initialize.apply(this);
      },

      render: function(){
        BaseToolView.prototype.render.apply(this);

        // add content
        this.$('.content').html(this.template({title: this.title}));
      }
    });

    return LayersView;
  }
);
