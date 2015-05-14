var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');


module.exports = Backbone.Model
.extend(LoadOnceMixin).extend({
  // temporary so we get path to static files..
  url: function() {
    return '/TEMPLATE/ampTemplate/gisModule/dev/app/mock-api/data/boundaries/'+ this.get("country") + '/' + this.id + '.json';
  }

});
