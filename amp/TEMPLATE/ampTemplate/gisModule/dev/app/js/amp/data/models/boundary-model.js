var Backbone = require('backbone');
var LoadOnceMixin = require('../../mixins/load-once-mixin');


module.exports = Backbone.Model
  .extend(LoadOnceMixin).extend({
    // temporary so we get path to static files..
    url: function() {
      return '/WEB-INF/gis/boundaries/' + this.get('country') + '/' + this.get('admLevel') + '.json';
    }

  });
