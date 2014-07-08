var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var SearchWidget = fs.readFileSync(path.join(__dirname, '../templates/search-control-widget.html'));
var Template = fs.readFileSync(path.join(__dirname, '../templates/search-results-template.html'));


var searchWidget = _.template(SearchWidget);

module.exports = Backbone.View.extend({

  template: _.template(Template),

  render: function() {

    // add content
    this.$el.html(this.template({
      searchWidget: searchWidget
      // with results!
    }));

    return this;
  },
});
