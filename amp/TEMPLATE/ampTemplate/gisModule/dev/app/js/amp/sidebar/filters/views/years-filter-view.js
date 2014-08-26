var fs = require('fs');
var _ = require('underscore');
var $ = require('jquery');
var BaseFilterView = require('../views/base-filter-view');
var YearsFilterModel = require('../models/years-filter-model');

require('../../../../libs/local/slider/jquery.nouislider.min.js');

var Template = fs.readFileSync(__dirname + '/../templates/years-template.html', 'utf8');

module.exports = BaseFilterView.extend({

  className: BaseFilterView.prototype.className + ' filter-years',
  template: _.template(Template),

  initialize: function(options) {
    BaseFilterView.prototype.initialize.apply(this);

    this.model = new YearsFilterModel(options);
    this.listenTo(this.model, 'change', this._updateTitle); 
  },


  renderFilters: function () {
    var self = this;
    BaseFilterView.prototype.renderFilters.apply(this);
    this.$('.filter-options').append(this.template());

    // TODO: uses window.jQuery because that was the only way I had luck with browserify shim... 
    // uses https://github.com/leongersen/noUiSlider
    this.slider = window.jQuery('.year-slider').noUiSlider({
      start: [self.model.get('start'), self.model.get('end')],
      step: 1,
      connect: true,
      range: {min: self.model.get('min'), max:self.model.get('max')},
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

    //ugly, too much data in the dom...but it's how the example goes.
    this.slider.on('change', function(){
      self.model.set('start', parseInt(self.$('.start-year').text(),10));
    });

    //ugly, too much data in the dom...but it's how the example goes.
    this.slider.on('change', function(){
      self.model.set('end',  parseInt(self.$('.end-year').text(),10));
    });
  },

  renderTitle: function () {
    BaseFilterView.prototype.renderTitle.apply(this);

    this.$('.filter-count').text(this.model.get('start') + ' - ' + this.model.get('end'));

    return this;
  },

  _updateTitle: function(){
    this.$('.filter-count').text(this.model.get('start') + ' - ' + this.model.get('end'));
  }

});
