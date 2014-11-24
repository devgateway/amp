var _ = require('underscore');
var BackboneDash = require('../backbone-dash');


module.exports = BackboneDash.Model.extend({

  defaults: {
    typed: true,
    limit: 3
  },

  initialize: function(attrs, options) {
    this.app = options.app;
    this.url = options.url;

    this._prepareTranslations();
  },

  _prepareTranslations: function() {
    var self = this;
    var ftypeBaseLanguage = {};

    /* Prepare the translations for the chart */
    var chartName = ['amp.dashboard:chart-', this.get('name').replace(/ /g, ''), '-'].join('');

    /*
     * TODO: load all the localizations in this chart's namespace to this array
     * from initial-translation-request.json -- For now just hardcode the two sorts.
     */
    if (this.get('name') === 'Funding Type') {
      ftypeBaseLanguage[chartName + 'Grant'] = 'Grant';
      ftypeBaseLanguage[chartName + 'Loan'] = 'Loan';
      ftypeBaseLanguage[chartName + 'others'] = 'Others';
    }

    this.localizedFType = this.app.translator.translateList(ftypeBaseLanguage).then(
      function(localizedKeyVal) {
        self.localizedLookup = localizedKeyVal;
      });
  },

  parse: function(data) {
    var self = this;

    // TODO: use filters info to trim years (api-side?)
    var years = _(data.values)
      .chain()
      .filter(function(y) {
        return parseInt(y.Year, 10) >= 2009 && parseInt(y.Year, 10) <= 2014;
      })
      .sortBy('Year')
      .value();

    var chartName = ['amp.dashboard:chart-', this.get('name').replace(/ /g, ''), '-'].join('');
    var localizedOthers = self.localizedLookup[chartName + 'others'];

    // reformat the data for nvd3
    data.processed = _(years)
      .chain()
      .reduce(function(series, year) {
        series.push.apply(series, _(year.values).pluck('type'));
        return series;
      }, [])
      .uniq()
      .map(function(s) {
        var cleanName = s.replace(/[ :.]/g, '');
        var localizedName = s;
        if (self.localizedLookup[chartName + cleanName]) {
          localizedName = self.localizedLookup[chartName + cleanName];
        }
        return {
          key: localizedName,
          values: _(years).map(function(y) {
            var yearValue = _(y.values).findWhere({type: s});
            return {
              x: parseInt(y.Year, 10),
              y: yearValue && yearValue.amount || 0
            };
          })
        };
      })
      .value();

    // group smallest contributors as "other"s
    if (this.get('limit') < data.processed.length) {
      var othersNames = _(data.processed)
        .chain()
        .map(function(series) {
          return {
            key: series.key,
            total: _(series.values).reduce(function(t, v) { return t + v.y; }, 0)
          };
        })
        .sortBy('total')
        .reverse()
        .rest(this.get('limit') - 2)  // 1 for .length offset, 1 for .rest offset
        .pluck('key')
        .value();

      var othersSeries = {
        key: localizedOthers,
        color: '#777',
        values: _(data.processed)
          .chain()
          .filter(function(series) { return _(othersNames).contains(series.key); })
          .map(function(series) { return series.values; })
          .transpose()
          .map(function(othersYear) {
            return {
              x: othersYear[0].x,
              y: _(othersYear).reduce(function(t, s) { return t + s.y; }, 0)
            };
          })
          .value()
      };
      data.processed = data.processed.slice(0, this.get('limit'));
      data.processed.push(othersSeries);
    }

    return data;
  }

});
