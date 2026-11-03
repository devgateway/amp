const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const test = require('node:test');
const vm = require('node:vm');
const { JSDOM } = require('jsdom');

const viewer = path.resolve(__dirname, '../../../TEMPLATE/ampTemplate/saikuui_reports');
const widgetBundle = path.resolve(__dirname, '../../../TEMPLATE/ampTemplate/node_modules/amp-filter/dist/amp-filter');

test('selecting disaggregation options reaches the report request and survives pagination', async () => {
    const dom = new JSDOM('<!doctype html><html><body><div id="filter-popup"></div></body></html>', {
        url: 'http://localhost'
    });
    global.window = dom.window;
    global.document = dom.window.document;
    global.navigator = dom.window.navigator;
    global.Node = dom.window.Node;
    global.$ = require('jquery');
    global.jQuery = global.$;
    window.$ = global.$;
    window.jQuery = global.jQuery;

    const Widget = require(widgetBundle);
    const bundledJQuery = window.jQuery;
    const metadata = [
        {
            id: 'date', name: 'Date', ui: true, fieldType: 'DATE_RANGE', dataType: 'DATE',
            endpoint: '/rest/filters/dates', componentType: ['ALL'], tab: 'Other'
        },
        {
            id: 'indicator-disaggregation', name: 'Disaggregation', ui: true, fieldType: 'TREE',
            dataType: 'TEXT', endpoint: '/rest/filters/indicator-disaggregation',
            componentType: ['REPORTS'], tab: 'Activity'
        }
    ];
    bundledJQuery.get = (url, callback) => {
        assert.equal(url, '/rest/filters/indicator-disaggregation');
        const deferred = bundledJQuery.Deferred();
        setTimeout(() => {
            callback({
                listDefinitions: [{
                    name: 'Disaggregation', displayName: 'Disaggregation', items: 'values',
                    filterIds: ['indicator-disaggregation-criterion', 'indicator-disaggregation'],
                    serializeSelectedLeaves: true
                }],
                items: {
                    values: [
                        { id: 1, name: 'Sex', children: [{ id: 11, name: 'Female' }, { id: 12, name: 'Male' }] },
                        { id: 2, name: 'Age', children: [{ id: 21, name: 'Adult' }, { id: 22, name: 'Youth' }] }
                    ]
                }
            });
            deferred.resolve();
        }, 0);
        return deferred.promise();
    };
    const widget = new Widget({
        el: document.getElementById('filter-popup'),
        draggable: false,
        caller: 'REPORTS',
        reportType: 'I',
        translator: { translateDOM() {}, translateSync(key, fallback) { return fallback; } },
        sync(method, model, options) {
            const deferred = bundledJQuery.Deferred();
            setTimeout(() => {
                const data = typeof model.url === 'string' && model.url.indexOf('/rest/filters') === 0
                    ? metadata : { 'default-date-format': 'dd/MM/yyyy', language: 'en' };
                options.success(data);
                deferred.resolve(data);
            }, 0);
            return deferred.promise();
        }
    });
    await new Promise((resolve, reject) => widget.loaded.done(resolve).fail(reject));
    await new Promise(resolve => setTimeout(resolve, 0));
    assert.equal(widget.view.allFilters.url, '/rest/filters?report-type=I');

    const requests = [];
    const context = {
        console,
        $: global.$,
        jQuery: global.jQuery,
        Settings: { PAGINATION: true, QUERY_PROPERTIES: {} },
        Saiku: {
            originalReportMetadata: { reportMetadata: { reportSpec: { reportType: 7 } } },
            logger: { log() {} },
            ui: { unblock() {}, block() {} },
            i18n: { locale: 'en', translate() {} },
            events: {
                bind() {},
                listenTo(source, event, callback) { source.on(event, callback); }
            }
        },
        SaikuQueryTemplate: {},
        SaikuQueryHelper: function () { this.model = () => ({ queryModel: {} }); },
        Result: function () {
            this.save = (attributes, options) => requests.push(JSON.parse(options.data));
        },
        CommonFilterUtils: { calculateMD5FromParameters: () => 'test' },
        ampFilter: function (options) {
            assert.equal(options.reportType, 'I');
            return widget;
        }
    };
    context.window = context;
    vm.createContext(context);
    for (const file of [
        'js/backbone/underscore.js',
        'js/backbone/backbone.min.js',
        'js/saiku/models/Query.js',
        'js/saiku/plugins/AMPFilters/plugin.js'
    ]) {
        vm.runInContext(fs.readFileSync(path.join(viewer, file), 'utf8'), context, { filename: file });
    }

    const workspace = {
        el: document.createElement('div'),
        toolbar: {},
        unblock() {},
        trigger() {},
        adjust() {},
        table: { clearOut() {} }
    };
    workspace.query = new context.Query({ report_id: 278, filters: {} }, { workspace });
    const plugin = {
        el: document.createElement('div'),
        add_button() { this.filters_button = { removeClass() {}, toggleClass() {} }; },
        parseSavedFilters() {},
        render() {},
        show() {},
        add_amp_filter() {}
    };
    context.AMPFilters.prototype.initialize.call(plugin, { workspace });
    widget.view.allFilters.get('date').set({ selectedStart: '2009-01-01', selectedEnd: '2032-07-12' });

    function select(name) {
        const node = bundledJQuery('#filter-popup .tree-container .node').filter(function () {
            return bundledJQuery(this).children('.toggle-nav').text().trim().startsWith(name);
        });
        assert.equal(node.length, 1, `Missing ${name} option`);
        node.children('.selectable').trigger('click');
    }
    select('Female');
    select('Adult');
    bundledJQuery('#filter-popup .apply').trigger('click');
    assert.equal(requests.length, 1);
    assert.deepEqual(requests[0].queryModel.filters, {
        date: { start: '2009-01-01', end: '2032-07-12' },
        'indicator-disaggregation': [21, 11]
    });
    assert.equal(requests[0].queryModel.filtersApplied, true);

    workspace.query.set('max_page_no', 2);
    workspace.query.next_page();
    assert.equal(requests[1].queryModel.page, 2);
    assert.deepEqual(requests[1].queryModel.filters['indicator-disaggregation'], [21, 11]);

    select('Female');
    select('Adult');
    bundledJQuery('#filter-popup .apply').trigger('click');
    assert.equal(requests.length, 3);
    assert.equal(requests[2].queryModel.filters['indicator-disaggregation'], undefined);
    assert.deepEqual(requests[2].queryModel.filters.date, { start: '2009-01-01', end: '2032-07-12' });
});
