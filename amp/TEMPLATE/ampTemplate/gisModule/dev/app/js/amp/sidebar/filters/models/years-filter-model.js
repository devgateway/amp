var BaseFilterModel = require('../models/base-filter-model');

module.exports = BaseFilterModel.extend({

  //TODO: serialize-deserialize functions

  initialize:function(options) {
    BaseFilterModel.prototype.initialize.apply(this, [options]);
    this.set({
      title: 'Years',
      selectedStart: 1990,
      selectedEnd: 2014,
      // range is provided by api, but will fallback to this if ot provided, or set to -1
      startYear: 1960,
      endYear: 2015
    });
  },

  parse:function(data) {
    if (!data.startYear || data.startYear === -1) {
      data.startYear = this.attributes.startYear;
    }

    if (!data.endYear || data.endYear === -1) {
      data.endYear = this.attributes.endYear;
    }

    this.get('_loaded').resolve();
    return data;
  },

  serialize: function(){
    //TODO: return format filter API expects
    return {
      startYear: this.get('selectedStart'),
      endYear: this.get('selectedEnd')
    };
  }
});
