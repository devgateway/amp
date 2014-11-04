var param = require('jquery').param;
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');


module.exports = BackboneDash.Model.extend({

  defaults: {
    adjtype: 'ac',
    limit: 5
  },

  initialize: function(attrs, options) {
    this.app = options.app;
    this.url = options.url;
  },

  parse: function(data) {
    var values = _(data.values.slice()).map(function(v) {
      return {
        x: v.name,
        y: v.amount
      };
    });

    // make sure we don't have any duplicate keys... nvd3 pukes on those
    if (_(_(values).pluck('x')).uniq().length < values.length) {
      this.app.report('Data Error',
        ['The data for ' + this.get('name') + ' was inconsistent due to duplicate keys',
        'The chart will be shown, but it may have errors or other issues as a result.']);
    }

    if (data.maxLimit > values.length) {
      values.push({
        x: 'Others',
        y: data.total -  // total minus the sum of what we have
          _.chain(values).pluck('y').reduce(function(l, r) { return l + r; }, 0).value(),
        color: '#777'
      });
    }

    data.processed = [{values: values}];
    return data;
  },

  fetch: function(options) {
    options = _.defaults(
      options || {},
      { url: this.url + '?' + param(this.pick('adjtype', 'limit')) });
    return BackboneDash.Model.prototype.fetch.call(this, options);
  }

});
