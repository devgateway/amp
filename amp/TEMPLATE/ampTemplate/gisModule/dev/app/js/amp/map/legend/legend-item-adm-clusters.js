var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/legend-item-adm-clusters.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),
  className: 'legend-admcluster',

  render: function() {
    var self = this;
    this.model.load().then(function() {
      self.$el.html(self.template(self.model.toJSON()));
    });
    return this;
  }

});
