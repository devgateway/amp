var fs = require('fs');
var _ = require('underscore');
var Backbone = require('backbone');
var BaseFilterView = require('../views/base-filter-view');
var YearsFilterModel = require('../models/years-filter-model');

require('../../../../libs/local/slider/jquery.nouislider.min.js');

var Template = fs.readFileSync(__dirname + '/../templates/years-template.html', 'utf8');

module.exports = BaseFilterView.extend({

  className: BaseFilterView.prototype.className + ' filter-years',
  template: _.template(Template),
  allowedRange: {min: 1980,max: 2015},

  initialize: function(options) {
    BaseFilterView.prototype.initialize.apply(this);

    this.model = new YearsFilterModel(options);

  },


  renderFilters: function () {
    var self = this;
    BaseFilterView.prototype.renderFilters.apply(this);
    this.$('.filter-options').append(this.template());

    // TODO: uses window.jQuery because that was the only way I had luck with browserify shim... 
    // uses https://github.com/leongersen/noUiSlider
    window.jQuery('.year-slider').noUiSlider({
      start: [1990, 2010],
      step: 1,
      connect: true,
      range: self.allowedRange,
      serialization: {
        lower: [
          window.jQuery.Link({
            target: window.jQuery('.start-year')
          })
        ],
        upper: [
          window.jQuery.Link({
            target: window.jQuery('.end-year')
          })
        ],
        format: {
          decimals: 0
        }
      }
    });
  }

});
