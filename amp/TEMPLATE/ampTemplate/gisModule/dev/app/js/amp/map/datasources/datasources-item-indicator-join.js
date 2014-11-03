var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/datasourcs-item-indicator-join.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),

  render: function() {
    var self = this;
    self.model.load().then(function() {
      self.$el.html(self.template(_.extend({}, self.model.toJSON(), {
        status: 'loaded',
        colourBuckets: self.model.palette.colours,
        unit: self.model.get('data').unit
      })));
    });
    return this;
  }

});
