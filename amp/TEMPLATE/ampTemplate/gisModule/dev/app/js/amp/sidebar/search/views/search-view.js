var fs = require('fs');
var _ = require('underscore');
var BaseToolView = require('../../base-control/base-control-view');
var ResultsView = require('../views/search-results-view');
var SearchWidget = fs.readFileSync(path.join(__dirname, '../templates/search-control-widget.html'));
var Template = fs.readFileSync(path.join(__dirname, '../templates/search-template'));


var searchWidget = _.template(SearchWidget);

module.exports = BaseToolView.extend({

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
