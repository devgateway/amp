var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/legend-item-indicator-arcgis-feature.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),
  className: 'legend-indicatorarcgisfeature',

  render: function() {
    var drawLegend = _.bind(function() {
      this.$el.html(this.template(_.extend({}, this.model.toJSON(), {
        status: 'loaded',
        colourBuckets: this.model.palette.colours
      })));
    }, this);

    drawLegend();
    this.listenTo(this.model, 'change:min change:max', drawLegend);

    return this;
  }

});
