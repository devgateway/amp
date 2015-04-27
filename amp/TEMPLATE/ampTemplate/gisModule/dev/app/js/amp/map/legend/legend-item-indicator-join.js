var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var chartUtils = require('../../../libs/local/chart-util');
var Template = fs.readFileSync(__dirname + '/legend-item-indicator-join.html', 'utf8');



module.exports = Backbone.View.extend({

  template: _.template(Template),
  className: 'legend-indicatorjoin',
  initialize: function(options) {
    this.app = options.app;
  },

  render: function() {
    var self = this;
    self.model.load().then(function() {

      if (self.model.palette) {
        /* Use translator promise in this legend to ensure translator loads in time too--
         * extra check is needed here but not on others because
         * this legend is the default fallthrough of its parent class
         * and it gets called several times in the page load.
         */
        self.app.translator.promise.then(function() {
          self.app.translator.translateDOM(
            self.template(_.extend({}, self.model.toJSON(), {
              status: 'loaded',
              colourBuckets: self.model.palette.colours,
              unit:  self.model.get('unit'),
              util: chartUtils
            }
          ))).then(function(legend) {
            self.$el.html(legend);
          });
        });

      }
    });

    return this;
  }
});
