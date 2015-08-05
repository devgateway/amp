/* non-sensical library */
define([], function() {
  var number = 1;

  function addNumber(n) {
    return n + number;
  }

  return addNumber;
});
