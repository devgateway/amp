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
    var data = JSON.parse(options.data);
    data.settings = this.app.settings.toAPI();

    if (this.get('adjtype')) {
      data.settings = _({}).extend(data.settings, {adjtype: this.get('adjtype')});
    }

    options.data = JSON.stringify(data);
    return BackboneDash.Model.prototype.fetch.call(this, options);
  }

});
