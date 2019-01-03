var widgetFilter = null;
$(document).ready(function () {
    // Load the filter after we rendered the DOM for perceived speed.
    var container = $('#filter-popup');
    widgetFilter = new ampFilter({el: container, draggable: true, caller: 'REPORTS'});
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
            data: serializedFilters,
            success: function (data) {
                $('#listFiltersDiv').html(data);
            }
        });
        $(container).hide();
        $('#useFiltersCheckbox').attr('checked', 'checked');
    });
});