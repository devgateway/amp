var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');

var Backbone = require('backbone');
var TitleTemplate = fs.readFileSync(__dirname + '/../templates/filter-title-template.html', 'utf8');
var ContentTemplate = fs.readFileSync(__dirname + '/../templates/filters-content-template.html', 'utf8');

require('jquery-ui/draggable'); // ?not sure if working...

// Parent base view for fitlers.
module.exports = Backbone.View.extend({

  className: 'filter-type tab-pane',

  titleTemplate: _.template(TitleTemplate),
  contentTemplate: _.template(ContentTemplate),

  initialize:function(options) {
    this.app = options.app;

  },

  renderFilters: function() {
    $(this.el).attr('id', 'filter-pane-' + this.model.get('title'));
  }
});
