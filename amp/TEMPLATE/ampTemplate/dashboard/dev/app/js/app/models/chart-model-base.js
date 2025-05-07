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
    data.settings = this.app.settingsWidget.toAPIFormat();

    if (this.get('adjtype')) {
      // TODO adjtype hard-coding key for now, should get from settings...
      data.settings = _({}).extend(data.settings, {'funding-type': this.get('adjtype')});
    }

    if (this.get('programType') && this.get('showProgramType') === true) {
      data.settings = _({}).extend(data.settings, {'program-settings': this.get('programType')});
    }

    _.defaults(data.settings,{ 'currency-code': this.app.settingsWidget.definitions.getDefaultCurrencyId()});
    options.data = JSON.stringify(data);
    return BackboneDash.Model.prototype.fetch.call(this, options);
  }

});
