/*
 * Underscore mixin for many-to-many groupings
 *
 * Sample Usage:
 *
 * var things = [
 *   {n: [1], name: 'alice'},
 *   {n: [2], name: 'bob'},
 *   {n: [1, 2], name: 'alob'}
 * ];
 * _(things).groupsBy(function (thing) { return thing.n; });
 *   // => {
 *           1: [{n: [1], name: 'alice'}, {n: [1, 2], name: 'alob'}],
 *           2: [{n: [2], name: 'bob'}, {n: [1, 2], name: 'alob'}]
 *         }
 *
 * The return value of `iterator` is expected to always be an array instead of
 * a value, as you would give to `_.groupBy`.
 */


function mix(_) {

  function groupsBy(list, iterator, context) {
    var mapping = {},
        keys;
    _(list).each(function(thing) {
      keys = iterator.apply(context, arguments);
      _(keys).each(function(key) {
        (mapping[key] || (mapping[key]=[])).push(thing);
      });
    });
    return mapping;
  }

  _.mixin({groupsBy: groupsBy});
  return groupsBy;
}


// mix with any globally-defined underscore
if (typeof _ !== "undefined" && _ !== null) {
  mix(_);
}

// mix with node/browserify requires
if (typeof module !== "undefined" && module !== null) {
  var nodeGroups = mix(require('underscore'));
  module.exports = nodeGroups;
}

// mix with requirejs underscore
if ((typeof requirejs !== "undefined" && requirejs !== null) &&
    (typeof define !== "undefined" && define !== null)) {
  define(['underscore'], mix);
}
