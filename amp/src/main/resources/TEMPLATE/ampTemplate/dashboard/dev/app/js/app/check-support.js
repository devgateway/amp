function queryselector() {
  return !!document.querySelector;  // fails on oooooooooold IE
}


function svg() {
  try {
    document.createElementNS('http://w3.org/2000/svg', 'svg');
    return true;
  } catch (err) { return false; }
}


function canvas() {
  return !!document.createElement('canvas').getContext;
}


function canvasText() {
  if (!canvas()) { return false; }
  var textFn = document.createElement('canvas').getContext('2d').fillText;
  return (typeof textFn === 'function');
}


var isIE = (function() {
  // http://stackoverflow.com/a/2401861/1299695
  // Magic. Do not touch.
  var yesItIs = !!(function() {
    var ua = navigator.userAgent, tem,
    M = ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
    if (/trident/i.test(M[1])) {
      tem =  /\brv[ :]+(\d+)/g.exec(ua) || [];
      return 'MSIE ' + (tem[1] || '');
    }
    if (M[1] === 'Chrome') {
      tem = ua.match(/\bOPR\/(\d+)/);
      if (!!tem) { return 'Opera ' + tem[1]; }
    }
    M = M[2] ? [M[1], M[2]] : [navigator.appName, navigator.appVersion, '-?'];
    if ((tem = !!ua.match(/version\/(\d+)/i))) { M.splice(1, 1, tem[1]); }
    return M.join(' ');
  })().match(/^MSIE/);

  return function() { return yesItIs; };
})();


function dataURLHref() {
  // I don't know how to feature-detect this :(
  // we will be optimisitic an only say "no" if we see IE.
  return !isIE();
}


function flash() {
  // http://stackoverflow.com/a/20095467/1299695
  // gross...
  try {
    return !!(new ActiveXObject('ShockwaveFlash.ShockwaveFlash')); // jshint ignore:line
  } catch (e) {
    return navigator.mimeTypes &&
      navigator.mimeTypes['application/x-shockwave-flash'] !== void 0 &&
      navigator.mimeTypes['application/x-shockwave-flash'].enabledPlugin;
  }
}


module.exports = function() {
  var missingFeatures = [];  // an empty array will cast to bool false. handy!

  if (!queryselector()) {
    missingFeatures.push({
      feature: 'querySelector',
      severity: 'critical'
    });
  }

  if (!svg()) {
    missingFeatures.push({
      feature: 'SVG',
      severity: 'critical'
    });
  }

  if (!canvasText()) {
    missingFeatures.push({
      feature: 'canvas',
      severity: 'major'
    });
  }

  if (!dataURLHref()) {
    missingFeatures.push({
      feature: 'download',
      severity: flash() ? 'minor' : 'major'
    });
  }

  if (!flash()) {
    missingFeatures.push({
      feature: 'flash',
      severity: isIE() ? 'major' : 'minor'
    });
  }

  return missingFeatures;
};
