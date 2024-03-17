var fs = require('fs');
var _ = require('underscore');
var BaseControlView = require('../../base-control/base-control-view');
var ResultsView = require('../views/search-results-view');
var SearchWidget = fs.readFileSync(__dirname + '/../templates/search-control-widget.html', 'utf8');
var Template = fs.readFileSync(__dirname + '/../templates/search-template.html', 'utf8');


var searchWidget = _.template(SearchWidget);

module.exports = BaseControlView.extend({

  id: 'tool-search',
  title: 'Keyword Search',
  iconClass: 'ampicon-search',
  description: '',

  template: _.template(Template),

  events: {
    'submit .search-form': 'renderResults'
  },

  render: function() {
    BaseControlView.prototype.render.apply(this, arguments);

    // add content
    this.$('.content').html(this.template({
      title: this.title,
      searchWidget: searchWidget
    }));

    return this;
  },

  renderResults: function(e) {
    if (e) {
      e.preventDefault();  // if triggered by an event (ie. submit), cancel it
    }
    var resultsView = new ResultsView();
    this.$('.results-placeholder').html(resultsView.render().el);
    this.$('.results-placeholder .modal').modal({show: true, backdrop: false});
  }
});
