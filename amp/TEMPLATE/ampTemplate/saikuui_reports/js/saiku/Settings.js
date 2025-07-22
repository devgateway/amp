/*  
 *   Copyright 2012 OSBI Ltd
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
 
/**
 * Change settings here
 */
var Settings = {
    BASE_URL: "",
    TOMCAT_WEBAPP: "/saiku",
    DEFAULT_VIEW_STATE: 'view', // could be 'edit' as well
    AMP_PATH: '/rest/data/report',
    AMP_PATH_RUN_TEMP: '/rest/data/report/run',
    PAGINATION: true,
    RESULTS_PER_PAGE: 10,
    USE_AMP_LANGUAGE: true,
    DEFAULT_LANGUAGE: "en",
    DEFAULT_PAGE_TITLE: 'Aid Management Platform - Reports'
};

/**
 * Extend settings with query parameters
 */
Settings.GET = function () {
    var qs = document.location.search;
    if (document.location.search.length == 0) {
    	var hs = document.location.hash;
    	qs = hs.substring(hs.indexOf('?')+1);
    } // the attributes cannot be fetched from location.search if the url contains #
    
    qs = qs.split("+").join(" ");
    var params = {},
        tokens,
        re = /[?&]?([^=]+)=([^&]*)/g;

    while (tokens = re.exec(qs)) {
        var value = decodeURIComponent(tokens[2]);
        if (! isNaN(value)) value = parseInt(value);
        if (value === "true") value = true;
        if (value === "false") value = false;
        params[decodeURIComponent(tokens[1]).toUpperCase()]
            = value;
    }

    return params;
}();
_.extend(Settings, Settings.GET);

Settings.PARAMS = (function() {
    var p = {};
    for (var key in Settings) {
        if (key.match("^PARAM")=="PARAM") {
            p[key] = Settings[key];
        }
    }
    return p;
}());

Settings.REST_URL = Settings.BASE_URL
    + Settings.TOMCAT_WEBAPP 
    + Settings.REST_MOUNT_POINT;

Settings.INITIAL_QUERY = true;
if (document.location.hash) {
    var hash = document.location.hash;
    if (hash.length > 11 && hash.substring(1, 11) == "query/open") {
        Settings.INITIAL_QUERY = true;
    }
}

/**
 * < IE9 doesn't support Array.indexOf
 */
if (!Array.prototype.indexOf)
{
  Array.prototype.indexOf = function(elt /*, from*/)
  {
    var len = this.length >>> 0;

    var from = Number(arguments[1]) || 0;
    from = (from < 0)
         ? Math.ceil(from)
         : Math.floor(from);
    if (from < 0)
      from += len;

    for (; from < len; from++)
    {
      if (from in this &&
          this[from] === elt)
        return from;
    }
    return -1;
  };
}

var tagsToReplace = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;'
};

function replaceTag(tag) {
    return tagsToReplace[tag] || tag;
}

function safe_tags_replace(str) {
    return str.replace(/[&<>]/g, replaceTag);
}

if ($.blockUI) {
    $.blockUI.defaults.css = {};
    $.blockUI.defaults.overlayCSS = {};
    $.blockUI.defaults.blockMsgClass = 'processing';
    $.blockUI.defaults.fadeOut = 0;
    $.blockUI.defaults.fadeIn = 0;
    $.blockUI.defaults.ignoreIfBlocked = false;
}

var isIE = (function(){
    var undef, v = 3; 
    
    var dav = navigator.appVersion;
    
    if(dav.indexOf('MSIE') != -1) {
        v  = parseFloat(dav.split('MSIE ')[1]);
        return v> 4 ? v : false;
    }
    return false;

}());


Settings.Util = {};
Settings.Util.numberToString = function(number, settings) {
	Saiku.logger.log("Settings.numberToString");
	var format = "";
	var stringNumber = null;

	// Create the formatting string to be applied.
	if (settings.useGrouping) {
		format = "0,0";
	}
	//AMP-19648: if number is 'x,xxx.00', show 'x,xxx'
	if (parseInt(number)!==number)
	{
		if (parseInt(settings.maxDecimalDigits) > 0) {
			format = format + "." + new Array(parseInt(settings.maxDecimalDigits) + 1).join("0");	
		}
	}
	// Define a new "language" for Numeral where we can change the default
	// delimiters.
	numeral.language('amp', Settings.Util.createLanguage(settings));
	// Apply new language.
	numeral.language('amp');
	// Apply the format.
	stringNumber = new numeral(number).format(format);
	return stringNumber;
};

Settings.Util.stringToNumber = function(stringNumber, settings) {
	Saiku.logger.log("Settings.stringToNumber");
	var format = "";
	var number = null;
	if(settings === undefined) return new numeral().unformat(stringNumber);

	// Create the formatting string to be applied.
	if (settings.useGrouping) {
		format = "0" + settings.currentThousandSeparator + "0";
	}
	if (settings.maxDecimalDigits > 0) {
		format = format + settings.currentDecimalSeparator + new Array(settings.maxDecimalDigits + 1).join("0");
	}
	// Define a new "language" for Numeral where we can change the default
	// delimiters.
	numeral.language('amp', Settings.Util.createLanguage(settings));
	// Apply new language.
	numeral.language('amp');
	// Apply the format.
	numeral.defaultFormat = format;
	number = new numeral().unformat(stringNumber);
	return number;
};

Settings.Util.createLanguage = function(auxSettings) {
	Saiku.logger.log("Settings.createLanguage");
	var ret = {
		delimiters : {
			thousands : auxSettings.currentThousandSeparator,
			decimal : auxSettings.currentDecimalSeparator
		},
		abbreviations : {
			thousand : 'k',
			million : 'm',
			billion : 'b',
			trillion : 't'
		},
		ordinal : function(number) {
			return number === 1 ? 'st' : 'rds';
		},
		currency : {
			symbol : '$'
		}
	};
	return ret;
};

Settings.AMP_GLOBAL_SETTINGS = {
		HIDE_EDITABLE_EXPORTS: 'hide-editable-export-formats-public-view'
};
