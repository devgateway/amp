// Is this view used at all?

define(
  [
    'underscore',
    'backbone',
    'js/amp/sidebar/filters/models/base-filter-model.js',
    'text!' + APP_ROOT + '/amp/sidebar/filters/templates/filter-title-template.html',
    'text!' + APP_ROOT + '/amp/sidebar/filters/templates/filter-content-template.html'
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
        //TODO: currently title is parent of content. might cause issues.
        this.$el.click(function(evt){          
          self.launchFilter();
          evt.preventDefault();
        });    
      },

      launchFilter: function() {
        //TODO: putting filter in global div modal could cause issues for Backbone view event listening etc...
        //      maybe make the filter title one view, and the filter content another view...
        $('#filter-modal').html(this.contentTemplate());
        $('#filter-modal').modal('show');        
        // called when user clicks on a title.
        this.renderContent();
      },

      renderContent: function () {
        // render common box with apply button, reset button, etc.

      },

      apply: function () {
        // trigger common event for applying filters.
      },



    });

    return View;
  }
);
