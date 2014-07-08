var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/../templates/map-legend-template.html', 'utf8');

module.exports = Backbone.View.extend({

  template: _.template(Template),

  id: 'legend',

  render: function () {
    this.$el.html(this.template());
    return this;

    //TODO: chevron toggle: http://jsfiddle.net/zessx/R6EAW/12/
  }

});
