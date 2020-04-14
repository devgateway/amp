var BROWSER_CHROME='Chrome';
var BROWSER_FIREFOX='Firefox';
var BROWSER_IE='Internet Explorer';
var BROWSER_SAFARI = 'Safari';
var BROWSER_OPERA = 'Opera';
var BROWSER_MOZILLA = 'Mozilla';
var browserDetection = {
    header: [navigator.platform, navigator.userAgent, navigator.appVersion, navigator.vendor, window.opera],
    databrowser: [
        {name: BROWSER_CHROME, value: 'Chrome', version: 'Chrome'},
        {name: BROWSER_FIREFOX, value: 'Firefox', version: 'Firefox'},
        {name: BROWSER_SAFARI, value: 'Safari', version: 'Version'},
        {name: BROWSER_IE, value: 'MSIE', version: 'MSIE'},
        {name: BROWSER_OPERA, value: 'Opera', version: 'Opera'},
        {name: BROWSER_MOZILLA, value: 'Mozilla', version: 'Mozilla'}
    ],
    init: function () {
        var agent = this.header.join(' '),
            browser = this.matchItem(agent, this.databrowser);
        return {browser: browser};
    },
    matchItem: function (string, data) {
        var i = 0,
            j = 0,
            html = '',
            regex,
            regexv,
            match,
            matches,
            version;
        for (i = 0; i < data.length; i += 1) {
            regex = new RegExp(data[i].value, 'i');
            match = regex.test(string);
            if (match) {
                regexv = new RegExp(data[i].version + '[- /:;]([\\d._]+)', 'i');
                matches = string.match(regexv);
                version = '';
                if (matches) {
                    if (matches[1]) {
                        matches = matches[1];
                    }
                }
                if (matches) {
                    matches = matches.split(/[._]+/);
                    for (j = 0; j < matches.length; j += 1) {
                        if (j === 0) {
                            version += matches[j] + '.';
                        } else {
                            version += matches[j];
                        }
                    }
                } else {
                    version = '0';
                }
                return {
                    name: data[i].name,
                    version: parseFloat(version)
                };
            }
        }
        return {name: 'unknown', version: 0};
    }
};