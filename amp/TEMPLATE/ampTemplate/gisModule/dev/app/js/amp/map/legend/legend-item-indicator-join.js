var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var chartUtils = require('../../../libs/local/chart-util');
var Template = fs.readFileSync(__dirname + '/legend-item-indicator-join.html', 'utf8');



module.exports = Backbone.View.extend({

  template: _.template(Template),
  className: 'legend-indicatorjoin',

  render: function() {
    var self = this;
    self.model.load().then(function() {

      // TODO drs prettify numbers
      if (self.model.palette) {
        self.$el.html(self.template(_.extend({}, self.model.toJSON(), {
          status: 'loaded',
          colourBuckets: self.model.palette.colours,
          unit:  self.model.get('currency') || self.model.get('data').unit,
          util: chartUtils
        })));
      } else {
        console.warn('no pallete...');
      }
    });
    return this;
  }
});
