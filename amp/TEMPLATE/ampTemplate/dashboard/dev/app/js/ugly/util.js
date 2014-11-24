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


var u16le64 = function(str) {
  // base64-encode a string as UTF-16-LE (for MS Excel, probably). It will only
  // work for 2-byte-wide utf-16 characters, and will break at the first hint
  // of any 4-byte char. Two bytes covers the Basic Multiningual Plane, so we
  // should be good.
  var u16num,
      asciiBytePairString = String.fromCharCode(0xFF) + String.fromCharCode(0xFE);
  asciiBytePairString += Array.prototype.reduce.call(str, function(acc, chr) {
    u16num = chr.charCodeAt(0);
    /* jshint bitwise:false */
    return acc + String.fromCharCode(u16num & 0xFF) + String.fromCharCode(u16num >> 8);
    /* jshint bitwise:true */
  }, '');
  return btoa(asciiBytePairString);
};


var textAsDataURL = function(str) {
  return 'data:text/plain;base64,' + u16le64(str);
};


module.exports = {
  formatKMB: formatKMB,
  formatShortText: formatShortText,
  categoryColours: categoryColours,
  u16le64: u16le64,
  textAsDataURL: textAsDataURL
};
