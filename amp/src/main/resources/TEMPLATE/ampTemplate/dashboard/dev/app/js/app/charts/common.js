var nv = window.nv;  // nvd3 is a pain
var d3 = require('d3');
var util = require('../../ugly/util');
var Numeral = require('numeral');
var _ = require('underscore');


// hack nvd3's calcApproxTextWidth because it is terrrrrrrrrrible
// this solution is just as bad :(
// nvd3 makes me so sad :(
(function(unfortunateLib) {
  var sadWayOfCalculatingTextWidth = unfortunateLib.utils.calcApproxTextWidth;
  unfortunateLib.utils.calcApproxTextWidth = function(svgTextEl) {
    var originalText = svgTextEl.text(),
        spacePaddedText = originalText,
        numberOfDots = (originalText.match(/\./g) || []).length,
        poorlyCalculatedWidth;

    if (originalText.toUpperCase() === originalText) {
      for (var i = 0; i < originalText.length / 5; i++) { spacePaddedText += ' '; }
      svgTextEl.text(spacePaddedText);
      poorlyCalculatedWidth = sadWayOfCalculatingTextWidth(svgTextEl);
      svgTextEl.text(originalText);
    } else {
      poorlyCalculatedWidth = sadWayOfCalculatingTextWidth(svgTextEl);
    }

    // ♪ do you believe in maaaagic ♫
    poorlyCalculatedWidth -=
      (poorlyCalculatedWidth / spacePaddedText.length) * (numberOfDots / 3);
    // ♪ magic magic magic ♫
    poorlyCalculatedWidth += 6;

    return poorlyCalculatedWidth;
  };
})(nv);


function fail(/* arguments */) {
  throw {
    name: 'Chart Error',
    message: Array.prototype.join.call(arguments, ', '),
    toString: function() { return this.name + ': ' + this.message; }
  };
}


function nvChartCommon(nvChart, maxCategories) {
  return nvChart
    .x(function(d) { return util.formatShortText(19)(d.x); })  // fix overflows in legends
    .color(util.categoryColours(maxCategories));
}


function getSecretRenderArea(height, width) {
  var secretArea = document.getElementById('super-secret-render-area-shh');
  if (!secretArea) {
    secretArea = document.createElement('div');
    secretArea.id = 'super-secret-render-area-shh';
    secretArea.style.position = 'absolute';
    secretArea.style.left = '-9999em';
    secretArea.style.top = '-9999em';  // for IE :(
    document.body.appendChild(secretArea);
  }
  if (height) { secretArea.style.height = height + 'px'; }
  if (width) { secretArea.style.width = width + 'px'; }
  return secretArea;
}


function mkChartSVG(height, width) {
  var svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
  svg.setAttribute('class', 'dash-chart');
  // put it in the dom but out of the way, so we can compute styles n stuff
  getSecretRenderArea(height, width).appendChild(svg);
  return svg;
}


function nvBoiler(nvData, chart, svg, specific, trimLabels, addLegend, width) {
  if (trimLabels) {
    chart.x(function(d) { return util.formatShortText(50)(d.x); });
  }
  if (addLegend && specific.addLegend) {
    specific.addLegend(svg, chart, nvData, trimLabels, width);
  } else if (!addLegend && specific.removeLegend) {
    specific.removeLegend(chart);
  }
  d3.select(svg)
    .datum(nvData)
    .call(chart);
  nv.utils.windowResize(chart.update);  // uuuuuguggggh
  nv.addGraph(function() { return chart; });  // uuuuugly is nvd3
  return svg;
}


function nvColorifyCategories(chart, data, specific) {
  var numberOfCategories = specific.countCategories(data);
  chart.color(util.categoryColours(numberOfCategories));
  return chart;
}


function defaultGetTTContent(context) {
  return {tt: {
    heading: context.x.raw,
    bodyText: context.y.raw,
    formattedAmount: context.z.raw
  }};
}


function getNiceContext(raw, data, fmtY) {
  var seriesIndex = raw.seriesIndex || 0;
  return {
    data: data,
    series: {
      key: data[seriesIndex].key,
      index: seriesIndex,
      values: data[seriesIndex].values
    },
    x: {
      raw: raw.point.x,
      fmt: raw.label,
      index: raw.pointIndex || raw.index || 0
    },
    y: {
      raw: raw.point.y,
      fmt: raw.point.z || fmtY
    }
  };
}


function nvBindTooltip(chart, data, specific, template, getTTContent) {
  var nvTTHandler = function(seriesName, fmtX, fmtY, raw) {
    return template(getTTContent(getNiceContext(raw, data, fmtY)));
  };

  if (specific.normalizeNvTTArgs) { // we might have to fix nv's inconsistent TT args
    nvTTHandler = util.transformArgs(specific.normalizeNvTTArgs, nvTTHandler);
  }
  chart.tooltipContent(nvTTHandler);
}


function defaultClickHandler(/* context */) {
  // does nothing
}


function nvBindOthersCb(chart, data, specific, clickHandler) {
  if (chart[specific.dispatchName] !== undefined) {
	  chart[specific.dispatchName].dispatch.on('elementClick', function(raw) {
		  clickHandler(getNiceContext(raw, data));
	  });
  }	else {
	  console.warn("Cant find " + specific.dispatchName);
  }
}


function nvCharter(specific) {
  return function(data, options) {
    var svg = mkChartSVG(options.height, options.width),
        nvChart = specific.chart(options, data),
        nvData = specific.dataToNv(data);
    nvColorifyCategories(nvChart, data, specific);
    nvBindTooltip(nvChart, data, specific, options.ttTemplate, options.getTTContent);
    nvBoiler(nvData, nvChart, svg, specific, options.trimLabels, options.addLegend, options.width);
    nvBindOthersCb(nvChart, data, specific, options.clickHandler);
    return svg;
  };
}

function formatNumber(number) {
	var format = "";
	if (app.generalSettings.numberFormatSettings.groupSeparator.length > 0) {
		format = "0,0";
	} else {
		format = "0";
	}
	if (app.generalSettings.numberFormatSettings.numberFormat.indexOf('.') > 0) {
		var decimalDigits = app.generalSettings.numberFormatSettings.numberFormat.length
			- app.generalSettings.numberFormatSettings.numberFormat.indexOf('.');
		format = format + "." + new Array(decimalDigits).join("0");
	}

	// Define a new "language" for Numeral where we can change the default
	// delimiters.
	var ampLang = {
		delimiters : {
			thousands : app.generalSettings.numberFormatSettings.groupSeparator,
			decimal : app.generalSettings.numberFormatSettings.decimalSeparator
		},
		abbreviations : {
			thousand : app.translator.translateSync('amp.dashboard:chart-thousand'),
			million : app.translator.translateSync('amp.dashboard:chart-million'),
			billion : app.translator.translateSync('amp.dashboard:chart-billion'),
			trillion : app.translator.translateSync('amp.dashboard:chart-trillion')
		},
		ordinal : function(number) {
			return number === 1 ? 'st' : 'rds';
		},
		currency : {
			symbol : '$'
		}
	};
	Numeral.language('amp', ampLang);
	// Apply new language.
	Numeral.language('amp');
	// Apply the format.
	var stringNumber = new Numeral(number).format(format);
	return stringNumber;
}

var ampLang =


module.exports = {
  fail: fail,
  nvChartCommon: nvChartCommon,
  getNiceContext: getNiceContext,
  defaultGetTTContent: defaultGetTTContent,
  defaultClickHandler: defaultClickHandler,
  mkChartSVG: mkChartSVG,
  nvBoiler: nvBoiler,
  nvCharter: nvCharter,
  formatNumber: formatNumber
};
