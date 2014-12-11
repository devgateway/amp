var fs = require('fs');
var _ = require('underscore');
var util = require('../../ugly/util');
var common = require('./common');
var tableTemplate = _.template(fs.readFileSync(
  __dirname + '/table.html', 'UTF-8'));


function fakeRawContext(e, data) {
  // transform a table-click event into something that looks like it came from
  // an nvd3 handler
  var t = e.target,
      seriesIndex = parseInt(util.data(t, 'seriesIndex'), 10) || 0,
      xIndex = parseInt(util.data(t, 'xIndex'), 10);
  return {
    label: xIndex && data[seriesIndex].values[xIndex].x,
    seriesIndex: seriesIndex,
    pointIndex: xIndex,
    point: xIndex && data[seriesIndex].values[xIndex]
  };
}


function bindClickHandler(el, data, options) {
  el.addEventListener('click', function(e) {
    var fakeRaw = fakeRawContext(e, data),  // so we can use the shared context transformer
        context = common.getNiceContext(fakeRaw, data, e.target.innerText);
    options.clickHandler(context);
  });
}


function charter(data, options) {

  var keys = _(data).pluck('key');

  var values = _(data)
    .chain()
    .map(function(datum) { return datum.values; })
    .transpose()
    .value();

  var html = tableTemplate({
    keys: keys,
    values: values,
    moneyFormat: options.shortFormatter
  });

  var chartEl = document.createElement('div');
  chartEl.innerHTML = html;

  bindClickHandler(chartEl, data, options);

  return chartEl;
}


module.exports = {
  charter: charter
};
