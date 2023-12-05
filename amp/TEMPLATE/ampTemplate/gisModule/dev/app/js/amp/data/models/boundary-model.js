var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');


module.exports = Backbone.Model
  .extend(LoadOnceMixin).extend({
    // temporary so we get path to static files..
    url: function() {
      return '/gis/boundaries/' + this.get('country') + '/' + this.id + '.json';
    }

  });
