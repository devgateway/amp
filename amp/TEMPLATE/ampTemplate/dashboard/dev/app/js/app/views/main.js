var fs = require('fs');
var _ = require('underscore');
var BackboneDash = require('../backbone-dash');

var StateLoadError = require('amp-state/index').StateLoadError;

var Controls = require('./controls');
var ChartsView = require('./charts');
var Charts = require('../models/charts-collection');
var boilerplate = require('amp-boilerplate');
var HeatMapChart = require('../models/chart-heatmaps');
var TopsChart = require('../models/chart-tops');
var PredictabilityChart = require('../models/chart-aid-predictability');
var FundingTypeChart = require('../models/chart-funding-type');

var template = _.template(fs.readFileSync(
    __dirname + '/../templates/main.html', 'UTF-8'));
var modalTemplate = _.template(fs.readFileSync(
    __dirname + '/../templates/modal.html', 'UTF-8'));

var EnabledChartsCollection = require('../models/enabled-charts-collection');
var HeatmapsConfigCollection = require('../models/heatmaps-config-collection');

var DO = '/Dashboards[true]/Top Donors[true]';
var DG = '/Dashboards[true]/Top Donor Group[true]';
var RE = '/Dashboards[true]/Top Regions[true]';
var PS = '/Dashboards[true]/Top Sectors[true]';
var AP = '/Dashboards[true]/Aid Predictability[true]';
var FTYPE = '/Dashboards[true]/Funding Type[true]';
var RO = '/Dashboards[true]/Responsible Organizations[true]';
var BA = '/Dashboards[true]/Beneficiary Agencies[true]';
var IA = '/Dashboards[true]/Implementing Agencies[true]';
var EA = '/Dashboards[true]/Executing Agencies[true]';
var NDD = '/Dashboards[true]/Peace-building and State-building Goals[true]';
var SEC = '/Dashboards[true]/Sector Fragmentation[true]';
var LOC = '/Dashboards[true]/Location Fragmentation[true]';
var PRG = '/Dashboards[true]/Program Fragmentation[true]';

module.exports = BackboneDash.View.extend({

    initialize: function (options) {
        this.app = options.app;

        // but we already do other fetches on init so...
        this.app.user.fetch();
        this.controls = new Controls({app: this.app});

        // AMP-19545: We instantiate the collection of enabled charts (from FM) and use it to enable or not each chart.
        var enabledChartsFM = new EnabledChartsCollection();
        enabledChartsFM.fetchData();

        // Get config of all heatmaps from backend.
        var heatmapsConfigs = new HeatmapsConfigCollection();
        heatmapsConfigs.fetchData();

        if (enabledChartsFM.models[0].get('error') !== undefined) {
            // The same endpoint will send an error if 'DASHBOARDS' is not active in the Feature Manager.
            window.location = '/';
        }

        var enabledCharts = enabledChartsFM.models[0].get('fm-settings')['DASHBOARDS'];

        var col = [];
        if (_.find(enabledCharts, function (item) {
            return item.indexOf(DO) > -1;
        })) {
            col.push(new TopsChart(
                {name: 'Top Donor Agencies', big: false, view: 'bar'},
                {app: this.app, url: '/rest/dashboard/tops/do'}));
        }
        if (_.find(enabledCharts, function (item) {
            return item === DG;
        })) {
            col.push(new TopsChart(
                {name: 'Top Donor Groups', big: false, view: 'bar'},
                {app: this.app, url: '/rest/dashboard/tops/dg'}));
        }
        if (_.find(enabledCharts, function (item) {
            return item === RE;
        })) {
            col.push(new TopsChart(
                {name: 'Top Regions', big: false, view: 'bar'},
                {app: this.app, url: '/rest/dashboard/tops/re'}));
        }
        if (_.find(enabledCharts, function (item) {
            return item === PS;
        })) {
            col.push(new TopsChart(
                {name: 'Top Sectors', big: false, view: 'bar'},
                {app: this.app, url: '/rest/dashboard/tops/ps'}));
        }
        if (_.find(enabledCharts, function (item) {
            return item === AP;
        })) {
            col.push(new PredictabilityChart(
                {name: 'Aid Predictability'},
                {app: this.app, url: '/rest/dashboard/aid-predictability'}));
        }
        if (_.find(enabledCharts, function (item) {
            return item === FTYPE;
        })) {
            col.push(new FundingTypeChart(
                {name: 'Funding Type'},
                {app: this.app, url: '/rest/dashboard/ftype'}));
        }
        if (_.find(enabledCharts, function (item) {
            return item === RO;
        })) {
            col.push(new TopsChart(
                {name: 'Responsible Organizations', big: false, view: 'bar'},
                {app: this.app, url: '/rest/dashboard/tops/ro'}));
        }
        if (_.find(enabledCharts, function (item) {
            return item === BA;
        })) {
            col.push(new TopsChart(
                {name: 'Beneficiary Agencies', big: false, view: 'bar'},
                {app: this.app, url: '/rest/dashboard/tops/ba'}));
        }
        if (_.find(enabledCharts, function (item) {
            return item === EA;
        })) {
            col.push(new TopsChart(
                {name: 'Executing Agencies', big: false, view: 'bar'},
                {app: this.app, url: '/rest/dashboard/tops/ea'}));
        }
        if (_.find(enabledCharts, function (item) {
            return item === IA;
        })) {
            col.push(new TopsChart(
                {name: 'Implementing Agencies', big: false, view: 'bar'},
                {app: this.app, url: '/rest/dashboard/tops/ia'}));
        }
        if (_.find(enabledCharts, function (item) {
            return item === NDD;
        })) {
            col.push(new TopsChart(
                {name: 'Peace-building and State-building Goals', big: true, showCategoriesInfo: true, view: 'pie'},
                {app: this.app, url: '/rest/dashboard/tops/ndd'}));
        }
        if (_.find(enabledCharts, function (item) {
            return item === SEC;
        })) {
            col.push(new HeatMapChart(
                {
                    name: 'HeatMap by Sector and Donor Group',
                    title: 'Sector Fragmentation',
                    big: true,
                    view: 'heatmap',
                    heatmap_config: heatmapsConfigs,
                    heatmap_type: 'sector'
                },
                {app: this.app, url: '/rest/dashboard/heat-map/sec'}));
        }
        if (_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function (item) {
            return item === LOC;
        })) {
            col.push(new HeatMapChart(
                {
                    name: 'HeatMap by Location and Donor Group',
                    title: 'Location Fragmentation',
                    big: true,
                    view: 'heatmap',
                    heatmap_config: heatmapsConfigs,
                    heatmap_type: 'location'
                },
                {app: this.app, url: '/rest/dashboard/heat-map/loc'}));
        }
        if (_.find(enabledChartsFM.models[0].get('DASHBOARDS'), function (item) {
            return item === PRG;
        })) {
            col.push(new HeatMapChart(
                {
                    name: 'HeatMap by Program and Donor Group',
                    title: 'Program Fragmentation',
                    big: true,
                    view: 'heatmap',
                    heatmap_config: heatmapsConfigs,
                    heatmap_type: 'program'
                },
                {app: this.app, url: '/rest/dashboard/heat-map/prg'}));
        }

        var chartsCollection = new Charts(col, {app: this.app});
        this.charts = new ChartsView({
            app: this.app,
            collection: chartsCollection
        });

        //auto-renders the layout
        this.headerWidget = new boilerplate.layout(
            {
                caller: 'DASHBOARD'
            });
    },

    render: function () {
        this.$el.html(template());
        this.$('.container').html([
            this.controls.render().el,
            this.charts.render().el
        ]);
        return this;
    },

    modal: function (title, options) {
        options = _({
            title: title,
            id: _.uniqueId('modal')
        }).extend(options);
        this.$el.parent().append(modalTemplate({m: options}));
        var thisModal = this.$el.parent().find('#' + options.id);
        if (options.bodyEl) {
            thisModal.find('.modal-body').html(options.bodyEl);
        }
        thisModal.modal();
        return thisModal[0];  // the actual DOM element
    }

});
