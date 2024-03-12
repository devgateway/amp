var $ = require('jquery');
var _ = require('underscore');
var BaseFilterModel = require('../models/base-filter-model');
var Constants = require('../utils/constants');

module.exports = BaseFilterModel.extend({

    defaults: {
        selectedYear: undefined,
        defaultYear: undefined,
        modelType: Constants.YEAR_SINGLE_VALUE
    },

    initialize: function (options) {
        BaseFilterModel.prototype.initialize.apply(this, [options]);
        this.url = options.endpoint;
        this.set('_loaded', $.Deferred());
    },

    parse: function (data) {
        if (data && data.items && data.items.values) {
            var foundItem = _.find(data.items.values, function (item) {
                return item.name === Constants.DEFAULT_COMPUTED_YEAR_NAME;
            });

            if (foundItem) {
                data.defaultYear = foundItem.value;
            }
        }

        this.get('_loaded').resolve();
        return data;
    },

    serialize: function (options) {
        if (this.get('selectedYear')) {
            var key = this.get('id');
            var obj = {};
            if (options.wholeModel === true) {
                obj[key] = {
                    year: this.get('selectedYear'),
                    modelType: this.get('modelType'),
                    displayName: this.get('name')
                };
            } else {
                obj[key] = this.get('selectedYear');
            }
            return obj;
        } else {
            return null;
        }
    },

    deserialize: function (obj) {
        var key = this.get('id');
        if (obj && obj[key]) {
            this.set('selectedYear', obj[key]);
        } else {
            this.reset();
        }
    },

    reset: function () {
        this.set('selectedYear', '');
        this.set('displayName', '')
    },

});
