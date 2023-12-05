var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/legend-item-indicator-arcgis-feature.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),
  className: 'legend-indicatorarcgisfeature',
  initialize: function(options) {
    this.app = options.app;
  },

  render: function() {
    var drawLegend = _.bind(function() {
      /* we still need self for inside the promise */
      var self = this;

      this.app.translator.translateDOM(
        this.template(_.extend({}, this.model.toJSON(), {
          status: 'loaded',
          colourBuckets: this.model.palette.colours
        })
      )).then(function(legend) {
        self.$el.html(legend);
      });

    }, this);

    drawLegend();
    this.listenTo(this.model, 'change:min change:max', drawLegend);

    return this;
  }

});
