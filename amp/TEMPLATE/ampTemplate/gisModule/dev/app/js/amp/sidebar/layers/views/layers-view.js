define(
  [
    'underscore',
    'backbone',
    'amp/sidebar/base-control/base-control-view',
    'text!amp/sidebar/layers/templates/layers-template.html'
  ],
  function (_, Backbone, BaseToolView, Template) {
    'use strict';

    var LayersView = BaseToolView.extend({

      id: 'tool-layers',
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

        return this;
      }
    });

    return LayersView;
  }
);
