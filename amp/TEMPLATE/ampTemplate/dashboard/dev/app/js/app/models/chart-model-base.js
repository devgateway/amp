var _ = require('underscore');
var BackboneDash = require('../backbone-dash');


module.exports = BackboneDash.Model.extend({

  defaults: {},

  initialize: function(attrs, options) {
    this.app = options.app;
    this.url = options.url;
    this._prepareTranslations();  // TODO: refactor this implementation into here
  },

  hasData: function() {
    return _(this.get('processed'))
      .chain()
      .pluck('values')
      .reduce(function(result, values) {
        return values.length && true || result;
      }, false)
      .value();
  },

  fetch: function(options) {
    if (this.get('adjtype')) {
      var data = JSON.parse(options.data);
      _(data.settings || (data.settings = {})).extend({adjtype: this.get('adjtype')});
      options.data = JSON.stringify(data);
    }
    return BackboneDash.Model.prototype.fetch.call(this, options);
  }

});
