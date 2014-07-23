var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');

var Backbone = require('backbone');
var FilterModel = require('../models/base-filter-model');
var TitleTemplate = fs.readFileSync(__dirname + '/../templates/filter-title-template.html', 'utf8');
var ContentTemplate = fs.readFileSync(__dirname + '/../templates/filter-content-template.html', 'utf8');

require('jquery-ui/draggable'); // ?not sure if working...

// Parent base view for fitlers.
module.exports = Backbone.View.extend({

  className: 'filter-type layer-heading-container',

  titleTemplate: _.template(TitleTemplate),
  contentTemplate: _.template(ContentTemplate),

  events: {
    'click .filter-title': 'launchFilter'
  },

  renderTitle: function () {
    var self = this;

    this.$el.html(this.titleTemplate(this.model.toJSON()));

    // TODO: replace checkbox with a custom div since user doesn't actually input, it's read only.
    if(this.model.get('activeCount') === this.model.get('totalCount')){
      this.$('input:checkbox').prop('checked', true);
      this.$('input:checkbox').prop('indeterminate', false);
    } else if(this.model.get('activeCount') > 0){
      this.$('input:checkbox').prop('checked', false);
      this.$('input:checkbox').prop('indeterminate', true);
    } else {
      this.$('input:checkbox').prop('checked', false);
      this.$('input:checkbox').prop('indeterminate', false);
    }

    return this;
  },


  // called when user clicks on a title.
  launchFilter: function() {

    this.renderContent();
  },

  // render common box with apply button, cancel button, etc.
  renderContent: function () {

    this.$('.modal-placeholder').html(this.contentTemplate(this.model.toJSON()));
    this.$('.modal-placeholder .modal').modal({show: true, backdrop: false});
    this.$('.modal-placeholder .modal-dialog').draggable({ cancel: '.modal-body, .modal-footer', cursor: 'move'  });

    this.renderFilters();

    return this;
  },

  renderFilters: function () {
    
  },

  apply: function () {
    // TODO: consider a different name to avoid collision with javascript function.apply
    // trigger common event for applying filters.
    // this.convertTreeToJSONFilter(); //implemented by child, and if not fallback to base.
  },


});
