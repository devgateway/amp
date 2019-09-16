define(['marionette', 'text!views/html/dynamicContentTemplate.html', 'text!views/html/settingsDialogTemplate.html', 'models/settings',
    'business/settings/settingsManager', 'business/filter/filterUtils', 'translationManager',
    'business/tabManager', 'jquery', 'jqueryui'], function (Marionette, dynamicContentTemplate, settingsDialogTemplate, Settings,
                                                            SettingsManager, FilterUtils, TranslationManager, TabManager, jQuery) {

    var reportId;
    var reportFilters;

    var DynamicContentView = Marionette.LayoutView.extend({
        template: _.template(dynamicContentTemplate),
        regions: {
            filters: '#dynamic-filters-region',
            legends: '#dynamic-legends-region',
            results: '#dynamic-results-region'
        },
        events: {
            'click #filters-button': "clickFiltersButton",
            'click #settings-button': "clickSettingsButton",
            'click #save-button': "clickSaveButton"
        },
        initialize: function (data) {
            reportId = data.id;
            reportFilters = data.filters;

            // NOTE: Moved here from settingsManager.js in order to have this
            // values ready for some checks in gridManager.js
            var settings = new Settings();
            app.TabsApp.settings = settings;
        },
        clickSaveButton: function () {
            TabManager.openSaveTabDialog();
        },
        clickFiltersButton: function () {
            // We need to reset the widget because is shared between all
            // tabs.
            app.TabsApp.filtersWidget.reset({
                silent: true
            });

            // Force the widget to show the first tab to avoid problems with include-location-children checkbox.
            jQuery('.filter-titles li a')[0].click();

            var containerName = '#filter-popup';
            var FilterDialogContainerView = Marionette.ItemView.extend({
                template: "<p></p>",
                render: function (model) {
                    // Close floating accordion if needed.
                    jQuery("#main-dynamic-content-region_" + reportId + " #filters-collapsible-area").accordion('option', 'active', false);

                    // Convert report filters to filterwidget filters.
                    var blob = undefined;
                    if (app.TabsApp.serializedFilters === null) {
                        blob = app.TabsApp.rawFilters;
                    } else {
                        blob = app.TabsApp.serializedFilters;
                    }
                    if (blob && blob.filters &&
                        (blob.filters.includeLocationChildren === undefined ||
                            blob.filters.includeLocationChildren === null)) {
                        blob.filters.includeLocationChildren = blob['include-location-children'];
                    }

                    app.TabsApp.filtersWidget.reset({
                        silent: true
                    });
                    app.TabsApp.filtersWidget.deserialize(blob, {
                        silent: true
                    });

                    var datesFilterView = app.TabsApp.filtersWidget.view.filterViewsInstances.others.viewList.filter(function (v) {
                        return v.model.get('id') === 'date';
                    })[0];
                    if (datesFilterView) {
                        datesFilterView._renderDatePickers();
                    }
                    jQuery(containerName).show();
                    jQuery(containerName).css('position', 'absolute');
                    jQuery(containerName).css('top', 10);
                    jQuery(containerName).css("min-width", jQuery(containerName + " .panel-heading").width() + 32);

                    app.TabsApp.filtersWidget.showFilters();

                }
            });
            var filterDialog = new FilterDialogContainerView();
            filterDialog.render();
        },
        clickSettingsButton: function () {
            SettingsManager.openDialog();
        },
        onShow: function (data) {
            // Create jQuery buttons.
            jQuery("#main-dynamic-content-region_" + reportId + " #filters-button").button({
                icons: {
                    primary: "ui-icon-search"
                }
            });
            jQuery("#main-dynamic-content-region_" + reportId + " #save-button").button({
                icons: {
                    primary: 'ui-icon-disk'
                }
            });
            jQuery("#main-dynamic-content-region_" + reportId + " #settings-button").button({
                icons: {
                    primary: 'ui-icon-gear'
                }
            });
        }
    });

    return DynamicContentView;

});