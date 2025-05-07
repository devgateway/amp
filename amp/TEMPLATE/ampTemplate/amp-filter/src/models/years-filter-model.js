var $ = require('jquery');
var _ = require('underscore');
var BaseFilterModel = require('../models/base-filter-model');
var Constants = require('../utils/constants');

module.exports = BaseFilterModel.extend({


    defaults: {
        selectedStart: null,
        selectedEnd: null,
        startYear: '',
        endYear: '',
        modelType: Constants.DATE_RANGE_VALUES
    },

    sync: function () {
        /**
         * hackish: the data coming off the /dates endpoint should be ignored at all moments, because
         * 1. for tabs/saiku, it should always be empty
         * 2. for gis/dashboards, it is coming off the /setting endpoint
         *
         * All the other years-filter-model should by spec be always empty at this point. In case this would cease to be true in the future,
         * just filter by "name"
         */

        return $.when(true);
    },

    initialize: function (options) {
        BaseFilterModel.prototype.initialize.apply(this, [options]);
        this.url = options.endpoint;
        this.set('_loaded', $.Deferred());
    },

    parse: function (data) {
        if (!data.startYear || data.startYear === -1) {
            data.startYear = this.defaults.startYear;
        } else {
            data.startYear = Constants.START_DATE_TEMPLATE.replace(Constants.YEAR_PLACEHOLDER, data.startYear);
        }

        if (!data.endYear || data.endYear === -1) {
            data.endYear = this.defaults.endYear;
        } else {
            data.endYear = Constants.END_DATE_TEMPLATE.replace(Constants.YEAR_PLACEHOLDER, data.endYear);
        }

        if (!data.selectedStart) {
            data.selectedStart = data.startYear;
            data.selectedEnd = data.endYear;
        }
        this.get('_loaded').resolve();
        return data;
    },

    serialize: function (options) {
        if (this.get('selectedStart') || this.get('selectedEnd')) {
            var obj = {};
            var key = this.get('id');
            obj[key] = {start: this.get('selectedStart'), end: this.get('selectedEnd')};

            if (options.wholeModel === true) {
                obj[key].modelType = this.get('modelType');
            }

            return obj;
        } else {
            return null;
        }
    },

    /**
     * postprocess model after having fetched data from the server
     */
    postprocess: function () {
        // only set if not set by deserialize
        if (!this.get('selectedStart')) {
            this.set('selectedStart', this.get('startYear'));
        }

        if (!this.get('selectedEnd')) {
            this.set('selectedEnd', this.get('endYear'));
        }
    },

    deserialize: function (obj) {
        var key = this.get('id');
        if (obj && obj[key]) {
            this.set('selectedStart', this._dateConvert(obj[key].start));
            this.set('selectedEnd', this._dateConvert(obj[key].end));
            this.postprocess();
        } else {
            this.set('selectedStart', this.get('startYear'));
            this.set('selectedEnd', this.get('endYear'));
        }
    },

    reset: function () {
        this.set('selectedStart', this.get('startYear'));
        this.set('selectedEnd', this.get('endYear'));
    },

    _dateConvert: function (input) {
        var output = null;
        if (input) {
            if (input.indexOf('/') > -1) {
                input = input.split('/');
                output = input[2] + '-' + input[1] + '-' + input[0];
            } else {
                output = input;
            }
        }
        return output;
    }

});
