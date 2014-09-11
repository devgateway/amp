var fs = require('fs');
var _ = require('underscore');
var BaseFilterView = require('../views/base-filter-view');
var YearsFilterModel = require('../models/years-filter-model');

require('../../../../libs/local/slider/jquery.nouislider.min.js');

var Template = fs.readFileSync(__dirname + '/../templates/years-template.html', 'utf8');

module.exports = BaseFilterView.extend({

  className: BaseFilterView.prototype.className + ' filter-years',
  template: _.template(Template),

  initialize: function(options) {
    var self = this;
    BaseFilterView.prototype.initialize.apply(this, [options]);

    this.model = new YearsFilterModel(options.modelValues);
    this.model.url = options.url;

    this.model.fetch().then(function() {
      self.model.set('selectedStart', self.model.get('startYear'));
      self.model.set('selectedEnd', self.model.get('endYear'));
      self._updateTitle();
    });
    this.listenTo(this.model, 'change', this._updateTitle);
  },


  renderFilters: function() {
    var self = this;
    BaseFilterView.prototype.renderFilters.apply(this);
    this.$('.filter-options').append(this.template());

    // TODO: uses window.jQuery because that was the only way I had luck with browserify shim...
    // uses https://github.com/leongersen/noUiSlider
    this.slider = window.jQuery('.year-slider').noUiSlider({
      start: [self.model.get('selectedStart'), self.model.get('selectedEnd')],
      step: 1,
      connect: true,
      range: {min: self.model.get('startYear'), max:self.model.get('endYear')},
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
    this.slider.on('change', function() {
      self.model.set('selectedStart', parseInt(self.$('.start-year').text(), 10));
    });

    //ugly, too much data in the dom...but it's how the example goes.
    this.slider.on('change', function() {
      self.model.set('selectedEnd',  parseInt(self.$('.end-year').text(), 10));
    });
  },

  renderTitle: function() {
    BaseFilterView.prototype.renderTitle.apply(this);
    this._updateTitle();

    return this;
  },

  _updateTitle: function() {
    this.$('.filter-count').text(this.model.get('selectedStart') + ' - ' + this.model.get('selectedEnd'));
  }

});
