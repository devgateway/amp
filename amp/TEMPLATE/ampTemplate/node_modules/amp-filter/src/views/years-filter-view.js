var fs = require('fs');
var _ = require('underscore');
var BaseFilterView = require('../views/base-filter-view');
require('jquery-ui/datepicker');

var Template = fs.readFileSync(__dirname + '/../templates/years-template.html', 'utf8');
var START_DATE_FIELD = 'selectedStart';
var END_DATE_FIELD = 'selectedEnd';
var CUSTOM_REGION_OPTIONS = {
    fr: {
        dayNamesMin: ["Di", "Lu", "Ma", "Me", "Je", "Ve", "Sa"]
    },
    es: {
        dayNamesMin: ["Do", "Lu", "Ma", "Mi", "Ju", "Vi", "Sá"]
    }
};

module.exports = BaseFilterView.extend({
    className: BaseFilterView.prototype.className + ' filter-years',
    template: _.template(Template),
    _loaded: null,
    initialize: function (options) {
        var self = this;
        this.filterView = options.filterView;
        BaseFilterView.prototype.initialize.apply(this, [options]);
        this.model = options.model;
        this.translator = options.translator;
        this.translate = options.translate;
        this._loaded = this.model.fetch().then(function () {
        });
        this.listenTo(this.model, 'change', this._updateUI);
    },
    renderFilters: function () {
        var self = this;
        BaseFilterView.prototype.renderFilters.apply(this);
        this.$el.html(this.template(this.model.toJSON()));
        this._loaded.then(function () {
            self._renderDatePickers();
            self._updateUI();
            self.translate(self);
            self.$('.clear-date').on('click', self._clearDate.bind(self));
        });

        return this;
    },

    _renderDatePickers: function () {
        var self = this;
        this._createDatePicker('start-date', 'selectedStart', true);
        this._createDatePicker('end-date', 'selectedEnd', false);
    },

    _createDatePicker: function (elementName, modelField, isStartDate) {
        var self = this;
        var options = {
            defaultDate: this.model.get(modelField),
            dateFormat: this.filterView.getDateFormat(),
            changeMonth: true,
            changeYear: true,
            numberOfMonths: 1,
            showOtherMonths: true,
            selectOtherMonths: true,
            yearRange: 'c-60:c+60',
            isRTL: self.getDirection(),
            onSelect: function (selectedDate) {
                if (isStartDate) {
                    self.$('#end-date').datepicker('option', 'minDate', selectedDate);
                } else {
                    self.$('#start-date').datepicker('option', 'maxDate', selectedDate);
                }


                self.model.set(modelField, $.datepicker.formatDate(self.filterView.PARAMS_DATE_FORMAT, $.datepicker.parseDate(self.filterView.getDateFormat(), selectedDate)));
            },
            beforeShowDay: function (date) {
                var formatted = $.datepicker.formatDate(self.filterView.PARAMS_DATE_FORMAT, date);
                if (self.model.get(modelField) && formatted === self.model.get(modelField)) {
                    return [true, "selected-date", ""];
                }

                return [true, "", ""];
            }
        };

        var lang = this.filterView.settings.get('language');
        lang = (lang === 'en' || lang == null) ? '' : lang;
        var region = $.datepicker.regional[lang];
        if (region && CUSTOM_REGION_OPTIONS[lang]) {
            region.dayNamesMin = CUSTOM_REGION_OPTIONS[lang].dayNamesMin;
        }

        return this.$('#' + elementName).datepicker($.extend(options, region)).datepicker('option', 'dateFormat', this.filterView.getDateFormat());
    },

    renderTitle: function () {
        BaseFilterView.prototype.renderTitle.apply(this);
        this._updateUI();

        return this;
    },

    getDirection: function () {
        var isRtl = false;
        if (this.filterView && this.filterView.settings) {
            isRtl = this.filterView.settings.get("rtl-direction");
        }
        return isRtl;
    },

    _updateUI: function () {
        var selectedStart = this.model.get('selectedStart') ? $.datepicker.formatDate(this.filterView.getDateFormat(), ($.datepicker.parseDate(this.filterView.PARAMS_DATE_FORMAT, this.model.get('selectedStart')))) : "";
        var selectedEnd = this.model.get('selectedEnd') ? $.datepicker.formatDate(this.filterView.getDateFormat(), ($.datepicker.parseDate(this.filterView.PARAMS_DATE_FORMAT, this.model.get('selectedEnd')))) : "";
        $('#datePicker').datepicker({dateFormat: this.filterView.getDateFormat()});
        this.$('#start-date').datepicker("setDate", selectedStart);
        this.$('#end-date').datepicker("setDate", selectedEnd);
        this._updateTabTitle(selectedStart, selectedEnd);
        this._updateSelectedDatesSection(selectedStart, selectedEnd);
    },

    _updateSelectedDatesSection: function (selectedStart, selectedEnd) {
        if (selectedStart) {
            this.$el.find('.selected-start-date').html(selectedStart);
            this.$el.find('.clear-start').removeClass('hide');
        } else {
            this.$el.find('.selected-start-date').html(this.translator.translateSync('amp.gis:not-set', 'NOT SET'));
            this.$el.find('.clear-start').addClass('hide');
        }

        if (selectedEnd) {
            this.$el.find('.selected-end-date').html(selectedEnd);
            this.$el.find('.clear-end').removeClass('hide');
        } else {
            this.$el.find('.selected-end-date').html(this.translator.translateSync('amp.gis:not-set', 'NOT SET'));
            this.$el.find('.clear-end').addClass('hide');
        }
    },

    _updateTabTitle: function (selectedStart, selectedEnd) {
        //update title
        var dates = "";
        if (selectedStart.length > 0 && selectedEnd.length > 0) {
            dates = selectedStart + ' - ' + selectedEnd;
        } else if (selectedStart.length > 0 && selectedEnd.length == 0) {
            dates = '<div class="filter-count-div">' + this.translator.translateSync('amp.gis:date-from', 'From') + '</div> ' + selectedStart;
        } else if (selectedStart.length == 0 && selectedEnd.length > 0) {
            dates = '<div class="filter-count-div">' + this.translator.translateSync('amp.gis:date-until', 'Until') + '</div> ' + selectedEnd;
        }

        this.$titleEl.find('.filter-count').html(dates);
    },

    _clearDate: function (event) {
        var field = $(event.target).attr("data-field");
        this.model.set(field, null);

        if (START_DATE_FIELD === field) {
            this.$('#end-date').datepicker('option', 'minDate', '');
        }

        if (END_DATE_FIELD === field) {
            this.$('#start-date').datepicker('option', 'maxDate', '');
        }
    }
});
