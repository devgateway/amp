define(
  [
    'underscore',
    'backbone',

    'amp/sidebar/filters/models/base-filter-model',
    'text!amp/sidebar/filters/templates/filter-title-template.html',
    'text!amp/sidebar/filters/templates/filter-content-template.html',

    'jqueryui/draggable',
  ],
  function (_, Backbone, FilterModel, TitleTemplate, ContentTemplate) {
    'use strict';

    // Parent base view for fitlers.
    var View = Backbone.View.extend({

      titleTemplate: _.template(TitleTemplate),
      contentTemplate: _.template(ContentTemplate),

      initialize: function () {

      },

      renderTitle: function () {
        var self = this;

        this.$el.html(this.titleTemplate(this.model.toJSON()));

        // TODO: replace checkbox with a custom div since user doesn't actually input, it's read only.
        if(this.model.get('activeCount') == this.model.get('totalCount')){
          this.$('input:checkbox').prop('checked', true);
          this.$('input:checkbox').prop('indeterminate', false);
        } else if(this.model.get('activeCount') > 0){
          this.$('input:checkbox').prop('checked', false);
          this.$('input:checkbox').prop('indeterminate', true);
        } else {
          this.$('input:checkbox').prop('checked', false);
          this.$('input:checkbox').prop('indeterminate', false);
        }

        // Add listener to title.
        this.$('.filter-title').click(function(evt){
          self.launchFilter();
          evt.preventDefault();
        });
      },


      // called when user clicks on a title.
      launchFilter: function() {

        this.renderContent();
      },

      // render common box with apply button, cancel button, etc.
      renderContent: function () {

        this.$('.modal-placeholder').html(this.contentTemplate(this.model.toJSON()));
        this.$('.modal-placeholder .modal').modal({show: true, backdrop: false});
        this.$('.modal-placeholder .modal-dialog').draggable({ cancel: '.modal-body, .cancel', cursor: 'move'  });

      },

      apply: function () {
        // trigger common event for applying filters.
        // this.convertTreeToJSONFilter(); //implemented by child, and if not fallback to base.
      },



    });

    return View;
  }
);
