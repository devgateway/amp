var _ = require('underscore');
var Backbone = require('backbone');
var BaseFilterView = require('../views/base-filter-view');
var YearsFilterModel = require('../models/years-filter-model');


module.exports = BaseFilterView.extend({

  className: BaseFilterView.className + ' filter-years',

  initialize: function(options) {
    BaseFilterView.prototype.initialize.apply(this);

    this.model = new YearsFilterModel(options.modelValues);
  },

  renderContent: function (){
    BaseFilterView.prototype.renderContent.apply(this);
    //TODO: slider like this: 
    // code: https://github.com/leongersen/noUiSlider
    // demo: http://refreshless.com/nouislider/
    console.log('TODO: render a two handle slider.');

  }

});
