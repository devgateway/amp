var fs = require('fs');
var _ = require('underscore');
var BaseFilterView = require('../views/base-filter-view');

require('../../../libs/local/slider/jquery.nouislider.min.js');
require('jquery-ui/datepicker');

var Template = fs.readFileSync(__dirname + '/../templates/years-template.html', 'utf8');

module.exports = BaseFilterView.extend({

  className: BaseFilterView.prototype.className + ' filter-years',
  template: _.template(Template),
  _loaded: null,

  initialize:function(options) {
    var self = this;
    BaseFilterView.prototype.initialize.apply(this, [options]);

    this.model = options.model;

    this._loaded = this.model.fetch().then(function() {
      // only set if not set by deserialize
      if(!self.model.get('selectedStart')){
        self.model.set('selectedStart', self.model.get('startYear'));
        self.model.set('selectedEnd', self.model.get('endYear')); 
      }
    });

    this.listenTo(this.model, 'change', this._updateTitle);
  },


  renderFilters:function() {
    var self = this;
    BaseFilterView.prototype.renderFilters.apply(this);

    this.$el.html(this.template(this.model.toJSON()));

    this._loaded.then(function() {
      //self._renderSlider();
      self._renderDatePickers();
      self._updateTitle();
    });

    return this;
  },

  _renderDatePickers: function() {
    var self = this;

    // TODO: format absed on admin setting....maybe get from year api..
    // TODO: abstract common properties from object inits below...
    var commonObj = {};

    this.$('#start-date').datepicker({
      defaultDate: this.model.get('selectedStart'),
      minDate: this.model.get('startYear'),
      maxDate: this.model.get('endYear'),
      dateFormat: 'dd/mm/yy',
      changeMonth: true,
      changeYear: true,
      numberOfMonths: 1,
      yearRange: 'c-60:c+60',
      onClose: function(selectedDate) {
        self.$('#end-date').datepicker('option', 'minDate', selectedDate);
        self.model.set('selectedStart', selectedDate);
        // self._updateTitle();
      }
    });

    this.$('#end-date').datepicker({
      defaultDate: this.model.get('selectedEnd'),
      minDate: this.model.get('startYear'),
      maxDate: this.model.get('endYear'),
      dateFormat: 'dd/mm/yy',
      changeMonth: true,
      changeYear: true,
      numberOfMonths: 1,
      yearRange: 'c-60:c+60',
      onClose: function(selectedDate) {
        self.$('#start-date').datepicker('option', 'maxDate', selectedDate);
        self.model.set('selectedEnd', selectedDate);
        // self._updateTitle();
      }
    });

    this.$('#start-date').val(this.model.get('selectedStart'));
    this.$('#end-date').val(this.model.get('selectedEnd'));
  },

  renderTitle:function() {
    BaseFilterView.prototype.renderTitle.apply(this);
    this._updateTitle();

    return this;
  },

  //TODO: do more in template.
  _updateTitle:function() {
    this.$titleEl.find('.filter-count').text(this.model.get('selectedStart') +
        ' - ' +
      this.model.get('selectedEnd'));

    this.$('.start-year').text(this.model.get('selectedStart'));
    this.$('.end-year').text(this.model.get('selectedEnd'));
  },



  _renderSlider: function() {
    var self = this;

    // uses window.jQuery because that was the only way I had luck with browserify shim...
    // uses https://github.com/leongersen/noUiSlider
    this.slider = window.jQuery(this.$('.year-slider')).noUiSlider({
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
  }

});
