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
    this.listenTo(this, 'change:values', this.processValues);
    // this.listenTo(this, 'change:adjtype', this.fetch);
  },

  processValues: function(me, values) {
    values = values.slice();  // get a copy we can push to

    // make sure we don't have any duplicate keys... nvd3 pukes on those
    if (_(_(values).pluck('name')).uniq().length < values.length) {
      this.app.report('Data Error',
        ['The data for ' + this.get('name') + ' was inconsistent due to duplicate keys',
        'The chart will be shown, but it may have errors or other issues as a result.']);
    }

    if (this.get('maxLimit') > values.length) {
      values.push({
        name: 'Others',
        amount: this.get('total') -  // total minus the sum of what we have
          _.chain(values).pluck('amount').reduce(function(l, r) { return l + r; }, 0).value(),
        color: '#777'
      });
    }

    this.set('processed', {values: values});  // put in an object at key "values" for nvd3
  },

  fetch: function(options) {
    options = _.defaults(
      options || {},
      { data: this.pick('adjtype', 'limit') });
    return BackboneDash.Model.prototype.fetch.call(this, options);
  }

});
