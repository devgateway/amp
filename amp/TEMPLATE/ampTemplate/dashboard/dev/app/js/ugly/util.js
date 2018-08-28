// hopefully not that ugly, but seemed as good a place as any for this stuff...

var d3 = require('d3');


var formatKMB = function(precision, decimalSeparator) {
  var app = window.app;
  var formatSI = d3.format('.' + (precision || 3) + 's');
  decimalSeparator = decimalSeparator || '.';
  return function(value) {
    return formatSI(value)
      .replace('k', app.translator.translateSync('amp.dashboard:chart-thousand'))
      .replace('M', app.translator.translateSync('amp.dashboard:chart-million'))
      .replace('G', app.translator.translateSync('amp.dashboard:chart-billion'))  // now just need to convert G Gigia -> B Billion
      .replace('T', app.translator.translateSync('amp.dashboard:chart-trillion'))
      .replace('P', app.translator.translateSync('amp.dashboard:chart-peta'))
      .replace('E', app.translator.translateSync('amp.dashboard:chart-exa'))
      .replace('.', decimalSeparator);
  };
};

var translateLanguage = function(value) {
  var app = window.app;
  return value
    .replace('k', app.translator.translateSync('amp.dashboard:chart-thousand'))
    .replace('M', app.translator.translateSync('amp.dashboard:chart-million'))
    .replace('B', app.translator.translateSync('amp.dashboard:chart-billion'))
    .replace('T', app.translator.translateSync('amp.dashboard:chart-trillion'))
    .replace('P', app.translator.translateSync('amp.dashboard:chart-peta'))
    .replace('E', app.translator.translateSync('amp.dashboard:chart-exa'));
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
var formatOfTotal = function(dividend, divisor,ofTotal) {
    var isRtl = app.generalSettings.attributes['rtl-direction'];

    if (dividend > 0) {
        var number = d3.format('f')(dividend / divisor * 100);
        if (isRtl) {
            ofTotal = ofTotal + ' &nbsp<span>' + '<b>% ' + number + '</b>';
        } else {
            ofTotal = '<b>' + number + ' %' + '</b>&nbsp<span>' + ofTotal;
        }
    }
    return ofTotal;
}


var categoryColours = function(cats) {
  // get an appropriate colour scale for the number of categories we are
  // dealing with
  var colours = d3.scale['category20']().range();
  return function(d, i) {
    return d.color || (d.data && d.data.color) || colours[i % colours.length];
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


function transformArgs(transformer, wrapped) {
  return function(/* arguments */) {
    var transformedArgs = transformer.apply(null, arguments);
    return wrapped.apply(null, transformedArgs);
  };
}


function toDashed(name) {
  // transform namesLikeThis to names-like-this
  return name.replace(/([A-Z])/g, function(u) {
    return '-' + u.toLowerCase();
  });
}


function data(el, name, newValue) {
  if (newValue === void 0) {
    return el.getAttribute('data-' + toDashed(name));
  }
  el.setAttribute('data-' + toDashed(name), newValue);
}

/**
 * This function calculates how much height we need to show a readable chart with different number of legends. 
 */
function calculateChartHeight(length, isDownload, model) {
	var height = null;
	var bigN = null;
	if (length < 30) {
		bigN = '0';
		if (isDownload === true) {
			height = 450;
		}
	} else if(length >= 30 && length < 40) {
		if (isDownload === true) {
			height = 550;
		}  else {
			height = 475;
		}
		bigN = '1';
	} else if(length >= 40 && length < 50) {
		if (isDownload === true) {
			height = 700;
		} else {
			height = 625;
		}
		bigN = '2';
	} else if(length >= 50 && length < 60) {
		if (isDownload === true) {
			height = 850;
		} else {
			height = 775;
		}
		bigN = '3';
	} else if(length >= 60 && length < 70) {
		if (isDownload === true) {
			height = 1000;
		} else {
			height = 925;
		}
		bigN = '4';
	} else if(length >= 70 && length < 80) {
		if (isDownload === true) {
			height = 1150;
		} else {
			height = 1075;
		}
		bigN = '5';
	} else if(length >= 80 && length < 90) {
		if (isDownload === true) {
			height = 1150;
		} else {
			height = 1225;
		}
		bigN = '6';	
	} else if(length >= 90) {
		// Seriously????
		if (isDownload === true) {
			height = 1300;
		} else {
			height = 1375;
		}
		bigN = '7';
	}
	if (model !== undefined) {
		model.set('bigN', bigN);
	}
	return height
}


module.exports = {
    formatKMB: formatKMB,
    translateLanguage: translateLanguage,
    formatShortText: formatShortText,
    categoryColours: categoryColours,
    u16le64: u16le64,
    textAsDataURL: textAsDataURL,
    transformArgs: transformArgs,
    data: data,
    calculateChartHeight: calculateChartHeight,
    formatOfTotal: formatOfTotal
};
