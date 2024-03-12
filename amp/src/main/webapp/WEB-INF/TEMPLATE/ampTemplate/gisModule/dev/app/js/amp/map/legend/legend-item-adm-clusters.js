var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/legend-item-adm-clusters.html', 'utf8');


module.exports = Backbone.View.extend({

  template: _.template(Template),
  className: 'legend-admcluster',

  initialize: function(options) {
    this.app = options.app;
  },

  render: function() {
    var self = this;

    this.model.load().done(function() {
      self.app.translator.translateDOM(
       self.template(self.model.toJSON())).then(
       function(newEl) {
         self.$el.html(newEl);
       });
    });
    return this;
  }

});
