const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const test = require('node:test');
const vm = require('node:vm');

const viewer = path.resolve(__dirname, '../../../TEMPLATE/ampTemplate/saikuui_reports');
const queryUrl = '/TEMPLATE/ampTemplate/saikuui_reports/mockData/query.json';

function createViewer() {
    let request;
    const jquery = {
        ajax(options) {
            request = options;
            return {};
        },
        Deferred() {
            return {};
        },
        each(object, callback) {
            Object.keys(object || {}).forEach(key => callback(key, object[key]));
        }
    };
    const context = vm.createContext({
        console,
        jQuery: jquery,
        $: jquery,
        Settings: { REST_URL: '/rest/', QUERY_PROPERTIES: {}, ERROR_TOLERANCE: 3 },
        Saiku: { logger: { log() {} }, ui: { unblock() {} }, session: { logout() {} } },
        SaikuQueryTemplate: {},
        SaikuQueryHelper: function () {},
        Result: function () {},
        isIE: false
    });
    context.window = context;
    for (const file of [
        'js/backbone/underscore.js',
        'js/backbone/backbone.min.js',
        'js/saiku/models/Query.js',
        'js/saiku/adapters/SaikuServer.js'
    ]) {
        vm.runInContext(fs.readFileSync(path.join(viewer, file), 'utf8'), context, { filename: file });
    }
    return { context, request: () => request };
}

test('opening a report reads the query fixture and initializes the saved query', () => {
    const { context, request } = createViewer();
    const query = new context.Query({ report_id: 42, filters: { sector: 'private' } }, {});
    let initialized = false;
    query.save({}, { success() { initialized = true; } });

    assert.equal(request().url, queryUrl);
    assert.equal(request().type, 'GET');
    assert.equal(request().data, undefined);
    assert.equal(request().cache, true);

    const fixture = JSON.parse(fs.readFileSync(path.join(viewer, 'mockData/query.json'), 'utf8'));
    request().success(fixture, 'success', {});
    assert.equal(initialized, true);
    assert.equal(query.get('report_id'), 42);
    assert.equal(query.get('filters').sector, 'private');
    assert.equal(query.id, fixture.name);
});

test('query fixture failures still reach the caller', () => {
    const { context, request } = createViewer();
    const query = new context.Query({ filters: {} }, {});
    let failed = false;
    query.save({}, { error() { failed = true; } });
    request().error({}, 'error', 'Forbidden');
    assert.equal(failed, true);
});

test('real report execution remains a POST with its request body', () => {
    const { context, request } = createViewer();
    const body = '{"filters":{"sector":"health"}}';
    context.Backbone.sync('create', {
        url: () => '../../../rest/data/saikureport/42',
        attributes: {}
    }, { data: body, contentType: 'application/json', success() {} });

    assert.equal(request().type, 'POST');
    assert.equal(request().data, body);
    assert.equal(request().contentType, 'application/json');
});

test('other JSON requests keep their existing behavior', () => {
    const { context, request } = createViewer();
    const attributes = { value: 'example' };
    context.Backbone.sync('read', {
        url: () => '/TEMPLATE/ampTemplate/saikuui_reports/mockData/session.json',
        attributes
    }, { success() {} });

    assert.equal(request().type, 'POST');
    assert.equal(request().data, attributes);
});
