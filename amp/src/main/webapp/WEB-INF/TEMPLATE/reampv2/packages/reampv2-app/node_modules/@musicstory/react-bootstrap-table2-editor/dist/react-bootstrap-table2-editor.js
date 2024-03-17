(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory(require("react"));
	else if(typeof define === 'function' && define.amd)
		define(["react"], factory);
	else if(typeof exports === 'object')
		exports["ReactBootstrapTable2Editor"] = factory(require("react"));
	else
		root["ReactBootstrapTable2Editor"] = factory(root["React"]);
})(this, function(__WEBPACK_EXTERNAL_MODULE_0__) {
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
/******/ 	return __webpack_require__(__webpack_require__.s = 5);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ (function(module, exports) {

module.exports = __WEBPACK_EXTERNAL_MODULE_0__;

/***/ }),
/* 1 */
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
/* 2 */
/***/ (function(module, exports, __webpack_require__) {

var __WEBPACK_AMD_DEFINE_ARRAY__, __WEBPACK_AMD_DEFINE_RESULT__;/*!
  Copyright (c) 2018 Jed Watson.
  Licensed under the MIT License (MIT), see
  http://jedwatson.github.io/classnames
*/
/* global define */

(function () {
	'use strict';

	var hasOwn = {}.hasOwnProperty;

	function classNames() {
		var classes = [];

		for (var i = 0; i < arguments.length; i++) {
			var arg = arguments[i];
			if (!arg) continue;

			var argType = typeof arg;

			if (argType === 'string' || argType === 'number') {
				classes.push(arg);
			} else if (Array.isArray(arg)) {
				if (arg.length) {
					var inner = classNames.apply(null, arg);
					if (inner) {
						classes.push(inner);
					}
				}
			} else if (argType === 'object') {
				if (arg.toString === Object.prototype.toString) {
					for (var key in arg) {
						if (hasOwn.call(arg, key) && arg[key]) {
							classes.push(key);
						}
					}
				} else {
					classes.push(arg.toString());
				}
			}
		}

		return classes.join(' ');
	}

	if (typeof module !== 'undefined' && module.exports) {
		classNames.default = classNames;
		module.exports = classNames;
	} else if (true) {
		// register as 'classnames', consistent with npm package name
		!(__WEBPACK_AMD_DEFINE_ARRAY__ = [], __WEBPACK_AMD_DEFINE_RESULT__ = function () {
			return classNames;
		}.apply(exports, __WEBPACK_AMD_DEFINE_ARRAY__),
				__WEBPACK_AMD_DEFINE_RESULT__ !== undefined && (module.exports = __WEBPACK_AMD_DEFINE_RESULT__));
	} else {
		window.classNames = classNames;
	}
}());


/***/ }),
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
var TIME_TO_CLOSE_MESSAGE = exports.TIME_TO_CLOSE_MESSAGE = 3000;
var DELAY_FOR_DBCLICK = exports.DELAY_FOR_DBCLICK = 200;
var CLICK_TO_CELL_EDIT = exports.CLICK_TO_CELL_EDIT = 'click';
var DBCLICK_TO_CELL_EDIT = exports.DBCLICK_TO_CELL_EDIT = 'dbclick';

var EDITTYPE = exports.EDITTYPE = {
  TEXT: 'text',
  SELECT: 'select',
  TEXTAREA: 'textarea',
  CHECKBOX: 'checkbox',
  DATE: 'date'
};

/***/ }),
/* 4 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Consumer = undefined;

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

var _const = __webpack_require__(3);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint disable-next-line: 0 */
/* eslint react/prop-types: 0 */
/* eslint react/require-default-props: 0 */
/* eslint camelcase: 0 */
/* eslint react/no-unused-prop-types: 0 */


var CellEditContext = _react2.default.createContext();

exports.default = function (_, dataOperator, isRemoteCellEdit, handleCellChange) {
  var CellEditProvider = function (_React$Component) {
    _inherits(CellEditProvider, _React$Component);

    function CellEditProvider(props) {
      _classCallCheck(this, CellEditProvider);

      var _this = _possibleConstructorReturn(this, (CellEditProvider.__proto__ || Object.getPrototypeOf(CellEditProvider)).call(this, props));

      _this.doUpdate = _this.doUpdate.bind(_this);
      _this.startEditing = _this.startEditing.bind(_this);
      _this.escapeEditing = _this.escapeEditing.bind(_this);
      _this.completeEditing = _this.completeEditing.bind(_this);
      _this.handleCellUpdate = _this.handleCellUpdate.bind(_this);
      _this.state = {
        ridx: null,
        cidx: null,
        message: null
      };
      return _this;
    }

    _createClass(CellEditProvider, [{
      key: 'UNSAFE_componentWillReceiveProps',
      value: function UNSAFE_componentWillReceiveProps(nextProps) {
        if (nextProps.cellEdit && isRemoteCellEdit()) {
          if (nextProps.cellEdit.options.errorMessage) {
            this.setState(function () {
              return {
                message: nextProps.cellEdit.options.errorMessage
              };
            });
          } else {
            this.escapeEditing();
          }
        }
      }
    }, {
      key: 'handleCellUpdate',
      value: function handleCellUpdate(row, column, newValue) {
        var _this2 = this;

        var newValueWithType = dataOperator.typeConvert(column.type, newValue);
        var cellEdit = this.props.cellEdit;
        var beforeSaveCell = cellEdit.options.beforeSaveCell;

        var oldValue = _.get(row, column.dataField);
        var beforeSaveCellDone = function beforeSaveCellDone() {
          var result = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : true;

          if (result) {
            _this2.doUpdate(row, column, newValueWithType);
          } else {
            _this2.escapeEditing();
          }
        };
        if (_.isFunction(beforeSaveCell)) {
          var result = beforeSaveCell(oldValue, newValueWithType, row, column, beforeSaveCellDone);
          if (_.isObject(result) && result.async) {
            return;
          }
        }
        this.doUpdate(row, column, newValueWithType);
      }
    }, {
      key: 'doUpdate',
      value: function doUpdate(row, column, newValue) {
        var _props = this.props,
            keyField = _props.keyField,
            cellEdit = _props.cellEdit,
            data = _props.data;
        var afterSaveCell = cellEdit.options.afterSaveCell;

        var rowId = _.get(row, keyField);
        var oldValue = _.get(row, column.dataField);
        if (isRemoteCellEdit()) {
          handleCellChange(rowId, column.dataField, newValue);
        } else {
          dataOperator.editCell(data, keyField, rowId, column.dataField, newValue);
          if (_.isFunction(afterSaveCell)) afterSaveCell(oldValue, newValue, row, column);
          this.completeEditing();
        }
      }
    }, {
      key: 'completeEditing',
      value: function completeEditing() {
        this.setState(function () {
          return {
            ridx: null,
            cidx: null,
            message: null
          };
        });
      }
    }, {
      key: 'startEditing',
      value: function startEditing(ridx, cidx) {
        var _this3 = this;

        var editing = function editing() {
          _this3.setState(function () {
            return {
              ridx: ridx,
              cidx: cidx
            };
          });
        };

        var selectRow = this.props.selectRow;

        if (!selectRow || selectRow.clickToEdit || !selectRow.clickToSelect) editing();
      }
    }, {
      key: 'escapeEditing',
      value: function escapeEditing() {
        this.setState(function () {
          return {
            ridx: null,
            cidx: null
          };
        });
      }
    }, {
      key: 'render',
      value: function render() {
        var _props$cellEdit = this.props.cellEdit,
            _props$cellEdit$optio = _props$cellEdit.options,
            nonEditableRows = _props$cellEdit$optio.nonEditableRows,
            errorMessage = _props$cellEdit$optio.errorMessage,
            optionsRest = _objectWithoutProperties(_props$cellEdit$optio, ['nonEditableRows', 'errorMessage']),
            cellEditRest = _objectWithoutProperties(_props$cellEdit, ['options']);

        var newCellEdit = _extends({}, optionsRest, cellEditRest, this.state, {
          nonEditableRows: _.isDefined(nonEditableRows) ? nonEditableRows() : [],
          onStart: this.startEditing,
          onEscape: this.escapeEditing,
          onUpdate: this.handleCellUpdate
        });

        return _react2.default.createElement(
          CellEditContext.Provider,
          {
            value: _extends({}, newCellEdit)
          },
          this.props.children
        );
      }
    }]);

    return CellEditProvider;
  }(_react2.default.Component);

  CellEditProvider.propTypes = {
    data: _propTypes2.default.array.isRequired,
    selectRow: _propTypes2.default.object,
    options: _propTypes2.default.shape({
      mode: _propTypes2.default.oneOf([_const.CLICK_TO_CELL_EDIT, _const.DBCLICK_TO_CELL_EDIT]).isRequired,
      onErrorMessageDisappear: _propTypes2.default.func,
      blurToSave: _propTypes2.default.bool,
      beforeSaveCell: _propTypes2.default.func,
      afterSaveCell: _propTypes2.default.func,
      onStartEdit: _propTypes2.default.func,
      nonEditableRows: _propTypes2.default.func,
      timeToCloseMessage: _propTypes2.default.number,
      errorMessage: _propTypes2.default.any
    })
  };

  return {
    Provider: CellEditProvider
  };
};

var Consumer = exports.Consumer = CellEditContext.Consumer;

/***/ }),
/* 5 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Type = undefined;

var _context = __webpack_require__(4);

var _context2 = _interopRequireDefault(_context);

var _rowConsumer = __webpack_require__(10);

var _rowConsumer2 = _interopRequireDefault(_rowConsumer);

var _editingCellConsumer = __webpack_require__(11);

var _editingCellConsumer2 = _interopRequireDefault(_editingCellConsumer);

var _const = __webpack_require__(3);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = function () {
  var options = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
  return {
    createContext: _context2.default,
    createEditingCell: _editingCellConsumer2.default,
    withRowLevelCellEdit: _rowConsumer2.default,
    DBCLICK_TO_CELL_EDIT: _const.DBCLICK_TO_CELL_EDIT,
    DELAY_FOR_DBCLICK: _const.DELAY_FOR_DBCLICK,
    options: options
  };
};

var Type = exports.Type = _const.EDITTYPE;

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

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; /* eslint react/prop-types: 0 */


var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _const = __webpack_require__(3);

var _context = __webpack_require__(4);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = function (Component, selectRowEnabled) {
  var renderWithCellEdit = function renderWithCellEdit(props, cellEdit) {
    var key = props.value;
    var editableRow = !(cellEdit.nonEditableRows.length > 0 && cellEdit.nonEditableRows.indexOf(key) > -1);

    var attrs = {};

    if (selectRowEnabled && cellEdit.mode === _const.DBCLICK_TO_CELL_EDIT) {
      attrs.DELAY_FOR_DBCLICK = _const.DELAY_FOR_DBCLICK;
    }

    return _react2.default.createElement(Component, _extends({}, props, attrs, {
      editingRowIdx: cellEdit.ridx,
      editingColIdx: cellEdit.cidx,
      editable: editableRow,
      onStart: cellEdit.onStart,
      clickToEdit: cellEdit.mode === _const.CLICK_TO_CELL_EDIT,
      dbclickToEdit: cellEdit.mode === _const.DBCLICK_TO_CELL_EDIT
    }));
  };
  function withConsumer(props) {
    return _react2.default.createElement(
      _context.Consumer,
      null,
      function (cellEdit) {
        return renderWithCellEdit(props, cellEdit);
      }
    );
  }

  withConsumer.displayName = 'WithCellEditingRowConsumer';
  return withConsumer;
};

/***/ }),
/* 11 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; }; /* eslint react/prop-types: 0 */


var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _context = __webpack_require__(4);

var _editingCell = __webpack_require__(12);

var _editingCell2 = _interopRequireDefault(_editingCell);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = function (_, onStartEdit) {
  var EditingCell = (0, _editingCell2.default)(_, onStartEdit);
  var renderWithEditingCell = function renderWithEditingCell(props, cellEdit) {
    var content = _.get(props.row, props.column.dataField);
    var editCellstyle = props.column.editCellStyle || {};
    var editCellclasses = props.column.editCellClasses;
    if (_.isFunction(props.column.editCellStyle)) {
      editCellstyle = props.column.editCellStyle(content, props.row, props.rowIndex, props.columnIndex);
    }
    if (_.isFunction(props.column.editCellClasses)) {
      editCellclasses = props.column.editCellClasses(content, props.row, props.rowIndex, props.columnIndex);
    }

    return _react2.default.createElement(EditingCell, _extends({}, props, {
      className: editCellclasses,
      style: editCellstyle
    }, cellEdit));
  };

  return function (props) {
    return _react2.default.createElement(
      _context.Consumer,
      null,
      function (cellEdit) {
        return renderWithEditingCell(props, cellEdit);
      }
    );
  };
};

/***/ }),
/* 12 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _classnames = __webpack_require__(2);

var _classnames2 = _interopRequireDefault(_classnames);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

var _dropdownEditor = __webpack_require__(13);

var _dropdownEditor2 = _interopRequireDefault(_dropdownEditor);

var _textareaEditor = __webpack_require__(14);

var _textareaEditor2 = _interopRequireDefault(_textareaEditor);

var _checkboxEditor = __webpack_require__(15);

var _checkboxEditor2 = _interopRequireDefault(_checkboxEditor);

var _dateEditor = __webpack_require__(16);

var _dateEditor2 = _interopRequireDefault(_dateEditor);

var _textEditor = __webpack_require__(17);

var _textEditor2 = _interopRequireDefault(_textEditor);

var _editorIndicator = __webpack_require__(18);

var _editorIndicator2 = _interopRequireDefault(_editorIndicator);

var _const = __webpack_require__(3);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/prop-types: 0 */
/* eslint no-return-assign: 0 */
/* eslint class-methods-use-this: 0 */
/* eslint jsx-a11y/no-noninteractive-element-interactions: 0 */
/* eslint camelcase: 0 */


exports.default = function (_, onStartEdit) {
  var _class, _temp;

  return _temp = _class = function (_Component) {
    _inherits(EditingCell, _Component);

    function EditingCell(props) {
      _classCallCheck(this, EditingCell);

      var _this = _possibleConstructorReturn(this, (EditingCell.__proto__ || Object.getPrototypeOf(EditingCell)).call(this, props));

      _this.indicatorTimer = null;
      _this.clearTimer = _this.clearTimer.bind(_this);
      _this.handleBlur = _this.handleBlur.bind(_this);
      _this.handleClick = _this.handleClick.bind(_this);
      _this.handleKeyDown = _this.handleKeyDown.bind(_this);
      _this.beforeComplete = _this.beforeComplete.bind(_this);
      _this.asyncbeforeCompete = _this.asyncbeforeCompete.bind(_this);
      _this.displayErrorMessage = _this.displayErrorMessage.bind(_this);
      _this.state = {
        invalidMessage: null
      };
      return _this;
    }

    _createClass(EditingCell, [{
      key: 'componentWillUnmount',
      value: function componentWillUnmount() {
        this.clearTimer();
      }
    }, {
      key: 'UNSAFE_componentWillReceiveProps',
      value: function UNSAFE_componentWillReceiveProps(_ref) {
        var message = _ref.message;

        if (_.isDefined(message)) {
          this.createTimer();
          this.setState(function () {
            return {
              invalidMessage: message
            };
          });
        }
      }
    }, {
      key: 'clearTimer',
      value: function clearTimer() {
        if (this.indicatorTimer) {
          clearTimeout(this.indicatorTimer);
        }
      }
    }, {
      key: 'createTimer',
      value: function createTimer() {
        var _this2 = this;

        this.clearTimer();
        var _props = this.props,
            timeToCloseMessage = _props.timeToCloseMessage,
            onErrorMessageDisappear = _props.onErrorMessageDisappear;

        this.indicatorTimer = _.sleep(function () {
          _this2.setState(function () {
            return {
              invalidMessage: null
            };
          });
          if (_.isFunction(onErrorMessageDisappear)) onErrorMessageDisappear();
        }, timeToCloseMessage);
      }
    }, {
      key: 'displayErrorMessage',
      value: function displayErrorMessage(message) {
        this.setState(function () {
          return {
            invalidMessage: message
          };
        });
        this.createTimer();
      }
    }, {
      key: 'asyncbeforeCompete',
      value: function asyncbeforeCompete(newValue) {
        var _this3 = this;

        return function () {
          var result = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : { valid: true };
          var valid = result.valid,
              message = result.message;
          var _props2 = _this3.props,
              onUpdate = _props2.onUpdate,
              row = _props2.row,
              column = _props2.column;

          if (!valid) {
            _this3.displayErrorMessage(message);
            return;
          }
          onUpdate(row, column, newValue);
        };
      }
    }, {
      key: 'beforeComplete',
      value: function beforeComplete(newValue) {
        var _props3 = this.props,
            onUpdate = _props3.onUpdate,
            row = _props3.row,
            column = _props3.column;

        if (_.isFunction(column.validator)) {
          var validateForm = column.validator(newValue, row, column, this.asyncbeforeCompete(newValue));
          if (_.isObject(validateForm)) {
            if (validateForm.async) {
              return;
            } else if (!validateForm.valid) {
              this.displayErrorMessage(validateForm.message);
              return;
            }
          }
        }
        onUpdate(row, column, newValue);
      }
    }, {
      key: 'handleBlur',
      value: function handleBlur() {
        var _props4 = this.props,
            onEscape = _props4.onEscape,
            blurToSave = _props4.blurToSave;

        if (blurToSave) {
          this.beforeComplete(this.editor.getValue());
        } else {
          onEscape();
        }
      }
    }, {
      key: 'handleKeyDown',
      value: function handleKeyDown(e) {
        var onEscape = this.props.onEscape;

        if (e.keyCode === 27) {
          // ESC
          onEscape();
        } else if (e.keyCode === 13) {
          // ENTER
          this.beforeComplete(this.editor.getValue());
        }
      }
    }, {
      key: 'handleClick',
      value: function handleClick(e) {
        if (e.target.tagName !== 'TD') {
          // To avoid the row selection event be triggered,
          // When user define selectRow.clickToSelect and selectRow.clickToEdit
          // We shouldn't trigger selection event even if user click on the cell editor(input)
          e.stopPropagation();
        }
      }
    }, {
      key: 'render',
      value: function render() {
        var _this4 = this;

        var editor = void 0;
        var _props5 = this.props,
            row = _props5.row,
            column = _props5.column,
            className = _props5.className,
            style = _props5.style,
            rowIndex = _props5.rowIndex,
            columnIndex = _props5.columnIndex,
            autoSelectText = _props5.autoSelectText;
        var dataField = column.dataField;


        var value = _.get(row, dataField);
        var hasError = _.isDefined(this.state.invalidMessage);

        var customEditorClass = column.editorClasses || '';
        if (_.isFunction(column.editorClasses)) {
          customEditorClass = column.editorClasses(value, row, rowIndex, columnIndex);
        }

        var editorStyle = column.editorStyle || {};
        if (_.isFunction(column.editorStyle)) {
          editorStyle = column.editorStyle(value, row, rowIndex, columnIndex);
        }

        var editorClass = (0, _classnames2.default)({
          animated: hasError,
          shake: hasError
        }, customEditorClass);

        var editorProps = {
          ref: function ref(node) {
            return _this4.editor = node;
          },
          defaultValue: value,
          style: editorStyle,
          className: editorClass,
          onKeyDown: this.handleKeyDown,
          onBlur: this.handleBlur
        };

        if (onStartEdit) {
          editorProps.didMount = function () {
            return onStartEdit(row, column, rowIndex, columnIndex);
          };
        }

        var isDefaultEditorDefined = _.isObject(column.editor);

        if (isDefaultEditorDefined) {
          editorProps = _extends({}, editorProps, column.editor);
        } else if (_.isFunction(column.editorRenderer)) {
          editorProps = _extends({}, editorProps, {
            onUpdate: this.beforeComplete
          });
        }

        if (_.isFunction(column.editorRenderer)) {
          editor = column.editorRenderer(editorProps, value, row, column, rowIndex, columnIndex);
        } else if (isDefaultEditorDefined && column.editor.type === _const.EDITTYPE.SELECT) {
          editor = _react2.default.createElement(_dropdownEditor2.default, _extends({}, editorProps, { row: row, column: column }));
        } else if (isDefaultEditorDefined && column.editor.type === _const.EDITTYPE.TEXTAREA) {
          editor = _react2.default.createElement(_textareaEditor2.default, _extends({}, editorProps, { autoSelectText: autoSelectText }));
        } else if (isDefaultEditorDefined && column.editor.type === _const.EDITTYPE.CHECKBOX) {
          editor = _react2.default.createElement(_checkboxEditor2.default, editorProps);
        } else if (isDefaultEditorDefined && column.editor.type === _const.EDITTYPE.DATE) {
          editor = _react2.default.createElement(_dateEditor2.default, editorProps);
        } else {
          editor = _react2.default.createElement(_textEditor2.default, _extends({}, editorProps, { autoSelectText: autoSelectText }));
        }

        return _react2.default.createElement(
          'td',
          {
            className: (0, _classnames2.default)('react-bootstrap-table-editing-cell', className),
            style: style,
            onClick: this.handleClick
          },
          editor,
          hasError ? _react2.default.createElement(_editorIndicator2.default, { invalidMessage: this.state.invalidMessage }) : null
        );
      }
    }]);

    return EditingCell;
  }(_react.Component), _class.propTypes = {
    row: _propTypes2.default.object.isRequired,
    rowIndex: _propTypes2.default.number.isRequired,
    column: _propTypes2.default.object.isRequired,
    columnIndex: _propTypes2.default.number.isRequired,
    onUpdate: _propTypes2.default.func.isRequired,
    onEscape: _propTypes2.default.func.isRequired,
    timeToCloseMessage: _propTypes2.default.number,
    autoSelectText: _propTypes2.default.bool,
    className: _propTypes2.default.string,
    style: _propTypes2.default.object
  }, _class.defaultProps = {
    timeToCloseMessage: _const.TIME_TO_CLOSE_MESSAGE,
    className: null,
    autoSelectText: false,
    style: {}
  }, _temp;
};

/***/ }),
/* 13 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _classnames = __webpack_require__(2);

var _classnames2 = _interopRequireDefault(_classnames);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint no-return-assign: 0 */


var DropDownEditor = function (_Component) {
  _inherits(DropDownEditor, _Component);

  function DropDownEditor(props) {
    _classCallCheck(this, DropDownEditor);

    var _this = _possibleConstructorReturn(this, (DropDownEditor.__proto__ || Object.getPrototypeOf(DropDownEditor)).call(this, props));

    var options = props.options;
    if (props.getOptions) {
      options = props.getOptions(_this.setOptions.bind(_this), {
        row: props.row,
        column: props.column
      }) || [];
    }
    _this.state = { options: options };
    return _this;
  }

  _createClass(DropDownEditor, [{
    key: 'componentDidMount',
    value: function componentDidMount() {
      var _props = this.props,
          defaultValue = _props.defaultValue,
          didMount = _props.didMount;

      this.select.value = defaultValue;
      this.select.focus();
      if (didMount) didMount();
    }
  }, {
    key: 'setOptions',
    value: function setOptions(options) {
      this.setState({ options: options });
    }
  }, {
    key: 'getValue',
    value: function getValue() {
      return this.select.value;
    }
  }, {
    key: 'render',
    value: function render() {
      var _this2 = this;

      var _props2 = this.props,
          defaultValue = _props2.defaultValue,
          didMount = _props2.didMount,
          getOptions = _props2.getOptions,
          className = _props2.className,
          rest = _objectWithoutProperties(_props2, ['defaultValue', 'didMount', 'getOptions', 'className']);

      var editorClass = (0, _classnames2.default)('form-control editor edit-select', className);

      var attr = _extends({}, rest, {
        className: editorClass
      });

      return _react2.default.createElement(
        'select',
        _extends({}, attr, {
          ref: function ref(node) {
            return _this2.select = node;
          },
          defaultValue: defaultValue
        }),
        this.state.options.map(function (_ref) {
          var label = _ref.label,
              value = _ref.value;
          return _react2.default.createElement(
            'option',
            { key: value, value: value },
            label
          );
        })
      );
    }
  }]);

  return DropDownEditor;
}(_react.Component);

DropDownEditor.propTypes = {
  row: _propTypes2.default.object.isRequired,
  column: _propTypes2.default.object.isRequired,
  defaultValue: _propTypes2.default.oneOfType([_propTypes2.default.string, _propTypes2.default.number]),
  className: _propTypes2.default.string,
  style: _propTypes2.default.object,
  options: _propTypes2.default.oneOfType([_propTypes2.default.arrayOf(_propTypes2.default.shape({
    label: _propTypes2.default.string,
    value: _propTypes2.default.any
  }))]),
  didMount: _propTypes2.default.func,
  getOptions: _propTypes2.default.func
};
DropDownEditor.defaultProps = {
  className: '',
  defaultValue: '',
  style: {},
  options: [],
  didMount: undefined,
  getOptions: undefined
};
exports.default = DropDownEditor;

/***/ }),
/* 14 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _classnames = __webpack_require__(2);

var _classnames2 = _interopRequireDefault(_classnames);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint no-return-assign: 0 */


var TextAreaEditor = function (_Component) {
  _inherits(TextAreaEditor, _Component);

  function TextAreaEditor(props) {
    _classCallCheck(this, TextAreaEditor);

    var _this = _possibleConstructorReturn(this, (TextAreaEditor.__proto__ || Object.getPrototypeOf(TextAreaEditor)).call(this, props));

    _this.handleKeyDown = _this.handleKeyDown.bind(_this);
    return _this;
  }

  _createClass(TextAreaEditor, [{
    key: 'componentDidMount',
    value: function componentDidMount() {
      var _props = this.props,
          defaultValue = _props.defaultValue,
          didMount = _props.didMount,
          autoSelectText = _props.autoSelectText;

      this.text.value = defaultValue;
      this.text.focus();
      if (autoSelectText) this.text.select();
      if (didMount) didMount();
    }
  }, {
    key: 'getValue',
    value: function getValue() {
      return this.text.value;
    }
  }, {
    key: 'handleKeyDown',
    value: function handleKeyDown(e) {
      if (e.keyCode === 13 && !e.shiftKey) return;
      if (this.props.onKeyDown) {
        this.props.onKeyDown(e);
      }
    }
  }, {
    key: 'render',
    value: function render() {
      var _this2 = this;

      var _props2 = this.props,
          defaultValue = _props2.defaultValue,
          didMount = _props2.didMount,
          className = _props2.className,
          autoSelectText = _props2.autoSelectText,
          rest = _objectWithoutProperties(_props2, ['defaultValue', 'didMount', 'className', 'autoSelectText']);

      var editorClass = (0, _classnames2.default)('form-control editor edit-textarea', className);
      return _react2.default.createElement('textarea', _extends({
        ref: function ref(node) {
          return _this2.text = node;
        },
        type: 'textarea',
        className: editorClass
      }, rest, {
        onKeyDown: this.handleKeyDown
      }));
    }
  }]);

  return TextAreaEditor;
}(_react.Component);

TextAreaEditor.propTypes = {
  className: _propTypes2.default.oneOfType([_propTypes2.default.string, _propTypes2.default.object]),
  defaultValue: _propTypes2.default.oneOfType([_propTypes2.default.string, _propTypes2.default.number]),
  onKeyDown: _propTypes2.default.func,
  autoSelectText: _propTypes2.default.bool,
  didMount: _propTypes2.default.func
};
TextAreaEditor.defaultProps = {
  className: '',
  defaultValue: '',
  autoSelectText: false,
  onKeyDown: undefined,
  didMount: undefined
};
exports.default = TextAreaEditor;

/***/ }),
/* 15 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _slicedToArray = function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; }();

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _classnames = __webpack_require__(2);

var _classnames2 = _interopRequireDefault(_classnames);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint no-return-assign: 0 */


var CheckBoxEditor = function (_Component) {
  _inherits(CheckBoxEditor, _Component);

  function CheckBoxEditor(props) {
    _classCallCheck(this, CheckBoxEditor);

    var _this = _possibleConstructorReturn(this, (CheckBoxEditor.__proto__ || Object.getPrototypeOf(CheckBoxEditor)).call(this, props));

    _this.state = {
      checked: props.defaultValue.toString() === props.value.split(':')[0]
    };
    _this.handleChange = _this.handleChange.bind(_this);
    return _this;
  }

  _createClass(CheckBoxEditor, [{
    key: 'componentDidMount',
    value: function componentDidMount() {
      var didMount = this.props.didMount;

      this.checkbox.focus();
      if (didMount) didMount();
    }
  }, {
    key: 'getValue',
    value: function getValue() {
      var _props$value$split = this.props.value.split(':'),
          _props$value$split2 = _slicedToArray(_props$value$split, 2),
          positive = _props$value$split2[0],
          negative = _props$value$split2[1];

      return this.checkbox.checked ? positive : negative;
    }
  }, {
    key: 'handleChange',
    value: function handleChange(e) {
      if (this.props.onChange) this.props.onChange(e);
      var target = e.target;

      this.setState(function () {
        return { checked: target.checked };
      });
    }
  }, {
    key: 'render',
    value: function render() {
      var _this2 = this;

      var _props = this.props,
          defaultValue = _props.defaultValue,
          didMount = _props.didMount,
          className = _props.className,
          rest = _objectWithoutProperties(_props, ['defaultValue', 'didMount', 'className']);

      var editorClass = (0, _classnames2.default)('editor edit-chseckbox checkbox', className);
      return _react2.default.createElement('input', _extends({
        ref: function ref(node) {
          return _this2.checkbox = node;
        },
        type: 'checkbox',
        className: editorClass
      }, rest, {
        checked: this.state.checked,
        onChange: this.handleChange
      }));
    }
  }]);

  return CheckBoxEditor;
}(_react.Component);

CheckBoxEditor.propTypes = {
  className: _propTypes2.default.oneOfType([_propTypes2.default.string, _propTypes2.default.object]),
  value: _propTypes2.default.string,
  defaultValue: _propTypes2.default.any,
  onChange: _propTypes2.default.func,
  didMount: _propTypes2.default.func
};
CheckBoxEditor.defaultProps = {
  className: '',
  value: 'on:off',
  defaultValue: false,
  onChange: undefined,
  didMount: undefined
};
exports.default = CheckBoxEditor;

/***/ }),
/* 16 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _classnames = __webpack_require__(2);

var _classnames2 = _interopRequireDefault(_classnames);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint no-return-assign: 0 */


var DateEditor = function (_Component) {
  _inherits(DateEditor, _Component);

  function DateEditor() {
    _classCallCheck(this, DateEditor);

    return _possibleConstructorReturn(this, (DateEditor.__proto__ || Object.getPrototypeOf(DateEditor)).apply(this, arguments));
  }

  _createClass(DateEditor, [{
    key: 'componentDidMount',
    value: function componentDidMount() {
      var _props = this.props,
          defaultValue = _props.defaultValue,
          didMount = _props.didMount;

      this.date.valueAsDate = new Date(defaultValue);
      this.date.focus();
      if (didMount) didMount();
    }
  }, {
    key: 'getValue',
    value: function getValue() {
      return this.date.value;
    }
  }, {
    key: 'render',
    value: function render() {
      var _this2 = this;

      var _props2 = this.props,
          defaultValue = _props2.defaultValue,
          didMount = _props2.didMount,
          className = _props2.className,
          rest = _objectWithoutProperties(_props2, ['defaultValue', 'didMount', 'className']);

      var editorClass = (0, _classnames2.default)('form-control editor edit-date', className);
      return _react2.default.createElement('input', _extends({
        ref: function ref(node) {
          return _this2.date = node;
        },
        type: 'date',
        className: editorClass
      }, rest));
    }
  }]);

  return DateEditor;
}(_react.Component);

DateEditor.propTypes = {
  className: _propTypes2.default.oneOfType([_propTypes2.default.string, _propTypes2.default.object]),
  defaultValue: _propTypes2.default.string,
  didMount: _propTypes2.default.func
};
DateEditor.defaultProps = {
  className: '',
  defaultValue: '',
  didMount: undefined
};
exports.default = DateEditor;

/***/ }),
/* 17 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _classnames = __webpack_require__(2);

var _classnames2 = _interopRequireDefault(_classnames);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint no-return-assign: 0 */


var TextEditor = function (_Component) {
  _inherits(TextEditor, _Component);

  function TextEditor() {
    _classCallCheck(this, TextEditor);

    return _possibleConstructorReturn(this, (TextEditor.__proto__ || Object.getPrototypeOf(TextEditor)).apply(this, arguments));
  }

  _createClass(TextEditor, [{
    key: 'componentDidMount',
    value: function componentDidMount() {
      var _props = this.props,
          defaultValue = _props.defaultValue,
          didMount = _props.didMount,
          autoSelectText = _props.autoSelectText;

      this.text.value = defaultValue;
      this.text.focus();
      if (autoSelectText) this.text.select();
      if (didMount) didMount();
    }
  }, {
    key: 'getValue',
    value: function getValue() {
      return this.text.value;
    }
  }, {
    key: 'render',
    value: function render() {
      var _this2 = this;

      var _props2 = this.props,
          defaultValue = _props2.defaultValue,
          didMount = _props2.didMount,
          className = _props2.className,
          autoSelectText = _props2.autoSelectText,
          rest = _objectWithoutProperties(_props2, ['defaultValue', 'didMount', 'className', 'autoSelectText']);

      var editorClass = (0, _classnames2.default)('form-control editor edit-text', className);
      return _react2.default.createElement('input', _extends({
        ref: function ref(node) {
          return _this2.text = node;
        },
        type: 'text',
        className: editorClass
      }, rest));
    }
  }]);

  return TextEditor;
}(_react.Component);

TextEditor.propTypes = {
  className: _propTypes2.default.oneOfType([_propTypes2.default.string, _propTypes2.default.object]),
  defaultValue: _propTypes2.default.oneOfType([_propTypes2.default.string, _propTypes2.default.number]),
  autoSelectText: _propTypes2.default.bool,
  didMount: _propTypes2.default.func
};
TextEditor.defaultProps = {
  className: null,
  defaultValue: '',
  autoSelectText: false,
  didMount: undefined
};
exports.default = TextEditor;

/***/ }),
/* 18 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/* eslint no-return-assign: 0 */
var EditorIndicator = function EditorIndicator(_ref) {
  var invalidMessage = _ref.invalidMessage;
  return _react2.default.createElement(
    'div',
    { className: 'alert alert-danger in', role: 'alert' },
    _react2.default.createElement(
      'strong',
      null,
      invalidMessage
    )
  );
};

EditorIndicator.propTypes = {
  invalidMessage: _propTypes2.default.string
};

EditorIndicator.defaultProps = {
  invalidMessage: null
};
exports.default = EditorIndicator;

/***/ })
/******/ ]);
});
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly8vd2VicGFjay91bml2ZXJzYWxNb2R1bGVEZWZpbml0aW9uIiwid2VicGFjazovLy93ZWJwYWNrL2Jvb3RzdHJhcCAzYjM1NmY2ZjkwN2Y2NDYyYjI3ZCIsIndlYnBhY2s6Ly8vZXh0ZXJuYWwge1wicm9vdFwiOlwiUmVhY3RcIixcImNvbW1vbmpzMlwiOlwicmVhY3RcIixcImNvbW1vbmpzXCI6XCJyZWFjdFwiLFwiYW1kXCI6XCJyZWFjdFwifSIsIndlYnBhY2s6Ly8vLi9ub2RlX21vZHVsZXMvcHJvcC10eXBlcy9pbmRleC5qcyIsIndlYnBhY2s6Ly8vLi9ub2RlX21vZHVsZXMvY2xhc3NuYW1lcy9pbmRleC5qcyIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLWVkaXRvci9zcmMvY29uc3QuanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1lZGl0b3Ivc3JjL2NvbnRleHQuanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1lZGl0b3IvaW5kZXguanMiLCJ3ZWJwYWNrOi8vLy4vbm9kZV9tb2R1bGVzL3Byb3AtdHlwZXMvZmFjdG9yeVdpdGhUaHJvd2luZ1NoaW1zLmpzIiwid2VicGFjazovLy8uL25vZGVfbW9kdWxlcy9mYmpzL2xpYi9lbXB0eUZ1bmN0aW9uLmpzIiwid2VicGFjazovLy8uL25vZGVfbW9kdWxlcy9mYmpzL2xpYi9pbnZhcmlhbnQuanMiLCJ3ZWJwYWNrOi8vLy4vbm9kZV9tb2R1bGVzL3Byb3AtdHlwZXMvbGliL1JlYWN0UHJvcFR5cGVzU2VjcmV0LmpzIiwid2VicGFjazovLy8uL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZWRpdG9yL3NyYy9yb3ctY29uc3VtZXIuanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1lZGl0b3Ivc3JjL2VkaXRpbmctY2VsbC1jb25zdW1lci5qcyIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLWVkaXRvci9zcmMvZWRpdGluZy1jZWxsLmpzIiwid2VicGFjazovLy8uL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZWRpdG9yL3NyYy9kcm9wZG93bi1lZGl0b3IuanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1lZGl0b3Ivc3JjL3RleHRhcmVhLWVkaXRvci5qcyIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLWVkaXRvci9zcmMvY2hlY2tib3gtZWRpdG9yLmpzIiwid2VicGFjazovLy8uL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZWRpdG9yL3NyYy9kYXRlLWVkaXRvci5qcyIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLWVkaXRvci9zcmMvdGV4dC1lZGl0b3IuanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1lZGl0b3Ivc3JjL2VkaXRvci1pbmRpY2F0b3IuanMiXSwibmFtZXMiOlsiVElNRV9UT19DTE9TRV9NRVNTQUdFIiwiREVMQVlfRk9SX0RCQ0xJQ0siLCJDTElDS19UT19DRUxMX0VESVQiLCJEQkNMSUNLX1RPX0NFTExfRURJVCIsIkVESVRUWVBFIiwiVEVYVCIsIlNFTEVDVCIsIlRFWFRBUkVBIiwiQ0hFQ0tCT1giLCJEQVRFIiwiQ2VsbEVkaXRDb250ZXh0IiwiUmVhY3QiLCJjcmVhdGVDb250ZXh0IiwiXyIsImRhdGFPcGVyYXRvciIsImlzUmVtb3RlQ2VsbEVkaXQiLCJoYW5kbGVDZWxsQ2hhbmdlIiwiQ2VsbEVkaXRQcm92aWRlciIsInByb3BzIiwiZG9VcGRhdGUiLCJiaW5kIiwic3RhcnRFZGl0aW5nIiwiZXNjYXBlRWRpdGluZyIsImNvbXBsZXRlRWRpdGluZyIsImhhbmRsZUNlbGxVcGRhdGUiLCJzdGF0ZSIsInJpZHgiLCJjaWR4IiwibWVzc2FnZSIsIm5leHRQcm9wcyIsImNlbGxFZGl0Iiwib3B0aW9ucyIsImVycm9yTWVzc2FnZSIsInNldFN0YXRlIiwicm93IiwiY29sdW1uIiwibmV3VmFsdWUiLCJuZXdWYWx1ZVdpdGhUeXBlIiwidHlwZUNvbnZlcnQiLCJ0eXBlIiwiYmVmb3JlU2F2ZUNlbGwiLCJvbGRWYWx1ZSIsImdldCIsImRhdGFGaWVsZCIsImJlZm9yZVNhdmVDZWxsRG9uZSIsInJlc3VsdCIsImlzRnVuY3Rpb24iLCJpc09iamVjdCIsImFzeW5jIiwia2V5RmllbGQiLCJkYXRhIiwiYWZ0ZXJTYXZlQ2VsbCIsInJvd0lkIiwiZWRpdENlbGwiLCJlZGl0aW5nIiwic2VsZWN0Um93IiwiY2xpY2tUb0VkaXQiLCJjbGlja1RvU2VsZWN0Iiwibm9uRWRpdGFibGVSb3dzIiwib3B0aW9uc1Jlc3QiLCJjZWxsRWRpdFJlc3QiLCJuZXdDZWxsRWRpdCIsImlzRGVmaW5lZCIsIm9uU3RhcnQiLCJvbkVzY2FwZSIsIm9uVXBkYXRlIiwiY2hpbGRyZW4iLCJDb21wb25lbnQiLCJwcm9wVHlwZXMiLCJQcm9wVHlwZXMiLCJhcnJheSIsImlzUmVxdWlyZWQiLCJvYmplY3QiLCJzaGFwZSIsIm1vZGUiLCJvbmVPZiIsIm9uRXJyb3JNZXNzYWdlRGlzYXBwZWFyIiwiZnVuYyIsImJsdXJUb1NhdmUiLCJib29sIiwib25TdGFydEVkaXQiLCJ0aW1lVG9DbG9zZU1lc3NhZ2UiLCJudW1iZXIiLCJhbnkiLCJQcm92aWRlciIsIkNvbnN1bWVyIiwiY3JlYXRlRWRpdGluZ0NlbGwiLCJ3aXRoUm93TGV2ZWxDZWxsRWRpdCIsIlR5cGUiLCJzZWxlY3RSb3dFbmFibGVkIiwicmVuZGVyV2l0aENlbGxFZGl0Iiwia2V5IiwidmFsdWUiLCJlZGl0YWJsZVJvdyIsImxlbmd0aCIsImluZGV4T2YiLCJhdHRycyIsIndpdGhDb25zdW1lciIsImRpc3BsYXlOYW1lIiwiRWRpdGluZ0NlbGwiLCJyZW5kZXJXaXRoRWRpdGluZ0NlbGwiLCJjb250ZW50IiwiZWRpdENlbGxzdHlsZSIsImVkaXRDZWxsU3R5bGUiLCJlZGl0Q2VsbGNsYXNzZXMiLCJlZGl0Q2VsbENsYXNzZXMiLCJyb3dJbmRleCIsImNvbHVtbkluZGV4IiwiaW5kaWNhdG9yVGltZXIiLCJjbGVhclRpbWVyIiwiaGFuZGxlQmx1ciIsImhhbmRsZUNsaWNrIiwiaGFuZGxlS2V5RG93biIsImJlZm9yZUNvbXBsZXRlIiwiYXN5bmNiZWZvcmVDb21wZXRlIiwiZGlzcGxheUVycm9yTWVzc2FnZSIsImludmFsaWRNZXNzYWdlIiwiY3JlYXRlVGltZXIiLCJjbGVhclRpbWVvdXQiLCJzbGVlcCIsInZhbGlkIiwidmFsaWRhdG9yIiwidmFsaWRhdGVGb3JtIiwiZWRpdG9yIiwiZ2V0VmFsdWUiLCJlIiwia2V5Q29kZSIsInRhcmdldCIsInRhZ05hbWUiLCJzdG9wUHJvcGFnYXRpb24iLCJjbGFzc05hbWUiLCJzdHlsZSIsImF1dG9TZWxlY3RUZXh0IiwiaGFzRXJyb3IiLCJjdXN0b21FZGl0b3JDbGFzcyIsImVkaXRvckNsYXNzZXMiLCJlZGl0b3JTdHlsZSIsImVkaXRvckNsYXNzIiwiYW5pbWF0ZWQiLCJzaGFrZSIsImVkaXRvclByb3BzIiwicmVmIiwibm9kZSIsImRlZmF1bHRWYWx1ZSIsIm9uS2V5RG93biIsIm9uQmx1ciIsImRpZE1vdW50IiwiaXNEZWZhdWx0RWRpdG9yRGVmaW5lZCIsImVkaXRvclJlbmRlcmVyIiwic3RyaW5nIiwiZGVmYXVsdFByb3BzIiwiRHJvcERvd25FZGl0b3IiLCJnZXRPcHRpb25zIiwic2V0T3B0aW9ucyIsInNlbGVjdCIsImZvY3VzIiwicmVzdCIsImF0dHIiLCJtYXAiLCJsYWJlbCIsIm9uZU9mVHlwZSIsImFycmF5T2YiLCJ1bmRlZmluZWQiLCJUZXh0QXJlYUVkaXRvciIsInRleHQiLCJzaGlmdEtleSIsIkNoZWNrQm94RWRpdG9yIiwiY2hlY2tlZCIsInRvU3RyaW5nIiwic3BsaXQiLCJoYW5kbGVDaGFuZ2UiLCJjaGVja2JveCIsInBvc2l0aXZlIiwibmVnYXRpdmUiLCJvbkNoYW5nZSIsIkRhdGVFZGl0b3IiLCJkYXRlIiwidmFsdWVBc0RhdGUiLCJEYXRlIiwiVGV4dEVkaXRvciIsIkVkaXRvckluZGljYXRvciJdLCJtYXBwaW5ncyI6IkFBQUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsQ0FBQztBQUNELE87UUNWQTtRQUNBOztRQUVBO1FBQ0E7O1FBRUE7UUFDQTtRQUNBO1FBQ0E7UUFDQTtRQUNBO1FBQ0E7UUFDQTtRQUNBO1FBQ0E7O1FBRUE7UUFDQTs7UUFFQTtRQUNBOztRQUVBO1FBQ0E7UUFDQTs7O1FBR0E7UUFDQTs7UUFFQTtRQUNBOztRQUVBO1FBQ0E7UUFDQTtRQUNBO1FBQ0E7UUFDQTtRQUNBO1FBQ0EsS0FBSztRQUNMO1FBQ0E7O1FBRUE7UUFDQTtRQUNBO1FBQ0EsMkJBQTJCLDBCQUEwQixFQUFFO1FBQ3ZELGlDQUFpQyxlQUFlO1FBQ2hEO1FBQ0E7UUFDQTs7UUFFQTtRQUNBLHNEQUFzRCwrREFBK0Q7O1FBRXJIO1FBQ0E7O1FBRUE7UUFDQTs7Ozs7OztBQzdEQSwrQzs7Ozs7O0FDQUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQSxJQUFJLEtBQXFDO0FBQ3pDO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQSxDQUFDO0FBQ0Q7QUFDQTtBQUNBLG1CQUFtQixtQkFBTyxDQUFDLENBQTRCO0FBQ3ZEOzs7Ozs7O0FDN0JBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBOztBQUVBLGdCQUFnQjs7QUFFaEI7QUFDQTs7QUFFQSxpQkFBaUIsc0JBQXNCO0FBQ3ZDO0FBQ0E7O0FBRUE7O0FBRUE7QUFDQTtBQUNBLElBQUk7QUFDSjtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQSxJQUFJO0FBQ0o7QUFDQTtBQUNB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S2tDLElBQUwsR0FBWXRCLElBQXBCO0FBQUEsU0FEUjtBQUVFLGNBQUssTUFGUDtBQUdFLG1CQUFZTDtBQUhkLFNBSU9tQixJQUpQLEVBREY7QUFRRDs7OztFQXhCc0J6RixnQjs7QUEyQnpCZ0gsV0FBVy9HLFNBQVgsR0FBdUI7QUFDckI4RCxhQUFXN0Qsb0JBQVUyRixTQUFWLENBQW9CLENBQzdCM0Ysb0JBQVVnRixNQURtQixFQUU3QmhGLG9CQUFVRyxNQUZtQixDQUFwQixDQURVO0FBS3JCdUUsZ0JBQWMxRSxvQkFBVTJGLFNBQVYsQ0FBb0IsQ0FDaEMzRixvQkFBVWdGLE1BRHNCLEVBRWhDaEYsb0JBQVVhLE1BRnNCLENBQXBCLENBTE87QUFTckJrRCxrQkFBZ0IvRCxvQkFBVVUsSUFUTDtBQVVyQm1FLFlBQVU3RSxvQkFBVVE7QUFWQyxDQUF2QjtBQVlBc0csV0FBVzdCLFlBQVgsR0FBMEI7QUFDeEJwQixhQUFXLElBRGE7QUFFeEJhLGdCQUFjLEVBRlU7QUFHeEJYLGtCQUFnQixLQUhRO0FBSXhCYyxZQUFVZ0I7QUFKYyxDQUExQjtrQkFNZWlCLFU7Ozs7Ozs7Ozs7Ozs7QUNqRGY7Ozs7QUFDQTs7Ozs7O0FBRkE7QUFJQSxJQUFNQyxrQkFBa0IsU0FBbEJBLGVBQWtCO0FBQUEsTUFBR2hFLGNBQUgsUUFBR0EsY0FBSDtBQUFBLFNBRXBCO0FBQUE7QUFBQSxNQUFLLFdBQVUsdUJBQWYsRUFBdUMsTUFBSyxPQUE1QztBQUNFO0FBQUE7QUFBQTtBQUFVQTtBQUFWO0FBREYsR0FGb0I7QUFBQSxDQUF4Qjs7QUFPQWdFLGdCQUFnQmhILFNBQWhCLEdBQTRCO0FBQzFCZ0Qsa0JBQWdCL0Msb0JBQVVnRjtBQURBLENBQTVCOztBQUlBK0IsZ0JBQWdCOUIsWUFBaEIsR0FBK0I7QUFDN0JsQyxrQkFBZ0I7QUFEYSxDQUEvQjtrQkFHZWdFLGUiLCJmaWxlIjoicmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1lZGl0b3IvZGlzdC9yZWFjdC1ib290c3RyYXAtdGFibGUyLWVkaXRvci5qcyIsInNvdXJjZXNDb250ZW50IjpbIihmdW5jdGlvbiB3ZWJwYWNrVW5pdmVyc2FsTW9kdWxlRGVmaW5pdGlvbihyb290LCBmYWN0b3J5KSB7XG5cdGlmKHR5cGVvZiBleHBvcnRzID09PSAnb2JqZWN0JyAmJiB0eXBlb2YgbW9kdWxlID09PSAnb2JqZWN0Jylcblx0XHRtb2R1bGUuZXhwb3J0cyA9IGZhY3RvcnkocmVxdWlyZShcInJlYWN0XCIpKTtcblx0ZWxzZSBpZih0eXBlb2YgZGVmaW5lID09PSAnZnVuY3Rpb24nICYmIGRlZmluZS5hbWQpXG5cdFx0ZGVmaW5lKFtcInJlYWN0XCJdLCBmYWN0b3J5KTtcblx0ZWxzZSBpZih0eXBlb2YgZXhwb3J0cyA9PT0gJ29iamVjdCcpXG5cdFx0ZXhwb3J0c1tcIlJlYWN0Qm9vdHN0cmFwVGFibGUyRWRpdG9yXCJdID0gZmFjdG9yeShyZXF1aXJlKFwicmVhY3RcIikpO1xuXHRlbHNlXG5cdFx0cm9vdFtcIlJlYWN0Qm9vdHN0cmFwVGFibGUyRWRpdG9yXCJdID0gZmFjdG9yeShyb290W1wiUmVhY3RcIl0pO1xufSkodGhpcywgZnVuY3Rpb24oX19XRUJQQUNLX0VYVEVSTkFMX01PRFVMRV8wX18pIHtcbnJldHVybiBcblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gd2VicGFjay91bml2ZXJzYWxNb2R1bGVEZWZpbml0aW9uIiwiIFx0Ly8gVGhlIG1vZHVsZSBjYWNoZVxuIFx0dmFyIGluc3RhbGxlZE1vZHVsZXMgPSB7fTtcblxuIFx0Ly8gVGhlIHJlcXVpcmUgZnVuY3Rpb25cbiBcdGZ1bmN0aW9uIF9fd2VicGFja19yZXF1aXJlX18obW9kdWxlSWQpIHtcblxuIFx0XHQvLyBDaGVjayBpZiBtb2R1bGUgaXMgaW4gY2FjaGVcbiBcdFx0aWYoaW5zdGFsbGVkTW9kdWxlc1ttb2R1bGVJZF0pIHtcbiBcdFx0XHRyZXR1cm4gaW5zdGFsbGVkTW9kdWxlc1ttb2R1bGVJZF0uZXhwb3J0cztcbiBcdFx0fVxuIFx0XHQvLyBDcmVhdGUgYSBuZXcgbW9kdWxlIChhbmQgcHV0IGl0IGludG8gdGhlIGNhY2hlKVxuIFx0XHR2YXIgbW9kdWxlID0gaW5zdGFsbGVkTW9kdWxlc1ttb2R1bGVJZF0gPSB7XG4gXHRcdFx0aTogbW9kdWxlSWQsXG4gXHRcdFx0bDogZmFsc2UsXG4gXHRcdFx0ZXhwb3J0czoge31cbiBcdFx0fTtcblxuIFx0XHQvLyBFeGVjdXRlIHRoZSBtb2R1bGUgZnVuY3Rpb25cbiBcdFx0bW9kdWxlc1ttb2R1bGVJZF0uY2FsbChtb2R1bGUuZXhwb3J0cywgbW9kdWxlLCBtb2R1bGUuZXhwb3J0cywgX193ZWJwYWNrX3JlcXVpcmVfXyk7XG5cbiBcdFx0Ly8gRmxhZyB0aGUgbW9kdWxlIGFzIGxvYWRlZFxuIFx0XHRtb2R1bGUubCA9IHRydWU7XG5cbiBcdFx0Ly8gUmV0dXJuIHRoZSBleHBvcnRzIG9mIHRoZSBtb2R1bGVcbiBcdFx0cmV0dXJuIG1vZHVsZS5leHBvcnRzO1xuIFx0fVxuXG5cbiBcdC8vIGV4cG9zZSB0aGUgbW9kdWxlcyBvYmplY3QgKF9fd2VicGFja19tb2R1bGVzX18pXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLm0gPSBtb2R1bGVzO1xuXG4gXHQvLyBleHBvc2UgdGhlIG1vZHVsZSBjYWNoZVxuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5jID0gaW5zdGFsbGVkTW9kdWxlcztcblxuIFx0Ly8gZGVmaW5lIGdldHRlciBmdW5jdGlvbiBmb3IgaGFybW9ueSBleHBvcnRzXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLmQgPSBmdW5jdGlvbihleHBvcnRzLCBuYW1lLCBnZXR0ZXIpIHtcbiBcdFx0aWYoIV9fd2VicGFja19yZXF1aXJlX18ubyhleHBvcnRzLCBuYW1lKSkge1xuIFx0XHRcdE9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBuYW1lLCB7XG4gXHRcdFx0XHRjb25maWd1cmFibGU6IGZhbHNlLFxuIFx0XHRcdFx0ZW51bWVyYWJsZTogdHJ1ZSxcbiBcdFx0XHRcdGdldDogZ2V0dGVyXG4gXHRcdFx0fSk7XG4gXHRcdH1cbiBcdH07XG5cbiBcdC8vIGdldERlZmF1bHRFeHBvcnQgZnVuY3Rpb24gZm9yIGNvbXBhdGliaWxpdHkgd2l0aCBub24taGFybW9ueSBtb2R1bGVzXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLm4gPSBmdW5jdGlvbihtb2R1bGUpIHtcbiBcdFx0dmFyIGdldHRlciA9IG1vZHVsZSAmJiBtb2R1bGUuX19lc01vZHVsZSA/XG4gXHRcdFx0ZnVuY3Rpb24gZ2V0RGVmYXVsdCgpIHsgcmV0dXJuIG1vZHVsZVsnZGVmYXVsdCddOyB9IDpcbiBcdFx0XHRmdW5jdGlvbiBnZXRNb2R1bGVFeHBvcnRzKCkgeyByZXR1cm4gbW9kdWxlOyB9O1xuIFx0XHRfX3dlYnBhY2tfcmVxdWlyZV9fLmQoZ2V0dGVyLCAnYScsIGdldHRlcik7XG4gXHRcdHJldHVybiBnZXR0ZXI7XG4gXHR9O1xuXG4gXHQvLyBPYmplY3QucHJvdG90eXBlLmhhc093blByb3BlcnR5LmNhbGxcbiBcdF9fd2VicGFja19yZXF1aXJlX18ubyA9IGZ1bmN0aW9uKG9iamVjdCwgcHJvcGVydHkpIHsgcmV0dXJuIE9iamVjdC5wcm90b3R5cGUuaGFzT3duUHJvcGVydHkuY2FsbChvYmplY3QsIHByb3BlcnR5KTsgfTtcblxuIFx0Ly8gX193ZWJwYWNrX3B1YmxpY19wYXRoX19cbiBcdF9fd2VicGFja19yZXF1aXJlX18ucCA9IFwiXCI7XG5cbiBcdC8vIExvYWQgZW50cnkgbW9kdWxlIGFuZCByZXR1cm4gZXhwb3J0c1xuIFx0cmV0dXJuIF9fd2VicGFja19yZXF1aXJlX18oX193ZWJwYWNrX3JlcXVpcmVfXy5zID0gNSk7XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gd2VicGFjay9ib290c3RyYXAgM2IzNTZmNmY5MDdmNjQ2MmIyN2QiLCJtb2R1bGUuZXhwb3J0cyA9IF9fV0VCUEFDS19FWFRFUk5BTF9NT0RVTEVfMF9fO1xuXG5cbi8vLy8vLy8vLy8vLy8vLy8vL1xuLy8gV0VCUEFDSyBGT09URVJcbi8vIGV4dGVybmFsIHtcInJvb3RcIjpcIlJlYWN0XCIsXCJjb21tb25qczJcIjpcInJlYWN0XCIsXCJjb21tb25qc1wiOlwicmVhY3RcIixcImFtZFwiOlwicmVhY3RcIn1cbi8vIG1vZHVsZSBpZCA9IDBcbi8vIG1vZHVsZSBjaHVua3MgPSAwIDEiLCIvKipcbiAqIENvcHlyaWdodCAyMDEzLXByZXNlbnQsIEZhY2Vib29rLCBJbmMuXG4gKiBBbGwgcmlnaHRzIHJlc2VydmVkLlxuICpcbiAqIFRoaXMgc291cmNlIGNvZGUgaXMgbGljZW5zZWQgdW5kZXIgdGhlIEJTRC1zdHlsZSBsaWNlbnNlIGZvdW5kIGluIHRoZVxuICogTElDRU5TRSBmaWxlIGluIHRoZSByb290IGRpcmVjdG9yeSBvZiB0aGlzIHNvdXJjZSB0cmVlLiBBbiBhZGRpdGlvbmFsIGdyYW50XG4gKiBvZiBwYXRlbnQgcmlnaHRzIGNhbiBiZSBmb3VuZCBpbiB0aGUgUEFURU5UUyBmaWxlIGluIHRoZSBzYW1lIGRpcmVjdG9yeS5cbiAqL1xuXG5pZiAocHJvY2Vzcy5lbnYuTk9ERV9FTlYgIT09ICdwcm9kdWN0aW9uJykge1xuICB2YXIgUkVBQ1RfRUxFTUVOVF9UWVBFID0gKHR5cGVvZiBTeW1ib2wgPT09ICdmdW5jdGlvbicgJiZcbiAgICBTeW1ib2wuZm9yICYmXG4gICAgU3ltYm9sLmZvcigncmVhY3QuZWxlbWVudCcpKSB8fFxuICAgIDB4ZWFjNztcblxuICB2YXIgaXNWYWxpZEVsZW1lbnQgPSBmdW5jdGlvbihvYmplY3QpIHtcbiAgICByZXR1cm4gdHlwZW9mIG9iamVjdCA9PT0gJ29iamVjdCcgJiZcbiAgICAgIG9iamVjdCAhPT0gbnVsbCAmJlxuICAgICAgb2JqZWN0LiQkdHlwZW9mID09PSBSRUFDVF9FTEVNRU5UX1RZUEU7XG4gIH07XG5cbiAgLy8gQnkgZXhwbGljaXRseSB1c2luZyBgcHJvcC10eXBlc2AgeW91IGFyZSBvcHRpbmcgaW50byBuZXcgZGV2ZWxvcG1lbnQgYmVoYXZpb3IuXG4gIC8vIGh0dHA6Ly9mYi5tZS9wcm9wLXR5cGVzLWluLXByb2RcbiAgdmFyIHRocm93T25EaXJlY3RBY2Nlc3MgPSB0cnVlO1xuICBtb2R1bGUuZXhwb3J0cyA9IHJlcXVpcmUoJy4vZmFjdG9yeVdpdGhUeXBlQ2hlY2tlcnMnKShpc1ZhbGlkRWxlbWVudCwgdGhyb3dPbkRpcmVjdEFjY2Vzcyk7XG59IGVsc2Uge1xuICAvLyBCeSBleHBsaWNpdGx5IHVzaW5nIGBwcm9wLXR5cGVzYCB5b3UgYXJlIG9wdGluZyBpbnRvIG5ldyBwcm9kdWN0aW9uIGJlaGF2aW9yLlxuICAvLyBodHRwOi8vZmIubWUvcHJvcC10eXBlcy1pbi1wcm9kXG4gIG1vZHVsZS5leHBvcnRzID0gcmVxdWlyZSgnLi9mYWN0b3J5V2l0aFRocm93aW5nU2hpbXMnKSgpO1xufVxuXG5cblxuLy8vLy8vLy8vLy8vLy8vLy8vXG4vLyBXRUJQQUNLIEZPT1RFUlxuLy8gLi9ub2RlX21vZHVsZXMvcHJvcC10eXBlcy9pbmRleC5qc1xuLy8gbW9kdWxlIGlkID0gMVxuLy8gbW9kdWxlIGNodW5rcyA9IDAgMSIsIi8qIVxuICBDb3B5cmlnaHQgKGMpIDIwMTggSmVkIFdhdHNvbi5cbiAgTGljZW5zZWQgdW5kZXIgdGhlIE1JVCBMaWNlbnNlIChNSVQpLCBzZWVcbiAgaHR0cDovL2plZHdhdHNvbi5naXRodWIuaW8vY2xhc3NuYW1lc1xuKi9cbi8qIGdsb2JhbCBkZWZpbmUgKi9cblxuKGZ1bmN0aW9uICgpIHtcblx0J3VzZSBzdHJpY3QnO1xuXG5cdHZhciBoYXNPd24gPSB7fS5oYXNPd25Qcm9wZXJ0eTtcblxuXHRmdW5jdGlvbiBjbGFzc05hbWVzKCkge1xuXHRcdHZhciBjbGFzc2VzID0gW107XG5cblx0XHRmb3IgKHZhciBpID0gMDsgaSA8IGFyZ3VtZW50cy5sZW5ndGg7IGkrKykge1xuXHRcdFx0dmFyIGFyZyA9IGFyZ3VtZW50c1tpXTtcblx0XHRcdGlmICghYXJnKSBjb250aW51ZTtcblxuXHRcdFx0dmFyIGFyZ1R5cGUgPSB0eXBlb2YgYXJnO1xuXG5cdFx0XHRpZiAoYXJnVHlwZSA9PT0gJ3N0cmluZycgfHwgYXJnVHlwZSA9PT0gJ251bWJlcicpIHtcblx0XHRcdFx0Y2xhc3Nlcy5wdXNoKGFyZyk7XG5cdFx0XHR9IGVsc2UgaWYgKEFycmF5LmlzQXJyYXkoYXJnKSkge1xuXHRcdFx0XHRpZiAoYXJnLmxlbmd0aCkge1xuXHRcdFx0XHRcdHZhciBpbm5lciA9IGNsYXNzTmFtZXMuYXBwbHkobnVsbCwgYXJnKTtcblx0XHRcdFx0XHRpZiAoaW5uZXIpIHtcblx0XHRcdFx0XHRcdGNsYXNzZXMucHVzaChpbm5lcik7XG5cdFx0XHRcdFx0fVxuXHRcdFx0XHR9XG5cdFx0XHR9IGVsc2UgaWYgKGFyZ1R5cGUgPT09ICdvYmplY3QnKSB7XG5cdFx0XHRcdGlmIChhcmcudG9TdHJpbmcgPT09IE9iamVjdC5wcm90b3R5cGUudG9TdHJpbmcpIHtcblx0XHRcdFx0XHRmb3IgKHZhciBrZXkgaW4gYXJnKSB7XG5cdFx0XHRcdFx0XHRpZiAoaGFzT3duLmNhbGwoYXJnLCBrZXkpICYmIGFyZ1trZXldKSB7XG5cdFx0XHRcdFx0XHRcdGNsYXNzZXMucHVzaChrZXkpO1xuXHRcdFx0XHRcdFx0fVxuXHRcdFx0XHRcdH1cblx0XHRcdFx0fSBlbHNlIHtcblx0XHRcdFx0XHRjbGFzc2VzLnB1c2goYXJnLnRvU3RyaW5nKCkpO1xuXHRcdFx0XHR9XG5cdFx0XHR9XG5cdFx0fVxuXG5cdFx0cmV0dXJuIGNsYXNzZXMuam9pbignICcpO1xuXHR9XG5cblx0aWYgKHR5cGVvZiBtb2R1bGUgIT09ICd1bmRlZmluZWQnICYmIG1vZHVsZS5leHBvcnRzKSB7XG5cdFx0Y2xhc3NOYW1lcy5kZWZhdWx0ID0gY2xhc3NOYW1lcztcblx0XHRtb2R1bGUuZXhwb3J0cyA9IGNsYXNzTmFtZXM7XG5cdH0gZWxzZSBpZiAodHlwZW9mIGRlZmluZSA9PT0gJ2Z1bmN0aW9uJyAmJiB0eXBlb2YgZGVmaW5lLmFtZCA9PT0gJ29iamVjdCcgJiYgZGVmaW5lLmFtZCkge1xuXHRcdC8vIHJlZ2lzdGVyIGFzICdjbGFzc25hbWVzJywgY29uc2lzdGVudCB3aXRoIG5wbSBwYWNrYWdlIG5hbWVcblx0XHRkZWZpbmUoJ2NsYXNzbmFtZXMnLCBbXSwgZnVuY3Rpb24gKCkge1xuXHRcdFx0cmV0dXJuIGNsYXNzTmFtZXM7XG5cdFx0fSk7XG5cdH0gZWxzZSB7XG5cdFx0d2luZG93LmNsYXNzTmFtZXMgPSBjbGFzc05hbWVzO1xuXHR9XG59KCkpO1xuXG5cblxuLy8vLy8vLy8vLy8vLy8vLy8vXG4vLyBXRUJQQUNLIEZPT1RFUlxuLy8gLi9ub2RlX21vZHVsZXMvY2xhc3NuYW1lcy9pbmRleC5qc1xuLy8gbW9kdWxlIGlkID0gMlxuLy8gbW9kdWxlIGNodW5rcyA9IDAgMSIsImV4cG9ydCBjb25zdCBUSU1FX1RPX0NMT1NFX01FU1NBR0UgPSAzMDAwO1xuZXhwb3J0IGNvbnN0IERFTEFZX0ZPUl9EQkNMSUNLID0gMjAwO1xuZXhwb3J0IGNvbnN0IENMSUNLX1RPX0NFTExfRURJVCA9ICdjbGljayc7XG5leHBvcnQgY29uc3QgREJDTElDS19UT19DRUxMX0VESVQgPSAnZGJjbGljayc7XG5cbmV4cG9ydCBjb25zdCBFRElUVFlQRSA9IHtcbiAgVEVYVDogJ3RleHQnLFxuICBTRUxFQ1Q6ICdzZWxlY3QnLFxuICBURVhUQVJFQTogJ3RleHRhcmVhJyxcbiAgQ0hFQ0tCT1g6ICdjaGVja2JveCcsXG4gIERBVEU6ICdkYXRlJ1xufTtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyAuL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZWRpdG9yL3NyYy9jb25zdC5qcyIsIi8qIGVzbGludCBkaXNhYmxlLW5leHQtbGluZTogMCAqL1xuLyogZXNsaW50IHJlYWN0L3Byb3AtdHlwZXM6IDAgKi9cbi8qIGVzbGludCByZWFjdC9yZXF1aXJlLWRlZmF1bHQtcHJvcHM6IDAgKi9cbi8qIGVzbGludCBjYW1lbGNhc2U6IDAgKi9cbi8qIGVzbGludCByZWFjdC9uby11bnVzZWQtcHJvcC10eXBlczogMCAqL1xuaW1wb3J0IFJlYWN0IGZyb20gJ3JlYWN0JztcbmltcG9ydCBQcm9wVHlwZXMgZnJvbSAncHJvcC10eXBlcyc7XG5pbXBvcnQgeyBDTElDS19UT19DRUxMX0VESVQsIERCQ0xJQ0tfVE9fQ0VMTF9FRElUIH0gZnJvbSAnLi9jb25zdCc7XG5cbmNvbnN0IENlbGxFZGl0Q29udGV4dCA9IFJlYWN0LmNyZWF0ZUNvbnRleHQoKTtcblxuZXhwb3J0IGRlZmF1bHQgKFxuICBfLFxuICBkYXRhT3BlcmF0b3IsXG4gIGlzUmVtb3RlQ2VsbEVkaXQsXG4gIGhhbmRsZUNlbGxDaGFuZ2VcbikgPT4ge1xuICBjbGFzcyBDZWxsRWRpdFByb3ZpZGVyIGV4dGVuZHMgUmVhY3QuQ29tcG9uZW50IHtcbiAgICBzdGF0aWMgcHJvcFR5cGVzID0ge1xuICAgICAgZGF0YTogUHJvcFR5cGVzLmFycmF5LmlzUmVxdWlyZWQsXG4gICAgICBzZWxlY3RSb3c6IFByb3BUeXBlcy5vYmplY3QsXG4gICAgICBvcHRpb25zOiBQcm9wVHlwZXMuc2hhcGUoe1xuICAgICAgICBtb2RlOiBQcm9wVHlwZXMub25lT2YoW0NMSUNLX1RPX0NFTExfRURJVCwgREJDTElDS19UT19DRUxMX0VESVRdKS5pc1JlcXVpcmVkLFxuICAgICAgICBvbkVycm9yTWVzc2FnZURpc2FwcGVhcjogUHJvcFR5cGVzLmZ1bmMsXG4gICAgICAgIGJsdXJUb1NhdmU6IFByb3BUeXBlcy5ib29sLFxuICAgICAgICBiZWZvcmVTYXZlQ2VsbDogUHJvcFR5cGVzLmZ1bmMsXG4gICAgICAgIGFmdGVyU2F2ZUNlbGw6IFByb3BUeXBlcy5mdW5jLFxuICAgICAgICBvblN0YXJ0RWRpdDogUHJvcFR5cGVzLmZ1bmMsXG4gICAgICAgIG5vbkVkaXRhYmxlUm93czogUHJvcFR5cGVzLmZ1bmMsXG4gICAgICAgIHRpbWVUb0Nsb3NlTWVzc2FnZTogUHJvcFR5cGVzLm51bWJlcixcbiAgICAgICAgZXJyb3JNZXNzYWdlOiBQcm9wVHlwZXMuYW55XG4gICAgICB9KVxuICAgIH1cblxuICAgIGNvbnN0cnVjdG9yKHByb3BzKSB7XG4gICAgICBzdXBlcihwcm9wcyk7XG4gICAgICB0aGlzLmRvVXBkYXRlID0gdGhpcy5kb1VwZGF0ZS5iaW5kKHRoaXMpO1xuICAgICAgdGhpcy5zdGFydEVkaXRpbmcgPSB0aGlzLnN0YXJ0RWRpdGluZy5iaW5kKHRoaXMpO1xuICAgICAgdGhpcy5lc2NhcGVFZGl0aW5nID0gdGhpcy5lc2NhcGVFZGl0aW5nLmJpbmQodGhpcyk7XG4gICAgICB0aGlzLmNvbXBsZXRlRWRpdGluZyA9IHRoaXMuY29tcGxldGVFZGl0aW5nLmJpbmQodGhpcyk7XG4gICAgICB0aGlzLmhhbmRsZUNlbGxVcGRhdGUgPSB0aGlzLmhhbmRsZUNlbGxVcGRhdGUuYmluZCh0aGlzKTtcbiAgICAgIHRoaXMuc3RhdGUgPSB7XG4gICAgICAgIHJpZHg6IG51bGwsXG4gICAgICAgIGNpZHg6IG51bGwsXG4gICAgICAgIG1lc3NhZ2U6IG51bGxcbiAgICAgIH07XG4gICAgfVxuXG4gICAgVU5TQUZFX2NvbXBvbmVudFdpbGxSZWNlaXZlUHJvcHMobmV4dFByb3BzKSB7XG4gICAgICBpZiAobmV4dFByb3BzLmNlbGxFZGl0ICYmIGlzUmVtb3RlQ2VsbEVkaXQoKSkge1xuICAgICAgICBpZiAobmV4dFByb3BzLmNlbGxFZGl0Lm9wdGlvbnMuZXJyb3JNZXNzYWdlKSB7XG4gICAgICAgICAgdGhpcy5zZXRTdGF0ZSgoKSA9PiAoe1xuICAgICAgICAgICAgbWVzc2FnZTogbmV4dFByb3BzLmNlbGxFZGl0Lm9wdGlvbnMuZXJyb3JNZXNzYWdlXG4gICAgICAgICAgfSkpO1xuICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgIHRoaXMuZXNjYXBlRWRpdGluZygpO1xuICAgICAgICB9XG4gICAgICB9XG4gICAgfVxuXG4gICAgaGFuZGxlQ2VsbFVwZGF0ZShyb3csIGNvbHVtbiwgbmV3VmFsdWUpIHtcbiAgICAgIGNvbnN0IG5ld1ZhbHVlV2l0aFR5cGUgPSBkYXRhT3BlcmF0b3IudHlwZUNvbnZlcnQoY29sdW1uLnR5cGUsIG5ld1ZhbHVlKTtcbiAgICAgIGNvbnN0IHsgY2VsbEVkaXQgfSA9IHRoaXMucHJvcHM7XG4gICAgICBjb25zdCB7IGJlZm9yZVNhdmVDZWxsIH0gPSBjZWxsRWRpdC5vcHRpb25zO1xuICAgICAgY29uc3Qgb2xkVmFsdWUgPSBfLmdldChyb3csIGNvbHVtbi5kYXRhRmllbGQpO1xuICAgICAgY29uc3QgYmVmb3JlU2F2ZUNlbGxEb25lID0gKHJlc3VsdCA9IHRydWUpID0+IHtcbiAgICAgICAgaWYgKHJlc3VsdCkge1xuICAgICAgICAgIHRoaXMuZG9VcGRhdGUocm93LCBjb2x1bW4sIG5ld1ZhbHVlV2l0aFR5cGUpO1xuICAgICAgICB9IGVsc2Uge1xuICAgICAgICAgIHRoaXMuZXNjYXBlRWRpdGluZygpO1xuICAgICAgICB9XG4gICAgICB9O1xuICAgICAgaWYgKF8uaXNGdW5jdGlvbihiZWZvcmVTYXZlQ2VsbCkpIHtcbiAgICAgICAgY29uc3QgcmVzdWx0ID0gYmVmb3JlU2F2ZUNlbGwoXG4gICAgICAgICAgb2xkVmFsdWUsXG4gICAgICAgICAgbmV3VmFsdWVXaXRoVHlwZSxcbiAgICAgICAgICByb3csXG4gICAgICAgICAgY29sdW1uLFxuICAgICAgICAgIGJlZm9yZVNhdmVDZWxsRG9uZVxuICAgICAgICApO1xuICAgICAgICBpZiAoXy5pc09iamVjdChyZXN1bHQpICYmIHJlc3VsdC5hc3luYykge1xuICAgICAgICAgIHJldHVybjtcbiAgICAgICAgfVxuICAgICAgfVxuICAgICAgdGhpcy5kb1VwZGF0ZShyb3csIGNvbHVtbiwgbmV3VmFsdWVXaXRoVHlwZSk7XG4gICAgfVxuXG4gICAgZG9VcGRhdGUocm93LCBjb2x1bW4sIG5ld1ZhbHVlKSB7XG4gICAgICBjb25zdCB7IGtleUZpZWxkLCBjZWxsRWRpdCwgZGF0YSB9ID0gdGhpcy5wcm9wcztcbiAgICAgIGNvbnN0IHsgYWZ0ZXJTYXZlQ2VsbCB9ID0gY2VsbEVkaXQub3B0aW9ucztcbiAgICAgIGNvbnN0IHJvd0lkID0gXy5nZXQocm93LCBrZXlGaWVsZCk7XG4gICAgICBjb25zdCBvbGRWYWx1ZSA9IF8uZ2V0KHJvdywgY29sdW1uLmRhdGFGaWVsZCk7XG4gICAgICBpZiAoaXNSZW1vdGVDZWxsRWRpdCgpKSB7XG4gICAgICAgIGhhbmRsZUNlbGxDaGFuZ2Uocm93SWQsIGNvbHVtbi5kYXRhRmllbGQsIG5ld1ZhbHVlKTtcbiAgICAgIH0gZWxzZSB7XG4gICAgICAgIGRhdGFPcGVyYXRvci5lZGl0Q2VsbChkYXRhLCBrZXlGaWVsZCwgcm93SWQsIGNvbHVtbi5kYXRhRmllbGQsIG5ld1ZhbHVlKTtcbiAgICAgICAgaWYgKF8uaXNGdW5jdGlvbihhZnRlclNhdmVDZWxsKSkgYWZ0ZXJTYXZlQ2VsbChvbGRWYWx1ZSwgbmV3VmFsdWUsIHJvdywgY29sdW1uKTtcbiAgICAgICAgdGhpcy5jb21wbGV0ZUVkaXRpbmcoKTtcbiAgICAgIH1cbiAgICB9XG5cbiAgICBjb21wbGV0ZUVkaXRpbmcoKSB7XG4gICAgICB0aGlzLnNldFN0YXRlKCgpID0+ICh7XG4gICAgICAgIHJpZHg6IG51bGwsXG4gICAgICAgIGNpZHg6IG51bGwsXG4gICAgICAgIG1lc3NhZ2U6IG51bGxcbiAgICAgIH0pKTtcbiAgICB9XG5cbiAgICBzdGFydEVkaXRpbmcocmlkeCwgY2lkeCkge1xuICAgICAgY29uc3QgZWRpdGluZyA9ICgpID0+IHtcbiAgICAgICAgdGhpcy5zZXRTdGF0ZSgoKSA9PiAoe1xuICAgICAgICAgIHJpZHgsXG4gICAgICAgICAgY2lkeFxuICAgICAgICB9KSk7XG4gICAgICB9O1xuXG4gICAgICBjb25zdCB7IHNlbGVjdFJvdyB9ID0gdGhpcy5wcm9wcztcbiAgICAgIGlmICghc2VsZWN0Um93IHx8IChzZWxlY3RSb3cuY2xpY2tUb0VkaXQgfHwgIXNlbGVjdFJvdy5jbGlja1RvU2VsZWN0KSkgZWRpdGluZygpO1xuICAgIH1cblxuICAgIGVzY2FwZUVkaXRpbmcoKSB7XG4gICAgICB0aGlzLnNldFN0YXRlKCgpID0+ICh7XG4gICAgICAgIHJpZHg6IG51bGwsXG4gICAgICAgIGNpZHg6IG51bGxcbiAgICAgIH0pKTtcbiAgICB9XG5cbiAgICByZW5kZXIoKSB7XG4gICAgICBjb25zdCB7XG4gICAgICAgIGNlbGxFZGl0OiB7XG4gICAgICAgICAgb3B0aW9uczogeyBub25FZGl0YWJsZVJvd3MsIGVycm9yTWVzc2FnZSwgLi4ub3B0aW9uc1Jlc3QgfSxcbiAgICAgICAgICAuLi5jZWxsRWRpdFJlc3RcbiAgICAgICAgfVxuICAgICAgfSA9IHRoaXMucHJvcHM7XG5cbiAgICAgIGNvbnN0IG5ld0NlbGxFZGl0ID0ge1xuICAgICAgICAuLi5vcHRpb25zUmVzdCxcbiAgICAgICAgLi4uY2VsbEVkaXRSZXN0LFxuICAgICAgICAuLi50aGlzLnN0YXRlLFxuICAgICAgICBub25FZGl0YWJsZVJvd3M6IF8uaXNEZWZpbmVkKG5vbkVkaXRhYmxlUm93cykgPyBub25FZGl0YWJsZVJvd3MoKSA6IFtdLFxuICAgICAgICBvblN0YXJ0OiB0aGlzLnN0YXJ0RWRpdGluZyxcbiAgICAgICAgb25Fc2NhcGU6IHRoaXMuZXNjYXBlRWRpdGluZyxcbiAgICAgICAgb25VcGRhdGU6IHRoaXMuaGFuZGxlQ2VsbFVwZGF0ZVxuICAgICAgfTtcblxuICAgICAgcmV0dXJuIChcbiAgICAgICAgPENlbGxFZGl0Q29udGV4dC5Qcm92aWRlclxuICAgICAgICAgIHZhbHVlPXsgeyAuLi5uZXdDZWxsRWRpdCB9IH1cbiAgICAgICAgPlxuICAgICAgICAgIHsgdGhpcy5wcm9wcy5jaGlsZHJlbiB9XG4gICAgICAgIDwvQ2VsbEVkaXRDb250ZXh0LlByb3ZpZGVyPlxuICAgICAgKTtcbiAgICB9XG4gIH1cbiAgcmV0dXJuIHtcbiAgICBQcm92aWRlcjogQ2VsbEVkaXRQcm92aWRlclxuICB9O1xufTtcblxuZXhwb3J0IGNvbnN0IENvbnN1bWVyID0gQ2VsbEVkaXRDb250ZXh0LkNvbnN1bWVyO1xuXG5cblxuLy8gV0VCUEFDSyBGT09URVIgLy9cbi8vIC4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1lZGl0b3Ivc3JjL2NvbnRleHQuanMiLCJpbXBvcnQgY3JlYXRlQ29udGV4dCBmcm9tICcuL3NyYy9jb250ZXh0JztcbmltcG9ydCB3aXRoUm93TGV2ZWxDZWxsRWRpdCBmcm9tICcuL3NyYy9yb3ctY29uc3VtZXInO1xuaW1wb3J0IGNyZWF0ZUVkaXRpbmdDZWxsIGZyb20gJy4vc3JjL2VkaXRpbmctY2VsbC1jb25zdW1lcic7XG5pbXBvcnQge1xuICBFRElUVFlQRSxcbiAgREJDTElDS19UT19DRUxMX0VESVQsXG4gIERFTEFZX0ZPUl9EQkNMSUNLXG59IGZyb20gJy4vc3JjL2NvbnN0JztcblxuZXhwb3J0IGRlZmF1bHQgKG9wdGlvbnMgPSB7fSkgPT4gKHtcbiAgY3JlYXRlQ29udGV4dCxcbiAgY3JlYXRlRWRpdGluZ0NlbGwsXG4gIHdpdGhSb3dMZXZlbENlbGxFZGl0LFxuICBEQkNMSUNLX1RPX0NFTExfRURJVCxcbiAgREVMQVlfRk9SX0RCQ0xJQ0ssXG4gIG9wdGlvbnNcbn0pO1xuXG5leHBvcnQgY29uc3QgVHlwZSA9IEVESVRUWVBFO1xuXG5cblxuLy8gV0VCUEFDSyBGT09URVIgLy9cbi8vIC4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1lZGl0b3IvaW5kZXguanMiLCIvKipcbiAqIENvcHlyaWdodCAyMDEzLXByZXNlbnQsIEZhY2Vib29rLCBJbmMuXG4gKiBBbGwgcmlnaHRzIHJlc2VydmVkLlxuICpcbiAqIFRoaXMgc291cmNlIGNvZGUgaXMgbGljZW5zZWQgdW5kZXIgdGhlIEJTRC1zdHlsZSBsaWNlbnNlIGZvdW5kIGluIHRoZVxuICogTElDRU5TRSBmaWxlIGluIHRoZSByb290IGRpcmVjdG9yeSBvZiB0aGlzIHNvdXJjZSB0cmVlLiBBbiBhZGRpdGlvbmFsIGdyYW50XG4gKiBvZiBwYXRlbnQgcmlnaHRzIGNhbiBiZSBmb3VuZCBpbiB0aGUgUEFURU5UUyBmaWxlIGluIHRoZSBzYW1lIGRpcmVjdG9yeS5cbiAqL1xuXG4ndXNlIHN0cmljdCc7XG5cbnZhciBlbXB0eUZ1bmN0aW9uID0gcmVxdWlyZSgnZmJqcy9saWIvZW1wdHlGdW5jdGlvbicpO1xudmFyIGludmFyaWFudCA9IHJlcXVpcmUoJ2ZianMvbGliL2ludmFyaWFudCcpO1xudmFyIFJlYWN0UHJvcFR5cGVzU2VjcmV0ID0gcmVxdWlyZSgnLi9saWIvUmVhY3RQcm9wVHlwZXNTZWNyZXQnKTtcblxubW9kdWxlLmV4cG9ydHMgPSBmdW5jdGlvbigpIHtcbiAgZnVuY3Rpb24gc2hpbShwcm9wcywgcHJvcE5hbWUsIGNvbXBvbmVudE5hbWUsIGxvY2F0aW9uLCBwcm9wRnVsbE5hbWUsIHNlY3JldCkge1xuICAgIGlmIChzZWNyZXQgPT09IFJlYWN0UHJvcFR5cGVzU2VjcmV0KSB7XG4gICAgICAvLyBJdCBpcyBzdGlsbCBzYWZlIHdoZW4gY2FsbGVkIGZyb20gUmVhY3QuXG4gICAgICByZXR1cm47XG4gICAgfVxuICAgIGludmFyaWFudChcbiAgICAgIGZhbHNlLFxuICAgICAgJ0NhbGxpbmcgUHJvcFR5cGVzIHZhbGlkYXRvcnMgZGlyZWN0bHkgaXMgbm90IHN1cHBvcnRlZCBieSB0aGUgYHByb3AtdHlwZXNgIHBhY2thZ2UuICcgK1xuICAgICAgJ1VzZSBQcm9wVHlwZXMuY2hlY2tQcm9wVHlwZXMoKSB0byBjYWxsIHRoZW0uICcgK1xuICAgICAgJ1JlYWQgbW9yZSBhdCBodHRwOi8vZmIubWUvdXNlLWNoZWNrLXByb3AtdHlwZXMnXG4gICAgKTtcbiAgfTtcbiAgc2hpbS5pc1JlcXVpcmVkID0gc2hpbTtcbiAgZnVuY3Rpb24gZ2V0U2hpbSgpIHtcbiAgICByZXR1cm4gc2hpbTtcbiAgfTtcbiAgLy8gSW1wb3J0YW50IVxuICAvLyBLZWVwIHRoaXMgbGlzdCBpbiBzeW5jIHdpdGggcHJvZHVjdGlvbiB2ZXJzaW9uIGluIGAuL2ZhY3RvcnlXaXRoVHlwZUNoZWNrZXJzLmpzYC5cbiAgdmFyIFJlYWN0UHJvcFR5cGVzID0ge1xuICAgIGFycmF5OiBzaGltLFxuICAgIGJvb2w6IHNoaW0sXG4gICAgZnVuYzogc2hpbSxcbiAgICBudW1iZXI6IHNoaW0sXG4gICAgb2JqZWN0OiBzaGltLFxuICAgIHN0cmluZzogc2hpbSxcbiAgICBzeW1ib2w6IHNoaW0sXG5cbiAgICBhbnk6IHNoaW0sXG4gICAgYXJyYXlPZjogZ2V0U2hpbSxcbiAgICBlbGVtZW50OiBzaGltLFxuICAgIGluc3RhbmNlT2Y6IGdldFNoaW0sXG4gICAgbm9kZTogc2hpbSxcbiAgICBvYmplY3RPZjogZ2V0U2hpbSxcbiAgICBvbmVPZjogZ2V0U2hpbSxcbiAgICBvbmVPZlR5cGU6IGdldFNoaW0sXG4gICAgc2hhcGU6IGdldFNoaW1cbiAgfTtcblxuICBSZWFjdFByb3BUeXBlcy5jaGVja1Byb3BUeXBlcyA9IGVtcHR5RnVuY3Rpb247XG4gIFJlYWN0UHJvcFR5cGVzLlByb3BUeXBlcyA9IFJlYWN0UHJvcFR5cGVzO1xuXG4gIHJldHVybiBSZWFjdFByb3BUeXBlcztcbn07XG5cblxuXG4vLy8vLy8vLy8vLy8vLy8vLy9cbi8vIFdFQlBBQ0sgRk9PVEVSXG4vLyAuL25vZGVfbW9kdWxlcy9wcm9wLXR5cGVzL2ZhY3RvcnlXaXRoVGhyb3dpbmdTaGltcy5qc1xuLy8gbW9kdWxlIGlkID0gNlxuLy8gbW9kdWxlIGNodW5rcyA9IDAgMSIsIlwidXNlIHN0cmljdFwiO1xuXG4vKipcbiAqIENvcHlyaWdodCAoYykgMjAxMy1wcmVzZW50LCBGYWNlYm9vaywgSW5jLlxuICpcbiAqIFRoaXMgc291cmNlIGNvZGUgaXMgbGljZW5zZWQgdW5kZXIgdGhlIE1JVCBsaWNlbnNlIGZvdW5kIGluIHRoZVxuICogTElDRU5TRSBmaWxlIGluIHRoZSByb290IGRpcmVjdG9yeSBvZiB0aGlzIHNvdXJjZSB0cmVlLlxuICpcbiAqIFxuICovXG5cbmZ1bmN0aW9uIG1ha2VFbXB0eUZ1bmN0aW9uKGFyZykge1xuICByZXR1cm4gZnVuY3Rpb24gKCkge1xuICAgIHJldHVybiBhcmc7XG4gIH07XG59XG5cbi8qKlxuICogVGhpcyBmdW5jdGlvbiBhY2NlcHRzIGFuZCBkaXNjYXJkcyBpbnB1dHM7IGl0IGhhcyBubyBzaWRlIGVmZmVjdHMuIFRoaXMgaXNcbiAqIHByaW1hcmlseSB1c2VmdWwgaWRpb21hdGljYWxseSBmb3Igb3ZlcnJpZGFibGUgZnVuY3Rpb24gZW5kcG9pbnRzIHdoaWNoXG4gKiBhbHdheXMgbmVlZCB0byBiZSBjYWxsYWJsZSwgc2luY2UgSlMgbGFja3MgYSBudWxsLWNhbGwgaWRpb20gYWxhIENvY29hLlxuICovXG52YXIgZW1wdHlGdW5jdGlvbiA9IGZ1bmN0aW9uIGVtcHR5RnVuY3Rpb24oKSB7fTtcblxuZW1wdHlGdW5jdGlvbi50aGF0UmV0dXJucyA9IG1ha2VFbXB0eUZ1bmN0aW9uO1xuZW1wdHlGdW5jdGlvbi50aGF0UmV0dXJuc0ZhbHNlID0gbWFrZUVtcHR5RnVuY3Rpb24oZmFsc2UpO1xuZW1wdHlGdW5jdGlvbi50aGF0UmV0dXJuc1RydWUgPSBtYWtlRW1wdHlGdW5jdGlvbih0cnVlKTtcbmVtcHR5RnVuY3Rpb24udGhhdFJldHVybnNOdWxsID0gbWFrZUVtcHR5RnVuY3Rpb24obnVsbCk7XG5lbXB0eUZ1bmN0aW9uLnRoYXRSZXR1cm5zVGhpcyA9IGZ1bmN0aW9uICgpIHtcbiAgcmV0dXJuIHRoaXM7XG59O1xuZW1wdHlGdW5jdGlvbi50aGF0UmV0dXJuc0FyZ3VtZW50ID0gZnVuY3Rpb24gKGFyZykge1xuICByZXR1cm4gYXJnO1xufTtcblxubW9kdWxlLmV4cG9ydHMgPSBlbXB0eUZ1bmN0aW9uO1xuXG5cbi8vLy8vLy8vLy8vLy8vLy8vL1xuLy8gV0VCUEFDSyBGT09URVJcbi8vIC4vbm9kZV9tb2R1bGVzL2ZianMvbGliL2VtcHR5RnVuY3Rpb24uanNcbi8vIG1vZHVsZSBpZCA9IDdcbi8vIG1vZHVsZSBjaHVua3MgPSAwIDEiLCIvKipcbiAqIENvcHlyaWdodCAoYykgMjAxMy1wcmVzZW50LCBGYWNlYm9vaywgSW5jLlxuICpcbiAqIFRoaXMgc291cmNlIGNvZGUgaXMgbGljZW5zZWQgdW5kZXIgdGhlIE1JVCBsaWNlbnNlIGZvdW5kIGluIHRoZVxuICogTElDRU5TRSBmaWxlIGluIHRoZSByb290IGRpcmVjdG9yeSBvZiB0aGlzIHNvdXJjZSB0cmVlLlxuICpcbiAqL1xuXG4ndXNlIHN0cmljdCc7XG5cbi8qKlxuICogVXNlIGludmFyaWFudCgpIHRvIGFzc2VydCBzdGF0ZSB3aGljaCB5b3VyIHByb2dyYW0gYXNzdW1lcyB0byBiZSB0cnVlLlxuICpcbiAqIFByb3ZpZGUgc3ByaW50Zi1zdHlsZSBmb3JtYXQgKG9ubHkgJXMgaXMgc3VwcG9ydGVkKSBhbmQgYXJndW1lbnRzXG4gKiB0byBwcm92aWRlIGluZm9ybWF0aW9uIGFib3V0IHdoYXQgYnJva2UgYW5kIHdoYXQgeW91IHdlcmVcbiAqIGV4cGVjdGluZy5cbiAqXG4gKiBUaGUgaW52YXJpYW50IG1lc3NhZ2Ugd2lsbCBiZSBzdHJpcHBlZCBpbiBwcm9kdWN0aW9uLCBidXQgdGhlIGludmFyaWFudFxuICogd2lsbCByZW1haW4gdG8gZW5zdXJlIGxvZ2ljIGRvZXMgbm90IGRpZmZlciBpbiBwcm9kdWN0aW9uLlxuICovXG5cbnZhciB2YWxpZGF0ZUZvcm1hdCA9IGZ1bmN0aW9uIHZhbGlkYXRlRm9ybWF0KGZvcm1hdCkge307XG5cbmlmIChwcm9jZXNzLmVudi5OT0RFX0VOViAhPT0gJ3Byb2R1Y3Rpb24nKSB7XG4gIHZhbGlkYXRlRm9ybWF0ID0gZnVuY3Rpb24gdmFsaWRhdGVGb3JtYXQoZm9ybWF0KSB7XG4gICAgaWYgKGZvcm1hdCA9PT0gdW5kZWZpbmVkKSB7XG4gICAgICB0aHJvdyBuZXcgRXJyb3IoJ2ludmFyaWFudCByZXF1aXJlcyBhbiBlcnJvciBtZXNzYWdlIGFyZ3VtZW50Jyk7XG4gICAgfVxuICB9O1xufVxuXG5mdW5jdGlvbiBpbnZhcmlhbnQoY29uZGl0aW9uLCBmb3JtYXQsIGEsIGIsIGMsIGQsIGUsIGYpIHtcbiAgdmFsaWRhdGVGb3JtYXQoZm9ybWF0KTtcblxuICBpZiAoIWNvbmRpdGlvbikge1xuICAgIHZhciBlcnJvcjtcbiAgICBpZiAoZm9ybWF0ID09PSB1bmRlZmluZWQpIHtcbiAgICAgIGVycm9yID0gbmV3IEVycm9yKCdNaW5pZmllZCBleGNlcHRpb24gb2NjdXJyZWQ7IHVzZSB0aGUgbm9uLW1pbmlmaWVkIGRldiBlbnZpcm9ubWVudCAnICsgJ2ZvciB0aGUgZnVsbCBlcnJvciBtZXNzYWdlIGFuZCBhZGRpdGlvbmFsIGhlbHBmdWwgd2FybmluZ3MuJyk7XG4gICAgfSBlbHNlIHtcbiAgICAgIHZhciBhcmdzID0gW2EsIGIsIGMsIGQsIGUsIGZdO1xuICAgICAgdmFyIGFyZ0luZGV4ID0gMDtcbiAgICAgIGVycm9yID0gbmV3IEVycm9yKGZvcm1hdC5yZXBsYWNlKC8lcy9nLCBmdW5jdGlvbiAoKSB7XG4gICAgICAgIHJldHVybiBhcmdzW2FyZ0luZGV4KytdO1xuICAgICAgfSkpO1xuICAgICAgZXJyb3IubmFtZSA9ICdJbnZhcmlhbnQgVmlvbGF0aW9uJztcbiAgICB9XG5cbiAgICBlcnJvci5mcmFtZXNUb1BvcCA9IDE7IC8vIHdlIGRvbid0IGNhcmUgYWJvdXQgaW52YXJpYW50J3Mgb3duIGZyYW1lXG4gICAgdGhyb3cgZXJyb3I7XG4gIH1cbn1cblxubW9kdWxlLmV4cG9ydHMgPSBpbnZhcmlhbnQ7XG5cblxuLy8vLy8vLy8vLy8vLy8vLy8vXG4vLyBXRUJQQUNLIEZPT1RFUlxuLy8gLi9ub2RlX21vZHVsZXMvZmJqcy9saWIvaW52YXJpYW50LmpzXG4vLyBtb2R1bGUgaWQgPSA4XG4vLyBtb2R1bGUgY2h1bmtzID0gMCAxIiwiLyoqXG4gKiBDb3B5cmlnaHQgMjAxMy1wcmVzZW50LCBGYWNlYm9vaywgSW5jLlxuICogQWxsIHJpZ2h0cyByZXNlcnZlZC5cbiAqXG4gKiBUaGlzIHNvdXJjZSBjb2RlIGlzIGxpY2Vuc2VkIHVuZGVyIHRoZSBCU0Qtc3R5bGUgbGljZW5zZSBmb3VuZCBpbiB0aGVcbiAqIExJQ0VOU0UgZmlsZSBpbiB0aGUgcm9vdCBkaXJlY3Rvcnkgb2YgdGhpcyBzb3VyY2UgdHJlZS4gQW4gYWRkaXRpb25hbCBncmFudFxuICogb2YgcGF0ZW50IHJpZ2h0cyBjYW4gYmUgZm91bmQgaW4gdGhlIFBBVEVOVFMgZmlsZSBpbiB0aGUgc2FtZSBkaXJlY3RvcnkuXG4gKi9cblxuJ3VzZSBzdHJpY3QnO1xuXG52YXIgUmVhY3RQcm9wVHlwZXNTZWNyZXQgPSAnU0VDUkVUX0RPX05PVF9QQVNTX1RISVNfT1JfWU9VX1dJTExfQkVfRklSRUQnO1xuXG5tb2R1bGUuZXhwb3J0cyA9IFJlYWN0UHJvcFR5cGVzU2VjcmV0O1xuXG5cblxuLy8vLy8vLy8vLy8vLy8vLy8vXG4vLyBXRUJQQUNLIEZPT1RFUlxuLy8gLi9ub2RlX21vZHVsZXMvcHJvcC10eXBlcy9saWIvUmVhY3RQcm9wVHlwZXNTZWNyZXQuanNcbi8vIG1vZHVsZSBpZCA9IDlcbi8vIG1vZHVsZSBjaHVua3MgPSAwIDEiLCIvKiBlc2xpbnQgcmVhY3QvcHJvcC10eXBlczogMCAqL1xuaW1wb3J0IFJlYWN0IGZyb20gJ3JlYWN0JztcbmltcG9ydCB7IERFTEFZX0ZPUl9EQkNMSUNLLCBEQkNMSUNLX1RPX0NFTExfRURJVCwgQ0xJQ0tfVE9fQ0VMTF9FRElUIH0gZnJvbSAnLi9jb25zdCc7XG5pbXBvcnQgeyBDb25zdW1lciB9IGZyb20gJy4vY29udGV4dCc7XG5cbmV4cG9ydCBkZWZhdWx0IChDb21wb25lbnQsIHNlbGVjdFJvd0VuYWJsZWQpID0+IHtcbiAgY29uc3QgcmVuZGVyV2l0aENlbGxFZGl0ID0gKHByb3BzLCBjZWxsRWRpdCkgPT4ge1xuICAgIGNvbnN0IGtleSA9IHByb3BzLnZhbHVlO1xuICAgIGNvbnN0IGVkaXRhYmxlUm93ID0gIShcbiAgICAgIGNlbGxFZGl0Lm5vbkVkaXRhYmxlUm93cy5sZW5ndGggPiAwICYmXG4gICAgICBjZWxsRWRpdC5ub25FZGl0YWJsZVJvd3MuaW5kZXhPZihrZXkpID4gLTFcbiAgICApO1xuXG4gICAgY29uc3QgYXR0cnMgPSB7fTtcblxuICAgIGlmIChzZWxlY3RSb3dFbmFibGVkICYmIGNlbGxFZGl0Lm1vZGUgPT09IERCQ0xJQ0tfVE9fQ0VMTF9FRElUKSB7XG4gICAgICBhdHRycy5ERUxBWV9GT1JfREJDTElDSyA9IERFTEFZX0ZPUl9EQkNMSUNLO1xuICAgIH1cblxuICAgIHJldHVybiAoXG4gICAgICA8Q29tcG9uZW50XG4gICAgICAgIHsgLi4ucHJvcHMgfVxuICAgICAgICB7IC4uLmF0dHJzIH1cbiAgICAgICAgZWRpdGluZ1Jvd0lkeD17IGNlbGxFZGl0LnJpZHggfVxuICAgICAgICBlZGl0aW5nQ29sSWR4PXsgY2VsbEVkaXQuY2lkeCB9XG4gICAgICAgIGVkaXRhYmxlPXsgZWRpdGFibGVSb3cgfVxuICAgICAgICBvblN0YXJ0PXsgY2VsbEVkaXQub25TdGFydCB9XG4gICAgICAgIGNsaWNrVG9FZGl0PXsgY2VsbEVkaXQubW9kZSA9PT0gQ0xJQ0tfVE9fQ0VMTF9FRElUIH1cbiAgICAgICAgZGJjbGlja1RvRWRpdD17IGNlbGxFZGl0Lm1vZGUgPT09IERCQ0xJQ0tfVE9fQ0VMTF9FRElUIH1cbiAgICAgIC8+XG4gICAgKTtcbiAgfTtcbiAgZnVuY3Rpb24gd2l0aENvbnN1bWVyKHByb3BzKSB7XG4gICAgcmV0dXJuIChcbiAgICAgIDxDb25zdW1lcj5cbiAgICAgICAgeyBjZWxsRWRpdCA9PiByZW5kZXJXaXRoQ2VsbEVkaXQocHJvcHMsIGNlbGxFZGl0KSB9XG4gICAgICA8L0NvbnN1bWVyPlxuICAgICk7XG4gIH1cblxuICB3aXRoQ29uc3VtZXIuZGlzcGxheU5hbWUgPSAnV2l0aENlbGxFZGl0aW5nUm93Q29uc3VtZXInO1xuICByZXR1cm4gd2l0aENvbnN1bWVyO1xufTtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyAuL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZWRpdG9yL3NyYy9yb3ctY29uc3VtZXIuanMiLCIvKiBlc2xpbnQgcmVhY3QvcHJvcC10eXBlczogMCAqL1xuaW1wb3J0IFJlYWN0IGZyb20gJ3JlYWN0JztcbmltcG9ydCB7IENvbnN1bWVyIH0gZnJvbSAnLi9jb250ZXh0JztcbmltcG9ydCBjcmVhdGVFZGl0aW5nQ2VsbCBmcm9tICcuL2VkaXRpbmctY2VsbCc7XG5cbmV4cG9ydCBkZWZhdWx0IChfLCBvblN0YXJ0RWRpdCkgPT4ge1xuICBjb25zdCBFZGl0aW5nQ2VsbCA9IGNyZWF0ZUVkaXRpbmdDZWxsKF8sIG9uU3RhcnRFZGl0KTtcbiAgY29uc3QgcmVuZGVyV2l0aEVkaXRpbmdDZWxsID0gKHByb3BzLCBjZWxsRWRpdCkgPT4ge1xuICAgIGNvbnN0IGNvbnRlbnQgPSBfLmdldChwcm9wcy5yb3csIHByb3BzLmNvbHVtbi5kYXRhRmllbGQpO1xuICAgIGxldCBlZGl0Q2VsbHN0eWxlID0gcHJvcHMuY29sdW1uLmVkaXRDZWxsU3R5bGUgfHwge307XG4gICAgbGV0IGVkaXRDZWxsY2xhc3NlcyA9IHByb3BzLmNvbHVtbi5lZGl0Q2VsbENsYXNzZXM7XG4gICAgaWYgKF8uaXNGdW5jdGlvbihwcm9wcy5jb2x1bW4uZWRpdENlbGxTdHlsZSkpIHtcbiAgICAgIGVkaXRDZWxsc3R5bGUgPSBwcm9wcy5jb2x1bW4uZWRpdENlbGxTdHlsZShcbiAgICAgICAgY29udGVudCxcbiAgICAgICAgcHJvcHMucm93LFxuICAgICAgICBwcm9wcy5yb3dJbmRleCxcbiAgICAgICAgcHJvcHMuY29sdW1uSW5kZXhcbiAgICAgICk7XG4gICAgfVxuICAgIGlmIChfLmlzRnVuY3Rpb24ocHJvcHMuY29sdW1uLmVkaXRDZWxsQ2xhc3NlcykpIHtcbiAgICAgIGVkaXRDZWxsY2xhc3NlcyA9IHByb3BzLmNvbHVtbi5lZGl0Q2VsbENsYXNzZXMoXG4gICAgICAgIGNvbnRlbnQsXG4gICAgICAgIHByb3BzLnJvdyxcbiAgICAgICAgcHJvcHMucm93SW5kZXgsXG4gICAgICAgIHByb3BzLmNvbHVtbkluZGV4KVxuICAgICAgO1xuICAgIH1cblxuICAgIHJldHVybiAoXG4gICAgICA8RWRpdGluZ0NlbGxcbiAgICAgICAgeyAuLi5wcm9wcyB9XG4gICAgICAgIGNsYXNzTmFtZT17IGVkaXRDZWxsY2xhc3NlcyB9XG4gICAgICAgIHN0eWxlPXsgZWRpdENlbGxzdHlsZSB9XG4gICAgICAgIHsgLi4uY2VsbEVkaXQgfVxuICAgICAgLz5cbiAgICApO1xuICB9O1xuXG4gIHJldHVybiBwcm9wcyA9PiAoXG4gICAgPENvbnN1bWVyPlxuICAgICAgeyBjZWxsRWRpdCA9PiByZW5kZXJXaXRoRWRpdGluZ0NlbGwocHJvcHMsIGNlbGxFZGl0KSB9XG4gICAgPC9Db25zdW1lcj5cbiAgKTtcbn07XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLWVkaXRvci9zcmMvZWRpdGluZy1jZWxsLWNvbnN1bWVyLmpzIiwiLyogZXNsaW50IHJlYWN0L3Byb3AtdHlwZXM6IDAgKi9cbi8qIGVzbGludCBuby1yZXR1cm4tYXNzaWduOiAwICovXG4vKiBlc2xpbnQgY2xhc3MtbWV0aG9kcy11c2UtdGhpczogMCAqL1xuLyogZXNsaW50IGpzeC1hMTF5L25vLW5vbmludGVyYWN0aXZlLWVsZW1lbnQtaW50ZXJhY3Rpb25zOiAwICovXG4vKiBlc2xpbnQgY2FtZWxjYXNlOiAwICovXG5pbXBvcnQgUmVhY3QsIHsgQ29tcG9uZW50IH0gZnJvbSAncmVhY3QnO1xuaW1wb3J0IGNzIGZyb20gJ2NsYXNzbmFtZXMnO1xuaW1wb3J0IFByb3BUeXBlcyBmcm9tICdwcm9wLXR5cGVzJztcblxuaW1wb3J0IERyb3Bkb3duRWRpdG9yIGZyb20gJy4vZHJvcGRvd24tZWRpdG9yJztcbmltcG9ydCBUZXh0QXJlYUVkaXRvciBmcm9tICcuL3RleHRhcmVhLWVkaXRvcic7XG5pbXBvcnQgQ2hlY2tCb3hFZGl0b3IgZnJvbSAnLi9jaGVja2JveC1lZGl0b3InO1xuaW1wb3J0IERhdGVFZGl0b3IgZnJvbSAnLi9kYXRlLWVkaXRvcic7XG5pbXBvcnQgVGV4dEVkaXRvciBmcm9tICcuL3RleHQtZWRpdG9yJztcbmltcG9ydCBFZGl0b3JJbmRpY2F0b3IgZnJvbSAnLi9lZGl0b3ItaW5kaWNhdG9yJztcbmltcG9ydCB7IFRJTUVfVE9fQ0xPU0VfTUVTU0FHRSwgRURJVFRZUEUgfSBmcm9tICcuL2NvbnN0JztcblxuZXhwb3J0IGRlZmF1bHQgKF8sIG9uU3RhcnRFZGl0KSA9PlxuICBjbGFzcyBFZGl0aW5nQ2VsbCBleHRlbmRzIENvbXBvbmVudCB7XG4gICAgc3RhdGljIHByb3BUeXBlcyA9IHtcbiAgICAgIHJvdzogUHJvcFR5cGVzLm9iamVjdC5pc1JlcXVpcmVkLFxuICAgICAgcm93SW5kZXg6IFByb3BUeXBlcy5udW1iZXIuaXNSZXF1aXJlZCxcbiAgICAgIGNvbHVtbjogUHJvcFR5cGVzLm9iamVjdC5pc1JlcXVpcmVkLFxuICAgICAgY29sdW1uSW5kZXg6IFByb3BUeXBlcy5udW1iZXIuaXNSZXF1aXJlZCxcbiAgICAgIG9uVXBkYXRlOiBQcm9wVHlwZXMuZnVuYy5pc1JlcXVpcmVkLFxuICAgICAgb25Fc2NhcGU6IFByb3BUeXBlcy5mdW5jLmlzUmVxdWlyZWQsXG4gICAgICB0aW1lVG9DbG9zZU1lc3NhZ2U6IFByb3BUeXBlcy5udW1iZXIsXG4gICAgICBhdXRvU2VsZWN0VGV4dDogUHJvcFR5cGVzLmJvb2wsXG4gICAgICBjbGFzc05hbWU6IFByb3BUeXBlcy5zdHJpbmcsXG4gICAgICBzdHlsZTogUHJvcFR5cGVzLm9iamVjdFxuICAgIH1cblxuICAgIHN0YXRpYyBkZWZhdWx0UHJvcHMgPSB7XG4gICAgICB0aW1lVG9DbG9zZU1lc3NhZ2U6IFRJTUVfVE9fQ0xPU0VfTUVTU0FHRSxcbiAgICAgIGNsYXNzTmFtZTogbnVsbCxcbiAgICAgIGF1dG9TZWxlY3RUZXh0OiBmYWxzZSxcbiAgICAgIHN0eWxlOiB7fVxuICAgIH1cblxuICAgIGNvbnN0cnVjdG9yKHByb3BzKSB7XG4gICAgICBzdXBlcihwcm9wcyk7XG4gICAgICB0aGlzLmluZGljYXRvclRpbWVyID0gbnVsbDtcbiAgICAgIHRoaXMuY2xlYXJUaW1lciA9IHRoaXMuY2xlYXJUaW1lci5iaW5kKHRoaXMpO1xuICAgICAgdGhpcy5oYW5kbGVCbHVyID0gdGhpcy5oYW5kbGVCbHVyLmJpbmQodGhpcyk7XG4gICAgICB0aGlzLmhhbmRsZUNsaWNrID0gdGhpcy5oYW5kbGVDbGljay5iaW5kKHRoaXMpO1xuICAgICAgdGhpcy5oYW5kbGVLZXlEb3duID0gdGhpcy5oYW5kbGVLZXlEb3duLmJpbmQodGhpcyk7XG4gICAgICB0aGlzLmJlZm9yZUNvbXBsZXRlID0gdGhpcy5iZWZvcmVDb21wbGV0ZS5iaW5kKHRoaXMpO1xuICAgICAgdGhpcy5hc3luY2JlZm9yZUNvbXBldGUgPSB0aGlzLmFzeW5jYmVmb3JlQ29tcGV0ZS5iaW5kKHRoaXMpO1xuICAgICAgdGhpcy5kaXNwbGF5RXJyb3JNZXNzYWdlID0gdGhpcy5kaXNwbGF5RXJyb3JNZXNzYWdlLmJpbmQodGhpcyk7XG4gICAgICB0aGlzLnN0YXRlID0ge1xuICAgICAgICBpbnZhbGlkTWVzc2FnZTogbnVsbFxuICAgICAgfTtcbiAgICB9XG5cbiAgICBjb21wb25lbnRXaWxsVW5tb3VudCgpIHtcbiAgICAgIHRoaXMuY2xlYXJUaW1lcigpO1xuICAgIH1cblxuICAgIFVOU0FGRV9jb21wb25lbnRXaWxsUmVjZWl2ZVByb3BzKHsgbWVzc2FnZSB9KSB7XG4gICAgICBpZiAoXy5pc0RlZmluZWQobWVzc2FnZSkpIHtcbiAgICAgICAgdGhpcy5jcmVhdGVUaW1lcigpO1xuICAgICAgICB0aGlzLnNldFN0YXRlKCgpID0+ICh7XG4gICAgICAgICAgaW52YWxpZE1lc3NhZ2U6IG1lc3NhZ2VcbiAgICAgICAgfSkpO1xuICAgICAgfVxuICAgIH1cblxuICAgIGNsZWFyVGltZXIoKSB7XG4gICAgICBpZiAodGhpcy5pbmRpY2F0b3JUaW1lcikge1xuICAgICAgICBjbGVhclRpbWVvdXQodGhpcy5pbmRpY2F0b3JUaW1lcik7XG4gICAgICB9XG4gICAgfVxuXG4gICAgY3JlYXRlVGltZXIoKSB7XG4gICAgICB0aGlzLmNsZWFyVGltZXIoKTtcbiAgICAgIGNvbnN0IHsgdGltZVRvQ2xvc2VNZXNzYWdlLCBvbkVycm9yTWVzc2FnZURpc2FwcGVhciB9ID0gdGhpcy5wcm9wcztcbiAgICAgIHRoaXMuaW5kaWNhdG9yVGltZXIgPSBfLnNsZWVwKCgpID0+IHtcbiAgICAgICAgdGhpcy5zZXRTdGF0ZSgoKSA9PiAoe1xuICAgICAgICAgIGludmFsaWRNZXNzYWdlOiBudWxsXG4gICAgICAgIH0pKTtcbiAgICAgICAgaWYgKF8uaXNGdW5jdGlvbihvbkVycm9yTWVzc2FnZURpc2FwcGVhcikpIG9uRXJyb3JNZXNzYWdlRGlzYXBwZWFyKCk7XG4gICAgICB9LCB0aW1lVG9DbG9zZU1lc3NhZ2UpO1xuICAgIH1cblxuICAgIGRpc3BsYXlFcnJvck1lc3NhZ2UobWVzc2FnZSkge1xuICAgICAgdGhpcy5zZXRTdGF0ZSgoKSA9PiAoe1xuICAgICAgICBpbnZhbGlkTWVzc2FnZTogbWVzc2FnZVxuICAgICAgfSkpO1xuICAgICAgdGhpcy5jcmVhdGVUaW1lcigpO1xuICAgIH1cblxuICAgIGFzeW5jYmVmb3JlQ29tcGV0ZShuZXdWYWx1ZSkge1xuICAgICAgcmV0dXJuIChyZXN1bHQgPSB7IHZhbGlkOiB0cnVlIH0pID0+IHtcbiAgICAgICAgY29uc3QgeyB2YWxpZCwgbWVzc2FnZSB9ID0gcmVzdWx0O1xuICAgICAgICBjb25zdCB7IG9uVXBkYXRlLCByb3csIGNvbHVtbiB9ID0gdGhpcy5wcm9wcztcbiAgICAgICAgaWYgKCF2YWxpZCkge1xuICAgICAgICAgIHRoaXMuZGlzcGxheUVycm9yTWVzc2FnZShtZXNzYWdlKTtcbiAgICAgICAgICByZXR1cm47XG4gICAgICAgIH1cbiAgICAgICAgb25VcGRhdGUocm93LCBjb2x1bW4sIG5ld1ZhbHVlKTtcbiAgICAgIH07XG4gICAgfVxuXG4gICAgYmVmb3JlQ29tcGxldGUobmV3VmFsdWUpIHtcbiAgICAgIGNvbnN0IHsgb25VcGRhdGUsIHJvdywgY29sdW1uIH0gPSB0aGlzLnByb3BzO1xuICAgICAgaWYgKF8uaXNGdW5jdGlvbihjb2x1bW4udmFsaWRhdG9yKSkge1xuICAgICAgICBjb25zdCB2YWxpZGF0ZUZvcm0gPSBjb2x1bW4udmFsaWRhdG9yKFxuICAgICAgICAgIG5ld1ZhbHVlLFxuICAgICAgICAgIHJvdyxcbiAgICAgICAgICBjb2x1bW4sXG4gICAgICAgICAgdGhpcy5hc3luY2JlZm9yZUNvbXBldGUobmV3VmFsdWUpXG4gICAgICAgICk7XG4gICAgICAgIGlmIChfLmlzT2JqZWN0KHZhbGlkYXRlRm9ybSkpIHtcbiAgICAgICAgICBpZiAodmFsaWRhdGVGb3JtLmFzeW5jKSB7XG4gICAgICAgICAgICByZXR1cm47XG4gICAgICAgICAgfSBlbHNlIGlmICghdmFsaWRhdGVGb3JtLnZhbGlkKSB7XG4gICAgICAgICAgICB0aGlzLmRpc3BsYXlFcnJvck1lc3NhZ2UodmFsaWRhdGVGb3JtLm1lc3NhZ2UpO1xuICAgICAgICAgICAgcmV0dXJuO1xuICAgICAgICAgIH1cbiAgICAgICAgfVxuICAgICAgfVxuICAgICAgb25VcGRhdGUocm93LCBjb2x1bW4sIG5ld1ZhbHVlKTtcbiAgICB9XG5cbiAgICBoYW5kbGVCbHVyKCkge1xuICAgICAgY29uc3QgeyBvbkVzY2FwZSwgYmx1clRvU2F2ZSB9ID0gdGhpcy5wcm9wcztcbiAgICAgIGlmIChibHVyVG9TYXZlKSB7XG4gICAgICAgIHRoaXMuYmVmb3JlQ29tcGxldGUodGhpcy5lZGl0b3IuZ2V0VmFsdWUoKSk7XG4gICAgICB9IGVsc2Uge1xuICAgICAgICBvbkVzY2FwZSgpO1xuICAgICAgfVxuICAgIH1cblxuICAgIGhhbmRsZUtleURvd24oZSkge1xuICAgICAgY29uc3QgeyBvbkVzY2FwZSB9ID0gdGhpcy5wcm9wcztcbiAgICAgIGlmIChlLmtleUNvZGUgPT09IDI3KSB7IC8vIEVTQ1xuICAgICAgICBvbkVzY2FwZSgpO1xuICAgICAgfSBlbHNlIGlmIChlLmtleUNvZGUgPT09IDEzKSB7IC8vIEVOVEVSXG4gICAgICAgIHRoaXMuYmVmb3JlQ29tcGxldGUodGhpcy5lZGl0b3IuZ2V0VmFsdWUoKSk7XG4gICAgICB9XG4gICAgfVxuXG4gICAgaGFuZGxlQ2xpY2soZSkge1xuICAgICAgaWYgKGUudGFyZ2V0LnRhZ05hbWUgIT09ICdURCcpIHtcbiAgICAgICAgLy8gVG8gYXZvaWQgdGhlIHJvdyBzZWxlY3Rpb24gZXZlbnQgYmUgdHJpZ2dlcmVkLFxuICAgICAgICAvLyBXaGVuIHVzZXIgZGVmaW5lIHNlbGVjdFJvdy5jbGlja1RvU2VsZWN0IGFuZCBzZWxlY3RSb3cuY2xpY2tUb0VkaXRcbiAgICAgICAgLy8gV2Ugc2hvdWxkbid0IHRyaWdnZXIgc2VsZWN0aW9uIGV2ZW50IGV2ZW4gaWYgdXNlciBjbGljayBvbiB0aGUgY2VsbCBlZGl0b3IoaW5wdXQpXG4gICAgICAgIGUuc3RvcFByb3BhZ2F0aW9uKCk7XG4gICAgICB9XG4gICAgfVxuXG4gICAgcmVuZGVyKCkge1xuICAgICAgbGV0IGVkaXRvcjtcbiAgICAgIGNvbnN0IHsgcm93LCBjb2x1bW4sIGNsYXNzTmFtZSwgc3R5bGUsIHJvd0luZGV4LCBjb2x1bW5JbmRleCwgYXV0b1NlbGVjdFRleHQgfSA9IHRoaXMucHJvcHM7XG4gICAgICBjb25zdCB7IGRhdGFGaWVsZCB9ID0gY29sdW1uO1xuXG4gICAgICBjb25zdCB2YWx1ZSA9IF8uZ2V0KHJvdywgZGF0YUZpZWxkKTtcbiAgICAgIGNvbnN0IGhhc0Vycm9yID0gXy5pc0RlZmluZWQodGhpcy5zdGF0ZS5pbnZhbGlkTWVzc2FnZSk7XG5cbiAgICAgIGxldCBjdXN0b21FZGl0b3JDbGFzcyA9IGNvbHVtbi5lZGl0b3JDbGFzc2VzIHx8ICcnO1xuICAgICAgaWYgKF8uaXNGdW5jdGlvbihjb2x1bW4uZWRpdG9yQ2xhc3NlcykpIHtcbiAgICAgICAgY3VzdG9tRWRpdG9yQ2xhc3MgPSBjb2x1bW4uZWRpdG9yQ2xhc3Nlcyh2YWx1ZSwgcm93LCByb3dJbmRleCwgY29sdW1uSW5kZXgpO1xuICAgICAgfVxuXG4gICAgICBsZXQgZWRpdG9yU3R5bGUgPSBjb2x1bW4uZWRpdG9yU3R5bGUgfHwge307XG4gICAgICBpZiAoXy5pc0Z1bmN0aW9uKGNvbHVtbi5lZGl0b3JTdHlsZSkpIHtcbiAgICAgICAgZWRpdG9yU3R5bGUgPSBjb2x1bW4uZWRpdG9yU3R5bGUodmFsdWUsIHJvdywgcm93SW5kZXgsIGNvbHVtbkluZGV4KTtcbiAgICAgIH1cblxuICAgICAgY29uc3QgZWRpdG9yQ2xhc3MgPSBjcyh7XG4gICAgICAgIGFuaW1hdGVkOiBoYXNFcnJvcixcbiAgICAgICAgc2hha2U6IGhhc0Vycm9yXG4gICAgICB9LCBjdXN0b21FZGl0b3JDbGFzcyk7XG5cbiAgICAgIGxldCBlZGl0b3JQcm9wcyA9IHtcbiAgICAgICAgcmVmOiBub2RlID0+IHRoaXMuZWRpdG9yID0gbm9kZSxcbiAgICAgICAgZGVmYXVsdFZhbHVlOiB2YWx1ZSxcbiAgICAgICAgc3R5bGU6IGVkaXRvclN0eWxlLFxuICAgICAgICBjbGFzc05hbWU6IGVkaXRvckNsYXNzLFxuICAgICAgICBvbktleURvd246IHRoaXMuaGFuZGxlS2V5RG93bixcbiAgICAgICAgb25CbHVyOiB0aGlzLmhhbmRsZUJsdXJcbiAgICAgIH07XG5cbiAgICAgIGlmIChvblN0YXJ0RWRpdCkge1xuICAgICAgICBlZGl0b3JQcm9wcy5kaWRNb3VudCA9ICgpID0+IG9uU3RhcnRFZGl0KHJvdywgY29sdW1uLCByb3dJbmRleCwgY29sdW1uSW5kZXgpO1xuICAgICAgfVxuXG4gICAgICBjb25zdCBpc0RlZmF1bHRFZGl0b3JEZWZpbmVkID0gXy5pc09iamVjdChjb2x1bW4uZWRpdG9yKTtcblxuICAgICAgaWYgKGlzRGVmYXVsdEVkaXRvckRlZmluZWQpIHtcbiAgICAgICAgZWRpdG9yUHJvcHMgPSB7XG4gICAgICAgICAgLi4uZWRpdG9yUHJvcHMsXG4gICAgICAgICAgLi4uY29sdW1uLmVkaXRvclxuICAgICAgICB9O1xuICAgICAgfSBlbHNlIGlmIChfLmlzRnVuY3Rpb24oY29sdW1uLmVkaXRvclJlbmRlcmVyKSkge1xuICAgICAgICBlZGl0b3JQcm9wcyA9IHtcbiAgICAgICAgICAuLi5lZGl0b3JQcm9wcyxcbiAgICAgICAgICBvblVwZGF0ZTogdGhpcy5iZWZvcmVDb21wbGV0ZVxuICAgICAgICB9O1xuICAgICAgfVxuXG4gICAgICBpZiAoXy5pc0Z1bmN0aW9uKGNvbHVtbi5lZGl0b3JSZW5kZXJlcikpIHtcbiAgICAgICAgZWRpdG9yID0gY29sdW1uLmVkaXRvclJlbmRlcmVyKGVkaXRvclByb3BzLCB2YWx1ZSwgcm93LCBjb2x1bW4sIHJvd0luZGV4LCBjb2x1bW5JbmRleCk7XG4gICAgICB9IGVsc2UgaWYgKGlzRGVmYXVsdEVkaXRvckRlZmluZWQgJiYgY29sdW1uLmVkaXRvci50eXBlID09PSBFRElUVFlQRS5TRUxFQ1QpIHtcbiAgICAgICAgZWRpdG9yID0gPERyb3Bkb3duRWRpdG9yIHsgLi4uZWRpdG9yUHJvcHMgfSByb3c9eyByb3cgfSBjb2x1bW49eyBjb2x1bW4gfSAvPjtcbiAgICAgIH0gZWxzZSBpZiAoaXNEZWZhdWx0RWRpdG9yRGVmaW5lZCAmJiBjb2x1bW4uZWRpdG9yLnR5cGUgPT09IEVESVRUWVBFLlRFWFRBUkVBKSB7XG4gICAgICAgIGVkaXRvciA9IDxUZXh0QXJlYUVkaXRvciB7IC4uLmVkaXRvclByb3BzIH0gYXV0b1NlbGVjdFRleHQ9eyBhdXRvU2VsZWN0VGV4dCB9IC8+O1xuICAgICAgfSBlbHNlIGlmIChpc0RlZmF1bHRFZGl0b3JEZWZpbmVkICYmIGNvbHVtbi5lZGl0b3IudHlwZSA9PT0gRURJVFRZUEUuQ0hFQ0tCT1gpIHtcbiAgICAgICAgZWRpdG9yID0gPENoZWNrQm94RWRpdG9yIHsgLi4uZWRpdG9yUHJvcHMgfSAvPjtcbiAgICAgIH0gZWxzZSBpZiAoaXNEZWZhdWx0RWRpdG9yRGVmaW5lZCAmJiBjb2x1bW4uZWRpdG9yLnR5cGUgPT09IEVESVRUWVBFLkRBVEUpIHtcbiAgICAgICAgZWRpdG9yID0gPERhdGVFZGl0b3IgeyAuLi5lZGl0b3JQcm9wcyB9IC8+O1xuICAgICAgfSBlbHNlIHtcbiAgICAgICAgZWRpdG9yID0gPFRleHRFZGl0b3IgeyAuLi5lZGl0b3JQcm9wcyB9IGF1dG9TZWxlY3RUZXh0PXsgYXV0b1NlbGVjdFRleHQgfSAvPjtcbiAgICAgIH1cblxuICAgICAgcmV0dXJuIChcbiAgICAgICAgPHRkXG4gICAgICAgICAgY2xhc3NOYW1lPXsgY3MoJ3JlYWN0LWJvb3RzdHJhcC10YWJsZS1lZGl0aW5nLWNlbGwnLCBjbGFzc05hbWUpIH1cbiAgICAgICAgICBzdHlsZT17IHN0eWxlIH1cbiAgICAgICAgICBvbkNsaWNrPXsgdGhpcy5oYW5kbGVDbGljayB9XG4gICAgICAgID5cbiAgICAgICAgICB7IGVkaXRvciB9XG4gICAgICAgICAgeyBoYXNFcnJvciA/IDxFZGl0b3JJbmRpY2F0b3IgaW52YWxpZE1lc3NhZ2U9eyB0aGlzLnN0YXRlLmludmFsaWRNZXNzYWdlIH0gLz4gOiBudWxsIH1cbiAgICAgICAgPC90ZD5cbiAgICAgICk7XG4gICAgfVxuICB9O1xuXG5cblxuLy8gV0VCUEFDSyBGT09URVIgLy9cbi8vIC4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1lZGl0b3Ivc3JjL2VkaXRpbmctY2VsbC5qcyIsIi8qIGVzbGludCBuby1yZXR1cm4tYXNzaWduOiAwICovXG5pbXBvcnQgUmVhY3QsIHsgQ29tcG9uZW50IH0gZnJvbSAncmVhY3QnO1xuaW1wb3J0IGNzIGZyb20gJ2NsYXNzbmFtZXMnO1xuaW1wb3J0IFByb3BUeXBlcyBmcm9tICdwcm9wLXR5cGVzJztcblxuY2xhc3MgRHJvcERvd25FZGl0b3IgZXh0ZW5kcyBDb21wb25lbnQge1xuICBjb25zdHJ1Y3Rvcihwcm9wcykge1xuICAgIHN1cGVyKHByb3BzKTtcbiAgICBsZXQgb3B0aW9ucyA9IHByb3BzLm9wdGlvbnM7XG4gICAgaWYgKHByb3BzLmdldE9wdGlvbnMpIHtcbiAgICAgIG9wdGlvbnMgPSBwcm9wcy5nZXRPcHRpb25zKFxuICAgICAgICB0aGlzLnNldE9wdGlvbnMuYmluZCh0aGlzKSxcbiAgICAgICAge1xuICAgICAgICAgIHJvdzogcHJvcHMucm93LFxuICAgICAgICAgIGNvbHVtbjogcHJvcHMuY29sdW1uXG4gICAgICAgIH1cbiAgICAgICkgfHwgW107XG4gICAgfVxuICAgIHRoaXMuc3RhdGUgPSB7IG9wdGlvbnMgfTtcbiAgfVxuXG4gIGNvbXBvbmVudERpZE1vdW50KCkge1xuICAgIGNvbnN0IHsgZGVmYXVsdFZhbHVlLCBkaWRNb3VudCB9ID0gdGhpcy5wcm9wcztcbiAgICB0aGlzLnNlbGVjdC52YWx1ZSA9IGRlZmF1bHRWYWx1ZTtcbiAgICB0aGlzLnNlbGVjdC5mb2N1cygpO1xuICAgIGlmIChkaWRNb3VudCkgZGlkTW91bnQoKTtcbiAgfVxuXG4gIHNldE9wdGlvbnMob3B0aW9ucykge1xuICAgIHRoaXMuc2V0U3RhdGUoeyBvcHRpb25zIH0pO1xuICB9XG5cbiAgZ2V0VmFsdWUoKSB7XG4gICAgcmV0dXJuIHRoaXMuc2VsZWN0LnZhbHVlO1xuICB9XG5cbiAgcmVuZGVyKCkge1xuICAgIGNvbnN0IHsgZGVmYXVsdFZhbHVlLCBkaWRNb3VudCwgZ2V0T3B0aW9ucywgY2xhc3NOYW1lLCAuLi5yZXN0IH0gPSB0aGlzLnByb3BzO1xuICAgIGNvbnN0IGVkaXRvckNsYXNzID0gY3MoJ2Zvcm0tY29udHJvbCBlZGl0b3IgZWRpdC1zZWxlY3QnLCBjbGFzc05hbWUpO1xuXG4gICAgY29uc3QgYXR0ciA9IHtcbiAgICAgIC4uLnJlc3QsXG4gICAgICBjbGFzc05hbWU6IGVkaXRvckNsYXNzXG4gICAgfTtcblxuICAgIHJldHVybiAoXG4gICAgICA8c2VsZWN0XG4gICAgICAgIHsgLi4uYXR0ciB9XG4gICAgICAgIHJlZj17IG5vZGUgPT4gdGhpcy5zZWxlY3QgPSBub2RlIH1cbiAgICAgICAgZGVmYXVsdFZhbHVlPXsgZGVmYXVsdFZhbHVlIH1cbiAgICAgID5cbiAgICAgICAge1xuICAgICAgICAgIHRoaXMuc3RhdGUub3B0aW9ucy5tYXAoKHsgbGFiZWwsIHZhbHVlIH0pID0+IChcbiAgICAgICAgICAgIDxvcHRpb24ga2V5PXsgdmFsdWUgfSB2YWx1ZT17IHZhbHVlIH0+eyBsYWJlbCB9PC9vcHRpb24+XG4gICAgICAgICAgKSlcbiAgICAgICAgfVxuICAgICAgPC9zZWxlY3Q+XG4gICAgKTtcbiAgfVxufVxuXG5Ecm9wRG93bkVkaXRvci5wcm9wVHlwZXMgPSB7XG4gIHJvdzogUHJvcFR5cGVzLm9iamVjdC5pc1JlcXVpcmVkLFxuICBjb2x1bW46IFByb3BUeXBlcy5vYmplY3QuaXNSZXF1aXJlZCxcbiAgZGVmYXVsdFZhbHVlOiBQcm9wVHlwZXMub25lT2ZUeXBlKFtcbiAgICBQcm9wVHlwZXMuc3RyaW5nLFxuICAgIFByb3BUeXBlcy5udW1iZXJcbiAgXSksXG4gIGNsYXNzTmFtZTogUHJvcFR5cGVzLnN0cmluZyxcbiAgc3R5bGU6IFByb3BUeXBlcy5vYmplY3QsXG4gIG9wdGlvbnM6IFByb3BUeXBlcy5vbmVPZlR5cGUoW1xuICAgIFByb3BUeXBlcy5hcnJheU9mKFByb3BUeXBlcy5zaGFwZSh7XG4gICAgICBsYWJlbDogUHJvcFR5cGVzLnN0cmluZyxcbiAgICAgIHZhbHVlOiBQcm9wVHlwZXMuYW55XG4gICAgfSkpXG4gIF0pLFxuICBkaWRNb3VudDogUHJvcFR5cGVzLmZ1bmMsXG4gIGdldE9wdGlvbnM6IFByb3BUeXBlcy5mdW5jXG59O1xuRHJvcERvd25FZGl0b3IuZGVmYXVsdFByb3BzID0ge1xuICBjbGFzc05hbWU6ICcnLFxuICBkZWZhdWx0VmFsdWU6ICcnLFxuICBzdHlsZToge30sXG4gIG9wdGlvbnM6IFtdLFxuICBkaWRNb3VudDogdW5kZWZpbmVkLFxuICBnZXRPcHRpb25zOiB1bmRlZmluZWRcbn07XG5leHBvcnQgZGVmYXVsdCBEcm9wRG93bkVkaXRvcjtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyAuL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZWRpdG9yL3NyYy9kcm9wZG93bi1lZGl0b3IuanMiLCIvKiBlc2xpbnQgbm8tcmV0dXJuLWFzc2lnbjogMCAqL1xuaW1wb3J0IFJlYWN0LCB7IENvbXBvbmVudCB9IGZyb20gJ3JlYWN0JztcbmltcG9ydCBjcyBmcm9tICdjbGFzc25hbWVzJztcbmltcG9ydCBQcm9wVHlwZXMgZnJvbSAncHJvcC10eXBlcyc7XG5cbmNsYXNzIFRleHRBcmVhRWRpdG9yIGV4dGVuZHMgQ29tcG9uZW50IHtcbiAgY29uc3RydWN0b3IocHJvcHMpIHtcbiAgICBzdXBlcihwcm9wcyk7XG4gICAgdGhpcy5oYW5kbGVLZXlEb3duID0gdGhpcy5oYW5kbGVLZXlEb3duLmJpbmQodGhpcyk7XG4gIH1cblxuICBjb21wb25lbnREaWRNb3VudCgpIHtcbiAgICBjb25zdCB7IGRlZmF1bHRWYWx1ZSwgZGlkTW91bnQsIGF1dG9TZWxlY3RUZXh0IH0gPSB0aGlzLnByb3BzO1xuICAgIHRoaXMudGV4dC52YWx1ZSA9IGRlZmF1bHRWYWx1ZTtcbiAgICB0aGlzLnRleHQuZm9jdXMoKTtcbiAgICBpZiAoYXV0b1NlbGVjdFRleHQpIHRoaXMudGV4dC5zZWxlY3QoKTtcbiAgICBpZiAoZGlkTW91bnQpIGRpZE1vdW50KCk7XG4gIH1cblxuICBnZXRWYWx1ZSgpIHtcbiAgICByZXR1cm4gdGhpcy50ZXh0LnZhbHVlO1xuICB9XG5cbiAgaGFuZGxlS2V5RG93bihlKSB7XG4gICAgaWYgKGUua2V5Q29kZSA9PT0gMTMgJiYgIWUuc2hpZnRLZXkpIHJldHVybjtcbiAgICBpZiAodGhpcy5wcm9wcy5vbktleURvd24pIHtcbiAgICAgIHRoaXMucHJvcHMub25LZXlEb3duKGUpO1xuICAgIH1cbiAgfVxuXG4gIHJlbmRlcigpIHtcbiAgICBjb25zdCB7IGRlZmF1bHRWYWx1ZSwgZGlkTW91bnQsIGNsYXNzTmFtZSwgYXV0b1NlbGVjdFRleHQsIC4uLnJlc3QgfSA9IHRoaXMucHJvcHM7XG4gICAgY29uc3QgZWRpdG9yQ2xhc3MgPSBjcygnZm9ybS1jb250cm9sIGVkaXRvciBlZGl0LXRleHRhcmVhJywgY2xhc3NOYW1lKTtcbiAgICByZXR1cm4gKFxuICAgICAgPHRleHRhcmVhXG4gICAgICAgIHJlZj17IG5vZGUgPT4gdGhpcy50ZXh0ID0gbm9kZSB9XG4gICAgICAgIHR5cGU9XCJ0ZXh0YXJlYVwiXG4gICAgICAgIGNsYXNzTmFtZT17IGVkaXRvckNsYXNzIH1cbiAgICAgICAgeyAuLi5yZXN0IH1cbiAgICAgICAgb25LZXlEb3duPXsgdGhpcy5oYW5kbGVLZXlEb3duIH1cbiAgICAgIC8+XG4gICAgKTtcbiAgfVxufVxuXG5UZXh0QXJlYUVkaXRvci5wcm9wVHlwZXMgPSB7XG4gIGNsYXNzTmFtZTogUHJvcFR5cGVzLm9uZU9mVHlwZShbXG4gICAgUHJvcFR5cGVzLnN0cmluZyxcbiAgICBQcm9wVHlwZXMub2JqZWN0XG4gIF0pLFxuICBkZWZhdWx0VmFsdWU6IFByb3BUeXBlcy5vbmVPZlR5cGUoW1xuICAgIFByb3BUeXBlcy5zdHJpbmcsXG4gICAgUHJvcFR5cGVzLm51bWJlclxuICBdKSxcbiAgb25LZXlEb3duOiBQcm9wVHlwZXMuZnVuYyxcbiAgYXV0b1NlbGVjdFRleHQ6IFByb3BUeXBlcy5ib29sLFxuICBkaWRNb3VudDogUHJvcFR5cGVzLmZ1bmNcbn07XG5UZXh0QXJlYUVkaXRvci5kZWZhdWx0UHJvcHMgPSB7XG4gIGNsYXNzTmFtZTogJycsXG4gIGRlZmF1bHRWYWx1ZTogJycsXG4gIGF1dG9TZWxlY3RUZXh0OiBmYWxzZSxcbiAgb25LZXlEb3duOiB1bmRlZmluZWQsXG4gIGRpZE1vdW50OiB1bmRlZmluZWRcbn07XG5leHBvcnQgZGVmYXVsdCBUZXh0QXJlYUVkaXRvcjtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyAuL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZWRpdG9yL3NyYy90ZXh0YXJlYS1lZGl0b3IuanMiLCIvKiBlc2xpbnQgbm8tcmV0dXJuLWFzc2lnbjogMCAqL1xuaW1wb3J0IFJlYWN0LCB7IENvbXBvbmVudCB9IGZyb20gJ3JlYWN0JztcbmltcG9ydCBjcyBmcm9tICdjbGFzc25hbWVzJztcbmltcG9ydCBQcm9wVHlwZXMgZnJvbSAncHJvcC10eXBlcyc7XG5cbmNsYXNzIENoZWNrQm94RWRpdG9yIGV4dGVuZHMgQ29tcG9uZW50IHtcbiAgY29uc3RydWN0b3IocHJvcHMpIHtcbiAgICBzdXBlcihwcm9wcyk7XG4gICAgdGhpcy5zdGF0ZSA9IHtcbiAgICAgIGNoZWNrZWQ6IHByb3BzLmRlZmF1bHRWYWx1ZS50b1N0cmluZygpID09PSBwcm9wcy52YWx1ZS5zcGxpdCgnOicpWzBdXG4gICAgfTtcbiAgICB0aGlzLmhhbmRsZUNoYW5nZSA9IHRoaXMuaGFuZGxlQ2hhbmdlLmJpbmQodGhpcyk7XG4gIH1cblxuICBjb21wb25lbnREaWRNb3VudCgpIHtcbiAgICBjb25zdCB7IGRpZE1vdW50IH0gPSB0aGlzLnByb3BzO1xuICAgIHRoaXMuY2hlY2tib3guZm9jdXMoKTtcbiAgICBpZiAoZGlkTW91bnQpIGRpZE1vdW50KCk7XG4gIH1cblxuICBnZXRWYWx1ZSgpIHtcbiAgICBjb25zdCBbcG9zaXRpdmUsIG5lZ2F0aXZlXSA9IHRoaXMucHJvcHMudmFsdWUuc3BsaXQoJzonKTtcbiAgICByZXR1cm4gdGhpcy5jaGVja2JveC5jaGVja2VkID8gcG9zaXRpdmUgOiBuZWdhdGl2ZTtcbiAgfVxuXG4gIGhhbmRsZUNoYW5nZShlKSB7XG4gICAgaWYgKHRoaXMucHJvcHMub25DaGFuZ2UpIHRoaXMucHJvcHMub25DaGFuZ2UoZSk7XG4gICAgY29uc3QgeyB0YXJnZXQgfSA9IGU7XG4gICAgdGhpcy5zZXRTdGF0ZSgoKSA9PiAoeyBjaGVja2VkOiB0YXJnZXQuY2hlY2tlZCB9KSk7XG4gIH1cblxuICByZW5kZXIoKSB7XG4gICAgY29uc3QgeyBkZWZhdWx0VmFsdWUsIGRpZE1vdW50LCBjbGFzc05hbWUsIC4uLnJlc3QgfSA9IHRoaXMucHJvcHM7XG4gICAgY29uc3QgZWRpdG9yQ2xhc3MgPSBjcygnZWRpdG9yIGVkaXQtY2hzZWNrYm94IGNoZWNrYm94JywgY2xhc3NOYW1lKTtcbiAgICByZXR1cm4gKFxuICAgICAgPGlucHV0XG4gICAgICAgIHJlZj17IG5vZGUgPT4gdGhpcy5jaGVja2JveCA9IG5vZGUgfVxuICAgICAgICB0eXBlPVwiY2hlY2tib3hcIlxuICAgICAgICBjbGFzc05hbWU9eyBlZGl0b3JDbGFzcyB9XG4gICAgICAgIHsgLi4ucmVzdCB9XG4gICAgICAgIGNoZWNrZWQ9eyB0aGlzLnN0YXRlLmNoZWNrZWQgfVxuICAgICAgICBvbkNoYW5nZT17IHRoaXMuaGFuZGxlQ2hhbmdlIH1cbiAgICAgIC8+XG4gICAgKTtcbiAgfVxufVxuXG5DaGVja0JveEVkaXRvci5wcm9wVHlwZXMgPSB7XG4gIGNsYXNzTmFtZTogUHJvcFR5cGVzLm9uZU9mVHlwZShbXG4gICAgUHJvcFR5cGVzLnN0cmluZyxcbiAgICBQcm9wVHlwZXMub2JqZWN0XG4gIF0pLFxuICB2YWx1ZTogUHJvcFR5cGVzLnN0cmluZyxcbiAgZGVmYXVsdFZhbHVlOiBQcm9wVHlwZXMuYW55LFxuICBvbkNoYW5nZTogUHJvcFR5cGVzLmZ1bmMsXG4gIGRpZE1vdW50OiBQcm9wVHlwZXMuZnVuY1xufTtcbkNoZWNrQm94RWRpdG9yLmRlZmF1bHRQcm9wcyA9IHtcbiAgY2xhc3NOYW1lOiAnJyxcbiAgdmFsdWU6ICdvbjpvZmYnLFxuICBkZWZhdWx0VmFsdWU6IGZhbHNlLFxuICBvbkNoYW5nZTogdW5kZWZpbmVkLFxuICBkaWRNb3VudDogdW5kZWZpbmVkXG59O1xuZXhwb3J0IGRlZmF1bHQgQ2hlY2tCb3hFZGl0b3I7XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLWVkaXRvci9zcmMvY2hlY2tib3gtZWRpdG9yLmpzIiwiLyogZXNsaW50IG5vLXJldHVybi1hc3NpZ246IDAgKi9cbmltcG9ydCBSZWFjdCwgeyBDb21wb25lbnQgfSBmcm9tICdyZWFjdCc7XG5pbXBvcnQgY3MgZnJvbSAnY2xhc3NuYW1lcyc7XG5pbXBvcnQgUHJvcFR5cGVzIGZyb20gJ3Byb3AtdHlwZXMnO1xuXG5jbGFzcyBEYXRlRWRpdG9yIGV4dGVuZHMgQ29tcG9uZW50IHtcbiAgY29tcG9uZW50RGlkTW91bnQoKSB7XG4gICAgY29uc3QgeyBkZWZhdWx0VmFsdWUsIGRpZE1vdW50IH0gPSB0aGlzLnByb3BzO1xuICAgIHRoaXMuZGF0ZS52YWx1ZUFzRGF0ZSA9IG5ldyBEYXRlKGRlZmF1bHRWYWx1ZSk7XG4gICAgdGhpcy5kYXRlLmZvY3VzKCk7XG4gICAgaWYgKGRpZE1vdW50KSBkaWRNb3VudCgpO1xuICB9XG5cbiAgZ2V0VmFsdWUoKSB7XG4gICAgcmV0dXJuIHRoaXMuZGF0ZS52YWx1ZTtcbiAgfVxuXG4gIHJlbmRlcigpIHtcbiAgICBjb25zdCB7IGRlZmF1bHRWYWx1ZSwgZGlkTW91bnQsIGNsYXNzTmFtZSwgLi4ucmVzdCB9ID0gdGhpcy5wcm9wcztcbiAgICBjb25zdCBlZGl0b3JDbGFzcyA9IGNzKCdmb3JtLWNvbnRyb2wgZWRpdG9yIGVkaXQtZGF0ZScsIGNsYXNzTmFtZSk7XG4gICAgcmV0dXJuIChcbiAgICAgIDxpbnB1dFxuICAgICAgICByZWY9eyBub2RlID0+IHRoaXMuZGF0ZSA9IG5vZGUgfVxuICAgICAgICB0eXBlPVwiZGF0ZVwiXG4gICAgICAgIGNsYXNzTmFtZT17IGVkaXRvckNsYXNzIH1cbiAgICAgICAgeyAuLi5yZXN0IH1cbiAgICAgIC8+XG4gICAgKTtcbiAgfVxufVxuXG5EYXRlRWRpdG9yLnByb3BUeXBlcyA9IHtcbiAgY2xhc3NOYW1lOiBQcm9wVHlwZXMub25lT2ZUeXBlKFtcbiAgICBQcm9wVHlwZXMuc3RyaW5nLFxuICAgIFByb3BUeXBlcy5vYmplY3RcbiAgXSksXG4gIGRlZmF1bHRWYWx1ZTogUHJvcFR5cGVzLnN0cmluZyxcbiAgZGlkTW91bnQ6IFByb3BUeXBlcy5mdW5jXG59O1xuRGF0ZUVkaXRvci5kZWZhdWx0UHJvcHMgPSB7XG4gIGNsYXNzTmFtZTogJycsXG4gIGRlZmF1bHRWYWx1ZTogJycsXG4gIGRpZE1vdW50OiB1bmRlZmluZWRcbn07XG5leHBvcnQgZGVmYXVsdCBEYXRlRWRpdG9yO1xuXG5cblxuLy8gV0VCUEFDSyBGT09URVIgLy9cbi8vIC4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1lZGl0b3Ivc3JjL2RhdGUtZWRpdG9yLmpzIiwiLyogZXNsaW50IG5vLXJldHVybi1hc3NpZ246IDAgKi9cbmltcG9ydCBSZWFjdCwgeyBDb21wb25lbnQgfSBmcm9tICdyZWFjdCc7XG5pbXBvcnQgY3MgZnJvbSAnY2xhc3NuYW1lcyc7XG5pbXBvcnQgUHJvcFR5cGVzIGZyb20gJ3Byb3AtdHlwZXMnO1xuXG5jbGFzcyBUZXh0RWRpdG9yIGV4dGVuZHMgQ29tcG9uZW50IHtcbiAgY29tcG9uZW50RGlkTW91bnQoKSB7XG4gICAgY29uc3QgeyBkZWZhdWx0VmFsdWUsIGRpZE1vdW50LCBhdXRvU2VsZWN0VGV4dCB9ID0gdGhpcy5wcm9wcztcbiAgICB0aGlzLnRleHQudmFsdWUgPSBkZWZhdWx0VmFsdWU7XG4gICAgdGhpcy50ZXh0LmZvY3VzKCk7XG4gICAgaWYgKGF1dG9TZWxlY3RUZXh0KSB0aGlzLnRleHQuc2VsZWN0KCk7XG4gICAgaWYgKGRpZE1vdW50KSBkaWRNb3VudCgpO1xuICB9XG5cbiAgZ2V0VmFsdWUoKSB7XG4gICAgcmV0dXJuIHRoaXMudGV4dC52YWx1ZTtcbiAgfVxuXG4gIHJlbmRlcigpIHtcbiAgICBjb25zdCB7IGRlZmF1bHRWYWx1ZSwgZGlkTW91bnQsIGNsYXNzTmFtZSwgYXV0b1NlbGVjdFRleHQsIC4uLnJlc3QgfSA9IHRoaXMucHJvcHM7XG4gICAgY29uc3QgZWRpdG9yQ2xhc3MgPSBjcygnZm9ybS1jb250cm9sIGVkaXRvciBlZGl0LXRleHQnLCBjbGFzc05hbWUpO1xuICAgIHJldHVybiAoXG4gICAgICA8aW5wdXRcbiAgICAgICAgcmVmPXsgbm9kZSA9PiB0aGlzLnRleHQgPSBub2RlIH1cbiAgICAgICAgdHlwZT1cInRleHRcIlxuICAgICAgICBjbGFzc05hbWU9eyBlZGl0b3JDbGFzcyB9XG4gICAgICAgIHsgLi4ucmVzdCB9XG4gICAgICAvPlxuICAgICk7XG4gIH1cbn1cblxuVGV4dEVkaXRvci5wcm9wVHlwZXMgPSB7XG4gIGNsYXNzTmFtZTogUHJvcFR5cGVzLm9uZU9mVHlwZShbXG4gICAgUHJvcFR5cGVzLnN0cmluZyxcbiAgICBQcm9wVHlwZXMub2JqZWN0XG4gIF0pLFxuICBkZWZhdWx0VmFsdWU6IFByb3BUeXBlcy5vbmVPZlR5cGUoW1xuICAgIFByb3BUeXBlcy5zdHJpbmcsXG4gICAgUHJvcFR5cGVzLm51bWJlclxuICBdKSxcbiAgYXV0b1NlbGVjdFRleHQ6IFByb3BUeXBlcy5ib29sLFxuICBkaWRNb3VudDogUHJvcFR5cGVzLmZ1bmNcbn07XG5UZXh0RWRpdG9yLmRlZmF1bHRQcm9wcyA9IHtcbiAgY2xhc3NOYW1lOiBudWxsLFxuICBkZWZhdWx0VmFsdWU6ICcnLFxuICBhdXRvU2VsZWN0VGV4dDogZmFsc2UsXG4gIGRpZE1vdW50OiB1bmRlZmluZWRcbn07XG5leHBvcnQgZGVmYXVsdCBUZXh0RWRpdG9yO1xuXG5cblxuLy8gV0VCUEFDSyBGT09URVIgLy9cbi8vIC4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1lZGl0b3Ivc3JjL3RleHQtZWRpdG9yLmpzIiwiLyogZXNsaW50IG5vLXJldHVybi1hc3NpZ246IDAgKi9cbmltcG9ydCBSZWFjdCBmcm9tICdyZWFjdCc7XG5pbXBvcnQgUHJvcFR5cGVzIGZyb20gJ3Byb3AtdHlwZXMnO1xuXG5jb25zdCBFZGl0b3JJbmRpY2F0b3IgPSAoeyBpbnZhbGlkTWVzc2FnZSB9KSA9PlxuICAoXG4gICAgPGRpdiBjbGFzc05hbWU9XCJhbGVydCBhbGVydC1kYW5nZXIgaW5cIiByb2xlPVwiYWxlcnRcIj5cbiAgICAgIDxzdHJvbmc+eyBpbnZhbGlkTWVzc2FnZSB9PC9zdHJvbmc+XG4gICAgPC9kaXY+XG4gICk7XG5cbkVkaXRvckluZGljYXRvci5wcm9wVHlwZXMgPSB7XG4gIGludmFsaWRNZXNzYWdlOiBQcm9wVHlwZXMuc3RyaW5nXG59O1xuXG5FZGl0b3JJbmRpY2F0b3IuZGVmYXVsdFByb3BzID0ge1xuICBpbnZhbGlkTWVzc2FnZTogbnVsbFxufTtcbmV4cG9ydCBkZWZhdWx0IEVkaXRvckluZGljYXRvcjtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyAuL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItZWRpdG9yL3NyYy9lZGl0b3ItaW5kaWNhdG9yLmpzIl0sInNvdXJjZVJvb3QiOiIifQ==
//# sourceMappingURL=react-bootstrap-table2-editor.js.map