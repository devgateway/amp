var BROWSER_CHROME = 'Chrome';
var BROWSER_FIREFOX = 'Firefox';
var BROWSER_IE = 'Internet Explorer';
var BROWSER_EDGE = 'Edge';
var BROWSER_OPERA = 'Opera';

function get_browser() {
    var ua = navigator.userAgent, tem,
        M = ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
    if (/trident/i.test(M[1])) {
        tem = /\brv[ :]+(\d+)/g.exec(ua) || [];
        return {name: BROWSER_IE, version: (tem[1] || '')};
    }
    if (M[1] === BROWSER_CHROME) {
        tem = ua.match(/\bEdge\/(\d+)/)
        if (tem != null) {
            return {name: BROWSER_EDGE, version: tem[1]};
        }else {
            tem = ua.match(/\bOPR\/(\d+)/)
            if (tem != null) {
                return {name: BROWSER_OPERA, version: tem[1]};
            }
        }
    }
    M = M[2] ? [M[1], M[2]] : [navigator.appName, navigator.appVersion, '-?'];
    if ((tem = ua.match(/version\/(\d+)/i)) != null) {
        M.splice(1, 1, tem[1]);
    }
    return {
        name: M[0],
        version: M[1]
    };
}
