var _ = require('underscore');

var extractDates = function (settings, filtersOut, minName, maxName) {
    filtersOut = filtersOut || {};
    if (_.isUndefined(filtersOut.date) || _.isEmpty(filtersOut.date)) {
        filtersOut.date = filtersOut.date || {
            start: '',
            end: ''
        };

        var defaultMinDate = settings.get(minName);
        if (defaultMinDate !== undefined && defaultMinDate !== '') {
            filtersOut.date.start = defaultMinDate;
        }
        var defaultMaxDate = settings.get(maxName);
        if (defaultMaxDate !== undefined && defaultMaxDate !== '') {
            filtersOut.date.end = defaultMaxDate;
        }
    }
};

module.exports = {
    extractDates: extractDates
};
