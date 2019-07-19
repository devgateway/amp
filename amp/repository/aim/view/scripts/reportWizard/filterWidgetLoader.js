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
    var advancedFilters = (document.URL.indexOf('queryEngine.do') > -1);
    var embedded = advancedFilters;
    widgetFilter = new ampFilter({
        el: container,
        draggable: true,
        caller: 'REPORTS',
        reportType: reportTypeCode,
        embedded: embedded
    });

    var events = _.extend({}, Backbone.Events);
    events.listenTo(widgetFilter, 'widgetLoaded', function () {
        widgetFilter.deserialize({filters: {includeLocationChildren: true}}, {silent: true});
    });
    // Register apply and cancel buttons.
    events.listenTo(widgetFilter, 'cancel', function () {
        $(container).hide();
    });
    events.listenTo(widgetFilter, 'close', function () {
        $(container).hide();
    });
    events.listenTo(widgetFilter, 'reset', function () {
        if (embedded) {
            $('#queryLabelsDiv').html('<div id="queryLabelsDiv"><digi:trn>No filters selected so far</digi:trn></div>');
        }
    });
    events.listenTo(widgetFilter, 'apply', function () {
        // Save just applied filters in case the user hits "reset" button.
        $('#queryLabelsDiv').html('<div><img class="loading-spinner" src="/TEMPLATE/ampTemplate/img_2/loading-icon.gif" /></div>');
        var serializedFilters = widgetFilter.serialize() || {};
        var url = '/aim/reportsFilterPicker.do?apply=true&cacheBuster=';
        if (advancedFilters) {
            url += new Date().getTime() + '&reportContextId=' + widgetFilter.auxId + '&doreset=true&queryEngine=true';
        } else {
            url += new Date().getTime() + '&reportContextId=' + widgetFilter.reportContextId +
                '&sourceIsReportWizard=true';
        }
        $.ajax({
            type: 'POST',
            url: url,
            data: "filtersWidget=" + JSON.stringify(serializedFilters),
            success: function (data) {
                if (!embedded) {
                    $('#listFiltersDiv').html(data);
                    $('#hasFilters').val(true);
                } else {
                    $('#queryLabelsDiv').html(data);
                    document.getElementById('queryLabelsDiv').scrollIntoView();
                }
            }
        });
        if (!embedded) {
            $(container).hide();
            $('#useFiltersCheckbox').attr('checked', 'checked');
        }
    });
});