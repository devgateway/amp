var fs = require('fs');
var _ = require('underscore');
var d3 = require('d3');
var util = require('../../ugly/util');
var common = require('./common');
var barSpecific = require('./_bar');
var multibarSpecific = require('./_multibar');
var pieSpecific = require('./_pie');
var table = require('./_table');
var heatmap = require('./_heatmap');
var defaultTTTemplate = _.template(fs.readFileSync(
  __dirname + '/tt-template.html', 'UTF-8'));


var charters = {
  bar: common.nvCharter(barSpecific),
  pie: common.nvCharter(pieSpecific),
  multibar: common.nvCharter(multibarSpecific),
  heatmap: common.nvCharter(heatmap),
  table: table.charter
};


function chart(type, data, options) {
  /*
   * @param type: 'bar' (default), 'pie', or 'multibar'
   * @param data: [{key: 'serisName', values: [{x:NN, y:NN}]}]
   * @param options: optional extra stuff
   */

  options = options || {};

  // settings defaults
  options = _(_(options).clone()).defaults({
    height: void 0,  // should fill container in most cases by default
    width: void 0,
    numberFormatter: d3.format(','),
    shortFormatter: options.numberFormatter || util.formatKMB(3, app.generalSettings.numberFormatSettings.decimalSeparator),
    addLegend: true,
    trimLabels: true,
    getTTContent: common.defaultGetTTContent,
    ttTemplate: defaultTTTemplate,
    clickHandler: common.defaultClickHandler,
    nvControls: true
  });

  var charter = charters[type] || common.fail('unrecognized chart type', type);

  return {
    el: charter(data, options)
  };
}


module.exports = chart;
