var d3 = require('d3-browserify');
var DecimalFormat = require('./DecimalFormat');


var formatKMB = function(precision) {
  var formatSI = d3.format('.' + (precision || 3) + 's');
  return function(value) {
    var value = formatSI(value).replace('G', 'B');  // now just need to convert G Gigia -> B Billion
    return value.replace(value[value.length - 1], ' ' + value[value.length - 1]);
  };
};

var formatPercentage = function(precision) {
  return d3.format(',.' + (precision || 2) + '%');
};

var formatShortText = function(maxWidth) {
  var ellipseWidth = 1;
  return function(text) {
    if (text.length - ellipseWidth > maxWidth) {
      text = text.slice(0, maxWidth - ellipseWidth) + '...';
    }
    return text;
  };
};


var categoryColours = function(cats) {
  // get an appropriate colour scale for the number of categories we are
  // dealing with
  var colours = d3.scale['category' + (cats > 10 ? '20' : '10')]().range();
  return function(d, i) {
    return d.color || colours[i % colours.length];
  };
};


module.exports = {
  formatKMB: formatKMB,
  formatShortText: formatShortText,
  categoryColours: categoryColours,
  DecimalFormat: DecimalFormat,
  formatPercentage: formatPercentage
};
