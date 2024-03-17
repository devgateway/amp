(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory(require("react"));
	else if(typeof define === 'function' && define.amd)
		define(["react"], factory);
	else if(typeof exports === 'object')
		exports["ReactBootstrapTable2Filter"] = factory(require("react"));
	else
		root["ReactBootstrapTable2Filter"] = factory(root["React"]);
})(this, function(__WEBPACK_EXTERNAL_MODULE_2__) {
return /******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};
/******/
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/
/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId]) {
/******/ 			return installedModules[moduleId].exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			i: moduleId,
/******/ 			l: false,
/******/ 			exports: {}
/******/ 		};
/******/
/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);
/******/
/******/ 		// Flag the module as loaded
/******/ 		module.l = true;
/******/
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/
/******/
/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;
/******/
/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;
/******/
/******/ 	// define getter function for harmony exports
/******/ 	__webpack_require__.d = function(exports, name, getter) {
/******/ 		if(!__webpack_require__.o(exports, name)) {
/******/ 			Object.defineProperty(exports, name, {
/******/ 				configurable: false,
/******/ 				enumerable: true,
/******/ 				get: getter
/******/ 			});
/******/ 		}
/******/ 	};
/******/
/******/ 	// getDefaultExport function for compatibility with non-harmony modules
/******/ 	__webpack_require__.n = function(module) {
/******/ 		var getter = module && module.__esModule ?
/******/ 			function getDefault() { return module['default']; } :
/******/ 			function getModuleExports() { return module; };
/******/ 		__webpack_require__.d(getter, 'a', getter);
/******/ 		return getter;
/******/ 	};
/******/
/******/ 	// Object.prototype.hasOwnProperty.call
/******/ 	__webpack_require__.o = function(object, property) { return Object.prototype.hasOwnProperty.call(object, property); };
/******/
/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";
/******/
/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(__webpack_require__.s = 4);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
var LIKE = exports.LIKE = 'LIKE';
var EQ = exports.EQ = '=';
var NE = exports.NE = '!=';
var GT = exports.GT = '>';
var GE = exports.GE = '>=';
var LT = exports.LT = '<';
var LE = exports.LE = '<=';

/***/ }),
/* 1 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
var FILTER_TYPE = exports.FILTER_TYPE = {
  TEXT: 'TEXT',
  SELECT: 'SELECT',
  MULTISELECT: 'MULTISELECT',
  NUMBER: 'NUMBER',
  DATE: 'DATE'
};

var FILTER_DELAY = exports.FILTER_DELAY = 500;

/***/ }),
/* 2 */
/***/ (function(module, exports) {

module.exports = __WEBPACK_EXTERNAL_MODULE_2__;

/***/ }),
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

/**
 * Copyright 2013-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

if (false) {
  var REACT_ELEMENT_TYPE = (typeof Symbol === 'function' &&
    Symbol.for &&
    Symbol.for('react.element')) ||
    0xeac7;

  var isValidElement = function(object) {
    return typeof object === 'object' &&
      object !== null &&
      object.$$typeof === REACT_ELEMENT_TYPE;
  };

  // By explicitly using `prop-types` you are opting into new development behavior.
  // http://fb.me/prop-types-in-prod
  var throwOnDirectAccess = true;
  module.exports = require('./factoryWithTypeCheckers')(isValidElement, throwOnDirectAccess);
} else {
  // By explicitly using `prop-types` you are opting into new production behavior.
  // http://fb.me/prop-types-in-prod
  module.exports = __webpack_require__(6)();
}


/***/ }),
/* 4 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.customFilter = exports.dateFilter = exports.numberFilter = exports.multiSelectFilter = exports.selectFilter = exports.textFilter = exports.Comparator = exports.FILTER_TYPES = undefined;

var _text = __webpack_require__(5);

var _text2 = _interopRequireDefault(_text);

var _select = __webpack_require__(10);

var _select2 = _interopRequireDefault(_select);

var _multiselect = __webpack_require__(11);

var _multiselect2 = _interopRequireDefault(_multiselect);

var _number = __webpack_require__(12);

var _number2 = _interopRequireDefault(_number);

var _date = __webpack_require__(13);

var _date2 = _interopRequireDefault(_date);

var _context = __webpack_require__(14);

var _context2 = _interopRequireDefault(_context);

var _comparison = __webpack_require__(0);

var Comparison = _interopRequireWildcard(_comparison);

var _const = __webpack_require__(1);

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

/***/ }),
/* 5 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(2);

var _react2 = _interopRequireDefault(_react);

var _propTypes = __webpack_require__(3);

var _comparison = __webpack_require__(0);

var _const = __webpack_require__(1);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/require-default-props: 0 */
/* eslint react/prop-types: 0 */
/* eslint no-return-assign: 0 */
/* eslint camelcase: 0 */


var TextFilter = function (_Component) {
  _inherits(TextFilter, _Component);

  function TextFilter(props) {
    _classCallCheck(this, TextFilter);

    var _this = _possibleConstructorReturn(this, (TextFilter.__proto__ || Object.getPrototypeOf(TextFilter)).call(this, props));

    _this.filter = _this.filter.bind(_this);
    _this.handleClick = _this.handleClick.bind(_this);
    _this.timeout = null;
    function getDefaultValue() {
      if (props.filterState && typeof props.filterState.filterVal !== 'undefined') {
        return props.filterState.filterVal;
      }
      return props.defaultValue;
    }
    _this.state = {
      value: getDefaultValue()
    };
    return _this;
  }

  _createClass(TextFilter, [{
    key: 'componentDidMount',
    value: function componentDidMount() {
      var _this2 = this;

      var _props = this.props,
          onFilter = _props.onFilter,
          getFilter = _props.getFilter,
          column = _props.column;

      var defaultValue = this.input.value;

      if (defaultValue) {
        onFilter(this.props.column, _const.FILTER_TYPE.TEXT, true)(defaultValue);
      }

      // export onFilter function to allow users to access
      if (getFilter) {
        getFilter(function (filterVal) {
          _this2.setState(function () {
            return { value: filterVal };
          });
          onFilter(column, _const.FILTER_TYPE.TEXT)(filterVal);
        });
      }
    }
  }, {
    key: 'componentWillUnmount',
    value: function componentWillUnmount() {
      this.cleanTimer();
    }
  }, {
    key: 'UNSAFE_componentWillReceiveProps',
    value: function UNSAFE_componentWillReceiveProps(nextProps) {
      if (nextProps.defaultValue !== this.props.defaultValue) {
        this.applyFilter(nextProps.defaultValue);
      }
    }
  }, {
    key: 'filter',
    value: function filter(e) {
      var _this3 = this;

      e.stopPropagation();
      this.cleanTimer();
      var filterValue = e.target.value;
      this.setState(function () {
        return { value: filterValue };
      });
      this.timeout = setTimeout(function () {
        _this3.props.onFilter(_this3.props.column, _const.FILTER_TYPE.TEXT)(filterValue);
      }, this.props.delay);
    }
  }, {
    key: 'cleanTimer',
    value: function cleanTimer() {
      if (this.timeout) {
        clearTimeout(this.timeout);
      }
    }
  }, {
    key: 'cleanFiltered',
    value: function cleanFiltered() {
      var value = this.props.defaultValue;
      this.setState(function () {
        return { value: value };
      });
      this.props.onFilter(this.props.column, _const.FILTER_TYPE.TEXT)(value);
    }
  }, {
    key: 'applyFilter',
    value: function applyFilter(filterText) {
      this.setState(function () {
        return { value: filterText };
      });
      this.props.onFilter(this.props.column, _const.FILTER_TYPE.TEXT)(filterText);
    }
  }, {
    key: 'handleClick',
    value: function handleClick(e) {
      e.stopPropagation();
      if (this.props.onClick) {
        this.props.onClick(e);
      }
    }
  }, {
    key: 'render',
    value: function render() {
      var _this4 = this;

      var _props2 = this.props,
          id = _props2.id,
          placeholder = _props2.placeholder,
          _props2$column = _props2.column,
          dataField = _props2$column.dataField,
          text = _props2$column.text,
          style = _props2.style,
          className = _props2.className,
          onFilter = _props2.onFilter,
          caseSensitive = _props2.caseSensitive,
          defaultValue = _props2.defaultValue,
          getFilter = _props2.getFilter,
          filterState = _props2.filterState,
          rest = _objectWithoutProperties(_props2, ['id', 'placeholder', 'column', 'style', 'className', 'onFilter', 'caseSensitive', 'defaultValue', 'getFilter', 'filterState']);

      var elmId = 'text-filter-column-' + dataField + (id ? '-' + id : '');

      return _react2.default.createElement(
        'label',
        {
          className: 'filter-label',
          htmlFor: elmId
        },
        _react2.default.createElement(
          'span',
          { className: 'sr-only' },
          'Filter by ',
          text
        ),
        _react2.default.createElement('input', _extends({}, rest, {
          ref: function ref(n) {
            return _this4.input = n;
          },
          type: 'text',
          id: elmId,
          className: 'filter text-filter form-control ' + className,
          style: style,
          onChange: this.filter,
          onClick: this.handleClick,
          placeholder: placeholder || 'Enter ' + text + '...',
          value: this.state.value
        }))
      );
    }
  }]);

  return TextFilter;
}(_react.Component);

TextFilter.propTypes = {
  onFilter: _propTypes.PropTypes.func.isRequired,
  column: _propTypes.PropTypes.object.isRequired,
  id: _propTypes.PropTypes.string,
  filterState: _propTypes.PropTypes.object,
  comparator: _propTypes.PropTypes.oneOf([_comparison.LIKE, _comparison.EQ]),
  defaultValue: _propTypes.PropTypes.string,
  delay: _propTypes.PropTypes.number,
  placeholder: _propTypes.PropTypes.string,
  style: _propTypes.PropTypes.object,
  className: _propTypes.PropTypes.string,
  caseSensitive: _propTypes.PropTypes.bool,
  getFilter: _propTypes.PropTypes.func
};

TextFilter.defaultProps = {
  delay: _const.FILTER_DELAY,
  filterState: {},
  defaultValue: '',
  caseSensitive: false,
  id: null
};

exports.default = TextFilter;

/***/ }),
/* 6 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright 2013-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */



var emptyFunction = __webpack_require__(7);
var invariant = __webpack_require__(8);
var ReactPropTypesSecret = __webpack_require__(9);

module.exports = function() {
  function shim(props, propName, componentName, location, propFullName, secret) {
    if (secret === ReactPropTypesSecret) {
      // It is still safe when called from React.
      return;
    }
    invariant(
      false,
      'Calling PropTypes validators directly is not supported by the `prop-types` package. ' +
      'Use PropTypes.checkPropTypes() to call them. ' +
      'Read more at http://fb.me/use-check-prop-types'
    );
  };
  shim.isRequired = shim;
  function getShim() {
    return shim;
  };
  // Important!
  // Keep this list in sync with production version in `./factoryWithTypeCheckers.js`.
  var ReactPropTypes = {
    array: shim,
    bool: shim,
    func: shim,
    number: shim,
    object: shim,
    string: shim,
    symbol: shim,

    any: shim,
    arrayOf: getShim,
    element: shim,
    instanceOf: getShim,
    node: shim,
    objectOf: getShim,
    oneOf: getShim,
    oneOfType: getShim,
    shape: getShim
  };

  ReactPropTypes.checkPropTypes = emptyFunction;
  ReactPropTypes.PropTypes = ReactPropTypes;

  return ReactPropTypes;
};


/***/ }),
/* 7 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


/**
 * Copyright (c) 2013-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 *
 * 
 */

function makeEmptyFunction(arg) {
  return function () {
    return arg;
  };
}

/**
 * This function accepts and discards inputs; it has no side effects. This is
 * primarily useful idiomatically for overridable function endpoints which
 * always need to be callable, since JS lacks a null-call idiom ala Cocoa.
 */
var emptyFunction = function emptyFunction() {};

emptyFunction.thatReturns = makeEmptyFunction;
emptyFunction.thatReturnsFalse = makeEmptyFunction(false);
emptyFunction.thatReturnsTrue = makeEmptyFunction(true);
emptyFunction.thatReturnsNull = makeEmptyFunction(null);
emptyFunction.thatReturnsThis = function () {
  return this;
};
emptyFunction.thatReturnsArgument = function (arg) {
  return arg;
};

module.exports = emptyFunction;

/***/ }),
/* 8 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright (c) 2013-present, Facebook, Inc.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 *
 */



/**
 * Use invariant() to assert state which your program assumes to be true.
 *
 * Provide sprintf-style format (only %s is supported) and arguments
 * to provide information about what broke and what you were
 * expecting.
 *
 * The invariant message will be stripped in production, but the invariant
 * will remain to ensure logic does not differ in production.
 */

var validateFormat = function validateFormat(format) {};

if (false) {
  validateFormat = function validateFormat(format) {
    if (format === undefined) {
      throw new Error('invariant requires an error message argument');
    }
  };
}

function invariant(condition, format, a, b, c, d, e, f) {
  validateFormat(format);

  if (!condition) {
    var error;
    if (format === undefined) {
      error = new Error('Minified exception occurred; use the non-minified dev environment ' + 'for the full error message and additional helpful warnings.');
    } else {
      var args = [a, b, c, d, e, f];
      var argIndex = 0;
      error = new Error(format.replace(/%s/g, function () {
        return args[argIndex++];
      }));
      error.name = 'Invariant Violation';
    }

    error.framesToPop = 1; // we don't care about invariant's own frame
    throw error;
  }
}

module.exports = invariant;

/***/ }),
/* 9 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
/**
 * Copyright 2013-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */



var ReactPropTypesSecret = 'SECRET_DO_NOT_PASS_THIS_OR_YOU_WILL_BE_FIRED';

module.exports = ReactPropTypesSecret;


/***/ }),
/* 10 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(2);

var _react2 = _interopRequireDefault(_react);

var _propTypes = __webpack_require__(3);

var _propTypes2 = _interopRequireDefault(_propTypes);

var _comparison = __webpack_require__(0);

var _const = __webpack_require__(1);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/require-default-props: 0 */
/* eslint no-return-assign: 0 */
/* eslint react/no-unused-prop-types: 0 */
/* eslint class-methods-use-this: 0 */


function optionsEquals(currOpts, prevOpts) {
  if (Array.isArray(currOpts)) {
    if (currOpts.length === prevOpts.length) {
      for (var i = 0; i < currOpts.length; i += 1) {
        if (currOpts[i].value !== prevOpts[i].value || currOpts[i].label !== prevOpts[i].label) {
          return false;
        }
      }
      return true;
    }
    return false;
  }
  var keys = Object.keys(currOpts);
  for (var _i = 0; _i < keys.length; _i += 1) {
    if (currOpts[keys[_i]] !== prevOpts[keys[_i]]) {
      return false;
    }
  }
  return Object.keys(currOpts).length === Object.keys(prevOpts).length;
}

function getOptionValue(options, key) {
  if (Array.isArray(options)) {
    var result = options.filter(function (_ref) {
      var label = _ref.label;
      return label === key;
    }).map(function (_ref2) {
      var value = _ref2.value;
      return value;
    });
    return result[0];
  }
  return options[key];
}

var SelectFilter = function (_Component) {
  _inherits(SelectFilter, _Component);

  function SelectFilter(props) {
    _classCallCheck(this, SelectFilter);

    var _this = _possibleConstructorReturn(this, (SelectFilter.__proto__ || Object.getPrototypeOf(SelectFilter)).call(this, props));

    _this.filter = _this.filter.bind(_this);
    _this.options = _this.getOptions(props);
    var isSelected = getOptionValue(_this.options, _this.getDefaultValue()) !== undefined;
    _this.state = { isSelected: isSelected };
    return _this;
  }

  _createClass(SelectFilter, [{
    key: 'componentDidMount',
    value: function componentDidMount() {
      var _this2 = this;

      var _props = this.props,
          column = _props.column,
          onFilter = _props.onFilter,
          getFilter = _props.getFilter;


      var value = this.selectInput.value;
      if (value && value !== '') {
        onFilter(column, _const.FILTER_TYPE.SELECT, true)(value);
      }

      // export onFilter function to allow users to access
      if (getFilter) {
        getFilter(function (filterVal) {
          _this2.setState(function () {
            return { isSelected: filterVal !== '' };
          });
          _this2.selectInput.value = filterVal;

          onFilter(column, _const.FILTER_TYPE.SELECT)(filterVal);
        });
      }
    }
  }, {
    key: 'componentDidUpdate',
    value: function componentDidUpdate(prevProps) {
      var needFilter = false;
      var _props2 = this.props,
          column = _props2.column,
          onFilter = _props2.onFilter,
          defaultValue = _props2.defaultValue;

      var nextOptions = this.getOptions(this.props);
      if (defaultValue !== prevProps.defaultValue) {
        needFilter = true;
      } else if (!optionsEquals(nextOptions, this.options)) {
        this.options = nextOptions;
        needFilter = true;
      }
      if (needFilter) {
        var value = this.selectInput.value;
        if (value) {
          onFilter(column, _const.FILTER_TYPE.SELECT)(value);
        }
      }
    }
  }, {
    key: 'getOptions',
    value: function getOptions(props) {
      return typeof props.options === 'function' ? props.options(props.column) : props.options;
    }
  }, {
    key: 'getDefaultValue',
    value: function getDefaultValue() {
      var _props3 = this.props,
          filterState = _props3.filterState,
          defaultValue = _props3.defaultValue;

      if (filterState && typeof filterState.filterVal !== 'undefined') {
        return filterState.filterVal;
      }
      return defaultValue;
    }
  }, {
    key: 'cleanFiltered',
    value: function cleanFiltered() {
      var value = this.props.defaultValue !== undefined ? this.props.defaultValue : '';
      this.setState(function () {
        return { isSelected: value !== '' };
      });
      this.selectInput.value = value;
      this.props.onFilter(this.props.column, _const.FILTER_TYPE.SELECT)(value);
    }
  }, {
    key: 'applyFilter',
    value: function applyFilter(value) {
      this.selectInput.value = value;
      this.setState(function () {
        return { isSelected: value !== '' };
      });
      this.props.onFilter(this.props.column, _const.FILTER_TYPE.SELECT)(value);
    }
  }, {
    key: 'filter',
    value: function filter(e) {
      var value = e.target.value;

      this.setState(function () {
        return { isSelected: value !== '' };
      });
      this.props.onFilter(this.props.column, _const.FILTER_TYPE.SELECT)(value);
    }
  }, {
    key: 'renderOptions',
    value: function renderOptions() {
      var optionTags = [];
      var options = this.options;
      var _props4 = this.props,
          placeholder = _props4.placeholder,
          column = _props4.column,
          withoutEmptyOption = _props4.withoutEmptyOption;

      if (!withoutEmptyOption) {
        optionTags.push(_react2.default.createElement(
          'option',
          { key: '-1', value: '' },
          placeholder || 'Select ' + column.text + '...'
        ));
      }
      if (Array.isArray(options)) {
        options.forEach(function (_ref3) {
          var value = _ref3.value,
              label = _ref3.label;
          return optionTags.push(_react2.default.createElement(
            'option',
            { key: value, value: value },
            label
          ));
        });
      } else {
        Object.keys(options).forEach(function (key) {
          return optionTags.push(_react2.default.createElement(
            'option',
            { key: key, value: key },
            options[key]
          ));
        });
      }
      return optionTags;
    }
  }, {
    key: 'render',
    value: function render() {
      var _this3 = this;

      var _props5 = this.props,
          id = _props5.id,
          style = _props5.style,
          className = _props5.className,
          defaultValue = _props5.defaultValue,
          onFilter = _props5.onFilter,
          column = _props5.column,
          options = _props5.options,
          comparator = _props5.comparator,
          withoutEmptyOption = _props5.withoutEmptyOption,
          caseSensitive = _props5.caseSensitive,
          getFilter = _props5.getFilter,
          filterState = _props5.filterState,
          rest = _objectWithoutProperties(_props5, ['id', 'style', 'className', 'defaultValue', 'onFilter', 'column', 'options', 'comparator', 'withoutEmptyOption', 'caseSensitive', 'getFilter', 'filterState']);

      var selectClass = 'filter select-filter form-control ' + className + ' ' + (this.state.isSelected ? '' : 'placeholder-selected');
      var elmId = 'select-filter-column-' + column.dataField + (id ? '-' + id : '');

      return _react2.default.createElement(
        'label',
        {
          className: 'filter-label',
          htmlFor: elmId
        },
        _react2.default.createElement(
          'span',
          { className: 'sr-only' },
          'Filter by ',
          column.text
        ),
        _react2.default.createElement(
          'select',
          _extends({}, rest, {
            ref: function ref(n) {
              return _this3.selectInput = n;
            },
            id: elmId,
            style: style,
            className: selectClass,
            onChange: this.filter,
            onClick: function onClick(e) {
              return e.stopPropagation();
            },
            defaultValue: this.getDefaultValue() || ''
          }),
          this.renderOptions()
        )
      );
    }
  }]);

  return SelectFilter;
}(_react.Component);

SelectFilter.propTypes = {
  onFilter: _propTypes2.default.func.isRequired,
  column: _propTypes2.default.object.isRequired,
  id: _propTypes2.default.string,
  filterState: _propTypes2.default.object,
  options: _propTypes2.default.oneOfType([_propTypes2.default.object, _propTypes2.default.array]).isRequired,
  comparator: _propTypes2.default.oneOf([_comparison.LIKE, _comparison.EQ]),
  placeholder: _propTypes2.default.string,
  style: _propTypes2.default.object,
  className: _propTypes2.default.string,
  withoutEmptyOption: _propTypes2.default.bool,
  defaultValue: _propTypes2.default.any,
  caseSensitive: _propTypes2.default.bool,
  getFilter: _propTypes2.default.func
};

SelectFilter.defaultProps = {
  defaultValue: '',
  filterState: {},
  className: '',
  withoutEmptyOption: false,
  comparator: _comparison.EQ,
  caseSensitive: true,
  id: null
};

exports.default = SelectFilter;

/***/ }),
/* 11 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(2);

var _react2 = _interopRequireDefault(_react);

var _propTypes = __webpack_require__(3);

var _propTypes2 = _interopRequireDefault(_propTypes);

var _comparison = __webpack_require__(0);

var _const = __webpack_require__(1);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/require-default-props: 0 */
/* eslint no-return-assign: 0 */
/* eslint no-param-reassign: 0 */
/* eslint react/no-unused-prop-types: 0 */


function optionsEquals(currOpts, prevOpts) {
  var keys = Object.keys(currOpts);
  for (var i = 0; i < keys.length; i += 1) {
    if (currOpts[keys[i]] !== prevOpts[keys[i]]) {
      return false;
    }
  }
  return Object.keys(currOpts).length === Object.keys(prevOpts).length;
}

var getSelections = function getSelections(container) {
  if (container.selectedOptions) {
    return Array.from(container.selectedOptions).map(function (item) {
      return item.value;
    });
  }
  var selections = [];
  var totalLen = container.options.length;
  for (var i = 0; i < totalLen; i += 1) {
    var option = container.options.item(i);
    if (option.selected) selections.push(option.value);
  }
  return selections;
};

var MultiSelectFilter = function (_Component) {
  _inherits(MultiSelectFilter, _Component);

  function MultiSelectFilter(props) {
    _classCallCheck(this, MultiSelectFilter);

    var _this = _possibleConstructorReturn(this, (MultiSelectFilter.__proto__ || Object.getPrototypeOf(MultiSelectFilter)).call(this, props));

    _this.filter = _this.filter.bind(_this);
    _this.applyFilter = _this.applyFilter.bind(_this);
    var isSelected = props.defaultValue.map(function (item) {
      return props.options[item];
    }).length > 0;
    _this.state = { isSelected: isSelected };
    return _this;
  }

  _createClass(MultiSelectFilter, [{
    key: 'componentDidMount',
    value: function componentDidMount() {
      var _this2 = this;

      var getFilter = this.props.getFilter;


      var value = getSelections(this.selectInput);
      if (value && value.length > 0) {
        this.applyFilter(value);
      }

      // export onFilter function to allow users to access
      if (getFilter) {
        getFilter(function (filterVal) {
          _this2.selectInput.value = filterVal;
          _this2.applyFilter(filterVal);
        });
      }
    }
  }, {
    key: 'componentDidUpdate',
    value: function componentDidUpdate(prevProps) {
      var needFilter = false;
      if (this.props.defaultValue !== prevProps.defaultValue) {
        needFilter = true;
      } else if (!optionsEquals(this.props.options, prevProps.options)) {
        needFilter = true;
      }
      if (needFilter) {
        this.applyFilter(getSelections(this.selectInput));
      }
    }
  }, {
    key: 'getDefaultValue',
    value: function getDefaultValue() {
      var _props = this.props,
          filterState = _props.filterState,
          defaultValue = _props.defaultValue;

      if (filterState && typeof filterState.filterVal !== 'undefined') {
        return filterState.filterVal;
      }
      return defaultValue;
    }
  }, {
    key: 'getOptions',
    value: function getOptions() {
      var optionTags = [];
      var _props2 = this.props,
          options = _props2.options,
          placeholder = _props2.placeholder,
          column = _props2.column,
          withoutEmptyOption = _props2.withoutEmptyOption;

      if (!withoutEmptyOption) {
        optionTags.push(_react2.default.createElement(
          'option',
          { key: '-1', value: '' },
          placeholder || 'Select ' + column.text + '...'
        ));
      }
      Object.keys(options).forEach(function (key) {
        return optionTags.push(_react2.default.createElement(
          'option',
          { key: key, value: key },
          options[key]
        ));
      });
      return optionTags;
    }
  }, {
    key: 'cleanFiltered',
    value: function cleanFiltered() {
      var value = this.props.defaultValue !== undefined ? this.props.defaultValue : [];
      this.selectInput.value = value;
      this.applyFilter(value);
    }
  }, {
    key: 'applyFilter',
    value: function applyFilter(value) {
      if (value.length === 1 && value[0] === '') {
        value = [];
      }
      this.setState(function () {
        return { isSelected: value.length > 0 };
      });
      this.props.onFilter(this.props.column, _const.FILTER_TYPE.MULTISELECT)(value);
    }
  }, {
    key: 'filter',
    value: function filter(e) {
      var value = getSelections(e.target);
      this.applyFilter(value);
    }
  }, {
    key: 'render',
    value: function render() {
      var _this3 = this;

      var _props3 = this.props,
          id = _props3.id,
          style = _props3.style,
          className = _props3.className,
          filterState = _props3.filterState,
          defaultValue = _props3.defaultValue,
          onFilter = _props3.onFilter,
          column = _props3.column,
          options = _props3.options,
          comparator = _props3.comparator,
          withoutEmptyOption = _props3.withoutEmptyOption,
          caseSensitive = _props3.caseSensitive,
          getFilter = _props3.getFilter,
          rest = _objectWithoutProperties(_props3, ['id', 'style', 'className', 'filterState', 'defaultValue', 'onFilter', 'column', 'options', 'comparator', 'withoutEmptyOption', 'caseSensitive', 'getFilter']);

      var selectClass = 'filter select-filter form-control ' + className + ' ' + (this.state.isSelected ? '' : 'placeholder-selected');
      var elmId = 'multiselect-filter-column-' + column.dataField + (id ? '-' + id : '');

      return _react2.default.createElement(
        'label',
        {
          className: 'filter-label',
          htmlFor: elmId
        },
        _react2.default.createElement(
          'span',
          { className: 'sr-only' },
          'Filter by ',
          column.text
        ),
        _react2.default.createElement(
          'select',
          _extends({}, rest, {
            ref: function ref(n) {
              return _this3.selectInput = n;
            },
            id: elmId,
            style: style,
            multiple: true,
            className: selectClass,
            onChange: this.filter,
            onClick: function onClick(e) {
              return e.stopPropagation();
            },
            defaultValue: this.getDefaultValue()
          }),
          this.getOptions()
        )
      );
    }
  }]);

  return MultiSelectFilter;
}(_react.Component);

MultiSelectFilter.propTypes = {
  onFilter: _propTypes2.default.func.isRequired,
  column: _propTypes2.default.object.isRequired,
  options: _propTypes2.default.object.isRequired,
  id: _propTypes2.default.string,
  filterState: _propTypes2.default.object,
  comparator: _propTypes2.default.oneOf([_comparison.LIKE, _comparison.EQ]),
  placeholder: _propTypes2.default.string,
  style: _propTypes2.default.object,
  className: _propTypes2.default.string,
  withoutEmptyOption: _propTypes2.default.bool,
  defaultValue: _propTypes2.default.array,
  caseSensitive: _propTypes2.default.bool,
  getFilter: _propTypes2.default.func
};

MultiSelectFilter.defaultProps = {
  defaultValue: [],
  filterState: {},
  className: '',
  withoutEmptyOption: false,
  comparator: _comparison.EQ,
  caseSensitive: true,
  id: null
};

exports.default = MultiSelectFilter;

/***/ }),
/* 12 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(2);

var _react2 = _interopRequireDefault(_react);

var _propTypes = __webpack_require__(3);

var _propTypes2 = _interopRequireDefault(_propTypes);

var _comparison = __webpack_require__(0);

var Comparator = _interopRequireWildcard(_comparison);

var _const = __webpack_require__(1);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint jsx-a11y/no-static-element-interactions: 0 */
/* eslint react/require-default-props: 0 */
/* eslint no-return-assign: 0 */

var legalComparators = [Comparator.EQ, Comparator.NE, Comparator.GT, Comparator.GE, Comparator.LT, Comparator.LE];

var NumberFilter = function (_Component) {
  _inherits(NumberFilter, _Component);

  function NumberFilter(props) {
    _classCallCheck(this, NumberFilter);

    var _this = _possibleConstructorReturn(this, (NumberFilter.__proto__ || Object.getPrototypeOf(NumberFilter)).call(this, props));

    _this.comparators = props.comparators || legalComparators;
    _this.timeout = null;
    var isSelected = props.defaultValue !== undefined && props.defaultValue.number !== undefined;
    if (props.options && isSelected) {
      isSelected = props.options.indexOf(props.defaultValue.number) > -1;
    }
    _this.state = { isSelected: isSelected };
    _this.onChangeNumber = _this.onChangeNumber.bind(_this);
    _this.onChangeNumberSet = _this.onChangeNumberSet.bind(_this);
    _this.onChangeComparator = _this.onChangeComparator.bind(_this);
    return _this;
  }

  _createClass(NumberFilter, [{
    key: 'componentDidMount',
    value: function componentDidMount() {
      var _this2 = this;

      var _props = this.props,
          column = _props.column,
          onFilter = _props.onFilter,
          getFilter = _props.getFilter;

      var comparator = this.numberFilterComparator.value;
      var number = this.numberFilter.value;
      if (comparator && number) {
        onFilter(column, _const.FILTER_TYPE.NUMBER, true)({ number: number, comparator: comparator });
      }

      // export onFilter function to allow users to access
      if (getFilter) {
        getFilter(function (filterVal) {
          _this2.setState(function () {
            return { isSelected: filterVal !== '' };
          });
          _this2.numberFilterComparator.value = filterVal.comparator;
          _this2.numberFilter.value = filterVal.number;

          onFilter(column, _const.FILTER_TYPE.NUMBER)({
            number: filterVal.number,
            comparator: filterVal.comparator
          });
        });
      }
    }
  }, {
    key: 'componentWillUnmount',
    value: function componentWillUnmount() {
      clearTimeout(this.timeout);
    }
  }, {
    key: 'onChangeNumber',
    value: function onChangeNumber(e) {
      var _props2 = this.props,
          delay = _props2.delay,
          column = _props2.column,
          onFilter = _props2.onFilter;

      var comparator = this.numberFilterComparator.value;
      if (comparator === '') {
        return;
      }
      if (this.timeout) {
        clearTimeout(this.timeout);
      }
      var filterValue = e.target.value;
      this.timeout = setTimeout(function () {
        onFilter(column, _const.FILTER_TYPE.NUMBER)({ number: filterValue, comparator: comparator });
      }, delay);
    }
  }, {
    key: 'onChangeNumberSet',
    value: function onChangeNumberSet(e) {
      var _props3 = this.props,
          column = _props3.column,
          onFilter = _props3.onFilter;

      var comparator = this.numberFilterComparator.value;
      var value = e.target.value;

      this.setState(function () {
        return { isSelected: value !== '' };
      });
      // if (comparator === '') {
      //   return;
      // }
      onFilter(column, _const.FILTER_TYPE.NUMBER)({ number: value, comparator: comparator });
    }
  }, {
    key: 'onChangeComparator',
    value: function onChangeComparator(e) {
      var _props4 = this.props,
          column = _props4.column,
          onFilter = _props4.onFilter;

      var value = this.numberFilter.value;
      var comparator = e.target.value;
      // if (value === '') {
      //   return;
      // }
      onFilter(column, _const.FILTER_TYPE.NUMBER)({ number: value, comparator: comparator });
    }
  }, {
    key: 'getDefaultComparator',
    value: function getDefaultComparator() {
      var _props5 = this.props,
          defaultValue = _props5.defaultValue,
          filterState = _props5.filterState;

      if (filterState && filterState.filterVal) {
        return filterState.filterVal.comparator;
      }
      if (defaultValue && defaultValue.comparator) {
        return defaultValue.comparator;
      }
      return '';
    }
  }, {
    key: 'getDefaultValue',
    value: function getDefaultValue() {
      var _props6 = this.props,
          defaultValue = _props6.defaultValue,
          filterState = _props6.filterState;

      if (filterState && filterState.filterVal) {
        return filterState.filterVal.number;
      }
      if (defaultValue && defaultValue.number) {
        return defaultValue.number;
      }
      return '';
    }
  }, {
    key: 'getComparatorOptions',
    value: function getComparatorOptions() {
      var optionTags = [];
      var withoutEmptyComparatorOption = this.props.withoutEmptyComparatorOption;

      if (!withoutEmptyComparatorOption) {
        optionTags.push(_react2.default.createElement('option', { key: '-1' }));
      }
      for (var i = 0; i < this.comparators.length; i += 1) {
        optionTags.push(_react2.default.createElement(
          'option',
          { key: i, value: this.comparators[i] },
          this.comparators[i]
        ));
      }
      return optionTags;
    }
  }, {
    key: 'getNumberOptions',
    value: function getNumberOptions() {
      var optionTags = [];
      var _props7 = this.props,
          options = _props7.options,
          column = _props7.column,
          withoutEmptyNumberOption = _props7.withoutEmptyNumberOption;

      if (!withoutEmptyNumberOption) {
        optionTags.push(_react2.default.createElement(
          'option',
          { key: '-1', value: '' },
          this.props.placeholder || 'Select ' + column.text + '...'
        ));
      }
      for (var i = 0; i < options.length; i += 1) {
        optionTags.push(_react2.default.createElement(
          'option',
          { key: i, value: options[i] },
          options[i]
        ));
      }
      return optionTags;
    }
  }, {
    key: 'applyFilter',
    value: function applyFilter(filterObj) {
      var _props8 = this.props,
          column = _props8.column,
          onFilter = _props8.onFilter;
      var number = filterObj.number,
          comparator = filterObj.comparator;

      this.setState(function () {
        return { isSelected: number !== '' };
      });
      this.numberFilterComparator.value = comparator;
      this.numberFilter.value = number;
      onFilter(column, _const.FILTER_TYPE.NUMBER)({ number: number, comparator: comparator });
    }
  }, {
    key: 'cleanFiltered',
    value: function cleanFiltered() {
      var _props9 = this.props,
          column = _props9.column,
          onFilter = _props9.onFilter,
          defaultValue = _props9.defaultValue;

      var value = defaultValue ? defaultValue.number : '';
      var comparator = defaultValue ? defaultValue.comparator : '';
      this.setState(function () {
        return { isSelected: value !== '' };
      });
      this.numberFilterComparator.value = comparator;
      this.numberFilter.value = value;
      onFilter(column, _const.FILTER_TYPE.NUMBER)({ number: value, comparator: comparator });
    }
  }, {
    key: 'render',
    value: function render() {
      var _this3 = this;

      var isSelected = this.state.isSelected;
      var _props10 = this.props,
          id = _props10.id,
          column = _props10.column,
          options = _props10.options,
          style = _props10.style,
          className = _props10.className,
          numberStyle = _props10.numberStyle,
          numberClassName = _props10.numberClassName,
          comparatorStyle = _props10.comparatorStyle,
          comparatorClassName = _props10.comparatorClassName,
          placeholder = _props10.placeholder;

      var selectClass = '\n      select-filter \n      number-filter-input \n      form-control \n      ' + numberClassName + ' \n      ' + (!isSelected ? 'placeholder-selected' : '') + '\n    ';

      var comparatorElmId = 'number-filter-comparator-' + column.dataField + (id ? '-' + id : '');
      var inputElmId = 'number-filter-column-' + column.dataField + (id ? '-' + id : '');

      return _react2.default.createElement(
        'div',
        {
          onClick: function onClick(e) {
            return e.stopPropagation();
          },
          className: 'filter number-filter ' + className,
          style: style
        },
        _react2.default.createElement(
          'label',
          {
            className: 'filter-label',
            htmlFor: comparatorElmId
          },
          _react2.default.createElement(
            'span',
            { className: 'sr-only' },
            'Filter comparator'
          ),
          _react2.default.createElement(
            'select',
            {
              ref: function ref(n) {
                return _this3.numberFilterComparator = n;
              },
              style: comparatorStyle,
              id: comparatorElmId,
              className: 'number-filter-comparator form-control ' + comparatorClassName,
              onChange: this.onChangeComparator,
              defaultValue: this.getDefaultComparator()
            },
            this.getComparatorOptions()
          )
        ),
        options ? _react2.default.createElement(
          'label',
          {
            className: 'filter-label',
            htmlFor: inputElmId
          },
          _react2.default.createElement(
            'span',
            { className: 'sr-only' },
            'Select ' + column.text
          ),
          _react2.default.createElement(
            'select',
            {
              ref: function ref(n) {
                return _this3.numberFilter = n;
              },
              id: inputElmId,
              style: numberStyle,
              className: selectClass,
              onChange: this.onChangeNumberSet,
              defaultValue: this.getDefaultValue()
            },
            this.getNumberOptions()
          )
        ) : _react2.default.createElement(
          'label',
          { htmlFor: inputElmId },
          _react2.default.createElement(
            'span',
            { className: 'sr-only' },
            'Enter ' + column.text
          ),
          _react2.default.createElement('input', {
            ref: function ref(n) {
              return _this3.numberFilter = n;
            },
            id: inputElmId,
            type: 'number',
            style: numberStyle,
            className: 'number-filter-input form-control ' + numberClassName,
            placeholder: placeholder || 'Enter ' + column.text + '...',
            onChange: this.onChangeNumber,
            defaultValue: this.getDefaultValue()
          })
        )
      );
    }
  }]);

  return NumberFilter;
}(_react.Component);

NumberFilter.propTypes = {
  onFilter: _propTypes2.default.func.isRequired,
  column: _propTypes2.default.object.isRequired,
  id: _propTypes2.default.string,
  filterState: _propTypes2.default.object,
  options: _propTypes2.default.arrayOf(_propTypes2.default.number),
  defaultValue: _propTypes2.default.shape({
    number: _propTypes2.default.oneOfType([_propTypes2.default.string, _propTypes2.default.number]),
    comparator: _propTypes2.default.oneOf([].concat(legalComparators, ['']))
  }),
  delay: _propTypes2.default.number,
  /* eslint consistent-return: 0 */
  comparators: function comparators(props, propName) {
    if (!props[propName]) {
      return;
    }
    for (var i = 0; i < props[propName].length; i += 1) {
      var comparatorIsValid = false;
      for (var j = 0; j < legalComparators.length; j += 1) {
        if (legalComparators[j] === props[propName][i] || props[propName][i] === '') {
          comparatorIsValid = true;
          break;
        }
      }
      if (!comparatorIsValid) {
        return new Error('Number comparator provided is not supported.\n          Use only ' + legalComparators);
      }
    }
  },
  placeholder: _propTypes2.default.string,
  withoutEmptyComparatorOption: _propTypes2.default.bool,
  withoutEmptyNumberOption: _propTypes2.default.bool,
  style: _propTypes2.default.object,
  className: _propTypes2.default.string,
  comparatorStyle: _propTypes2.default.object,
  comparatorClassName: _propTypes2.default.string,
  numberStyle: _propTypes2.default.object,
  numberClassName: _propTypes2.default.string,
  getFilter: _propTypes2.default.func
};

NumberFilter.defaultProps = {
  delay: _const.FILTER_DELAY,
  options: undefined,
  defaultValue: {
    number: undefined,
    comparator: ''
  },
  filterState: {},
  withoutEmptyComparatorOption: false,
  withoutEmptyNumberOption: false,
  comparators: legalComparators,
  placeholder: undefined,
  style: undefined,
  className: '',
  comparatorStyle: undefined,
  comparatorClassName: '',
  numberStyle: undefined,
  numberClassName: '',
  id: null
};

exports.default = NumberFilter;

/***/ }),
/* 13 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(2);

var _react2 = _interopRequireDefault(_react);

var _propTypes = __webpack_require__(3);

var _comparison = __webpack_require__(0);

var Comparator = _interopRequireWildcard(_comparison);

var _const = __webpack_require__(1);

function _interopRequireWildcard(obj) { if (obj && obj.__esModule) { return obj; } else { var newObj = {}; if (obj != null) { for (var key in obj) { if (Object.prototype.hasOwnProperty.call(obj, key)) newObj[key] = obj[key]; } } newObj.default = obj; return newObj; } }

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/require-default-props: 0 */
/* eslint jsx-a11y/no-static-element-interactions: 0 */
/* eslint no-return-assign: 0 */
/* eslint prefer-template: 0 */


var legalComparators = [Comparator.EQ, Comparator.NE, Comparator.GT, Comparator.GE, Comparator.LT, Comparator.LE];

function dateParser(d) {
  return d.getUTCFullYear() + '-' + ('0' + (d.getUTCMonth() + 1)).slice(-2) + '-' + ('0' + d.getUTCDate()).slice(-2);
}

var DateFilter = function (_Component) {
  _inherits(DateFilter, _Component);

  function DateFilter(props) {
    _classCallCheck(this, DateFilter);

    var _this = _possibleConstructorReturn(this, (DateFilter.__proto__ || Object.getPrototypeOf(DateFilter)).call(this, props));

    _this.timeout = null;
    _this.comparators = props.comparators || legalComparators;
    _this.applyFilter = _this.applyFilter.bind(_this);
    _this.onChangeDate = _this.onChangeDate.bind(_this);
    _this.onChangeComparator = _this.onChangeComparator.bind(_this);
    return _this;
  }

  _createClass(DateFilter, [{
    key: 'componentDidMount',
    value: function componentDidMount() {
      var _this2 = this;

      var getFilter = this.props.getFilter;

      var comparator = this.dateFilterComparator.value;
      var date = this.inputDate.value;
      if (comparator && date) {
        this.applyFilter(date, comparator, true);
      }

      // export onFilter function to allow users to access
      if (getFilter) {
        getFilter(function (filterVal) {
          var nullableFilterVal = filterVal || { date: null, comparator: null };
          _this2.dateFilterComparator.value = nullableFilterVal.comparator;
          _this2.inputDate.value = nullableFilterVal.date ? dateParser(nullableFilterVal.date) : null;

          _this2.applyFilter(nullableFilterVal.date, nullableFilterVal.comparator);
        });
      }
    }
  }, {
    key: 'componentWillUnmount',
    value: function componentWillUnmount() {
      if (this.timeout) clearTimeout(this.timeout);
    }
  }, {
    key: 'onChangeDate',
    value: function onChangeDate(e) {
      var comparator = this.dateFilterComparator.value;
      var filterValue = e.target.value;
      this.applyFilter(filterValue, comparator);
    }
  }, {
    key: 'onChangeComparator',
    value: function onChangeComparator(e) {
      var value = this.inputDate.value;
      var comparator = e.target.value;
      this.applyFilter(value, comparator);
    }
  }, {
    key: 'getComparatorOptions',
    value: function getComparatorOptions() {
      var optionTags = [];
      var withoutEmptyComparatorOption = this.props.withoutEmptyComparatorOption;

      if (!withoutEmptyComparatorOption) {
        optionTags.push(_react2.default.createElement('option', { key: '-1' }));
      }
      for (var i = 0; i < this.comparators.length; i += 1) {
        optionTags.push(_react2.default.createElement(
          'option',
          { key: i, value: this.comparators[i] },
          this.comparators[i]
        ));
      }
      return optionTags;
    }
  }, {
    key: 'getDefaultComparator',
    value: function getDefaultComparator() {
      var _props = this.props,
          defaultValue = _props.defaultValue,
          filterState = _props.filterState;

      if (filterState && filterState.filterVal) {
        return filterState.filterVal.comparator;
      }
      if (defaultValue && defaultValue.comparator) {
        return defaultValue.comparator;
      }
      return '';
    }
  }, {
    key: 'getDefaultDate',
    value: function getDefaultDate() {
      // Set the appropriate format for the input type=date, i.e. "YYYY-MM-DD"
      var _props2 = this.props,
          defaultValue = _props2.defaultValue,
          filterState = _props2.filterState;

      if (filterState && filterState.filterVal && filterState.filterVal.date) {
        return dateParser(filterState.filterVal.date);
      }
      if (defaultValue && defaultValue.date) {
        return dateParser(new Date(defaultValue.date));
      }
      return '';
    }
  }, {
    key: 'applyFilter',
    value: function applyFilter(value, comparator, isInitial) {
      // if (!comparator || !value) {
      //  return;
      // }
      var _props3 = this.props,
          column = _props3.column,
          onFilter = _props3.onFilter,
          delay = _props3.delay;

      var execute = function execute() {
        // Incoming value should always be a string, and the defaultDate
        // above is implemented as an empty string, so we can just check for that.
        // instead of parsing an invalid Date. The filter function will interpret
        // null as an empty date field
        var date = value === '' ? null : new Date(value);
        onFilter(column, _const.FILTER_TYPE.DATE, isInitial)({ date: date, comparator: comparator });
      };
      if (delay) {
        this.timeout = setTimeout(function () {
          execute();
        }, delay);
      } else {
        execute();
      }
    }
  }, {
    key: 'render',
    value: function render() {
      var _this3 = this;

      var _props4 = this.props,
          id = _props4.id,
          placeholder = _props4.placeholder,
          _props4$column = _props4.column,
          dataField = _props4$column.dataField,
          text = _props4$column.text,
          style = _props4.style,
          comparatorStyle = _props4.comparatorStyle,
          dateStyle = _props4.dateStyle,
          className = _props4.className,
          comparatorClassName = _props4.comparatorClassName,
          dateClassName = _props4.dateClassName;


      var comparatorElmId = 'date-filter-comparator-' + dataField + (id ? '-' + id : '');
      var inputElmId = 'date-filter-column-' + dataField + (id ? '-' + id : '');

      return _react2.default.createElement(
        'div',
        {
          onClick: function onClick(e) {
            return e.stopPropagation();
          },
          className: 'filter date-filter ' + className,
          style: style
        },
        _react2.default.createElement(
          'label',
          {
            className: 'filter-label',
            htmlFor: comparatorElmId
          },
          _react2.default.createElement(
            'span',
            { className: 'sr-only' },
            'Filter comparator'
          ),
          _react2.default.createElement(
            'select',
            {
              ref: function ref(n) {
                return _this3.dateFilterComparator = n;
              },
              id: comparatorElmId,
              style: comparatorStyle,
              className: 'date-filter-comparator form-control ' + comparatorClassName,
              onChange: this.onChangeComparator,
              defaultValue: this.getDefaultComparator()
            },
            this.getComparatorOptions()
          )
        ),
        _react2.default.createElement(
          'label',
          { htmlFor: inputElmId },
          _react2.default.createElement(
            'span',
            { className: 'sr-only' },
            'Enter $',
            text
          ),
          _react2.default.createElement('input', {
            ref: function ref(n) {
              return _this3.inputDate = n;
            },
            id: inputElmId,
            className: 'filter date-filter-input form-control ' + dateClassName,
            style: dateStyle,
            type: 'date',
            onChange: this.onChangeDate,
            placeholder: placeholder || 'Enter ' + text + '...',
            defaultValue: this.getDefaultDate()
          })
        )
      );
    }
  }]);

  return DateFilter;
}(_react.Component);

DateFilter.propTypes = {
  onFilter: _propTypes.PropTypes.func.isRequired,
  column: _propTypes.PropTypes.object.isRequired,
  id: _propTypes.PropTypes.string,
  filterState: _propTypes.PropTypes.object,
  delay: _propTypes.PropTypes.number,
  defaultValue: _propTypes.PropTypes.shape({
    date: _propTypes.PropTypes.oneOfType([_propTypes.PropTypes.object]),
    comparator: _propTypes.PropTypes.oneOf([].concat(legalComparators, ['']))
  }),
  /* eslint consistent-return: 0 */
  comparators: function comparators(props, propName) {
    if (!props[propName]) {
      return;
    }
    for (var i = 0; i < props[propName].length; i += 1) {
      var comparatorIsValid = false;
      for (var j = 0; j < legalComparators.length; j += 1) {
        if (legalComparators[j] === props[propName][i] || props[propName][i] === '') {
          comparatorIsValid = true;
          break;
        }
      }
      if (!comparatorIsValid) {
        return new Error('Date comparator provided is not supported.\n          Use only ' + legalComparators);
      }
    }
  },
  placeholder: _propTypes.PropTypes.string,
  withoutEmptyComparatorOption: _propTypes.PropTypes.bool,
  style: _propTypes.PropTypes.object,
  comparatorStyle: _propTypes.PropTypes.object,
  dateStyle: _propTypes.PropTypes.object,
  className: _propTypes.PropTypes.string,
  comparatorClassName: _propTypes.PropTypes.string,
  dateClassName: _propTypes.PropTypes.string,
  getFilter: _propTypes.PropTypes.func
};

DateFilter.defaultProps = {
  delay: 0,
  defaultValue: {
    date: undefined,
    comparator: ''
  },
  filterState: {},
  withoutEmptyComparatorOption: false,
  comparators: legalComparators,
  placeholder: undefined,
  style: undefined,
  className: '',
  comparatorStyle: undefined,
  comparatorClassName: '',
  dateStyle: undefined,
  dateClassName: '',
  id: null
};

exports.default = DateFilter;

/***/ }),
/* 14 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(2);

var _react2 = _interopRequireDefault(_react);

var _propTypes = __webpack_require__(3);

var _propTypes2 = _interopRequireDefault(_propTypes);

var _filter = __webpack_require__(15);

var _comparison = __webpack_require__(0);

var _const = __webpack_require__(1);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/prop-types: 0 */
/* eslint react/require-default-props: 0 */
/* eslint camelcase: 0 */


exports.default = function (_, isRemoteFiltering, handleFilterChange) {
  var FilterContext = _react2.default.createContext();

  var FilterProvider = function (_React$Component) {
    _inherits(FilterProvider, _React$Component);

    function FilterProvider(props) {
      _classCallCheck(this, FilterProvider);

      var _this = _possibleConstructorReturn(this, (FilterProvider.__proto__ || Object.getPrototypeOf(FilterProvider)).call(this, props));

      _this.currFilters = {};
      _this.clearFilters = {};
      _this.onFilter = _this.onFilter.bind(_this);
      _this.doFilter = _this.doFilter.bind(_this);
      _this.onExternalFilter = _this.onExternalFilter.bind(_this);
      _this.data = props.data;
      _this.isEmitDataChange = false;
      return _this;
    }

    _createClass(FilterProvider, [{
      key: 'componentDidMount',
      value: function componentDidMount() {
        if (isRemoteFiltering() && Object.keys(this.currFilters).length > 0) {
          handleFilterChange(this.currFilters);
        }
      }
    }, {
      key: 'onFilter',
      value: function onFilter(column, filterType) {
        var _this2 = this;

        var initialize = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : false;

        return function (filterVal) {
          // watch out here if migration to context API, #334
          var currFilters = Object.assign({}, _this2.currFilters);
          _this2.clearFilters = {};
          var dataField = column.dataField,
              filter = column.filter;


          var needClearFilters = !_.isDefined(filterVal) || filterVal === '' || filterVal.length === 0;

          if (needClearFilters) {
            delete currFilters[dataField];
            _this2.clearFilters = _defineProperty({}, dataField, { clear: true, filterVal: filterVal });
          } else {
            // select default comparator is EQ, others are LIKE
            var _filter$props = filter.props,
                _filter$props$compara = _filter$props.comparator,
                comparator = _filter$props$compara === undefined ? filterType === _const.FILTER_TYPE.SELECT ? _comparison.EQ : _comparison.LIKE : _filter$props$compara,
                _filter$props$caseSen = _filter$props.caseSensitive,
                caseSensitive = _filter$props$caseSen === undefined ? false : _filter$props$caseSen;

            currFilters[dataField] = { filterVal: filterVal, filterType: filterType, comparator: comparator, caseSensitive: caseSensitive };
          }

          _this2.currFilters = currFilters;

          if (isRemoteFiltering()) {
            if (!initialize) {
              handleFilterChange(_this2.currFilters);
            }
            return;
          }
          _this2.doFilter(_this2.props);
        };
      }
    }, {
      key: 'onExternalFilter',
      value: function onExternalFilter(column, filterType) {
        var _this3 = this;

        return function (value) {
          _this3.onFilter(column, filterType)(value);
        };
      }
    }, {
      key: 'getFiltered',
      value: function getFiltered() {
        return this.data;
      }
    }, {
      key: 'UNSAFE_componentWillReceiveProps',
      value: function UNSAFE_componentWillReceiveProps(nextProps) {
        // let nextData = nextProps.data;
        if (!isRemoteFiltering() && !_.isEqual(nextProps.data, this.data)) {
          this.doFilter(nextProps, this.isEmitDataChange);
        } else {
          this.data = nextProps.data;
        }
      }
    }, {
      key: 'doFilter',
      value: function doFilter(props) {
        var ignoreEmitDataChange = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : false;
        var dataChangeListener = props.dataChangeListener,
            data = props.data,
            columns = props.columns,
            filter = props.filter;

        var result = (0, _filter.filters)(data, columns, _)(this.currFilters, this.clearFilters);
        if (filter.afterFilter) {
          filter.afterFilter(result, this.currFilters);
        }
        this.data = result;
        if (dataChangeListener && !ignoreEmitDataChange) {
          this.isEmitDataChange = true;
          dataChangeListener.emit('filterChanged', result.length);
        } else {
          this.isEmitDataChange = false;
          this.forceUpdate();
        }
      }
    }, {
      key: 'render',
      value: function render() {
        return _react2.default.createElement(
          FilterContext.Provider,
          { value: {
              data: this.data,
              onFilter: this.onFilter,
              onExternalFilter: this.onExternalFilter,
              currFilters: this.currFilters
            }
          },
          this.props.children
        );
      }
    }]);

    return FilterProvider;
  }(_react2.default.Component);

  FilterProvider.propTypes = {
    data: _propTypes2.default.array.isRequired,
    columns: _propTypes2.default.array.isRequired,
    dataChangeListener: _propTypes2.default.object
  };


  return {
    Provider: FilterProvider,
    Consumer: FilterContext.Consumer
  };
};

/***/ }),
/* 15 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.filters = exports.filterFactory = exports.filterByArray = exports.filterByDate = exports.filterByNumber = exports.filterByText = undefined;

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; }; /* eslint eqeqeq: 0 */
/* eslint no-console: 0 */


var _const = __webpack_require__(1);

var _comparison = __webpack_require__(0);

var filterByText = exports.filterByText = function filterByText(_) {
  return function (data, dataField, _ref, customFilterValue) {
    var _ref$filterVal = _ref.filterVal,
        userInput = _ref$filterVal === undefined ? '' : _ref$filterVal,
        _ref$comparator = _ref.comparator,
        comparator = _ref$comparator === undefined ? _comparison.LIKE : _ref$comparator,
        caseSensitive = _ref.caseSensitive;

    // make sure filter value to be a string
    var filterVal = userInput.toString();

    return data.filter(function (row) {
      var cell = _.get(row, dataField);
      if (customFilterValue) {
        cell = customFilterValue(cell, row);
      }
      var cellStr = _.isDefined(cell) ? cell.toString() : '';
      if (comparator === _comparison.EQ) {
        return cellStr === filterVal;
      }
      if (caseSensitive) {
        return cellStr.includes(filterVal);
      }

      return cellStr.toLocaleUpperCase().indexOf(filterVal.toLocaleUpperCase()) !== -1;
    });
  };
};

var filterByNumber = exports.filterByNumber = function filterByNumber(_) {
  return function (data, dataField, _ref2, customFilterValue) {
    var _ref2$filterVal = _ref2.filterVal,
        comparator = _ref2$filterVal.comparator,
        number = _ref2$filterVal.number;
    return data.filter(function (row) {
      if (number === '' || !comparator) return true;
      var cell = _.get(row, dataField);

      if (customFilterValue) {
        cell = customFilterValue(cell, row);
      }

      switch (comparator) {
        case _comparison.EQ:
          {
            return cell == number;
          }
        case _comparison.GT:
          {
            return cell > number;
          }
        case _comparison.GE:
          {
            return cell >= number;
          }
        case _comparison.LT:
          {
            return cell < number;
          }
        case _comparison.LE:
          {
            return cell <= number;
          }
        case _comparison.NE:
          {
            return cell != number;
          }
        default:
          {
            console.error('Number comparator provided is not supported');
            return true;
          }
      }
    });
  };
};

var filterByDate = exports.filterByDate = function filterByDate(_) {
  return function (data, dataField, _ref3, customFilterValue) {
    var _ref3$filterVal = _ref3.filterVal,
        comparator = _ref3$filterVal.comparator,
        date = _ref3$filterVal.date;

    if (!date || !comparator) return data;
    var filterDate = date.getUTCDate();
    var filterMonth = date.getUTCMonth();
    var filterYear = date.getUTCFullYear();

    return data.filter(function (row) {
      var valid = true;
      var cell = _.get(row, dataField);

      if (customFilterValue) {
        cell = customFilterValue(cell, row);
      }

      if ((typeof cell === 'undefined' ? 'undefined' : _typeof(cell)) !== 'object') {
        cell = new Date(cell);
      }

      var targetDate = cell.getUTCDate();
      var targetMonth = cell.getUTCMonth();
      var targetYear = cell.getUTCFullYear();

      switch (comparator) {
        case _comparison.EQ:
          {
            if (filterDate !== targetDate || filterMonth !== targetMonth || filterYear !== targetYear) {
              valid = false;
            }
            break;
          }
        case _comparison.GT:
          {
            if (cell <= date) {
              valid = false;
            }
            break;
          }
        case _comparison.GE:
          {
            if (targetYear < filterYear) {
              valid = false;
            } else if (targetYear === filterYear && targetMonth < filterMonth) {
              valid = false;
            } else if (targetYear === filterYear && targetMonth === filterMonth && targetDate < filterDate) {
              valid = false;
            }
            break;
          }
        case _comparison.LT:
          {
            if (cell >= date) {
              valid = false;
            }
            break;
          }
        case _comparison.LE:
          {
            if (targetYear > filterYear) {
              valid = false;
            } else if (targetYear === filterYear && targetMonth > filterMonth) {
              valid = false;
            } else if (targetYear === filterYear && targetMonth === filterMonth && targetDate > filterDate) {
              valid = false;
            }
            break;
          }
        case _comparison.NE:
          {
            if (filterDate === targetDate && filterMonth === targetMonth && filterYear === targetYear) {
              valid = false;
            }
            break;
          }
        default:
          {
            console.error('Date comparator provided is not supported');
            break;
          }
      }
      return valid;
    });
  };
};

var filterByArray = exports.filterByArray = function filterByArray(_) {
  return function (data, dataField, _ref4) {
    var filterVal = _ref4.filterVal,
        comparator = _ref4.comparator;

    if (filterVal.length === 0) return data;
    var refinedFilterVal = filterVal.filter(function (x) {
      return _.isDefined(x);
    }).map(function (x) {
      return x.toString();
    });
    return data.filter(function (row) {
      var cell = _.get(row, dataField);
      var cellStr = _.isDefined(cell) ? cell.toString() : '';
      if (comparator === _comparison.EQ) {
        return refinedFilterVal.indexOf(cellStr) !== -1;
      }
      cellStr = cellStr.toLocaleUpperCase();
      return refinedFilterVal.some(function (item) {
        return cellStr.indexOf(item.toLocaleUpperCase()) !== -1;
      });
    });
  };
};

var filterFactory = exports.filterFactory = function filterFactory(_) {
  return function (filterType) {
    switch (filterType) {
      case _const.FILTER_TYPE.MULTISELECT:
        return filterByArray(_);
      case _const.FILTER_TYPE.NUMBER:
        return filterByNumber(_);
      case _const.FILTER_TYPE.DATE:
        return filterByDate(_);
      case _const.FILTER_TYPE.TEXT:
      case _const.FILTER_TYPE.SELECT:
      default:
        // Use `text` filter as default filter
        return filterByText(_);
    }
  };
};

var filters = exports.filters = function filters(data, columns, _) {
  return function (currFilters) {
    var clearFilters = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : {};

    var factory = filterFactory(_);
    var filterState = _extends({}, clearFilters, currFilters);
    var result = data;
    var filterFn = void 0;
    Object.keys(filterState).forEach(function (dataField) {
      var currentResult = void 0;
      var filterValue = void 0;
      var customFilter = void 0;
      for (var i = 0; i < columns.length; i += 1) {
        if (columns[i].dataField === dataField) {
          filterValue = columns[i].filterValue;
          if (columns[i].filter) {
            customFilter = columns[i].filter.props.onFilter;
          }
          break;
        }
      }

      if (clearFilters[dataField] && customFilter) {
        currentResult = customFilter(clearFilters[dataField].filterVal, result);
        if (typeof currentResult !== 'undefined') {
          result = currentResult;
        }
      } else {
        var filterObj = filterState[dataField];
        filterFn = factory(filterObj.filterType);
        if (customFilter) {
          currentResult = customFilter(filterObj.filterVal, result);
        }
        if (typeof currentResult === 'undefined') {
          result = filterFn(result, dataField, filterObj, filterValue);
        } else {
          result = currentResult;
        }
      }
    });
    return result;
  };
};

/***/ })
/******/ ]);
});
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly8vd2VicGFjay91bml2ZXJzYWxNb2R1bGVEZWZpbml0aW9uIiwid2VicGFjazovLy93ZWJwYWNrL2Jvb3RzdHJhcCAyYjRiZTRmNGQzMTcwYjgxNjM2ZiIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLWZpbHRlci9zcmMvY29tcGFyaXNvbi5qcyIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLWZpbHRlci9zcmMvY29uc3QuanMiLCJ3ZWJwYWNrOi8vL2V4dGVybmFsIHtcInJvb3RcIjpcIlJlYWN0XCIsXCJjb21tb25qczJcIjpcInJlYWN0XCIsXCJjb21tb25qc1wiOlwicmVhY3RcIixcImFtZFwiOlwicmVhY3RcIn0iLCJ3ZWJwYWNrOi8vLy4vbm9kZV9tb2R1bGVzL3Byb3AtdHlwZXMvaW5kZXguanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1maWx0ZXIvaW5kZXguanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1maWx0ZXIvc3JjL2NvbXBvbmVudHMvdGV4dC5qcyIsIndlYnBhY2s6Ly8vLi9ub2RlX21vZHVsZXMvcHJvcC10eXBlcy9mYWN0b3J5V2l0aFRocm93aW5nU2hpbXMuanMiLCJ3ZWJwYWNrOi8vLy4vbm9kZV9tb2R1bGVzL2ZianMvbGliL2VtcHR5RnVuY3Rpb24uanMiLCJ3ZWJwYWNrOi8vLy4vbm9kZV9tb2R1bGVzL2ZianMvbGliL2ludmFyaWFudC5qcyIsIndlYnBhY2s6Ly8vLi9ub2RlX21vZHVsZXMvcHJvcC10eXBlcy9saWIvUmVhY3RQcm9wVHlwZXNTZWNyZXQuanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1maWx0ZXIvc3JjL2NvbXBvbmVudHMvc2VsZWN0LmpzIiwid2VicGFjazovLy8uL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZmlsdGVyL3NyYy9jb21wb25lbnRzL211bHRpc2VsZWN0LmpzIiwid2VicGFjazovLy8uL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZmlsdGVyL3NyYy9jb21wb25lbnRzL251bWJlci5qcyIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLWZpbHRlci9zcmMvY29tcG9uZW50cy9kYXRlLmpzIiwid2VicGFjazovLy8uL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZmlsdGVyL3NyYy9jb250ZXh0LmpzIiwid2VicGFjazovLy8uL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZmlsdGVyL3NyYy9maWx0ZXIuanMiXSwibmFtZXMiOlsiTElLRSIsIkVRIiwiTkUiLCJHVCIsIkdFIiwiTFQiLCJMRSIsIkZJTFRFUl9UWVBFIiwiVEVYVCIsIlNFTEVDVCIsIk1VTFRJU0VMRUNUIiwiTlVNQkVSIiwiREFURSIsIkZJTFRFUl9ERUxBWSIsIkNvbXBhcmlzb24iLCJvcHRpb25zIiwiY3JlYXRlQ29udGV4dCIsIkZJTFRFUl9UWVBFUyIsIkNvbXBhcmF0b3IiLCJ0ZXh0RmlsdGVyIiwicHJvcHMiLCJGaWx0ZXIiLCJUZXh0RmlsdGVyIiwic2VsZWN0RmlsdGVyIiwiU2VsZWN0RmlsdGVyIiwibXVsdGlTZWxlY3RGaWx0ZXIiLCJNdWx0aVNlbGVjdEZpbHRlciIsIm51bWJlckZpbHRlciIsIk51bWJlckZpbHRlciIsImRhdGVGaWx0ZXIiLCJEYXRlRmlsdGVyIiwiY3VzdG9tRmlsdGVyIiwiZmlsdGVyIiwiYmluZCIsImhhbmRsZUNsaWNrIiwidGltZW91dCIsImdldERlZmF1bHRWYWx1ZSIsImZpbHRlclN0YXRlIiwiZmlsdGVyVmFsIiwiZGVmYXVsdFZhbHVlIiwic3RhdGUiLCJ2YWx1ZSIsIm9uRmlsdGVyIiwiZ2V0RmlsdGVyIiwiY29sdW1uIiwiaW5wdXQiLCJzZXRTdGF0ZSIsImNsZWFuVGltZXIiLCJuZXh0UHJvcHMiLCJhcHBseUZpbHRlciIsImUiLCJzdG9wUHJvcGFnYXRpb24iLCJmaWx0ZXJWYWx1ZSIsInRhcmdldCIsInNldFRpbWVvdXQiLCJkZWxheSIsImNsZWFyVGltZW91dCIsImZpbHRlclRleHQiLCJvbkNsaWNrIiwiaWQiLCJwbGFjZWhvbGRlciIsImRhdGFGaWVsZCIsInRleHQiLCJzdHlsZSIsImNsYXNzTmFtZSIsImNhc2VTZW5zaXRpdmUiLCJyZXN0IiwiZWxtSWQiLCJuIiwiQ29tcG9uZW50IiwicHJvcFR5cGVzIiwiUHJvcFR5cGVzIiwiZnVuYyIsImlzUmVxdWlyZWQiLCJvYmplY3QiLCJzdHJpbmciLCJjb21wYXJhdG9yIiwib25lT2YiLCJudW1iZXIiLCJib29sIiwiZGVmYXVsdFByb3BzIiwib3B0aW9uc0VxdWFscyIsImN1cnJPcHRzIiwicHJldk9wdHMiLCJBcnJheSIsImlzQXJyYXkiLCJsZW5ndGgiLCJpIiwibGFiZWwiLCJrZXlzIiwiT2JqZWN0IiwiZ2V0T3B0aW9uVmFsdWUiLCJrZXkiLCJyZXN1bHQiLCJtYXAiLCJnZXRPcHRpb25zIiwiaXNTZWxlY3RlZCIsInVuZGVmaW5lZCIsInNlbGVjdElucHV0IiwicHJldlByb3BzIiwibmVlZEZpbHRlciIsIm5leHRPcHRpb25zIiwib3B0aW9uVGFncyIsIndpdGhvdXRFbXB0eU9wdGlvbiIsInB1c2giLCJmb3JFYWNoIiwic2VsZWN0Q2xhc3MiLCJyZW5kZXJPcHRpb25zIiwib25lT2ZUeXBlIiwiYXJyYXkiLCJhbnkiLCJnZXRTZWxlY3Rpb25zIiwiY29udGFpbmVyIiwic2VsZWN0ZWRPcHRpb25zIiwiZnJvbSIsIml0ZW0iLCJzZWxlY3Rpb25zIiwidG90YWxMZW4iLCJvcHRpb24iLCJzZWxlY3RlZCIsImxlZ2FsQ29tcGFyYXRvcnMiLCJjb21wYXJhdG9ycyIsImluZGV4T2YiLCJvbkNoYW5nZU51bWJlciIsIm9uQ2hhbmdlTnVtYmVyU2V0Iiwib25DaGFuZ2VDb21wYXJhdG9yIiwibnVtYmVyRmlsdGVyQ29tcGFyYXRvciIsIndpdGhvdXRFbXB0eUNvbXBhcmF0b3JPcHRpb24iLCJ3aXRob3V0RW1wdHlOdW1iZXJPcHRpb24iLCJmaWx0ZXJPYmoiLCJudW1iZXJTdHlsZSIsIm51bWJlckNsYXNzTmFtZSIsImNvbXBhcmF0b3JTdHlsZSIsImNvbXBhcmF0b3JDbGFzc05hbWUiLCJjb21wYXJhdG9yRWxtSWQiLCJpbnB1dEVsbUlkIiwiZ2V0RGVmYXVsdENvbXBhcmF0b3IiLCJnZXRDb21wYXJhdG9yT3B0aW9ucyIsImdldE51bWJlck9wdGlvbnMiLCJhcnJheU9mIiwic2hhcGUiLCJwcm9wTmFtZSIsImNvbXBhcmF0b3JJc1ZhbGlkIiwiaiIsIkVycm9yIiwiZGF0ZVBhcnNlciIsImQiLCJnZXRVVENGdWxsWWVhciIsImdldFVUQ01vbnRoIiwic2xpY2UiLCJnZXRVVENEYXRlIiwib25DaGFuZ2VEYXRlIiwiZGF0ZUZpbHRlckNvbXBhcmF0b3IiLCJkYXRlIiwiaW5wdXREYXRlIiwibnVsbGFibGVGaWx0ZXJWYWwiLCJEYXRlIiwiaXNJbml0aWFsIiwiZXhlY3V0ZSIsImRhdGVTdHlsZSIsImRhdGVDbGFzc05hbWUiLCJnZXREZWZhdWx0RGF0ZSIsIl8iLCJpc1JlbW90ZUZpbHRlcmluZyIsImhhbmRsZUZpbHRlckNoYW5nZSIsIkZpbHRlckNvbnRleHQiLCJSZWFjdCIsIkZpbHRlclByb3ZpZGVyIiwiY3VyckZpbHRlcnMiLCJjbGVhckZpbHRlcnMiLCJkb0ZpbHRlciIsIm9uRXh0ZXJuYWxGaWx0ZXIiLCJkYXRhIiwiaXNFbWl0RGF0YUNoYW5nZSIsImZpbHRlclR5cGUiLCJpbml0aWFsaXplIiwiYXNzaWduIiwibmVlZENsZWFyRmlsdGVycyIsImlzRGVmaW5lZCIsImNsZWFyIiwiaXNFcXVhbCIsImlnbm9yZUVtaXREYXRhQ2hhbmdlIiwiZGF0YUNoYW5nZUxpc3RlbmVyIiwiY29sdW1ucyIsImFmdGVyRmlsdGVyIiwiZW1pdCIsImZvcmNlVXBkYXRlIiwiY2hpbGRyZW4iLCJQcm92aWRlciIsIkNvbnN1bWVyIiwiZmlsdGVyQnlUZXh0IiwiY3VzdG9tRmlsdGVyVmFsdWUiLCJ1c2VySW5wdXQiLCJ0b1N0cmluZyIsInJvdyIsImNlbGwiLCJnZXQiLCJjZWxsU3RyIiwiaW5jbHVkZXMiLCJ0b0xvY2FsZVVwcGVyQ2FzZSIsImZpbHRlckJ5TnVtYmVyIiwiY29uc29sZSIsImVycm9yIiwiZmlsdGVyQnlEYXRlIiwiZmlsdGVyRGF0ZSIsImZpbHRlck1vbnRoIiwiZmlsdGVyWWVhciIsInZhbGlkIiwidGFyZ2V0RGF0ZSIsInRhcmdldE1vbnRoIiwidGFyZ2V0WWVhciIsImZpbHRlckJ5QXJyYXkiLCJyZWZpbmVkRmlsdGVyVmFsIiwieCIsInNvbWUiLCJmaWx0ZXJGYWN0b3J5IiwiZmlsdGVycyIsImZhY3RvcnkiLCJmaWx0ZXJGbiIsImN1cnJlbnRSZXN1bHQiXSwibWFwcGluZ3MiOiJBQUFBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLENBQUM7QUFDRCxPO1FDVkE7UUFDQTs7UUFFQTtRQUNBOztRQUVBO1FBQ0E7UUFDQTtRQUNBO1FBQ0E7UUFDQTtRQUNBO1FBQ0E7UUFDQTtRQUNBOztRQUVBO1FBQ0E7O1FBRUE7UUFDQTs7UUFFQTtRQUNBO1FBQ0E7OztRQUdBO1FBQ0E7O1FBRUE7UUFDQTs7UUFFQTtRQUNBO1FBQ0E7UUFDQTtRQUNBO1FBQ0E7UUFDQTtRQUNBLEtBQUs7UUFDTDtRQUNBOztRQUVBO1FBQ0E7UUFDQTtRQUNBLDJCQUEyQiwwQkFBMEIsRUFBRTtRQUN2RCxpQ0FBaUMsZUFBZTtRQUNoRDtRQUNBO1FBQ0E7O1FBRUE7UUFDQSxzREFBc0QsK0RBQStEOztRQUVySDtRQUNBOztRQUVBO1FBQ0E7Ozs7Ozs7Ozs7Ozs7QUM3RE8sSUFBTUEsc0JBQU8sTUFBYjtBQUNBLElBQU1DLGtCQUFLLEdBQVg7QUFDQSxJQUFNQyxrQkFBSyxJQUFYO0FBQ0EsSUFBTUMsa0JBQUssR0FBWDtBQUNBLElBQU1DLGtCQUFLLElBQVg7QUFDQSxJQUFNQyxrQkFBSyxH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QUFPOEgsYUFBUCxLQUF5QixXQUE3QixFQUEwQztBQUN4QzlILG1CQUFTNkgsU0FBUzdILE1BQVQsRUFBaUJoQyxTQUFqQixFQUE0Qm9FLFNBQTVCLEVBQXVDN0UsV0FBdkMsQ0FBVDtBQUNELFNBRkQsTUFFTztBQUNMeUMsbUJBQVM4SCxhQUFUO0FBQ0Q7QUFDRjtBQUNGLEtBL0JEO0FBZ0NBLFdBQU85SCxNQUFQO0FBQ0QsR0F0Q3NCO0FBQUEsQ0FBaEIsQyIsImZpbGUiOiJyZWFjdC1ib290c3RyYXAtdGFibGUyLWZpbHRlci9kaXN0L3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZmlsdGVyLmpzIiwic291cmNlc0NvbnRlbnQiOlsiKGZ1bmN0aW9uIHdlYnBhY2tVbml2ZXJzYWxNb2R1bGVEZWZpbml0aW9uKHJvb3QsIGZhY3RvcnkpIHtcblx0aWYodHlwZW9mIGV4cG9ydHMgPT09ICdvYmplY3QnICYmIHR5cGVvZiBtb2R1bGUgPT09ICdvYmplY3QnKVxuXHRcdG1vZHVsZS5leHBvcnRzID0gZmFjdG9yeShyZXF1aXJlKFwicmVhY3RcIikpO1xuXHRlbHNlIGlmKHR5cGVvZiBkZWZpbmUgPT09ICdmdW5jdGlvbicgJiYgZGVmaW5lLmFtZClcblx0XHRkZWZpbmUoW1wicmVhY3RcIl0sIGZhY3RvcnkpO1xuXHRlbHNlIGlmKHR5cGVvZiBleHBvcnRzID09PSAnb2JqZWN0Jylcblx0XHRleHBvcnRzW1wiUmVhY3RCb290c3RyYXBUYWJsZTJGaWx0ZXJcIl0gPSBmYWN0b3J5KHJlcXVpcmUoXCJyZWFjdFwiKSk7XG5cdGVsc2Vcblx0XHRyb290W1wiUmVhY3RCb290c3RyYXBUYWJsZTJGaWx0ZXJcIl0gPSBmYWN0b3J5KHJvb3RbXCJSZWFjdFwiXSk7XG59KSh0aGlzLCBmdW5jdGlvbihfX1dFQlBBQ0tfRVhURVJOQUxfTU9EVUxFXzJfXykge1xucmV0dXJuIFxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyB3ZWJwYWNrL3VuaXZlcnNhbE1vZHVsZURlZmluaXRpb24iLCIgXHQvLyBUaGUgbW9kdWxlIGNhY2hlXG4gXHR2YXIgaW5zdGFsbGVkTW9kdWxlcyA9IHt9O1xuXG4gXHQvLyBUaGUgcmVxdWlyZSBmdW5jdGlvblxuIFx0ZnVuY3Rpb24gX193ZWJwYWNrX3JlcXVpcmVfXyhtb2R1bGVJZCkge1xuXG4gXHRcdC8vIENoZWNrIGlmIG1vZHVsZSBpcyBpbiBjYWNoZVxuIFx0XHRpZihpbnN0YWxsZWRNb2R1bGVzW21vZHVsZUlkXSkge1xuIFx0XHRcdHJldHVybiBpbnN0YWxsZWRNb2R1bGVzW21vZHVsZUlkXS5leHBvcnRzO1xuIFx0XHR9XG4gXHRcdC8vIENyZWF0ZSBhIG5ldyBtb2R1bGUgKGFuZCBwdXQgaXQgaW50byB0aGUgY2FjaGUpXG4gXHRcdHZhciBtb2R1bGUgPSBpbnN0YWxsZWRNb2R1bGVzW21vZHVsZUlkXSA9IHtcbiBcdFx0XHRpOiBtb2R1bGVJZCxcbiBcdFx0XHRsOiBmYWxzZSxcbiBcdFx0XHRleHBvcnRzOiB7fVxuIFx0XHR9O1xuXG4gXHRcdC8vIEV4ZWN1dGUgdGhlIG1vZHVsZSBmdW5jdGlvblxuIFx0XHRtb2R1bGVzW21vZHVsZUlkXS5jYWxsKG1vZHVsZS5leHBvcnRzLCBtb2R1bGUsIG1vZHVsZS5leHBvcnRzLCBfX3dlYnBhY2tfcmVxdWlyZV9fKTtcblxuIFx0XHQvLyBGbGFnIHRoZSBtb2R1bGUgYXMgbG9hZGVkXG4gXHRcdG1vZHVsZS5sID0gdHJ1ZTtcblxuIFx0XHQvLyBSZXR1cm4gdGhlIGV4cG9ydHMgb2YgdGhlIG1vZHVsZVxuIFx0XHRyZXR1cm4gbW9kdWxlLmV4cG9ydHM7XG4gXHR9XG5cblxuIFx0Ly8gZXhwb3NlIHRoZSBtb2R1bGVzIG9iamVjdCAoX193ZWJwYWNrX21vZHVsZXNfXylcbiBcdF9fd2VicGFja19yZXF1aXJlX18ubSA9IG1vZHVsZXM7XG5cbiBcdC8vIGV4cG9zZSB0aGUgbW9kdWxlIGNhY2hlXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLmMgPSBpbnN0YWxsZWRNb2R1bGVzO1xuXG4gXHQvLyBkZWZpbmUgZ2V0dGVyIGZ1bmN0aW9uIGZvciBoYXJtb255IGV4cG9ydHNcbiBcdF9fd2VicGFja19yZXF1aXJlX18uZCA9IGZ1bmN0aW9uKGV4cG9ydHMsIG5hbWUsIGdldHRlcikge1xuIFx0XHRpZighX193ZWJwYWNrX3JlcXVpcmVfXy5vKGV4cG9ydHMsIG5hbWUpKSB7XG4gXHRcdFx0T2JqZWN0LmRlZmluZVByb3BlcnR5KGV4cG9ydHMsIG5hbWUsIHtcbiBcdFx0XHRcdGNvbmZpZ3VyYWJsZTogZmFsc2UsXG4gXHRcdFx0XHRlbnVtZXJhYmxlOiB0cnVlLFxuIFx0XHRcdFx0Z2V0OiBnZXR0ZXJcbiBcdFx0XHR9KTtcbiBcdFx0fVxuIFx0fTtcblxuIFx0Ly8gZ2V0RGVmYXVsdEV4cG9ydCBmdW5jdGlvbiBmb3IgY29tcGF0aWJpbGl0eSB3aXRoIG5vbi1oYXJtb255IG1vZHVsZXNcbiBcdF9fd2VicGFja19yZXF1aXJlX18ubiA9IGZ1bmN0aW9uKG1vZHVsZSkge1xuIFx0XHR2YXIgZ2V0dGVyID0gbW9kdWxlICYmIG1vZHVsZS5fX2VzTW9kdWxlID9cbiBcdFx0XHRmdW5jdGlvbiBnZXREZWZhdWx0KCkgeyByZXR1cm4gbW9kdWxlWydkZWZhdWx0J107IH0gOlxuIFx0XHRcdGZ1bmN0aW9uIGdldE1vZHVsZUV4cG9ydHMoKSB7IHJldHVybiBtb2R1bGU7IH07XG4gXHRcdF9fd2VicGFja19yZXF1aXJlX18uZChnZXR0ZXIsICdhJywgZ2V0dGVyKTtcbiBcdFx0cmV0dXJuIGdldHRlcjtcbiBcdH07XG5cbiBcdC8vIE9iamVjdC5wcm90b3R5cGUuaGFzT3duUHJvcGVydHkuY2FsbFxuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5vID0gZnVuY3Rpb24ob2JqZWN0LCBwcm9wZXJ0eSkgeyByZXR1cm4gT2JqZWN0LnByb3RvdHlwZS5oYXNPd25Qcm9wZXJ0eS5jYWxsKG9iamVjdCwgcHJvcGVydHkpOyB9O1xuXG4gXHQvLyBfX3dlYnBhY2tfcHVibGljX3BhdGhfX1xuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5wID0gXCJcIjtcblxuIFx0Ly8gTG9hZCBlbnRyeSBtb2R1bGUgYW5kIHJldHVybiBleHBvcnRzXG4gXHRyZXR1cm4gX193ZWJwYWNrX3JlcXVpcmVfXyhfX3dlYnBhY2tfcmVxdWlyZV9fLnMgPSA0KTtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyB3ZWJwYWNrL2Jvb3RzdHJhcCAyYjRiZTRmNGQzMTcwYjgxNjM2ZiIsImV4cG9ydCBjb25zdCBMSUtFID0gJ0xJS0UnO1xuZXhwb3J0IGNvbnN0IEVRID0gJz0nO1xuZXhwb3J0IGNvbnN0IE5FID0gJyE9JztcbmV4cG9ydCBjb25zdCBHVCA9ICc+JztcbmV4cG9ydCBjb25zdCBHRSA9ICc+PSc7XG5leHBvcnQgY29uc3QgTFQgPSAnPCc7XG5leHBvcnQgY29uc3QgTEUgPSAnPD0nO1xuXG5cblxuLy8gV0VCUEFDSyBGT09URVIgLy9cbi8vIC4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1maWx0ZXIvc3JjL2NvbXBhcmlzb24uanMiLCJleHBvcnQgY29uc3QgRklMVEVSX1RZUEUgPSB7XG4gIFRFWFQ6ICdURVhUJyxcbiAgU0VMRUNUOiAnU0VMRUNUJyxcbiAgTVVMVElTRUxFQ1Q6ICdNVUxUSVNFTEVDVCcsXG4gIE5VTUJFUjogJ05VTUJFUicsXG4gIERBVEU6ICdEQVRFJ1xufTtcblxuZXhwb3J0IGNvbnN0IEZJTFRFUl9ERUxBWSA9IDUwMDtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyAuL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZmlsdGVyL3NyYy9jb25zdC5qcyIsIm1vZHVsZS5leHBvcnRzID0gX19XRUJQQUNLX0VYVEVSTkFMX01PRFVMRV8yX187XG5cblxuLy8vLy8vLy8vLy8vLy8vLy8vXG4vLyBXRUJQQUNLIEZPT1RFUlxuLy8gZXh0ZXJuYWwge1wicm9vdFwiOlwiUmVhY3RcIixcImNvbW1vbmpzMlwiOlwicmVhY3RcIixcImNvbW1vbmpzXCI6XCJyZWFjdFwiLFwiYW1kXCI6XCJyZWFjdFwifVxuLy8gbW9kdWxlIGlkID0gMlxuLy8gbW9kdWxlIGNodW5rcyA9IDAgMSIsIi8qKlxuICogQ29weXJpZ2h0IDIwMTMtcHJlc2VudCwgRmFjZWJvb2ssIEluYy5cbiAqIEFsbCByaWdodHMgcmVzZXJ2ZWQuXG4gKlxuICogVGhpcyBzb3VyY2UgY29kZSBpcyBsaWNlbnNlZCB1bmRlciB0aGUgQlNELXN0eWxlIGxpY2Vuc2UgZm91bmQgaW4gdGhlXG4gKiBMSUNFTlNFIGZpbGUgaW4gdGhlIHJvb3QgZGlyZWN0b3J5IG9mIHRoaXMgc291cmNlIHRyZWUuIEFuIGFkZGl0aW9uYWwgZ3JhbnRcbiAqIG9mIHBhdGVudCByaWdodHMgY2FuIGJlIGZvdW5kIGluIHRoZSBQQVRFTlRTIGZpbGUgaW4gdGhlIHNhbWUgZGlyZWN0b3J5LlxuICovXG5cbmlmIChwcm9jZXNzLmVudi5OT0RFX0VOViAhPT0gJ3Byb2R1Y3Rpb24nKSB7XG4gIHZhciBSRUFDVF9FTEVNRU5UX1RZUEUgPSAodHlwZW9mIFN5bWJvbCA9PT0gJ2Z1bmN0aW9uJyAmJlxuICAgIFN5bWJvbC5mb3IgJiZcbiAgICBTeW1ib2wuZm9yKCdyZWFjdC5lbGVtZW50JykpIHx8XG4gICAgMHhlYWM3O1xuXG4gIHZhciBpc1ZhbGlkRWxlbWVudCA9IGZ1bmN0aW9uKG9iamVjdCkge1xuICAgIHJldHVybiB0eXBlb2Ygb2JqZWN0ID09PSAnb2JqZWN0JyAmJlxuICAgICAgb2JqZWN0ICE9PSBudWxsICYmXG4gICAgICBvYmplY3QuJCR0eXBlb2YgPT09IFJFQUNUX0VMRU1FTlRfVFlQRTtcbiAgfTtcblxuICAvLyBCeSBleHBsaWNpdGx5IHVzaW5nIGBwcm9wLXR5cGVzYCB5b3UgYXJlIG9wdGluZyBpbnRvIG5ldyBkZXZlbG9wbWVudCBiZWhhdmlvci5cbiAgLy8gaHR0cDovL2ZiLm1lL3Byb3AtdHlwZXMtaW4tcHJvZFxuICB2YXIgdGhyb3dPbkRpcmVjdEFjY2VzcyA9IHRydWU7XG4gIG1vZHVsZS5leHBvcnRzID0gcmVxdWlyZSgnLi9mYWN0b3J5V2l0aFR5cGVDaGVja2VycycpKGlzVmFsaWRFbGVtZW50LCB0aHJvd09uRGlyZWN0QWNjZXNzKTtcbn0gZWxzZSB7XG4gIC8vIEJ5IGV4cGxpY2l0bHkgdXNpbmcgYHByb3AtdHlwZXNgIHlvdSBhcmUgb3B0aW5nIGludG8gbmV3IHByb2R1Y3Rpb24gYmVoYXZpb3IuXG4gIC8vIGh0dHA6Ly9mYi5tZS9wcm9wLXR5cGVzLWluLXByb2RcbiAgbW9kdWxlLmV4cG9ydHMgPSByZXF1aXJlKCcuL2ZhY3RvcnlXaXRoVGhyb3dpbmdTaGltcycpKCk7XG59XG5cblxuXG4vLy8vLy8vLy8vLy8vLy8vLy9cbi8vIFdFQlBBQ0sgRk9PVEVSXG4vLyAuL25vZGVfbW9kdWxlcy9wcm9wLXR5cGVzL2luZGV4LmpzXG4vLyBtb2R1bGUgaWQgPSAzXG4vLyBtb2R1bGUgY2h1bmtzID0gMCAxIiwiaW1wb3J0IFRleHRGaWx0ZXIgZnJvbSAnLi9zcmMvY29tcG9uZW50cy90ZXh0JztcbmltcG9ydCBTZWxlY3RGaWx0ZXIgZnJvbSAnLi9zcmMvY29tcG9uZW50cy9zZWxlY3QnO1xuaW1wb3J0IE11bHRpU2VsZWN0RmlsdGVyIGZyb20gJy4vc3JjL2NvbXBvbmVudHMvbXVsdGlzZWxlY3QnO1xuaW1wb3J0IE51bWJlckZpbHRlciBmcm9tICcuL3NyYy9jb21wb25lbnRzL251bWJlcic7XG5pbXBvcnQgRGF0ZUZpbHRlciBmcm9tICcuL3NyYy9jb21wb25lbnRzL2RhdGUnO1xuaW1wb3J0IGNyZWF0ZUNvbnRleHQgZnJvbSAnLi9zcmMvY29udGV4dCc7XG5pbXBvcnQgKiBhcyBDb21wYXJpc29uIGZyb20gJy4vc3JjL2NvbXBhcmlzb24nO1xuaW1wb3J0IHsgRklMVEVSX1RZUEUgfSBmcm9tICcuL3NyYy9jb25zdCc7XG5cbmV4cG9ydCBkZWZhdWx0IChvcHRpb25zID0ge30pID0+ICh7XG4gIGNyZWF0ZUNvbnRleHQsXG4gIG9wdGlvbnNcbn0pO1xuXG5leHBvcnQgY29uc3QgRklMVEVSX1RZUEVTID0gRklMVEVSX1RZUEU7XG5cbmV4cG9ydCBjb25zdCBDb21wYXJhdG9yID0gQ29tcGFyaXNvbjtcblxuZXhwb3J0IGNvbnN0IHRleHRGaWx0ZXIgPSAocHJvcHMgPSB7fSkgPT4gKHtcbiAgRmlsdGVyOiBUZXh0RmlsdGVyLFxuICBwcm9wc1xufSk7XG5cbmV4cG9ydCBjb25zdCBzZWxlY3RGaWx0ZXIgPSAocHJvcHMgPSB7fSkgPT4gKHtcbiAgRmlsdGVyOiBTZWxlY3RGaWx0ZXIsXG4gIHByb3BzXG59KTtcblxuZXhwb3J0IGNvbnN0IG11bHRpU2VsZWN0RmlsdGVyID0gKHByb3BzID0ge30pID0+ICh7XG4gIEZpbHRlcjogTXVsdGlTZWxlY3RGaWx0ZXIsXG4gIHByb3BzXG59KTtcblxuZXhwb3J0IGNvbnN0IG51bWJlckZpbHRlciA9IChwcm9wcyA9IHt9KSA9PiAoe1xuICBGaWx0ZXI6IE51bWJlckZpbHRlcixcbiAgcHJvcHNcbn0pO1xuXG5leHBvcnQgY29uc3QgZGF0ZUZpbHRlciA9IChwcm9wcyA9IHt9KSA9PiAoe1xuICBGaWx0ZXI6IERhdGVGaWx0ZXIsXG4gIHByb3BzXG59KTtcblxuZXhwb3J0IGNvbnN0IGN1c3RvbUZpbHRlciA9IChwcm9wcyA9IHt9KSA9PiAoe1xuICBwcm9wc1xufSk7XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLWZpbHRlci9pbmRleC5qcyIsIi8qIGVzbGludCByZWFjdC9yZXF1aXJlLWRlZmF1bHQtcHJvcHM6IDAgKi9cbi8qIGVzbGludCByZWFjdC9wcm9wLXR5cGVzOiAwICovXG4vKiBlc2xpbnQgbm8tcmV0dXJuLWFzc2lnbjogMCAqL1xuLyogZXNsaW50IGNhbWVsY2FzZTogMCAqL1xuaW1wb3J0IFJlYWN0LCB7IENvbXBvbmVudCB9IGZyb20gJ3JlYWN0JztcbmltcG9ydCB7IFByb3BUeXBlcyB9IGZyb20gJ3Byb3AtdHlwZXMnO1xuXG5pbXBvcnQgeyBMSUtFLCBFUSB9IGZyb20gJy4uL2NvbXBhcmlzb24nO1xuaW1wb3J0IHsgRklMVEVSX1RZUEUsIEZJTFRFUl9ERUxBWSB9IGZyb20gJy4uL2NvbnN0JztcblxuY2xhc3MgVGV4dEZpbHRlciBleHRlbmRzIENvbXBvbmVudCB7XG4gIGNvbnN0cnVjdG9yKHByb3BzKSB7XG4gICAgc3VwZXIocHJvcHMpO1xuICAgIHRoaXMuZmlsdGVyID0gdGhpcy5maWx0ZXIuYmluZCh0aGlzKTtcbiAgICB0aGlzLmhhbmRsZUNsaWNrID0gdGhpcy5oYW5kbGVDbGljay5iaW5kKHRoaXMpO1xuICAgIHRoaXMudGltZW91dCA9IG51bGw7XG4gICAgZnVuY3Rpb24gZ2V0RGVmYXVsdFZhbHVlKCkge1xuICAgICAgaWYgKHByb3BzLmZpbHRlclN0YXRlICYmIHR5cGVvZiBwcm9wcy5maWx0ZXJTdGF0ZS5maWx0ZXJWYWwgIT09ICd1bmRlZmluZWQnKSB7XG4gICAgICAgIHJldHVybiBwcm9wcy5maWx0ZXJTdGF0ZS5maWx0ZXJWYWw7XG4gICAgICB9XG4gICAgICByZXR1cm4gcHJvcHMuZGVmYXVsdFZhbHVlO1xuICAgIH1cbiAgICB0aGlzLnN0YXRlID0ge1xuICAgICAgdmFsdWU6IGdldERlZmF1bHRWYWx1ZSgpXG4gICAgfTtcbiAgfVxuXG4gIGNvbXBvbmVudERpZE1vdW50KCkge1xuICAgIGNvbnN0IHsgb25GaWx0ZXIsIGdldEZpbHRlciwgY29sdW1uIH0gPSB0aGlzLnByb3BzO1xuICAgIGNvbnN0IGRlZmF1bHRWYWx1ZSA9IHRoaXMuaW5wdXQudmFsdWU7XG5cbiAgICBpZiAoZGVmYXVsdFZhbHVlKSB7XG4gICAgICBvbkZpbHRlcih0aGlzLnByb3BzLmNvbHVtbiwgRklMVEVSX1RZUEUuVEVYVCwgdHJ1ZSkoZGVmYXVsdFZhbHVlKTtcbiAgICB9XG5cbiAgICAvLyBleHBvcnQgb25GaWx0ZXIgZnVuY3Rpb24gdG8gYWxsb3cgdXNlcnMgdG8gYWNjZXNzXG4gICAgaWYgKGdldEZpbHRlcikge1xuICAgICAgZ2V0RmlsdGVyKChmaWx0ZXJWYWwpID0+IHtcbiAgICAgICAgdGhpcy5zZXRTdGF0ZSgoKSA9PiAoeyB2YWx1ZTogZmlsdGVyVmFsIH0pKTtcbiAgICAgICAgb25GaWx0ZXIoY29sdW1uLCBGSUxURVJfVFlQRS5URVhUKShmaWx0ZXJWYWwpO1xuICAgICAgfSk7XG4gICAgfVxuICB9XG5cbiAgY29tcG9uZW50V2lsbFVubW91bnQoKSB7XG4gICAgdGhpcy5jbGVhblRpbWVyKCk7XG4gIH1cblxuICBVTlNBRkVfY29tcG9uZW50V2lsbFJlY2VpdmVQcm9wcyhuZXh0UHJvcHMpIHtcbiAgICBpZiAobmV4dFByb3BzLmRlZmF1bHRWYWx1ZSAhPT0gdGhpcy5wcm9wcy5kZWZhdWx0VmFsdWUpIHtcbiAgICAgIHRoaXMuYXBwbHlGaWx0ZXIobmV4dFByb3BzLmRlZmF1bHRWYWx1ZSk7XG4gICAgfVxuICB9XG5cbiAgZmlsdGVyKGUpIHtcbiAgICBlLnN0b3BQcm9wYWdhdGlvbigpO1xuICAgIHRoaXMuY2xlYW5UaW1lcigpO1xuICAgIGNvbnN0IGZpbHRlclZhbHVlID0gZS50YXJnZXQudmFsdWU7XG4gICAgdGhpcy5zZXRTdGF0ZSgoKSA9PiAoeyB2YWx1ZTogZmlsdGVyVmFsdWUgfSkpO1xuICAgIHRoaXMudGltZW91dCA9IHNldFRpbWVvdXQoKCkgPT4ge1xuICAgICAgdGhpcy5wcm9wcy5vbkZpbHRlcih0aGlzLnByb3BzLmNvbHVtbiwgRklMVEVSX1RZUEUuVEVYVCkoZmlsdGVyVmFsdWUpO1xuICAgIH0sIHRoaXMucHJvcHMuZGVsYXkpO1xuICB9XG5cbiAgY2xlYW5UaW1lcigpIHtcbiAgICBpZiAodGhpcy50aW1lb3V0KSB7XG4gICAgICBjbGVhclRpbWVvdXQodGhpcy50aW1lb3V0KTtcbiAgICB9XG4gIH1cblxuICBjbGVhbkZpbHRlcmVkKCkge1xuICAgIGNvbnN0IHZhbHVlID0gdGhpcy5wcm9wcy5kZWZhdWx0VmFsdWU7XG4gICAgdGhpcy5zZXRTdGF0ZSgoKSA9PiAoeyB2YWx1ZSB9KSk7XG4gICAgdGhpcy5wcm9wcy5vbkZpbHRlcih0aGlzLnByb3BzLmNvbHVtbiwgRklMVEVSX1RZUEUuVEVYVCkodmFsdWUpO1xuICB9XG5cbiAgYXBwbHlGaWx0ZXIoZmlsdGVyVGV4dCkge1xuICAgIHRoaXMuc2V0U3RhdGUoKCkgPT4gKHsgdmFsdWU6IGZpbHRlclRleHQgfSkpO1xuICAgIHRoaXMucHJvcHMub25GaWx0ZXIodGhpcy5wcm9wcy5jb2x1bW4sIEZJTFRFUl9UWVBFLlRFWFQpKGZpbHRlclRleHQpO1xuICB9XG5cbiAgaGFuZGxlQ2xpY2soZSkge1xuICAgIGUuc3RvcFByb3BhZ2F0aW9uKCk7XG4gICAgaWYgKHRoaXMucHJvcHMub25DbGljaykge1xuICAgICAgdGhpcy5wcm9wcy5vbkNsaWNrKGUpO1xuICAgIH1cbiAgfVxuXG4gIHJlbmRlcigpIHtcbiAgICBjb25zdCB7XG4gICAgICBpZCxcbiAgICAgIHBsYWNlaG9sZGVyLFxuICAgICAgY29sdW1uOiB7IGRhdGFGaWVsZCwgdGV4dCB9LFxuICAgICAgc3R5bGUsXG4gICAgICBjbGFzc05hbWUsXG4gICAgICBvbkZpbHRlcixcbiAgICAgIGNhc2VTZW5zaXRpdmUsXG4gICAgICBkZWZhdWx0VmFsdWUsXG4gICAgICBnZXRGaWx0ZXIsXG4gICAgICBmaWx0ZXJTdGF0ZSxcbiAgICAgIC4uLnJlc3RcbiAgICB9ID0gdGhpcy5wcm9wcztcblxuICAgIGNvbnN0IGVsbUlkID0gYHRleHQtZmlsdGVyLWNvbHVtbi0ke2RhdGFGaWVsZH0ke2lkID8gYC0ke2lkfWAgOiAnJ31gO1xuXG4gICAgcmV0dXJuIChcbiAgICAgIDxsYWJlbFxuICAgICAgICBjbGFzc05hbWU9XCJmaWx0ZXItbGFiZWxcIlxuICAgICAgICBodG1sRm9yPXsgZWxtSWQgfVxuICAgICAgPlxuICAgICAgICA8c3BhbiBjbGFzc05hbWU9XCJzci1vbmx5XCI+RmlsdGVyIGJ5IHt0ZXh0fTwvc3Bhbj5cbiAgICAgICAgPGlucHV0XG4gICAgICAgICAgeyAuLi5yZXN0IH1cbiAgICAgICAgICByZWY9eyBuID0+IHRoaXMuaW5wdXQgPSBuIH1cbiAgICAgICAgICB0eXBlPVwidGV4dFwiXG4gICAgICAgICAgaWQ9eyBlbG1JZCB9XG4gICAgICAgICAgY2xhc3NOYW1lPXsgYGZpbHRlciB0ZXh0LWZpbHRlciBmb3JtLWNvbnRyb2wgJHtjbGFzc05hbWV9YCB9XG4gICAgICAgICAgc3R5bGU9eyBzdHlsZSB9XG4gICAgICAgICAgb25DaGFuZ2U9eyB0aGlzLmZpbHRlciB9XG4gICAgICAgICAgb25DbGljaz17IHRoaXMuaGFuZGxlQ2xpY2sgfVxuICAgICAgICAgIHBsYWNlaG9sZGVyPXsgcGxhY2Vob2xkZXIgfHwgYEVudGVyICR7dGV4dH0uLi5gIH1cbiAgICAgICAgICB2YWx1ZT17IHRoaXMuc3RhdGUudmFsdWUgfVxuICAgICAgICAvPlxuICAgICAgPC9sYWJlbD5cbiAgICApO1xuICB9XG59XG5cblRleHRGaWx0ZXIucHJvcFR5cGVzID0ge1xuICBvbkZpbHRlcjogUHJvcFR5cGVzLmZ1bmMuaXNSZXF1aXJlZCxcbiAgY29sdW1uOiBQcm9wVHlwZXMub2JqZWN0LmlzUmVxdWlyZWQsXG4gIGlkOiBQcm9wVHlwZXMuc3RyaW5nLFxuICBmaWx0ZXJTdGF0ZTogUHJvcFR5cGVzLm9iamVjdCxcbiAgY29tcGFyYXRvcjogUHJvcFR5cGVzLm9uZU9mKFtMSUtFLCBFUV0pLFxuICBkZWZhdWx0VmFsdWU6IFByb3BUeXBlcy5zdHJpbmcsXG4gIGRlbGF5OiBQcm9wVHlwZXMubnVtYmVyLFxuICBwbGFjZWhvbGRlcjogUHJvcFR5cGVzLnN0cmluZyxcbiAgc3R5bGU6IFByb3BUeXBlcy5vYmplY3QsXG4gIGNsYXNzTmFtZTogUHJvcFR5cGVzLnN0cmluZyxcbiAgY2FzZVNlbnNpdGl2ZTogUHJvcFR5cGVzLmJvb2wsXG4gIGdldEZpbHRlcjogUHJvcFR5cGVzLmZ1bmNcbn07XG5cblRleHRGaWx0ZXIuZGVmYXVsdFByb3BzID0ge1xuICBkZWxheTogRklMVEVSX0RFTEFZLFxuICBmaWx0ZXJTdGF0ZToge30sXG4gIGRlZmF1bHRWYWx1ZTogJycsXG4gIGNhc2VTZW5zaXRpdmU6IGZhbHNlLFxuICBpZDogbnVsbFxufTtcblxuXG5leHBvcnQgZGVmYXVsdCBUZXh0RmlsdGVyO1xuXG5cblxuLy8gV0VCUEFDSyBGT09URVIgLy9cbi8vIC4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1maWx0ZXIvc3JjL2NvbXBvbmVudHMvdGV4dC5qcyIsIi8qKlxuICogQ29weXJpZ2h0IDIwMTMtcHJlc2VudCwgRmFjZWJvb2ssIEluYy5cbiAqIEFsbCByaWdodHMgcmVzZXJ2ZWQuXG4gKlxuICogVGhpcyBzb3VyY2UgY29kZSBpcyBsaWNlbnNlZCB1bmRlciB0aGUgQlNELXN0eWxlIGxpY2Vuc2UgZm91bmQgaW4gdGhlXG4gKiBMSUNFTlNFIGZpbGUgaW4gdGhlIHJvb3QgZGlyZWN0b3J5IG9mIHRoaXMgc291cmNlIHRyZWUuIEFuIGFkZGl0aW9uYWwgZ3JhbnRcbiAqIG9mIHBhdGVudCByaWdodHMgY2FuIGJlIGZvdW5kIGluIHRoZSBQQVRFTlRTIGZpbGUgaW4gdGhlIHNhbWUgZGlyZWN0b3J5LlxuICovXG5cbid1c2Ugc3RyaWN0JztcblxudmFyIGVtcHR5RnVuY3Rpb24gPSByZXF1aXJlKCdmYmpzL2xpYi9lbXB0eUZ1bmN0aW9uJyk7XG52YXIgaW52YXJpYW50ID0gcmVxdWlyZSgnZmJqcy9saWIvaW52YXJpYW50Jyk7XG52YXIgUmVhY3RQcm9wVHlwZXNTZWNyZXQgPSByZXF1aXJlKCcuL2xpYi9SZWFjdFByb3BUeXBlc1NlY3JldCcpO1xuXG5tb2R1bGUuZXhwb3J0cyA9IGZ1bmN0aW9uKCkge1xuICBmdW5jdGlvbiBzaGltKHByb3BzLCBwcm9wTmFtZSwgY29tcG9uZW50TmFtZSwgbG9jYXRpb24sIHByb3BGdWxsTmFtZSwgc2VjcmV0KSB7XG4gICAgaWYgKHNlY3JldCA9PT0gUmVhY3RQcm9wVHlwZXNTZWNyZXQpIHtcbiAgICAgIC8vIEl0IGlzIHN0aWxsIHNhZmUgd2hlbiBjYWxsZWQgZnJvbSBSZWFjdC5cbiAgICAgIHJldHVybjtcbiAgICB9XG4gICAgaW52YXJpYW50KFxuICAgICAgZmFsc2UsXG4gICAgICAnQ2FsbGluZyBQcm9wVHlwZXMgdmFsaWRhdG9ycyBkaXJlY3RseSBpcyBub3Qgc3VwcG9ydGVkIGJ5IHRoZSBgcHJvcC10eXBlc2AgcGFja2FnZS4gJyArXG4gICAgICAnVXNlIFByb3BUeXBlcy5jaGVja1Byb3BUeXBlcygpIHRvIGNhbGwgdGhlbS4gJyArXG4gICAgICAnUmVhZCBtb3JlIGF0IGh0dHA6Ly9mYi5tZS91c2UtY2hlY2stcHJvcC10eXBlcydcbiAgICApO1xuICB9O1xuICBzaGltLmlzUmVxdWlyZWQgPSBzaGltO1xuICBmdW5jdGlvbiBnZXRTaGltKCkge1xuICAgIHJldHVybiBzaGltO1xuICB9O1xuICAvLyBJbXBvcnRhbnQhXG4gIC8vIEtlZXAgdGhpcyBsaXN0IGluIHN5bmMgd2l0aCBwcm9kdWN0aW9uIHZlcnNpb24gaW4gYC4vZmFjdG9yeVdpdGhUeXBlQ2hlY2tlcnMuanNgLlxuICB2YXIgUmVhY3RQcm9wVHlwZXMgPSB7XG4gICAgYXJyYXk6IHNoaW0sXG4gICAgYm9vbDogc2hpbSxcbiAgICBmdW5jOiBzaGltLFxuICAgIG51bWJlcjogc2hpbSxcbiAgICBvYmplY3Q6IHNoaW0sXG4gICAgc3RyaW5nOiBzaGltLFxuICAgIHN5bWJvbDogc2hpbSxcblxuICAgIGFueTogc2hpbSxcbiAgICBhcnJheU9mOiBnZXRTaGltLFxuICAgIGVsZW1lbnQ6IHNoaW0sXG4gICAgaW5zdGFuY2VPZjogZ2V0U2hpbSxcbiAgICBub2RlOiBzaGltLFxuICAgIG9iamVjdE9mOiBnZXRTaGltLFxuICAgIG9uZU9mOiBnZXRTaGltLFxuICAgIG9uZU9mVHlwZTogZ2V0U2hpbSxcbiAgICBzaGFwZTogZ2V0U2hpbVxuICB9O1xuXG4gIFJlYWN0UHJvcFR5cGVzLmNoZWNrUHJvcFR5cGVzID0gZW1wdHlGdW5jdGlvbjtcbiAgUmVhY3RQcm9wVHlwZXMuUHJvcFR5cGVzID0gUmVhY3RQcm9wVHlwZXM7XG5cbiAgcmV0dXJuIFJlYWN0UHJvcFR5cGVzO1xufTtcblxuXG5cbi8vLy8vLy8vLy8vLy8vLy8vL1xuLy8gV0VCUEFDSyBGT09URVJcbi8vIC4vbm9kZV9tb2R1bGVzL3Byb3AtdHlwZXMvZmFjdG9yeVdpdGhUaHJvd2luZ1NoaW1zLmpzXG4vLyBtb2R1bGUgaWQgPSA2XG4vLyBtb2R1bGUgY2h1bmtzID0gMCAxIiwiXCJ1c2Ugc3RyaWN0XCI7XG5cbi8qKlxuICogQ29weXJpZ2h0IChjKSAyMDEzLXByZXNlbnQsIEZhY2Vib29rLCBJbmMuXG4gKlxuICogVGhpcyBzb3VyY2UgY29kZSBpcyBsaWNlbnNlZCB1bmRlciB0aGUgTUlUIGxpY2Vuc2UgZm91bmQgaW4gdGhlXG4gKiBMSUNFTlNFIGZpbGUgaW4gdGhlIHJvb3QgZGlyZWN0b3J5IG9mIHRoaXMgc291cmNlIHRyZWUuXG4gKlxuICogXG4gKi9cblxuZnVuY3Rpb24gbWFrZUVtcHR5RnVuY3Rpb24oYXJnKSB7XG4gIHJldHVybiBmdW5jdGlvbiAoKSB7XG4gICAgcmV0dXJuIGFyZztcbiAgfTtcbn1cblxuLyoqXG4gKiBUaGlzIGZ1bmN0aW9uIGFjY2VwdHMgYW5kIGRpc2NhcmRzIGlucHV0czsgaXQgaGFzIG5vIHNpZGUgZWZmZWN0cy4gVGhpcyBpc1xuICogcHJpbWFyaWx5IHVzZWZ1bCBpZGlvbWF0aWNhbGx5IGZvciBvdmVycmlkYWJsZSBmdW5jdGlvbiBlbmRwb2ludHMgd2hpY2hcbiAqIGFsd2F5cyBuZWVkIHRvIGJlIGNhbGxhYmxlLCBzaW5jZSBKUyBsYWNrcyBhIG51bGwtY2FsbCBpZGlvbSBhbGEgQ29jb2EuXG4gKi9cbnZhciBlbXB0eUZ1bmN0aW9uID0gZnVuY3Rpb24gZW1wdHlGdW5jdGlvbigpIHt9O1xuXG5lbXB0eUZ1bmN0aW9uLnRoYXRSZXR1cm5zID0gbWFrZUVtcHR5RnVuY3Rpb247XG5lbXB0eUZ1bmN0aW9uLnRoYXRSZXR1cm5zRmFsc2UgPSBtYWtlRW1wdHlGdW5jdGlvbihmYWxzZSk7XG5lbXB0eUZ1bmN0aW9uLnRoYXRSZXR1cm5zVHJ1ZSA9IG1ha2VFbXB0eUZ1bmN0aW9uKHRydWUpO1xuZW1wdHlGdW5jdGlvbi50aGF0UmV0dXJuc051bGwgPSBtYWtlRW1wdHlGdW5jdGlvbihudWxsKTtcbmVtcHR5RnVuY3Rpb24udGhhdFJldHVybnNUaGlzID0gZnVuY3Rpb24gKCkge1xuICByZXR1cm4gdGhpcztcbn07XG5lbXB0eUZ1bmN0aW9uLnRoYXRSZXR1cm5zQXJndW1lbnQgPSBmdW5jdGlvbiAoYXJnKSB7XG4gIHJldHVybiBhcmc7XG59O1xuXG5tb2R1bGUuZXhwb3J0cyA9IGVtcHR5RnVuY3Rpb247XG5cblxuLy8vLy8vLy8vLy8vLy8vLy8vXG4vLyBXRUJQQUNLIEZPT1RFUlxuLy8gLi9ub2RlX21vZHVsZXMvZmJqcy9saWIvZW1wdHlGdW5jdGlvbi5qc1xuLy8gbW9kdWxlIGlkID0gN1xuLy8gbW9kdWxlIGNodW5rcyA9IDAgMSIsIi8qKlxuICogQ29weXJpZ2h0IChjKSAyMDEzLXByZXNlbnQsIEZhY2Vib29rLCBJbmMuXG4gKlxuICogVGhpcyBzb3VyY2UgY29kZSBpcyBsaWNlbnNlZCB1bmRlciB0aGUgTUlUIGxpY2Vuc2UgZm91bmQgaW4gdGhlXG4gKiBMSUNFTlNFIGZpbGUgaW4gdGhlIHJvb3QgZGlyZWN0b3J5IG9mIHRoaXMgc291cmNlIHRyZWUuXG4gKlxuICovXG5cbid1c2Ugc3RyaWN0JztcblxuLyoqXG4gKiBVc2UgaW52YXJpYW50KCkgdG8gYXNzZXJ0IHN0YXRlIHdoaWNoIHlvdXIgcHJvZ3JhbSBhc3N1bWVzIHRvIGJlIHRydWUuXG4gKlxuICogUHJvdmlkZSBzcHJpbnRmLXN0eWxlIGZvcm1hdCAob25seSAlcyBpcyBzdXBwb3J0ZWQpIGFuZCBhcmd1bWVudHNcbiAqIHRvIHByb3ZpZGUgaW5mb3JtYXRpb24gYWJvdXQgd2hhdCBicm9rZSBhbmQgd2hhdCB5b3Ugd2VyZVxuICogZXhwZWN0aW5nLlxuICpcbiAqIFRoZSBpbnZhcmlhbnQgbWVzc2FnZSB3aWxsIGJlIHN0cmlwcGVkIGluIHByb2R1Y3Rpb24sIGJ1dCB0aGUgaW52YXJpYW50XG4gKiB3aWxsIHJlbWFpbiB0byBlbnN1cmUgbG9naWMgZG9lcyBub3QgZGlmZmVyIGluIHByb2R1Y3Rpb24uXG4gKi9cblxudmFyIHZhbGlkYXRlRm9ybWF0ID0gZnVuY3Rpb24gdmFsaWRhdGVGb3JtYXQoZm9ybWF0KSB7fTtcblxuaWYgKHByb2Nlc3MuZW52Lk5PREVfRU5WICE9PSAncHJvZHVjdGlvbicpIHtcbiAgdmFsaWRhdGVGb3JtYXQgPSBmdW5jdGlvbiB2YWxpZGF0ZUZvcm1hdChmb3JtYXQpIHtcbiAgICBpZiAoZm9ybWF0ID09PSB1bmRlZmluZWQpIHtcbiAgICAgIHRocm93IG5ldyBFcnJvcignaW52YXJpYW50IHJlcXVpcmVzIGFuIGVycm9yIG1lc3NhZ2UgYXJndW1lbnQnKTtcbiAgICB9XG4gIH07XG59XG5cbmZ1bmN0aW9uIGludmFyaWFudChjb25kaXRpb24sIGZvcm1hdCwgYSwgYiwgYywgZCwgZSwgZikge1xuICB2YWxpZGF0ZUZvcm1hdChmb3JtYXQpO1xuXG4gIGlmICghY29uZGl0aW9uKSB7XG4gICAgdmFyIGVycm9yO1xuICAgIGlmIChmb3JtYXQgPT09IHVuZGVmaW5lZCkge1xuICAgICAgZXJyb3IgPSBuZXcgRXJyb3IoJ01pbmlmaWVkIGV4Y2VwdGlvbiBvY2N1cnJlZDsgdXNlIHRoZSBub24tbWluaWZpZWQgZGV2IGVudmlyb25tZW50ICcgKyAnZm9yIHRoZSBmdWxsIGVycm9yIG1lc3NhZ2UgYW5kIGFkZGl0aW9uYWwgaGVscGZ1bCB3YXJuaW5ncy4nKTtcbiAgICB9IGVsc2Uge1xuICAgICAgdmFyIGFyZ3MgPSBbYSwgYiwgYywgZCwgZSwgZl07XG4gICAgICB2YXIgYXJnSW5kZXggPSAwO1xuICAgICAgZXJyb3IgPSBuZXcgRXJyb3IoZm9ybWF0LnJlcGxhY2UoLyVzL2csIGZ1bmN0aW9uICgpIHtcbiAgICAgICAgcmV0dXJuIGFyZ3NbYXJnSW5kZXgrK107XG4gICAgICB9KSk7XG4gICAgICBlcnJvci5uYW1lID0gJ0ludmFyaWFudCBWaW9sYXRpb24nO1xuICAgIH1cblxuICAgIGVycm9yLmZyYW1lc1RvUG9wID0gMTsgLy8gd2UgZG9uJ3QgY2FyZSBhYm91dCBpbnZhcmlhbnQncyBvd24gZnJhbWVcbiAgICB0aHJvdyBlcnJvcjtcbiAgfVxufVxuXG5tb2R1bGUuZXhwb3J0cyA9IGludmFyaWFudDtcblxuXG4vLy8vLy8vLy8vLy8vLy8vLy9cbi8vIFdFQlBBQ0sgRk9PVEVSXG4vLyAuL25vZGVfbW9kdWxlcy9mYmpzL2xpYi9pbnZhcmlhbnQuanNcbi8vIG1vZHVsZSBpZCA9IDhcbi8vIG1vZHVsZSBjaHVua3MgPSAwIDEiLCIvKipcbiAqIENvcHlyaWdodCAyMDEzLXByZXNlbnQsIEZhY2Vib29rLCBJbmMuXG4gKiBBbGwgcmlnaHRzIHJlc2VydmVkLlxuICpcbiAqIFRoaXMgc291cmNlIGNvZGUgaXMgbGljZW5zZWQgdW5kZXIgdGhlIEJTRC1zdHlsZSBsaWNlbnNlIGZvdW5kIGluIHRoZVxuICogTElDRU5TRSBmaWxlIGluIHRoZSByb290IGRpcmVjdG9yeSBvZiB0aGlzIHNvdXJjZSB0cmVlLiBBbiBhZGRpdGlvbmFsIGdyYW50XG4gKiBvZiBwYXRlbnQgcmlnaHRzIGNhbiBiZSBmb3VuZCBpbiB0aGUgUEFURU5UUyBmaWxlIGluIHRoZSBzYW1lIGRpcmVjdG9yeS5cbiAqL1xuXG4ndXNlIHN0cmljdCc7XG5cbnZhciBSZWFjdFByb3BUeXBlc1NlY3JldCA9ICdTRUNSRVRfRE9fTk9UX1BBU1NfVEhJU19PUl9ZT1VfV0lMTF9CRV9GSVJFRCc7XG5cbm1vZHVsZS5leHBvcnRzID0gUmVhY3RQcm9wVHlwZXNTZWNyZXQ7XG5cblxuXG4vLy8vLy8vLy8vLy8vLy8vLy9cbi8vIFdFQlBBQ0sgRk9PVEVSXG4vLyAuL25vZGVfbW9kdWxlcy9wcm9wLXR5cGVzL2xpYi9SZWFjdFByb3BUeXBlc1NlY3JldC5qc1xuLy8gbW9kdWxlIGlkID0gOVxuLy8gbW9kdWxlIGNodW5rcyA9IDAgMSIsIi8qIGVzbGludCByZWFjdC9yZXF1aXJlLWRlZmF1bHQtcHJvcHM6IDAgKi9cbi8qIGVzbGludCBuby1yZXR1cm4tYXNzaWduOiAwICovXG4vKiBlc2xpbnQgcmVhY3Qvbm8tdW51c2VkLXByb3AtdHlwZXM6IDAgKi9cbi8qIGVzbGludCBjbGFzcy1tZXRob2RzLXVzZS10aGlzOiAwICovXG5pbXBvcnQgUmVhY3QsIHsgQ29tcG9uZW50IH0gZnJvbSAncmVhY3QnO1xuaW1wb3J0IFByb3BUeXBlcyBmcm9tICdwcm9wLXR5cGVzJztcbmltcG9ydCB7IExJS0UsIEVRIH0gZnJvbSAnLi4vY29tcGFyaXNvbic7XG5pbXBvcnQgeyBGSUxURVJfVFlQRSB9IGZyb20gJy4uL2NvbnN0JztcblxuZnVuY3Rpb24gb3B0aW9uc0VxdWFscyhjdXJyT3B0cywgcHJldk9wdHMpIHtcbiAgaWYgKEFycmF5LmlzQXJyYXkoY3Vyck9wdHMpKSB7XG4gICAgaWYgKGN1cnJPcHRzLmxlbmd0aCA9PT0gcHJldk9wdHMubGVuZ3RoKSB7XG4gICAgICBmb3IgKGxldCBpID0gMDsgaSA8IGN1cnJPcHRzLmxlbmd0aDsgaSArPSAxKSB7XG4gICAgICAgIGlmIChcbiAgICAgICAgICBjdXJyT3B0c1tpXS52YWx1ZSAhPT0gcHJldk9wdHNbaV0udmFsdWUgfHxcbiAgICAgICAgICBjdXJyT3B0c1tpXS5sYWJlbCAhPT0gcHJldk9wdHNbaV0ubGFiZWxcbiAgICAgICAgKSB7XG4gICAgICAgICAgcmV0dXJuIGZhbHNlO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgICByZXR1cm4gdHJ1ZTtcbiAgICB9XG4gICAgcmV0dXJuIGZhbHNlO1xuICB9XG4gIGNvbnN0IGtleXMgPSBPYmplY3Qua2V5cyhjdXJyT3B0cyk7XG4gIGZvciAobGV0IGkgPSAwOyBpIDwga2V5cy5sZW5ndGg7IGkgKz0gMSkge1xuICAgIGlmIChjdXJyT3B0c1trZXlzW2ldXSAhPT0gcHJldk9wdHNba2V5c1tpXV0pIHtcbiAgICAgIHJldHVybiBmYWxzZTtcbiAgICB9XG4gIH1cbiAgcmV0dXJuIE9iamVjdC5rZXlzKGN1cnJPcHRzKS5sZW5ndGggPT09IE9iamVjdC5rZXlzKHByZXZPcHRzKS5sZW5ndGg7XG59XG5cbmZ1bmN0aW9uIGdldE9wdGlvblZhbHVlKG9wdGlvbnMsIGtleSkge1xuICBpZiAoQXJyYXkuaXNBcnJheShvcHRpb25zKSkge1xuICAgIGNvbnN0IHJlc3VsdCA9IG9wdGlvbnNcbiAgICAgIC5maWx0ZXIoKHsgbGFiZWwgfSkgPT4gbGFiZWwgPT09IGtleSlcbiAgICAgIC5tYXAoKHsgdmFsdWUgfSkgPT4gdmFsdWUpO1xuICAgIHJldHVybiByZXN1bHRbMF07XG4gIH1cbiAgcmV0dXJuIG9wdGlvbnNba2V5XTtcbn1cblxuY2xhc3MgU2VsZWN0RmlsdGVyIGV4dGVuZHMgQ29tcG9uZW50IHtcbiAgY29uc3RydWN0b3IocHJvcHMpIHtcbiAgICBzdXBlcihwcm9wcyk7XG4gICAgdGhpcy5maWx0ZXIgPSB0aGlzLmZpbHRlci5iaW5kKHRoaXMpO1xuICAgIHRoaXMub3B0aW9ucyA9IHRoaXMuZ2V0T3B0aW9ucyhwcm9wcyk7XG4gICAgY29uc3QgaXNTZWxlY3RlZCA9IGdldE9wdGlvblZhbHVlKHRoaXMub3B0aW9ucywgdGhpcy5nZXREZWZhdWx0VmFsdWUoKSkgIT09IHVuZGVmaW5lZDtcbiAgICB0aGlzLnN0YXRlID0geyBpc1NlbGVjdGVkIH07XG4gIH1cblxuICBjb21wb25lbnREaWRNb3VudCgpIHtcbiAgICBjb25zdCB7IGNvbHVtbiwgb25GaWx0ZXIsIGdldEZpbHRlciB9ID0gdGhpcy5wcm9wcztcblxuICAgIGNvbnN0IHZhbHVlID0gdGhpcy5zZWxlY3RJbnB1dC52YWx1ZTtcbiAgICBpZiAodmFsdWUgJiYgdmFsdWUgIT09ICcnKSB7XG4gICAgICBvbkZpbHRlcihjb2x1bW4sIEZJTFRFUl9UWVBFLlNFTEVDVCwgdHJ1ZSkodmFsdWUpO1xuICAgIH1cblxuICAgIC8vIGV4cG9ydCBvbkZpbHRlciBmdW5jdGlvbiB0byBhbGxvdyB1c2VycyB0byBhY2Nlc3NcbiAgICBpZiAoZ2V0RmlsdGVyKSB7XG4gICAgICBnZXRGaWx0ZXIoKGZpbHRlclZhbCkgPT4ge1xuICAgICAgICB0aGlzLnNldFN0YXRlKCgpID0+ICh7IGlzU2VsZWN0ZWQ6IGZpbHRlclZhbCAhPT0gJycgfSkpO1xuICAgICAgICB0aGlzLnNlbGVjdElucHV0LnZhbHVlID0gZmlsdGVyVmFsO1xuXG4gICAgICAgIG9uRmlsdGVyKGNvbHVtbiwgRklMVEVSX1RZUEUuU0VMRUNUKShmaWx0ZXJWYWwpO1xuICAgICAgfSk7XG4gICAgfVxuICB9XG5cbiAgY29tcG9uZW50RGlkVXBkYXRlKHByZXZQcm9wcykge1xuICAgIGxldCBuZWVkRmlsdGVyID0gZmFsc2U7XG4gICAgY29uc3Qge1xuICAgICAgY29sdW1uLFxuICAgICAgb25GaWx0ZXIsXG4gICAgICBkZWZhdWx0VmFsdWVcbiAgICB9ID0gdGhpcy5wcm9wcztcbiAgICBjb25zdCBuZXh0T3B0aW9ucyA9IHRoaXMuZ2V0T3B0aW9ucyh0aGlzLnByb3BzKTtcbiAgICBpZiAoZGVmYXVsdFZhbHVlICE9PSBwcmV2UHJvcHMuZGVmYXVsdFZhbHVlKSB7XG4gICAgICBuZWVkRmlsdGVyID0gdHJ1ZTtcbiAgICB9IGVsc2UgaWYgKCFvcHRpb25zRXF1YWxzKG5leHRPcHRpb25zLCB0aGlzLm9wdGlvbnMpKSB7XG4gICAgICB0aGlzLm9wdGlvbnMgPSBuZXh0T3B0aW9ucztcbiAgICAgIG5lZWRGaWx0ZXIgPSB0cnVlO1xuICAgIH1cbiAgICBpZiAobmVlZEZpbHRlcikge1xuICAgICAgY29uc3QgdmFsdWUgPSB0aGlzLnNlbGVjdElucHV0LnZhbHVlO1xuICAgICAgaWYgKHZhbHVlKSB7XG4gICAgICAgIG9uRmlsdGVyKGNvbHVtbiwgRklMVEVSX1RZUEUuU0VMRUNUKSh2YWx1ZSk7XG4gICAgICB9XG4gICAgfVxuICB9XG5cbiAgZ2V0T3B0aW9ucyhwcm9wcykge1xuICAgIHJldHVybiB0eXBlb2YgcHJvcHMub3B0aW9ucyA9PT0gJ2Z1bmN0aW9uJyA/IHByb3BzLm9wdGlvbnMocHJvcHMuY29sdW1uKSA6IHByb3BzLm9wdGlvbnM7XG4gIH1cblxuICBnZXREZWZhdWx0VmFsdWUoKSB7XG4gICAgY29uc3QgeyBmaWx0ZXJTdGF0ZSwgZGVmYXVsdFZhbHVlIH0gPSB0aGlzLnByb3BzO1xuICAgIGlmIChmaWx0ZXJTdGF0ZSAmJiB0eXBlb2YgZmlsdGVyU3RhdGUuZmlsdGVyVmFsICE9PSAndW5kZWZpbmVkJykge1xuICAgICAgcmV0dXJuIGZpbHRlclN0YXRlLmZpbHRlclZhbDtcbiAgICB9XG4gICAgcmV0dXJuIGRlZmF1bHRWYWx1ZTtcbiAgfVxuXG4gIGNsZWFuRmlsdGVyZWQoKSB7XG4gICAgY29uc3QgdmFsdWUgPSAodGhpcy5wcm9wcy5kZWZhdWx0VmFsdWUgIT09IHVuZGVmaW5lZCkgPyB0aGlzLnByb3BzLmRlZmF1bHRWYWx1ZSA6ICcnO1xuICAgIHRoaXMuc2V0U3RhdGUoKCkgPT4gKHsgaXNTZWxlY3RlZDogdmFsdWUgIT09ICcnIH0pKTtcbiAgICB0aGlzLnNlbGVjdElucHV0LnZhbHVlID0gdmFsdWU7XG4gICAgdGhpcy5wcm9wcy5vbkZpbHRlcih0aGlzLnByb3BzLmNvbHVtbiwgRklMVEVSX1RZUEUuU0VMRUNUKSh2YWx1ZSk7XG4gIH1cblxuICBhcHBseUZpbHRlcih2YWx1ZSkge1xuICAgIHRoaXMuc2VsZWN0SW5wdXQudmFsdWUgPSB2YWx1ZTtcbiAgICB0aGlzLnNldFN0YXRlKCgpID0+ICh7IGlzU2VsZWN0ZWQ6IHZhbHVlICE9PSAnJyB9KSk7XG4gICAgdGhpcy5wcm9wcy5vbkZpbHRlcih0aGlzLnByb3BzLmNvbHVtbiwgRklMVEVSX1RZUEUuU0VMRUNUKSh2YWx1ZSk7XG4gIH1cblxuICBmaWx0ZXIoZSkge1xuICAgIGNvbnN0IHsgdmFsdWUgfSA9IGUudGFyZ2V0O1xuICAgIHRoaXMuc2V0U3RhdGUoKCkgPT4gKHsgaXNTZWxlY3RlZDogdmFsdWUgIT09ICcnIH0pKTtcbiAgICB0aGlzLnByb3BzLm9uRmlsdGVyKHRoaXMucHJvcHMuY29sdW1uLCBGSUxURVJfVFlQRS5TRUxFQ1QpKHZhbHVlKTtcbiAgfVxuXG4gIHJlbmRlck9wdGlvbnMoKSB7XG4gICAgY29uc3Qgb3B0aW9uVGFncyA9IFtdO1xuICAgIGNvbnN0IHsgb3B0aW9ucyB9ID0gdGhpcztcbiAgICBjb25zdCB7IHBsYWNlaG9sZGVyLCBjb2x1bW4sIHdpdGhvdXRFbXB0eU9wdGlvbiB9ID0gdGhpcy5wcm9wcztcbiAgICBpZiAoIXdpdGhvdXRFbXB0eU9wdGlvbikge1xuICAgICAgb3B0aW9uVGFncy5wdXNoKChcbiAgICAgICAgPG9wdGlvbiBrZXk9XCItMVwiIHZhbHVlPVwiXCI+eyBwbGFjZWhvbGRlciB8fCBgU2VsZWN0ICR7Y29sdW1uLnRleHR9Li4uYCB9PC9vcHRpb24+XG4gICAgICApKTtcbiAgICB9XG4gICAgaWYgKEFycmF5LmlzQXJyYXkob3B0aW9ucykpIHtcbiAgICAgIG9wdGlvbnMuZm9yRWFjaCgoeyB2YWx1ZSwgbGFiZWwgfSkgPT5cbiAgICAgICAgb3B0aW9uVGFncy5wdXNoKDxvcHRpb24ga2V5PXsgdmFsdWUgfSB2YWx1ZT17IHZhbHVlIH0+eyBsYWJlbCB9PC9vcHRpb24+KSk7XG4gICAgfSBlbHNlIHtcbiAgICAgIE9iamVjdC5rZXlzKG9wdGlvbnMpLmZvckVhY2goa2V5ID0+XG4gICAgICAgIG9wdGlvblRhZ3MucHVzaCg8b3B0aW9uIGtleT17IGtleSB9IHZhbHVlPXsga2V5IH0+eyBvcHRpb25zW2tleV0gfTwvb3B0aW9uPilcbiAgICAgICk7XG4gICAgfVxuICAgIHJldHVybiBvcHRpb25UYWdzO1xuICB9XG5cbiAgcmVuZGVyKCkge1xuICAgIGNvbnN0IHtcbiAgICAgIGlkLFxuICAgICAgc3R5bGUsXG4gICAgICBjbGFzc05hbWUsXG4gICAgICBkZWZhdWx0VmFsdWUsXG4gICAgICBvbkZpbHRlcixcbiAgICAgIGNvbHVtbixcbiAgICAgIG9wdGlvbnMsXG4gICAgICBjb21wYXJhdG9yLFxuICAgICAgd2l0aG91dEVtcHR5T3B0aW9uLFxuICAgICAgY2FzZVNlbnNpdGl2ZSxcbiAgICAgIGdldEZpbHRlcixcbiAgICAgIGZpbHRlclN0YXRlLFxuICAgICAgLi4ucmVzdFxuICAgIH0gPSB0aGlzLnByb3BzO1xuXG4gICAgY29uc3Qgc2VsZWN0Q2xhc3MgPVxuICAgICAgYGZpbHRlciBzZWxlY3QtZmlsdGVyIGZvcm0tY29udHJvbCAke2NsYXNzTmFtZX0gJHt0aGlzLnN0YXRlLmlzU2VsZWN0ZWQgPyAnJyA6ICdwbGFjZWhvbGRlci1zZWxlY3RlZCd9YDtcbiAgICBjb25zdCBlbG1JZCA9IGBzZWxlY3QtZmlsdGVyLWNvbHVtbi0ke2NvbHVtbi5kYXRhRmllbGR9JHtpZCA/IGAtJHtpZH1gIDogJyd9YDtcblxuICAgIHJldHVybiAoXG4gICAgICA8bGFiZWxcbiAgICAgICAgY2xhc3NOYW1lPVwiZmlsdGVyLWxhYmVsXCJcbiAgICAgICAgaHRtbEZvcj17IGVsbUlkIH1cbiAgICAgID5cbiAgICAgICAgPHNwYW4gY2xhc3NOYW1lPVwic3Itb25seVwiPkZpbHRlciBieSB7IGNvbHVtbi50ZXh0IH08L3NwYW4+XG4gICAgICAgIDxzZWxlY3RcbiAgICAgICAgICB7IC4uLnJlc3QgfVxuICAgICAgICAgIHJlZj17IG4gPT4gdGhpcy5zZWxlY3RJbnB1dCA9IG4gfVxuICAgICAgICAgIGlkPXsgZWxtSWQgfVxuICAgICAgICAgIHN0eWxlPXsgc3R5bGUgfVxuICAgICAgICAgIGNsYXNzTmFtZT17IHNlbGVjdENsYXNzIH1cbiAgICAgICAgICBvbkNoYW5nZT17IHRoaXMuZmlsdGVyIH1cbiAgICAgICAgICBvbkNsaWNrPXsgZSA9PiBlLnN0b3BQcm9wYWdhdGlvbigpIH1cbiAgICAgICAgICBkZWZhdWx0VmFsdWU9eyB0aGlzLmdldERlZmF1bHRWYWx1ZSgpIHx8ICcnIH1cbiAgICAgICAgPlxuICAgICAgICAgIHsgdGhpcy5yZW5kZXJPcHRpb25zKCkgfVxuICAgICAgICA8L3NlbGVjdD5cbiAgICAgIDwvbGFiZWw+XG4gICAgKTtcbiAgfVxufVxuXG5TZWxlY3RGaWx0ZXIucHJvcFR5cGVzID0ge1xuICBvbkZpbHRlcjogUHJvcFR5cGVzLmZ1bmMuaXNSZXF1aXJlZCxcbiAgY29sdW1uOiBQcm9wVHlwZXMub2JqZWN0LmlzUmVxdWlyZWQsXG4gIGlkOiBQcm9wVHlwZXMuc3RyaW5nLFxuICBmaWx0ZXJTdGF0ZTogUHJvcFR5cGVzLm9iamVjdCxcbiAgb3B0aW9uczogUHJvcFR5cGVzLm9uZU9mVHlwZShbUHJvcFR5cGVzLm9iamVjdCwgUHJvcFR5cGVzLmFycmF5XSkuaXNSZXF1aXJlZCxcbiAgY29tcGFyYXRvcjogUHJvcFR5cGVzLm9uZU9mKFtMSUtFLCBFUV0pLFxuICBwbGFjZWhvbGRlcjogUHJvcFR5cGVzLnN0cmluZyxcbiAgc3R5bGU6IFByb3BUeXBlcy5vYmplY3QsXG4gIGNsYXNzTmFtZTogUHJvcFR5cGVzLnN0cmluZyxcbiAgd2l0aG91dEVtcHR5T3B0aW9uOiBQcm9wVHlwZXMuYm9vbCxcbiAgZGVmYXVsdFZhbHVlOiBQcm9wVHlwZXMuYW55LFxuICBjYXNlU2Vuc2l0aXZlOiBQcm9wVHlwZXMuYm9vbCxcbiAgZ2V0RmlsdGVyOiBQcm9wVHlwZXMuZnVuY1xufTtcblxuU2VsZWN0RmlsdGVyLmRlZmF1bHRQcm9wcyA9IHtcbiAgZGVmYXVsdFZhbHVlOiAnJyxcbiAgZmlsdGVyU3RhdGU6IHt9LFxuICBjbGFzc05hbWU6ICcnLFxuICB3aXRob3V0RW1wdHlPcHRpb246IGZhbHNlLFxuICBjb21wYXJhdG9yOiBFUSxcbiAgY2FzZVNlbnNpdGl2ZTogdHJ1ZSxcbiAgaWQ6IG51bGxcbn07XG5cbmV4cG9ydCBkZWZhdWx0IFNlbGVjdEZpbHRlcjtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyAuL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZmlsdGVyL3NyYy9jb21wb25lbnRzL3NlbGVjdC5qcyIsIi8qIGVzbGludCByZWFjdC9yZXF1aXJlLWRlZmF1bHQtcHJvcHM6IDAgKi9cbi8qIGVzbGludCBuby1yZXR1cm4tYXNzaWduOiAwICovXG4vKiBlc2xpbnQgbm8tcGFyYW0tcmVhc3NpZ246IDAgKi9cbi8qIGVzbGludCByZWFjdC9uby11bnVzZWQtcHJvcC10eXBlczogMCAqL1xuaW1wb3J0IFJlYWN0LCB7IENvbXBvbmVudCB9IGZyb20gJ3JlYWN0JztcbmltcG9ydCBQcm9wVHlwZXMgZnJvbSAncHJvcC10eXBlcyc7XG5pbXBvcnQgeyBMSUtFLCBFUSB9IGZyb20gJy4uL2NvbXBhcmlzb24nO1xuaW1wb3J0IHsgRklMVEVSX1RZUEUgfSBmcm9tICcuLi9jb25zdCc7XG5cblxuZnVuY3Rpb24gb3B0aW9uc0VxdWFscyhjdXJyT3B0cywgcHJldk9wdHMpIHtcbiAgY29uc3Qga2V5cyA9IE9iamVjdC5rZXlzKGN1cnJPcHRzKTtcbiAgZm9yIChsZXQgaSA9IDA7IGkgPCBrZXlzLmxlbmd0aDsgaSArPSAxKSB7XG4gICAgaWYgKGN1cnJPcHRzW2tleXNbaV1dICE9PSBwcmV2T3B0c1trZXlzW2ldXSkge1xuICAgICAgcmV0dXJuIGZhbHNlO1xuICAgIH1cbiAgfVxuICByZXR1cm4gT2JqZWN0LmtleXMoY3Vyck9wdHMpLmxlbmd0aCA9PT0gT2JqZWN0LmtleXMocHJldk9wdHMpLmxlbmd0aDtcbn1cblxuY29uc3QgZ2V0U2VsZWN0aW9ucyA9IChjb250YWluZXIpID0+IHtcbiAgaWYgKGNvbnRhaW5lci5zZWxlY3RlZE9wdGlvbnMpIHtcbiAgICByZXR1cm4gQXJyYXkuZnJvbShjb250YWluZXIuc2VsZWN0ZWRPcHRpb25zKS5tYXAoaXRlbSA9PiBpdGVtLnZhbHVlKTtcbiAgfVxuICBjb25zdCBzZWxlY3Rpb25zID0gW107XG4gIGNvbnN0IHRvdGFsTGVuID0gY29udGFpbmVyLm9wdGlvbnMubGVuZ3RoO1xuICBmb3IgKGxldCBpID0gMDsgaSA8IHRvdGFsTGVuOyBpICs9IDEpIHtcbiAgICBjb25zdCBvcHRpb24gPSBjb250YWluZXIub3B0aW9ucy5pdGVtKGkpO1xuICAgIGlmIChvcHRpb24uc2VsZWN0ZWQpIHNlbGVjdGlvbnMucHVzaChvcHRpb24udmFsdWUpO1xuICB9XG4gIHJldHVybiBzZWxlY3Rpb25zO1xufTtcblxuY2xhc3MgTXVsdGlTZWxlY3RGaWx0ZXIgZXh0ZW5kcyBDb21wb25lbnQge1xuICBjb25zdHJ1Y3Rvcihwcm9wcykge1xuICAgIHN1cGVyKHByb3BzKTtcbiAgICB0aGlzLmZpbHRlciA9IHRoaXMuZmlsdGVyLmJpbmQodGhpcyk7XG4gICAgdGhpcy5hcHBseUZpbHRlciA9IHRoaXMuYXBwbHlGaWx0ZXIuYmluZCh0aGlzKTtcbiAgICBjb25zdCBpc1NlbGVjdGVkID0gcHJvcHMuZGVmYXVsdFZhbHVlLm1hcChpdGVtID0+IHByb3BzLm9wdGlvbnNbaXRlbV0pLmxlbmd0aCA+IDA7XG4gICAgdGhpcy5zdGF0ZSA9IHsgaXNTZWxlY3RlZCB9O1xuICB9XG5cbiAgY29tcG9uZW50RGlkTW91bnQoKSB7XG4gICAgY29uc3QgeyBnZXRGaWx0ZXIgfSA9IHRoaXMucHJvcHM7XG5cbiAgICBjb25zdCB2YWx1ZSA9IGdldFNlbGVjdGlvbnModGhpcy5zZWxlY3RJbnB1dCk7XG4gICAgaWYgKHZhbHVlICYmIHZhbHVlLmxlbmd0aCA+IDApIHtcbiAgICAgIHRoaXMuYXBwbHlGaWx0ZXIodmFsdWUpO1xuICAgIH1cblxuICAgIC8vIGV4cG9ydCBvbkZpbHRlciBmdW5jdGlvbiB0byBhbGxvdyB1c2VycyB0byBhY2Nlc3NcbiAgICBpZiAoZ2V0RmlsdGVyKSB7XG4gICAgICBnZXRGaWx0ZXIoKGZpbHRlclZhbCkgPT4ge1xuICAgICAgICB0aGlzLnNlbGVjdElucHV0LnZhbHVlID0gZmlsdGVyVmFsO1xuICAgICAgICB0aGlzLmFwcGx5RmlsdGVyKGZpbHRlclZhbCk7XG4gICAgICB9KTtcbiAgICB9XG4gIH1cblxuICBjb21wb25lbnREaWRVcGRhdGUocHJldlByb3BzKSB7XG4gICAgbGV0IG5lZWRGaWx0ZXIgPSBmYWxzZTtcbiAgICBpZiAodGhpcy5wcm9wcy5kZWZhdWx0VmFsdWUgIT09IHByZXZQcm9wcy5kZWZhdWx0VmFsdWUpIHtcbiAgICAgIG5lZWRGaWx0ZXIgPSB0cnVlO1xuICAgIH0gZWxzZSBpZiAoIW9wdGlvbnNFcXVhbHModGhpcy5wcm9wcy5vcHRpb25zLCBwcmV2UHJvcHMub3B0aW9ucykpIHtcbiAgICAgIG5lZWRGaWx0ZXIgPSB0cnVlO1xuICAgIH1cbiAgICBpZiAobmVlZEZpbHRlcikge1xuICAgICAgdGhpcy5hcHBseUZpbHRlcihnZXRTZWxlY3Rpb25zKHRoaXMuc2VsZWN0SW5wdXQpKTtcbiAgICB9XG4gIH1cblxuICBnZXREZWZhdWx0VmFsdWUoKSB7XG4gICAgY29uc3QgeyBmaWx0ZXJTdGF0ZSwgZGVmYXVsdFZhbHVlIH0gPSB0aGlzLnByb3BzO1xuICAgIGlmIChmaWx0ZXJTdGF0ZSAmJiB0eXBlb2YgZmlsdGVyU3RhdGUuZmlsdGVyVmFsICE9PSAndW5kZWZpbmVkJykge1xuICAgICAgcmV0dXJuIGZpbHRlclN0YXRlLmZpbHRlclZhbDtcbiAgICB9XG4gICAgcmV0dXJuIGRlZmF1bHRWYWx1ZTtcbiAgfVxuXG4gIGdldE9wdGlvbnMoKSB7XG4gICAgY29uc3Qgb3B0aW9uVGFncyA9IFtdO1xuICAgIGNvbnN0IHsgb3B0aW9ucywgcGxhY2Vob2xkZXIsIGNvbHVtbiwgd2l0aG91dEVtcHR5T3B0aW9uIH0gPSB0aGlzLnByb3BzO1xuICAgIGlmICghd2l0aG91dEVtcHR5T3B0aW9uKSB7XG4gICAgICBvcHRpb25UYWdzLnB1c2goKFxuICAgICAgICA8b3B0aW9uIGtleT1cIi0xXCIgdmFsdWU9XCJcIj57IHBsYWNlaG9sZGVyIHx8IGBTZWxlY3QgJHtjb2x1bW4udGV4dH0uLi5gIH08L29wdGlvbj5cbiAgICAgICkpO1xuICAgIH1cbiAgICBPYmplY3Qua2V5cyhvcHRpb25zKS5mb3JFYWNoKGtleSA9PlxuICAgICAgb3B0aW9uVGFncy5wdXNoKDxvcHRpb24ga2V5PXsga2V5IH0gdmFsdWU9eyBrZXkgfT57IG9wdGlvbnNba2V5XSB9PC9vcHRpb24+KVxuICAgICk7XG4gICAgcmV0dXJuIG9wdGlvblRhZ3M7XG4gIH1cblxuICBjbGVhbkZpbHRlcmVkKCkge1xuICAgIGNvbnN0IHZhbHVlID0gKHRoaXMucHJvcHMuZGVmYXVsdFZhbHVlICE9PSB1bmRlZmluZWQpID8gdGhpcy5wcm9wcy5kZWZhdWx0VmFsdWUgOiBbXTtcbiAgICB0aGlzLnNlbGVjdElucHV0LnZhbHVlID0gdmFsdWU7XG4gICAgdGhpcy5hcHBseUZpbHRlcih2YWx1ZSk7XG4gIH1cblxuICBhcHBseUZpbHRlcih2YWx1ZSkge1xuICAgIGlmICh2YWx1ZS5sZW5ndGggPT09IDEgJiYgdmFsdWVbMF0gPT09ICcnKSB7XG4gICAgICB2YWx1ZSA9IFtdO1xuICAgIH1cbiAgICB0aGlzLnNldFN0YXRlKCgpID0+ICh7IGlzU2VsZWN0ZWQ6IHZhbHVlLmxlbmd0aCA+IDAgfSkpO1xuICAgIHRoaXMucHJvcHMub25GaWx0ZXIodGhpcy5wcm9wcy5jb2x1bW4sIEZJTFRFUl9UWVBFLk1VTFRJU0VMRUNUKSh2YWx1ZSk7XG4gIH1cblxuICBmaWx0ZXIoZSkge1xuICAgIGNvbnN0IHZhbHVlID0gZ2V0U2VsZWN0aW9ucyhlLnRhcmdldCk7XG4gICAgdGhpcy5hcHBseUZpbHRlcih2YWx1ZSk7XG4gIH1cblxuICByZW5kZXIoKSB7XG4gICAgY29uc3Qge1xuICAgICAgaWQsXG4gICAgICBzdHlsZSxcbiAgICAgIGNsYXNzTmFtZSxcbiAgICAgIGZpbHRlclN0YXRlLFxuICAgICAgZGVmYXVsdFZhbHVlLFxuICAgICAgb25GaWx0ZXIsXG4gICAgICBjb2x1bW4sXG4gICAgICBvcHRpb25zLFxuICAgICAgY29tcGFyYXRvcixcbiAgICAgIHdpdGhvdXRFbXB0eU9wdGlvbixcbiAgICAgIGNhc2VTZW5zaXRpdmUsXG4gICAgICBnZXRGaWx0ZXIsXG4gICAgICAuLi5yZXN0XG4gICAgfSA9IHRoaXMucHJvcHM7XG5cbiAgICBjb25zdCBzZWxlY3RDbGFzcyA9XG4gICAgICBgZmlsdGVyIHNlbGVjdC1maWx0ZXIgZm9ybS1jb250cm9sICR7Y2xhc3NOYW1lfSAke3RoaXMuc3RhdGUuaXNTZWxlY3RlZCA/ICcnIDogJ3BsYWNlaG9sZGVyLXNlbGVjdGVkJ31gO1xuICAgIGNvbnN0IGVsbUlkID0gYG11bHRpc2VsZWN0LWZpbHRlci1jb2x1bW4tJHtjb2x1bW4uZGF0YUZpZWxkfSR7aWQgPyBgLSR7aWR9YCA6ICcnfWA7XG5cbiAgICByZXR1cm4gKFxuICAgICAgPGxhYmVsXG4gICAgICAgIGNsYXNzTmFtZT1cImZpbHRlci1sYWJlbFwiXG4gICAgICAgIGh0bWxGb3I9eyBlbG1JZCB9XG4gICAgICA+XG4gICAgICAgIDxzcGFuIGNsYXNzTmFtZT1cInNyLW9ubHlcIj5GaWx0ZXIgYnkge2NvbHVtbi50ZXh0fTwvc3Bhbj5cbiAgICAgICAgPHNlbGVjdFxuICAgICAgICAgIHsgLi4ucmVzdCB9XG4gICAgICAgICAgcmVmPXsgbiA9PiB0aGlzLnNlbGVjdElucHV0ID0gbiB9XG4gICAgICAgICAgaWQ9eyBlbG1JZCB9XG4gICAgICAgICAgc3R5bGU9eyBzdHlsZSB9XG4gICAgICAgICAgbXVsdGlwbGVcbiAgICAgICAgICBjbGFzc05hbWU9eyBzZWxlY3RDbGFzcyB9XG4gICAgICAgICAgb25DaGFuZ2U9eyB0aGlzLmZpbHRlciB9XG4gICAgICAgICAgb25DbGljaz17IGUgPT4gZS5zdG9wUHJvcGFnYXRpb24oKSB9XG4gICAgICAgICAgZGVmYXVsdFZhbHVlPXsgdGhpcy5nZXREZWZhdWx0VmFsdWUoKSB9XG4gICAgICAgID5cbiAgICAgICAgICB7IHRoaXMuZ2V0T3B0aW9ucygpIH1cbiAgICAgICAgPC9zZWxlY3Q+XG4gICAgICA8L2xhYmVsPlxuICAgICk7XG4gIH1cbn1cblxuTXVsdGlTZWxlY3RGaWx0ZXIucHJvcFR5cGVzID0ge1xuICBvbkZpbHRlcjogUHJvcFR5cGVzLmZ1bmMuaXNSZXF1aXJlZCxcbiAgY29sdW1uOiBQcm9wVHlwZXMub2JqZWN0LmlzUmVxdWlyZWQsXG4gIG9wdGlvbnM6IFByb3BUeXBlcy5vYmplY3QuaXNSZXF1aXJlZCxcbiAgaWQ6IFByb3BUeXBlcy5zdHJpbmcsXG4gIGZpbHRlclN0YXRlOiBQcm9wVHlwZXMub2JqZWN0LFxuICBjb21wYXJhdG9yOiBQcm9wVHlwZXMub25lT2YoW0xJS0UsIEVRXSksXG4gIHBsYWNlaG9sZGVyOiBQcm9wVHlwZXMuc3RyaW5nLFxuICBzdHlsZTogUHJvcFR5cGVzLm9iamVjdCxcbiAgY2xhc3NOYW1lOiBQcm9wVHlwZXMuc3RyaW5nLFxuICB3aXRob3V0RW1wdHlPcHRpb246IFByb3BUeXBlcy5ib29sLFxuICBkZWZhdWx0VmFsdWU6IFByb3BUeXBlcy5hcnJheSxcbiAgY2FzZVNlbnNpdGl2ZTogUHJvcFR5cGVzLmJvb2wsXG4gIGdldEZpbHRlcjogUHJvcFR5cGVzLmZ1bmNcbn07XG5cbk11bHRpU2VsZWN0RmlsdGVyLmRlZmF1bHRQcm9wcyA9IHtcbiAgZGVmYXVsdFZhbHVlOiBbXSxcbiAgZmlsdGVyU3RhdGU6IHt9LFxuICBjbGFzc05hbWU6ICcnLFxuICB3aXRob3V0RW1wdHlPcHRpb246IGZhbHNlLFxuICBjb21wYXJhdG9yOiBFUSxcbiAgY2FzZVNlbnNpdGl2ZTogdHJ1ZSxcbiAgaWQ6IG51bGxcbn07XG5cbmV4cG9ydCBkZWZhdWx0IE11bHRpU2VsZWN0RmlsdGVyO1xuXG5cblxuLy8gV0VCUEFDSyBGT09URVIgLy9cbi8vIC4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1maWx0ZXIvc3JjL2NvbXBvbmVudHMvbXVsdGlzZWxlY3QuanMiLCIvKiBlc2xpbnQganN4LWExMXkvbm8tc3RhdGljLWVsZW1lbnQtaW50ZXJhY3Rpb25zOiAwICovXG4vKiBlc2xpbnQgcmVhY3QvcmVxdWlyZS1kZWZhdWx0LXByb3BzOiAwICovXG4vKiBlc2xpbnQgbm8tcmV0dXJuLWFzc2lnbjogMCAqL1xuXG5pbXBvcnQgUmVhY3QsIHsgQ29tcG9uZW50IH0gZnJvbSAncmVhY3QnO1xuaW1wb3J0IFByb3BUeXBlcyBmcm9tICdwcm9wLXR5cGVzJztcbmltcG9ydCAqIGFzIENvbXBhcmF0b3IgZnJvbSAnLi4vY29tcGFyaXNvbic7XG5pbXBvcnQgeyBGSUxURVJfVFlQRSwgRklMVEVSX0RFTEFZIH0gZnJvbSAnLi4vY29uc3QnO1xuXG5jb25zdCBsZWdhbENvbXBhcmF0b3JzID0gW1xuICBDb21wYXJhdG9yLkVRLFxuICBDb21wYXJhdG9yLk5FLFxuICBDb21wYXJhdG9yLkdULFxuICBDb21wYXJhdG9yLkdFLFxuICBDb21wYXJhdG9yLkxULFxuICBDb21wYXJhdG9yLkxFXG5dO1xuXG5jbGFzcyBOdW1iZXJGaWx0ZXIgZXh0ZW5kcyBDb21wb25lbnQge1xuICBjb25zdHJ1Y3Rvcihwcm9wcykge1xuICAgIHN1cGVyKHByb3BzKTtcbiAgICB0aGlzLmNvbXBhcmF0b3JzID0gcHJvcHMuY29tcGFyYXRvcnMgfHwgbGVnYWxDb21wYXJhdG9ycztcbiAgICB0aGlzLnRpbWVvdXQgPSBudWxsO1xuICAgIGxldCBpc1NlbGVjdGVkID0gcHJvcHMuZGVmYXVsdFZhbHVlICE9PSB1bmRlZmluZWQgJiYgcHJvcHMuZGVmYXVsdFZhbHVlLm51bWJlciAhPT0gdW5kZWZpbmVkO1xuICAgIGlmIChwcm9wcy5vcHRpb25zICYmIGlzU2VsZWN0ZWQpIHtcbiAgICAgIGlzU2VsZWN0ZWQgPSBwcm9wcy5vcHRpb25zLmluZGV4T2YocHJvcHMuZGVmYXVsdFZhbHVlLm51bWJlcikgPiAtMTtcbiAgICB9XG4gICAgdGhpcy5zdGF0ZSA9IHsgaXNTZWxlY3RlZCB9O1xuICAgIHRoaXMub25DaGFuZ2VOdW1iZXIgPSB0aGlzLm9uQ2hhbmdlTnVtYmVyLmJpbmQodGhpcyk7XG4gICAgdGhpcy5vbkNoYW5nZU51bWJlclNldCA9IHRoaXMub25DaGFuZ2VOdW1iZXJTZXQuYmluZCh0aGlzKTtcbiAgICB0aGlzLm9uQ2hhbmdlQ29tcGFyYXRvciA9IHRoaXMub25DaGFuZ2VDb21wYXJhdG9yLmJpbmQodGhpcyk7XG4gIH1cblxuICBjb21wb25lbnREaWRNb3VudCgpIHtcbiAgICBjb25zdCB7IGNvbHVtbiwgb25GaWx0ZXIsIGdldEZpbHRlciB9ID0gdGhpcy5wcm9wcztcbiAgICBjb25zdCBjb21wYXJhdG9yID0gdGhpcy5udW1iZXJGaWx0ZXJDb21wYXJhdG9yLnZhbHVlO1xuICAgIGNvbnN0IG51bWJlciA9IHRoaXMubnVtYmVyRmlsdGVyLnZhbHVlO1xuICAgIGlmIChjb21wYXJhdG9yICYmIG51bWJlcikge1xuICAgICAgb25GaWx0ZXIoY29sdW1uLCBGSUxURVJfVFlQRS5OVU1CRVIsIHRydWUpKHsgbnVtYmVyLCBjb21wYXJhdG9yIH0pO1xuICAgIH1cblxuICAgIC8vIGV4cG9ydCBvbkZpbHRlciBmdW5jdGlvbiB0byBhbGxvdyB1c2VycyB0byBhY2Nlc3NcbiAgICBpZiAoZ2V0RmlsdGVyKSB7XG4gICAgICBnZXRGaWx0ZXIoKGZpbHRlclZhbCkgPT4ge1xuICAgICAgICB0aGlzLnNldFN0YXRlKCgpID0+ICh7IGlzU2VsZWN0ZWQ6IChmaWx0ZXJWYWwgIT09ICcnKSB9KSk7XG4gICAgICAgIHRoaXMubnVtYmVyRmlsdGVyQ29tcGFyYXRvci52YWx1ZSA9IGZpbHRlclZhbC5jb21wYXJhdG9yO1xuICAgICAgICB0aGlzLm51bWJlckZpbHRlci52YWx1ZSA9IGZpbHRlclZhbC5udW1iZXI7XG5cbiAgICAgICAgb25GaWx0ZXIoY29sdW1uLCBGSUxURVJfVFlQRS5OVU1CRVIpKHtcbiAgICAgICAgICBudW1iZXI6IGZpbHRlclZhbC5udW1iZXIsXG4gICAgICAgICAgY29tcGFyYXRvcjogZmlsdGVyVmFsLmNvbXBhcmF0b3JcbiAgICAgICAgfSk7XG4gICAgICB9KTtcbiAgICB9XG4gIH1cblxuICBjb21wb25lbnRXaWxsVW5tb3VudCgpIHtcbiAgICBjbGVhclRpbWVvdXQodGhpcy50aW1lb3V0KTtcbiAgfVxuXG4gIG9uQ2hhbmdlTnVtYmVyKGUpIHtcbiAgICBjb25zdCB7IGRlbGF5LCBjb2x1bW4sIG9uRmlsdGVyIH0gPSB0aGlzLnByb3BzO1xuICAgIGNvbnN0IGNvbXBhcmF0b3IgPSB0aGlzLm51bWJlckZpbHRlckNvbXBhcmF0b3IudmFsdWU7XG4gICAgaWYgKGNvbXBhcmF0b3IgPT09ICcnKSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuICAgIGlmICh0aGlzLnRpbWVvdXQpIHtcbiAgICAgIGNsZWFyVGltZW91dCh0aGlzLnRpbWVvdXQpO1xuICAgIH1cbiAgICBjb25zdCBmaWx0ZXJWYWx1ZSA9IGUudGFyZ2V0LnZhbHVlO1xuICAgIHRoaXMudGltZW91dCA9IHNldFRpbWVvdXQoKCkgPT4ge1xuICAgICAgb25GaWx0ZXIoY29sdW1uLCBGSUxURVJfVFlQRS5OVU1CRVIpKHsgbnVtYmVyOiBmaWx0ZXJWYWx1ZSwgY29tcGFyYXRvciB9KTtcbiAgICB9LCBkZWxheSk7XG4gIH1cblxuICBvbkNoYW5nZU51bWJlclNldChlKSB7XG4gICAgY29uc3QgeyBjb2x1bW4sIG9uRmlsdGVyIH0gPSB0aGlzLnByb3BzO1xuICAgIGNvbnN0IGNvbXBhcmF0b3IgPSB0aGlzLm51bWJlckZpbHRlckNvbXBhcmF0b3IudmFsdWU7XG4gICAgY29uc3QgeyB2YWx1ZSB9ID0gZS50YXJnZXQ7XG4gICAgdGhpcy5zZXRTdGF0ZSgoKSA9PiAoeyBpc1NlbGVjdGVkOiAodmFsdWUgIT09ICcnKSB9KSk7XG4gICAgLy8gaWYgKGNvbXBhcmF0b3IgPT09ICcnKSB7XG4gICAgLy8gICByZXR1cm47XG4gICAgLy8gfVxuICAgIG9uRmlsdGVyKGNvbHVtbiwgRklMVEVSX1RZUEUuTlVNQkVSKSh7IG51bWJlcjogdmFsdWUsIGNvbXBhcmF0b3IgfSk7XG4gIH1cblxuICBvbkNoYW5nZUNvbXBhcmF0b3IoZSkge1xuICAgIGNvbnN0IHsgY29sdW1uLCBvbkZpbHRlciB9ID0gdGhpcy5wcm9wcztcbiAgICBjb25zdCB2YWx1ZSA9IHRoaXMubnVtYmVyRmlsdGVyLnZhbHVlO1xuICAgIGNvbnN0IGNvbXBhcmF0b3IgPSBlLnRhcmdldC52YWx1ZTtcbiAgICAvLyBpZiAodmFsdWUgPT09ICcnKSB7XG4gICAgLy8gICByZXR1cm47XG4gICAgLy8gfVxuICAgIG9uRmlsdGVyKGNvbHVtbiwgRklMVEVSX1RZUEUuTlVNQkVSKSh7IG51bWJlcjogdmFsdWUsIGNvbXBhcmF0b3IgfSk7XG4gIH1cblxuICBnZXREZWZhdWx0Q29tcGFyYXRvcigpIHtcbiAgICBjb25zdCB7IGRlZmF1bHRWYWx1ZSwgZmlsdGVyU3RhdGUgfSA9IHRoaXMucHJvcHM7XG4gICAgaWYgKGZpbHRlclN0YXRlICYmIGZpbHRlclN0YXRlLmZpbHRlclZhbCkge1xuICAgICAgcmV0dXJuIGZpbHRlclN0YXRlLmZpbHRlclZhbC5jb21wYXJhdG9yO1xuICAgIH1cbiAgICBpZiAoZGVmYXVsdFZhbHVlICYmIGRlZmF1bHRWYWx1ZS5jb21wYXJhdG9yKSB7XG4gICAgICByZXR1cm4gZGVmYXVsdFZhbHVlLmNvbXBhcmF0b3I7XG4gICAgfVxuICAgIHJldHVybiAnJztcbiAgfVxuXG4gIGdldERlZmF1bHRWYWx1ZSgpIHtcbiAgICBjb25zdCB7IGRlZmF1bHRWYWx1ZSwgZmlsdGVyU3RhdGUgfSA9IHRoaXMucHJvcHM7XG4gICAgaWYgKGZpbHRlclN0YXRlICYmIGZpbHRlclN0YXRlLmZpbHRlclZhbCkge1xuICAgICAgcmV0dXJuIGZpbHRlclN0YXRlLmZpbHRlclZhbC5udW1iZXI7XG4gICAgfVxuICAgIGlmIChkZWZhdWx0VmFsdWUgJiYgZGVmYXVsdFZhbHVlLm51bWJlcikge1xuICAgICAgcmV0dXJuIGRlZmF1bHRWYWx1ZS5udW1iZXI7XG4gICAgfVxuICAgIHJldHVybiAnJztcbiAgfVxuXG4gIGdldENvbXBhcmF0b3JPcHRpb25zKCkge1xuICAgIGNvbnN0IG9wdGlvblRhZ3MgPSBbXTtcbiAgICBjb25zdCB7IHdpdGhvdXRFbXB0eUNvbXBhcmF0b3JPcHRpb24gfSA9IHRoaXMucHJvcHM7XG4gICAgaWYgKCF3aXRob3V0RW1wdHlDb21wYXJhdG9yT3B0aW9uKSB7XG4gICAgICBvcHRpb25UYWdzLnB1c2goPG9wdGlvbiBrZXk9XCItMVwiIC8+KTtcbiAgICB9XG4gICAgZm9yIChsZXQgaSA9IDA7IGkgPCB0aGlzLmNvbXBhcmF0b3JzLmxlbmd0aDsgaSArPSAxKSB7XG4gICAgICBvcHRpb25UYWdzLnB1c2goXG4gICAgICAgIDxvcHRpb24ga2V5PXsgaSB9IHZhbHVlPXsgdGhpcy5jb21wYXJhdG9yc1tpXSB9PlxuICAgICAgICAgIHsgdGhpcy5jb21wYXJhdG9yc1tpXSB9XG4gICAgICAgIDwvb3B0aW9uPlxuICAgICAgKTtcbiAgICB9XG4gICAgcmV0dXJuIG9wdGlvblRhZ3M7XG4gIH1cblxuICBnZXROdW1iZXJPcHRpb25zKCkge1xuICAgIGNvbnN0IG9wdGlvblRhZ3MgPSBbXTtcbiAgICBjb25zdCB7IG9wdGlvbnMsIGNvbHVtbiwgd2l0aG91dEVtcHR5TnVtYmVyT3B0aW9uIH0gPSB0aGlzLnByb3BzO1xuICAgIGlmICghd2l0aG91dEVtcHR5TnVtYmVyT3B0aW9uKSB7XG4gICAgICBvcHRpb25UYWdzLnB1c2goXG4gICAgICAgIDxvcHRpb24ga2V5PVwiLTFcIiB2YWx1ZT1cIlwiPlxuICAgICAgICAgIHsgdGhpcy5wcm9wcy5wbGFjZWhvbGRlciB8fCBgU2VsZWN0ICR7Y29sdW1uLnRleHR9Li4uYCB9XG4gICAgICAgIDwvb3B0aW9uPlxuICAgICAgKTtcbiAgICB9XG4gICAgZm9yIChsZXQgaSA9IDA7IGkgPCBvcHRpb25zLmxlbmd0aDsgaSArPSAxKSB7XG4gICAgICBvcHRpb25UYWdzLnB1c2goPG9wdGlvbiBrZXk9eyBpIH0gdmFsdWU9eyBvcHRpb25zW2ldIH0+eyBvcHRpb25zW2ldIH08L29wdGlvbj4pO1xuICAgIH1cbiAgICByZXR1cm4gb3B0aW9uVGFncztcbiAgfVxuXG4gIGFwcGx5RmlsdGVyKGZpbHRlck9iaikge1xuICAgIGNvbnN0IHsgY29sdW1uLCBvbkZpbHRlciB9ID0gdGhpcy5wcm9wcztcbiAgICBjb25zdCB7IG51bWJlciwgY29tcGFyYXRvciB9ID0gZmlsdGVyT2JqO1xuICAgIHRoaXMuc2V0U3RhdGUoKCkgPT4gKHsgaXNTZWxlY3RlZDogKG51bWJlciAhPT0gJycpIH0pKTtcbiAgICB0aGlzLm51bWJlckZpbHRlckNvbXBhcmF0b3IudmFsdWUgPSBjb21wYXJhdG9yO1xuICAgIHRoaXMubnVtYmVyRmlsdGVyLnZhbHVlID0gbnVtYmVyO1xuICAgIG9uRmlsdGVyKGNvbHVtbiwgRklMVEVSX1RZUEUuTlVNQkVSKSh7IG51bWJlciwgY29tcGFyYXRvciB9KTtcbiAgfVxuXG4gIGNsZWFuRmlsdGVyZWQoKSB7XG4gICAgY29uc3QgeyBjb2x1bW4sIG9uRmlsdGVyLCBkZWZhdWx0VmFsdWUgfSA9IHRoaXMucHJvcHM7XG4gICAgY29uc3QgdmFsdWUgPSBkZWZhdWx0VmFsdWUgPyBkZWZhdWx0VmFsdWUubnVtYmVyIDogJyc7XG4gICAgY29uc3QgY29tcGFyYXRvciA9IGRlZmF1bHRWYWx1ZSA/IGRlZmF1bHRWYWx1ZS5jb21wYXJhdG9yIDogJyc7XG4gICAgdGhpcy5zZXRTdGF0ZSgoKSA9PiAoeyBpc1NlbGVjdGVkOiAodmFsdWUgIT09ICcnKSB9KSk7XG4gICAgdGhpcy5udW1iZXJGaWx0ZXJDb21wYXJhdG9yLnZhbHVlID0gY29tcGFyYXRvcjtcbiAgICB0aGlzLm51bWJlckZpbHRlci52YWx1ZSA9IHZhbHVlO1xuICAgIG9uRmlsdGVyKGNvbHVtbiwgRklMVEVSX1RZUEUuTlVNQkVSKSh7IG51bWJlcjogdmFsdWUsIGNvbXBhcmF0b3IgfSk7XG4gIH1cblxuICByZW5kZXIoKSB7XG4gICAgY29uc3QgeyBpc1NlbGVjdGVkIH0gPSB0aGlzLnN0YXRlO1xuICAgIGNvbnN0IHtcbiAgICAgIGlkLFxuICAgICAgY29sdW1uLFxuICAgICAgb3B0aW9ucyxcbiAgICAgIHN0eWxlLFxuICAgICAgY2xhc3NOYW1lLFxuICAgICAgbnVtYmVyU3R5bGUsXG4gICAgICBudW1iZXJDbGFzc05hbWUsXG4gICAgICBjb21wYXJhdG9yU3R5bGUsXG4gICAgICBjb21wYXJhdG9yQ2xhc3NOYW1lLFxuICAgICAgcGxhY2Vob2xkZXJcbiAgICB9ID0gdGhpcy5wcm9wcztcbiAgICBjb25zdCBzZWxlY3RDbGFzcyA9IGBcbiAgICAgIHNlbGVjdC1maWx0ZXIgXG4gICAgICBudW1iZXItZmlsdGVyLWlucHV0IFxuICAgICAgZm9ybS1jb250cm9sIFxuICAgICAgJHtudW1iZXJDbGFzc05hbWV9IFxuICAgICAgJHshaXNTZWxlY3RlZCA/ICdwbGFjZWhvbGRlci1zZWxlY3RlZCcgOiAnJ31cbiAgICBgO1xuXG4gICAgY29uc3QgY29tcGFyYXRvckVsbUlkID0gYG51bWJlci1maWx0ZXItY29tcGFyYXRvci0ke2NvbHVtbi5kYXRhRmllbGR9JHtpZCA/IGAtJHtpZH1gIDogJyd9YDtcbiAgICBjb25zdCBpbnB1dEVsbUlkID0gYG51bWJlci1maWx0ZXItY29sdW1uLSR7Y29sdW1uLmRhdGFGaWVsZH0ke2lkID8gYC0ke2lkfWAgOiAnJ31gO1xuXG4gICAgcmV0dXJuIChcbiAgICAgIDxkaXZcbiAgICAgICAgb25DbGljaz17IGUgPT4gZS5zdG9wUHJvcGFnYXRpb24oKSB9XG4gICAgICAgIGNsYXNzTmFtZT17IGBmaWx0ZXIgbnVtYmVyLWZpbHRlciAke2NsYXNzTmFtZX1gIH1cbiAgICAgICAgc3R5bGU9eyBzdHlsZSB9XG4gICAgICA+XG4gICAgICAgIDxsYWJlbFxuICAgICAgICAgIGNsYXNzTmFtZT1cImZpbHRlci1sYWJlbFwiXG4gICAgICAgICAgaHRtbEZvcj17IGNvbXBhcmF0b3JFbG1JZCB9XG4gICAgICAgID5cbiAgICAgICAgICA8c3BhbiBjbGFzc05hbWU9XCJzci1vbmx5XCI+RmlsdGVyIGNvbXBhcmF0b3I8L3NwYW4+XG4gICAgICAgICAgPHNlbGVjdFxuICAgICAgICAgICAgcmVmPXsgbiA9PiB0aGlzLm51bWJlckZpbHRlckNvbXBhcmF0b3IgPSBuIH1cbiAgICAgICAgICAgIHN0eWxlPXsgY29tcGFyYXRvclN0eWxlIH1cbiAgICAgICAgICAgIGlkPXsgY29tcGFyYXRvckVsbUlkIH1cbiAgICAgICAgICAgIGNsYXNzTmFtZT17IGBudW1iZXItZmlsdGVyLWNvbXBhcmF0b3IgZm9ybS1jb250cm9sICR7Y29tcGFyYXRvckNsYXNzTmFtZX1gIH1cbiAgICAgICAgICAgIG9uQ2hhbmdlPXsgdGhpcy5vbkNoYW5nZUNvbXBhcmF0b3IgfVxuICAgICAgICAgICAgZGVmYXVsdFZhbHVlPXsgdGhpcy5nZXREZWZhdWx0Q29tcGFyYXRvcigpIH1cbiAgICAgICAgICA+XG4gICAgICAgICAgICB7IHRoaXMuZ2V0Q29tcGFyYXRvck9wdGlvbnMoKSB9XG4gICAgICAgICAgPC9zZWxlY3Q+XG4gICAgICAgIDwvbGFiZWw+XG4gICAgICAgIHtcbiAgICAgICAgICBvcHRpb25zID9cbiAgICAgICAgICAgIDxsYWJlbFxuICAgICAgICAgICAgICBjbGFzc05hbWU9XCJmaWx0ZXItbGFiZWxcIlxuICAgICAgICAgICAgICBodG1sRm9yPXsgaW5wdXRFbG1JZCB9XG4gICAgICAgICAgICA+XG4gICAgICAgICAgICAgIDxzcGFuIGNsYXNzTmFtZT1cInNyLW9ubHlcIj57YFNlbGVjdCAke2NvbHVtbi50ZXh0fWB9PC9zcGFuPlxuICAgICAgICAgICAgICA8c2VsZWN0XG4gICAgICAgICAgICAgICAgcmVmPXsgbiA9PiB0aGlzLm51bWJlckZpbHRlciA9IG4gfVxuICAgICAgICAgICAgICAgIGlkPXsgaW5wdXRFbG1JZCB9XG4gICAgICAgICAgICAgICAgc3R5bGU9eyBudW1iZXJTdHlsZSB9XG4gICAgICAgICAgICAgICAgY2xhc3NOYW1lPXsgc2VsZWN0Q2xhc3MgfVxuICAgICAgICAgICAgICAgIG9uQ2hhbmdlPXsgdGhpcy5vbkNoYW5nZU51bWJlclNldCB9XG4gICAgICAgICAgICAgICAgZGVmYXVsdFZhbHVlPXsgdGhpcy5nZXREZWZhdWx0VmFsdWUoKSB9XG4gICAgICAgICAgICAgID5cbiAgICAgICAgICAgICAgICB7IHRoaXMuZ2V0TnVtYmVyT3B0aW9ucygpIH1cbiAgICAgICAgICAgICAgPC9zZWxlY3Q+XG4gICAgICAgICAgICA8L2xhYmVsPiA6XG4gICAgICAgICAgICA8bGFiZWwgaHRtbEZvcj17IGlucHV0RWxtSWQgfT5cbiAgICAgICAgICAgICAgPHNwYW4gY2xhc3NOYW1lPVwic3Itb25seVwiPntgRW50ZXIgJHtjb2x1bW4udGV4dH1gfTwvc3Bhbj5cbiAgICAgICAgICAgICAgPGlucHV0XG4gICAgICAgICAgICAgICAgcmVmPXsgbiA9PiB0aGlzLm51bWJlckZpbHRlciA9IG4gfVxuICAgICAgICAgICAgICAgIGlkPXsgaW5wdXRFbG1JZCB9XG4gICAgICAgICAgICAgICAgdHlwZT1cIm51bWJlclwiXG4gICAgICAgICAgICAgICAgc3R5bGU9eyBudW1iZXJTdHlsZSB9XG4gICAgICAgICAgICAgICAgY2xhc3NOYW1lPXsgYG51bWJlci1maWx0ZXItaW5wdXQgZm9ybS1jb250cm9sICR7bnVtYmVyQ2xhc3NOYW1lfWAgfVxuICAgICAgICAgICAgICAgIHBsYWNlaG9sZGVyPXsgcGxhY2Vob2xkZXIgfHwgYEVudGVyICR7Y29sdW1uLnRleHR9Li4uYCB9XG4gICAgICAgICAgICAgICAgb25DaGFuZ2U9eyB0aGlzLm9uQ2hhbmdlTnVtYmVyIH1cbiAgICAgICAgICAgICAgICBkZWZhdWx0VmFsdWU9eyB0aGlzLmdldERlZmF1bHRWYWx1ZSgpIH1cbiAgICAgICAgICAgICAgLz5cbiAgICAgICAgICAgIDwvbGFiZWw+XG4gICAgICAgIH1cbiAgICAgIDwvZGl2PlxuICAgICk7XG4gIH1cbn1cblxuTnVtYmVyRmlsdGVyLnByb3BUeXBlcyA9IHtcbiAgb25GaWx0ZXI6IFByb3BUeXBlcy5mdW5jLmlzUmVxdWlyZWQsXG4gIGNvbHVtbjogUHJvcFR5cGVzLm9iamVjdC5pc1JlcXVpcmVkLFxuICBpZDogUHJvcFR5cGVzLnN0cmluZyxcbiAgZmlsdGVyU3RhdGU6IFByb3BUeXBlcy5vYmplY3QsXG4gIG9wdGlvbnM6IFByb3BUeXBlcy5hcnJheU9mKFByb3BUeXBlcy5udW1iZXIpLFxuICBkZWZhdWx0VmFsdWU6IFByb3BUeXBlcy5zaGFwZSh7XG4gICAgbnVtYmVyOiBQcm9wVHlwZXMub25lT2ZUeXBlKFtQcm9wVHlwZXMuc3RyaW5nLCBQcm9wVHlwZXMubnVtYmVyXSksXG4gICAgY29tcGFyYXRvcjogUHJvcFR5cGVzLm9uZU9mKFsuLi5sZWdhbENvbXBhcmF0b3JzLCAnJ10pXG4gIH0pLFxuICBkZWxheTogUHJvcFR5cGVzLm51bWJlcixcbiAgLyogZXNsaW50IGNvbnNpc3RlbnQtcmV0dXJuOiAwICovXG4gIGNvbXBhcmF0b3JzOiAocHJvcHMsIHByb3BOYW1lKSA9PiB7XG4gICAgaWYgKCFwcm9wc1twcm9wTmFtZV0pIHtcbiAgICAgIHJldHVybjtcbiAgICB9XG4gICAgZm9yIChsZXQgaSA9IDA7IGkgPCBwcm9wc1twcm9wTmFtZV0ubGVuZ3RoOyBpICs9IDEpIHtcbiAgICAgIGxldCBjb21wYXJhdG9ySXNWYWxpZCA9IGZhbHNlO1xuICAgICAgZm9yIChsZXQgaiA9IDA7IGogPCBsZWdhbENvbXBhcmF0b3JzLmxlbmd0aDsgaiArPSAxKSB7XG4gICAgICAgIGlmIChsZWdhbENvbXBhcmF0b3JzW2pdID09PSBwcm9wc1twcm9wTmFtZV1baV0gfHwgcHJvcHNbcHJvcE5hbWVdW2ldID09PSAnJykge1xuICAgICAgICAgIGNvbXBhcmF0b3JJc1ZhbGlkID0gdHJ1ZTtcbiAgICAgICAgICBicmVhaztcbiAgICAgICAgfVxuICAgICAgfVxuICAgICAgaWYgKCFjb21wYXJhdG9ySXNWYWxpZCkge1xuICAgICAgICByZXR1cm4gbmV3IEVycm9yKGBOdW1iZXIgY29tcGFyYXRvciBwcm92aWRlZCBpcyBub3Qgc3VwcG9ydGVkLlxuICAgICAgICAgIFVzZSBvbmx5ICR7bGVnYWxDb21wYXJhdG9yc31gKTtcbiAgICAgIH1cbiAgICB9XG4gIH0sXG4gIHBsYWNlaG9sZGVyOiBQcm9wVHlwZXMuc3RyaW5nLFxuICB3aXRob3V0RW1wdHlDb21wYXJhdG9yT3B0aW9uOiBQcm9wVHlwZXMuYm9vbCxcbiAgd2l0aG91dEVtcHR5TnVtYmVyT3B0aW9uOiBQcm9wVHlwZXMuYm9vbCxcbiAgc3R5bGU6IFByb3BUeXBlcy5vYmplY3QsXG4gIGNsYXNzTmFtZTogUHJvcFR5cGVzLnN0cmluZyxcbiAgY29tcGFyYXRvclN0eWxlOiBQcm9wVHlwZXMub2JqZWN0LFxuICBjb21wYXJhdG9yQ2xhc3NOYW1lOiBQcm9wVHlwZXMuc3RyaW5nLFxuICBudW1iZXJTdHlsZTogUHJvcFR5cGVzLm9iamVjdCxcbiAgbnVtYmVyQ2xhc3NOYW1lOiBQcm9wVHlwZXMuc3RyaW5nLFxuICBnZXRGaWx0ZXI6IFByb3BUeXBlcy5mdW5jXG59O1xuXG5OdW1iZXJGaWx0ZXIuZGVmYXVsdFByb3BzID0ge1xuICBkZWxheTogRklMVEVSX0RFTEFZLFxuICBvcHRpb25zOiB1bmRlZmluZWQsXG4gIGRlZmF1bHRWYWx1ZToge1xuICAgIG51bWJlcjogdW5kZWZpbmVkLFxuICAgIGNvbXBhcmF0b3I6ICcnXG4gIH0sXG4gIGZpbHRlclN0YXRlOiB7fSxcbiAgd2l0aG91dEVtcHR5Q29tcGFyYXRvck9wdGlvbjogZmFsc2UsXG4gIHdpdGhvdXRFbXB0eU51bWJlck9wdGlvbjogZmFsc2UsXG4gIGNvbXBhcmF0b3JzOiBsZWdhbENvbXBhcmF0b3JzLFxuICBwbGFjZWhvbGRlcjogdW5kZWZpbmVkLFxuICBzdHlsZTogdW5kZWZpbmVkLFxuICBjbGFzc05hbWU6ICcnLFxuICBjb21wYXJhdG9yU3R5bGU6IHVuZGVmaW5lZCxcbiAgY29tcGFyYXRvckNsYXNzTmFtZTogJycsXG4gIG51bWJlclN0eWxlOiB1bmRlZmluZWQsXG4gIG51bWJlckNsYXNzTmFtZTogJycsXG4gIGlkOiBudWxsXG59O1xuXG5leHBvcnQgZGVmYXVsdCBOdW1iZXJGaWx0ZXI7XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLWZpbHRlci9zcmMvY29tcG9uZW50cy9udW1iZXIuanMiLCIvKiBlc2xpbnQgcmVhY3QvcmVxdWlyZS1kZWZhdWx0LXByb3BzOiAwICovXG4vKiBlc2xpbnQganN4LWExMXkvbm8tc3RhdGljLWVsZW1lbnQtaW50ZXJhY3Rpb25zOiAwICovXG4vKiBlc2xpbnQgbm8tcmV0dXJuLWFzc2lnbjogMCAqL1xuLyogZXNsaW50IHByZWZlci10ZW1wbGF0ZTogMCAqL1xuaW1wb3J0IFJlYWN0LCB7IENvbXBvbmVudCB9IGZyb20gJ3JlYWN0JztcbmltcG9ydCB7IFByb3BUeXBlcyB9IGZyb20gJ3Byb3AtdHlwZXMnO1xuXG5pbXBvcnQgKiBhcyBDb21wYXJhdG9yIGZyb20gJy4uL2NvbXBhcmlzb24nO1xuaW1wb3J0IHsgRklMVEVSX1RZUEUgfSBmcm9tICcuLi9jb25zdCc7XG5cbmNvbnN0IGxlZ2FsQ29tcGFyYXRvcnMgPSBbXG4gIENvbXBhcmF0b3IuRVEsXG4gIENvbXBhcmF0b3IuTkUsXG4gIENvbXBhcmF0b3IuR1QsXG4gIENvbXBhcmF0b3IuR0UsXG4gIENvbXBhcmF0b3IuTFQsXG4gIENvbXBhcmF0b3IuTEVcbl07XG5cbmZ1bmN0aW9uIGRhdGVQYXJzZXIoZCkge1xuICByZXR1cm4gYCR7ZC5nZXRVVENGdWxsWWVhcigpfS0keygnMCcgKyAoZC5nZXRVVENNb250aCgpICsgMSkpLnNsaWNlKC0yKX0tJHsoJzAnICsgZC5nZXRVVENEYXRlKCkpLnNsaWNlKC0yKX1gO1xufVxuXG5jbGFzcyBEYXRlRmlsdGVyIGV4dGVuZHMgQ29tcG9uZW50IHtcbiAgY29uc3RydWN0b3IocHJvcHMpIHtcbiAgICBzdXBlcihwcm9wcyk7XG4gICAgdGhpcy50aW1lb3V0ID0gbnVsbDtcbiAgICB0aGlzLmNvbXBhcmF0b3JzID0gcHJvcHMuY29tcGFyYXRvcnMgfHwgbGVnYWxDb21wYXJhdG9ycztcbiAgICB0aGlzLmFwcGx5RmlsdGVyID0gdGhpcy5hcHBseUZpbHRlci5iaW5kKHRoaXMpO1xuICAgIHRoaXMub25DaGFuZ2VEYXRlID0gdGhpcy5vbkNoYW5nZURhdGUuYmluZCh0aGlzKTtcbiAgICB0aGlzLm9uQ2hhbmdlQ29tcGFyYXRvciA9IHRoaXMub25DaGFuZ2VDb21wYXJhdG9yLmJpbmQodGhpcyk7XG4gIH1cblxuICBjb21wb25lbnREaWRNb3VudCgpIHtcbiAgICBjb25zdCB7IGdldEZpbHRlciB9ID0gdGhpcy5wcm9wcztcbiAgICBjb25zdCBjb21wYXJhdG9yID0gdGhpcy5kYXRlRmlsdGVyQ29tcGFyYXRvci52YWx1ZTtcbiAgICBjb25zdCBkYXRlID0gdGhpcy5pbnB1dERhdGUudmFsdWU7XG4gICAgaWYgKGNvbXBhcmF0b3IgJiYgZGF0ZSkge1xuICAgICAgdGhpcy5hcHBseUZpbHRlcihkYXRlLCBjb21wYXJhdG9yLCB0cnVlKTtcbiAgICB9XG5cbiAgICAvLyBleHBvcnQgb25GaWx0ZXIgZnVuY3Rpb24gdG8gYWxsb3cgdXNlcnMgdG8gYWNjZXNzXG4gICAgaWYgKGdldEZpbHRlcikge1xuICAgICAgZ2V0RmlsdGVyKChmaWx0ZXJWYWwpID0+IHtcbiAgICAgICAgY29uc3QgbnVsbGFibGVGaWx0ZXJWYWwgPSBmaWx0ZXJWYWwgfHwgeyBkYXRlOiBudWxsLCBjb21wYXJhdG9yOiBudWxsIH07XG4gICAgICAgIHRoaXMuZGF0ZUZpbHRlckNvbXBhcmF0b3IudmFsdWUgPSBudWxsYWJsZUZpbHRlclZhbC5jb21wYXJhdG9yO1xuICAgICAgICB0aGlzLmlucHV0RGF0ZS52YWx1ZSA9IG51bGxhYmxlRmlsdGVyVmFsLmRhdGUgPyBkYXRlUGFyc2VyKG51bGxhYmxlRmlsdGVyVmFsLmRhdGUpIDogbnVsbDtcblxuICAgICAgICB0aGlzLmFwcGx5RmlsdGVyKG51bGxhYmxlRmlsdGVyVmFsLmRhdGUsIG51bGxhYmxlRmlsdGVyVmFsLmNvbXBhcmF0b3IpO1xuICAgICAgfSk7XG4gICAgfVxuICB9XG5cbiAgY29tcG9uZW50V2lsbFVubW91bnQoKSB7XG4gICAgaWYgKHRoaXMudGltZW91dCkgY2xlYXJUaW1lb3V0KHRoaXMudGltZW91dCk7XG4gIH1cblxuICBvbkNoYW5nZURhdGUoZSkge1xuICAgIGNvbnN0IGNvbXBhcmF0b3IgPSB0aGlzLmRhdGVGaWx0ZXJDb21wYXJhdG9yLnZhbHVlO1xuICAgIGNvbnN0IGZpbHRlclZhbHVlID0gZS50YXJnZXQudmFsdWU7XG4gICAgdGhpcy5hcHBseUZpbHRlcihmaWx0ZXJWYWx1ZSwgY29tcGFyYXRvcik7XG4gIH1cblxuICBvbkNoYW5nZUNvbXBhcmF0b3IoZSkge1xuICAgIGNvbnN0IHZhbHVlID0gdGhpcy5pbnB1dERhdGUudmFsdWU7XG4gICAgY29uc3QgY29tcGFyYXRvciA9IGUudGFyZ2V0LnZhbHVlO1xuICAgIHRoaXMuYXBwbHlGaWx0ZXIodmFsdWUsIGNvbXBhcmF0b3IpO1xuICB9XG5cbiAgZ2V0Q29tcGFyYXRvck9wdGlvbnMoKSB7XG4gICAgY29uc3Qgb3B0aW9uVGFncyA9IFtdO1xuICAgIGNvbnN0IHsgd2l0aG91dEVtcHR5Q29tcGFyYXRvck9wdGlvbiB9ID0gdGhpcy5wcm9wcztcbiAgICBpZiAoIXdpdGhvdXRFbXB0eUNvbXBhcmF0b3JPcHRpb24pIHtcbiAgICAgIG9wdGlvblRhZ3MucHVzaCg8b3B0aW9uIGtleT1cIi0xXCIgLz4pO1xuICAgIH1cbiAgICBmb3IgKGxldCBpID0gMDsgaSA8IHRoaXMuY29tcGFyYXRvcnMubGVuZ3RoOyBpICs9IDEpIHtcbiAgICAgIG9wdGlvblRhZ3MucHVzaChcbiAgICAgICAgPG9wdGlvbiBrZXk9eyBpIH0gdmFsdWU9eyB0aGlzLmNvbXBhcmF0b3JzW2ldIH0+XG4gICAgICAgICAgeyB0aGlzLmNvbXBhcmF0b3JzW2ldIH1cbiAgICAgICAgPC9vcHRpb24+XG4gICAgICApO1xuICAgIH1cbiAgICByZXR1cm4gb3B0aW9uVGFncztcbiAgfVxuXG4gIGdldERlZmF1bHRDb21wYXJhdG9yKCkge1xuICAgIGNvbnN0IHsgZGVmYXVsdFZhbHVlLCBmaWx0ZXJTdGF0ZSB9ID0gdGhpcy5wcm9wcztcbiAgICBpZiAoZmlsdGVyU3RhdGUgJiYgZmlsdGVyU3RhdGUuZmlsdGVyVmFsKSB7XG4gICAgICByZXR1cm4gZmlsdGVyU3RhdGUuZmlsdGVyVmFsLmNvbXBhcmF0b3I7XG4gICAgfVxuICAgIGlmIChkZWZhdWx0VmFsdWUgJiYgZGVmYXVsdFZhbHVlLmNvbXBhcmF0b3IpIHtcbiAgICAgIHJldHVybiBkZWZhdWx0VmFsdWUuY29tcGFyYXRvcjtcbiAgICB9XG4gICAgcmV0dXJuICcnO1xuICB9XG5cbiAgZ2V0RGVmYXVsdERhdGUoKSB7XG4gICAgLy8gU2V0IHRoZSBhcHByb3ByaWF0ZSBmb3JtYXQgZm9yIHRoZSBpbnB1dCB0eXBlPWRhdGUsIGkuZS4gXCJZWVlZLU1NLUREXCJcbiAgICBjb25zdCB7IGRlZmF1bHRWYWx1ZSwgZmlsdGVyU3RhdGUgfSA9IHRoaXMucHJvcHM7XG4gICAgaWYgKGZpbHRlclN0YXRlICYmIGZpbHRlclN0YXRlLmZpbHRlclZhbCAmJiBmaWx0ZXJTdGF0ZS5maWx0ZXJWYWwuZGF0ZSkge1xuICAgICAgcmV0dXJuIGRhdGVQYXJzZXIoZmlsdGVyU3RhdGUuZmlsdGVyVmFsLmRhdGUpO1xuICAgIH1cbiAgICBpZiAoZGVmYXVsdFZhbHVlICYmIGRlZmF1bHRWYWx1ZS5kYXRlKSB7XG4gICAgICByZXR1cm4gZGF0ZVBhcnNlcihuZXcgRGF0ZShkZWZhdWx0VmFsdWUuZGF0ZSkpO1xuICAgIH1cbiAgICByZXR1cm4gJyc7XG4gIH1cblxuICBhcHBseUZpbHRlcih2YWx1ZSwgY29tcGFyYXRvciwgaXNJbml0aWFsKSB7XG4gICAgLy8gaWYgKCFjb21wYXJhdG9yIHx8ICF2YWx1ZSkge1xuICAgIC8vICByZXR1cm47XG4gICAgLy8gfVxuICAgIGNvbnN0IHsgY29sdW1uLCBvbkZpbHRlciwgZGVsYXkgfSA9IHRoaXMucHJvcHM7XG4gICAgY29uc3QgZXhlY3V0ZSA9ICgpID0+IHtcbiAgICAgIC8vIEluY29taW5nIHZhbHVlIHNob3VsZCBhbHdheXMgYmUgYSBzdHJpbmcsIGFuZCB0aGUgZGVmYXVsdERhdGVcbiAgICAgIC8vIGFib3ZlIGlzIGltcGxlbWVudGVkIGFzIGFuIGVtcHR5IHN0cmluZywgc28gd2UgY2FuIGp1c3QgY2hlY2sgZm9yIHRoYXQuXG4gICAgICAvLyBpbnN0ZWFkIG9mIHBhcnNpbmcgYW4gaW52YWxpZCBEYXRlLiBUaGUgZmlsdGVyIGZ1bmN0aW9uIHdpbGwgaW50ZXJwcmV0XG4gICAgICAvLyBudWxsIGFzIGFuIGVtcHR5IGRhdGUgZmllbGRcbiAgICAgIGNvbnN0IGRhdGUgPSB2YWx1ZSA9PT0gJycgPyBudWxsIDogbmV3IERhdGUodmFsdWUpO1xuICAgICAgb25GaWx0ZXIoY29sdW1uLCBGSUxURVJfVFlQRS5EQVRFLCBpc0luaXRpYWwpKHsgZGF0ZSwgY29tcGFyYXRvciB9KTtcbiAgICB9O1xuICAgIGlmIChkZWxheSkge1xuICAgICAgdGhpcy50aW1lb3V0ID0gc2V0VGltZW91dCgoKSA9PiB7IGV4ZWN1dGUoKTsgfSwgZGVsYXkpO1xuICAgIH0gZWxzZSB7XG4gICAgICBleGVjdXRlKCk7XG4gICAgfVxuICB9XG5cbiAgcmVuZGVyKCkge1xuICAgIGNvbnN0IHtcbiAgICAgIGlkLFxuICAgICAgcGxhY2Vob2xkZXIsXG4gICAgICBjb2x1bW46IHsgZGF0YUZpZWxkLCB0ZXh0IH0sXG4gICAgICBzdHlsZSxcbiAgICAgIGNvbXBhcmF0b3JTdHlsZSxcbiAgICAgIGRhdGVTdHlsZSxcbiAgICAgIGNsYXNzTmFtZSxcbiAgICAgIGNvbXBhcmF0b3JDbGFzc05hbWUsXG4gICAgICBkYXRlQ2xhc3NOYW1lXG4gICAgfSA9IHRoaXMucHJvcHM7XG5cbiAgICBjb25zdCBjb21wYXJhdG9yRWxtSWQgPSBgZGF0ZS1maWx0ZXItY29tcGFyYXRvci0ke2RhdGFGaWVsZH0ke2lkID8gYC0ke2lkfWAgOiAnJ31gO1xuICAgIGNvbnN0IGlucHV0RWxtSWQgPSBgZGF0ZS1maWx0ZXItY29sdW1uLSR7ZGF0YUZpZWxkfSR7aWQgPyBgLSR7aWR9YCA6ICcnfWA7XG5cbiAgICByZXR1cm4gKFxuICAgICAgPGRpdlxuICAgICAgICBvbkNsaWNrPXsgZSA9PiBlLnN0b3BQcm9wYWdhdGlvbigpIH1cbiAgICAgICAgY2xhc3NOYW1lPXsgYGZpbHRlciBkYXRlLWZpbHRlciAke2NsYXNzTmFtZX1gIH1cbiAgICAgICAgc3R5bGU9eyBzdHlsZSB9XG4gICAgICA+XG4gICAgICAgIDxsYWJlbFxuICAgICAgICAgIGNsYXNzTmFtZT1cImZpbHRlci1sYWJlbFwiXG4gICAgICAgICAgaHRtbEZvcj17IGNvbXBhcmF0b3JFbG1JZCB9XG4gICAgICAgID5cbiAgICAgICAgICA8c3BhbiBjbGFzc05hbWU9XCJzci1vbmx5XCI+RmlsdGVyIGNvbXBhcmF0b3I8L3NwYW4+XG4gICAgICAgICAgPHNlbGVjdFxuICAgICAgICAgICAgcmVmPXsgbiA9PiB0aGlzLmRhdGVGaWx0ZXJDb21wYXJhdG9yID0gbiB9XG4gICAgICAgICAgICBpZD17IGNvbXBhcmF0b3JFbG1JZCB9XG4gICAgICAgICAgICBzdHlsZT17IGNvbXBhcmF0b3JTdHlsZSB9XG4gICAgICAgICAgICBjbGFzc05hbWU9eyBgZGF0ZS1maWx0ZXItY29tcGFyYXRvciBmb3JtLWNvbnRyb2wgJHtjb21wYXJhdG9yQ2xhc3NOYW1lfWAgfVxuICAgICAgICAgICAgb25DaGFuZ2U9eyB0aGlzLm9uQ2hhbmdlQ29tcGFyYXRvciB9XG4gICAgICAgICAgICBkZWZhdWx0VmFsdWU9eyB0aGlzLmdldERlZmF1bHRDb21wYXJhdG9yKCkgfVxuICAgICAgICAgID5cbiAgICAgICAgICAgIHsgdGhpcy5nZXRDb21wYXJhdG9yT3B0aW9ucygpIH1cbiAgICAgICAgICA8L3NlbGVjdD5cbiAgICAgICAgPC9sYWJlbD5cbiAgICAgICAgPGxhYmVsIGh0bWxGb3I9eyBpbnB1dEVsbUlkIH0+XG4gICAgICAgICAgPHNwYW4gY2xhc3NOYW1lPVwic3Itb25seVwiPkVudGVyICR7IHRleHQgfTwvc3Bhbj5cbiAgICAgICAgICA8aW5wdXRcbiAgICAgICAgICAgIHJlZj17IG4gPT4gdGhpcy5pbnB1dERhdGUgPSBuIH1cbiAgICAgICAgICAgIGlkPXsgaW5wdXRFbG1JZCB9XG4gICAgICAgICAgICBjbGFzc05hbWU9eyBgZmlsdGVyIGRhdGUtZmlsdGVyLWlucHV0IGZvcm0tY29udHJvbCAke2RhdGVDbGFzc05hbWV9YCB9XG4gICAgICAgICAgICBzdHlsZT17IGRhdGVTdHlsZSB9XG4gICAgICAgICAgICB0eXBlPVwiZGF0ZVwiXG4gICAgICAgICAgICBvbkNoYW5nZT17IHRoaXMub25DaGFuZ2VEYXRlIH1cbiAgICAgICAgICAgIHBsYWNlaG9sZGVyPXsgcGxhY2Vob2xkZXIgfHwgYEVudGVyICR7dGV4dH0uLi5gIH1cbiAgICAgICAgICAgIGRlZmF1bHRWYWx1ZT17IHRoaXMuZ2V0RGVmYXVsdERhdGUoKSB9XG4gICAgICAgICAgLz5cbiAgICAgICAgPC9sYWJlbD5cbiAgICAgIDwvZGl2PlxuICAgICk7XG4gIH1cbn1cblxuRGF0ZUZpbHRlci5wcm9wVHlwZXMgPSB7XG4gIG9uRmlsdGVyOiBQcm9wVHlwZXMuZnVuYy5pc1JlcXVpcmVkLFxuICBjb2x1bW46IFByb3BUeXBlcy5vYmplY3QuaXNSZXF1aXJlZCxcbiAgaWQ6IFByb3BUeXBlcy5zdHJpbmcsXG4gIGZpbHRlclN0YXRlOiBQcm9wVHlwZXMub2JqZWN0LFxuICBkZWxheTogUHJvcFR5cGVzLm51bWJlcixcbiAgZGVmYXVsdFZhbHVlOiBQcm9wVHlwZXMuc2hhcGUoe1xuICAgIGRhdGU6IFByb3BUeXBlcy5vbmVPZlR5cGUoW1Byb3BUeXBlcy5vYmplY3RdKSxcbiAgICBjb21wYXJhdG9yOiBQcm9wVHlwZXMub25lT2YoWy4uLmxlZ2FsQ29tcGFyYXRvcnMsICcnXSlcbiAgfSksXG4gIC8qIGVzbGludCBjb25zaXN0ZW50LXJldHVybjogMCAqL1xuICBjb21wYXJhdG9yczogKHByb3BzLCBwcm9wTmFtZSkgPT4ge1xuICAgIGlmICghcHJvcHNbcHJvcE5hbWVdKSB7XG4gICAgICByZXR1cm47XG4gICAgfVxuICAgIGZvciAobGV0IGkgPSAwOyBpIDwgcHJvcHNbcHJvcE5hbWVdLmxlbmd0aDsgaSArPSAxKSB7XG4gICAgICBsZXQgY29tcGFyYXRvcklzVmFsaWQgPSBmYWxzZTtcbiAgICAgIGZvciAobGV0IGogPSAwOyBqIDwgbGVnYWxDb21wYXJhdG9ycy5sZW5ndGg7IGogKz0gMSkge1xuICAgICAgICBpZiAobGVnYWxDb21wYXJhdG9yc1tqXSA9PT0gcHJvcHNbcHJvcE5hbWVdW2ldIHx8IHByb3BzW3Byb3BOYW1lXVtpXSA9PT0gJycpIHtcbiAgICAgICAgICBjb21wYXJhdG9ySXNWYWxpZCA9IHRydWU7XG4gICAgICAgICAgYnJlYWs7XG4gICAgICAgIH1cbiAgICAgIH1cbiAgICAgIGlmICghY29tcGFyYXRvcklzVmFsaWQpIHtcbiAgICAgICAgcmV0dXJuIG5ldyBFcnJvcihgRGF0ZSBjb21wYXJhdG9yIHByb3ZpZGVkIGlzIG5vdCBzdXBwb3J0ZWQuXG4gICAgICAgICAgVXNlIG9ubHkgJHtsZWdhbENvbXBhcmF0b3JzfWApO1xuICAgICAgfVxuICAgIH1cbiAgfSxcbiAgcGxhY2Vob2xkZXI6IFByb3BUeXBlcy5zdHJpbmcsXG4gIHdpdGhvdXRFbXB0eUNvbXBhcmF0b3JPcHRpb246IFByb3BUeXBlcy5ib29sLFxuICBzdHlsZTogUHJvcFR5cGVzLm9iamVjdCxcbiAgY29tcGFyYXRvclN0eWxlOiBQcm9wVHlwZXMub2JqZWN0LFxuICBkYXRlU3R5bGU6IFByb3BUeXBlcy5vYmplY3QsXG4gIGNsYXNzTmFtZTogUHJvcFR5cGVzLnN0cmluZyxcbiAgY29tcGFyYXRvckNsYXNzTmFtZTogUHJvcFR5cGVzLnN0cmluZyxcbiAgZGF0ZUNsYXNzTmFtZTogUHJvcFR5cGVzLnN0cmluZyxcbiAgZ2V0RmlsdGVyOiBQcm9wVHlwZXMuZnVuY1xufTtcblxuRGF0ZUZpbHRlci5kZWZhdWx0UHJvcHMgPSB7XG4gIGRlbGF5OiAwLFxuICBkZWZhdWx0VmFsdWU6IHtcbiAgICBkYXRlOiB1bmRlZmluZWQsXG4gICAgY29tcGFyYXRvcjogJydcbiAgfSxcbiAgZmlsdGVyU3RhdGU6IHt9LFxuICB3aXRob3V0RW1wdHlDb21wYXJhdG9yT3B0aW9uOiBmYWxzZSxcbiAgY29tcGFyYXRvcnM6IGxlZ2FsQ29tcGFyYXRvcnMsXG4gIHBsYWNlaG9sZGVyOiB1bmRlZmluZWQsXG4gIHN0eWxlOiB1bmRlZmluZWQsXG4gIGNsYXNzTmFtZTogJycsXG4gIGNvbXBhcmF0b3JTdHlsZTogdW5kZWZpbmVkLFxuICBjb21wYXJhdG9yQ2xhc3NOYW1lOiAnJyxcbiAgZGF0ZVN0eWxlOiB1bmRlZmluZWQsXG4gIGRhdGVDbGFzc05hbWU6ICcnLFxuICBpZDogbnVsbFxufTtcblxuXG5leHBvcnQgZGVmYXVsdCBEYXRlRmlsdGVyO1xuXG5cblxuLy8gV0VCUEFDSyBGT09URVIgLy9cbi8vIC4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1maWx0ZXIvc3JjL2NvbXBvbmVudHMvZGF0ZS5qcyIsIi8qIGVzbGludCByZWFjdC9wcm9wLXR5cGVzOiAwICovXG4vKiBlc2xpbnQgcmVhY3QvcmVxdWlyZS1kZWZhdWx0LXByb3BzOiAwICovXG4vKiBlc2xpbnQgY2FtZWxjYXNlOiAwICovXG5pbXBvcnQgUmVhY3QgZnJvbSAncmVhY3QnO1xuaW1wb3J0IFByb3BUeXBlcyBmcm9tICdwcm9wLXR5cGVzJztcblxuaW1wb3J0IHsgZmlsdGVycyB9IGZyb20gJy4vZmlsdGVyJztcbmltcG9ydCB7IExJS0UsIEVRIH0gZnJvbSAnLi9jb21wYXJpc29uJztcbmltcG9ydCB7IEZJTFRFUl9UWVBFIH0gZnJvbSAnLi9jb25zdCc7XG5cbmV4cG9ydCBkZWZhdWx0IChcbiAgXyxcbiAgaXNSZW1vdGVGaWx0ZXJpbmcsXG4gIGhhbmRsZUZpbHRlckNoYW5nZVxuKSA9PiB7XG4gIGNvbnN0IEZpbHRlckNvbnRleHQgPSBSZWFjdC5jcmVhdGVDb250ZXh0KCk7XG5cbiAgY2xhc3MgRmlsdGVyUHJvdmlkZXIgZXh0ZW5kcyBSZWFjdC5Db21wb25lbnQge1xuICAgIHN0YXRpYyBwcm9wVHlwZXMgPSB7XG4gICAgICBkYXRhOiBQcm9wVHlwZXMuYXJyYXkuaXNSZXF1aXJlZCxcbiAgICAgIGNvbHVtbnM6IFByb3BUeXBlcy5hcnJheS5pc1JlcXVpcmVkLFxuICAgICAgZGF0YUNoYW5nZUxpc3RlbmVyOiBQcm9wVHlwZXMub2JqZWN0XG4gICAgfVxuXG4gICAgY29uc3RydWN0b3IocHJvcHMpIHtcbiAgICAgIHN1cGVyKHByb3BzKTtcbiAgICAgIHRoaXMuY3VyckZpbHRlcnMgPSB7fTtcbiAgICAgIHRoaXMuY2xlYXJGaWx0ZXJzID0ge307XG4gICAgICB0aGlzLm9uRmlsdGVyID0gdGhpcy5vbkZpbHRlci5iaW5kKHRoaXMpO1xuICAgICAgdGhpcy5kb0ZpbHRlciA9IHRoaXMuZG9GaWx0ZXIuYmluZCh0aGlzKTtcbiAgICAgIHRoaXMub25FeHRlcm5hbEZpbHRlciA9IHRoaXMub25FeHRlcm5hbEZpbHRlci5iaW5kKHRoaXMpO1xuICAgICAgdGhpcy5kYXRhID0gcHJvcHMuZGF0YTtcbiAgICAgIHRoaXMuaXNFbWl0RGF0YUNoYW5nZSA9IGZhbHNlO1xuICAgIH1cblxuICAgIGNvbXBvbmVudERpZE1vdW50KCkge1xuICAgICAgaWYgKGlzUmVtb3RlRmlsdGVyaW5nKCkgJiYgT2JqZWN0LmtleXModGhpcy5jdXJyRmlsdGVycykubGVuZ3RoID4gMCkge1xuICAgICAgICBoYW5kbGVGaWx0ZXJDaGFuZ2UodGhpcy5jdXJyRmlsdGVycyk7XG4gICAgICB9XG4gICAgfVxuXG4gICAgb25GaWx0ZXIoY29sdW1uLCBmaWx0ZXJUeXBlLCBpbml0aWFsaXplID0gZmFsc2UpIHtcbiAgICAgIHJldHVybiAoZmlsdGVyVmFsKSA9PiB7XG4gICAgICAgIC8vIHdhdGNoIG91dCBoZXJlIGlmIG1pZ3JhdGlvbiB0byBjb250ZXh0IEFQSSwgIzMzNFxuICAgICAgICBjb25zdCBjdXJyRmlsdGVycyA9IE9iamVjdC5hc3NpZ24oe30sIHRoaXMuY3VyckZpbHRlcnMpO1xuICAgICAgICB0aGlzLmNsZWFyRmlsdGVycyA9IHt9O1xuICAgICAgICBjb25zdCB7IGRhdGFGaWVsZCwgZmlsdGVyIH0gPSBjb2x1bW47XG5cbiAgICAgICAgY29uc3QgbmVlZENsZWFyRmlsdGVycyA9XG4gICAgICAgICAgIV8uaXNEZWZpbmVkKGZpbHRlclZhbCkgfHxcbiAgICAgICAgICBmaWx0ZXJWYWwgPT09ICcnIHx8XG4gICAgICAgICAgZmlsdGVyVmFsLmxlbmd0aCA9PT0gMDtcblxuICAgICAgICBpZiAobmVlZENsZWFyRmlsdGVycykge1xuICAgICAgICAgIGRlbGV0ZSBjdXJyRmlsdGVyc1tkYXRhRmllbGRdO1xuICAgICAgICAgIHRoaXMuY2xlYXJGaWx0ZXJzID0geyBbZGF0YUZpZWxkXTogeyBjbGVhcjogdHJ1ZSwgZmlsdGVyVmFsIH0gfTtcbiAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICAvLyBzZWxlY3QgZGVmYXVsdCBjb21wYXJhdG9yIGlzIEVRLCBvdGhlcnMgYXJlIExJS0VcbiAgICAgICAgICBjb25zdCB7XG4gICAgICAgICAgICBjb21wYXJhdG9yID0gKGZpbHRlclR5cGUgPT09IEZJTFRFUl9UWVBFLlNFTEVDVCA/IEVRIDogTElLRSksXG4gICAgICAgICAgICBjYXNlU2Vuc2l0aXZlID0gZmFsc2VcbiAgICAgICAgICB9ID0gZmlsdGVyLnByb3BzO1xuICAgICAgICAgIGN1cnJGaWx0ZXJzW2RhdGFGaWVsZF0gPSB7IGZpbHRlclZhbCwgZmlsdGVyVHlwZSwgY29tcGFyYXRvciwgY2FzZVNlbnNpdGl2ZSB9O1xuICAgICAgICB9XG5cbiAgICAgICAgdGhpcy5jdXJyRmlsdGVycyA9IGN1cnJGaWx0ZXJzO1xuXG4gICAgICAgIGlmIChpc1JlbW90ZUZpbHRlcmluZygpKSB7XG4gICAgICAgICAgaWYgKCFpbml0aWFsaXplKSB7XG4gICAgICAgICAgICBoYW5kbGVGaWx0ZXJDaGFuZ2UodGhpcy5jdXJyRmlsdGVycyk7XG4gICAgICAgICAgfVxuICAgICAgICAgIHJldHVybjtcbiAgICAgICAgfVxuICAgICAgICB0aGlzLmRvRmlsdGVyKHRoaXMucHJvcHMpO1xuICAgICAgfTtcbiAgICB9XG5cbiAgICBvbkV4dGVybmFsRmlsdGVyKGNvbHVtbiwgZmlsdGVyVHlwZSkge1xuICAgICAgcmV0dXJuICh2YWx1ZSkgPT4ge1xuICAgICAgICB0aGlzLm9uRmlsdGVyKGNvbHVtbiwgZmlsdGVyVHlwZSkodmFsdWUpO1xuICAgICAgfTtcbiAgICB9XG5cbiAgICBnZXRGaWx0ZXJlZCgpIHtcbiAgICAgIHJldHVybiB0aGlzLmRhdGE7XG4gICAgfVxuXG4gICAgVU5TQUZFX2NvbXBvbmVudFdpbGxSZWNlaXZlUHJvcHMobmV4dFByb3BzKSB7XG4gICAgICAvLyBsZXQgbmV4dERhdGEgPSBuZXh0UHJvcHMuZGF0YTtcbiAgICAgIGlmICghaXNSZW1vdGVGaWx0ZXJpbmcoKSAmJiAhXy5pc0VxdWFsKG5leHRQcm9wcy5kYXRhLCB0aGlzLmRhdGEpKSB7XG4gICAgICAgIHRoaXMuZG9GaWx0ZXIobmV4dFByb3BzLCB0aGlzLmlzRW1pdERhdGFDaGFuZ2UpO1xuICAgICAgfSBlbHNlIHtcbiAgICAgICAgdGhpcy5kYXRhID0gbmV4dFByb3BzLmRhdGE7XG4gICAgICB9XG4gICAgfVxuXG4gICAgZG9GaWx0ZXIocHJvcHMsIGlnbm9yZUVtaXREYXRhQ2hhbmdlID0gZmFsc2UpIHtcbiAgICAgIGNvbnN0IHsgZGF0YUNoYW5nZUxpc3RlbmVyLCBkYXRhLCBjb2x1bW5zLCBmaWx0ZXIgfSA9IHByb3BzO1xuICAgICAgY29uc3QgcmVzdWx0ID0gZmlsdGVycyhkYXRhLCBjb2x1bW5zLCBfKSh0aGlzLmN1cnJGaWx0ZXJzLCB0aGlzLmNsZWFyRmlsdGVycyk7XG4gICAgICBpZiAoZmlsdGVyLmFmdGVyRmlsdGVyKSB7XG4gICAgICAgIGZpbHRlci5hZnRlckZpbHRlcihyZXN1bHQsIHRoaXMuY3VyckZpbHRlcnMpO1xuICAgICAgfVxuICAgICAgdGhpcy5kYXRhID0gcmVzdWx0O1xuICAgICAgaWYgKGRhdGFDaGFuZ2VMaXN0ZW5lciAmJiAhaWdub3JlRW1pdERhdGFDaGFuZ2UpIHtcbiAgICAgICAgdGhpcy5pc0VtaXREYXRhQ2hhbmdlID0gdHJ1ZTtcbiAgICAgICAgZGF0YUNoYW5nZUxpc3RlbmVyLmVtaXQoJ2ZpbHRlckNoYW5nZWQnLCByZXN1bHQubGVuZ3RoKTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIHRoaXMuaXNFbWl0RGF0YUNoYW5nZSA9IGZhbHNlO1xuICAgICAgICB0aGlzLmZvcmNlVXBkYXRlKCk7XG4gICAgICB9XG4gICAgfVxuXG4gICAgcmVuZGVyKCkge1xuICAgICAgcmV0dXJuIChcbiAgICAgICAgPEZpbHRlckNvbnRleHQuUHJvdmlkZXIgdmFsdWU9eyB7XG4gICAgICAgICAgZGF0YTogdGhpcy5kYXRhLFxuICAgICAgICAgIG9uRmlsdGVyOiB0aGlzLm9uRmlsdGVyLFxuICAgICAgICAgIG9uRXh0ZXJuYWxGaWx0ZXI6IHRoaXMub25FeHRlcm5hbEZpbHRlcixcbiAgICAgICAgICBjdXJyRmlsdGVyczogdGhpcy5jdXJyRmlsdGVyc1xuICAgICAgICB9IH1cbiAgICAgICAgPlxuICAgICAgICAgIHsgdGhpcy5wcm9wcy5jaGlsZHJlbiB9XG4gICAgICAgIDwvRmlsdGVyQ29udGV4dC5Qcm92aWRlcj5cbiAgICAgICk7XG4gICAgfVxuICB9XG5cbiAgcmV0dXJuIHtcbiAgICBQcm92aWRlcjogRmlsdGVyUHJvdmlkZXIsXG4gICAgQ29uc3VtZXI6IEZpbHRlckNvbnRleHQuQ29uc3VtZXJcbiAgfTtcbn07XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLWZpbHRlci9zcmMvY29udGV4dC5qcyIsIi8qIGVzbGludCBlcWVxZXE6IDAgKi9cbi8qIGVzbGludCBuby1jb25zb2xlOiAwICovXG5pbXBvcnQgeyBGSUxURVJfVFlQRSB9IGZyb20gJy4vY29uc3QnO1xuaW1wb3J0IHsgTElLRSwgRVEsIE5FLCBHVCwgR0UsIExULCBMRSB9IGZyb20gJy4vY29tcGFyaXNvbic7XG5cbmV4cG9ydCBjb25zdCBmaWx0ZXJCeVRleHQgPSBfID0+IChcbiAgZGF0YSxcbiAgZGF0YUZpZWxkLFxuICB7IGZpbHRlclZhbDogdXNlcklucHV0ID0gJycsIGNvbXBhcmF0b3IgPSBMSUtFLCBjYXNlU2Vuc2l0aXZlIH0sXG4gIGN1c3RvbUZpbHRlclZhbHVlXG4pID0+IHtcbiAgLy8gbWFrZSBzdXJlIGZpbHRlciB2YWx1ZSB0byBiZSBhIHN0cmluZ1xuICBjb25zdCBmaWx0ZXJWYWwgPSB1c2VySW5wdXQudG9TdHJpbmcoKTtcblxuICByZXR1cm4gKFxuICAgIGRhdGEuZmlsdGVyKChyb3cpID0+IHtcbiAgICAgIGxldCBjZWxsID0gXy5nZXQocm93LCBkYXRhRmllbGQpO1xuICAgICAgaWYgKGN1c3RvbUZpbHRlclZhbHVlKSB7XG4gICAgICAgIGNlbGwgPSBjdXN0b21GaWx0ZXJWYWx1ZShjZWxsLCByb3cpO1xuICAgICAgfVxuICAgICAgY29uc3QgY2VsbFN0ciA9IF8uaXNEZWZpbmVkKGNlbGwpID8gY2VsbC50b1N0cmluZygpIDogJyc7XG4gICAgICBpZiAoY29tcGFyYXRvciA9PT0gRVEpIHtcbiAgICAgICAgcmV0dXJuIGNlbGxTdHIgPT09IGZpbHRlclZhbDtcbiAgICAgIH1cbiAgICAgIGlmIChjYXNlU2Vuc2l0aXZlKSB7XG4gICAgICAgIHJldHVybiBjZWxsU3RyLmluY2x1ZGVzKGZpbHRlclZhbCk7XG4gICAgICB9XG5cbiAgICAgIHJldHVybiBjZWxsU3RyLnRvTG9jYWxlVXBwZXJDYXNlKCkuaW5kZXhPZihmaWx0ZXJWYWwudG9Mb2NhbGVVcHBlckNhc2UoKSkgIT09IC0xO1xuICAgIH0pXG4gICk7XG59O1xuXG5leHBvcnQgY29uc3QgZmlsdGVyQnlOdW1iZXIgPSBfID0+IChcbiAgZGF0YSxcbiAgZGF0YUZpZWxkLFxuICB7IGZpbHRlclZhbDogeyBjb21wYXJhdG9yLCBudW1iZXIgfSB9LFxuICBjdXN0b21GaWx0ZXJWYWx1ZVxuKSA9PiAoXG4gIGRhdGEuZmlsdGVyKChyb3cpID0+IHtcbiAgICBpZiAobnVtYmVyID09PSAnJyB8fCAhY29tcGFyYXRvcikgcmV0dXJuIHRydWU7XG4gICAgbGV0IGNlbGwgPSBfLmdldChyb3csIGRhdGFGaWVsZCk7XG5cbiAgICBpZiAoY3VzdG9tRmlsdGVyVmFsdWUpIHtcbiAgICAgIGNlbGwgPSBjdXN0b21GaWx0ZXJWYWx1ZShjZWxsLCByb3cpO1xuICAgIH1cblxuICAgIHN3aXRjaCAoY29tcGFyYXRvcikge1xuICAgICAgY2FzZSBFUToge1xuICAgICAgICByZXR1cm4gY2VsbCA9PSBudW1iZXI7XG4gICAgICB9XG4gICAgICBjYXNlIEdUOiB7XG4gICAgICAgIHJldHVybiBjZWxsID4gbnVtYmVyO1xuICAgICAgfVxuICAgICAgY2FzZSBHRToge1xuICAgICAgICByZXR1cm4gY2VsbCA+PSBudW1iZXI7XG4gICAgICB9XG4gICAgICBjYXNlIExUOiB7XG4gICAgICAgIHJldHVybiBjZWxsIDwgbnVtYmVyO1xuICAgICAgfVxuICAgICAgY2FzZSBMRToge1xuICAgICAgICByZXR1cm4gY2VsbCA8PSBudW1iZXI7XG4gICAgICB9XG4gICAgICBjYXNlIE5FOiB7XG4gICAgICAgIHJldHVybiBjZWxsICE9IG51bWJlcjtcbiAgICAgIH1cbiAgICAgIGRlZmF1bHQ6IHtcbiAgICAgICAgY29uc29sZS5lcnJvcignTnVtYmVyIGNvbXBhcmF0b3IgcHJvdmlkZWQgaXMgbm90IHN1cHBvcnRlZCcpO1xuICAgICAgICByZXR1cm4gdHJ1ZTtcbiAgICAgIH1cbiAgICB9XG4gIH0pXG4pO1xuXG5leHBvcnQgY29uc3QgZmlsdGVyQnlEYXRlID0gXyA9PiAoXG4gIGRhdGEsXG4gIGRhdGFGaWVsZCxcbiAgeyBmaWx0ZXJWYWw6IHsgY29tcGFyYXRvciwgZGF0ZSB9IH0sXG4gIGN1c3RvbUZpbHRlclZhbHVlXG4pID0+IHtcbiAgaWYgKCFkYXRlIHx8ICFjb21wYXJhdG9yKSByZXR1cm4gZGF0YTtcbiAgY29uc3QgZmlsdGVyRGF0ZSA9IGRhdGUuZ2V0VVRDRGF0ZSgpO1xuICBjb25zdCBmaWx0ZXJNb250aCA9IGRhdGUuZ2V0VVRDTW9udGgoKTtcbiAgY29uc3QgZmlsdGVyWWVhciA9IGRhdGUuZ2V0VVRDRnVsbFllYXIoKTtcblxuICByZXR1cm4gZGF0YS5maWx0ZXIoKHJvdykgPT4ge1xuICAgIGxldCB2YWxpZCA9IHRydWU7XG4gICAgbGV0IGNlbGwgPSBfLmdldChyb3csIGRhdGFGaWVsZCk7XG5cbiAgICBpZiAoY3VzdG9tRmlsdGVyVmFsdWUpIHtcbiAgICAgIGNlbGwgPSBjdXN0b21GaWx0ZXJWYWx1ZShjZWxsLCByb3cpO1xuICAgIH1cblxuICAgIGlmICh0eXBlb2YgY2VsbCAhPT0gJ29iamVjdCcpIHtcbiAgICAgIGNlbGwgPSBuZXcgRGF0ZShjZWxsKTtcbiAgICB9XG5cbiAgICBjb25zdCB0YXJnZXREYXRlID0gY2VsbC5nZXRVVENEYXRlKCk7XG4gICAgY29uc3QgdGFyZ2V0TW9udGggPSBjZWxsLmdldFVUQ01vbnRoKCk7XG4gICAgY29uc3QgdGFyZ2V0WWVhciA9IGNlbGwuZ2V0VVRDRnVsbFllYXIoKTtcblxuXG4gICAgc3dpdGNoIChjb21wYXJhdG9yKSB7XG4gICAgICBjYXNlIEVROiB7XG4gICAgICAgIGlmIChcbiAgICAgICAgICBmaWx0ZXJEYXRlICE9PSB0YXJnZXREYXRlIHx8XG4gICAgICAgICAgZmlsdGVyTW9udGggIT09IHRhcmdldE1vbnRoIHx8XG4gICAgICAgICAgZmlsdGVyWWVhciAhPT0gdGFyZ2V0WWVhclxuICAgICAgICApIHtcbiAgICAgICAgICB2YWxpZCA9IGZhbHNlO1xuICAgICAgICB9XG4gICAgICAgIGJyZWFrO1xuICAgICAgfVxuICAgICAgY2FzZSBHVDoge1xuICAgICAgICBpZiAoY2VsbCA8PSBkYXRlKSB7XG4gICAgICAgICAgdmFsaWQgPSBmYWxzZTtcbiAgICAgICAgfVxuICAgICAgICBicmVhaztcbiAgICAgIH1cbiAgICAgIGNhc2UgR0U6IHtcbiAgICAgICAgaWYgKHRhcmdldFllYXIgPCBmaWx0ZXJZZWFyKSB7XG4gICAgICAgICAgdmFsaWQgPSBmYWxzZTtcbiAgICAgICAgfSBlbHNlIGlmICh0YXJnZXRZZWFyID09PSBmaWx0ZXJZZWFyICYmXG4gICAgICAgICAgdGFyZ2V0TW9udGggPCBmaWx0ZXJNb250aCkge1xuICAgICAgICAgIHZhbGlkID0gZmFsc2U7XG4gICAgICAgIH0gZWxzZSBpZiAodGFyZ2V0WWVhciA9PT0gZmlsdGVyWWVhciAmJlxuICAgICAgICAgIHRhcmdldE1vbnRoID09PSBmaWx0ZXJNb250aCAmJlxuICAgICAgICAgIHRhcmdldERhdGUgPCBmaWx0ZXJEYXRlKSB7XG4gICAgICAgICAgdmFsaWQgPSBmYWxzZTtcbiAgICAgICAgfVxuICAgICAgICBicmVhaztcbiAgICAgIH1cbiAgICAgIGNhc2UgTFQ6IHtcbiAgICAgICAgaWYgKGNlbGwgPj0gZGF0ZSkge1xuICAgICAgICAgIHZhbGlkID0gZmFsc2U7XG4gICAgICAgIH1cbiAgICAgICAgYnJlYWs7XG4gICAgICB9XG4gICAgICBjYXNlIExFOiB7XG4gICAgICAgIGlmICh0YXJnZXRZZWFyID4gZmlsdGVyWWVhcikge1xuICAgICAgICAgIHZhbGlkID0gZmFsc2U7XG4gICAgICAgIH0gZWxzZSBpZiAodGFyZ2V0WWVhciA9PT0gZmlsdGVyWWVhciAmJlxuICAgICAgICAgIHRhcmdldE1vbnRoID4gZmlsdGVyTW9udGgpIHtcbiAgICAgICAgICB2YWxpZCA9IGZhbHNlO1xuICAgICAgICB9IGVsc2UgaWYgKHRhcmdldFllYXIgPT09IGZpbHRlclllYXIgJiZcbiAgICAgICAgICB0YXJnZXRNb250aCA9PT0gZmlsdGVyTW9udGggJiZcbiAgICAgICAgICB0YXJnZXREYXRlID4gZmlsdGVyRGF0ZSkge1xuICAgICAgICAgIHZhbGlkID0gZmFsc2U7XG4gICAgICAgIH1cbiAgICAgICAgYnJlYWs7XG4gICAgICB9XG4gICAgICBjYXNlIE5FOiB7XG4gICAgICAgIGlmIChcbiAgICAgICAgICBmaWx0ZXJEYXRlID09PSB0YXJnZXREYXRlICYmXG4gICAgICAgICAgZmlsdGVyTW9udGggPT09IHRhcmdldE1vbnRoICYmXG4gICAgICAgICAgZmlsdGVyWWVhciA9PT0gdGFyZ2V0WWVhclxuICAgICAgICApIHtcbiAgICAgICAgICB2YWxpZCA9IGZhbHNlO1xuICAgICAgICB9XG4gICAgICAgIGJyZWFrO1xuICAgICAgfVxuICAgICAgZGVmYXVsdDoge1xuICAgICAgICBjb25zb2xlLmVycm9yKCdEYXRlIGNvbXBhcmF0b3IgcHJvdmlkZWQgaXMgbm90IHN1cHBvcnRlZCcpO1xuICAgICAgICBicmVhaztcbiAgICAgIH1cbiAgICB9XG4gICAgcmV0dXJuIHZhbGlkO1xuICB9KTtcbn07XG5cbmV4cG9ydCBjb25zdCBmaWx0ZXJCeUFycmF5ID0gXyA9PiAoXG4gIGRhdGEsXG4gIGRhdGFGaWVsZCxcbiAgeyBmaWx0ZXJWYWwsIGNvbXBhcmF0b3IgfVxuKSA9PiB7XG4gIGlmIChmaWx0ZXJWYWwubGVuZ3RoID09PSAwKSByZXR1cm4gZGF0YTtcbiAgY29uc3QgcmVmaW5lZEZpbHRlclZhbCA9IGZpbHRlclZhbFxuICAgIC5maWx0ZXIoeCA9PiBfLmlzRGVmaW5lZCh4KSlcbiAgICAubWFwKHggPT4geC50b1N0cmluZygpKTtcbiAgcmV0dXJuIGRhdGEuZmlsdGVyKChyb3cpID0+IHtcbiAgICBjb25zdCBjZWxsID0gXy5nZXQocm93LCBkYXRhRmllbGQpO1xuICAgIGxldCBjZWxsU3RyID0gXy5pc0RlZmluZWQoY2VsbCkgPyBjZWxsLnRvU3RyaW5nKCkgOiAnJztcbiAgICBpZiAoY29tcGFyYXRvciA9PT0gRVEpIHtcbiAgICAgIHJldHVybiByZWZpbmVkRmlsdGVyVmFsLmluZGV4T2YoY2VsbFN0cikgIT09IC0xO1xuICAgIH1cbiAgICBjZWxsU3RyID0gY2VsbFN0ci50b0xvY2FsZVVwcGVyQ2FzZSgpO1xuICAgIHJldHVybiByZWZpbmVkRmlsdGVyVmFsLnNvbWUoaXRlbSA9PiBjZWxsU3RyLmluZGV4T2YoaXRlbS50b0xvY2FsZVVwcGVyQ2FzZSgpKSAhPT0gLTEpO1xuICB9KTtcbn07XG5cbmV4cG9ydCBjb25zdCBmaWx0ZXJGYWN0b3J5ID0gXyA9PiAoZmlsdGVyVHlwZSkgPT4ge1xuICBzd2l0Y2ggKGZpbHRlclR5cGUpIHtcbiAgICBjYXNlIEZJTFRFUl9UWVBFLk1VTFRJU0VMRUNUOlxuICAgICAgcmV0dXJuIGZpbHRlckJ5QXJyYXkoXyk7XG4gICAgY2FzZSBGSUxURVJfVFlQRS5OVU1CRVI6XG4gICAgICByZXR1cm4gZmlsdGVyQnlOdW1iZXIoXyk7XG4gICAgY2FzZSBGSUxURVJfVFlQRS5EQVRFOlxuICAgICAgcmV0dXJuIGZpbHRlckJ5RGF0ZShfKTtcbiAgICBjYXNlIEZJTFRFUl9UWVBFLlRFWFQ6XG4gICAgY2FzZSBGSUxURVJfVFlQRS5TRUxFQ1Q6XG4gICAgZGVmYXVsdDpcbiAgICAgIC8vIFVzZSBgdGV4dGAgZmlsdGVyIGFzIGRlZmF1bHQgZmlsdGVyXG4gICAgICByZXR1cm4gZmlsdGVyQnlUZXh0KF8pO1xuICB9XG59O1xuXG5leHBvcnQgY29uc3QgZmlsdGVycyA9IChkYXRhLCBjb2x1bW5zLCBfKSA9PiAoY3VyckZpbHRlcnMsIGNsZWFyRmlsdGVycyA9IHt9KSA9PiB7XG4gIGNvbnN0IGZhY3RvcnkgPSBmaWx0ZXJGYWN0b3J5KF8pO1xuICBjb25zdCBmaWx0ZXJTdGF0ZSA9IHsgLi4uY2xlYXJGaWx0ZXJzLCAuLi5jdXJyRmlsdGVycyB9O1xuICBsZXQgcmVzdWx0ID0gZGF0YTtcbiAgbGV0IGZpbHRlckZuO1xuICBPYmplY3Qua2V5cyhmaWx0ZXJTdGF0ZSkuZm9yRWFjaCgoZGF0YUZpZWxkKSA9PiB7XG4gICAgbGV0IGN1cnJlbnRSZXN1bHQ7XG4gICAgbGV0IGZpbHRlclZhbHVlO1xuICAgIGxldCBjdXN0b21GaWx0ZXI7XG4gICAgZm9yIChsZXQgaSA9IDA7IGkgPCBjb2x1bW5zLmxlbmd0aDsgaSArPSAxKSB7XG4gICAgICBpZiAoY29sdW1uc1tpXS5kYXRhRmllbGQgPT09IGRhdGFGaWVsZCkge1xuICAgICAgICBmaWx0ZXJWYWx1ZSA9IGNvbHVtbnNbaV0uZmlsdGVyVmFsdWU7XG4gICAgICAgIGlmIChjb2x1bW5zW2ldLmZpbHRlcikge1xuICAgICAgICAgIGN1c3RvbUZpbHRlciA9IGNvbHVtbnNbaV0uZmlsdGVyLnByb3BzLm9uRmlsdGVyO1xuICAgICAgICB9XG4gICAgICAgIGJyZWFrO1xuICAgICAgfVxuICAgIH1cblxuICAgIGlmIChjbGVhckZpbHRlcnNbZGF0YUZpZWxkXSAmJiBjdXN0b21GaWx0ZXIpIHtcbiAgICAgIGN1cnJlbnRSZXN1bHQgPSBjdXN0b21GaWx0ZXIoY2xlYXJGaWx0ZXJzW2RhdGFGaWVsZF0uZmlsdGVyVmFsLCByZXN1bHQpO1xuICAgICAgaWYgKHR5cGVvZiBjdXJyZW50UmVzdWx0ICE9PSAndW5kZWZpbmVkJykge1xuICAgICAgICByZXN1bHQgPSBjdXJyZW50UmVzdWx0O1xuICAgICAgfVxuICAgIH0gZWxzZSB7XG4gICAgICBjb25zdCBmaWx0ZXJPYmogPSBmaWx0ZXJTdGF0ZVtkYXRhRmllbGRdO1xuICAgICAgZmlsdGVyRm4gPSBmYWN0b3J5KGZpbHRlck9iai5maWx0ZXJUeXBlKTtcbiAgICAgIGlmIChjdXN0b21GaWx0ZXIpIHtcbiAgICAgICAgY3VycmVudFJlc3VsdCA9IGN1c3RvbUZpbHRlcihmaWx0ZXJPYmouZmlsdGVyVmFsLCByZXN1bHQpO1xuICAgICAgfVxuICAgICAgaWYgKHR5cGVvZiBjdXJyZW50UmVzdWx0ID09PSAndW5kZWZpbmVkJykge1xuICAgICAgICByZXN1bHQgPSBmaWx0ZXJGbihyZXN1bHQsIGRhdGFGaWVsZCwgZmlsdGVyT2JqLCBmaWx0ZXJWYWx1ZSk7XG4gICAgICB9IGVsc2Uge1xuICAgICAgICByZXN1bHQgPSBjdXJyZW50UmVzdWx0O1xuICAgICAgfVxuICAgIH1cbiAgfSk7XG4gIHJldHVybiByZXN1bHQ7XG59O1xuXG5cblxuLy8gV0VCUEFDSyBGT09URVIgLy9cbi8vIC4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1maWx0ZXIvc3JjL2ZpbHRlci5qcyJdLCJzb3VyY2VSb290IjoiIn0=
//# sourceMappingURL=react-bootstrap-table2-filter.js.map