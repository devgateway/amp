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

  return missingFeatures;
};
