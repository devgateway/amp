// hopefully not that ugly, but seemed as good a place as any for this stuff...

var d3 = require('d3');


var formatKMB = function(precision) {
  var formatSI = d3.format('.' + (precision || 3) + 's');
  return function(value) {
    return formatSI(value)
      .replace('G', 'B');  // now just need to convert G Gigia -> B Billion
  }
}


module.exports = {
  formatKMB: formatKMB
};
