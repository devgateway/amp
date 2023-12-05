var Backbone = require('backbone');
var _ = require('underscore');

var EnabledChartModel = Backbone.Model.extend({});

var EnabledChartsCollection = Backbone.Collection.extend({
    model: EnabledChartModel,
    url: '/rest/common/fm/flat',
    fetchData: function () {
        var params = {
            'enabled-modules': false,
            'reporting-fields': false,
            'full-enabled-paths': false,
            'detail-modules': ['DASHBOARDS']
        };
        this.fetch({
            type: 'POST',
            async: false,
            processData: false,
            mimeType: 'application/json',
            traditional: true,
            headers: {
                'Content-Type': 'application/json',
                'Cache-Control': 'no-cache'
            },
            data: JSON.stringify(params), // This is necessary due to
            // incompatibilities with Jersey
            // when receiving the params.
            error: function (collection, response) {
                console.error('error loading charts.');
            },
            success: function (collection, response) {
            }
        });
    }
});

module.exports = EnabledChartsCollection;