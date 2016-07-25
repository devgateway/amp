var fs = require('fs');
var _ = require('underscore');
var util = require('../../ugly/util');
var common = require('./common');
var tableTemplate = _.template(fs.readFileSync(
  __dirname + '/table.html', 'UTF-8'));
var heatmapTableTemplate = _.template(fs.readFileSync(
		  __dirname + '/heatmap-table.html', 'UTF-8'));


function fakeRawContext(e, data) {
  // transform a table-click event into something that looks like it came from
  // an nvd3 handler
  var t = e.target,
      seriesIndex = parseInt(util.data(t, 'seriesIndex'), 10) || 0,
      xIndex = parseInt(util.data(t, 'xIndex'), 10);
  return {
    label: data[seriesIndex].values[xIndex].x || xIndex,
    seriesIndex: seriesIndex,
    pointIndex: xIndex,
    point: data[seriesIndex].values[xIndex] || xIndex
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
	if (options.model.get('chartType') === 'fragmentation') {
		return heatmapCharter(data, options);
	} else {
		return commonCharter(data, options);
	}	
}

function commonCharter(data, options) {
	var keys = _(data).pluck('key');

	var values = _(data).chain().map(function(datum) {
		return datum.values;
	}).transpose().value();

	var html = tableTemplate({
		keys : keys,
		values : values,
		moneyFormat : options.shortFormatter
	});

	var chartEl = document.createElement('div');
	chartEl.innerHTML = html;

	bindClickHandler(chartEl, data, options);

	return chartEl;
}

function heatmapCharter(data, options) {
	var keys = _(data).pluck('key');

	var matrix = _.map(options.model.get("matrix"), function(itemY, i) {
		return _.map(itemY, function(itemX, j) {
			return {
				y : options.model.get("yDataSet")[i],
				x : options.model.get("xDataSet")[j],
				value : (options.model.get("matrix")[i][j] ? options.model.get("matrix")[i][j] : {
					'dv' : '',
					'p' : ''
				})
			}
		})
	});

	var html = heatmapTableTemplate({
		keys : keys,
		matrix : matrix,
		moneyFormat : options.shortFormatter
	});

	var chartEl = document.createElement('div');
	chartEl.innerHTML = html;
	
	bindClickHandler(chartEl, data, options);

	return chartEl;
}


module.exports = {
  charter: charter
};