// hopefully not that ugly, but seemed as good a place as any for this stuff...

var d3 = require('d3-browserify');


var formatKMB = function(precision) {
  var formatSI = d3.format('.' + (precision || 3) + 's');
  return function(value) {
    return formatSI(value)
      .replace('G', 'B');  // now just need to convert G Gigia -> B Billion
  };
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
  categoryColours: categoryColours
};
