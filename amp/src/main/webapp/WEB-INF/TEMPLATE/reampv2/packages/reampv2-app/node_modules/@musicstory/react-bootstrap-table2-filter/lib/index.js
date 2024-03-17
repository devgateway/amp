'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.customFilter = exports.dateFilter = exports.numberFilter = exports.multiSelectFilter = exports.selectFilter = exports.textFilter = exports.Comparator = exports.FILTER_TYPES = undefined;

var _text = require('./src/components/text');

var _text2 = _interopRequireDefault(_text);

var _select = require('./src/components/select');

var _select2 = _interopRequireDefault(_select);

var _multiselect = require('./src/components/multiselect');

var _multiselect2 = _interopRequireDefault(_multiselect);

var _number = require('./src/components/number');

var _number2 = _interopRequireDefault(_number);

var _date = require('./src/components/date');

var _date2 = _interopRequireDefault(_date);

var _context = require('./src/context');

var _context2 = _interopRequireDefault(_context);

var _comparison = require('./src/comparison');

var Comparison = _interopRequireWildcard(_comparison);

var _const = require('./src/const');

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = function () {
  var options = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
  return {
    createContext: _context2.default,
    options: options
  };
};

var FILTER_TYPES = exports.FILTER_TYPES = _const.FILTER_TYPE;

var Comparator = exports.Comparator = Comparison;

var textFilter = exports.textFilter = function textFilter() {
  var props = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
  return {
    Filter: _text2.default,
    props: props
  };
};

var selectFilter = exports.selectFilter = function selectFilter() {
  var props = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
  return {
    Filter: _select2.default,
    props: props
  };
};

var multiSelectFilter = exports.multiSelectFilter = function multiSelectFilter() {
  var props = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
  return {
    Filter: _multiselect2.default,
    props: props
  };
};

var numberFilter = exports.numberFilter = function numberFilter() {
  var props = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
  return {
    Filter: _number2.default,
    props: props
  };
};

var dateFilter = exports.dateFilter = function dateFilter() {
  var props = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
  return {
    Filter: _date2.default,
    props: props
  };
};

var customFilter = exports.customFilter = function customFilter() {
  var props = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
  return {
    props: props
  };
};