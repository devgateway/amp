const assert = require('node:assert/strict');
const fs = require('node:fs');
const path = require('node:path');
const test = require('node:test');
const vm = require('node:vm');

const plugin = path.resolve(__dirname,
    '../../../TEMPLATE/ampTemplate/saikuui_reports/js/saiku/plugins/AMPFilters/plugin.js');

for (const [reportType, expected] of [[7, 'I'], [5, 'P'], [1, 'D']]) {
    test(`report type ${reportType} initializes its ${expected} filter list`, () => {
        let options;
        const context = {
            Saiku: {
                originalReportMetadata: { reportMetadata: { reportSpec: { reportType } } },
                events: { bind() {} }
            },
            Backbone: { View: { extend() {} } },
            ampFilter: function (value) { options = value; },
            $: () => ({})
        };
        context.window = context;
        vm.runInNewContext(fs.readFileSync(plugin, 'utf8'), context, { filename: plugin });
        assert.equal(options.caller, 'REPORTS');
        assert.equal(options.reportType, expected);
    });
}
