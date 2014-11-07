var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var Template = fs.readFileSync(__dirname + '/datasources-item-adm-clusters.html', 'utf8');


module.exports = Backbone.View.extend({
  tagName: 'tbody',

  template: _.template(Template),
  initialize: function(options) {
    this.app = options.app;

    _.bindAll(this, 'render');
  },

  render: function() {
    var self = this;
    this.collection.load().then(function() {

    _.each(self.collection.models, function(project) {

      self.$el.append(self.template({
          activity: project.attributes,
        }));

      });

    });
    return this;
  },


});
