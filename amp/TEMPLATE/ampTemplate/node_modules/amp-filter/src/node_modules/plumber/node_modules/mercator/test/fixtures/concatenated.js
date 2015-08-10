var x = 3;
var y = x;

/* a comment */

function inc(x) {
    return x + 1;
}

var z = inc(x);


/* non-sensical library */
define([], function() {
  var number = 1;

  function addNumber(n) {
    return n + number;
  }

  return addNumber;
});
//# sourceMappingURL=concatenated.js.map