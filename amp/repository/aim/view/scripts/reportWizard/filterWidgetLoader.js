var PLEDGE_ID = '5';
var PLEDGE_TYPE = 'P';
var DONOR_TYPE = 'D';

var widgetFilter = null;
$(document).ready(function () {
    // Load the filter after we rendered the DOM for perceived speed.
    var container = $('#filter-popup');
    var reportTypeCode = DONOR_TYPE;
    // TODO: replace the magic number with "P" in case of Pledges/D for Donors.
    if (new URL(window.location).searchParams.get('type') === PLEDGE_ID) {
        reportTypeCode = PLEDGE_TYPE;
    }
    var embedded = (document.URL.indexOf('queryEngine.do') > -1);
    widgetFilter = new ampFilter({
        el: container,
        draggable: true,
        caller: 'REPORTS',
        reportType: reportTypeCode,
        embedded: embedded
    });
    var events = _.extend({}, Backbone.Events);
    // Register apply and cancel buttons.
    events.listenTo(widgetFilter, 'cancel', function () {
        $(container).hide();
    });
    events.listenTo(widgetFilter, 'close', function () {
        $(container).hide();
    });
    events.listenTo(widgetFilter, 'apply', function () {
        // Save just applied filters in case the user hits "reset" button.
        var serializedFilters = widgetFilter.serialize() || {};
        $.ajax({
            type: 'POST',
            url: '/aim/reportsFilterPicker.do?applyWithNewWidget=true&cacheBuster=' +
                new Date().getTime() +
                '&reportContextId=' + widgetFilter.reportContextId +
                '&sourceIsReportWizard=true',
            data: "filtersWidget=" + JSON.stringify(serializedFilters),
            success: function (data) {
                $('#listFiltersDiv').html(data);
                $('#hasFilters').val(true);
            }
        });
        if (!embedded) {
            $(container).hide();
            $('#useFiltersCheckbox').attr('checked', 'checked');
        }
    });
});