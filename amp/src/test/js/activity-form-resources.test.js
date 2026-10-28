const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const test = require('node:test');
const vm = require('node:vm');

const sourceRoot = path.resolve(__dirname, '../../main');
const template = fs.readFileSync(path.join(sourceRoot,
    'java/org/dgfoundation/amp/onepager/behaviors/documentReady.js'), 'utf8');

function createPage(elements = {}) {
    const state = { elements: { ...elements }, loaders: [], selected: [], appended: [], css: {} };
    const document = { getElementById: id => state.elements[id] ? { id } : null };
    const window = {};

    function $(selector) {
        const api = {
            ready(callback) { state.ready = callback; return api; },
            resize() { return api; },
            scroll() { return api; },
            each(callback) {
                if (typeof selector === 'string' && selector.startsWith('div[data-is_')) {
                    callback.call({ id: 'tab-content' }, 0);
                }
                return api;
            },
            appendTo(target) { state.appended.push(target); return api; },
            replaceWith() { return api; },
            attr() { return api; },
            html() { return ''; },
            css(property, value) {
                if (typeof value !== 'function') state.css[`${selector}:${property}`] = value;
                return api;
            },
            find() { return api; },
            parent() { return api; },
            removeClass() { return api; },
            addClass() { return api; },
            first() { return api; },
            last() { return api; },
            show() { return api; },
            off() { return api; },
            on() { return api; },
            offset() { return { top: 150, left: 100 }; },
            height() { return 100; },
            width() { return 1000; },
            scrollTop() { return 0; },
            scrollLeft() { return 0; }
        };
        return api;
    }

    const context = vm.createContext({
        document, window, $, navigator: {},
        Opentip: { findElements() {} },
        Wicket: { Ajax: { Call: function () {} } },
        YAHOO: {
            util: {
                YUILoader: function (options) {
                    state.loaders.push(options);
                    this.insert = () => {};
                }
            },
            widget: {
                TabView: function (id) {
                    this.get = () => state.elements[id].tabs;
                    this.selectTab = index => state.selected.push([id, index]);
                }
            }
        }
    });
    const script = template.replace(/\$\{onepagerMode\}/g, 'false')
        .replace(/\$\{isTabView\}/g, 'true')
        .replace(/\$\{isMeTabView\}/g, 'true')
        .replace(/\$\{isRtl\}/g, 'false');
    vm.runInContext(script, context);
    return { context, state };
}

test('the menu initializes when optional tab sections are absent', () => {
    const { state } = createPage();
    state.ready();
    assert.equal(state.css['#rightMenu:display'], 'block');
    assert.equal(state.loaders.length, 0);
    assert.deepEqual(state.appended, []);
});

for (const [method, id, container] of [
    ['switchTabs', 'fundingTabs', '#theContent'],
    ['indicatorTabs', 'indicatorTabs', '#theLocationIndicatorContent']
]) {
    test(`${method} initializes existing tabs and preserves selection`, () => {
        const { context, state } = createPage({ [id]: { tabs: [1, 2, 3] } });
        context[method](-1);
        assert.deepEqual(state.appended, [container]);
        state.loaders[0].onSuccess();
        assert.deepEqual(state.selected, [[id, 2]]);

        context[method](1);
        state.loaders[1].onSuccess();
        assert.deepEqual(state.selected, [[id, 2], [id, 1]]);

        context[method]();
        state.loaders[2].onSuccess();
        assert.deepEqual(state.selected, [[id, 2], [id, 1], [id, 0]]);
    });

    test(`${method} tolerates an empty or removed tab section`, () => {
        const { context, state } = createPage({ [id]: { tabs: [] } });
        context[method](-1);
        state.loaders[0].onSuccess();
        assert.deepEqual(state.selected, []);

        context[method]();
        delete state.elements[id];
        state.loaders[1].onSuccess();
        assert.deepEqual(state.selected, []);
    });
}

test('mode icons refer to public image files', () => {
    const markup = fs.readFileSync(path.join(sourceRoot,
        'resources/org/dgfoundation/amp/onepager/components/features/AmpActivityFormFeature.html'), 'utf8');
    for (const [id, name] of [['imgOnepagerMode', 'layout.gif'], ['imgGroupMode', 'group.gif']]) {
        const url = `/resources/images/jsp/admin/view/images/tree/icons/${name}`;
        assert.ok(markup.includes(`id="${id}" alt="" src="${url}"`));
        assert.ok(fs.existsSync(path.join(sourceRoot, 'webapp', url)));
    }
});
