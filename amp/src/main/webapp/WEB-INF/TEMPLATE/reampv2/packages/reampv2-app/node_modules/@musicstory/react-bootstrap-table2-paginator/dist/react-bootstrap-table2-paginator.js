(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory(require("react"));
	else if(typeof define === 'function' && define.amd)
		define(["react"], factory);
	else if(typeof exports === 'object')
		exports["ReactBootstrapTable2Paginator"] = factory(require("react"));
	else
		root["ReactBootstrapTable2Paginator"] = factory(root["React"]);
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
/******/ 	return __webpack_require__(__webpack_require__.s = 15);
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
  module.exports = __webpack_require__(16)();
}


/***/ }),
/* 2 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = {
  PAGINATION_SIZE: 5,
  PAGE_START_INDEX: 1,
  With_FIRST_AND_LAST: true,
  SHOW_ALL_PAGE_BTNS: false,
  SHOW_TOTAL: false,
  PAGINATION_TOTAL: null,
  FIRST_PAGE_TEXT: '<<',
  PRE_PAGE_TEXT: '<',
  NEXT_PAGE_TEXT: '>',
  LAST_PAGE_TEXT: '>>',
  NEXT_PAGE_TITLE: 'next page',
  LAST_PAGE_TITLE: 'last page',
  PRE_PAGE_TITLE: 'previous page',
  FIRST_PAGE_TITLE: 'first page',
  SIZE_PER_PAGE_LIST: [10, 25, 30, 50],
  HIDE_SIZE_PER_PAGE: false,
  HIDE_PAGE_LIST_ONLY_ONE_PAGE: false
};

/***/ }),
/* 3 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _const = __webpack_require__(2);

var _const2 = _interopRequireDefault(_const);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint no-mixed-operators: 0 */


exports.default = function (ExtendBase) {
  return function (_ExtendBase) {
    _inherits(PageResolver, _ExtendBase);

    function PageResolver() {
      _classCallCheck(this, PageResolver);

      return _possibleConstructorReturn(this, (PageResolver.__proto__ || Object.getPrototypeOf(PageResolver)).apply(this, arguments));
    }

    _createClass(PageResolver, [{
      key: 'backToPrevPage',
      value: function backToPrevPage() {
        var _props = this.props,
            currPage = _props.currPage,
            pageStartIndex = _props.pageStartIndex;

        return currPage - 1 < pageStartIndex ? pageStartIndex : currPage - 1;
      }
    }, {
      key: 'initialState',
      value: function initialState() {
        var totalPages = this.calculateTotalPage();
        var lastPage = this.calculateLastPage(totalPages);
        return { totalPages: totalPages, lastPage: lastPage };
      }
    }, {
      key: 'calculateTotalPage',
      value: function calculateTotalPage() {
        var sizePerPage = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : this.props.currSizePerPage;
        var dataSize = arguments.length > 1 && arguments[1] !== undefined ? arguments[1] : this.props.dataSize;

        return Math.ceil(dataSize / sizePerPage);
      }
    }, {
      key: 'calculateLastPage',
      value: function calculateLastPage(totalPages) {
        var pageStartIndex = this.props.pageStartIndex;

        return pageStartIndex + totalPages - 1;
      }
    }, {
      key: 'calculateFromTo',
      value: function calculateFromTo() {
        var _props2 = this.props,
            dataSize = _props2.dataSize,
            currPage = _props2.currPage,
            currSizePerPage = _props2.currSizePerPage,
            pageStartIndex = _props2.pageStartIndex;

        var offset = Math.abs(_const2.default.PAGE_START_INDEX - pageStartIndex);

        var from = (currPage - pageStartIndex) * currSizePerPage;
        from = dataSize === 0 ? 0 : from + 1;
        var to = Math.min(currSizePerPage * (currPage + offset), dataSize);
        if (to > dataSize) to = dataSize;

        return [from, to];
      }
    }, {
      key: 'calculatePages',
      value: function calculatePages(totalPages, lastPage) {
        var _props3 = this.props,
            currPage = _props3.currPage,
            paginationSize = _props3.paginationSize,
            pageStartIndex = _props3.pageStartIndex,
            withFirstAndLast = _props3.withFirstAndLast,
            firstPageText = _props3.firstPageText,
            prePageText = _props3.prePageText,
            nextPageText = _props3.nextPageText,
            lastPageText = _props3.lastPageText,
            alwaysShowAllBtns = _props3.alwaysShowAllBtns;


        var pages = [];
        var endPage = totalPages;
        if (endPage <= 0) return [];

        var startPage = Math.max(currPage - Math.floor(paginationSize / 2), pageStartIndex);
        endPage = startPage + paginationSize - 1;

        if (endPage > lastPage) {
          endPage = lastPage;
          startPage = endPage - paginationSize + 1;
        }

        if (alwaysShowAllBtns) {
          if (withFirstAndLast) {
            pages = [firstPageText, prePageText];
          } else {
            pages = [prePageText];
          }
        }

        if (startPage !== pageStartIndex && totalPages > paginationSize && withFirstAndLast && pages.length === 0) {
          pages = [firstPageText, prePageText];
        } else if (totalPages > 1 && pages.length === 0) {
          pages = [prePageText];
        }

        for (var i = startPage; i <= endPage; i += 1) {
          if (i >= pageStartIndex) pages.push(i);
        }

        if (alwaysShowAllBtns || endPage <= lastPage && pages.length > 1) {
          pages.push(nextPageText);
        }
        if (endPage !== lastPage && withFirstAndLast || withFirstAndLast && alwaysShowAllBtns) {
          pages.push(lastPageText);
        }

        // if ((endPage <= lastPage && pages.length > 1) || alwaysShowAllBtns) {
        //   pages.push(nextPageText);
        // }
        // if (endPage !== lastPage && withFirstAndLast) {
        //   pages.push(lastPageText);
        // }

        return pages;
      }
    }, {
      key: 'calculatePageStatus',
      value: function calculatePageStatus() {
        var pages = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : [];

        var _this2 = this;

        var lastPage = arguments[1];
        var disablePageTitle = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : false;
        var _props4 = this.props,
            currPage = _props4.currPage,
            pageStartIndex = _props4.pageStartIndex,
            firstPageText = _props4.firstPageText,
            prePageText = _props4.prePageText,
            nextPageText = _props4.nextPageText,
            lastPageText = _props4.lastPageText,
            alwaysShowAllBtns = _props4.alwaysShowAllBtns;

        var isStart = function isStart(page) {
          return currPage === pageStartIndex && (page === firstPageText || page === prePageText);
        };
        var isEnd = function isEnd(page) {
          return currPage === lastPage && (page === nextPageText || page === lastPageText);
        };

        return pages.filter(function (page) {
          if (alwaysShowAllBtns) {
            return true;
          }
          return !(isStart(page) || isEnd(page));
        }).map(function (page) {
          var title = void 0;
          var active = page === currPage;
          var disabled = isStart(page) || isEnd(page);

          if (page === nextPageText) {
            title = _this2.props.nextPageTitle;
          } else if (page === prePageText) {
            title = _this2.props.prePageTitle;
          } else if (page === firstPageText) {
            title = _this2.props.firstPageTitle;
          } else if (page === lastPageText) {
            title = _this2.props.lastPageTitle;
          } else {
            title = '' + page;
          }

          var pageResult = { page: page, active: active, disabled: disabled };
          if (!disablePageTitle) {
            pageResult.title = title;
          }
          return pageResult;
        });
      }
    }, {
      key: 'calculateSizePerPageStatus',
      value: function calculateSizePerPageStatus() {
        var sizePerPageList = this.props.sizePerPageList;

        return sizePerPageList.map(function (_sizePerPage) {
          var pageText = typeof _sizePerPage.text !== 'undefined' ? _sizePerPage.text : _sizePerPage;
          var pageNumber = typeof _sizePerPage.value !== 'undefined' ? _sizePerPage.value : _sizePerPage;
          return {
            text: '' + pageText,
            page: pageNumber
          };
        });
      }
    }]);

    return PageResolver;
  }(ExtendBase);
};

/***/ }),
/* 4 */
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
/* 5 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _pageResolver2 = __webpack_require__(3);

var _pageResolver3 = _interopRequireDefault(_pageResolver2);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/prop-types: 0 */
/* eslint camelcase: 0 */


exports.default = function (WrappedComponent) {
  return function (_pageResolver) {
    _inherits(PaginationHandler, _pageResolver);

    function PaginationHandler(props) {
      _classCallCheck(this, PaginationHandler);

      var _this = _possibleConstructorReturn(this, (PaginationHandler.__proto__ || Object.getPrototypeOf(PaginationHandler)).call(this, props));

      _this.handleChangePage = _this.handleChangePage.bind(_this);
      _this.handleChangeSizePerPage = _this.handleChangeSizePerPage.bind(_this);
      _this.state = _this.initialState();
      return _this;
    }

    _createClass(PaginationHandler, [{
      key: 'UNSAFE_componentWillReceiveProps',
      value: function UNSAFE_componentWillReceiveProps(nextProps) {
        var dataSize = nextProps.dataSize,
            currSizePerPage = nextProps.currSizePerPage;

        if (currSizePerPage !== this.props.currSizePerPage || dataSize !== this.props.dataSize) {
          var totalPages = this.calculateTotalPage(currSizePerPage, dataSize);
          var lastPage = this.calculateLastPage(totalPages);
          this.setState({ totalPages: totalPages, lastPage: lastPage });
        }
      }
    }, {
      key: 'handleChangeSizePerPage',
      value: function handleChangeSizePerPage(sizePerPage) {
        var _props = this.props,
            currSizePerPage = _props.currSizePerPage,
            onSizePerPageChange = _props.onSizePerPageChange;

        var selectedSize = typeof sizePerPage === 'string' ? parseInt(sizePerPage, 10) : sizePerPage;
        var currPage = this.props.currPage;

        if (selectedSize !== currSizePerPage) {
          var newTotalPages = this.calculateTotalPage(selectedSize);
          var newLastPage = this.calculateLastPage(newTotalPages);
          if (currPage > newLastPage) currPage = newLastPage;
          onSizePerPageChange(selectedSize, currPage);
        }
      }
    }, {
      key: 'handleChangePage',
      value: function handleChangePage(newPage) {
        var page = void 0;
        var _props2 = this.props,
            currPage = _props2.currPage,
            pageStartIndex = _props2.pageStartIndex,
            prePageText = _props2.prePageText,
            nextPageText = _props2.nextPageText,
            lastPageText = _props2.lastPageText,
            firstPageText = _props2.firstPageText,
            onPageChange = _props2.onPageChange;
        var lastPage = this.state.lastPage;


        if (newPage === prePageText) {
          page = this.backToPrevPage();
        } else if (newPage === nextPageText) {
          page = currPage + 1 > lastPage ? lastPage : currPage + 1;
        } else if (newPage === lastPageText) {
          page = lastPage;
        } else if (newPage === firstPageText) {
          page = pageStartIndex;
        } else {
          page = parseInt(newPage, 10);
        }
        if (page !== currPage) {
          onPageChange(page);
        }
      }
    }, {
      key: 'render',
      value: function render() {
        return _react2.default.createElement(WrappedComponent, _extends({}, this.props, {
          lastPage: this.state.lastPage,
          totalPages: this.state.totalPages,
          onPageChange: this.handleChangePage,
          onSizePerPageChange: this.handleChangeSizePerPage
        }));
      }
    }]);

    return PaginationHandler;
  }((0, _pageResolver3.default)(_react.Component));
};

/***/ }),
/* 6 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; } /* eslint react/prop-types: 0 */


exports.default = function (WrappedComponent) {
  return function (_ref) {
    var page = _ref.page,
        sizePerPage = _ref.sizePerPage,
        rest = _objectWithoutProperties(_ref, ['page', 'sizePerPage']);

    return _react2.default.createElement(WrappedComponent, _extends({}, rest, {
      currPage: page,
      currSizePerPage: sizePerPage
    }));
  };
};

/***/ }),
/* 7 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _typeof = typeof Symbol === "function" && typeof Symbol.iterator === "symbol" ? function (obj) { return typeof obj; } : function (obj) { return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _events = __webpack_require__(20);

var _events2 = _interopRequireDefault(_events);

var _const = __webpack_require__(2);

var _const2 = _interopRequireDefault(_const);

var _page = __webpack_require__(8);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/prop-types: 0 */
/* eslint react/require-default-props: 0 */
/* eslint no-lonely-if: 0 */
/* eslint camelcase: 0 */


var StateContext = _react2.default.createContext();

var StateProvider = function (_React$Component) {
  _inherits(StateProvider, _React$Component);

  function StateProvider(props) {
    _classCallCheck(this, StateProvider);

    var _this = _possibleConstructorReturn(this, (StateProvider.__proto__ || Object.getPrototypeOf(StateProvider)).call(this, props));

    _initialiseProps.call(_this);

    _this.handleChangePage = _this.handleChangePage.bind(_this);
    _this.handleDataSizeChange = _this.handleDataSizeChange.bind(_this);
    _this.handleChangeSizePerPage = _this.handleChangeSizePerPage.bind(_this);

    var currPage = void 0;
    var currSizePerPage = void 0;
    var options = props.pagination.options;

    var sizePerPageList = options.sizePerPageList || _const2.default.SIZE_PER_PAGE_LIST;

    // initialize current page
    if (typeof options.page !== 'undefined') {
      currPage = options.page;
    } else if (typeof options.pageStartIndex !== 'undefined') {
      currPage = options.pageStartIndex;
    } else {
      currPage = _const2.default.PAGE_START_INDEX;
    }

    // initialize current sizePerPage
    if (typeof options.sizePerPage !== 'undefined') {
      currSizePerPage = options.sizePerPage;
    } else if (_typeof(sizePerPageList[0]) === 'object') {
      currSizePerPage = sizePerPageList[0].value;
    } else {
      currSizePerPage = sizePerPageList[0];
    }

    _this.currPage = currPage;
    _this.dataSize = options.totalSize;
    _this.currSizePerPage = currSizePerPage;
    _this.dataChangeListener = new _events2.default();
    _this.dataChangeListener.on('filterChanged', _this.handleDataSizeChange);
    return _this;
  }

  _createClass(StateProvider, [{
    key: 'UNSAFE_componentWillReceiveProps',
    value: function UNSAFE_componentWillReceiveProps(nextProps) {
      var custom = nextProps.pagination.options.custom;

      // user should align the page when the page is not fit to the data size when remote enable

      if (this.isRemotePagination() || custom) {
        if (typeof nextProps.pagination.options.page !== 'undefined') {
          this.currPage = nextProps.pagination.options.page;
        }
        if (typeof nextProps.pagination.options.sizePerPage !== 'undefined') {
          this.currSizePerPage = nextProps.pagination.options.sizePerPage;
        }
        if (typeof nextProps.pagination.options.totalSize !== 'undefined') {
          this.dataSize = nextProps.pagination.options.totalSize;
        }
      }
    }
  }, {
    key: 'handleDataSizeChange',
    value: function handleDataSizeChange(newDataSize) {
      var options = this.props.pagination.options;

      var pageStartIndex = typeof options.pageStartIndex === 'undefined' ? _const2.default.PAGE_START_INDEX : options.pageStartIndex;
      this.currPage = (0, _page.alignPage)(newDataSize, this.dataSize, this.currPage, this.currSizePerPage, pageStartIndex);
      this.dataSize = newDataSize;
      this.forceUpdate();
    }
  }, {
    key: 'handleChangePage',
    value: function handleChangePage(currPage) {
      var currSizePerPage = this.currSizePerPage;
      var options = this.props.pagination.options;


      if (options.onPageChange) {
        options.onPageChange(currPage, currSizePerPage);
      }

      this.currPage = currPage;

      if (this.isRemotePagination()) {
        this.getPaginationRemoteEmitter().emit('paginationChange', currPage, currSizePerPage);
        return;
      }
      this.forceUpdate();
    }
  }, {
    key: 'handleChangeSizePerPage',
    value: function handleChangeSizePerPage(currSizePerPage, currPage) {
      var options = this.props.pagination.options;


      if (options.onSizePerPageChange) {
        options.onSizePerPageChange(currSizePerPage, currPage);
      }

      this.currPage = currPage;
      this.currSizePerPage = currSizePerPage;

      if (this.isRemotePagination()) {
        this.getPaginationRemoteEmitter().emit('paginationChange', currPage, currSizePerPage);
        return;
      }
      this.forceUpdate();
    }
  }, {
    key: 'render',
    value: function render() {
      var paginationProps = this.getPaginationProps();
      var pagination = _extends({}, this.props.pagination, {
        options: paginationProps
      });

      return _react2.default.createElement(
        StateContext.Provider,
        {
          value: {
            paginationProps: paginationProps,
            paginationTableProps: {
              pagination: pagination,
              setPaginationRemoteEmitter: this.setPaginationRemoteEmitter,
              dataChangeListener: this.dataChangeListener
            }
          }
        },
        this.props.children
      );
    }
  }]);

  return StateProvider;
}(_react2.default.Component);

var _initialiseProps = function _initialiseProps() {
  var _this2 = this;

  this.getPaginationProps = function () {
    var _props = _this2.props,
        options = _props.pagination.options,
        bootstrap4 = _props.bootstrap4,
        tableId = _props.tableId;
    var currPage = _this2.currPage,
        currSizePerPage = _this2.currSizePerPage,
        dataSize = _this2.dataSize;

    var withFirstAndLast = typeof options.withFirstAndLast === 'undefined' ? _const2.default.With_FIRST_AND_LAST : options.withFirstAndLast;
    var alwaysShowAllBtns = typeof options.alwaysShowAllBtns === 'undefined' ? _const2.default.SHOW_ALL_PAGE_BTNS : options.alwaysShowAllBtns;
    var hideSizePerPage = typeof options.hideSizePerPage === 'undefined' ? _const2.default.HIDE_SIZE_PER_PAGE : options.hideSizePerPage;
    var hidePageListOnlyOnePage = typeof options.hidePageListOnlyOnePage === 'undefined' ? _const2.default.HIDE_PAGE_LIST_ONLY_ONE_PAGE : options.hidePageListOnlyOnePage;
    var pageStartIndex = typeof options.pageStartIndex === 'undefined' ? _const2.default.PAGE_START_INDEX : options.pageStartIndex;
    return _extends({}, options, {
      bootstrap4: bootstrap4,
      tableId: tableId,
      page: currPage,
      sizePerPage: currSizePerPage,
      pageStartIndex: pageStartIndex,
      hidePageListOnlyOnePage: hidePageListOnlyOnePage,
      hideSizePerPage: hideSizePerPage,
      alwaysShowAllBtns: alwaysShowAllBtns,
      withFirstAndLast: withFirstAndLast,
      dataSize: dataSize,
      sizePerPageList: options.sizePerPageList || _const2.default.SIZE_PER_PAGE_LIST,
      paginationSize: options.paginationSize || _const2.default.PAGINATION_SIZE,
      showTotal: options.showTotal,
      pageListRenderer: options.pageListRenderer,
      pageButtonRenderer: options.pageButtonRenderer,
      sizePerPageRenderer: options.sizePerPageRenderer,
      paginationTotalRenderer: options.paginationTotalRenderer,
      sizePerPageOptionRenderer: options.sizePerPageOptionRenderer,
      firstPageText: options.firstPageText || _const2.default.FIRST_PAGE_TEXT,
      prePageText: options.prePageText || _const2.default.PRE_PAGE_TEXT,
      nextPageText: options.nextPageText || _const2.default.NEXT_PAGE_TEXT,
      lastPageText: options.lastPageText || _const2.default.LAST_PAGE_TEXT,
      prePageTitle: options.prePageTitle || _const2.default.PRE_PAGE_TITLE,
      nextPageTitle: options.nextPageTitle || _const2.default.NEXT_PAGE_TITLE,
      firstPageTitle: options.firstPageTitle || _const2.default.FIRST_PAGE_TITLE,
      lastPageTitle: options.lastPageTitle || _const2.default.LAST_PAGE_TITLE,
      onPageChange: _this2.handleChangePage,
      onSizePerPageChange: _this2.handleChangeSizePerPage
    });
  };

  this.setPaginationRemoteEmitter = function (remoteEmitter) {
    _this2.remoteEmitter = remoteEmitter;
  };

  this.getPaginationRemoteEmitter = function () {
    return _this2.remoteEmitter || _this2.props.remoteEmitter;
  };

  this.isRemotePagination = function () {
    var e = {};
    _this2.remoteEmitter.emit('isRemotePagination', e);
    return e.result;
  };
};

exports.default = function () {
  return {
    Provider: StateProvider,
    Consumer: StateContext.Consumer
  };
};

/***/ }),
/* 8 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.getByCurrPage = exports.alignPage = undefined;

var _const = __webpack_require__(2);

var _const2 = _interopRequireDefault(_const);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var getNormalizedPage = function getNormalizedPage(page, pageStartIndex) {
  var offset = Math.abs(1 - pageStartIndex);
  return page + offset;
};

var endIndex = function endIndex(page, sizePerPage, pageStartIndex) {
  return getNormalizedPage(page, pageStartIndex) * sizePerPage - 1;
};

var startIndex = function startIndex(end, sizePerPage) {
  return end - (sizePerPage - 1);
};

var alignPage = exports.alignPage = function alignPage(dataSize, prevDataSize, page, sizePerPage, pageStartIndex) {
  if (prevDataSize < dataSize) return page;
  if (page < pageStartIndex) return pageStartIndex;
  if (dataSize <= 0) return pageStartIndex;
  if (page >= Math.floor(dataSize / sizePerPage) + pageStartIndex && pageStartIndex === 1) {
    return Math.ceil(dataSize / sizePerPage);
  }
  if (page >= Math.floor(dataSize / sizePerPage) && pageStartIndex === 0) {
    var newPage = Math.ceil(dataSize / sizePerPage);
    return newPage - Math.abs(_const2.default.PAGE_START_INDEX - pageStartIndex);
  }
  return page;
};

var getByCurrPage = exports.getByCurrPage = function getByCurrPage(data, page, sizePerPage, pageStartIndex) {
  var dataSize = data.length;
  if (!dataSize) return [];

  var end = endIndex(page, sizePerPage, pageStartIndex);
  var start = startIndex(end, sizePerPage);

  var result = [];
  for (var i = start; i <= end; i += 1) {
    result.push(data[i]);
    if (i + 1 === dataSize) break;
  }
  return result;
};

/***/ }),
/* 9 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.SizePerPageDropdownWithAdapter = undefined;

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _pageResolver2 = __webpack_require__(3);

var _pageResolver3 = _interopRequireDefault(_pageResolver2);

var _sizePerPageDropdown = __webpack_require__(10);

var _sizePerPageDropdown2 = _interopRequireDefault(_sizePerPageDropdown);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/prop-types: 0 */


var sizePerPageDropdownAdapter = function sizePerPageDropdownAdapter(WrappedComponent) {
  return function (_pageResolver) {
    _inherits(SizePerPageDropdownAdapter, _pageResolver);

    function SizePerPageDropdownAdapter(props) {
      _classCallCheck(this, SizePerPageDropdownAdapter);

      var _this = _possibleConstructorReturn(this, (SizePerPageDropdownAdapter.__proto__ || Object.getPrototypeOf(SizePerPageDropdownAdapter)).call(this, props));

      _this.closeDropDown = _this.closeDropDown.bind(_this);
      _this.toggleDropDown = _this.toggleDropDown.bind(_this);
      _this.handleChangeSizePerPage = _this.handleChangeSizePerPage.bind(_this);
      _this.state = { dropdownOpen: false };
      return _this;
    }

    _createClass(SizePerPageDropdownAdapter, [{
      key: 'toggleDropDown',
      value: function toggleDropDown() {
        var dropdownOpen = !this.state.dropdownOpen;
        this.setState(function () {
          return { dropdownOpen: dropdownOpen };
        });
      }
    }, {
      key: 'closeDropDown',
      value: function closeDropDown() {
        this.setState(function () {
          return { dropdownOpen: false };
        });
      }
    }, {
      key: 'handleChangeSizePerPage',
      value: function handleChangeSizePerPage(sizePerPage) {
        this.props.onSizePerPageChange(sizePerPage);
        this.closeDropDown();
      }
    }, {
      key: 'render',
      value: function render() {
        var _props = this.props,
            tableId = _props.tableId,
            bootstrap4 = _props.bootstrap4,
            sizePerPageList = _props.sizePerPageList,
            currSizePerPage = _props.currSizePerPage,
            hideSizePerPage = _props.hideSizePerPage,
            sizePerPageRenderer = _props.sizePerPageRenderer,
            sizePerPageOptionRenderer = _props.sizePerPageOptionRenderer;
        var open = this.state.dropdownOpen;


        if (sizePerPageList.length > 1 && !hideSizePerPage) {
          if (sizePerPageRenderer) {
            return sizePerPageRenderer({
              options: this.calculateSizePerPageStatus(),
              currSizePerPage: '' + currSizePerPage,
              onSizePerPageChange: this.handleChangeSizePerPage
            });
          }
          return _react2.default.createElement(WrappedComponent, _extends({}, this.props, {
            currSizePerPage: '' + currSizePerPage,
            options: this.calculateSizePerPageStatus(),
            optionRenderer: sizePerPageOptionRenderer,
            onSizePerPageChange: this.handleChangeSizePerPage,
            onClick: this.toggleDropDown,
            onBlur: this.closeDropDown,
            open: open,
            tableId: tableId,
            bootstrap4: bootstrap4
          }));
        }
        return null;
      }
    }]);

    return SizePerPageDropdownAdapter;
  }((0, _pageResolver3.default)(_react.Component));
};

var SizePerPageDropdownWithAdapter = exports.SizePerPageDropdownWithAdapter = sizePerPageDropdownAdapter(_sizePerPageDropdown2.default);
exports.default = sizePerPageDropdownAdapter;

/***/ }),
/* 10 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _classnames = __webpack_require__(4);

var _classnames2 = _interopRequireDefault(_classnames);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

var _sizePerPageOption = __webpack_require__(23);

var _sizePerPageOption2 = _interopRequireDefault(_sizePerPageOption);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var sizePerPageDefaultClass = 'react-bs-table-sizePerPage-dropdown';

var SizePerPageDropDown = function SizePerPageDropDown(props) {
  var open = props.open,
      tableId = props.tableId,
      hidden = props.hidden,
      onClick = props.onClick,
      onBlur = props.onBlur,
      options = props.options,
      className = props.className,
      variation = props.variation,
      bootstrap4 = props.bootstrap4,
      btnContextual = props.btnContextual,
      optionRenderer = props.optionRenderer,
      currSizePerPage = props.currSizePerPage,
      onSizePerPageChange = props.onSizePerPageChange;


  var dropDownStyle = { visibility: hidden ? 'hidden' : 'visible' };
  var openClass = open ? 'open show' : '';
  var dropdownClasses = (0, _classnames2.default)(openClass, sizePerPageDefaultClass, variation, className);

  var id = tableId ? tableId + '-pageDropDown' : 'pageDropDown';

  return _react2.default.createElement(
    'span',
    {
      style: dropDownStyle,
      className: dropdownClasses
    },
    _react2.default.createElement(
      'button',
      {
        id: id,
        type: 'button',
        className: 'btn ' + btnContextual + ' dropdown-toggle',
        'data-toggle': 'dropdown',
        'aria-expanded': open,
        onClick: onClick,
        onBlur: onBlur
      },
      currSizePerPage,
      ' ',
      bootstrap4 ? null : _react2.default.createElement(
        'span',
        null,
        _react2.default.createElement('span', { className: 'caret' })
      )
    ),
    _react2.default.createElement(
      'ul',
      {
        className: 'dropdown-menu ' + openClass,
        role: 'menu',
        'aria-labelledby': id
      },
      options.map(function (option) {
        if (optionRenderer) {
          return optionRenderer(_extends({}, option, {
            onSizePerPageChange: onSizePerPageChange
          }));
        }
        return _react2.default.createElement(_sizePerPageOption2.default, _extends({}, option, {
          key: option.text,
          bootstrap4: bootstrap4,
          onSizePerPageChange: onSizePerPageChange
        }));
      })
    )
  );
};

SizePerPageDropDown.propTypes = {
  currSizePerPage: _propTypes2.default.string.isRequired,
  options: _propTypes2.default.array.isRequired,
  onClick: _propTypes2.default.func.isRequired,
  onBlur: _propTypes2.default.func.isRequired,
  onSizePerPageChange: _propTypes2.default.func.isRequired,
  bootstrap4: _propTypes2.default.bool,
  tableId: _propTypes2.default.string,
  open: _propTypes2.default.bool,
  hidden: _propTypes2.default.bool,
  btnContextual: _propTypes2.default.string,
  variation: _propTypes2.default.oneOf(['dropdown', 'dropup']),
  className: _propTypes2.default.string,
  optionRenderer: _propTypes2.default.func
};
SizePerPageDropDown.defaultProps = {
  open: false,
  hidden: false,
  btnContextual: 'btn-default btn-secondary',
  variation: 'dropdown',
  className: '',
  optionRenderer: null,
  bootstrap4: false,
  tableId: null
};

exports.default = SizePerPageDropDown;

/***/ }),
/* 11 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PaginationListWithAdapter = undefined;

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _pageResolver2 = __webpack_require__(3);

var _pageResolver3 = _interopRequireDefault(_pageResolver2);

var _paginationList = __webpack_require__(12);

var _paginationList2 = _interopRequireDefault(_paginationList);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/prop-types: 0 */


var paginationListAdapter = function paginationListAdapter(WrappedComponent) {
  return function (_pageResolver) {
    _inherits(PaginationListAdapter, _pageResolver);

    function PaginationListAdapter() {
      _classCallCheck(this, PaginationListAdapter);

      return _possibleConstructorReturn(this, (PaginationListAdapter.__proto__ || Object.getPrototypeOf(PaginationListAdapter)).apply(this, arguments));
    }

    _createClass(PaginationListAdapter, [{
      key: 'render',
      value: function render() {
        var _props = this.props,
            lastPage = _props.lastPage,
            totalPages = _props.totalPages,
            pageButtonRenderer = _props.pageButtonRenderer,
            onPageChange = _props.onPageChange,
            disablePageTitle = _props.disablePageTitle,
            hidePageListOnlyOnePage = _props.hidePageListOnlyOnePage;

        var pages = this.calculatePageStatus(this.calculatePages(totalPages, lastPage), lastPage, disablePageTitle);
        if (totalPages === 1 && hidePageListOnlyOnePage) {
          return null;
        }
        return _react2.default.createElement(WrappedComponent, {
          pageButtonRenderer: pageButtonRenderer,
          onPageChange: onPageChange,
          pages: pages
        });
      }
    }]);

    return PaginationListAdapter;
  }((0, _pageResolver3.default)(_react.Component));
};

var PaginationListWithAdapter = exports.PaginationListWithAdapter = paginationListAdapter(_paginationList2.default);
exports.default = paginationListAdapter;

/***/ }),
/* 12 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

var _pageButton = __webpack_require__(24);

var _pageButton2 = _interopRequireDefault(_pageButton);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var PaginatonList = function PaginatonList(props) {
  return _react2.default.createElement(
    'ul',
    { className: 'pagination react-bootstrap-table-page-btns-ul' },
    props.pages.map(function (pageProps) {
      if (props.pageButtonRenderer) {
        return props.pageButtonRenderer(_extends({}, pageProps, {
          onPageChange: props.onPageChange
        }));
      }
      return _react2.default.createElement(_pageButton2.default, _extends({
        key: pageProps.page
      }, pageProps, {
        onPageChange: props.onPageChange
      }));
    })
  );
};

PaginatonList.propTypes = {
  pages: _propTypes2.default.arrayOf(_propTypes2.default.shape({
    page: _propTypes2.default.oneOfType([_propTypes2.default.node, _propTypes2.default.number, _propTypes2.default.string]),
    active: _propTypes2.default.bool,
    disable: _propTypes2.default.bool,
    title: _propTypes2.default.string
  })).isRequired,
  onPageChange: _propTypes2.default.func.isRequired,
  pageButtonRenderer: _propTypes2.default.func
};

PaginatonList.defaultProps = {
  pageButtonRenderer: null
};

exports.default = PaginatonList;

/***/ }),
/* 13 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PaginationTotalWithAdapter = undefined;

var _slicedToArray = function () { function sliceIterator(arr, i) { var _arr = []; var _n = true; var _d = false; var _e = undefined; try { for (var _i = arr[Symbol.iterator](), _s; !(_n = (_s = _i.next()).done); _n = true) { _arr.push(_s.value); if (i && _arr.length === i) break; } } catch (err) { _d = true; _e = err; } finally { try { if (!_n && _i["return"]) _i["return"](); } finally { if (_d) throw _e; } } return _arr; } return function (arr, i) { if (Array.isArray(arr)) { return arr; } else if (Symbol.iterator in Object(arr)) { return sliceIterator(arr, i); } else { throw new TypeError("Invalid attempt to destructure non-iterable instance"); } }; }();

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _pageResolver2 = __webpack_require__(3);

var _pageResolver3 = _interopRequireDefault(_pageResolver2);

var _paginationTotal = __webpack_require__(14);

var _paginationTotal2 = _interopRequireDefault(_paginationTotal);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/prop-types: 0 */


var paginationTotalAdapter = function paginationTotalAdapter(WrappedComponent) {
  return function (_pageResolver) {
    _inherits(PaginationTotalAdapter, _pageResolver);

    function PaginationTotalAdapter() {
      _classCallCheck(this, PaginationTotalAdapter);

      return _possibleConstructorReturn(this, (PaginationTotalAdapter.__proto__ || Object.getPrototypeOf(PaginationTotalAdapter)).apply(this, arguments));
    }

    _createClass(PaginationTotalAdapter, [{
      key: 'render',
      value: function render() {
        var _calculateFromTo = this.calculateFromTo(),
            _calculateFromTo2 = _slicedToArray(_calculateFromTo, 2),
            from = _calculateFromTo2[0],
            to = _calculateFromTo2[1];

        return _react2.default.createElement(WrappedComponent, {
          from: from,
          to: to,
          dataSize: this.props.dataSize,
          paginationTotalRenderer: this.props.paginationTotalRenderer
        });
      }
    }]);

    return PaginationTotalAdapter;
  }((0, _pageResolver3.default)(_react.Component));
};

var PaginationTotalWithAdapter = exports.PaginationTotalWithAdapter = paginationTotalAdapter(_paginationTotal2.default);
exports.default = paginationTotalAdapter;

/***/ }),
/* 14 */
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

var PaginationTotal = function PaginationTotal(props) {
  if (props.paginationTotalRenderer) {
    return props.paginationTotalRenderer(props.from, props.to, props.dataSize);
  }
  return _react2.default.createElement(
    'span',
    { className: 'react-bootstrap-table-pagination-total' },
    '\xA0Showing rows ',
    props.from,
    ' to\xA0',
    props.to,
    ' of\xA0',
    props.dataSize
  );
};

PaginationTotal.propTypes = {
  from: _propTypes2.default.number.isRequired,
  to: _propTypes2.default.number.isRequired,
  dataSize: _propTypes2.default.number.isRequired,
  paginationTotalRenderer: _propTypes2.default.func
};

PaginationTotal.defaultProps = {
  paginationTotalRenderer: undefined
};

exports.default = PaginationTotal;

/***/ }),
/* 15 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.PaginationTotalStandalone = exports.SizePerPageDropdownStandalone = exports.PaginationListStandalone = exports.PaginationProvider = undefined;

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

var _stateContext = __webpack_require__(7);

var _stateContext2 = _interopRequireDefault(_stateContext);

var _dataContext = __webpack_require__(21);

var _dataContext2 = _interopRequireDefault(_dataContext);

var _paginationListStandalone = __webpack_require__(25);

var _paginationListStandalone2 = _interopRequireDefault(_paginationListStandalone);

var _sizePerPageDropdownStandalone = __webpack_require__(26);

var _sizePerPageDropdownStandalone2 = _interopRequireDefault(_sizePerPageDropdownStandalone);

var _paginationTotalStandalone = __webpack_require__(27);

var _paginationTotalStandalone2 = _interopRequireDefault(_paginationTotalStandalone);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

exports.default = function () {
  var options = arguments.length > 0 && arguments[0] !== undefined ? arguments[0] : {};
  return {
    createContext: _dataContext2.default,
    options: options
  };
};

var _createBaseContext = (0, _stateContext2.default)(),
    Provider = _createBaseContext.Provider,
    Consumer = _createBaseContext.Consumer;

var CustomizableProvider = function CustomizableProvider(props) {
  return _react2.default.createElement(
    Provider,
    props,
    _react2.default.createElement(
      Consumer,
      null,
      function (paginationProps) {
        return props.children(paginationProps);
      }
    )
  );
};

CustomizableProvider.propTypes = {
  children: _propTypes2.default.func.isRequired
};

var PaginationProvider = exports.PaginationProvider = CustomizableProvider;
exports.PaginationListStandalone = _paginationListStandalone2.default;
exports.SizePerPageDropdownStandalone = _sizePerPageDropdownStandalone2.default;
exports.PaginationTotalStandalone = _paginationTotalStandalone2.default;

/***/ }),
/* 16 */
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



var emptyFunction = __webpack_require__(17);
var invariant = __webpack_require__(18);
var ReactPropTypesSecret = __webpack_require__(19);

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
/* 17 */
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
/* 18 */
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
/* 19 */
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
/* 20 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";
// Copyright Joyent, Inc. and other Node contributors.
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the
// following conditions:
//
// The above copyright notice and this permission notice shall be included
// in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
// USE OR OTHER DEALINGS IN THE SOFTWARE.



var R = typeof Reflect === 'object' ? Reflect : null
var ReflectApply = R && typeof R.apply === 'function'
  ? R.apply
  : function ReflectApply(target, receiver, args) {
    return Function.prototype.apply.call(target, receiver, args);
  }

var ReflectOwnKeys
if (R && typeof R.ownKeys === 'function') {
  ReflectOwnKeys = R.ownKeys
} else if (Object.getOwnPropertySymbols) {
  ReflectOwnKeys = function ReflectOwnKeys(target) {
    return Object.getOwnPropertyNames(target)
      .concat(Object.getOwnPropertySymbols(target));
  };
} else {
  ReflectOwnKeys = function ReflectOwnKeys(target) {
    return Object.getOwnPropertyNames(target);
  };
}

function ProcessEmitWarning(warning) {
  if (console && console.warn) console.warn(warning);
}

var NumberIsNaN = Number.isNaN || function NumberIsNaN(value) {
  return value !== value;
}

function EventEmitter() {
  EventEmitter.init.call(this);
}
module.exports = EventEmitter;
module.exports.once = once;

// Backwards-compat with node 0.10.x
EventEmitter.EventEmitter = EventEmitter;

EventEmitter.prototype._events = undefined;
EventEmitter.prototype._eventsCount = 0;
EventEmitter.prototype._maxListeners = undefined;

// By default EventEmitters will print a warning if more than 10 listeners are
// added to it. This is a useful default which helps finding memory leaks.
var defaultMaxListeners = 10;

function checkListener(listener) {
  if (typeof listener !== 'function') {
    throw new TypeError('The "listener" argument must be of type Function. Received type ' + typeof listener);
  }
}

Object.defineProperty(EventEmitter, 'defaultMaxListeners', {
  enumerable: true,
  get: function() {
    return defaultMaxListeners;
  },
  set: function(arg) {
    if (typeof arg !== 'number' || arg < 0 || NumberIsNaN(arg)) {
      throw new RangeError('The value of "defaultMaxListeners" is out of range. It must be a non-negative number. Received ' + arg + '.');
    }
    defaultMaxListeners = arg;
  }
});

EventEmitter.init = function() {

  if (this._events === undefined ||
      this._events === Object.getPrototypeOf(this)._events) {
    this._events = Object.create(null);
    this._eventsCount = 0;
  }

  this._maxListeners = this._maxListeners || undefined;
};

// Obviously not all Emitters should be limited to 10. This function allows
// that to be increased. Set to zero for unlimited.
EventEmitter.prototype.setMaxListeners = function setMaxListeners(n) {
  if (typeof n !== 'number' || n < 0 || NumberIsNaN(n)) {
    throw new RangeError('The value of "n" is out of range. It must be a non-negative number. Received ' + n + '.');
  }
  this._maxListeners = n;
  return this;
};

function _getMaxListeners(that) {
  if (that._maxListeners === undefined)
    return EventEmitter.defaultMaxListeners;
  return that._maxListeners;
}

EventEmitter.prototype.getMaxListeners = function getMaxListeners() {
  return _getMaxListeners(this);
};

EventEmitter.prototype.emit = function emit(type) {
  var args = [];
  for (var i = 1; i < arguments.length; i++) args.push(arguments[i]);
  var doError = (type === 'error');

  var events = this._events;
  if (events !== undefined)
    doError = (doError && events.error === undefined);
  else if (!doError)
    return false;

  // If there is no 'error' event listener then throw.
  if (doError) {
    var er;
    if (args.length > 0)
      er = args[0];
    if (er instanceof Error) {
      // Note: The comments on the `throw` lines are intentional, they show
      // up in Node's output if this results in an unhandled exception.
      throw er; // Unhandled 'error' event
    }
    // At least give some kind of context to the user
    var err = new Error('Unhandled error.' + (er ? ' (' + er.message + ')' : ''));
    err.context = er;
    throw err; // Unhandled 'error' event
  }

  var handler = events[type];

  if (handler === undefined)
    return false;

  if (typeof handler === 'function') {
    ReflectApply(handler, this, args);
  } else {
    var len = handler.length;
    var listeners = arrayClone(handler, len);
    for (var i = 0; i < len; ++i)
      ReflectApply(listeners[i], this, args);
  }

  return true;
};

function _addListener(target, type, listener, prepend) {
  var m;
  var events;
  var existing;

  checkListener(listener);

  events = target._events;
  if (events === undefined) {
    events = target._events = Object.create(null);
    target._eventsCount = 0;
  } else {
    // To avoid recursion in the case that type === "newListener"! Before
    // adding it to the listeners, first emit "newListener".
    if (events.newListener !== undefined) {
      target.emit('newListener', type,
                  listener.listener ? listener.listener : listener);

      // Re-assign `events` because a newListener handler could have caused the
      // this._events to be assigned to a new object
      events = target._events;
    }
    existing = events[type];
  }

  if (existing === undefined) {
    // Optimize the case of one listener. Don't need the extra array object.
    existing = events[type] = listener;
    ++target._eventsCount;
  } else {
    if (typeof existing === 'function') {
      // Adding the second element, need to change to array.
      existing = events[type] =
        prepend ? [listener, existing] : [existing, listener];
      // If we've already got an array, just append.
    } else if (prepend) {
      existing.unshift(listener);
    } else {
      existing.push(listener);
    }

    // Check for listener leak
    m = _getMaxListeners(target);
    if (m > 0 && existing.length > m && !existing.warned) {
      existing.warned = true;
      // No error code for this since it is a Warning
      // eslint-disable-next-line no-restricted-syntax
      var w = new Error('Possible EventEmitter memory leak detected. ' +
                          existing.length + ' ' + String(type) + ' listeners ' +
                          'added. Use emitter.setMaxListeners() to ' +
                          'increase limit');
      w.name = 'MaxListenersExceededWarning';
      w.emitter = target;
      w.type = type;
      w.count = existing.length;
      ProcessEmitWarning(w);
    }
  }

  return target;
}

EventEmitter.prototype.addListener = function addListener(type, listener) {
  return _addListener(this, type, listener, false);
};

EventEmitter.prototype.on = EventEmitter.prototype.addListener;

EventEmitter.prototype.prependListener =
    function prependListener(type, listener) {
      return _addListener(this, type, listener, true);
    };

function onceWrapper() {
  if (!this.fired) {
    this.target.removeListener(this.type, this.wrapFn);
    this.fired = true;
    if (arguments.length === 0)
      return this.listener.call(this.target);
    return this.listener.apply(this.target, arguments);
  }
}

function _onceWrap(target, type, listener) {
  var state = { fired: false, wrapFn: undefined, target: target, type: type, listener: listener };
  var wrapped = onceWrapper.bind(state);
  wrapped.listener = listener;
  state.wrapFn = wrapped;
  return wrapped;
}

EventEmitter.prototype.once = function once(type, listener) {
  checkListener(listener);
  this.on(type, _onceWrap(this, type, listener));
  return this;
};

EventEmitter.prototype.prependOnceListener =
    function prependOnceListener(type, listener) {
      checkListener(listener);
      this.prependListener(type, _onceWrap(this, type, listener));
      return this;
    };

// Emits a 'removeListener' event if and only if the listener was removed.
EventEmitter.prototype.removeListener =
    function removeListener(type, listener) {
      var list, events, position, i, originalListener;

      checkListener(listener);

      events = this._events;
      if (events === undefined)
        return this;

      list = events[type];
      if (list === undefined)
        return this;

      if (list === listener || list.listener === listener) {
        if (--this._eventsCount === 0)
          this._events = Object.create(null);
        else {
          delete events[type];
          if (events.removeListener)
            this.emit('removeListener', type, list.listener || listener);
        }
      } else if (typeof list !== 'function') {
        position = -1;

        for (i = list.length - 1; i >= 0; i--) {
          if (list[i] === listener || list[i].listener === listener) {
            originalListener = list[i].listener;
            position = i;
            break;
          }
        }

        if (position < 0)
          return this;

        if (position === 0)
          list.shift();
        else {
          spliceOne(list, position);
        }

        if (list.length === 1)
          events[type] = list[0];

        if (events.removeListener !== undefined)
          this.emit('removeListener', type, originalListener || listener);
      }

      return this;
    };

EventEmitter.prototype.off = EventEmitter.prototype.removeListener;

EventEmitter.prototype.removeAllListeners =
    function removeAllListeners(type) {
      var listeners, events, i;

      events = this._events;
      if (events === undefined)
        return this;

      // not listening for removeListener, no need to emit
      if (events.removeListener === undefined) {
        if (arguments.length === 0) {
          this._events = Object.create(null);
          this._eventsCount = 0;
        } else if (events[type] !== undefined) {
          if (--this._eventsCount === 0)
            this._events = Object.create(null);
          else
            delete events[type];
        }
        return this;
      }

      // emit removeListener for all listeners on all events
      if (arguments.length === 0) {
        var keys = Object.keys(events);
        var key;
        for (i = 0; i < keys.length; ++i) {
          key = keys[i];
          if (key === 'removeListener') continue;
          this.removeAllListeners(key);
        }
        this.removeAllListeners('removeListener');
        this._events = Object.create(null);
        this._eventsCount = 0;
        return this;
      }

      listeners = events[type];

      if (typeof listeners === 'function') {
        this.removeListener(type, listeners);
      } else if (listeners !== undefined) {
        // LIFO order
        for (i = listeners.length - 1; i >= 0; i--) {
          this.removeListener(type, listeners[i]);
        }
      }

      return this;
    };

function _listeners(target, type, unwrap) {
  var events = target._events;

  if (events === undefined)
    return [];

  var evlistener = events[type];
  if (evlistener === undefined)
    return [];

  if (typeof evlistener === 'function')
    return unwrap ? [evlistener.listener || evlistener] : [evlistener];

  return unwrap ?
    unwrapListeners(evlistener) : arrayClone(evlistener, evlistener.length);
}

EventEmitter.prototype.listeners = function listeners(type) {
  return _listeners(this, type, true);
};

EventEmitter.prototype.rawListeners = function rawListeners(type) {
  return _listeners(this, type, false);
};

EventEmitter.listenerCount = function(emitter, type) {
  if (typeof emitter.listenerCount === 'function') {
    return emitter.listenerCount(type);
  } else {
    return listenerCount.call(emitter, type);
  }
};

EventEmitter.prototype.listenerCount = listenerCount;
function listenerCount(type) {
  var events = this._events;

  if (events !== undefined) {
    var evlistener = events[type];

    if (typeof evlistener === 'function') {
      return 1;
    } else if (evlistener !== undefined) {
      return evlistener.length;
    }
  }

  return 0;
}

EventEmitter.prototype.eventNames = function eventNames() {
  return this._eventsCount > 0 ? ReflectOwnKeys(this._events) : [];
};

function arrayClone(arr, n) {
  var copy = new Array(n);
  for (var i = 0; i < n; ++i)
    copy[i] = arr[i];
  return copy;
}

function spliceOne(list, index) {
  for (; index + 1 < list.length; index++)
    list[index] = list[index + 1];
  list.pop();
}

function unwrapListeners(arr) {
  var ret = new Array(arr.length);
  for (var i = 0; i < ret.length; ++i) {
    ret[i] = arr[i].listener || arr[i];
  }
  return ret;
}

function once(emitter, name) {
  return new Promise(function (resolve, reject) {
    function errorListener(err) {
      emitter.removeListener(name, resolver);
      reject(err);
    }

    function resolver() {
      if (typeof emitter.removeListener === 'function') {
        emitter.removeListener('error', errorListener);
      }
      resolve([].slice.call(arguments));
    };

    eventTargetAgnosticAddListener(emitter, name, resolver, { once: true });
    if (name !== 'error') {
      addErrorHandlerIfEventEmitter(emitter, errorListener, { once: true });
    }
  });
}

function addErrorHandlerIfEventEmitter(emitter, handler, flags) {
  if (typeof emitter.on === 'function') {
    eventTargetAgnosticAddListener(emitter, 'error', handler, flags);
  }
}

function eventTargetAgnosticAddListener(emitter, name, listener, flags) {
  if (typeof emitter.on === 'function') {
    if (flags.once) {
      emitter.once(name, listener);
    } else {
      emitter.on(name, listener);
    }
  } else if (typeof emitter.addEventListener === 'function') {
    // EventTarget does not have `error` event semantics like Node
    // EventEmitters, we do not listen for `error` events here.
    emitter.addEventListener(name, function wrapListener(arg) {
      // IE does not have builtin `{ once: true }` support so we
      // have to do it manually.
      if (flags.once) {
        emitter.removeEventListener(name, wrapListener);
      }
      listener(arg);
    });
  } else {
    throw new TypeError('The "emitter" argument must be of type EventEmitter. Received type ' + typeof emitter);
  }
}


/***/ }),
/* 21 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _get = function get(object, property, receiver) { if (object === null) object = Function.prototype; var desc = Object.getOwnPropertyDescriptor(object, property); if (desc === undefined) { var parent = Object.getPrototypeOf(object); if (parent === null) { return undefined; } else { return get(parent, property, receiver); } } else if ("value" in desc) { return desc.value; } else { var getter = desc.get; if (getter === undefined) { return undefined; } return getter.call(receiver); } };

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

var _const = __webpack_require__(2);

var _const2 = _interopRequireDefault(_const);

var _pagination = __webpack_require__(22);

var _pagination2 = _interopRequireDefault(_pagination);

var _page = __webpack_require__(8);

var _stateContext = __webpack_require__(7);

var _stateContext2 = _interopRequireDefault(_stateContext);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/prop-types: 0 */
/* eslint react/require-default-props: 0 */
/* eslint no-lonely-if: 0 */


var _createBaseContext = (0, _stateContext2.default)(),
    Provider = _createBaseContext.Provider;

var PaginationDataContext = _react2.default.createContext();

var PaginationDataProvider = function (_Provider) {
  _inherits(PaginationDataProvider, _Provider);

  function PaginationDataProvider() {
    var _ref;

    var _temp, _this, _ret;

    _classCallCheck(this, PaginationDataProvider);

    for (var _len = arguments.length, args = Array(_len), _key = 0; _key < _len; _key++) {
      args[_key] = arguments[_key];
    }

    return _ret = (_temp = (_this = _possibleConstructorReturn(this, (_ref = PaginationDataProvider.__proto__ || Object.getPrototypeOf(PaginationDataProvider)).call.apply(_ref, [this].concat(args))), _this), _this.isRemotePagination = function () {
      return _this.props.isRemotePagination();
    }, _this.renderDefaultPagination = function () {
      if (!_this.props.pagination.options.custom) {
        var _this$getPaginationPr = _this.getPaginationProps(),
            currPage = _this$getPaginationPr.page,
            currSizePerPage = _this$getPaginationPr.sizePerPage,
            dataSize = _this$getPaginationPr.dataSize,
            rest = _objectWithoutProperties(_this$getPaginationPr, ['page', 'sizePerPage', 'dataSize']);

        return _react2.default.createElement(_pagination2.default, _extends({}, rest, {
          key: 'pagination',
          dataSize: dataSize || _this.props.data.length,
          currPage: currPage,
          currSizePerPage: currSizePerPage
        }));
      }
      return null;
    }, _temp), _possibleConstructorReturn(_this, _ret);
  }

  _createClass(PaginationDataProvider, [{
    key: 'UNSAFE_componentWillReceiveProps',


    // eslint-disable-next-line camelcase, react/sort-comp
    value: function UNSAFE_componentWillReceiveProps(nextProps) {
      _get(PaginationDataProvider.prototype.__proto__ || Object.getPrototypeOf(PaginationDataProvider.prototype), 'UNSAFE_componentWillReceiveProps', this).call(this, nextProps);
      var currSizePerPage = this.currSizePerPage;
      var _nextProps$pagination = nextProps.pagination.options,
          custom = _nextProps$pagination.custom,
          onPageChange = _nextProps$pagination.onPageChange;


      var pageStartIndex = typeof nextProps.pagination.options.pageStartIndex !== 'undefined' ? nextProps.pagination.options.pageStartIndex : _const2.default.PAGE_START_INDEX;

      // user should align the page when the page is not fit to the data size when remote enable
      if (!this.isRemotePagination() && !custom) {
        var newPage = (0, _page.alignPage)(nextProps.data.length, this.props.data.length, this.currPage, currSizePerPage, pageStartIndex);

        if (this.currPage !== newPage) {
          if (onPageChange) {
            onPageChange(newPage, currSizePerPage);
          }
          this.currPage = newPage;
        }
      }
      if (nextProps.onDataSizeChange && nextProps.data.length !== this.props.data.length) {
        nextProps.onDataSizeChange({ dataSize: nextProps.data.length });
      }
    }
  }, {
    key: 'render',
    value: function render() {
      var data = this.props.data;
      var options = this.props.pagination.options;
      var currPage = this.currPage,
          currSizePerPage = this.currSizePerPage;

      var pageStartIndex = typeof options.pageStartIndex === 'undefined' ? _const2.default.PAGE_START_INDEX : options.pageStartIndex;

      data = this.isRemotePagination() ? data : (0, _page.getByCurrPage)(data, currPage, currSizePerPage, pageStartIndex);

      return _react2.default.createElement(
        PaginationDataContext.Provider,
        { value: { data: data, setRemoteEmitter: this.setRemoteEmitter } },
        this.props.children,
        this.renderDefaultPagination()
      );
    }
  }]);

  return PaginationDataProvider;
}(Provider);

PaginationDataProvider.propTypes = {
  data: _propTypes2.default.array.isRequired,
  remoteEmitter: _propTypes2.default.object.isRequired,
  isRemotePagination: _propTypes2.default.func.isRequired };

exports.default = function () {
  return {
    Provider: PaginationDataProvider,
    Consumer: PaginationDataContext.Consumer
  };
};

/***/ }),
/* 22 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _classnames = __webpack_require__(4);

var _classnames2 = _interopRequireDefault(_classnames);

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

var _pageResolver2 = __webpack_require__(3);

var _pageResolver3 = _interopRequireDefault(_pageResolver2);

var _paginationHandler = __webpack_require__(5);

var _paginationHandler2 = _interopRequireDefault(_paginationHandler);

var _sizePerPageDropdownAdapter = __webpack_require__(9);

var _paginationListAdapter = __webpack_require__(11);

var _paginationTotalAdapter = __webpack_require__(13);

var _const = __webpack_require__(2);

var _const2 = _interopRequireDefault(_const);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _objectWithoutProperties(obj, keys) { var target = {}; for (var i in obj) { if (keys.indexOf(i) >= 0) continue; if (!Object.prototype.hasOwnProperty.call(obj, i)) continue; target[i] = obj[i]; } return target; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/require-default-props: 0 */
/* eslint arrow-body-style: 0 */


var Pagination = function (_pageResolver) {
  _inherits(Pagination, _pageResolver);

  function Pagination() {
    _classCallCheck(this, Pagination);

    return _possibleConstructorReturn(this, (Pagination.__proto__ || Object.getPrototypeOf(Pagination)).apply(this, arguments));
  }

  _createClass(Pagination, [{
    key: 'render',
    value: function render() {
      var _props = this.props,
          tableId = _props.tableId,
          currPage = _props.currPage,
          pageStartIndex = _props.pageStartIndex,
          showTotal = _props.showTotal,
          dataSize = _props.dataSize,
          pageListRenderer = _props.pageListRenderer,
          pageButtonRenderer = _props.pageButtonRenderer,
          paginationTotalRenderer = _props.paginationTotalRenderer,
          hidePageListOnlyOnePage = _props.hidePageListOnlyOnePage,
          totalPages = _props.totalPages,
          lastPage = _props.lastPage,
          onPageChange = _props.onPageChange,
          sizePerPageList = _props.sizePerPageList,
          currSizePerPage = _props.currSizePerPage,
          hideSizePerPage = _props.hideSizePerPage,
          sizePerPageRenderer = _props.sizePerPageRenderer,
          sizePerPageOptionRenderer = _props.sizePerPageOptionRenderer,
          onSizePerPageChange = _props.onSizePerPageChange,
          bootstrap4 = _props.bootstrap4,
          rest = _objectWithoutProperties(_props, ['tableId', 'currPage', 'pageStartIndex', 'showTotal', 'dataSize', 'pageListRenderer', 'pageButtonRenderer', 'paginationTotalRenderer', 'hidePageListOnlyOnePage', 'totalPages', 'lastPage', 'onPageChange', 'sizePerPageList', 'currSizePerPage', 'hideSizePerPage', 'sizePerPageRenderer', 'sizePerPageOptionRenderer', 'onSizePerPageChange', 'bootstrap4']);

      var pages = this.calculatePageStatus(this.calculatePages(totalPages, lastPage), lastPage);
      var pageListClass = (0, _classnames2.default)('react-bootstrap-table-pagination-list', 'col-md-6 col-xs-6 col-sm-6 col-lg-6', {
        'react-bootstrap-table-pagination-list-hidden': hidePageListOnlyOnePage && totalPages === 1
      });
      return _react2.default.createElement(
        'div',
        { className: 'row react-bootstrap-table-pagination' },
        _react2.default.createElement(
          'div',
          { className: 'col-md-6 col-xs-6 col-sm-6 col-lg-6' },
          _react2.default.createElement(_sizePerPageDropdownAdapter.SizePerPageDropdownWithAdapter, {
            bootstrap4: bootstrap4,
            tableId: tableId,
            sizePerPageList: sizePerPageList,
            currSizePerPage: currSizePerPage,
            hideSizePerPage: hideSizePerPage,
            sizePerPageRenderer: sizePerPageRenderer,
            sizePerPageOptionRenderer: sizePerPageOptionRenderer,
            onSizePerPageChange: onSizePerPageChange
          }),
          showTotal ? _react2.default.createElement(_paginationTotalAdapter.PaginationTotalWithAdapter, {
            currPage: currPage,
            currSizePerPage: currSizePerPage,
            pageStartIndex: pageStartIndex,
            dataSize: dataSize,
            paginationTotalRenderer: paginationTotalRenderer
          }) : null
        ),
        pageListRenderer ? pageListRenderer({
          pages: pages,
          onPageChange: onPageChange
        }) : _react2.default.createElement(
          'div',
          { className: pageListClass },
          _react2.default.createElement(_paginationListAdapter.PaginationListWithAdapter, _extends({}, rest, {
            currPage: currPage,
            currSizePerPage: currSizePerPage,
            pageStartIndex: pageStartIndex,
            lastPage: lastPage,
            totalPages: totalPages,
            pageButtonRenderer: pageButtonRenderer,
            onPageChange: onPageChange
          }))
        )
      );
    }
  }]);

  return Pagination;
}((0, _pageResolver3.default)(_react.Component));

Pagination.propTypes = {
  dataSize: _propTypes2.default.number.isRequired,
  sizePerPageList: _propTypes2.default.array.isRequired,
  currPage: _propTypes2.default.number.isRequired,
  currSizePerPage: _propTypes2.default.number.isRequired,
  onPageChange: _propTypes2.default.func.isRequired,
  onSizePerPageChange: _propTypes2.default.func.isRequired,
  disablePageTitle: _propTypes2.default.bool,
  pageStartIndex: _propTypes2.default.number,
  paginationSize: _propTypes2.default.number,
  showTotal: _propTypes2.default.bool,
  pageListRenderer: _propTypes2.default.func,
  pageButtonRenderer: _propTypes2.default.func,
  sizePerPageRenderer: _propTypes2.default.func,
  paginationTotalRenderer: _propTypes2.default.func,
  sizePerPageOptionRenderer: _propTypes2.default.func,
  firstPageText: _propTypes2.default.oneOfType([_propTypes2.default.string, _propTypes2.default.node]),
  prePageText: _propTypes2.default.oneOfType([_propTypes2.default.string, _propTypes2.default.node]),
  nextPageText: _propTypes2.default.oneOfType([_propTypes2.default.string, _propTypes2.default.node]),
  lastPageText: _propTypes2.default.oneOfType([_propTypes2.default.string, _propTypes2.default.node]),
  nextPageTitle: _propTypes2.default.string,
  prePageTitle: _propTypes2.default.string,
  firstPageTitle: _propTypes2.default.string,
  lastPageTitle: _propTypes2.default.string,
  withFirstAndLast: _propTypes2.default.bool,
  alwaysShowAllBtns: _propTypes2.default.bool,
  hideSizePerPage: _propTypes2.default.bool,
  hidePageListOnlyOnePage: _propTypes2.default.bool,
  bootstrap4: _propTypes2.default.bool
};

Pagination.defaultProps = {
  disablePageTitle: false,
  bootstrap4: false,
  pageStartIndex: _const2.default.PAGE_START_INDEX,
  paginationSize: _const2.default.PAGINATION_SIZE,
  withFirstAndLast: _const2.default.With_FIRST_AND_LAST,
  alwaysShowAllBtns: _const2.default.SHOW_ALL_PAGE_BTNS,
  showTotal: _const2.default.SHOW_TOTAL,
  pageListRenderer: null,
  pageButtonRenderer: null,
  sizePerPageRenderer: null,
  paginationTotalRenderer: _const2.default.PAGINATION_TOTAL,
  sizePerPageOptionRenderer: null,
  firstPageText: _const2.default.FIRST_PAGE_TEXT,
  prePageText: _const2.default.PRE_PAGE_TEXT,
  nextPageText: _const2.default.NEXT_PAGE_TEXT,
  lastPageText: _const2.default.LAST_PAGE_TEXT,
  sizePerPageList: _const2.default.SIZE_PER_PAGE_LIST,
  nextPageTitle: _const2.default.NEXT_PAGE_TITLE,
  prePageTitle: _const2.default.PRE_PAGE_TITLE,
  firstPageTitle: _const2.default.FIRST_PAGE_TITLE,
  lastPageTitle: _const2.default.LAST_PAGE_TITLE,
  hideSizePerPage: _const2.default.HIDE_SIZE_PER_PAGE,
  hidePageListOnlyOnePage: _const2.default.HIDE_PAGE_LIST_ONLY_ONE_PAGE
};

exports.default = (0, _paginationHandler2.default)(Pagination);

/***/ }),
/* 23 */
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

/* eslint jsx-a11y/href-no-hash: 0 */
var SizePerPageOption = function SizePerPageOption(_ref) {
  var text = _ref.text,
      page = _ref.page,
      onSizePerPageChange = _ref.onSizePerPageChange,
      bootstrap4 = _ref.bootstrap4;
  return bootstrap4 ? _react2.default.createElement(
    'a',
    {
      href: '#',
      tabIndex: '-1',
      role: 'menuitem',
      className: 'dropdown-item',
      'data-page': page,
      onMouseDown: function onMouseDown(e) {
        e.preventDefault();
        onSizePerPageChange(page);
      }
    },
    text
  ) : _react2.default.createElement(
    'li',
    {
      key: text,
      role: 'presentation',
      className: 'dropdown-item'
    },
    _react2.default.createElement(
      'a',
      {
        href: '#',
        tabIndex: '-1',
        role: 'menuitem',
        'data-page': page,
        onMouseDown: function onMouseDown(e) {
          e.preventDefault();
          onSizePerPageChange(page);
        }
      },
      text
    )
  );
};

SizePerPageOption.propTypes = {
  text: _propTypes2.default.string.isRequired,
  page: _propTypes2.default.number.isRequired,
  onSizePerPageChange: _propTypes2.default.func.isRequired,
  bootstrap4: _propTypes2.default.bool
};

SizePerPageOption.defaultProps = {
  bootstrap4: false
};

exports.default = SizePerPageOption;

/***/ }),
/* 24 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _classnames = __webpack_require__(4);

var _classnames2 = _interopRequireDefault(_classnames);

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _propTypes = __webpack_require__(1);

var _propTypes2 = _interopRequireDefault(_propTypes);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

function _classCallCheck(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new TypeError("Cannot call a class as a function"); } }

function _possibleConstructorReturn(self, call) { if (!self) { throw new ReferenceError("this hasn't been initialised - super() hasn't been called"); } return call && (typeof call === "object" || typeof call === "function") ? call : self; }

function _inherits(subClass, superClass) { if (typeof superClass !== "function" && superClass !== null) { throw new TypeError("Super expression must either be null or a function, not " + typeof superClass); } subClass.prototype = Object.create(superClass && superClass.prototype, { constructor: { value: subClass, enumerable: false, writable: true, configurable: true } }); if (superClass) Object.setPrototypeOf ? Object.setPrototypeOf(subClass, superClass) : subClass.__proto__ = superClass; } /* eslint react/require-default-props: 0 */
/* eslint jsx-a11y/href-no-hash: 0 */


var PageButton = function (_Component) {
  _inherits(PageButton, _Component);

  function PageButton(props) {
    _classCallCheck(this, PageButton);

    var _this = _possibleConstructorReturn(this, (PageButton.__proto__ || Object.getPrototypeOf(PageButton)).call(this, props));

    _this.handleClick = _this.handleClick.bind(_this);
    return _this;
  }

  _createClass(PageButton, [{
    key: 'handleClick',
    value: function handleClick(e) {
      e.preventDefault();
      this.props.onPageChange(this.props.page);
    }
  }, {
    key: 'render',
    value: function render() {
      var _props = this.props,
          page = _props.page,
          title = _props.title,
          active = _props.active,
          disabled = _props.disabled,
          className = _props.className;

      var classes = (0, _classnames2.default)({
        active: active,
        disabled: disabled,
        'page-item': true
      }, className);

      return _react2.default.createElement(
        'li',
        { className: classes, title: title },
        _react2.default.createElement(
          'a',
          { href: '#', onClick: this.handleClick, className: 'page-link' },
          page
        )
      );
    }
  }]);

  return PageButton;
}(_react.Component);

PageButton.propTypes = {
  onPageChange: _propTypes2.default.func.isRequired,
  page: _propTypes2.default.oneOfType([_propTypes2.default.node, _propTypes2.default.number, _propTypes2.default.string]).isRequired,
  active: _propTypes2.default.bool.isRequired,
  disabled: _propTypes2.default.bool.isRequired,
  className: _propTypes2.default.string,
  title: _propTypes2.default.string
};

exports.default = PageButton;

/***/ }),
/* 25 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _paginationList = __webpack_require__(12);

var _paginationList2 = _interopRequireDefault(_paginationList);

var _standaloneAdapter = __webpack_require__(6);

var _standaloneAdapter2 = _interopRequireDefault(_standaloneAdapter);

var _paginationHandler = __webpack_require__(5);

var _paginationHandler2 = _interopRequireDefault(_paginationHandler);

var _paginationListAdapter = __webpack_require__(11);

var _paginationListAdapter2 = _interopRequireDefault(_paginationListAdapter);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var PaginationListStandalone = function PaginationListStandalone(props) {
  return _react2.default.createElement(_paginationList2.default, props);
};

exports.default = (0, _standaloneAdapter2.default)((0, _paginationHandler2.default)((0, _paginationListAdapter2.default)(PaginationListStandalone)));

/***/ }),
/* 26 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _sizePerPageDropdown = __webpack_require__(10);

var _sizePerPageDropdown2 = _interopRequireDefault(_sizePerPageDropdown);

var _standaloneAdapter = __webpack_require__(6);

var _standaloneAdapter2 = _interopRequireDefault(_standaloneAdapter);

var _paginationHandler = __webpack_require__(5);

var _paginationHandler2 = _interopRequireDefault(_paginationHandler);

var _sizePerPageDropdownAdapter = __webpack_require__(9);

var _sizePerPageDropdownAdapter2 = _interopRequireDefault(_sizePerPageDropdownAdapter);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var SizePerPageDropdownStandalone = function SizePerPageDropdownStandalone(props) {
  return _react2.default.createElement(_sizePerPageDropdown2.default, props);
};

exports.default = (0, _standaloneAdapter2.default)((0, _paginationHandler2.default)((0, _sizePerPageDropdownAdapter2.default)(SizePerPageDropdownStandalone)));

/***/ }),
/* 27 */
/***/ (function(module, exports, __webpack_require__) {

"use strict";


Object.defineProperty(exports, "__esModule", {
  value: true
});

var _react = __webpack_require__(0);

var _react2 = _interopRequireDefault(_react);

var _paginationTotal = __webpack_require__(14);

var _paginationTotal2 = _interopRequireDefault(_paginationTotal);

var _standaloneAdapter = __webpack_require__(6);

var _standaloneAdapter2 = _interopRequireDefault(_standaloneAdapter);

var _paginationTotalAdapter = __webpack_require__(13);

var _paginationTotalAdapter2 = _interopRequireDefault(_paginationTotalAdapter);

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

var PaginationTotalStandalone = function PaginationTotalStandalone(props) {
  return _react2.default.createElement(_paginationTotal2.default, props);
};

exports.default = (0, _standaloneAdapter2.default)((0, _paginationTotalAdapter2.default)(PaginationTotalStandalone));

/***/ })
/******/ ]);
});
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly8vd2VicGFjay91bml2ZXJzYWxNb2R1bGVEZWZpbml0aW9uIiwid2VicGFjazovLy93ZWJwYWNrL2Jvb3RzdHJhcCA0MmQ4NmNhZmIxYzAyNGZlYzUxZiIsIndlYnBhY2s6Ly8vZXh0ZXJuYWwge1wicm9vdFwiOlwiUmVhY3RcIixcImNvbW1vbmpzMlwiOlwicmVhY3RcIixcImNvbW1vbmpzXCI6XCJyZWFjdFwiLFwiYW1kXCI6XCJyZWFjdFwifSIsIndlYnBhY2s6Ly8vLi9ub2RlX21vZHVsZXMvcHJvcC10eXBlcy9pbmRleC5qcyIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvY29uc3QuanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1wYWdpbmF0b3Ivc3JjL3BhZ2UtcmVzb2x2ZXIuanMiLCJ3ZWJwYWNrOi8vLy4vbm9kZV9tb2R1bGVzL2NsYXNzbmFtZXMvaW5kZXguanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1wYWdpbmF0b3Ivc3JjL3BhZ2luYXRpb24taGFuZGxlci5qcyIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvc3RhbmRhbG9uZS1hZGFwdGVyLmpzIiwid2VicGFjazovLy8uL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItcGFnaW5hdG9yL3NyYy9zdGF0ZS1jb250ZXh0LmpzIiwid2VicGFjazovLy8uL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItcGFnaW5hdG9yL3NyYy9wYWdlLmpzIiwid2VicGFjazovLy8uL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItcGFnaW5hdG9yL3NyYy9zaXplLXBlci1wYWdlLWRyb3Bkb3duLWFkYXB0ZXIuanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1wYWdpbmF0b3Ivc3JjL3NpemUtcGVyLXBhZ2UtZHJvcGRvd24uanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1wYWdpbmF0b3Ivc3JjL3BhZ2luYXRpb24tbGlzdC1hZGFwdGVyLmpzIiwid2VicGFjazovLy8uL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItcGFnaW5hdG9yL3NyYy9wYWdpbmF0aW9uLWxpc3QuanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1wYWdpbmF0b3Ivc3JjL3BhZ2luYXRpb24tdG90YWwtYWRhcHRlci5qcyIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvcGFnaW5hdGlvbi10b3RhbC5qcyIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9pbmRleC5qcyIsIndlYnBhY2s6Ly8vLi9ub2RlX21vZHVsZXMvcHJvcC10eXBlcy9mYWN0b3J5V2l0aFRocm93aW5nU2hpbXMuanMiLCJ3ZWJwYWNrOi8vLy4vbm9kZV9tb2R1bGVzL2ZianMvbGliL2VtcHR5RnVuY3Rpb24uanMiLCJ3ZWJwYWNrOi8vLy4vbm9kZV9tb2R1bGVzL2ZianMvbGliL2ludmFyaWFudC5qcyIsIndlYnBhY2s6Ly8vLi9ub2RlX21vZHVsZXMvcHJvcC10eXBlcy9saWIvUmVhY3RQcm9wVHlwZXNTZWNyZXQuanMiLCJ3ZWJwYWNrOi8vLy4vbm9kZV9tb2R1bGVzL2V2ZW50cy9ldmVudHMuanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1wYWdpbmF0b3Ivc3JjL2RhdGEtY29udGV4dC5qcyIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvcGFnaW5hdGlvbi5qcyIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvc2l6ZS1wZXItcGFnZS1vcHRpb24uanMiLCJ3ZWJwYWNrOi8vLy4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1wYWdpbmF0b3Ivc3JjL3BhZ2UtYnV0dG9uLmpzIiwid2VicGFjazovLy8uL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItcGFnaW5hdG9yL3NyYy9wYWdpbmF0aW9uLWxpc3Qtc3RhbmRhbG9uZS5qcyIsIndlYnBhY2s6Ly8vLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvc2l6ZS1wZXItcGFnZS1kcm9wZG93bi1zdGFuZGFsb25lLmpzIiwid2VicGFjazovLy8uL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItcGFnaW5hdG9yL3NyYy9wYWdpbmF0aW9uLXRvdGFsLXN0YW5kYWxvbmUuanMiXSwibmFtZXMiOlsiUEFHSU5BVElPTl9TSVpFIiwiUEFHRV9TVEFSVF9JTkRFWCIsIldpdGhfRklSU1RfQU5EX0xBU1QiLCJTSE9XX0FMTF9QQUdFX0JUTlMiLCJTSE9XX1RPVEFMIiwiUEFHSU5BVElPTl9UT1RBTCIsIkZJUlNUX1BBR0VfVEVYVCIsIlBSRV9QQUdFX1RFWFQiLCJORVhUX1BBR0VfVEVYVCIsIkxBU1RfUEFHRV9URVhUIiwiTkVYVF9QQUdFX1RJVExFIiwiTEFTVF9QQUdFX1RJVExFIiwiUFJFX1BBR0VfVElUTEUiLCJGSVJTVF9QQUdFX1RJVExFIiwiU0laRV9QRVJfUEFHRV9MSVNUIiwiSElERV9TSVpFX1BFUl9QQUdFIiwiSElERV9QQUdFX0xJU1RfT05MWV9PTkVfUEFHRSIsInByb3BzIiwiY3VyclBhZ2UiLCJwYWdlU3RhcnRJbmRleCIsInRvdGFsUGFnZXMiLCJjYWxjdWxhdGVUb3RhbFBhZ2UiLCJsYXN0UGFnZSIsImNhbGN1bGF0ZUxhc3RQYWdlIiwic2l6ZVBlclBhZ2UiLCJjdXJyU2l6ZVBlclBhZ2UiLCJkYXRhU2l6ZSIsIk1hdGgiLCJjZWlsIiwib2Zmc2V0IiwiYWJzIiwiQ29uc3QiLCJmcm9tIiwidG8iLCJtaW4iLCJwYWdpbmF0aW9uU2l6ZSIsIndpdGhGaXJzdEFuZExhc3QiLCJmaXJzdFBhZ2VUZXh0IiwicHJlUGFnZVRleHQiLCJuZXh0UGFnZVRleHQiLCJsYXN0UGFnZVRleHQiLCJhbHdheXNTaG93QWxsQnRucyIsInBhZ2VzIiwiZW5kUGFnZSIsInN0YXJ0UGFnZSIsIm1heCIsImZsb29yIiwibGVuZ3RoIiwiaSIsInB1c2giLCJkaXNhYmxlUGFnZVRpdGxlIiwiaXNTdGFydCIsInBhZ2UiLCJpc0VuZCIsImZpbHRlciIsIm1hcCIsInRpdGxlIiwiYWN0aXZlIiwiZGlzYWJsZWQiLCJuZXh0UGFnZVRpdGxlIiwicHJlUGFnZVRpdGxlIiwiZmlyc3RQYWdlVGl0bGUiLCJsYXN0UGFnZVRpdGxlIiwicGFnZVJlc3VsdCIsInNpemVQZXJQYWdlTGlzdCIsIl9zaXplUGVyUGFnZSIsInBhZ2VUZXh0IiwidGV4dCIsInBhZ2VOdW1iZXIiLCJ2YWx1ZSIsIkV4dGVuZEJhc2UiLCJoYW5kbGVDaGFuZ2VQYWdlIiwiYmluZCIsImhhbmRsZUNoYW5nZVNpemVQZXJQYWdlIiwic3RhdGUiLCJpbml0aWFsU3RhdGUiLCJuZXh0UHJvcHMiLCJzZXRTdGF0ZSIsIm9uU2l6ZVBlclBhZ2VDaGFuZ2UiLCJzZWxlY3RlZFNpemUiLCJwYXJzZUludCIsIm5ld1RvdGFsUGFnZXMiLCJuZXdMYXN0UGFnZSIsIm5ld1BhZ2UiLCJvblBhZ2VDaGFuZ2UiLCJiYWNrVG9QcmV2UGFnZSIsIkNvbXBvbmVudCIsInJlc3QiLCJTdGF0ZUNvbnRleHQiLCJSZWFjdCIsImNyZWF0ZUNvbnRleHQiLCJTdGF0ZVByb3ZpZGVyIiwiaGFuZGxlRGF0YVNpemVDaGFuZ2UiLCJvcHRpb25zIiwicGFnaW5hdGlvbiIsInRvdGFsU2l6ZSIsImRhdGFDaGFuZ2VMaXN0ZW5lciIsIkV2ZW50RW1pdHRlciIsIm9uIiwiY3VzdG9tIiwiaXNSZW1vdGVQYWdpbmF0aW9uIiwibmV3RGF0YVNpemUiLCJmb3JjZVVwZGF0ZSIsImdldFBhZ2luYXRpb25SZW1vdGVFbWl0dGVyIiwiZW1pdCIsInBhZ2luYXRpb25Qcm9wcyIsImdldFBhZ2luYXRpb25Qcm9wcyIsInBhZ2luYXRpb25UYWJsZVByb3BzIiwic2V0UGFnaW5hdGlvblJlbW90ZUVtaXR0ZXIiLCJjaGlsZHJlbiIsImJvb3RzdHJhcDQiLCJ0YWJsZUlkIiwiaGlkZVNpemVQZXJQYWdlIiwiaGlkZVBhZ2VMaXN0T25seU9uZVBhZ2UiLCJzaG93VG90YWwiLCJwYWdlTGlzdFJlbmRlcmVyIiwicGFnZUJ1dHRvblJlbmRlcmVyIiwic2l6ZVBlclBhZ2VSZW5kZXJlciIsInBhZ2luYXRpb25Ub3RhbFJlbmRlcmVyIiwic2l6ZVBlclBhZ2VPcHRpb25SZW5kZXJlciIsInJlbW90ZUVtaXR0ZXIiLCJlIiwicmVzdWx0IiwiUHJvdmlkZXIiLCJDb25zdW1lciIsImdldE5vcm1hbGl6ZWRQYWdlIiwiZW5kSW5kZXgiLCJzdGFydEluZGV4IiwiZW5kIiwiYWxpZ25QYWdlIiwicHJldkRhdGFTaXplIiwiZ2V0QnlDdXJyUGFnZSIsImRhdGEiLCJzdGFydCIsInNpemVQZXJQYWdlRHJvcGRvd25BZGFwdGVyIiwiY2xvc2VEcm9wRG93biIsInRvZ2dsZURyb3BEb3duIiwiZHJvcGRvd25PcGVuIiwib3BlbiIsImNhbGN1bGF0ZVNpemVQZXJQYWdlU3RhdHVzIiwiU2l6ZVBlclBhZ2VEcm9wZG93bldpdGhBZGFwdGVyIiwiU2l6ZVBlclBhZ2VEcm9wRG93biIsInNpemVQZXJQYWdlRGVmYXVsdENsYXNzIiwiaGlkZGVuIiwib25DbGljayIsIm9uQmx1ciIsImNsYXNzTmFtZSIsInZhcmlhdGlvbiIsImJ0bkNvbnRleHR1YWwiLCJvcHRpb25SZW5kZXJlciIsImRyb3BEb3duU3R5bGUiLCJ2aXNpYmlsaXR5Iiwib3BlbkNsYXNzIiwiZHJvcGRvd25DbGFzc2VzIiwiaWQiLCJvcHRpb24iLCJwcm9wVHlwZXMiLCJQcm9wVHlwZXMiLCJzdHJpbmciLCJpc1JlcXVpcmVkIiwiYXJyYXkiLCJmdW5jIiwiYm9vbCIsIm9uZU9mIiwiZGVmYXVsdFByb3BzIiwicGFnaW5hdGlvbkxpc3RBZGFwdGVyIiwiY2FsY3VsYXRlUGFnZVN0YXR1cyIsImNhbGN1bGF0ZVBhZ2VzIiwiUGFnaW5hdGlvbkxpc3RXaXRoQWRhcHRlciIsIlBhZ2luYXRpb25MaXN0IiwiUGFnaW5hdG9uTGlzdCIsInBhZ2VQcm9wcyIsImFycmF5T2YiLCJzaGFwZSIsIm9uZU9mVHlwZSIsIm5vZGUiLCJudW1iZXIiLCJkaXNhYmxlIiwicGFnaW5hdGlvblRvdGFsQWRhcHRlciIsImNhbGN1bGF0ZUZyb21UbyIsIlBhZ2luYXRpb25Ub3RhbFdpdGhBZGFwdGVyIiwiUGFnaW5hdGlvblRvdGFsIiwidW5kZWZpbmVkIiwiY3JlYXRlRGF0YUNvbnRleHQiLCJDdXN0b21pemFibGVQcm92aWRlciIsIlBhZ2luYXRpb25Qcm92aWRlciIsIlBhZ2luYXRpb25MaXN0U3RhbmRhbG9uZSIsIlNpemVQZXJQYWdlRHJvcGRvd25TdGFuZGFsb25lIiwiUGFnaW5hdGlvblRvdGFsU3RhbmRhbG9uZSIsIlBhZ2luYXRpb25EYXRhQ29udGV4dCIsIlBhZ2luYXRpb25EYXRhUHJvdmlkZXIiLCJyZW5kZXJEZWZhdWx0UGFnaW5hdGlvbiIsIm9uRGF0YVNpemVDaGFuZ2UiLCJzZXRSZW1vdGVFbWl0dGVyIiwib2JqZWN0IiwiUGFnaW5hdGlvbiIsInBhZ2VMaXN0Q2xhc3MiLCJTaXplUGVyUGFnZU9wdGlvbiIsInByZXZlbnREZWZhdWx0IiwiUGFnZUJ1dHRvbiIsImhhbmRsZUNsaWNrIiwiY2xhc3NlcyJdLCJtYXBwaW5ncyI6IkFBQUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsQ0FBQztBQUNELE87UUNWQTtRQUNBOztRQUVBO1FBQ0E7O1FBRUE7UUFDQTtRQUNBO1FBQ0E7UUFDQTtRQUNBO1FBQ0E7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O0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTs7QUFFQTtBQUNBOzs7Ozs7OztBQzFEYTs7QUFFYjtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQSw2Q0FBNkM7QUFDN0M7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUEsK0I7Ozs7Ozs7QUNuQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRWE7O0FBRWI7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7O0FBRUEsSUFBSSxLQUFxQztBQUN6QztBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQSxxREFBcUQ7QUFDckQsS0FBSztBQUNMO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsT0FBTztBQUNQO0FBQ0E7O0FBRUEsMEJBQTBCO0FBQzFCO0FBQ0E7QUFDQTs7QUFFQSwyQjs7Ozs7OztBQ3BEQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOztBQUVhOztBQUViOztBQUVBOzs7Ozs7OztBQ2JBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRWE7O0FBRWI7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBLENBQUM7QUFDRDtBQUNBO0FBQ0E7QUFDQTtBQUNBLENBQUM7QUFDRDtBQUNBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQSxHQUFHO0FBQ0g7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsQ0FBQzs7QUFFRDs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQSxpQkFBaUIsc0JBQXNCO0FBQ3ZDOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLGVBQWU7QUFDZjtBQUNBO0FBQ0E7QUFDQTtBQUNBLGNBQWM7QUFDZDs7QUFFQTs7QUFFQTtBQUNBOztBQUVBO0FBQ0E7QUFDQSxHQUFHO0FBQ0g7QUFDQTtBQUNBLG1CQUFtQixTQUFTO0FBQzVCO0FBQ0E7O0FBRUE7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLEdBQUc7QUFDSDtBQUNBO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLEdBQUc7QUFDSDtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsS0FBSztBQUNMO0FBQ0EsS0FBSztBQUNMO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTs7QUFFQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtB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LENBQXZCOztrQkFhZXlDLFU7Ozs7Ozs7Ozs7Ozs7QUNwRGY7Ozs7QUFDQTs7OztBQUNBOzs7O0FBQ0E7Ozs7QUFDQTs7Ozs7O0FBRUEsSUFBTWIsMkJBQTJCLFNBQTNCQSx3QkFBMkI7QUFBQSxTQUMvQiw4QkFBQyx3QkFBRCxFQUFxQnpLLEtBQXJCLENBRCtCO0FBQUEsQ0FBakM7O2tCQUtBLGlDQUFrQixpQ0FBa0IscUNBQXNCeUssd0JBQXRCLENBQWxCLENBQWxCLEM7Ozs7Ozs7Ozs7Ozs7QUNYQTs7OztBQUNBOzs7O0FBQ0E7Ozs7QUFDQTs7OztBQUNBOzs7Ozs7QUFFQSxJQUFNQyxnQ0FBZ0MsU0FBaENBLDZCQUFnQztBQUFBLFNBQ3BDLDhCQUFDLDZCQUFELEVBQTBCMUssS0FBMUIsQ0FEb0M7QUFBQSxDQUF0Qzs7a0JBS0EsaUNBQWtCLGlDQUFrQiwwQ0FBMkIwSyw2QkFBM0IsQ0FBbEIsQ0FBbEIsQzs7Ozs7Ozs7Ozs7OztBQ1hBOzs7O0FBQ0E7Ozs7QUFDQTs7OztBQUNBOzs7Ozs7QUFFQSxJQUFNQyw0QkFBNEIsU0FBNUJBLHlCQUE0QjtBQUFBLFNBQ2hDLDhCQUFDLHlCQUFELEVBQXNCM0ssS0FBdEIsQ0FEZ0M7QUFBQSxDQUFsQzs7a0JBS0EsaUNBQWtCLHNDQUF1QjJLLHlCQUF2QixDQUFsQixDIiwiZmlsZSI6InJlYWN0LWJvb3RzdHJhcC10YWJsZTItcGFnaW5hdG9yL2Rpc3QvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1wYWdpbmF0b3IuanMiLCJzb3VyY2VzQ29udGVudCI6WyIoZnVuY3Rpb24gd2VicGFja1VuaXZlcnNhbE1vZHVsZURlZmluaXRpb24ocm9vdCwgZmFjdG9yeSkge1xuXHRpZih0eXBlb2YgZXhwb3J0cyA9PT0gJ29iamVjdCcgJiYgdHlwZW9mIG1vZHVsZSA9PT0gJ29iamVjdCcpXG5cdFx0bW9kdWxlLmV4cG9ydHMgPSBmYWN0b3J5KHJlcXVpcmUoXCJyZWFjdFwiKSk7XG5cdGVsc2UgaWYodHlwZW9mIGRlZmluZSA9PT0gJ2Z1bmN0aW9uJyAmJiBkZWZpbmUuYW1kKVxuXHRcdGRlZmluZShbXCJyZWFjdFwiXSwgZmFjdG9yeSk7XG5cdGVsc2UgaWYodHlwZW9mIGV4cG9ydHMgPT09ICdvYmplY3QnKVxuXHRcdGV4cG9ydHNbXCJSZWFjdEJvb3RzdHJhcFRhYmxlMlBhZ2luYXRvclwiXSA9IGZhY3RvcnkocmVxdWlyZShcInJlYWN0XCIpKTtcblx0ZWxzZVxuXHRcdHJvb3RbXCJSZWFjdEJvb3RzdHJhcFRhYmxlMlBhZ2luYXRvclwiXSA9IGZhY3Rvcnkocm9vdFtcIlJlYWN0XCJdKTtcbn0pKHRoaXMsIGZ1bmN0aW9uKF9fV0VCUEFDS19FWFRFUk5BTF9NT0RVTEVfMF9fKSB7XG5yZXR1cm4gXG5cblxuLy8gV0VCUEFDSyBGT09URVIgLy9cbi8vIHdlYnBhY2svdW5pdmVyc2FsTW9kdWxlRGVmaW5pdGlvbiIsIiBcdC8vIFRoZSBtb2R1bGUgY2FjaGVcbiBcdHZhciBpbnN0YWxsZWRNb2R1bGVzID0ge307XG5cbiBcdC8vIFRoZSByZXF1aXJlIGZ1bmN0aW9uXG4gXHRmdW5jdGlvbiBfX3dlYnBhY2tfcmVxdWlyZV9fKG1vZHVsZUlkKSB7XG5cbiBcdFx0Ly8gQ2hlY2sgaWYgbW9kdWxlIGlzIGluIGNhY2hlXG4gXHRcdGlmKGluc3RhbGxlZE1vZHVsZXNbbW9kdWxlSWRdKSB7XG4gXHRcdFx0cmV0dXJuIGluc3RhbGxlZE1vZHVsZXNbbW9kdWxlSWRdLmV4cG9ydHM7XG4gXHRcdH1cbiBcdFx0Ly8gQ3JlYXRlIGEgbmV3IG1vZHVsZSAoYW5kIHB1dCBpdCBpbnRvIHRoZSBjYWNoZSlcbiBcdFx0dmFyIG1vZHVsZSA9IGluc3RhbGxlZE1vZHVsZXNbbW9kdWxlSWRdID0ge1xuIFx0XHRcdGk6IG1vZHVsZUlkLFxuIFx0XHRcdGw6IGZhbHNlLFxuIFx0XHRcdGV4cG9ydHM6IHt9XG4gXHRcdH07XG5cbiBcdFx0Ly8gRXhlY3V0ZSB0aGUgbW9kdWxlIGZ1bmN0aW9uXG4gXHRcdG1vZHVsZXNbbW9kdWxlSWRdLmNhbGwobW9kdWxlLmV4cG9ydHMsIG1vZHVsZSwgbW9kdWxlLmV4cG9ydHMsIF9fd2VicGFja19yZXF1aXJlX18pO1xuXG4gXHRcdC8vIEZsYWcgdGhlIG1vZHVsZSBhcyBsb2FkZWRcbiBcdFx0bW9kdWxlLmwgPSB0cnVlO1xuXG4gXHRcdC8vIFJldHVybiB0aGUgZXhwb3J0cyBvZiB0aGUgbW9kdWxlXG4gXHRcdHJldHVybiBtb2R1bGUuZXhwb3J0cztcbiBcdH1cblxuXG4gXHQvLyBleHBvc2UgdGhlIG1vZHVsZXMgb2JqZWN0IChfX3dlYnBhY2tfbW9kdWxlc19fKVxuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5tID0gbW9kdWxlcztcblxuIFx0Ly8gZXhwb3NlIHRoZSBtb2R1bGUgY2FjaGVcbiBcdF9fd2VicGFja19yZXF1aXJlX18uYyA9IGluc3RhbGxlZE1vZHVsZXM7XG5cbiBcdC8vIGRlZmluZSBnZXR0ZXIgZnVuY3Rpb24gZm9yIGhhcm1vbnkgZXhwb3J0c1xuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5kID0gZnVuY3Rpb24oZXhwb3J0cywgbmFtZSwgZ2V0dGVyKSB7XG4gXHRcdGlmKCFfX3dlYnBhY2tfcmVxdWlyZV9fLm8oZXhwb3J0cywgbmFtZSkpIHtcbiBcdFx0XHRPYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgbmFtZSwge1xuIFx0XHRcdFx0Y29uZmlndXJhYmxlOiBmYWxzZSxcbiBcdFx0XHRcdGVudW1lcmFibGU6IHRydWUsXG4gXHRcdFx0XHRnZXQ6IGdldHRlclxuIFx0XHRcdH0pO1xuIFx0XHR9XG4gXHR9O1xuXG4gXHQvLyBnZXREZWZhdWx0RXhwb3J0IGZ1bmN0aW9uIGZvciBjb21wYXRpYmlsaXR5IHdpdGggbm9uLWhhcm1vbnkgbW9kdWxlc1xuIFx0X193ZWJwYWNrX3JlcXVpcmVfXy5uID0gZnVuY3Rpb24obW9kdWxlKSB7XG4gXHRcdHZhciBnZXR0ZXIgPSBtb2R1bGUgJiYgbW9kdWxlLl9fZXNNb2R1bGUgP1xuIFx0XHRcdGZ1bmN0aW9uIGdldERlZmF1bHQoKSB7IHJldHVybiBtb2R1bGVbJ2RlZmF1bHQnXTsgfSA6XG4gXHRcdFx0ZnVuY3Rpb24gZ2V0TW9kdWxlRXhwb3J0cygpIHsgcmV0dXJuIG1vZHVsZTsgfTtcbiBcdFx0X193ZWJwYWNrX3JlcXVpcmVfXy5kKGdldHRlciwgJ2EnLCBnZXR0ZXIpO1xuIFx0XHRyZXR1cm4gZ2V0dGVyO1xuIFx0fTtcblxuIFx0Ly8gT2JqZWN0LnByb3RvdHlwZS5oYXNPd25Qcm9wZXJ0eS5jYWxsXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLm8gPSBmdW5jdGlvbihvYmplY3QsIHByb3BlcnR5KSB7IHJldHVybiBPYmplY3QucHJvdG90eXBlLmhhc093blByb3BlcnR5LmNhbGwob2JqZWN0LCBwcm9wZXJ0eSk7IH07XG5cbiBcdC8vIF9fd2VicGFja19wdWJsaWNfcGF0aF9fXG4gXHRfX3dlYnBhY2tfcmVxdWlyZV9fLnAgPSBcIlwiO1xuXG4gXHQvLyBMb2FkIGVudHJ5IG1vZHVsZSBhbmQgcmV0dXJuIGV4cG9ydHNcbiBcdHJldHVybiBfX3dlYnBhY2tfcmVxdWlyZV9fKF9fd2VicGFja19yZXF1aXJlX18ucyA9IDE1KTtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyB3ZWJwYWNrL2Jvb3RzdHJhcCA0MmQ4NmNhZmIxYzAyNGZlYzUxZiIsIm1vZHVsZS5leHBvcnRzID0gX19XRUJQQUNLX0VYVEVSTkFMX01PRFVMRV8wX187XG5cblxuLy8vLy8vLy8vLy8vLy8vLy8vXG4vLyBXRUJQQUNLIEZPT1RFUlxuLy8gZXh0ZXJuYWwge1wicm9vdFwiOlwiUmVhY3RcIixcImNvbW1vbmpzMlwiOlwicmVhY3RcIixcImNvbW1vbmpzXCI6XCJyZWFjdFwiLFwiYW1kXCI6XCJyZWFjdFwifVxuLy8gbW9kdWxlIGlkID0gMFxuLy8gbW9kdWxlIGNodW5rcyA9IDAgMSIsIi8qKlxuICogQ29weXJpZ2h0IDIwMTMtcHJlc2VudCwgRmFjZWJvb2ssIEluYy5cbiAqIEFsbCByaWdodHMgcmVzZXJ2ZWQuXG4gKlxuICogVGhpcyBzb3VyY2UgY29kZSBpcyBsaWNlbnNlZCB1bmRlciB0aGUgQlNELXN0eWxlIGxpY2Vuc2UgZm91bmQgaW4gdGhlXG4gKiBMSUNFTlNFIGZpbGUgaW4gdGhlIHJvb3QgZGlyZWN0b3J5IG9mIHRoaXMgc291cmNlIHRyZWUuIEFuIGFkZGl0aW9uYWwgZ3JhbnRcbiAqIG9mIHBhdGVudCByaWdodHMgY2FuIGJlIGZvdW5kIGluIHRoZSBQQVRFTlRTIGZpbGUgaW4gdGhlIHNhbWUgZGlyZWN0b3J5LlxuICovXG5cbmlmIChwcm9jZXNzLmVudi5OT0RFX0VOViAhPT0gJ3Byb2R1Y3Rpb24nKSB7XG4gIHZhciBSRUFDVF9FTEVNRU5UX1RZUEUgPSAodHlwZW9mIFN5bWJvbCA9PT0gJ2Z1bmN0aW9uJyAmJlxuICAgIFN5bWJvbC5mb3IgJiZcbiAgICBTeW1ib2wuZm9yKCdyZWFjdC5lbGVtZW50JykpIHx8XG4gICAgMHhlYWM3O1xuXG4gIHZhciBpc1ZhbGlkRWxlbWVudCA9IGZ1bmN0aW9uKG9iamVjdCkge1xuICAgIHJldHVybiB0eXBlb2Ygb2JqZWN0ID09PSAnb2JqZWN0JyAmJlxuICAgICAgb2JqZWN0ICE9PSBudWxsICYmXG4gICAgICBvYmplY3QuJCR0eXBlb2YgPT09IFJFQUNUX0VMRU1FTlRfVFlQRTtcbiAgfTtcblxuICAvLyBCeSBleHBsaWNpdGx5IHVzaW5nIGBwcm9wLXR5cGVzYCB5b3UgYXJlIG9wdGluZyBpbnRvIG5ldyBkZXZlbG9wbWVudCBiZWhhdmlvci5cbiAgLy8gaHR0cDovL2ZiLm1lL3Byb3AtdHlwZXMtaW4tcHJvZFxuICB2YXIgdGhyb3dPbkRpcmVjdEFjY2VzcyA9IHRydWU7XG4gIG1vZHVsZS5leHBvcnRzID0gcmVxdWlyZSgnLi9mYWN0b3J5V2l0aFR5cGVDaGVja2VycycpKGlzVmFsaWRFbGVtZW50LCB0aHJvd09uRGlyZWN0QWNjZXNzKTtcbn0gZWxzZSB7XG4gIC8vIEJ5IGV4cGxpY2l0bHkgdXNpbmcgYHByb3AtdHlwZXNgIHlvdSBhcmUgb3B0aW5nIGludG8gbmV3IHByb2R1Y3Rpb24gYmVoYXZpb3IuXG4gIC8vIGh0dHA6Ly9mYi5tZS9wcm9wLXR5cGVzLWluLXByb2RcbiAgbW9kdWxlLmV4cG9ydHMgPSByZXF1aXJlKCcuL2ZhY3RvcnlXaXRoVGhyb3dpbmdTaGltcycpKCk7XG59XG5cblxuXG4vLy8vLy8vLy8vLy8vLy8vLy9cbi8vIFdFQlBBQ0sgRk9PVEVSXG4vLyAuL25vZGVfbW9kdWxlcy9wcm9wLXR5cGVzL2luZGV4LmpzXG4vLyBtb2R1bGUgaWQgPSAxXG4vLyBtb2R1bGUgY2h1bmtzID0gMCAxIiwiZXhwb3J0IGRlZmF1bHQge1xuICBQQUdJTkFUSU9OX1NJWkU6IDUsXG4gIFBBR0VfU1RBUlRfSU5ERVg6IDEsXG4gIFdpdGhfRklSU1RfQU5EX0xBU1Q6IHRydWUsXG4gIFNIT1dfQUxMX1BBR0VfQlROUzogZmFsc2UsXG4gIFNIT1dfVE9UQUw6IGZhbHNlLFxuICBQQUdJTkFUSU9OX1RPVEFMOiBudWxsLFxuICBGSVJTVF9QQUdFX1RFWFQ6ICc8PCcsXG4gIFBSRV9QQUdFX1RFWFQ6ICc8JyxcbiAgTkVYVF9QQUdFX1RFWFQ6ICc+JyxcbiAgTEFTVF9QQUdFX1RFWFQ6ICc+PicsXG4gIE5FWFRfUEFHRV9USVRMRTogJ25leHQgcGFnZScsXG4gIExBU1RfUEFHRV9USVRMRTogJ2xhc3QgcGFnZScsXG4gIFBSRV9QQUdFX1RJVExFOiAncHJldmlvdXMgcGFnZScsXG4gIEZJUlNUX1BBR0VfVElUTEU6ICdmaXJzdCBwYWdlJyxcbiAgU0laRV9QRVJfUEFHRV9MSVNUOiBbMTAsIDI1LCAzMCwgNTBdLFxuICBISURFX1NJWkVfUEVSX1BBR0U6IGZhbHNlLFxuICBISURFX1BBR0VfTElTVF9PTkxZX09ORV9QQUdFOiBmYWxzZVxufTtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyAuL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItcGFnaW5hdG9yL3NyYy9jb25zdC5qcyIsIi8qIGVzbGludCBuby1taXhlZC1vcGVyYXRvcnM6IDAgKi9cbmltcG9ydCBDb25zdCBmcm9tICcuL2NvbnN0JztcblxuZXhwb3J0IGRlZmF1bHQgRXh0ZW5kQmFzZSA9PlxuICBjbGFzcyBQYWdlUmVzb2x2ZXIgZXh0ZW5kcyBFeHRlbmRCYXNlIHtcbiAgICBiYWNrVG9QcmV2UGFnZSgpIHtcbiAgICAgIGNvbnN0IHsgY3VyclBhZ2UsIHBhZ2VTdGFydEluZGV4IH0gPSB0aGlzLnByb3BzO1xuICAgICAgcmV0dXJuIChjdXJyUGFnZSAtIDEpIDwgcGFnZVN0YXJ0SW5kZXggPyBwYWdlU3RhcnRJbmRleCA6IGN1cnJQYWdlIC0gMTtcbiAgICB9XG5cbiAgICBpbml0aWFsU3RhdGUoKSB7XG4gICAgICBjb25zdCB0b3RhbFBhZ2VzID0gdGhpcy5jYWxjdWxhdGVUb3RhbFBhZ2UoKTtcbiAgICAgIGNvbnN0IGxhc3RQYWdlID0gdGhpcy5jYWxjdWxhdGVMYXN0UGFnZSh0b3RhbFBhZ2VzKTtcbiAgICAgIHJldHVybiB7IHRvdGFsUGFnZXMsIGxhc3RQYWdlIH07XG4gICAgfVxuXG4gICAgY2FsY3VsYXRlVG90YWxQYWdlKHNpemVQZXJQYWdlID0gdGhpcy5wcm9wcy5jdXJyU2l6ZVBlclBhZ2UsIGRhdGFTaXplID0gdGhpcy5wcm9wcy5kYXRhU2l6ZSkge1xuICAgICAgcmV0dXJuIE1hdGguY2VpbChkYXRhU2l6ZSAvIHNpemVQZXJQYWdlKTtcbiAgICB9XG5cbiAgICBjYWxjdWxhdGVMYXN0UGFnZSh0b3RhbFBhZ2VzKSB7XG4gICAgICBjb25zdCB7IHBhZ2VTdGFydEluZGV4IH0gPSB0aGlzLnByb3BzO1xuICAgICAgcmV0dXJuIHBhZ2VTdGFydEluZGV4ICsgdG90YWxQYWdlcyAtIDE7XG4gICAgfVxuXG4gICAgY2FsY3VsYXRlRnJvbVRvKCkge1xuICAgICAgY29uc3Qge1xuICAgICAgICBkYXRhU2l6ZSxcbiAgICAgICAgY3VyclBhZ2UsXG4gICAgICAgIGN1cnJTaXplUGVyUGFnZSxcbiAgICAgICAgcGFnZVN0YXJ0SW5kZXhcbiAgICAgIH0gPSB0aGlzLnByb3BzO1xuICAgICAgY29uc3Qgb2Zmc2V0ID0gTWF0aC5hYnMoQ29uc3QuUEFHRV9TVEFSVF9JTkRFWCAtIHBhZ2VTdGFydEluZGV4KTtcblxuICAgICAgbGV0IGZyb20gPSAoKGN1cnJQYWdlIC0gcGFnZVN0YXJ0SW5kZXgpICogY3VyclNpemVQZXJQYWdlKTtcbiAgICAgIGZyb20gPSBkYXRhU2l6ZSA9PT0gMCA/IDAgOiBmcm9tICsgMTtcbiAgICAgIGxldCB0byA9IE1hdGgubWluKGN1cnJTaXplUGVyUGFnZSAqIChjdXJyUGFnZSArIG9mZnNldCksIGRhdGFTaXplKTtcbiAgICAgIGlmICh0byA+IGRhdGFTaXplKSB0byA9IGRhdGFTaXplO1xuXG4gICAgICByZXR1cm4gW2Zyb20sIHRvXTtcbiAgICB9XG5cbiAgICBjYWxjdWxhdGVQYWdlcyhcbiAgICAgIHRvdGFsUGFnZXMsXG4gICAgICBsYXN0UGFnZVxuICAgICkge1xuICAgICAgY29uc3Qge1xuICAgICAgICBjdXJyUGFnZSxcbiAgICAgICAgcGFnaW5hdGlvblNpemUsXG4gICAgICAgIHBhZ2VTdGFydEluZGV4LFxuICAgICAgICB3aXRoRmlyc3RBbmRMYXN0LFxuICAgICAgICBmaXJzdFBhZ2VUZXh0LFxuICAgICAgICBwcmVQYWdlVGV4dCxcbiAgICAgICAgbmV4dFBhZ2VUZXh0LFxuICAgICAgICBsYXN0UGFnZVRleHQsXG4gICAgICAgIGFsd2F5c1Nob3dBbGxCdG5zXG4gICAgICB9ID0gdGhpcy5wcm9wcztcblxuICAgICAgbGV0IHBhZ2VzID0gW107XG4gICAgICBsZXQgZW5kUGFnZSA9IHRvdGFsUGFnZXM7XG4gICAgICBpZiAoZW5kUGFnZSA8PSAwKSByZXR1cm4gW107XG5cbiAgICAgIGxldCBzdGFydFBhZ2UgPSBNYXRoLm1heChjdXJyUGFnZSAtIE1hdGguZmxvb3IocGFnaW5hdGlvblNpemUgLyAyKSwgcGFnZVN0YXJ0SW5kZXgpO1xuICAgICAgZW5kUGFnZSA9IHN0YXJ0UGFnZSArIHBhZ2luYXRpb25TaXplIC0gMTtcblxuICAgICAgaWYgKGVuZFBhZ2UgPiBsYXN0UGFnZSkge1xuICAgICAgICBlbmRQYWdlID0gbGFzdFBhZ2U7XG4gICAgICAgIHN0YXJ0UGFnZSA9IGVuZFBhZ2UgLSBwYWdpbmF0aW9uU2l6ZSArIDE7XG4gICAgICB9XG5cbiAgICAgIGlmIChhbHdheXNTaG93QWxsQnRucykge1xuICAgICAgICBpZiAod2l0aEZpcnN0QW5kTGFzdCkge1xuICAgICAgICAgIHBhZ2VzID0gW2ZpcnN0UGFnZVRleHQsIHByZVBhZ2VUZXh0XTtcbiAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICBwYWdlcyA9IFtwcmVQYWdlVGV4dF07XG4gICAgICAgIH1cbiAgICAgIH1cblxuICAgICAgaWYgKHN0YXJ0UGFnZSAhPT0gcGFnZVN0YXJ0SW5kZXggJiZcbiAgICAgICAgdG90YWxQYWdlcyA+IHBhZ2luYXRpb25TaXplICYmXG4gICAgICAgIHdpdGhGaXJzdEFuZExhc3QgJiZcbiAgICAgICAgcGFnZXMubGVuZ3RoID09PSAwXG4gICAgICApIHtcbiAgICAgICAgcGFnZXMgPSBbZmlyc3RQYWdlVGV4dCwgcHJlUGFnZVRleHRdO1xuICAgICAgfSBlbHNlIGlmICh0b3RhbFBhZ2VzID4gMSAmJiBwYWdlcy5sZW5ndGggPT09IDApIHtcbiAgICAgICAgcGFnZXMgPSBbcHJlUGFnZVRleHRdO1xuICAgICAgfVxuXG4gICAgICBmb3IgKGxldCBpID0gc3RhcnRQYWdlOyBpIDw9IGVuZFBhZ2U7IGkgKz0gMSkge1xuICAgICAgICBpZiAoaSA+PSBwYWdlU3RhcnRJbmRleCkgcGFnZXMucHVzaChpKTtcbiAgICAgIH1cblxuICAgICAgaWYgKGFsd2F5c1Nob3dBbGxCdG5zIHx8IChlbmRQYWdlIDw9IGxhc3RQYWdlICYmIHBhZ2VzLmxlbmd0aCA+IDEpKSB7XG4gICAgICAgIHBhZ2VzLnB1c2gobmV4dFBhZ2VUZXh0KTtcbiAgICAgIH1cbiAgICAgIGlmICgoZW5kUGFnZSAhPT0gbGFzdFBhZ2UgJiYgd2l0aEZpcnN0QW5kTGFzdCkgfHwgKHdpdGhGaXJzdEFuZExhc3QgJiYgYWx3YXlzU2hvd0FsbEJ0bnMpKSB7XG4gICAgICAgIHBhZ2VzLnB1c2gobGFzdFBhZ2VUZXh0KTtcbiAgICAgIH1cblxuICAgICAgLy8gaWYgKChlbmRQYWdlIDw9IGxhc3RQYWdlICYmIHBhZ2VzLmxlbmd0aCA+IDEpIHx8IGFsd2F5c1Nob3dBbGxCdG5zKSB7XG4gICAgICAvLyAgIHBhZ2VzLnB1c2gobmV4dFBhZ2VUZXh0KTtcbiAgICAgIC8vIH1cbiAgICAgIC8vIGlmIChlbmRQYWdlICE9PSBsYXN0UGFnZSAmJiB3aXRoRmlyc3RBbmRMYXN0KSB7XG4gICAgICAvLyAgIHBhZ2VzLnB1c2gobGFzdFBhZ2VUZXh0KTtcbiAgICAgIC8vIH1cblxuICAgICAgcmV0dXJuIHBhZ2VzO1xuICAgIH1cblxuICAgIGNhbGN1bGF0ZVBhZ2VTdGF0dXMocGFnZXMgPSBbXSwgbGFzdFBhZ2UsIGRpc2FibGVQYWdlVGl0bGUgPSBmYWxzZSkge1xuICAgICAgY29uc3Qge1xuICAgICAgICBjdXJyUGFnZSxcbiAgICAgICAgcGFnZVN0YXJ0SW5kZXgsXG4gICAgICAgIGZpcnN0UGFnZVRleHQsXG4gICAgICAgIHByZVBhZ2VUZXh0LFxuICAgICAgICBuZXh0UGFnZVRleHQsXG4gICAgICAgIGxhc3RQYWdlVGV4dCxcbiAgICAgICAgYWx3YXlzU2hvd0FsbEJ0bnNcbiAgICAgIH0gPSB0aGlzLnByb3BzO1xuICAgICAgY29uc3QgaXNTdGFydCA9IHBhZ2UgPT5cbiAgICAgICAgKGN1cnJQYWdlID09PSBwYWdlU3RhcnRJbmRleCAmJiAocGFnZSA9PT0gZmlyc3RQYWdlVGV4dCB8fCBwYWdlID09PSBwcmVQYWdlVGV4dCkpO1xuICAgICAgY29uc3QgaXNFbmQgPSBwYWdlID0+XG4gICAgICAgIChjdXJyUGFnZSA9PT0gbGFzdFBhZ2UgJiYgKHBhZ2UgPT09IG5leHRQYWdlVGV4dCB8fCBwYWdlID09PSBsYXN0UGFnZVRleHQpKTtcblxuICAgICAgcmV0dXJuIHBhZ2VzXG4gICAgICAgIC5maWx0ZXIoKHBhZ2UpID0+IHtcbiAgICAgICAgICBpZiAoYWx3YXlzU2hvd0FsbEJ0bnMpIHtcbiAgICAgICAgICAgIHJldHVybiB0cnVlO1xuICAgICAgICAgIH1cbiAgICAgICAgICByZXR1cm4gIShpc1N0YXJ0KHBhZ2UpIHx8IGlzRW5kKHBhZ2UpKTtcbiAgICAgICAgfSlcbiAgICAgICAgLm1hcCgocGFnZSkgPT4ge1xuICAgICAgICAgIGxldCB0aXRsZTtcbiAgICAgICAgICBjb25zdCBhY3RpdmUgPSBwYWdlID09PSBjdXJyUGFnZTtcbiAgICAgICAgICBjb25zdCBkaXNhYmxlZCA9IChpc1N0YXJ0KHBhZ2UpIHx8IGlzRW5kKHBhZ2UpKTtcblxuICAgICAgICAgIGlmIChwYWdlID09PSBuZXh0UGFnZVRleHQpIHtcbiAgICAgICAgICAgIHRpdGxlID0gdGhpcy5wcm9wcy5uZXh0UGFnZVRpdGxlO1xuICAgICAgICAgIH0gZWxzZSBpZiAocGFnZSA9PT0gcHJlUGFnZVRleHQpIHtcbiAgICAgICAgICAgIHRpdGxlID0gdGhpcy5wcm9wcy5wcmVQYWdlVGl0bGU7XG4gICAgICAgICAgfSBlbHNlIGlmIChwYWdlID09PSBmaXJzdFBhZ2VUZXh0KSB7XG4gICAgICAgICAgICB0aXRsZSA9IHRoaXMucHJvcHMuZmlyc3RQYWdlVGl0bGU7XG4gICAgICAgICAgfSBlbHNlIGlmIChwYWdlID09PSBsYXN0UGFnZVRleHQpIHtcbiAgICAgICAgICAgIHRpdGxlID0gdGhpcy5wcm9wcy5sYXN0UGFnZVRpdGxlO1xuICAgICAgICAgIH0gZWxzZSB7XG4gICAgICAgICAgICB0aXRsZSA9IGAke3BhZ2V9YDtcbiAgICAgICAgICB9XG5cbiAgICAgICAgICBjb25zdCBwYWdlUmVzdWx0ID0geyBwYWdlLCBhY3RpdmUsIGRpc2FibGVkIH07XG4gICAgICAgICAgaWYgKCFkaXNhYmxlUGFnZVRpdGxlKSB7XG4gICAgICAgICAgICBwYWdlUmVzdWx0LnRpdGxlID0gdGl0bGU7XG4gICAgICAgICAgfVxuICAgICAgICAgIHJldHVybiBwYWdlUmVzdWx0O1xuICAgICAgICB9KTtcbiAgICB9XG5cbiAgICBjYWxjdWxhdGVTaXplUGVyUGFnZVN0YXR1cygpIHtcbiAgICAgIGNvbnN0IHsgc2l6ZVBlclBhZ2VMaXN0IH0gPSB0aGlzLnByb3BzO1xuICAgICAgcmV0dXJuIHNpemVQZXJQYWdlTGlzdC5tYXAoKF9zaXplUGVyUGFnZSkgPT4ge1xuICAgICAgICBjb25zdCBwYWdlVGV4dCA9IHR5cGVvZiBfc2l6ZVBlclBhZ2UudGV4dCAhPT0gJ3VuZGVmaW5lZCcgPyBfc2l6ZVBlclBhZ2UudGV4dCA6IF9zaXplUGVyUGFnZTtcbiAgICAgICAgY29uc3QgcGFnZU51bWJlciA9IHR5cGVvZiBfc2l6ZVBlclBhZ2UudmFsdWUgIT09ICd1bmRlZmluZWQnID8gX3NpemVQZXJQYWdlLnZhbHVlIDogX3NpemVQZXJQYWdlO1xuICAgICAgICByZXR1cm4ge1xuICAgICAgICAgIHRleHQ6IGAke3BhZ2VUZXh0fWAsXG4gICAgICAgICAgcGFnZTogcGFnZU51bWJlclxuICAgICAgICB9O1xuICAgICAgfSk7XG4gICAgfVxuICB9O1xuXG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvcGFnZS1yZXNvbHZlci5qcyIsIi8qIVxuICBDb3B5cmlnaHQgKGMpIDIwMTggSmVkIFdhdHNvbi5cbiAgTGljZW5zZWQgdW5kZXIgdGhlIE1JVCBMaWNlbnNlIChNSVQpLCBzZWVcbiAgaHR0cDovL2plZHdhdHNvbi5naXRodWIuaW8vY2xhc3NuYW1lc1xuKi9cbi8qIGdsb2JhbCBkZWZpbmUgKi9cblxuKGZ1bmN0aW9uICgpIHtcblx0J3VzZSBzdHJpY3QnO1xuXG5cdHZhciBoYXNPd24gPSB7fS5oYXNPd25Qcm9wZXJ0eTtcblxuXHRmdW5jdGlvbiBjbGFzc05hbWVzKCkge1xuXHRcdHZhciBjbGFzc2VzID0gW107XG5cblx0XHRmb3IgKHZhciBpID0gMDsgaSA8IGFyZ3VtZW50cy5sZW5ndGg7IGkrKykge1xuXHRcdFx0dmFyIGFyZyA9IGFyZ3VtZW50c1tpXTtcblx0XHRcdGlmICghYXJnKSBjb250aW51ZTtcblxuXHRcdFx0dmFyIGFyZ1R5cGUgPSB0eXBlb2YgYXJnO1xuXG5cdFx0XHRpZiAoYXJnVHlwZSA9PT0gJ3N0cmluZycgfHwgYXJnVHlwZSA9PT0gJ251bWJlcicpIHtcblx0XHRcdFx0Y2xhc3Nlcy5wdXNoKGFyZyk7XG5cdFx0XHR9IGVsc2UgaWYgKEFycmF5LmlzQXJyYXkoYXJnKSkge1xuXHRcdFx0XHRpZiAoYXJnLmxlbmd0aCkge1xuXHRcdFx0XHRcdHZhciBpbm5lciA9IGNsYXNzTmFtZXMuYXBwbHkobnVsbCwgYXJnKTtcblx0XHRcdFx0XHRpZiAoaW5uZXIpIHtcblx0XHRcdFx0XHRcdGNsYXNzZXMucHVzaChpbm5lcik7XG5cdFx0XHRcdFx0fVxuXHRcdFx0XHR9XG5cdFx0XHR9IGVsc2UgaWYgKGFyZ1R5cGUgPT09ICdvYmplY3QnKSB7XG5cdFx0XHRcdGlmIChhcmcudG9TdHJpbmcgPT09IE9iamVjdC5wcm90b3R5cGUudG9TdHJpbmcpIHtcblx0XHRcdFx0XHRmb3IgKHZhciBrZXkgaW4gYXJnKSB7XG5cdFx0XHRcdFx0XHRpZiAoaGFzT3duLmNhbGwoYXJnLCBrZXkpICYmIGFyZ1trZXldKSB7XG5cdFx0XHRcdFx0XHRcdGNsYXNzZXMucHVzaChrZXkpO1xuXHRcdFx0XHRcdFx0fVxuXHRcdFx0XHRcdH1cblx0XHRcdFx0fSBlbHNlIHtcblx0XHRcdFx0XHRjbGFzc2VzLnB1c2goYXJnLnRvU3RyaW5nKCkpO1xuXHRcdFx0XHR9XG5cdFx0XHR9XG5cdFx0fVxuXG5cdFx0cmV0dXJuIGNsYXNzZXMuam9pbignICcpO1xuXHR9XG5cblx0aWYgKHR5cGVvZiBtb2R1bGUgIT09ICd1bmRlZmluZWQnICYmIG1vZHVsZS5leHBvcnRzKSB7XG5cdFx0Y2xhc3NOYW1lcy5kZWZhdWx0ID0gY2xhc3NOYW1lcztcblx0XHRtb2R1bGUuZXhwb3J0cyA9IGNsYXNzTmFtZXM7XG5cdH0gZWxzZSBpZiAodHlwZW9mIGRlZmluZSA9PT0gJ2Z1bmN0aW9uJyAmJiB0eXBlb2YgZGVmaW5lLmFtZCA9PT0gJ29iamVjdCcgJiYgZGVmaW5lLmFtZCkge1xuXHRcdC8vIHJlZ2lzdGVyIGFzICdjbGFzc25hbWVzJywgY29uc2lzdGVudCB3aXRoIG5wbSBwYWNrYWdlIG5hbWVcblx0XHRkZWZpbmUoJ2NsYXNzbmFtZXMnLCBbXSwgZnVuY3Rpb24gKCkge1xuXHRcdFx0cmV0dXJuIGNsYXNzTmFtZXM7XG5cdFx0fSk7XG5cdH0gZWxzZSB7XG5cdFx0d2luZG93LmNsYXNzTmFtZXMgPSBjbGFzc05hbWVzO1xuXHR9XG59KCkpO1xuXG5cblxuLy8vLy8vLy8vLy8vLy8vLy8vXG4vLyBXRUJQQUNLIEZPT1RFUlxuLy8gLi9ub2RlX21vZHVsZXMvY2xhc3NuYW1lcy9pbmRleC5qc1xuLy8gbW9kdWxlIGlkID0gNFxuLy8gbW9kdWxlIGNodW5rcyA9IDAgMSIsIi8qIGVzbGludCByZWFjdC9wcm9wLXR5cGVzOiAwICovXG4vKiBlc2xpbnQgY2FtZWxjYXNlOiAwICovXG5pbXBvcnQgUmVhY3QsIHsgQ29tcG9uZW50IH0gZnJvbSAncmVhY3QnO1xuXG5pbXBvcnQgcGFnZVJlc29sdmVyIGZyb20gJy4vcGFnZS1yZXNvbHZlcic7XG5cbmV4cG9ydCBkZWZhdWx0IFdyYXBwZWRDb21wb25lbnQgPT5cbiAgY2xhc3MgUGFnaW5hdGlvbkhhbmRsZXIgZXh0ZW5kcyBwYWdlUmVzb2x2ZXIoQ29tcG9uZW50KSB7XG4gICAgY29uc3RydWN0b3IocHJvcHMpIHtcbiAgICAgIHN1cGVyKHByb3BzKTtcbiAgICAgIHRoaXMuaGFuZGxlQ2hhbmdlUGFnZSA9IHRoaXMuaGFuZGxlQ2hhbmdlUGFnZS5iaW5kKHRoaXMpO1xuICAgICAgdGhpcy5oYW5kbGVDaGFuZ2VTaXplUGVyUGFnZSA9IHRoaXMuaGFuZGxlQ2hhbmdlU2l6ZVBlclBhZ2UuYmluZCh0aGlzKTtcbiAgICAgIHRoaXMuc3RhdGUgPSB0aGlzLmluaXRpYWxTdGF0ZSgpO1xuICAgIH1cblxuICAgIFVOU0FGRV9jb21wb25lbnRXaWxsUmVjZWl2ZVByb3BzKG5leHRQcm9wcykge1xuICAgICAgY29uc3QgeyBkYXRhU2l6ZSwgY3VyclNpemVQZXJQYWdlIH0gPSBuZXh0UHJvcHM7XG4gICAgICBpZiAoY3VyclNpemVQZXJQYWdlICE9PSB0aGlzLnByb3BzLmN1cnJTaXplUGVyUGFnZSB8fCBkYXRhU2l6ZSAhPT0gdGhpcy5wcm9wcy5kYXRhU2l6ZSkge1xuICAgICAgICBjb25zdCB0b3RhbFBhZ2VzID0gdGhpcy5jYWxjdWxhdGVUb3RhbFBhZ2UoY3VyclNpemVQZXJQYWdlLCBkYXRhU2l6ZSk7XG4gICAgICAgIGNvbnN0IGxhc3RQYWdlID0gdGhpcy5jYWxjdWxhdGVMYXN0UGFnZSh0b3RhbFBhZ2VzKTtcbiAgICAgICAgdGhpcy5zZXRTdGF0ZSh7IHRvdGFsUGFnZXMsIGxhc3RQYWdlIH0pO1xuICAgICAgfVxuICAgIH1cblxuICAgIGhhbmRsZUNoYW5nZVNpemVQZXJQYWdlKHNpemVQZXJQYWdlKSB7XG4gICAgICBjb25zdCB7IGN1cnJTaXplUGVyUGFnZSwgb25TaXplUGVyUGFnZUNoYW5nZSB9ID0gdGhpcy5wcm9wcztcbiAgICAgIGNvbnN0IHNlbGVjdGVkU2l6ZSA9IHR5cGVvZiBzaXplUGVyUGFnZSA9PT0gJ3N0cmluZycgPyBwYXJzZUludChzaXplUGVyUGFnZSwgMTApIDogc2l6ZVBlclBhZ2U7XG4gICAgICBsZXQgeyBjdXJyUGFnZSB9ID0gdGhpcy5wcm9wcztcbiAgICAgIGlmIChzZWxlY3RlZFNpemUgIT09IGN1cnJTaXplUGVyUGFnZSkge1xuICAgICAgICBjb25zdCBuZXdUb3RhbFBhZ2VzID0gdGhpcy5jYWxjdWxhdGVUb3RhbFBhZ2Uoc2VsZWN0ZWRTaXplKTtcbiAgICAgICAgY29uc3QgbmV3TGFzdFBhZ2UgPSB0aGlzLmNhbGN1bGF0ZUxhc3RQYWdlKG5ld1RvdGFsUGFnZXMpO1xuICAgICAgICBpZiAoY3VyclBhZ2UgPiBuZXdMYXN0UGFnZSkgY3VyclBhZ2UgPSBuZXdMYXN0UGFnZTtcbiAgICAgICAgb25TaXplUGVyUGFnZUNoYW5nZShzZWxlY3RlZFNpemUsIGN1cnJQYWdlKTtcbiAgICAgIH1cbiAgICB9XG5cbiAgICBoYW5kbGVDaGFuZ2VQYWdlKG5ld1BhZ2UpIHtcbiAgICAgIGxldCBwYWdlO1xuICAgICAgY29uc3Qge1xuICAgICAgICBjdXJyUGFnZSxcbiAgICAgICAgcGFnZVN0YXJ0SW5kZXgsXG4gICAgICAgIHByZVBhZ2VUZXh0LFxuICAgICAgICBuZXh0UGFnZVRleHQsXG4gICAgICAgIGxhc3RQYWdlVGV4dCxcbiAgICAgICAgZmlyc3RQYWdlVGV4dCxcbiAgICAgICAgb25QYWdlQ2hhbmdlXG4gICAgICB9ID0gdGhpcy5wcm9wcztcbiAgICAgIGNvbnN0IHsgbGFzdFBhZ2UgfSA9IHRoaXMuc3RhdGU7XG5cbiAgICAgIGlmIChuZXdQYWdlID09PSBwcmVQYWdlVGV4dCkge1xuICAgICAgICBwYWdlID0gdGhpcy5iYWNrVG9QcmV2UGFnZSgpO1xuICAgICAgfSBlbHNlIGlmIChuZXdQYWdlID09PSBuZXh0UGFnZVRleHQpIHtcbiAgICAgICAgcGFnZSA9IChjdXJyUGFnZSArIDEpID4gbGFzdFBhZ2UgPyBsYXN0UGFnZSA6IGN1cnJQYWdlICsgMTtcbiAgICAgIH0gZWxzZSBpZiAobmV3UGFnZSA9PT0gbGFzdFBhZ2VUZXh0KSB7XG4gICAgICAgIHBhZ2UgPSBsYXN0UGFnZTtcbiAgICAgIH0gZWxzZSBpZiAobmV3UGFnZSA9PT0gZmlyc3RQYWdlVGV4dCkge1xuICAgICAgICBwYWdlID0gcGFnZVN0YXJ0SW5kZXg7XG4gICAgICB9IGVsc2Uge1xuICAgICAgICBwYWdlID0gcGFyc2VJbnQobmV3UGFnZSwgMTApO1xuICAgICAgfVxuICAgICAgaWYgKHBhZ2UgIT09IGN1cnJQYWdlKSB7XG4gICAgICAgIG9uUGFnZUNoYW5nZShwYWdlKTtcbiAgICAgIH1cbiAgICB9XG5cbiAgICByZW5kZXIoKSB7XG4gICAgICByZXR1cm4gKFxuICAgICAgICA8V3JhcHBlZENvbXBvbmVudFxuICAgICAgICAgIHsgLi4udGhpcy5wcm9wcyB9XG4gICAgICAgICAgbGFzdFBhZ2U9eyB0aGlzLnN0YXRlLmxhc3RQYWdlIH1cbiAgICAgICAgICB0b3RhbFBhZ2VzPXsgdGhpcy5zdGF0ZS50b3RhbFBhZ2VzIH1cbiAgICAgICAgICBvblBhZ2VDaGFuZ2U9eyB0aGlzLmhhbmRsZUNoYW5nZVBhZ2UgfVxuICAgICAgICAgIG9uU2l6ZVBlclBhZ2VDaGFuZ2U9eyB0aGlzLmhhbmRsZUNoYW5nZVNpemVQZXJQYWdlIH1cbiAgICAgICAgLz5cbiAgICAgICk7XG4gICAgfVxuICB9O1xuXG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvcGFnaW5hdGlvbi1oYW5kbGVyLmpzIiwiLyogZXNsaW50IHJlYWN0L3Byb3AtdHlwZXM6IDAgKi9cbmltcG9ydCBSZWFjdCBmcm9tICdyZWFjdCc7XG5cbmV4cG9ydCBkZWZhdWx0IFdyYXBwZWRDb21wb25lbnQgPT4gKHtcbiAgcGFnZSxcbiAgc2l6ZVBlclBhZ2UsXG4gIC4uLnJlc3Rcbn0pID0+IChcbiAgPFdyYXBwZWRDb21wb25lbnRcbiAgICB7IC4uLnJlc3QgfVxuICAgIGN1cnJQYWdlPXsgcGFnZSB9XG4gICAgY3VyclNpemVQZXJQYWdlPXsgc2l6ZVBlclBhZ2UgfVxuICAvPlxuKTtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyAuL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItcGFnaW5hdG9yL3NyYy9zdGFuZGFsb25lLWFkYXB0ZXIuanMiLCIvKiBlc2xpbnQgcmVhY3QvcHJvcC10eXBlczogMCAqL1xuLyogZXNsaW50IHJlYWN0L3JlcXVpcmUtZGVmYXVsdC1wcm9wczogMCAqL1xuLyogZXNsaW50IG5vLWxvbmVseS1pZjogMCAqL1xuLyogZXNsaW50IGNhbWVsY2FzZTogMCAqL1xuaW1wb3J0IFJlYWN0IGZyb20gJ3JlYWN0JztcbmltcG9ydCBFdmVudEVtaXR0ZXIgZnJvbSAnZXZlbnRzJztcbmltcG9ydCBDb25zdCBmcm9tICcuL2NvbnN0JztcbmltcG9ydCB7IGFsaWduUGFnZSB9IGZyb20gJy4vcGFnZSc7XG5cbmNvbnN0IFN0YXRlQ29udGV4dCA9IFJlYWN0LmNyZWF0ZUNvbnRleHQoKTtcblxuY2xhc3MgU3RhdGVQcm92aWRlciBleHRlbmRzIFJlYWN0LkNvbXBvbmVudCB7XG4gIGNvbnN0cnVjdG9yKHByb3BzKSB7XG4gICAgc3VwZXIocHJvcHMpO1xuICAgIHRoaXMuaGFuZGxlQ2hhbmdlUGFnZSA9IHRoaXMuaGFuZGxlQ2hhbmdlUGFnZS5iaW5kKHRoaXMpO1xuICAgIHRoaXMuaGFuZGxlRGF0YVNpemVDaGFuZ2UgPSB0aGlzLmhhbmRsZURhdGFTaXplQ2hhbmdlLmJpbmQodGhpcyk7XG4gICAgdGhpcy5oYW5kbGVDaGFuZ2VTaXplUGVyUGFnZSA9IHRoaXMuaGFuZGxlQ2hhbmdlU2l6ZVBlclBhZ2UuYmluZCh0aGlzKTtcblxuICAgIGxldCBjdXJyUGFnZTtcbiAgICBsZXQgY3VyclNpemVQZXJQYWdlO1xuICAgIGNvbnN0IHsgb3B0aW9ucyB9ID0gcHJvcHMucGFnaW5hdGlvbjtcbiAgICBjb25zdCBzaXplUGVyUGFnZUxpc3QgPSBvcHRpb25zLnNpemVQZXJQYWdlTGlzdCB8fCBDb25zdC5TSVpFX1BFUl9QQUdFX0xJU1Q7XG5cbiAgICAvLyBpbml0aWFsaXplIGN1cnJlbnQgcGFnZVxuICAgIGlmICh0eXBlb2Ygb3B0aW9ucy5wYWdlICE9PSAndW5kZWZpbmVkJykge1xuICAgICAgY3VyclBhZ2UgPSBvcHRpb25zLnBhZ2U7XG4gICAgfSBlbHNlIGlmICh0eXBlb2Ygb3B0aW9ucy5wYWdlU3RhcnRJbmRleCAhPT0gJ3VuZGVmaW5lZCcpIHtcbiAgICAgIGN1cnJQYWdlID0gb3B0aW9ucy5wYWdlU3RhcnRJbmRleDtcbiAgICB9IGVsc2Uge1xuICAgICAgY3VyclBhZ2UgPSBDb25zdC5QQUdFX1NUQVJUX0lOREVYO1xuICAgIH1cblxuICAgIC8vIGluaXRpYWxpemUgY3VycmVudCBzaXplUGVyUGFnZVxuICAgIGlmICh0eXBlb2Ygb3B0aW9ucy5zaXplUGVyUGFnZSAhPT0gJ3VuZGVmaW5lZCcpIHtcbiAgICAgIGN1cnJTaXplUGVyUGFnZSA9IG9wdGlvbnMuc2l6ZVBlclBhZ2U7XG4gICAgfSBlbHNlIGlmICh0eXBlb2Ygc2l6ZVBlclBhZ2VMaXN0WzBdID09PSAnb2JqZWN0Jykge1xuICAgICAgY3VyclNpemVQZXJQYWdlID0gc2l6ZVBlclBhZ2VMaXN0WzBdLnZhbHVlO1xuICAgIH0gZWxzZSB7XG4gICAgICBjdXJyU2l6ZVBlclBhZ2UgPSBzaXplUGVyUGFnZUxpc3RbMF07XG4gICAgfVxuXG4gICAgdGhpcy5jdXJyUGFnZSA9IGN1cnJQYWdlO1xuICAgIHRoaXMuZGF0YVNpemUgPSBvcHRpb25zLnRvdGFsU2l6ZTtcbiAgICB0aGlzLmN1cnJTaXplUGVyUGFnZSA9IGN1cnJTaXplUGVyUGFnZTtcbiAgICB0aGlzLmRhdGFDaGFuZ2VMaXN0ZW5lciA9IG5ldyBFdmVudEVtaXR0ZXIoKTtcbiAgICB0aGlzLmRhdGFDaGFuZ2VMaXN0ZW5lci5vbignZmlsdGVyQ2hhbmdlZCcsIHRoaXMuaGFuZGxlRGF0YVNpemVDaGFuZ2UpO1xuICB9XG5cbiAgZ2V0UGFnaW5hdGlvblByb3BzID0gKCkgPT4ge1xuICAgIGNvbnN0IHsgcGFnaW5hdGlvbjogeyBvcHRpb25zIH0sIGJvb3RzdHJhcDQsIHRhYmxlSWQgfSA9IHRoaXMucHJvcHM7XG4gICAgY29uc3QgeyBjdXJyUGFnZSwgY3VyclNpemVQZXJQYWdlLCBkYXRhU2l6ZSB9ID0gdGhpcztcbiAgICBjb25zdCB3aXRoRmlyc3RBbmRMYXN0ID0gdHlwZW9mIG9wdGlvbnMud2l0aEZpcnN0QW5kTGFzdCA9PT0gJ3VuZGVmaW5lZCcgP1xuICAgICAgQ29uc3QuV2l0aF9GSVJTVF9BTkRfTEFTVCA6IG9wdGlvbnMud2l0aEZpcnN0QW5kTGFzdDtcbiAgICBjb25zdCBhbHdheXNTaG93QWxsQnRucyA9IHR5cGVvZiBvcHRpb25zLmFsd2F5c1Nob3dBbGxCdG5zID09PSAndW5kZWZpbmVkJyA/XG4gICAgICBDb25zdC5TSE9XX0FMTF9QQUdFX0JUTlMgOiBvcHRpb25zLmFsd2F5c1Nob3dBbGxCdG5zO1xuICAgIGNvbnN0IGhpZGVTaXplUGVyUGFnZSA9IHR5cGVvZiBvcHRpb25zLmhpZGVTaXplUGVyUGFnZSA9PT0gJ3VuZGVmaW5lZCcgP1xuICAgICAgQ29uc3QuSElERV9TSVpFX1BFUl9QQUdFIDogb3B0aW9ucy5oaWRlU2l6ZVBlclBhZ2U7XG4gICAgY29uc3QgaGlkZVBhZ2VMaXN0T25seU9uZVBhZ2UgPSB0eXBlb2Ygb3B0aW9ucy5oaWRlUGFnZUxpc3RPbmx5T25lUGFnZSA9PT0gJ3VuZGVmaW5lZCcgP1xuICAgICAgQ29uc3QuSElERV9QQUdFX0xJU1RfT05MWV9PTkVfUEFHRSA6IG9wdGlvbnMuaGlkZVBhZ2VMaXN0T25seU9uZVBhZ2U7XG4gICAgY29uc3QgcGFnZVN0YXJ0SW5kZXggPSB0eXBlb2Ygb3B0aW9ucy5wYWdlU3RhcnRJbmRleCA9PT0gJ3VuZGVmaW5lZCcgP1xuICAgICAgQ29uc3QuUEFHRV9TVEFSVF9JTkRFWCA6IG9wdGlvbnMucGFnZVN0YXJ0SW5kZXg7XG4gICAgcmV0dXJuIHtcbiAgICAgIC4uLm9wdGlvbnMsXG4gICAgICBib290c3RyYXA0LFxuICAgICAgdGFibGVJZCxcbiAgICAgIHBhZ2U6IGN1cnJQYWdlLFxuICAgICAgc2l6ZVBlclBhZ2U6IGN1cnJTaXplUGVyUGFnZSxcbiAgICAgIHBhZ2VTdGFydEluZGV4LFxuICAgICAgaGlkZVBhZ2VMaXN0T25seU9uZVBhZ2UsXG4gICAgICBoaWRlU2l6ZVBlclBhZ2UsXG4gICAgICBhbHdheXNTaG93QWxsQnRucyxcbiAgICAgIHdpdGhGaXJzdEFuZExhc3QsXG4gICAgICBkYXRhU2l6ZSxcbiAgICAgIHNpemVQZXJQYWdlTGlzdDogb3B0aW9ucy5zaXplUGVyUGFnZUxpc3QgfHwgQ29uc3QuU0laRV9QRVJfUEFHRV9MSVNULFxuICAgICAgcGFnaW5hdGlvblNpemU6IG9wdGlvbnMucGFnaW5hdGlvblNpemUgfHwgQ29uc3QuUEFHSU5BVElPTl9TSVpFLFxuICAgICAgc2hvd1RvdGFsOiBvcHRpb25zLnNob3dUb3RhbCxcbiAgICAgIHBhZ2VMaXN0UmVuZGVyZXI6IG9wdGlvbnMucGFnZUxpc3RSZW5kZXJlcixcbiAgICAgIHBhZ2VCdXR0b25SZW5kZXJlcjogb3B0aW9ucy5wYWdlQnV0dG9uUmVuZGVyZXIsXG4gICAgICBzaXplUGVyUGFnZVJlbmRlcmVyOiBvcHRpb25zLnNpemVQZXJQYWdlUmVuZGVyZXIsXG4gICAgICBwYWdpbmF0aW9uVG90YWxSZW5kZXJlcjogb3B0aW9ucy5wYWdpbmF0aW9uVG90YWxSZW5kZXJlcixcbiAgICAgIHNpemVQZXJQYWdlT3B0aW9uUmVuZGVyZXI6IG9wdGlvbnMuc2l6ZVBlclBhZ2VPcHRpb25SZW5kZXJlcixcbiAgICAgIGZpcnN0UGFnZVRleHQ6IG9wdGlvbnMuZmlyc3RQYWdlVGV4dCB8fCBDb25zdC5GSVJTVF9QQUdFX1RFWFQsXG4gICAgICBwcmVQYWdlVGV4dDogb3B0aW9ucy5wcmVQYWdlVGV4dCB8fCBDb25zdC5QUkVfUEFHRV9URVhULFxuICAgICAgbmV4dFBhZ2VUZXh0OiBvcHRpb25zLm5leHRQYWdlVGV4dCB8fCBDb25zdC5ORVhUX1BBR0VfVEVYVCxcbiAgICAgIGxhc3RQYWdlVGV4dDogb3B0aW9ucy5sYXN0UGFnZVRleHQgfHwgQ29uc3QuTEFTVF9QQUdFX1RFWFQsXG4gICAgICBwcmVQYWdlVGl0bGU6IG9wdGlvbnMucHJlUGFnZVRpdGxlIHx8IENvbnN0LlBSRV9QQUdFX1RJVExFLFxuICAgICAgbmV4dFBhZ2VUaXRsZTogb3B0aW9ucy5uZXh0UGFnZVRpdGxlIHx8IENvbnN0Lk5FWFRfUEFHRV9USVRMRSxcbiAgICAgIGZpcnN0UGFnZVRpdGxlOiBvcHRpb25zLmZpcnN0UGFnZVRpdGxlIHx8IENvbnN0LkZJUlNUX1BBR0VfVElUTEUsXG4gICAgICBsYXN0UGFnZVRpdGxlOiBvcHRpb25zLmxhc3RQYWdlVGl0bGUgfHwgQ29uc3QuTEFTVF9QQUdFX1RJVExFLFxuICAgICAgb25QYWdlQ2hhbmdlOiB0aGlzLmhhbmRsZUNoYW5nZVBhZ2UsXG4gICAgICBvblNpemVQZXJQYWdlQ2hhbmdlOiB0aGlzLmhhbmRsZUNoYW5nZVNpemVQZXJQYWdlXG4gICAgfTtcbiAgfVxuXG4gIHNldFBhZ2luYXRpb25SZW1vdGVFbWl0dGVyID0gKHJlbW90ZUVtaXR0ZXIpID0+IHtcbiAgICB0aGlzLnJlbW90ZUVtaXR0ZXIgPSByZW1vdGVFbWl0dGVyO1xuICB9XG5cbiAgZ2V0UGFnaW5hdGlvblJlbW90ZUVtaXR0ZXIgPSAoKSA9PiB0aGlzLnJlbW90ZUVtaXR0ZXIgfHwgdGhpcy5wcm9wcy5yZW1vdGVFbWl0dGVyO1xuXG4gIFVOU0FGRV9jb21wb25lbnRXaWxsUmVjZWl2ZVByb3BzKG5leHRQcm9wcykge1xuICAgIGNvbnN0IHsgY3VzdG9tIH0gPSBuZXh0UHJvcHMucGFnaW5hdGlvbi5vcHRpb25zO1xuXG4gICAgLy8gdXNlciBzaG91bGQgYWxpZ24gdGhlIHBhZ2Ugd2hlbiB0aGUgcGFnZSBpcyBub3QgZml0IHRvIHRoZSBkYXRhIHNpemUgd2hlbiByZW1vdGUgZW5hYmxlXG4gICAgaWYgKHRoaXMuaXNSZW1vdGVQYWdpbmF0aW9uKCkgfHwgY3VzdG9tKSB7XG4gICAgICBpZiAodHlwZW9mIG5leHRQcm9wcy5wYWdpbmF0aW9uLm9wdGlvbnMucGFnZSAhPT0gJ3VuZGVmaW5lZCcpIHtcbiAgICAgICAgdGhpcy5jdXJyUGFnZSA9IG5leHRQcm9wcy5wYWdpbmF0aW9uLm9wdGlvbnMucGFnZTtcbiAgICAgIH1cbiAgICAgIGlmICh0eXBlb2YgbmV4dFByb3BzLnBhZ2luYXRpb24ub3B0aW9ucy5zaXplUGVyUGFnZSAhPT0gJ3VuZGVmaW5lZCcpIHtcbiAgICAgICAgdGhpcy5jdXJyU2l6ZVBlclBhZ2UgPSBuZXh0UHJvcHMucGFnaW5hdGlvbi5vcHRpb25zLnNpemVQZXJQYWdlO1xuICAgICAgfVxuICAgICAgaWYgKHR5cGVvZiBuZXh0UHJvcHMucGFnaW5hdGlvbi5vcHRpb25zLnRvdGFsU2l6ZSAhPT0gJ3VuZGVmaW5lZCcpIHtcbiAgICAgICAgdGhpcy5kYXRhU2l6ZSA9IG5leHRQcm9wcy5wYWdpbmF0aW9uLm9wdGlvbnMudG90YWxTaXplO1xuICAgICAgfVxuICAgIH1cbiAgfVxuXG4gIGlzUmVtb3RlUGFnaW5hdGlvbiA9ICgpID0+IHtcbiAgICBjb25zdCBlID0ge307XG4gICAgdGhpcy5yZW1vdGVFbWl0dGVyLmVtaXQoJ2lzUmVtb3RlUGFnaW5hdGlvbicsIGUpO1xuICAgIHJldHVybiBlLnJlc3VsdDtcbiAgfTtcblxuICBoYW5kbGVEYXRhU2l6ZUNoYW5nZShuZXdEYXRhU2l6ZSkge1xuICAgIGNvbnN0IHsgcGFnaW5hdGlvbjogeyBvcHRpb25zIH0gfSA9IHRoaXMucHJvcHM7XG4gICAgY29uc3QgcGFnZVN0YXJ0SW5kZXggPSB0eXBlb2Ygb3B0aW9ucy5wYWdlU3RhcnRJbmRleCA9PT0gJ3VuZGVmaW5lZCcgP1xuICAgICAgQ29uc3QuUEFHRV9TVEFSVF9JTkRFWCA6IG9wdGlvbnMucGFnZVN0YXJ0SW5kZXg7XG4gICAgdGhpcy5jdXJyUGFnZSA9IGFsaWduUGFnZShcbiAgICAgIG5ld0RhdGFTaXplLFxuICAgICAgdGhpcy5kYXRhU2l6ZSxcbiAgICAgIHRoaXMuY3VyclBhZ2UsXG4gICAgICB0aGlzLmN1cnJTaXplUGVyUGFnZSxcbiAgICAgIHBhZ2VTdGFydEluZGV4XG4gICAgKTtcbiAgICB0aGlzLmRhdGFTaXplID0gbmV3RGF0YVNpemU7XG4gICAgdGhpcy5mb3JjZVVwZGF0ZSgpO1xuICB9XG5cbiAgaGFuZGxlQ2hhbmdlUGFnZShjdXJyUGFnZSkge1xuICAgIGNvbnN0IHsgY3VyclNpemVQZXJQYWdlIH0gPSB0aGlzO1xuICAgIGNvbnN0IHsgcGFnaW5hdGlvbjogeyBvcHRpb25zIH0gfSA9IHRoaXMucHJvcHM7XG5cbiAgICBpZiAob3B0aW9ucy5vblBhZ2VDaGFuZ2UpIHtcbiAgICAgIG9wdGlvbnMub25QYWdlQ2hhbmdlKGN1cnJQYWdlLCBjdXJyU2l6ZVBlclBhZ2UpO1xuICAgIH1cblxuICAgIHRoaXMuY3VyclBhZ2UgPSBjdXJyUGFnZTtcblxuICAgIGlmICh0aGlzLmlzUmVtb3RlUGFnaW5hdGlvbigpKSB7XG4gICAgICB0aGlzLmdldFBhZ2luYXRpb25SZW1vdGVFbWl0dGVyKCkuZW1pdCgncGFnaW5hdGlvbkNoYW5nZScsIGN1cnJQYWdlLCBjdXJyU2l6ZVBlclBhZ2UpO1xuICAgICAgcmV0dXJuO1xuICAgIH1cbiAgICB0aGlzLmZvcmNlVXBkYXRlKCk7XG4gIH1cblxuICBoYW5kbGVDaGFuZ2VTaXplUGVyUGFnZShjdXJyU2l6ZVBlclBhZ2UsIGN1cnJQYWdlKSB7XG4gICAgY29uc3QgeyBwYWdpbmF0aW9uOiB7IG9wdGlvbnMgfSB9ID0gdGhpcy5wcm9wcztcblxuICAgIGlmIChvcHRpb25zLm9uU2l6ZVBlclBhZ2VDaGFuZ2UpIHtcbiAgICAgIG9wdGlvbnMub25TaXplUGVyUGFnZUNoYW5nZShjdXJyU2l6ZVBlclBhZ2UsIGN1cnJQYWdlKTtcbiAgICB9XG5cbiAgICB0aGlzLmN1cnJQYWdlID0gY3VyclBhZ2U7XG4gICAgdGhpcy5jdXJyU2l6ZVBlclBhZ2UgPSBjdXJyU2l6ZVBlclBhZ2U7XG5cbiAgICBpZiAodGhpcy5pc1JlbW90ZVBhZ2luYXRpb24oKSkge1xuICAgICAgdGhpcy5nZXRQYWdpbmF0aW9uUmVtb3RlRW1pdHRlcigpLmVtaXQoJ3BhZ2luYXRpb25DaGFuZ2UnLCBjdXJyUGFnZSwgY3VyclNpemVQZXJQYWdlKTtcbiAgICAgIHJldHVybjtcbiAgICB9XG4gICAgdGhpcy5mb3JjZVVwZGF0ZSgpO1xuICB9XG5cbiAgcmVuZGVyKCkge1xuICAgIGNvbnN0IHBhZ2luYXRpb25Qcm9wcyA9IHRoaXMuZ2V0UGFnaW5hdGlvblByb3BzKCk7XG4gICAgY29uc3QgcGFnaW5hdGlvbiA9IHtcbiAgICAgIC4uLnRoaXMucHJvcHMucGFnaW5hdGlvbixcbiAgICAgIG9wdGlvbnM6IHBhZ2luYXRpb25Qcm9wc1xuICAgIH07XG5cbiAgICByZXR1cm4gKFxuICAgICAgPFN0YXRlQ29udGV4dC5Qcm92aWRlclxuICAgICAgICB2YWx1ZT17IHtcbiAgICAgICAgICBwYWdpbmF0aW9uUHJvcHMsXG4gICAgICAgICAgcGFnaW5hdGlvblRhYmxlUHJvcHM6IHtcbiAgICAgICAgICAgIHBhZ2luYXRpb24sXG4gICAgICAgICAgICBzZXRQYWdpbmF0aW9uUmVtb3RlRW1pdHRlcjogdGhpcy5zZXRQYWdpbmF0aW9uUmVtb3RlRW1pdHRlcixcbiAgICAgICAgICAgIGRhdGFDaGFuZ2VMaXN0ZW5lcjogdGhpcy5kYXRhQ2hhbmdlTGlzdGVuZXJcbiAgICAgICAgICB9XG4gICAgICAgIH0gfVxuICAgICAgPlxuICAgICAgICB7IHRoaXMucHJvcHMuY2hpbGRyZW4gfVxuICAgICAgPC9TdGF0ZUNvbnRleHQuUHJvdmlkZXI+XG4gICAgKTtcbiAgfVxufVxuXG5leHBvcnQgZGVmYXVsdCAoKSA9PiAoe1xuICBQcm92aWRlcjogU3RhdGVQcm92aWRlcixcbiAgQ29uc3VtZXI6IFN0YXRlQ29udGV4dC5Db25zdW1lclxufSk7XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvc3RhdGUtY29udGV4dC5qcyIsImltcG9ydCBDb25zdCBmcm9tICcuL2NvbnN0JztcblxuY29uc3QgZ2V0Tm9ybWFsaXplZFBhZ2UgPSAoXG4gIHBhZ2UsXG4gIHBhZ2VTdGFydEluZGV4XG4pID0+IHtcbiAgY29uc3Qgb2Zmc2V0ID0gTWF0aC5hYnMoMSAtIHBhZ2VTdGFydEluZGV4KTtcbiAgcmV0dXJuIHBhZ2UgKyBvZmZzZXQ7XG59O1xuXG5jb25zdCBlbmRJbmRleCA9IChcbiAgcGFnZSxcbiAgc2l6ZVBlclBhZ2UsXG4gIHBhZ2VTdGFydEluZGV4XG4pID0+IChnZXROb3JtYWxpemVkUGFnZShwYWdlLCBwYWdlU3RhcnRJbmRleCkgKiBzaXplUGVyUGFnZSkgLSAxO1xuXG5jb25zdCBzdGFydEluZGV4ID0gKFxuICBlbmQsXG4gIHNpemVQZXJQYWdlLFxuKSA9PiBlbmQgLSAoc2l6ZVBlclBhZ2UgLSAxKTtcblxuZXhwb3J0IGNvbnN0IGFsaWduUGFnZSA9IChcbiAgZGF0YVNpemUsXG4gIHByZXZEYXRhU2l6ZSxcbiAgcGFnZSxcbiAgc2l6ZVBlclBhZ2UsXG4gIHBhZ2VTdGFydEluZGV4XG4pID0+IHtcbiAgaWYgKHByZXZEYXRhU2l6ZSA8IGRhdGFTaXplKSByZXR1cm4gcGFnZTtcbiAgaWYgKHBhZ2UgPCBwYWdlU3RhcnRJbmRleCkgcmV0dXJuIHBhZ2VTdGFydEluZGV4O1xuICBpZiAoZGF0YVNpemUgPD0gMCkgcmV0dXJuIHBhZ2VTdGFydEluZGV4O1xuICBpZiAoKHBhZ2UgPj0gKE1hdGguZmxvb3IoZGF0YVNpemUgLyBzaXplUGVyUGFnZSkgKyBwYWdlU3RhcnRJbmRleCkpICYmIHBhZ2VTdGFydEluZGV4ID09PSAxKSB7XG4gICAgcmV0dXJuIE1hdGguY2VpbChkYXRhU2l6ZSAvIHNpemVQZXJQYWdlKTtcbiAgfVxuICBpZiAocGFnZSA+PSBNYXRoLmZsb29yKGRhdGFTaXplIC8gc2l6ZVBlclBhZ2UpICYmIHBhZ2VTdGFydEluZGV4ID09PSAwKSB7XG4gICAgY29uc3QgbmV3UGFnZSA9IE1hdGguY2VpbChkYXRhU2l6ZSAvIHNpemVQZXJQYWdlKTtcbiAgICByZXR1cm4gbmV3UGFnZSAtIE1hdGguYWJzKChDb25zdC5QQUdFX1NUQVJUX0lOREVYIC0gcGFnZVN0YXJ0SW5kZXgpKTtcbiAgfVxuICByZXR1cm4gcGFnZTtcbn07XG5cbmV4cG9ydCBjb25zdCBnZXRCeUN1cnJQYWdlID0gKFxuICBkYXRhLFxuICBwYWdlLFxuICBzaXplUGVyUGFnZSxcbiAgcGFnZVN0YXJ0SW5kZXhcbikgPT4ge1xuICBjb25zdCBkYXRhU2l6ZSA9IGRhdGEubGVuZ3RoO1xuICBpZiAoIWRhdGFTaXplKSByZXR1cm4gW107XG5cbiAgY29uc3QgZW5kID0gZW5kSW5kZXgocGFnZSwgc2l6ZVBlclBhZ2UsIHBhZ2VTdGFydEluZGV4KTtcbiAgY29uc3Qgc3RhcnQgPSBzdGFydEluZGV4KGVuZCwgc2l6ZVBlclBhZ2UpO1xuXG4gIGNvbnN0IHJlc3VsdCA9IFtdO1xuICBmb3IgKGxldCBpID0gc3RhcnQ7IGkgPD0gZW5kOyBpICs9IDEpIHtcbiAgICByZXN1bHQucHVzaChkYXRhW2ldKTtcbiAgICBpZiAoaSArIDEgPT09IGRhdGFTaXplKSBicmVhaztcbiAgfVxuICByZXR1cm4gcmVzdWx0O1xufTtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyAuL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItcGFnaW5hdG9yL3NyYy9wYWdlLmpzIiwiLyogZXNsaW50IHJlYWN0L3Byb3AtdHlwZXM6IDAgKi9cbmltcG9ydCBSZWFjdCwgeyBDb21wb25lbnQgfSBmcm9tICdyZWFjdCc7XG5cbmltcG9ydCBwYWdlUmVzb2x2ZXIgZnJvbSAnLi9wYWdlLXJlc29sdmVyJztcbmltcG9ydCBTaXplUGVyUGFnZURyb3BEb3duIGZyb20gJy4vc2l6ZS1wZXItcGFnZS1kcm9wZG93bic7XG5cbmNvbnN0IHNpemVQZXJQYWdlRHJvcGRvd25BZGFwdGVyID0gV3JhcHBlZENvbXBvbmVudCA9PlxuICBjbGFzcyBTaXplUGVyUGFnZURyb3Bkb3duQWRhcHRlciBleHRlbmRzIHBhZ2VSZXNvbHZlcihDb21wb25lbnQpIHtcbiAgICBjb25zdHJ1Y3Rvcihwcm9wcykge1xuICAgICAgc3VwZXIocHJvcHMpO1xuICAgICAgdGhpcy5jbG9zZURyb3BEb3duID0gdGhpcy5jbG9zZURyb3BEb3duLmJpbmQodGhpcyk7XG4gICAgICB0aGlzLnRvZ2dsZURyb3BEb3duID0gdGhpcy50b2dnbGVEcm9wRG93bi5iaW5kKHRoaXMpO1xuICAgICAgdGhpcy5oYW5kbGVDaGFuZ2VTaXplUGVyUGFnZSA9IHRoaXMuaGFuZGxlQ2hhbmdlU2l6ZVBlclBhZ2UuYmluZCh0aGlzKTtcbiAgICAgIHRoaXMuc3RhdGUgPSB7IGRyb3Bkb3duT3BlbjogZmFsc2UgfTtcbiAgICB9XG5cbiAgICB0b2dnbGVEcm9wRG93bigpIHtcbiAgICAgIGNvbnN0IGRyb3Bkb3duT3BlbiA9ICF0aGlzLnN0YXRlLmRyb3Bkb3duT3BlbjtcbiAgICAgIHRoaXMuc2V0U3RhdGUoKCkgPT4gKHsgZHJvcGRvd25PcGVuIH0pKTtcbiAgICB9XG5cbiAgICBjbG9zZURyb3BEb3duKCkge1xuICAgICAgdGhpcy5zZXRTdGF0ZSgoKSA9PiAoeyBkcm9wZG93bk9wZW46IGZhbHNlIH0pKTtcbiAgICB9XG5cbiAgICBoYW5kbGVDaGFuZ2VTaXplUGVyUGFnZShzaXplUGVyUGFnZSkge1xuICAgICAgdGhpcy5wcm9wcy5vblNpemVQZXJQYWdlQ2hhbmdlKHNpemVQZXJQYWdlKTtcbiAgICAgIHRoaXMuY2xvc2VEcm9wRG93bigpO1xuICAgIH1cblxuICAgIHJlbmRlcigpIHtcbiAgICAgIGNvbnN0IHtcbiAgICAgICAgdGFibGVJZCxcbiAgICAgICAgYm9vdHN0cmFwNCxcbiAgICAgICAgc2l6ZVBlclBhZ2VMaXN0LFxuICAgICAgICBjdXJyU2l6ZVBlclBhZ2UsXG4gICAgICAgIGhpZGVTaXplUGVyUGFnZSxcbiAgICAgICAgc2l6ZVBlclBhZ2VSZW5kZXJlcixcbiAgICAgICAgc2l6ZVBlclBhZ2VPcHRpb25SZW5kZXJlclxuICAgICAgfSA9IHRoaXMucHJvcHM7XG4gICAgICBjb25zdCB7IGRyb3Bkb3duT3Blbjogb3BlbiB9ID0gdGhpcy5zdGF0ZTtcblxuICAgICAgaWYgKHNpemVQZXJQYWdlTGlzdC5sZW5ndGggPiAxICYmICFoaWRlU2l6ZVBlclBhZ2UpIHtcbiAgICAgICAgaWYgKHNpemVQZXJQYWdlUmVuZGVyZXIpIHtcbiAgICAgICAgICByZXR1cm4gc2l6ZVBlclBhZ2VSZW5kZXJlcih7XG4gICAgICAgICAgICBvcHRpb25zOiB0aGlzLmNhbGN1bGF0ZVNpemVQZXJQYWdlU3RhdHVzKCksXG4gICAgICAgICAgICBjdXJyU2l6ZVBlclBhZ2U6IGAke2N1cnJTaXplUGVyUGFnZX1gLFxuICAgICAgICAgICAgb25TaXplUGVyUGFnZUNoYW5nZTogdGhpcy5oYW5kbGVDaGFuZ2VTaXplUGVyUGFnZVxuICAgICAgICAgIH0pO1xuICAgICAgICB9XG4gICAgICAgIHJldHVybiAoXG4gICAgICAgICAgPFdyYXBwZWRDb21wb25lbnRcbiAgICAgICAgICAgIHsgLi4udGhpcy5wcm9wcyB9XG4gICAgICAgICAgICBjdXJyU2l6ZVBlclBhZ2U9eyBgJHtjdXJyU2l6ZVBlclBhZ2V9YCB9XG4gICAgICAgICAgICBvcHRpb25zPXsgdGhpcy5jYWxjdWxhdGVTaXplUGVyUGFnZVN0YXR1cygpIH1cbiAgICAgICAgICAgIG9wdGlvblJlbmRlcmVyPXsgc2l6ZVBlclBhZ2VPcHRpb25SZW5kZXJlciB9XG4gICAgICAgICAgICBvblNpemVQZXJQYWdlQ2hhbmdlPXsgdGhpcy5oYW5kbGVDaGFuZ2VTaXplUGVyUGFnZSB9XG4gICAgICAgICAgICBvbkNsaWNrPXsgdGhpcy50b2dnbGVEcm9wRG93biB9XG4gICAgICAgICAgICBvbkJsdXI9eyB0aGlzLmNsb3NlRHJvcERvd24gfVxuICAgICAgICAgICAgb3Blbj17IG9wZW4gfVxuICAgICAgICAgICAgdGFibGVJZD17IHRhYmxlSWQgfVxuICAgICAgICAgICAgYm9vdHN0cmFwND17IGJvb3RzdHJhcDQgfVxuICAgICAgICAgIC8+XG4gICAgICAgICk7XG4gICAgICB9XG4gICAgICByZXR1cm4gbnVsbDtcbiAgICB9XG4gIH07XG5cblxuZXhwb3J0IGNvbnN0IFNpemVQZXJQYWdlRHJvcGRvd25XaXRoQWRhcHRlciA9IHNpemVQZXJQYWdlRHJvcGRvd25BZGFwdGVyKFNpemVQZXJQYWdlRHJvcERvd24pO1xuZXhwb3J0IGRlZmF1bHQgc2l6ZVBlclBhZ2VEcm9wZG93bkFkYXB0ZXI7XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvc2l6ZS1wZXItcGFnZS1kcm9wZG93bi1hZGFwdGVyLmpzIiwiaW1wb3J0IFJlYWN0IGZyb20gJ3JlYWN0JztcbmltcG9ydCBjcyBmcm9tICdjbGFzc25hbWVzJztcbmltcG9ydCBQcm9wVHlwZXMgZnJvbSAncHJvcC10eXBlcyc7XG5pbXBvcnQgU2l6ZVBlclBhZ2VPcHRpb24gZnJvbSAnLi9zaXplLXBlci1wYWdlLW9wdGlvbic7XG5cbmNvbnN0IHNpemVQZXJQYWdlRGVmYXVsdENsYXNzID0gJ3JlYWN0LWJzLXRhYmxlLXNpemVQZXJQYWdlLWRyb3Bkb3duJztcblxuY29uc3QgU2l6ZVBlclBhZ2VEcm9wRG93biA9IChwcm9wcykgPT4ge1xuICBjb25zdCB7XG4gICAgb3BlbixcbiAgICB0YWJsZUlkLFxuICAgIGhpZGRlbixcbiAgICBvbkNsaWNrLFxuICAgIG9uQmx1cixcbiAgICBvcHRpb25zLFxuICAgIGNsYXNzTmFtZSxcbiAgICB2YXJpYXRpb24sXG4gICAgYm9vdHN0cmFwNCxcbiAgICBidG5Db250ZXh0dWFsLFxuICAgIG9wdGlvblJlbmRlcmVyLFxuICAgIGN1cnJTaXplUGVyUGFnZSxcbiAgICBvblNpemVQZXJQYWdlQ2hhbmdlXG4gIH0gPSBwcm9wcztcblxuICBjb25zdCBkcm9wRG93blN0eWxlID0geyB2aXNpYmlsaXR5OiBoaWRkZW4gPyAnaGlkZGVuJyA6ICd2aXNpYmxlJyB9O1xuICBjb25zdCBvcGVuQ2xhc3MgPSBvcGVuID8gJ29wZW4gc2hvdycgOiAnJztcbiAgY29uc3QgZHJvcGRvd25DbGFzc2VzID0gY3MoXG4gICAgb3BlbkNsYXNzLFxuICAgIHNpemVQZXJQYWdlRGVmYXVsdENsYXNzLFxuICAgIHZhcmlhdGlvbixcbiAgICBjbGFzc05hbWUsXG4gICk7XG5cbiAgY29uc3QgaWQgPSB0YWJsZUlkID8gYCR7dGFibGVJZH0tcGFnZURyb3BEb3duYCA6ICdwYWdlRHJvcERvd24nO1xuXG4gIHJldHVybiAoXG4gICAgPHNwYW5cbiAgICAgIHN0eWxlPXsgZHJvcERvd25TdHlsZSB9XG4gICAgICBjbGFzc05hbWU9eyBkcm9wZG93bkNsYXNzZXMgfVxuICAgID5cbiAgICAgIDxidXR0b25cbiAgICAgICAgaWQ9eyBpZCB9XG4gICAgICAgIHR5cGU9XCJidXR0b25cIlxuICAgICAgICBjbGFzc05hbWU9eyBgYnRuICR7YnRuQ29udGV4dHVhbH0gZHJvcGRvd24tdG9nZ2xlYCB9XG4gICAgICAgIGRhdGEtdG9nZ2xlPVwiZHJvcGRvd25cIlxuICAgICAgICBhcmlhLWV4cGFuZGVkPXsgb3BlbiB9XG4gICAgICAgIG9uQ2xpY2s9eyBvbkNsaWNrIH1cbiAgICAgICAgb25CbHVyPXsgb25CbHVyIH1cbiAgICAgID5cbiAgICAgICAgeyBjdXJyU2l6ZVBlclBhZ2UgfVxuICAgICAgICB7ICcgJyB9XG4gICAgICAgIHtcbiAgICAgICAgICBib290c3RyYXA0ID8gbnVsbCA6IChcbiAgICAgICAgICAgIDxzcGFuPlxuICAgICAgICAgICAgICA8c3BhbiBjbGFzc05hbWU9XCJjYXJldFwiIC8+XG4gICAgICAgICAgICA8L3NwYW4+XG4gICAgICAgICAgKVxuICAgICAgICB9XG4gICAgICA8L2J1dHRvbj5cbiAgICAgIDx1bFxuICAgICAgICBjbGFzc05hbWU9eyBgZHJvcGRvd24tbWVudSAke29wZW5DbGFzc31gIH1cbiAgICAgICAgcm9sZT1cIm1lbnVcIlxuICAgICAgICBhcmlhLWxhYmVsbGVkYnk9eyBpZCB9XG4gICAgICA+XG4gICAgICAgIHtcbiAgICAgICAgICBvcHRpb25zLm1hcCgob3B0aW9uKSA9PiB7XG4gICAgICAgICAgICBpZiAob3B0aW9uUmVuZGVyZXIpIHtcbiAgICAgICAgICAgICAgcmV0dXJuIG9wdGlvblJlbmRlcmVyKHtcbiAgICAgICAgICAgICAgICAuLi5vcHRpb24sXG4gICAgICAgICAgICAgICAgb25TaXplUGVyUGFnZUNoYW5nZVxuICAgICAgICAgICAgICB9KTtcbiAgICAgICAgICAgIH1cbiAgICAgICAgICAgIHJldHVybiAoXG4gICAgICAgICAgICAgIDxTaXplUGVyUGFnZU9wdGlvblxuICAgICAgICAgICAgICAgIHsgLi4ub3B0aW9uIH1cbiAgICAgICAgICAgICAgICBrZXk9eyBvcHRpb24udGV4dCB9XG4gICAgICAgICAgICAgICAgYm9vdHN0cmFwND17IGJvb3RzdHJhcDQgfVxuICAgICAgICAgICAgICAgIG9uU2l6ZVBlclBhZ2VDaGFuZ2U9eyBvblNpemVQZXJQYWdlQ2hhbmdlIH1cbiAgICAgICAgICAgICAgLz5cbiAgICAgICAgICAgICk7XG4gICAgICAgICAgfSlcbiAgICAgICAgfVxuICAgICAgPC91bD5cbiAgICA8L3NwYW4+XG4gICk7XG59O1xuXG5TaXplUGVyUGFnZURyb3BEb3duLnByb3BUeXBlcyA9IHtcbiAgY3VyclNpemVQZXJQYWdlOiBQcm9wVHlwZXMuc3RyaW5nLmlzUmVxdWlyZWQsXG4gIG9wdGlvbnM6IFByb3BUeXBlcy5hcnJheS5pc1JlcXVpcmVkLFxuICBvbkNsaWNrOiBQcm9wVHlwZXMuZnVuYy5pc1JlcXVpcmVkLFxuICBvbkJsdXI6IFByb3BUeXBlcy5mdW5jLmlzUmVxdWlyZWQsXG4gIG9uU2l6ZVBlclBhZ2VDaGFuZ2U6IFByb3BUeXBlcy5mdW5jLmlzUmVxdWlyZWQsXG4gIGJvb3RzdHJhcDQ6IFByb3BUeXBlcy5ib29sLFxuICB0YWJsZUlkOiBQcm9wVHlwZXMuc3RyaW5nLFxuICBvcGVuOiBQcm9wVHlwZXMuYm9vbCxcbiAgaGlkZGVuOiBQcm9wVHlwZXMuYm9vbCxcbiAgYnRuQ29udGV4dHVhbDogUHJvcFR5cGVzLnN0cmluZyxcbiAgdmFyaWF0aW9uOiBQcm9wVHlwZXMub25lT2YoWydkcm9wZG93bicsICdkcm9wdXAnXSksXG4gIGNsYXNzTmFtZTogUHJvcFR5cGVzLnN0cmluZyxcbiAgb3B0aW9uUmVuZGVyZXI6IFByb3BUeXBlcy5mdW5jXG59O1xuU2l6ZVBlclBhZ2VEcm9wRG93bi5kZWZhdWx0UHJvcHMgPSB7XG4gIG9wZW46IGZhbHNlLFxuICBoaWRkZW46IGZhbHNlLFxuICBidG5Db250ZXh0dWFsOiAnYnRuLWRlZmF1bHQgYnRuLXNlY29uZGFyeScsXG4gIHZhcmlhdGlvbjogJ2Ryb3Bkb3duJyxcbiAgY2xhc3NOYW1lOiAnJyxcbiAgb3B0aW9uUmVuZGVyZXI6IG51bGwsXG4gIGJvb3RzdHJhcDQ6IGZhbHNlLFxuICB0YWJsZUlkOiBudWxsXG59O1xuXG5cbmV4cG9ydCBkZWZhdWx0IFNpemVQZXJQYWdlRHJvcERvd247XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvc2l6ZS1wZXItcGFnZS1kcm9wZG93bi5qcyIsIi8qIGVzbGludCByZWFjdC9wcm9wLXR5cGVzOiAwICovXG5pbXBvcnQgUmVhY3QsIHsgQ29tcG9uZW50IH0gZnJvbSAncmVhY3QnO1xuXG5pbXBvcnQgcGFnZVJlc29sdmVyIGZyb20gJy4vcGFnZS1yZXNvbHZlcic7XG5pbXBvcnQgUGFnaW5hdGlvbkxpc3QgZnJvbSAnLi9wYWdpbmF0aW9uLWxpc3QnO1xuXG5jb25zdCBwYWdpbmF0aW9uTGlzdEFkYXB0ZXIgPSBXcmFwcGVkQ29tcG9uZW50ID0+XG4gIGNsYXNzIFBhZ2luYXRpb25MaXN0QWRhcHRlciBleHRlbmRzIHBhZ2VSZXNvbHZlcihDb21wb25lbnQpIHtcbiAgICByZW5kZXIoKSB7XG4gICAgICBjb25zdCB7XG4gICAgICAgIGxhc3RQYWdlLFxuICAgICAgICB0b3RhbFBhZ2VzLFxuICAgICAgICBwYWdlQnV0dG9uUmVuZGVyZXIsXG4gICAgICAgIG9uUGFnZUNoYW5nZSxcbiAgICAgICAgZGlzYWJsZVBhZ2VUaXRsZSxcbiAgICAgICAgaGlkZVBhZ2VMaXN0T25seU9uZVBhZ2VcbiAgICAgIH0gPSB0aGlzLnByb3BzO1xuICAgICAgY29uc3QgcGFnZXMgPSB0aGlzLmNhbGN1bGF0ZVBhZ2VTdGF0dXMoXG4gICAgICAgIHRoaXMuY2FsY3VsYXRlUGFnZXModG90YWxQYWdlcywgbGFzdFBhZ2UpLFxuICAgICAgICBsYXN0UGFnZSxcbiAgICAgICAgZGlzYWJsZVBhZ2VUaXRsZVxuICAgICAgKTtcbiAgICAgIGlmICh0b3RhbFBhZ2VzID09PSAxICYmIGhpZGVQYWdlTGlzdE9ubHlPbmVQYWdlKSB7XG4gICAgICAgIHJldHVybiBudWxsO1xuICAgICAgfVxuICAgICAgcmV0dXJuIChcbiAgICAgICAgPFdyYXBwZWRDb21wb25lbnRcbiAgICAgICAgICBwYWdlQnV0dG9uUmVuZGVyZXI9eyBwYWdlQnV0dG9uUmVuZGVyZXIgfVxuICAgICAgICAgIG9uUGFnZUNoYW5nZT17IG9uUGFnZUNoYW5nZSB9XG4gICAgICAgICAgcGFnZXM9eyBwYWdlcyB9XG4gICAgICAgIC8+XG4gICAgICApO1xuICAgIH1cbiAgfTtcblxuXG5leHBvcnQgY29uc3QgUGFnaW5hdGlvbkxpc3RXaXRoQWRhcHRlciA9IHBhZ2luYXRpb25MaXN0QWRhcHRlcihQYWdpbmF0aW9uTGlzdCk7XG5leHBvcnQgZGVmYXVsdCBwYWdpbmF0aW9uTGlzdEFkYXB0ZXI7XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvcGFnaW5hdGlvbi1saXN0LWFkYXB0ZXIuanMiLCJpbXBvcnQgUmVhY3QgZnJvbSAncmVhY3QnO1xuaW1wb3J0IFByb3BUeXBlcyBmcm9tICdwcm9wLXR5cGVzJztcblxuaW1wb3J0IFBhZ2VCdXR0b24gZnJvbSAnLi9wYWdlLWJ1dHRvbic7XG5cbmNvbnN0IFBhZ2luYXRvbkxpc3QgPSBwcm9wcyA9PiAoXG4gIDx1bCBjbGFzc05hbWU9XCJwYWdpbmF0aW9uIHJlYWN0LWJvb3RzdHJhcC10YWJsZS1wYWdlLWJ0bnMtdWxcIj5cbiAgICB7XG4gICAgICBwcm9wcy5wYWdlcy5tYXAoKHBhZ2VQcm9wcykgPT4ge1xuICAgICAgICBpZiAocHJvcHMucGFnZUJ1dHRvblJlbmRlcmVyKSB7XG4gICAgICAgICAgcmV0dXJuIHByb3BzLnBhZ2VCdXR0b25SZW5kZXJlcih7XG4gICAgICAgICAgICAuLi5wYWdlUHJvcHMsXG4gICAgICAgICAgICBvblBhZ2VDaGFuZ2U6IHByb3BzLm9uUGFnZUNoYW5nZVxuICAgICAgICAgIH0pO1xuICAgICAgICB9XG4gICAgICAgIHJldHVybiAoXG4gICAgICAgICAgPFBhZ2VCdXR0b25cbiAgICAgICAgICAgIGtleT17IHBhZ2VQcm9wcy5wYWdlIH1cbiAgICAgICAgICAgIHsgLi4ucGFnZVByb3BzIH1cbiAgICAgICAgICAgIG9uUGFnZUNoYW5nZT17IHByb3BzLm9uUGFnZUNoYW5nZSB9XG4gICAgICAgICAgLz5cbiAgICAgICAgKTtcbiAgICAgIH0pXG4gICAgfVxuICA8L3VsPlxuKTtcblxuUGFnaW5hdG9uTGlzdC5wcm9wVHlwZXMgPSB7XG4gIHBhZ2VzOiBQcm9wVHlwZXMuYXJyYXlPZihQcm9wVHlwZXMuc2hhcGUoe1xuICAgIHBhZ2U6IFByb3BUeXBlcy5vbmVPZlR5cGUoW1xuICAgICAgUHJvcFR5cGVzLm5vZGUsXG4gICAgICBQcm9wVHlwZXMubnVtYmVyLFxuICAgICAgUHJvcFR5cGVzLnN0cmluZ1xuICAgIF0pLFxuICAgIGFjdGl2ZTogUHJvcFR5cGVzLmJvb2wsXG4gICAgZGlzYWJsZTogUHJvcFR5cGVzLmJvb2wsXG4gICAgdGl0bGU6IFByb3BUeXBlcy5zdHJpbmdcbiAgfSkpLmlzUmVxdWlyZWQsXG4gIG9uUGFnZUNoYW5nZTogUHJvcFR5cGVzLmZ1bmMuaXNSZXF1aXJlZCxcbiAgcGFnZUJ1dHRvblJlbmRlcmVyOiBQcm9wVHlwZXMuZnVuY1xufTtcblxuUGFnaW5hdG9uTGlzdC5kZWZhdWx0UHJvcHMgPSB7XG4gIHBhZ2VCdXR0b25SZW5kZXJlcjogbnVsbFxufTtcblxuZXhwb3J0IGRlZmF1bHQgUGFnaW5hdG9uTGlzdDtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyAuL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItcGFnaW5hdG9yL3NyYy9wYWdpbmF0aW9uLWxpc3QuanMiLCIvKiBlc2xpbnQgcmVhY3QvcHJvcC10eXBlczogMCAqL1xuaW1wb3J0IFJlYWN0LCB7IENvbXBvbmVudCB9IGZyb20gJ3JlYWN0JztcblxuaW1wb3J0IHBhZ2VSZXNvbHZlciBmcm9tICcuL3BhZ2UtcmVzb2x2ZXInO1xuaW1wb3J0IFBhZ2luYXRpb25Ub3RhbCBmcm9tICcuL3BhZ2luYXRpb24tdG90YWwnO1xuXG5jb25zdCBwYWdpbmF0aW9uVG90YWxBZGFwdGVyID0gV3JhcHBlZENvbXBvbmVudCA9PlxuICBjbGFzcyBQYWdpbmF0aW9uVG90YWxBZGFwdGVyIGV4dGVuZHMgcGFnZVJlc29sdmVyKENvbXBvbmVudCkge1xuICAgIHJlbmRlcigpIHtcbiAgICAgIGNvbnN0IFtmcm9tLCB0b10gPSB0aGlzLmNhbGN1bGF0ZUZyb21UbygpO1xuICAgICAgcmV0dXJuIChcbiAgICAgICAgPFdyYXBwZWRDb21wb25lbnRcbiAgICAgICAgICBmcm9tPXsgZnJvbSB9XG4gICAgICAgICAgdG89eyB0byB9XG4gICAgICAgICAgZGF0YVNpemU9eyB0aGlzLnByb3BzLmRhdGFTaXplIH1cbiAgICAgICAgICBwYWdpbmF0aW9uVG90YWxSZW5kZXJlcj17IHRoaXMucHJvcHMucGFnaW5hdGlvblRvdGFsUmVuZGVyZXIgfVxuICAgICAgICAvPlxuICAgICAgKTtcbiAgICB9XG4gIH07XG5cblxuZXhwb3J0IGNvbnN0IFBhZ2luYXRpb25Ub3RhbFdpdGhBZGFwdGVyID0gcGFnaW5hdGlvblRvdGFsQWRhcHRlcihQYWdpbmF0aW9uVG90YWwpO1xuZXhwb3J0IGRlZmF1bHQgcGFnaW5hdGlvblRvdGFsQWRhcHRlcjtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyAuL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItcGFnaW5hdG9yL3NyYy9wYWdpbmF0aW9uLXRvdGFsLWFkYXB0ZXIuanMiLCJpbXBvcnQgUmVhY3QgZnJvbSAncmVhY3QnO1xuaW1wb3J0IFByb3BUeXBlcyBmcm9tICdwcm9wLXR5cGVzJztcblxuY29uc3QgUGFnaW5hdGlvblRvdGFsID0gKHByb3BzKSA9PiB7XG4gIGlmIChwcm9wcy5wYWdpbmF0aW9uVG90YWxSZW5kZXJlcikge1xuICAgIHJldHVybiBwcm9wcy5wYWdpbmF0aW9uVG90YWxSZW5kZXJlcihwcm9wcy5mcm9tLCBwcm9wcy50bywgcHJvcHMuZGF0YVNpemUpO1xuICB9XG4gIHJldHVybiAoXG4gICAgPHNwYW4gY2xhc3NOYW1lPVwicmVhY3QtYm9vdHN0cmFwLXRhYmxlLXBhZ2luYXRpb24tdG90YWxcIj5cbiAgICAgICZuYnNwO1Nob3dpbmcgcm93cyB7IHByb3BzLmZyb20gfSB0byZuYnNwO3sgcHJvcHMudG8gfSBvZiZuYnNwO3sgcHJvcHMuZGF0YVNpemUgfVxuICAgIDwvc3Bhbj5cbiAgKTtcbn07XG5cblBhZ2luYXRpb25Ub3RhbC5wcm9wVHlwZXMgPSB7XG4gIGZyb206IFByb3BUeXBlcy5udW1iZXIuaXNSZXF1aXJlZCxcbiAgdG86IFByb3BUeXBlcy5udW1iZXIuaXNSZXF1aXJlZCxcbiAgZGF0YVNpemU6IFByb3BUeXBlcy5udW1iZXIuaXNSZXF1aXJlZCxcbiAgcGFnaW5hdGlvblRvdGFsUmVuZGVyZXI6IFByb3BUeXBlcy5mdW5jXG59O1xuXG5QYWdpbmF0aW9uVG90YWwuZGVmYXVsdFByb3BzID0ge1xuICBwYWdpbmF0aW9uVG90YWxSZW5kZXJlcjogdW5kZWZpbmVkXG59O1xuXG5leHBvcnQgZGVmYXVsdCBQYWdpbmF0aW9uVG90YWw7XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvcGFnaW5hdGlvbi10b3RhbC5qcyIsImltcG9ydCBSZWFjdCBmcm9tICdyZWFjdCc7XG5pbXBvcnQgUHJvcFR5cGVzIGZyb20gJ3Byb3AtdHlwZXMnO1xuaW1wb3J0IGNyZWF0ZUJhc2VDb250ZXh0IGZyb20gJy4vc3JjL3N0YXRlLWNvbnRleHQnO1xuaW1wb3J0IGNyZWF0ZURhdGFDb250ZXh0IGZyb20gJy4vc3JjL2RhdGEtY29udGV4dCc7XG5pbXBvcnQgUGFnaW5hdGlvbkxpc3RTdGFuZGFsb25lIGZyb20gJy4vc3JjL3BhZ2luYXRpb24tbGlzdC1zdGFuZGFsb25lJztcbmltcG9ydCBTaXplUGVyUGFnZURyb3Bkb3duU3RhbmRhbG9uZSBmcm9tICcuL3NyYy9zaXplLXBlci1wYWdlLWRyb3Bkb3duLXN0YW5kYWxvbmUnO1xuaW1wb3J0IFBhZ2luYXRpb25Ub3RhbFN0YW5kYWxvbmUgZnJvbSAnLi9zcmMvcGFnaW5hdGlvbi10b3RhbC1zdGFuZGFsb25lJztcblxuZXhwb3J0IGRlZmF1bHQgKG9wdGlvbnMgPSB7fSkgPT4gKHtcbiAgY3JlYXRlQ29udGV4dDogY3JlYXRlRGF0YUNvbnRleHQsXG4gIG9wdGlvbnNcbn0pO1xuXG5jb25zdCB7IFByb3ZpZGVyLCBDb25zdW1lciB9ID0gY3JlYXRlQmFzZUNvbnRleHQoKTtcblxuY29uc3QgQ3VzdG9taXphYmxlUHJvdmlkZXIgPSBwcm9wcyA9PiAoXG4gIDxQcm92aWRlciB7IC4uLnByb3BzIH0+XG4gICAgPENvbnN1bWVyPnsgcGFnaW5hdGlvblByb3BzID0+IHByb3BzLmNoaWxkcmVuKHBhZ2luYXRpb25Qcm9wcykgfTwvQ29uc3VtZXI+XG4gIDwvUHJvdmlkZXI+XG4pO1xuXG5DdXN0b21pemFibGVQcm92aWRlci5wcm9wVHlwZXMgPSB7XG4gIGNoaWxkcmVuOiBQcm9wVHlwZXMuZnVuYy5pc1JlcXVpcmVkXG59O1xuXG5leHBvcnQgY29uc3QgUGFnaW5hdGlvblByb3ZpZGVyID0gQ3VzdG9taXphYmxlUHJvdmlkZXI7XG5leHBvcnQgeyBQYWdpbmF0aW9uTGlzdFN0YW5kYWxvbmUsIFNpemVQZXJQYWdlRHJvcGRvd25TdGFuZGFsb25lLCBQYWdpbmF0aW9uVG90YWxTdGFuZGFsb25lIH07XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9pbmRleC5qcyIsIi8qKlxuICogQ29weXJpZ2h0IDIwMTMtcHJlc2VudCwgRmFjZWJvb2ssIEluYy5cbiAqIEFsbCByaWdodHMgcmVzZXJ2ZWQuXG4gKlxuICogVGhpcyBzb3VyY2UgY29kZSBpcyBsaWNlbnNlZCB1bmRlciB0aGUgQlNELXN0eWxlIGxpY2Vuc2UgZm91bmQgaW4gdGhlXG4gKiBMSUNFTlNFIGZpbGUgaW4gdGhlIHJvb3QgZGlyZWN0b3J5IG9mIHRoaXMgc291cmNlIHRyZWUuIEFuIGFkZGl0aW9uYWwgZ3JhbnRcbiAqIG9mIHBhdGVudCByaWdodHMgY2FuIGJlIGZvdW5kIGluIHRoZSBQQVRFTlRTIGZpbGUgaW4gdGhlIHNhbWUgZGlyZWN0b3J5LlxuICovXG5cbid1c2Ugc3RyaWN0JztcblxudmFyIGVtcHR5RnVuY3Rpb24gPSByZXF1aXJlKCdmYmpzL2xpYi9lbXB0eUZ1bmN0aW9uJyk7XG52YXIgaW52YXJpYW50ID0gcmVxdWlyZSgnZmJqcy9saWIvaW52YXJpYW50Jyk7XG52YXIgUmVhY3RQcm9wVHlwZXNTZWNyZXQgPSByZXF1aXJlKCcuL2xpYi9SZWFjdFByb3BUeXBlc1NlY3JldCcpO1xuXG5tb2R1bGUuZXhwb3J0cyA9IGZ1bmN0aW9uKCkge1xuICBmdW5jdGlvbiBzaGltKHByb3BzLCBwcm9wTmFtZSwgY29tcG9uZW50TmFtZSwgbG9jYXRpb24sIHByb3BGdWxsTmFtZSwgc2VjcmV0KSB7XG4gICAgaWYgKHNlY3JldCA9PT0gUmVhY3RQcm9wVHlwZXNTZWNyZXQpIHtcbiAgICAgIC8vIEl0IGlzIHN0aWxsIHNhZmUgd2hlbiBjYWxsZWQgZnJvbSBSZWFjdC5cbiAgICAgIHJldHVybjtcbiAgICB9XG4gICAgaW52YXJpYW50KFxuICAgICAgZmFsc2UsXG4gICAgICAnQ2FsbGluZyBQcm9wVHlwZXMgdmFsaWRhdG9ycyBkaXJlY3RseSBpcyBub3Qgc3VwcG9ydGVkIGJ5IHRoZSBgcHJvcC10eXBlc2AgcGFja2FnZS4gJyArXG4gICAgICAnVXNlIFByb3BUeXBlcy5jaGVja1Byb3BUeXBlcygpIHRvIGNhbGwgdGhlbS4gJyArXG4gICAgICAnUmVhZCBtb3JlIGF0IGh0dHA6Ly9mYi5tZS91c2UtY2hlY2stcHJvcC10eXBlcydcbiAgICApO1xuICB9O1xuICBzaGltLmlzUmVxdWlyZWQgPSBzaGltO1xuICBmdW5jdGlvbiBnZXRTaGltKCkge1xuICAgIHJldHVybiBzaGltO1xuICB9O1xuICAvLyBJbXBvcnRhbnQhXG4gIC8vIEtlZXAgdGhpcyBsaXN0IGluIHN5bmMgd2l0aCBwcm9kdWN0aW9uIHZlcnNpb24gaW4gYC4vZmFjdG9yeVdpdGhUeXBlQ2hlY2tlcnMuanNgLlxuICB2YXIgUmVhY3RQcm9wVHlwZXMgPSB7XG4gICAgYXJyYXk6IHNoaW0sXG4gICAgYm9vbDogc2hpbSxcbiAgICBmdW5jOiBzaGltLFxuICAgIG51bWJlcjogc2hpbSxcbiAgICBvYmplY3Q6IHNoaW0sXG4gICAgc3RyaW5nOiBzaGltLFxuICAgIHN5bWJvbDogc2hpbSxcblxuICAgIGFueTogc2hpbSxcbiAgICBhcnJheU9mOiBnZXRTaGltLFxuICAgIGVsZW1lbnQ6IHNoaW0sXG4gICAgaW5zdGFuY2VPZjogZ2V0U2hpbSxcbiAgICBub2RlOiBzaGltLFxuICAgIG9iamVjdE9mOiBnZXRTaGltLFxuICAgIG9uZU9mOiBnZXRTaGltLFxuICAgIG9uZU9mVHlwZTogZ2V0U2hpbSxcbiAgICBzaGFwZTogZ2V0U2hpbVxuICB9O1xuXG4gIFJlYWN0UHJvcFR5cGVzLmNoZWNrUHJvcFR5cGVzID0gZW1wdHlGdW5jdGlvbjtcbiAgUmVhY3RQcm9wVHlwZXMuUHJvcFR5cGVzID0gUmVhY3RQcm9wVHlwZXM7XG5cbiAgcmV0dXJuIFJlYWN0UHJvcFR5cGVzO1xufTtcblxuXG5cbi8vLy8vLy8vLy8vLy8vLy8vL1xuLy8gV0VCUEFDSyBGT09URVJcbi8vIC4vbm9kZV9tb2R1bGVzL3Byb3AtdHlwZXMvZmFjdG9yeVdpdGhUaHJvd2luZ1NoaW1zLmpzXG4vLyBtb2R1bGUgaWQgPSAxNlxuLy8gbW9kdWxlIGNodW5rcyA9IDAgMSIsIlwidXNlIHN0cmljdFwiO1xuXG4vKipcbiAqIENvcHlyaWdodCAoYykgMjAxMy1wcmVzZW50LCBGYWNlYm9vaywgSW5jLlxuICpcbiAqIFRoaXMgc291cmNlIGNvZGUgaXMgbGljZW5zZWQgdW5kZXIgdGhlIE1JVCBsaWNlbnNlIGZvdW5kIGluIHRoZVxuICogTElDRU5TRSBmaWxlIGluIHRoZSByb290IGRpcmVjdG9yeSBvZiB0aGlzIHNvdXJjZSB0cmVlLlxuICpcbiAqIFxuICovXG5cbmZ1bmN0aW9uIG1ha2VFbXB0eUZ1bmN0aW9uKGFyZykge1xuICByZXR1cm4gZnVuY3Rpb24gKCkge1xuICAgIHJldHVybiBhcmc7XG4gIH07XG59XG5cbi8qKlxuICogVGhpcyBmdW5jdGlvbiBhY2NlcHRzIGFuZCBkaXNjYXJkcyBpbnB1dHM7IGl0IGhhcyBubyBzaWRlIGVmZmVjdHMuIFRoaXMgaXNcbiAqIHByaW1hcmlseSB1c2VmdWwgaWRpb21hdGljYWxseSBmb3Igb3ZlcnJpZGFibGUgZnVuY3Rpb24gZW5kcG9pbnRzIHdoaWNoXG4gKiBhbHdheXMgbmVlZCB0byBiZSBjYWxsYWJsZSwgc2luY2UgSlMgbGFja3MgYSBudWxsLWNhbGwgaWRpb20gYWxhIENvY29hLlxuICovXG52YXIgZW1wdHlGdW5jdGlvbiA9IGZ1bmN0aW9uIGVtcHR5RnVuY3Rpb24oKSB7fTtcblxuZW1wdHlGdW5jdGlvbi50aGF0UmV0dXJucyA9IG1ha2VFbXB0eUZ1bmN0aW9uO1xuZW1wdHlGdW5jdGlvbi50aGF0UmV0dXJuc0ZhbHNlID0gbWFrZUVtcHR5RnVuY3Rpb24oZmFsc2UpO1xuZW1wdHlGdW5jdGlvbi50aGF0UmV0dXJuc1RydWUgPSBtYWtlRW1wdHlGdW5jdGlvbih0cnVlKTtcbmVtcHR5RnVuY3Rpb24udGhhdFJldHVybnNOdWxsID0gbWFrZUVtcHR5RnVuY3Rpb24obnVsbCk7XG5lbXB0eUZ1bmN0aW9uLnRoYXRSZXR1cm5zVGhpcyA9IGZ1bmN0aW9uICgpIHtcbiAgcmV0dXJuIHRoaXM7XG59O1xuZW1wdHlGdW5jdGlvbi50aGF0UmV0dXJuc0FyZ3VtZW50ID0gZnVuY3Rpb24gKGFyZykge1xuICByZXR1cm4gYXJnO1xufTtcblxubW9kdWxlLmV4cG9ydHMgPSBlbXB0eUZ1bmN0aW9uO1xuXG5cbi8vLy8vLy8vLy8vLy8vLy8vL1xuLy8gV0VCUEFDSyBGT09URVJcbi8vIC4vbm9kZV9tb2R1bGVzL2ZianMvbGliL2VtcHR5RnVuY3Rpb24uanNcbi8vIG1vZHVsZSBpZCA9IDE3XG4vLyBtb2R1bGUgY2h1bmtzID0gMCAxIiwiLyoqXG4gKiBDb3B5cmlnaHQgKGMpIDIwMTMtcHJlc2VudCwgRmFjZWJvb2ssIEluYy5cbiAqXG4gKiBUaGlzIHNvdXJjZSBjb2RlIGlzIGxpY2Vuc2VkIHVuZGVyIHRoZSBNSVQgbGljZW5zZSBmb3VuZCBpbiB0aGVcbiAqIExJQ0VOU0UgZmlsZSBpbiB0aGUgcm9vdCBkaXJlY3Rvcnkgb2YgdGhpcyBzb3VyY2UgdHJlZS5cbiAqXG4gKi9cblxuJ3VzZSBzdHJpY3QnO1xuXG4vKipcbiAqIFVzZSBpbnZhcmlhbnQoKSB0byBhc3NlcnQgc3RhdGUgd2hpY2ggeW91ciBwcm9ncmFtIGFzc3VtZXMgdG8gYmUgdHJ1ZS5cbiAqXG4gKiBQcm92aWRlIHNwcmludGYtc3R5bGUgZm9ybWF0IChvbmx5ICVzIGlzIHN1cHBvcnRlZCkgYW5kIGFyZ3VtZW50c1xuICogdG8gcHJvdmlkZSBpbmZvcm1hdGlvbiBhYm91dCB3aGF0IGJyb2tlIGFuZCB3aGF0IHlvdSB3ZXJlXG4gKiBleHBlY3RpbmcuXG4gKlxuICogVGhlIGludmFyaWFudCBtZXNzYWdlIHdpbGwgYmUgc3RyaXBwZWQgaW4gcHJvZHVjdGlvbiwgYnV0IHRoZSBpbnZhcmlhbnRcbiAqIHdpbGwgcmVtYWluIHRvIGVuc3VyZSBsb2dpYyBkb2VzIG5vdCBkaWZmZXIgaW4gcHJvZHVjdGlvbi5cbiAqL1xuXG52YXIgdmFsaWRhdGVGb3JtYXQgPSBmdW5jdGlvbiB2YWxpZGF0ZUZvcm1hdChmb3JtYXQpIHt9O1xuXG5pZiAocHJvY2Vzcy5lbnYuTk9ERV9FTlYgIT09ICdwcm9kdWN0aW9uJykge1xuICB2YWxpZGF0ZUZvcm1hdCA9IGZ1bmN0aW9uIHZhbGlkYXRlRm9ybWF0KGZvcm1hdCkge1xuICAgIGlmIChmb3JtYXQgPT09IHVuZGVmaW5lZCkge1xuICAgICAgdGhyb3cgbmV3IEVycm9yKCdpbnZhcmlhbnQgcmVxdWlyZXMgYW4gZXJyb3IgbWVzc2FnZSBhcmd1bWVudCcpO1xuICAgIH1cbiAgfTtcbn1cblxuZnVuY3Rpb24gaW52YXJpYW50KGNvbmRpdGlvbiwgZm9ybWF0LCBhLCBiLCBjLCBkLCBlLCBmKSB7XG4gIHZhbGlkYXRlRm9ybWF0KGZvcm1hdCk7XG5cbiAgaWYgKCFjb25kaXRpb24pIHtcbiAgICB2YXIgZXJyb3I7XG4gICAgaWYgKGZvcm1hdCA9PT0gdW5kZWZpbmVkKSB7XG4gICAgICBlcnJvciA9IG5ldyBFcnJvcignTWluaWZpZWQgZXhjZXB0aW9uIG9jY3VycmVkOyB1c2UgdGhlIG5vbi1taW5pZmllZCBkZXYgZW52aXJvbm1lbnQgJyArICdmb3IgdGhlIGZ1bGwgZXJyb3IgbWVzc2FnZSBhbmQgYWRkaXRpb25hbCBoZWxwZnVsIHdhcm5pbmdzLicpO1xuICAgIH0gZWxzZSB7XG4gICAgICB2YXIgYXJncyA9IFthLCBiLCBjLCBkLCBlLCBmXTtcbiAgICAgIHZhciBhcmdJbmRleCA9IDA7XG4gICAgICBlcnJvciA9IG5ldyBFcnJvcihmb3JtYXQucmVwbGFjZSgvJXMvZywgZnVuY3Rpb24gKCkge1xuICAgICAgICByZXR1cm4gYXJnc1thcmdJbmRleCsrXTtcbiAgICAgIH0pKTtcbiAgICAgIGVycm9yLm5hbWUgPSAnSW52YXJpYW50IFZpb2xhdGlvbic7XG4gICAgfVxuXG4gICAgZXJyb3IuZnJhbWVzVG9Qb3AgPSAxOyAvLyB3ZSBkb24ndCBjYXJlIGFib3V0IGludmFyaWFudCdzIG93biBmcmFtZVxuICAgIHRocm93IGVycm9yO1xuICB9XG59XG5cbm1vZHVsZS5leHBvcnRzID0gaW52YXJpYW50O1xuXG5cbi8vLy8vLy8vLy8vLy8vLy8vL1xuLy8gV0VCUEFDSyBGT09URVJcbi8vIC4vbm9kZV9tb2R1bGVzL2ZianMvbGliL2ludmFyaWFudC5qc1xuLy8gbW9kdWxlIGlkID0gMThcbi8vIG1vZHVsZSBjaHVua3MgPSAwIDEiLCIvKipcbiAqIENvcHlyaWdodCAyMDEzLXByZXNlbnQsIEZhY2Vib29rLCBJbmMuXG4gKiBBbGwgcmlnaHRzIHJlc2VydmVkLlxuICpcbiAqIFRoaXMgc291cmNlIGNvZGUgaXMgbGljZW5zZWQgdW5kZXIgdGhlIEJTRC1zdHlsZSBsaWNlbnNlIGZvdW5kIGluIHRoZVxuICogTElDRU5TRSBmaWxlIGluIHRoZSByb290IGRpcmVjdG9yeSBvZiB0aGlzIHNvdXJjZSB0cmVlLiBBbiBhZGRpdGlvbmFsIGdyYW50XG4gKiBvZiBwYXRlbnQgcmlnaHRzIGNhbiBiZSBmb3VuZCBpbiB0aGUgUEFURU5UUyBmaWxlIGluIHRoZSBzYW1lIGRpcmVjdG9yeS5cbiAqL1xuXG4ndXNlIHN0cmljdCc7XG5cbnZhciBSZWFjdFByb3BUeXBlc1NlY3JldCA9ICdTRUNSRVRfRE9fTk9UX1BBU1NfVEhJU19PUl9ZT1VfV0lMTF9CRV9GSVJFRCc7XG5cbm1vZHVsZS5leHBvcnRzID0gUmVhY3RQcm9wVHlwZXNTZWNyZXQ7XG5cblxuXG4vLy8vLy8vLy8vLy8vLy8vLy9cbi8vIFdFQlBBQ0sgRk9PVEVSXG4vLyAuL25vZGVfbW9kdWxlcy9wcm9wLXR5cGVzL2xpYi9SZWFjdFByb3BUeXBlc1NlY3JldC5qc1xuLy8gbW9kdWxlIGlkID0gMTlcbi8vIG1vZHVsZSBjaHVua3MgPSAwIDEiLCIvLyBDb3B5cmlnaHQgSm95ZW50LCBJbmMuIGFuZCBvdGhlciBOb2RlIGNvbnRyaWJ1dG9ycy5cbi8vXG4vLyBQZXJtaXNzaW9uIGlzIGhlcmVieSBncmFudGVkLCBmcmVlIG9mIGNoYXJnZSwgdG8gYW55IHBlcnNvbiBvYnRhaW5pbmcgYVxuLy8gY29weSBvZiB0aGlzIHNvZnR3YXJlIGFuZCBhc3NvY2lhdGVkIGRvY3VtZW50YXRpb24gZmlsZXMgKHRoZVxuLy8gXCJTb2Z0d2FyZVwiKSwgdG8gZGVhbCBpbiB0aGUgU29mdHdhcmUgd2l0aG91dCByZXN0cmljdGlvbiwgaW5jbHVkaW5nXG4vLyB3aXRob3V0IGxpbWl0YXRpb24gdGhlIHJpZ2h0cyB0byB1c2UsIGNvcHksIG1vZGlmeSwgbWVyZ2UsIHB1Ymxpc2gsXG4vLyBkaXN0cmlidXRlLCBzdWJsaWNlbnNlLCBhbmQvb3Igc2VsbCBjb3BpZXMgb2YgdGhlIFNvZnR3YXJlLCBhbmQgdG8gcGVybWl0XG4vLyBwZXJzb25zIHRvIHdob20gdGhlIFNvZnR3YXJlIGlzIGZ1cm5pc2hlZCB0byBkbyBzbywgc3ViamVjdCB0byB0aGVcbi8vIGZvbGxvd2luZyBjb25kaXRpb25zOlxuLy9cbi8vIFRoZSBhYm92ZSBjb3B5cmlnaHQgbm90aWNlIGFuZCB0aGlzIHBlcm1pc3Npb24gbm90aWNlIHNoYWxsIGJlIGluY2x1ZGVkXG4vLyBpbiBhbGwgY29waWVzIG9yIHN1YnN0YW50aWFsIHBvcnRpb25zIG9mIHRoZSBTb2Z0d2FyZS5cbi8vXG4vLyBUSEUgU09GVFdBUkUgSVMgUFJPVklERUQgXCJBUyBJU1wiLCBXSVRIT1VUIFdBUlJBTlRZIE9GIEFOWSBLSU5ELCBFWFBSRVNTXG4vLyBPUiBJTVBMSUVELCBJTkNMVURJTkcgQlVUIE5PVCBMSU1JVEVEIFRPIFRIRSBXQVJSQU5USUVTIE9GXG4vLyBNRVJDSEFOVEFCSUxJVFksIEZJVE5FU1MgRk9SIEEgUEFSVElDVUxBUiBQVVJQT1NFIEFORCBOT05JTkZSSU5HRU1FTlQuIElOXG4vLyBOTyBFVkVOVCBTSEFMTCBUSEUgQVVUSE9SUyBPUiBDT1BZUklHSFQgSE9MREVSUyBCRSBMSUFCTEUgRk9SIEFOWSBDTEFJTSxcbi8vIERBTUFHRVMgT1IgT1RIRVIgTElBQklMSVRZLCBXSEVUSEVSIElOIEFOIEFDVElPTiBPRiBDT05UUkFDVCwgVE9SVCBPUlxuLy8gT1RIRVJXSVNFLCBBUklTSU5HIEZST00sIE9VVCBPRiBPUiBJTiBDT05ORUNUSU9OIFdJVEggVEhFIFNPRlRXQVJFIE9SIFRIRVxuLy8gVVNFIE9SIE9USEVSIERFQUxJTkdTIElOIFRIRSBTT0ZUV0FSRS5cblxuJ3VzZSBzdHJpY3QnO1xuXG52YXIgUiA9IHR5cGVvZiBSZWZsZWN0ID09PSAnb2JqZWN0JyA/IFJlZmxlY3QgOiBudWxsXG52YXIgUmVmbGVjdEFwcGx5ID0gUiAmJiB0eXBlb2YgUi5hcHBseSA9PT0gJ2Z1bmN0aW9uJ1xuICA/IFIuYXBwbHlcbiAgOiBmdW5jdGlvbiBSZWZsZWN0QXBwbHkodGFyZ2V0LCByZWNlaXZlciwgYXJncykge1xuICAgIHJldHVybiBGdW5jdGlvbi5wcm90b3R5cGUuYXBwbHkuY2FsbCh0YXJnZXQsIHJlY2VpdmVyLCBhcmdzKTtcbiAgfVxuXG52YXIgUmVmbGVjdE93bktleXNcbmlmIChSICYmIHR5cGVvZiBSLm93bktleXMgPT09ICdmdW5jdGlvbicpIHtcbiAgUmVmbGVjdE93bktleXMgPSBSLm93bktleXNcbn0gZWxzZSBpZiAoT2JqZWN0LmdldE93blByb3BlcnR5U3ltYm9scykge1xuICBSZWZsZWN0T3duS2V5cyA9IGZ1bmN0aW9uIFJlZmxlY3RPd25LZXlzKHRhcmdldCkge1xuICAgIHJldHVybiBPYmplY3QuZ2V0T3duUHJvcGVydHlOYW1lcyh0YXJnZXQpXG4gICAgICAuY29uY2F0KE9iamVjdC5nZXRPd25Qcm9wZXJ0eVN5bWJvbHModGFyZ2V0KSk7XG4gIH07XG59IGVsc2Uge1xuICBSZWZsZWN0T3duS2V5cyA9IGZ1bmN0aW9uIFJlZmxlY3RPd25LZXlzKHRhcmdldCkge1xuICAgIHJldHVybiBPYmplY3QuZ2V0T3duUHJvcGVydHlOYW1lcyh0YXJnZXQpO1xuICB9O1xufVxuXG5mdW5jdGlvbiBQcm9jZXNzRW1pdFdhcm5pbmcod2FybmluZykge1xuICBpZiAoY29uc29sZSAmJiBjb25zb2xlLndhcm4pIGNvbnNvbGUud2Fybih3YXJuaW5nKTtcbn1cblxudmFyIE51bWJlcklzTmFOID0gTnVtYmVyLmlzTmFOIHx8IGZ1bmN0aW9uIE51bWJlcklzTmFOKHZhbHVlKSB7XG4gIHJldHVybiB2YWx1ZSAhPT0gdmFsdWU7XG59XG5cbmZ1bmN0aW9uIEV2ZW50RW1pdHRlcigpIHtcbiAgRXZlbnRFbWl0dGVyLmluaXQuY2FsbCh0aGlzKTtcbn1cbm1vZHVsZS5leHBvcnRzID0gRXZlbnRFbWl0dGVyO1xubW9kdWxlLmV4cG9ydHMub25jZSA9IG9uY2U7XG5cbi8vIEJhY2t3YXJkcy1jb21wYXQgd2l0aCBub2RlIDAuMTAueFxuRXZlbnRFbWl0dGVyLkV2ZW50RW1pdHRlciA9IEV2ZW50RW1pdHRlcjtcblxuRXZlbnRFbWl0dGVyLnByb3RvdHlwZS5fZXZlbnRzID0gdW5kZWZpbmVkO1xuRXZlbnRFbWl0dGVyLnByb3RvdHlwZS5fZXZlbnRzQ291bnQgPSAwO1xuRXZlbnRFbWl0dGVyLnByb3RvdHlwZS5fbWF4TGlzdGVuZXJzID0gdW5kZWZpbmVkO1xuXG4vLyBCeSBkZWZhdWx0IEV2ZW50RW1pdHRlcnMgd2lsbCBwcmludCBhIHdhcm5pbmcgaWYgbW9yZSB0aGFuIDEwIGxpc3RlbmVycyBhcmVcbi8vIGFkZGVkIHRvIGl0LiBUaGlzIGlzIGEgdXNlZnVsIGRlZmF1bHQgd2hpY2ggaGVscHMgZmluZGluZyBtZW1vcnkgbGVha3MuXG52YXIgZGVmYXVsdE1heExpc3RlbmVycyA9IDEwO1xuXG5mdW5jdGlvbiBjaGVja0xpc3RlbmVyKGxpc3RlbmVyKSB7XG4gIGlmICh0eXBlb2YgbGlzdGVuZXIgIT09ICdmdW5jdGlvbicpIHtcbiAgICB0aHJvdyBuZXcgVHlwZUVycm9yKCdUaGUgXCJsaXN0ZW5lclwiIGFyZ3VtZW50IG11c3QgYmUgb2YgdHlwZSBGdW5jdGlvbi4gUmVjZWl2ZWQgdHlwZSAnICsgdHlwZW9mIGxpc3RlbmVyKTtcbiAgfVxufVxuXG5PYmplY3QuZGVmaW5lUHJvcGVydHkoRXZlbnRFbWl0dGVyLCAnZGVmYXVsdE1heExpc3RlbmVycycsIHtcbiAgZW51bWVyYWJsZTogdHJ1ZSxcbiAgZ2V0OiBmdW5jdGlvbigpIHtcbiAgICByZXR1cm4gZGVmYXVsdE1heExpc3RlbmVycztcbiAgfSxcbiAgc2V0OiBmdW5jdGlvbihhcmcpIHtcbiAgICBpZiAodHlwZW9mIGFyZyAhPT0gJ251bWJlcicgfHwgYXJnIDwgMCB8fCBOdW1iZXJJc05hTihhcmcpKSB7XG4gICAgICB0aHJvdyBuZXcgUmFuZ2VFcnJvcignVGhlIHZhbHVlIG9mIFwiZGVmYXVsdE1heExpc3RlbmVyc1wiIGlzIG91dCBvZiByYW5nZS4gSXQgbXVzdCBiZSBhIG5vbi1uZWdhdGl2ZSBudW1iZXIuIFJlY2VpdmVkICcgKyBhcmcgKyAnLicpO1xuICAgIH1cbiAgICBkZWZhdWx0TWF4TGlzdGVuZXJzID0gYXJnO1xuICB9XG59KTtcblxuRXZlbnRFbWl0dGVyLmluaXQgPSBmdW5jdGlvbigpIHtcblxuICBpZiAodGhpcy5fZXZlbnRzID09PSB1bmRlZmluZWQgfHxcbiAgICAgIHRoaXMuX2V2ZW50cyA9PT0gT2JqZWN0LmdldFByb3RvdHlwZU9mKHRoaXMpLl9ldmVudHMpIHtcbiAgICB0aGlzLl9ldmVudHMgPSBPYmplY3QuY3JlYXRlKG51bGwpO1xuICAgIHRoaXMuX2V2ZW50c0NvdW50ID0gMDtcbiAgfVxuXG4gIHRoaXMuX21heExpc3RlbmVycyA9IHRoaXMuX21heExpc3RlbmVycyB8fCB1bmRlZmluZWQ7XG59O1xuXG4vLyBPYnZpb3VzbHkgbm90IGFsbCBFbWl0dGVycyBzaG91bGQgYmUgbGltaXRlZCB0byAxMC4gVGhpcyBmdW5jdGlvbiBhbGxvd3Ncbi8vIHRoYXQgdG8gYmUgaW5jcmVhc2VkLiBTZXQgdG8gemVybyBmb3IgdW5saW1pdGVkLlxuRXZlbnRFbWl0dGVyLnByb3RvdHlwZS5zZXRNYXhMaXN0ZW5lcnMgPSBmdW5jdGlvbiBzZXRNYXhMaXN0ZW5lcnMobikge1xuICBpZiAodHlwZW9mIG4gIT09ICdudW1iZXInIHx8IG4gPCAwIHx8IE51bWJlcklzTmFOKG4pKSB7XG4gICAgdGhyb3cgbmV3IFJhbmdlRXJyb3IoJ1RoZSB2YWx1ZSBvZiBcIm5cIiBpcyBvdXQgb2YgcmFuZ2UuIEl0IG11c3QgYmUgYSBub24tbmVnYXRpdmUgbnVtYmVyLiBSZWNlaXZlZCAnICsgbiArICcuJyk7XG4gIH1cbiAgdGhpcy5fbWF4TGlzdGVuZXJzID0gbjtcbiAgcmV0dXJuIHRoaXM7XG59O1xuXG5mdW5jdGlvbiBfZ2V0TWF4TGlzdGVuZXJzKHRoYXQpIHtcbiAgaWYgKHRoYXQuX21heExpc3RlbmVycyA9PT0gdW5kZWZpbmVkKVxuICAgIHJldHVybiBFdmVudEVtaXR0ZXIuZGVmYXVsdE1heExpc3RlbmVycztcbiAgcmV0dXJuIHRoYXQuX21heExpc3RlbmVycztcbn1cblxuRXZlbnRFbWl0dGVyLnByb3RvdHlwZS5nZXRNYXhMaXN0ZW5lcnMgPSBmdW5jdGlvbiBnZXRNYXhMaXN0ZW5lcnMoKSB7XG4gIHJldHVybiBfZ2V0TWF4TGlzdGVuZXJzKHRoaXMpO1xufTtcblxuRXZlbnRFbWl0dGVyLnByb3RvdHlwZS5lbWl0ID0gZnVuY3Rpb24gZW1pdCh0eXBlKSB7XG4gIHZhciBhcmdzID0gW107XG4gIGZvciAodmFyIGkgPSAxOyBpIDwgYXJndW1lbnRzLmxlbmd0aDsgaSsrKSBhcmdzLnB1c2goYXJndW1lbnRzW2ldKTtcbiAgdmFyIGRvRXJyb3IgPSAodHlwZSA9PT0gJ2Vycm9yJyk7XG5cbiAgdmFyIGV2ZW50cyA9IHRoaXMuX2V2ZW50cztcbiAgaWYgKGV2ZW50cyAhPT0gdW5kZWZpbmVkKVxuICAgIGRvRXJyb3IgPSAoZG9FcnJvciAmJiBldmVudHMuZXJyb3IgPT09IHVuZGVmaW5lZCk7XG4gIGVsc2UgaWYgKCFkb0Vycm9yKVxuICAgIHJldHVybiBmYWxzZTtcblxuICAvLyBJZiB0aGVyZSBpcyBubyAnZXJyb3InIGV2ZW50IGxpc3RlbmVyIHRoZW4gdGhyb3cuXG4gIGlmIChkb0Vycm9yKSB7XG4gICAgdmFyIGVyO1xuICAgIGlmIChhcmdzLmxlbmd0aCA+IDApXG4gICAgICBlciA9IGFyZ3NbMF07XG4gICAgaWYgKGVyIGluc3RhbmNlb2YgRXJyb3IpIHtcbiAgICAgIC8vIE5vdGU6IFRoZSBjb21tZW50cyBvbiB0aGUgYHRocm93YCBsaW5lcyBhcmUgaW50ZW50aW9uYWwsIHRoZXkgc2hvd1xuICAgICAgLy8gdXAgaW4gTm9kZSdzIG91dHB1dCBpZiB0aGlzIHJlc3VsdHMgaW4gYW4gdW5oYW5kbGVkIGV4Y2VwdGlvbi5cbiAgICAgIHRocm93IGVyOyAvLyBVbmhhbmRsZWQgJ2Vycm9yJyBldmVudFxuICAgIH1cbiAgICAvLyBBdCBsZWFzdCBnaXZlIHNvbWUga2luZCBvZiBjb250ZXh0IHRvIHRoZSB1c2VyXG4gICAgdmFyIGVyciA9IG5ldyBFcnJvcignVW5oYW5kbGVkIGVycm9yLicgKyAoZXIgPyAnICgnICsgZXIubWVzc2FnZSArICcpJyA6ICcnKSk7XG4gICAgZXJyLmNvbnRleHQgPSBlcjtcbiAgICB0aHJvdyBlcnI7IC8vIFVuaGFuZGxlZCAnZXJyb3InIGV2ZW50XG4gIH1cblxuICB2YXIgaGFuZGxlciA9IGV2ZW50c1t0eXBlXTtcblxuICBpZiAoaGFuZGxlciA9PT0gdW5kZWZpbmVkKVxuICAgIHJldHVybiBmYWxzZTtcblxuICBpZiAodHlwZW9mIGhhbmRsZXIgPT09ICdmdW5jdGlvbicpIHtcbiAgICBSZWZsZWN0QXBwbHkoaGFuZGxlciwgdGhpcywgYXJncyk7XG4gIH0gZWxzZSB7XG4gICAgdmFyIGxlbiA9IGhhbmRsZXIubGVuZ3RoO1xuICAgIHZhciBsaXN0ZW5lcnMgPSBhcnJheUNsb25lKGhhbmRsZXIsIGxlbik7XG4gICAgZm9yICh2YXIgaSA9IDA7IGkgPCBsZW47ICsraSlcbiAgICAgIFJlZmxlY3RBcHBseShsaXN0ZW5lcnNbaV0sIHRoaXMsIGFyZ3MpO1xuICB9XG5cbiAgcmV0dXJuIHRydWU7XG59O1xuXG5mdW5jdGlvbiBfYWRkTGlzdGVuZXIodGFyZ2V0LCB0eXBlLCBsaXN0ZW5lciwgcHJlcGVuZCkge1xuICB2YXIgbTtcbiAgdmFyIGV2ZW50cztcbiAgdmFyIGV4aXN0aW5nO1xuXG4gIGNoZWNrTGlzdGVuZXIobGlzdGVuZXIpO1xuXG4gIGV2ZW50cyA9IHRhcmdldC5fZXZlbnRzO1xuICBpZiAoZXZlbnRzID09PSB1bmRlZmluZWQpIHtcbiAgICBldmVudHMgPSB0YXJnZXQuX2V2ZW50cyA9IE9iamVjdC5jcmVhdGUobnVsbCk7XG4gICAgdGFyZ2V0Ll9ldmVudHNDb3VudCA9IDA7XG4gIH0gZWxzZSB7XG4gICAgLy8gVG8gYXZvaWQgcmVjdXJzaW9uIGluIHRoZSBjYXNlIHRoYXQgdHlwZSA9PT0gXCJuZXdMaXN0ZW5lclwiISBCZWZvcmVcbiAgICAvLyBhZGRpbmcgaXQgdG8gdGhlIGxpc3RlbmVycywgZmlyc3QgZW1pdCBcIm5ld0xpc3RlbmVyXCIuXG4gICAgaWYgKGV2ZW50cy5uZXdMaXN0ZW5lciAhPT0gdW5kZWZpbmVkKSB7XG4gICAgICB0YXJnZXQuZW1pdCgnbmV3TGlzdGVuZXInLCB0eXBlLFxuICAgICAgICAgICAgICAgICAgbGlzdGVuZXIubGlzdGVuZXIgPyBsaXN0ZW5lci5saXN0ZW5lciA6IGxpc3RlbmVyKTtcblxuICAgICAgLy8gUmUtYXNzaWduIGBldmVudHNgIGJlY2F1c2UgYSBuZXdMaXN0ZW5lciBoYW5kbGVyIGNvdWxkIGhhdmUgY2F1c2VkIHRoZVxuICAgICAgLy8gdGhpcy5fZXZlbnRzIHRvIGJlIGFzc2lnbmVkIHRvIGEgbmV3IG9iamVjdFxuICAgICAgZXZlbnRzID0gdGFyZ2V0Ll9ldmVudHM7XG4gICAgfVxuICAgIGV4aXN0aW5nID0gZXZlbnRzW3R5cGVdO1xuICB9XG5cbiAgaWYgKGV4aXN0aW5nID09PSB1bmRlZmluZWQpIHtcbiAgICAvLyBPcHRpbWl6ZSB0aGUgY2FzZSBvZiBvbmUgbGlzdGVuZXIuIERvbid0IG5lZWQgdGhlIGV4dHJhIGFycmF5IG9iamVjdC5cbiAgICBleGlzdGluZyA9IGV2ZW50c1t0eXBlXSA9IGxpc3RlbmVyO1xuICAgICsrdGFyZ2V0Ll9ldmVudHNDb3VudDtcbiAgfSBlbHNlIHtcbiAgICBpZiAodHlwZW9mIGV4aXN0aW5nID09PSAnZnVuY3Rpb24nKSB7XG4gICAgICAvLyBBZGRpbmcgdGhlIHNlY29uZCBlbGVtZW50LCBuZWVkIHRvIGNoYW5nZSB0byBhcnJheS5cbiAgICAgIGV4aXN0aW5nID0gZXZlbnRzW3R5cGVdID1cbiAgICAgICAgcHJlcGVuZCA/IFtsaXN0ZW5lciwgZXhpc3RpbmddIDogW2V4aXN0aW5nLCBsaXN0ZW5lcl07XG4gICAgICAvLyBJZiB3ZSd2ZSBhbHJlYWR5IGdvdCBhbiBhcnJheSwganVzdCBhcHBlbmQuXG4gICAgfSBlbHNlIGlmIChwcmVwZW5kKSB7XG4gICAgICBleGlzdGluZy51bnNoaWZ0KGxpc3RlbmVyKTtcbiAgICB9IGVsc2Uge1xuICAgICAgZXhpc3RpbmcucHVzaChsaXN0ZW5lcik7XG4gICAgfVxuXG4gICAgLy8gQ2hlY2sgZm9yIGxpc3RlbmVyIGxlYWtcbiAgICBtID0gX2dldE1heExpc3RlbmVycyh0YXJnZXQpO1xuICAgIGlmIChtID4gMCAmJiBleGlzdGluZy5sZW5ndGggPiBtICYmICFleGlzdGluZy53YXJuZWQpIHtcbiAgICAgIGV4aXN0aW5nLndhcm5lZCA9IHRydWU7XG4gICAgICAvLyBObyBlcnJvciBjb2RlIGZvciB0aGlzIHNpbmNlIGl0IGlzIGEgV2FybmluZ1xuICAgICAgLy8gZXNsaW50LWRpc2FibGUtbmV4dC1saW5lIG5vLXJlc3RyaWN0ZWQtc3ludGF4XG4gICAgICB2YXIgdyA9IG5ldyBFcnJvcignUG9zc2libGUgRXZlbnRFbWl0dGVyIG1lbW9yeSBsZWFrIGRldGVjdGVkLiAnICtcbiAgICAgICAgICAgICAgICAgICAgICAgICAgZXhpc3RpbmcubGVuZ3RoICsgJyAnICsgU3RyaW5nKHR5cGUpICsgJyBsaXN0ZW5lcnMgJyArXG4gICAgICAgICAgICAgICAgICAgICAgICAgICdhZGRlZC4gVXNlIGVtaXR0ZXIuc2V0TWF4TGlzdGVuZXJzKCkgdG8gJyArXG4gICAgICAgICAgICAgICAgICAgICAgICAgICdpbmNyZWFzZSBsaW1pdCcpO1xuICAgICAgdy5uYW1lID0gJ01heExpc3RlbmVyc0V4Y2VlZGVkV2FybmluZyc7XG4gICAgICB3LmVtaXR0ZXIgPSB0YXJnZXQ7XG4gICAgICB3LnR5cGUgPSB0eXBlO1xuICAgICAgdy5jb3VudCA9IGV4aXN0aW5nLmxlbmd0aDtcbiAgICAgIFByb2Nlc3NFbWl0V2FybmluZyh3KTtcbiAgICB9XG4gIH1cblxuICByZXR1cm4gdGFyZ2V0O1xufVxuXG5FdmVudEVtaXR0ZXIucHJvdG90eXBlLmFkZExpc3RlbmVyID0gZnVuY3Rpb24gYWRkTGlzdGVuZXIodHlwZSwgbGlzdGVuZXIpIHtcbiAgcmV0dXJuIF9hZGRMaXN0ZW5lcih0aGlzLCB0eXBlLCBsaXN0ZW5lciwgZmFsc2UpO1xufTtcblxuRXZlbnRFbWl0dGVyLnByb3RvdHlwZS5vbiA9IEV2ZW50RW1pdHRlci5wcm90b3R5cGUuYWRkTGlzdGVuZXI7XG5cbkV2ZW50RW1pdHRlci5wcm90b3R5cGUucHJlcGVuZExpc3RlbmVyID1cbiAgICBmdW5jdGlvbiBwcmVwZW5kTGlzdGVuZXIodHlwZSwgbGlzdGVuZXIpIHtcbiAgICAgIHJldHVybiBfYWRkTGlzdGVuZXIodGhpcywgdHlwZSwgbGlzdGVuZXIsIHRydWUpO1xuICAgIH07XG5cbmZ1bmN0aW9uIG9uY2VXcmFwcGVyKCkge1xuICBpZiAoIXRoaXMuZmlyZWQpIHtcbiAgICB0aGlzLnRhcmdldC5yZW1vdmVMaXN0ZW5lcih0aGlzLnR5cGUsIHRoaXMud3JhcEZuKTtcbiAgICB0aGlzLmZpcmVkID0gdHJ1ZTtcbiAgICBpZiAoYXJndW1lbnRzLmxlbmd0aCA9PT0gMClcbiAgICAgIHJldHVybiB0aGlzLmxpc3RlbmVyLmNhbGwodGhpcy50YXJnZXQpO1xuICAgIHJldHVybiB0aGlzLmxpc3RlbmVyLmFwcGx5KHRoaXMudGFyZ2V0LCBhcmd1bWVudHMpO1xuICB9XG59XG5cbmZ1bmN0aW9uIF9vbmNlV3JhcCh0YXJnZXQsIHR5cGUsIGxpc3RlbmVyKSB7XG4gIHZhciBzdGF0ZSA9IHsgZmlyZWQ6IGZhbHNlLCB3cmFwRm46IHVuZGVmaW5lZCwgdGFyZ2V0OiB0YXJnZXQsIHR5cGU6IHR5cGUsIGxpc3RlbmVyOiBsaXN0ZW5lciB9O1xuICB2YXIgd3JhcHBlZCA9IG9uY2VXcmFwcGVyLmJpbmQoc3RhdGUpO1xuICB3cmFwcGVkLmxpc3RlbmVyID0gbGlzdGVuZXI7XG4gIHN0YXRlLndyYXBGbiA9IHdyYXBwZWQ7XG4gIHJldHVybiB3cmFwcGVkO1xufVxuXG5FdmVudEVtaXR0ZXIucHJvdG90eXBlLm9uY2UgPSBmdW5jdGlvbiBvbmNlKHR5cGUsIGxpc3RlbmVyKSB7XG4gIGNoZWNrTGlzdGVuZXIobGlzdGVuZXIpO1xuICB0aGlzLm9uKHR5cGUsIF9vbmNlV3JhcCh0aGlzLCB0eXBlLCBsaXN0ZW5lcikpO1xuICByZXR1cm4gdGhpcztcbn07XG5cbkV2ZW50RW1pdHRlci5wcm90b3R5cGUucHJlcGVuZE9uY2VMaXN0ZW5lciA9XG4gICAgZnVuY3Rpb24gcHJlcGVuZE9uY2VMaXN0ZW5lcih0eXBlLCBsaXN0ZW5lcikge1xuICAgICAgY2hlY2tMaXN0ZW5lcihsaXN0ZW5lcik7XG4gICAgICB0aGlzLnByZXBlbmRMaXN0ZW5lcih0eXBlLCBfb25jZVdyYXAodGhpcywgdHlwZSwgbGlzdGVuZXIpKTtcbiAgICAgIHJldHVybiB0aGlzO1xuICAgIH07XG5cbi8vIEVtaXRzIGEgJ3JlbW92ZUxpc3RlbmVyJyBldmVudCBpZiBhbmQgb25seSBpZiB0aGUgbGlzdGVuZXIgd2FzIHJlbW92ZWQuXG5FdmVudEVtaXR0ZXIucHJvdG90eXBlLnJlbW92ZUxpc3RlbmVyID1cbiAgICBmdW5jdGlvbiByZW1vdmVMaXN0ZW5lcih0eXBlLCBsaXN0ZW5lcikge1xuICAgICAgdmFyIGxpc3QsIGV2ZW50cywgcG9zaXRpb24sIGksIG9yaWdpbmFsTGlzdGVuZXI7XG5cbiAgICAgIGNoZWNrTGlzdGVuZXIobGlzdGVuZXIpO1xuXG4gICAgICBldmVudHMgPSB0aGlzLl9ldmVudHM7XG4gICAgICBpZiAoZXZlbnRzID09PSB1bmRlZmluZWQpXG4gICAgICAgIHJldHVybiB0aGlzO1xuXG4gICAgICBsaXN0ID0gZXZlbnRzW3R5cGVdO1xuICAgICAgaWYgKGxpc3QgPT09IHVuZGVmaW5lZClcbiAgICAgICAgcmV0dXJuIHRoaXM7XG5cbiAgICAgIGlmIChsaXN0ID09PSBsaXN0ZW5lciB8fCBsaXN0Lmxpc3RlbmVyID09PSBsaXN0ZW5lcikge1xuICAgICAgICBpZiAoLS10aGlzLl9ldmVudHNDb3VudCA9PT0gMClcbiAgICAgICAgICB0aGlzLl9ldmVudHMgPSBPYmplY3QuY3JlYXRlKG51bGwpO1xuICAgICAgICBlbHNlIHtcbiAgICAgICAgICBkZWxldGUgZXZlbnRzW3R5cGVdO1xuICAgICAgICAgIGlmIChldmVudHMucmVtb3ZlTGlzdGVuZXIpXG4gICAgICAgICAgICB0aGlzLmVtaXQoJ3JlbW92ZUxpc3RlbmVyJywgdHlwZSwgbGlzdC5saXN0ZW5lciB8fCBsaXN0ZW5lcik7XG4gICAgICAgIH1cbiAgICAgIH0gZWxzZSBpZiAodHlwZW9mIGxpc3QgIT09ICdmdW5jdGlvbicpIHtcbiAgICAgICAgcG9zaXRpb24gPSAtMTtcblxuICAgICAgICBmb3IgKGkgPSBsaXN0Lmxlbmd0aCAtIDE7IGkgPj0gMDsgaS0tKSB7XG4gICAgICAgICAgaWYgKGxpc3RbaV0gPT09IGxpc3RlbmVyIHx8IGxpc3RbaV0ubGlzdGVuZXIgPT09IGxpc3RlbmVyKSB7XG4gICAgICAgICAgICBvcmlnaW5hbExpc3RlbmVyID0gbGlzdFtpXS5saXN0ZW5lcjtcbiAgICAgICAgICAgIHBvc2l0aW9uID0gaTtcbiAgICAgICAgICAgIGJyZWFrO1xuICAgICAgICAgIH1cbiAgICAgICAgfVxuXG4gICAgICAgIGlmIChwb3NpdGlvbiA8IDApXG4gICAgICAgICAgcmV0dXJuIHRoaXM7XG5cbiAgICAgICAgaWYgKHBvc2l0aW9uID09PSAwKVxuICAgICAgICAgIGxpc3Quc2hpZnQoKTtcbiAgICAgICAgZWxzZSB7XG4gICAgICAgICAgc3BsaWNlT25lKGxpc3QsIHBvc2l0aW9uKTtcbiAgICAgICAgfVxuXG4gICAgICAgIGlmIChsaXN0Lmxlbmd0aCA9PT0gMSlcbiAgICAgICAgICBldmVudHNbdHlwZV0gPSBsaXN0WzBdO1xuXG4gICAgICAgIGlmIChldmVudHMucmVtb3ZlTGlzdGVuZXIgIT09IHVuZGVmaW5lZClcbiAgICAgICAgICB0aGlzLmVtaXQoJ3JlbW92ZUxpc3RlbmVyJywgdHlwZSwgb3JpZ2luYWxMaXN0ZW5lciB8fCBsaXN0ZW5lcik7XG4gICAgICB9XG5cbiAgICAgIHJldHVybiB0aGlzO1xuICAgIH07XG5cbkV2ZW50RW1pdHRlci5wcm90b3R5cGUub2ZmID0gRXZlbnRFbWl0dGVyLnByb3RvdHlwZS5yZW1vdmVMaXN0ZW5lcjtcblxuRXZlbnRFbWl0dGVyLnByb3RvdHlwZS5yZW1vdmVBbGxMaXN0ZW5lcnMgPVxuICAgIGZ1bmN0aW9uIHJlbW92ZUFsbExpc3RlbmVycyh0eXBlKSB7XG4gICAgICB2YXIgbGlzdGVuZXJzLCBldmVudHMsIGk7XG5cbiAgICAgIGV2ZW50cyA9IHRoaXMuX2V2ZW50cztcbiAgICAgIGlmIChldmVudHMgPT09IHVuZGVmaW5lZClcbiAgICAgICAgcmV0dXJuIHRoaXM7XG5cbiAgICAgIC8vIG5vdCBsaXN0ZW5pbmcgZm9yIHJlbW92ZUxpc3RlbmVyLCBubyBuZWVkIHRvIGVtaXRcbiAgICAgIGlmIChldmVudHMucmVtb3ZlTGlzdGVuZXIgPT09IHVuZGVmaW5lZCkge1xuICAgICAgICBpZiAoYXJndW1lbnRzLmxlbmd0aCA9PT0gMCkge1xuICAgICAgICAgIHRoaXMuX2V2ZW50cyA9IE9iamVjdC5jcmVhdGUobnVsbCk7XG4gICAgICAgICAgdGhpcy5fZXZlbnRzQ291bnQgPSAwO1xuICAgICAgICB9IGVsc2UgaWYgKGV2ZW50c1t0eXBlXSAhPT0gdW5kZWZpbmVkKSB7XG4gICAgICAgICAgaWYgKC0tdGhpcy5fZXZlbnRzQ291bnQgPT09IDApXG4gICAgICAgICAgICB0aGlzLl9ldmVudHMgPSBPYmplY3QuY3JlYXRlKG51bGwpO1xuICAgICAgICAgIGVsc2VcbiAgICAgICAgICAgIGRlbGV0ZSBldmVudHNbdHlwZV07XG4gICAgICAgIH1cbiAgICAgICAgcmV0dXJuIHRoaXM7XG4gICAgICB9XG5cbiAgICAgIC8vIGVtaXQgcmVtb3ZlTGlzdGVuZXIgZm9yIGFsbCBsaXN0ZW5lcnMgb24gYWxsIGV2ZW50c1xuICAgICAgaWYgKGFyZ3VtZW50cy5sZW5ndGggPT09IDApIHtcbiAgICAgICAgdmFyIGtleXMgPSBPYmplY3Qua2V5cyhldmVudHMpO1xuICAgICAgICB2YXIga2V5O1xuICAgICAgICBmb3IgKGkgPSAwOyBpIDwga2V5cy5sZW5ndGg7ICsraSkge1xuICAgICAgICAgIGtleSA9IGtleXNbaV07XG4gICAgICAgICAgaWYgKGtleSA9PT0gJ3JlbW92ZUxpc3RlbmVyJykgY29udGludWU7XG4gICAgICAgICAgdGhpcy5yZW1vdmVBbGxMaXN0ZW5lcnMoa2V5KTtcbiAgICAgICAgfVxuICAgICAgICB0aGlzLnJlbW92ZUFsbExpc3RlbmVycygncmVtb3ZlTGlzdGVuZXInKTtcbiAgICAgICAgdGhpcy5fZXZlbnRzID0gT2JqZWN0LmNyZWF0ZShudWxsKTtcbiAgICAgICAgdGhpcy5fZXZlbnRzQ291bnQgPSAwO1xuICAgICAgICByZXR1cm4gdGhpcztcbiAgICAgIH1cblxuICAgICAgbGlzdGVuZXJzID0gZXZlbnRzW3R5cGVdO1xuXG4gICAgICBpZiAodHlwZW9mIGxpc3RlbmVycyA9PT0gJ2Z1bmN0aW9uJykge1xuICAgICAgICB0aGlzLnJlbW92ZUxpc3RlbmVyKHR5cGUsIGxpc3RlbmVycyk7XG4gICAgICB9IGVsc2UgaWYgKGxpc3RlbmVycyAhPT0gdW5kZWZpbmVkKSB7XG4gICAgICAgIC8vIExJRk8gb3JkZXJcbiAgICAgICAgZm9yIChpID0gbGlzdGVuZXJzLmxlbmd0aCAtIDE7IGkgPj0gMDsgaS0tKSB7XG4gICAgICAgICAgdGhpcy5yZW1vdmVMaXN0ZW5lcih0eXBlLCBsaXN0ZW5lcnNbaV0pO1xuICAgICAgICB9XG4gICAgICB9XG5cbiAgICAgIHJldHVybiB0aGlzO1xuICAgIH07XG5cbmZ1bmN0aW9uIF9saXN0ZW5lcnModGFyZ2V0LCB0eXBlLCB1bndyYXApIHtcbiAgdmFyIGV2ZW50cyA9IHRhcmdldC5fZXZlbnRzO1xuXG4gIGlmIChldmVudHMgPT09IHVuZGVmaW5lZClcbiAgICByZXR1cm4gW107XG5cbiAgdmFyIGV2bGlzdGVuZXIgPSBldmVudHNbdHlwZV07XG4gIGlmIChldmxpc3RlbmVyID09PSB1bmRlZmluZWQpXG4gICAgcmV0dXJuIFtdO1xuXG4gIGlmICh0eXBlb2YgZXZsaXN0ZW5lciA9PT0gJ2Z1bmN0aW9uJylcbiAgICByZXR1cm4gdW53cmFwID8gW2V2bGlzdGVuZXIubGlzdGVuZXIgfHwgZXZsaXN0ZW5lcl0gOiBbZXZsaXN0ZW5lcl07XG5cbiAgcmV0dXJuIHVud3JhcCA/XG4gICAgdW53cmFwTGlzdGVuZXJzKGV2bGlzdGVuZXIpIDogYXJyYXlDbG9uZShldmxpc3RlbmVyLCBldmxpc3RlbmVyLmxlbmd0aCk7XG59XG5cbkV2ZW50RW1pdHRlci5wcm90b3R5cGUubGlzdGVuZXJzID0gZnVuY3Rpb24gbGlzdGVuZXJzKHR5cGUpIHtcbiAgcmV0dXJuIF9saXN0ZW5lcnModGhpcywgdHlwZSwgdHJ1ZSk7XG59O1xuXG5FdmVudEVtaXR0ZXIucHJvdG90eXBlLnJhd0xpc3RlbmVycyA9IGZ1bmN0aW9uIHJhd0xpc3RlbmVycyh0eXBlKSB7XG4gIHJldHVybiBfbGlzdGVuZXJzKHRoaXMsIHR5cGUsIGZhbHNlKTtcbn07XG5cbkV2ZW50RW1pdHRlci5saXN0ZW5lckNvdW50ID0gZnVuY3Rpb24oZW1pdHRlciwgdHlwZSkge1xuICBpZiAodHlwZW9mIGVtaXR0ZXIubGlzdGVuZXJDb3VudCA9PT0gJ2Z1bmN0aW9uJykge1xuICAgIHJldHVybiBlbWl0dGVyLmxpc3RlbmVyQ291bnQodHlwZSk7XG4gIH0gZWxzZSB7XG4gICAgcmV0dXJuIGxpc3RlbmVyQ291bnQuY2FsbChlbWl0dGVyLCB0eXBlKTtcbiAgfVxufTtcblxuRXZlbnRFbWl0dGVyLnByb3RvdHlwZS5saXN0ZW5lckNvdW50ID0gbGlzdGVuZXJDb3VudDtcbmZ1bmN0aW9uIGxpc3RlbmVyQ291bnQodHlwZSkge1xuICB2YXIgZXZlbnRzID0gdGhpcy5fZXZlbnRzO1xuXG4gIGlmIChldmVudHMgIT09IHVuZGVmaW5lZCkge1xuICAgIHZhciBldmxpc3RlbmVyID0gZXZlbnRzW3R5cGVdO1xuXG4gICAgaWYgKHR5cGVvZiBldmxpc3RlbmVyID09PSAnZnVuY3Rpb24nKSB7XG4gICAgICByZXR1cm4gMTtcbiAgICB9IGVsc2UgaWYgKGV2bGlzdGVuZXIgIT09IHVuZGVmaW5lZCkge1xuICAgICAgcmV0dXJuIGV2bGlzdGVuZXIubGVuZ3RoO1xuICAgIH1cbiAgfVxuXG4gIHJldHVybiAwO1xufVxuXG5FdmVudEVtaXR0ZXIucHJvdG90eXBlLmV2ZW50TmFtZXMgPSBmdW5jdGlvbiBldmVudE5hbWVzKCkge1xuICByZXR1cm4gdGhpcy5fZXZlbnRzQ291bnQgPiAwID8gUmVmbGVjdE93bktleXModGhpcy5fZXZlbnRzKSA6IFtdO1xufTtcblxuZnVuY3Rpb24gYXJyYXlDbG9uZShhcnIsIG4pIHtcbiAgdmFyIGNvcHkgPSBuZXcgQXJyYXkobik7XG4gIGZvciAodmFyIGkgPSAwOyBpIDwgbjsgKytpKVxuICAgIGNvcHlbaV0gPSBhcnJbaV07XG4gIHJldHVybiBjb3B5O1xufVxuXG5mdW5jdGlvbiBzcGxpY2VPbmUobGlzdCwgaW5kZXgpIHtcbiAgZm9yICg7IGluZGV4ICsgMSA8IGxpc3QubGVuZ3RoOyBpbmRleCsrKVxuICAgIGxpc3RbaW5kZXhdID0gbGlzdFtpbmRleCArIDFdO1xuICBsaXN0LnBvcCgpO1xufVxuXG5mdW5jdGlvbiB1bndyYXBMaXN0ZW5lcnMoYXJyKSB7XG4gIHZhciByZXQgPSBuZXcgQXJyYXkoYXJyLmxlbmd0aCk7XG4gIGZvciAodmFyIGkgPSAwOyBpIDwgcmV0Lmxlbmd0aDsgKytpKSB7XG4gICAgcmV0W2ldID0gYXJyW2ldLmxpc3RlbmVyIHx8IGFycltpXTtcbiAgfVxuICByZXR1cm4gcmV0O1xufVxuXG5mdW5jdGlvbiBvbmNlKGVtaXR0ZXIsIG5hbWUpIHtcbiAgcmV0dXJuIG5ldyBQcm9taXNlKGZ1bmN0aW9uIChyZXNvbHZlLCByZWplY3QpIHtcbiAgICBmdW5jdGlvbiBlcnJvckxpc3RlbmVyKGVycikge1xuICAgICAgZW1pdHRlci5yZW1vdmVMaXN0ZW5lcihuYW1lLCByZXNvbHZlcik7XG4gICAgICByZWplY3QoZXJyKTtcbiAgICB9XG5cbiAgICBmdW5jdGlvbiByZXNvbHZlcigpIHtcbiAgICAgIGlmICh0eXBlb2YgZW1pdHRlci5yZW1vdmVMaXN0ZW5lciA9PT0gJ2Z1bmN0aW9uJykge1xuICAgICAgICBlbWl0dGVyLnJlbW92ZUxpc3RlbmVyKCdlcnJvcicsIGVycm9yTGlzdGVuZXIpO1xuICAgICAgfVxuICAgICAgcmVzb2x2ZShbXS5zbGljZS5jYWxsKGFyZ3VtZW50cykpO1xuICAgIH07XG5cbiAgICBldmVudFRhcmdldEFnbm9zdGljQWRkTGlzdGVuZXIoZW1pdHRlciwgbmFtZSwgcmVzb2x2ZXIsIHsgb25jZTogdHJ1ZSB9KTtcbiAgICBpZiAobmFtZSAhPT0gJ2Vycm9yJykge1xuICAgICAgYWRkRXJyb3JIYW5kbGVySWZFdmVudEVtaXR0ZXIoZW1pdHRlciwgZXJyb3JMaXN0ZW5lciwgeyBvbmNlOiB0cnVlIH0pO1xuICAgIH1cbiAgfSk7XG59XG5cbmZ1bmN0aW9uIGFkZEVycm9ySGFuZGxlcklmRXZlbnRFbWl0dGVyKGVtaXR0ZXIsIGhhbmRsZXIsIGZsYWdzKSB7XG4gIGlmICh0eXBlb2YgZW1pdHRlci5vbiA9PT0gJ2Z1bmN0aW9uJykge1xuICAgIGV2ZW50VGFyZ2V0QWdub3N0aWNBZGRMaXN0ZW5lcihlbWl0dGVyLCAnZXJyb3InLCBoYW5kbGVyLCBmbGFncyk7XG4gIH1cbn1cblxuZnVuY3Rpb24gZXZlbnRUYXJnZXRBZ25vc3RpY0FkZExpc3RlbmVyKGVtaXR0ZXIsIG5hbWUsIGxpc3RlbmVyLCBmbGFncykge1xuICBpZiAodHlwZW9mIGVtaXR0ZXIub24gPT09ICdmdW5jdGlvbicpIHtcbiAgICBpZiAoZmxhZ3Mub25jZSkge1xuICAgICAgZW1pdHRlci5vbmNlKG5hbWUsIGxpc3RlbmVyKTtcbiAgICB9IGVsc2Uge1xuICAgICAgZW1pdHRlci5vbihuYW1lLCBsaXN0ZW5lcik7XG4gICAgfVxuICB9IGVsc2UgaWYgKHR5cGVvZiBlbWl0dGVyLmFkZEV2ZW50TGlzdGVuZXIgPT09ICdmdW5jdGlvbicpIHtcbiAgICAvLyBFdmVudFRhcmdldCBkb2VzIG5vdCBoYXZlIGBlcnJvcmAgZXZlbnQgc2VtYW50aWNzIGxpa2UgTm9kZVxuICAgIC8vIEV2ZW50RW1pdHRlcnMsIHdlIGRvIG5vdCBsaXN0ZW4gZm9yIGBlcnJvcmAgZXZlbnRzIGhlcmUuXG4gICAgZW1pdHRlci5hZGRFdmVudExpc3RlbmVyKG5hbWUsIGZ1bmN0aW9uIHdyYXBMaXN0ZW5lcihhcmcpIHtcbiAgICAgIC8vIElFIGRvZXMgbm90IGhhdmUgYnVpbHRpbiBgeyBvbmNlOiB0cnVlIH1gIHN1cHBvcnQgc28gd2VcbiAgICAgIC8vIGhhdmUgdG8gZG8gaXQgbWFudWFsbHkuXG4gICAgICBpZiAoZmxhZ3Mub25jZSkge1xuICAgICAgICBlbWl0dGVyLnJlbW92ZUV2ZW50TGlzdGVuZXIobmFtZSwgd3JhcExpc3RlbmVyKTtcbiAgICAgIH1cbiAgICAgIGxpc3RlbmVyKGFyZyk7XG4gICAgfSk7XG4gIH0gZWxzZSB7XG4gICAgdGhyb3cgbmV3IFR5cGVFcnJvcignVGhlIFwiZW1pdHRlclwiIGFyZ3VtZW50IG11c3QgYmUgb2YgdHlwZSBFdmVudEVtaXR0ZXIuIFJlY2VpdmVkIHR5cGUgJyArIHR5cGVvZiBlbWl0dGVyKTtcbiAgfVxufVxuXG5cblxuLy8vLy8vLy8vLy8vLy8vLy8vXG4vLyBXRUJQQUNLIEZPT1RFUlxuLy8gLi9ub2RlX21vZHVsZXMvZXZlbnRzL2V2ZW50cy5qc1xuLy8gbW9kdWxlIGlkID0gMjBcbi8vIG1vZHVsZSBjaHVua3MgPSAwIDEiLCIvKiBlc2xpbnQgcmVhY3QvcHJvcC10eXBlczogMCAqL1xuLyogZXNsaW50IHJlYWN0L3JlcXVpcmUtZGVmYXVsdC1wcm9wczogMCAqL1xuLyogZXNsaW50IG5vLWxvbmVseS1pZjogMCAqL1xuaW1wb3J0IFJlYWN0IGZyb20gJ3JlYWN0JztcbmltcG9ydCBQcm9wVHlwZXMgZnJvbSAncHJvcC10eXBlcyc7XG5cbmltcG9ydCBDb25zdCBmcm9tICcuL2NvbnN0JztcbmltcG9ydCBQYWdpbmF0aW9uIGZyb20gJy4vcGFnaW5hdGlvbic7XG5pbXBvcnQgeyBnZXRCeUN1cnJQYWdlLCBhbGlnblBhZ2UgfSBmcm9tICcuL3BhZ2UnO1xuaW1wb3J0IGNyZWF0ZUJhc2VDb250ZXh0IGZyb20gJy4vc3RhdGUtY29udGV4dCc7XG5cbmNvbnN0IHsgUHJvdmlkZXIgfSA9IGNyZWF0ZUJhc2VDb250ZXh0KCk7XG5cbmNvbnN0IFBhZ2luYXRpb25EYXRhQ29udGV4dCA9IFJlYWN0LmNyZWF0ZUNvbnRleHQoKTtcblxuY2xhc3MgUGFnaW5hdGlvbkRhdGFQcm92aWRlciBleHRlbmRzIFByb3ZpZGVyIHtcbiAgc3RhdGljIHByb3BUeXBlcyA9IHtcbiAgICBkYXRhOiBQcm9wVHlwZXMuYXJyYXkuaXNSZXF1aXJlZCxcbiAgICByZW1vdGVFbWl0dGVyOiBQcm9wVHlwZXMub2JqZWN0LmlzUmVxdWlyZWQsXG4gICAgaXNSZW1vdGVQYWdpbmF0aW9uOiBQcm9wVHlwZXMuZnVuYy5pc1JlcXVpcmVkXG4gIH1cblxuICAvLyBlc2xpbnQtZGlzYWJsZS1uZXh0LWxpbmUgY2FtZWxjYXNlLCByZWFjdC9zb3J0LWNvbXBcbiAgVU5TQUZFX2NvbXBvbmVudFdpbGxSZWNlaXZlUHJvcHMobmV4dFByb3BzKSB7XG4gICAgc3VwZXIuVU5TQUZFX2NvbXBvbmVudFdpbGxSZWNlaXZlUHJvcHMobmV4dFByb3BzKTtcbiAgICBjb25zdCB7IGN1cnJTaXplUGVyUGFnZSB9ID0gdGhpcztcbiAgICBjb25zdCB7IGN1c3RvbSwgb25QYWdlQ2hhbmdlIH0gPSBuZXh0UHJvcHMucGFnaW5hdGlvbi5vcHRpb25zO1xuXG4gICAgY29uc3QgcGFnZVN0YXJ0SW5kZXggPSB0eXBlb2YgbmV4dFByb3BzLnBhZ2luYXRpb24ub3B0aW9ucy5wYWdlU3RhcnRJbmRleCAhPT0gJ3VuZGVmaW5lZCcgP1xuICAgICAgbmV4dFByb3BzLnBhZ2luYXRpb24ub3B0aW9ucy5wYWdlU3RhcnRJbmRleCA6IENvbnN0LlBBR0VfU1RBUlRfSU5ERVg7XG5cbiAgICAvLyB1c2VyIHNob3VsZCBhbGlnbiB0aGUgcGFnZSB3aGVuIHRoZSBwYWdlIGlzIG5vdCBmaXQgdG8gdGhlIGRhdGEgc2l6ZSB3aGVuIHJlbW90ZSBlbmFibGVcbiAgICBpZiAoIXRoaXMuaXNSZW1vdGVQYWdpbmF0aW9uKCkgJiYgIWN1c3RvbSkge1xuICAgICAgY29uc3QgbmV3UGFnZSA9IGFsaWduUGFnZShcbiAgICAgICAgbmV4dFByb3BzLmRhdGEubGVuZ3RoLFxuICAgICAgICB0aGlzLnByb3BzLmRhdGEubGVuZ3RoLFxuICAgICAgICB0aGlzLmN1cnJQYWdlLFxuICAgICAgICBjdXJyU2l6ZVBlclBhZ2UsXG4gICAgICAgIHBhZ2VTdGFydEluZGV4XG4gICAgICApO1xuXG4gICAgICBpZiAodGhpcy5jdXJyUGFnZSAhPT0gbmV3UGFnZSkge1xuICAgICAgICBpZiAob25QYWdlQ2hhbmdlKSB7XG4gICAgICAgICAgb25QYWdlQ2hhbmdlKG5ld1BhZ2UsIGN1cnJTaXplUGVyUGFnZSk7XG4gICAgICAgIH1cbiAgICAgICAgdGhpcy5jdXJyUGFnZSA9IG5ld1BhZ2U7XG4gICAgICB9XG4gICAgfVxuICAgIGlmIChuZXh0UHJvcHMub25EYXRhU2l6ZUNoYW5nZSAmJiBuZXh0UHJvcHMuZGF0YS5sZW5ndGggIT09IHRoaXMucHJvcHMuZGF0YS5sZW5ndGgpIHtcbiAgICAgIG5leHRQcm9wcy5vbkRhdGFTaXplQ2hhbmdlKHsgZGF0YVNpemU6IG5leHRQcm9wcy5kYXRhLmxlbmd0aCB9KTtcbiAgICB9XG4gIH1cblxuICBpc1JlbW90ZVBhZ2luYXRpb24gPSAoKSA9PiB0aGlzLnByb3BzLmlzUmVtb3RlUGFnaW5hdGlvbigpO1xuXG4gIHJlbmRlckRlZmF1bHRQYWdpbmF0aW9uID0gKCkgPT4ge1xuICAgIGlmICghdGhpcy5wcm9wcy5wYWdpbmF0aW9uLm9wdGlvbnMuY3VzdG9tKSB7XG4gICAgICBjb25zdCB7XG4gICAgICAgIHBhZ2U6IGN1cnJQYWdlLFxuICAgICAgICBzaXplUGVyUGFnZTogY3VyclNpemVQZXJQYWdlLFxuICAgICAgICBkYXRhU2l6ZSxcbiAgICAgICAgLi4ucmVzdFxuICAgICAgfSA9IHRoaXMuZ2V0UGFnaW5hdGlvblByb3BzKCk7XG4gICAgICByZXR1cm4gKFxuICAgICAgICA8UGFnaW5hdGlvblxuICAgICAgICAgIHsgLi4ucmVzdCB9XG4gICAgICAgICAga2V5PVwicGFnaW5hdGlvblwiXG4gICAgICAgICAgZGF0YVNpemU9eyBkYXRhU2l6ZSB8fCB0aGlzLnByb3BzLmRhdGEubGVuZ3RoIH1cbiAgICAgICAgICBjdXJyUGFnZT17IGN1cnJQYWdlIH1cbiAgICAgICAgICBjdXJyU2l6ZVBlclBhZ2U9eyBjdXJyU2l6ZVBlclBhZ2UgfVxuICAgICAgICAvPlxuICAgICAgKTtcbiAgICB9XG4gICAgcmV0dXJuIG51bGw7XG4gIH1cblxuICByZW5kZXIoKSB7XG4gICAgbGV0IHsgZGF0YSB9ID0gdGhpcy5wcm9wcztcbiAgICBjb25zdCB7IHBhZ2luYXRpb246IHsgb3B0aW9ucyB9IH0gPSB0aGlzLnByb3BzO1xuICAgIGNvbnN0IHsgY3VyclBhZ2UsIGN1cnJTaXplUGVyUGFnZSB9ID0gdGhpcztcbiAgICBjb25zdCBwYWdlU3RhcnRJbmRleCA9IHR5cGVvZiBvcHRpb25zLnBhZ2VTdGFydEluZGV4ID09PSAndW5kZWZpbmVkJyA/XG4gICAgICBDb25zdC5QQUdFX1NUQVJUX0lOREVYIDogb3B0aW9ucy5wYWdlU3RhcnRJbmRleDtcblxuICAgIGRhdGEgPSB0aGlzLmlzUmVtb3RlUGFnaW5hdGlvbigpID9cbiAgICAgIGRhdGEgOlxuICAgICAgZ2V0QnlDdXJyUGFnZShcbiAgICAgICAgZGF0YSxcbiAgICAgICAgY3VyclBhZ2UsXG4gICAgICAgIGN1cnJTaXplUGVyUGFnZSxcbiAgICAgICAgcGFnZVN0YXJ0SW5kZXhcbiAgICAgICk7XG5cbiAgICByZXR1cm4gKFxuICAgICAgPFBhZ2luYXRpb25EYXRhQ29udGV4dC5Qcm92aWRlciB2YWx1ZT17IHsgZGF0YSwgc2V0UmVtb3RlRW1pdHRlcjogdGhpcy5zZXRSZW1vdGVFbWl0dGVyIH0gfT5cbiAgICAgICAgeyB0aGlzLnByb3BzLmNoaWxkcmVuIH1cbiAgICAgICAgeyB0aGlzLnJlbmRlckRlZmF1bHRQYWdpbmF0aW9uKCkgfVxuICAgICAgPC9QYWdpbmF0aW9uRGF0YUNvbnRleHQuUHJvdmlkZXI+XG4gICAgKTtcbiAgfVxufVxuXG5leHBvcnQgZGVmYXVsdCAoKSA9PiAoe1xuICBQcm92aWRlcjogUGFnaW5hdGlvbkRhdGFQcm92aWRlcixcbiAgQ29uc3VtZXI6IFBhZ2luYXRpb25EYXRhQ29udGV4dC5Db25zdW1lclxufSk7XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvZGF0YS1jb250ZXh0LmpzIiwiLyogZXNsaW50IHJlYWN0L3JlcXVpcmUtZGVmYXVsdC1wcm9wczogMCAqL1xuLyogZXNsaW50IGFycm93LWJvZHktc3R5bGU6IDAgKi9cbmltcG9ydCBjcyBmcm9tICdjbGFzc25hbWVzJztcbmltcG9ydCBSZWFjdCwgeyBDb21wb25lbnQgfSBmcm9tICdyZWFjdCc7XG5pbXBvcnQgUHJvcFR5cGVzIGZyb20gJ3Byb3AtdHlwZXMnO1xuaW1wb3J0IHBhZ2VSZXNvbHZlciBmcm9tICcuL3BhZ2UtcmVzb2x2ZXInO1xuaW1wb3J0IHBhZ2luYXRpb25IYW5kbGVyIGZyb20gJy4vcGFnaW5hdGlvbi1oYW5kbGVyJztcbmltcG9ydCB7IFNpemVQZXJQYWdlRHJvcGRvd25XaXRoQWRhcHRlciB9IGZyb20gJy4vc2l6ZS1wZXItcGFnZS1kcm9wZG93bi1hZGFwdGVyJztcbmltcG9ydCB7IFBhZ2luYXRpb25MaXN0V2l0aEFkYXB0ZXIgfSBmcm9tICcuL3BhZ2luYXRpb24tbGlzdC1hZGFwdGVyJztcbmltcG9ydCB7IFBhZ2luYXRpb25Ub3RhbFdpdGhBZGFwdGVyIH0gZnJvbSAnLi9wYWdpbmF0aW9uLXRvdGFsLWFkYXB0ZXInO1xuaW1wb3J0IENvbnN0IGZyb20gJy4vY29uc3QnO1xuXG5jbGFzcyBQYWdpbmF0aW9uIGV4dGVuZHMgcGFnZVJlc29sdmVyKENvbXBvbmVudCkge1xuICByZW5kZXIoKSB7XG4gICAgY29uc3Qge1xuICAgICAgdGFibGVJZCxcbiAgICAgIGN1cnJQYWdlLFxuICAgICAgcGFnZVN0YXJ0SW5kZXgsXG4gICAgICBzaG93VG90YWwsXG4gICAgICBkYXRhU2l6ZSxcbiAgICAgIHBhZ2VMaXN0UmVuZGVyZXIsXG4gICAgICBwYWdlQnV0dG9uUmVuZGVyZXIsXG4gICAgICBwYWdpbmF0aW9uVG90YWxSZW5kZXJlcixcbiAgICAgIGhpZGVQYWdlTGlzdE9ubHlPbmVQYWdlLFxuICAgICAgdG90YWxQYWdlcyxcbiAgICAgIGxhc3RQYWdlLFxuICAgICAgb25QYWdlQ2hhbmdlLFxuICAgICAgc2l6ZVBlclBhZ2VMaXN0LFxuICAgICAgY3VyclNpemVQZXJQYWdlLFxuICAgICAgaGlkZVNpemVQZXJQYWdlLFxuICAgICAgc2l6ZVBlclBhZ2VSZW5kZXJlcixcbiAgICAgIHNpemVQZXJQYWdlT3B0aW9uUmVuZGVyZXIsXG4gICAgICBvblNpemVQZXJQYWdlQ2hhbmdlLFxuICAgICAgYm9vdHN0cmFwNCxcbiAgICAgIC4uLnJlc3RcbiAgICB9ID0gdGhpcy5wcm9wcztcblxuICAgIGNvbnN0IHBhZ2VzID0gdGhpcy5jYWxjdWxhdGVQYWdlU3RhdHVzKHRoaXMuY2FsY3VsYXRlUGFnZXModG90YWxQYWdlcywgbGFzdFBhZ2UpLCBsYXN0UGFnZSk7XG4gICAgY29uc3QgcGFnZUxpc3RDbGFzcyA9IGNzKFxuICAgICAgJ3JlYWN0LWJvb3RzdHJhcC10YWJsZS1wYWdpbmF0aW9uLWxpc3QnLFxuICAgICAgJ2NvbC1tZC02IGNvbC14cy02IGNvbC1zbS02IGNvbC1sZy02Jywge1xuICAgICAgICAncmVhY3QtYm9vdHN0cmFwLXRhYmxlLXBhZ2luYXRpb24tbGlzdC1oaWRkZW4nOiAoaGlkZVBhZ2VMaXN0T25seU9uZVBhZ2UgJiYgdG90YWxQYWdlcyA9PT0gMSlcbiAgICAgIH0pO1xuICAgIHJldHVybiAoXG4gICAgICA8ZGl2IGNsYXNzTmFtZT1cInJvdyByZWFjdC1ib290c3RyYXAtdGFibGUtcGFnaW5hdGlvblwiPlxuICAgICAgICA8ZGl2IGNsYXNzTmFtZT1cImNvbC1tZC02IGNvbC14cy02IGNvbC1zbS02IGNvbC1sZy02XCI+XG4gICAgICAgICAgPFNpemVQZXJQYWdlRHJvcGRvd25XaXRoQWRhcHRlclxuICAgICAgICAgICAgYm9vdHN0cmFwND17IGJvb3RzdHJhcDQgfVxuICAgICAgICAgICAgdGFibGVJZD17IHRhYmxlSWQgfVxuICAgICAgICAgICAgc2l6ZVBlclBhZ2VMaXN0PXsgc2l6ZVBlclBhZ2VMaXN0IH1cbiAgICAgICAgICAgIGN1cnJTaXplUGVyUGFnZT17IGN1cnJTaXplUGVyUGFnZSB9XG4gICAgICAgICAgICBoaWRlU2l6ZVBlclBhZ2U9eyBoaWRlU2l6ZVBlclBhZ2UgfVxuICAgICAgICAgICAgc2l6ZVBlclBhZ2VSZW5kZXJlcj17IHNpemVQZXJQYWdlUmVuZGVyZXIgfVxuICAgICAgICAgICAgc2l6ZVBlclBhZ2VPcHRpb25SZW5kZXJlcj17IHNpemVQZXJQYWdlT3B0aW9uUmVuZGVyZXIgfVxuICAgICAgICAgICAgb25TaXplUGVyUGFnZUNoYW5nZT17IG9uU2l6ZVBlclBhZ2VDaGFuZ2UgfVxuICAgICAgICAgIC8+XG4gICAgICAgICAge1xuICAgICAgICAgICAgc2hvd1RvdGFsID9cbiAgICAgICAgICAgICAgPFBhZ2luYXRpb25Ub3RhbFdpdGhBZGFwdGVyXG4gICAgICAgICAgICAgICAgY3VyclBhZ2U9eyBjdXJyUGFnZSB9XG4gICAgICAgICAgICAgICAgY3VyclNpemVQZXJQYWdlPXsgY3VyclNpemVQZXJQYWdlIH1cbiAgICAgICAgICAgICAgICBwYWdlU3RhcnRJbmRleD17IHBhZ2VTdGFydEluZGV4IH1cbiAgICAgICAgICAgICAgICBkYXRhU2l6ZT17IGRhdGFTaXplIH1cbiAgICAgICAgICAgICAgICBwYWdpbmF0aW9uVG90YWxSZW5kZXJlcj17IHBhZ2luYXRpb25Ub3RhbFJlbmRlcmVyIH1cbiAgICAgICAgICAgICAgLz4gOiBudWxsXG4gICAgICAgICAgfVxuICAgICAgICA8L2Rpdj5cbiAgICAgICAge1xuICAgICAgICAgIHBhZ2VMaXN0UmVuZGVyZXIgPyBwYWdlTGlzdFJlbmRlcmVyKHtcbiAgICAgICAgICAgIHBhZ2VzLFxuICAgICAgICAgICAgb25QYWdlQ2hhbmdlXG4gICAgICAgICAgfSkgOiAoXG4gICAgICAgICAgICA8ZGl2IGNsYXNzTmFtZT17IHBhZ2VMaXN0Q2xhc3MgfT5cbiAgICAgICAgICAgICAgPFBhZ2luYXRpb25MaXN0V2l0aEFkYXB0ZXJcbiAgICAgICAgICAgICAgICB7IC4uLnJlc3QgfVxuICAgICAgICAgICAgICAgIGN1cnJQYWdlPXsgY3VyclBhZ2UgfVxuICAgICAgICAgICAgICAgIGN1cnJTaXplUGVyUGFnZT17IGN1cnJTaXplUGVyUGFnZSB9XG4gICAgICAgICAgICAgICAgcGFnZVN0YXJ0SW5kZXg9eyBwYWdlU3RhcnRJbmRleCB9XG4gICAgICAgICAgICAgICAgbGFzdFBhZ2U9eyBsYXN0UGFnZSB9XG4gICAgICAgICAgICAgICAgdG90YWxQYWdlcz17IHRvdGFsUGFnZXMgfVxuICAgICAgICAgICAgICAgIHBhZ2VCdXR0b25SZW5kZXJlcj17IHBhZ2VCdXR0b25SZW5kZXJlciB9XG4gICAgICAgICAgICAgICAgb25QYWdlQ2hhbmdlPXsgb25QYWdlQ2hhbmdlIH1cbiAgICAgICAgICAgICAgLz5cbiAgICAgICAgICAgIDwvZGl2PlxuICAgICAgICAgIClcbiAgICAgICAgfVxuICAgICAgPC9kaXY+XG4gICAgKTtcbiAgfVxufVxuXG5QYWdpbmF0aW9uLnByb3BUeXBlcyA9IHtcbiAgZGF0YVNpemU6IFByb3BUeXBlcy5udW1iZXIuaXNSZXF1aXJlZCxcbiAgc2l6ZVBlclBhZ2VMaXN0OiBQcm9wVHlwZXMuYXJyYXkuaXNSZXF1aXJlZCxcbiAgY3VyclBhZ2U6IFByb3BUeXBlcy5udW1iZXIuaXNSZXF1aXJlZCxcbiAgY3VyclNpemVQZXJQYWdlOiBQcm9wVHlwZXMubnVtYmVyLmlzUmVxdWlyZWQsXG4gIG9uUGFnZUNoYW5nZTogUHJvcFR5cGVzLmZ1bmMuaXNSZXF1aXJlZCxcbiAgb25TaXplUGVyUGFnZUNoYW5nZTogUHJvcFR5cGVzLmZ1bmMuaXNSZXF1aXJlZCxcbiAgZGlzYWJsZVBhZ2VUaXRsZTogUHJvcFR5cGVzLmJvb2wsXG4gIHBhZ2VTdGFydEluZGV4OiBQcm9wVHlwZXMubnVtYmVyLFxuICBwYWdpbmF0aW9uU2l6ZTogUHJvcFR5cGVzLm51bWJlcixcbiAgc2hvd1RvdGFsOiBQcm9wVHlwZXMuYm9vbCxcbiAgcGFnZUxpc3RSZW5kZXJlcjogUHJvcFR5cGVzLmZ1bmMsXG4gIHBhZ2VCdXR0b25SZW5kZXJlcjogUHJvcFR5cGVzLmZ1bmMsXG4gIHNpemVQZXJQYWdlUmVuZGVyZXI6IFByb3BUeXBlcy5mdW5jLFxuICBwYWdpbmF0aW9uVG90YWxSZW5kZXJlcjogUHJvcFR5cGVzLmZ1bmMsXG4gIHNpemVQZXJQYWdlT3B0aW9uUmVuZGVyZXI6IFByb3BUeXBlcy5mdW5jLFxuICBmaXJzdFBhZ2VUZXh0OiBQcm9wVHlwZXMub25lT2ZUeXBlKFtQcm9wVHlwZXMuc3RyaW5nLCBQcm9wVHlwZXMubm9kZV0pLFxuICBwcmVQYWdlVGV4dDogUHJvcFR5cGVzLm9uZU9mVHlwZShbUHJvcFR5cGVzLnN0cmluZywgUHJvcFR5cGVzLm5vZGVdKSxcbiAgbmV4dFBhZ2VUZXh0OiBQcm9wVHlwZXMub25lT2ZUeXBlKFtQcm9wVHlwZXMuc3RyaW5nLCBQcm9wVHlwZXMubm9kZV0pLFxuICBsYXN0UGFnZVRleHQ6IFByb3BUeXBlcy5vbmVPZlR5cGUoW1Byb3BUeXBlcy5zdHJpbmcsIFByb3BUeXBlcy5ub2RlXSksXG4gIG5leHRQYWdlVGl0bGU6IFByb3BUeXBlcy5zdHJpbmcsXG4gIHByZVBhZ2VUaXRsZTogUHJvcFR5cGVzLnN0cmluZyxcbiAgZmlyc3RQYWdlVGl0bGU6IFByb3BUeXBlcy5zdHJpbmcsXG4gIGxhc3RQYWdlVGl0bGU6IFByb3BUeXBlcy5zdHJpbmcsXG4gIHdpdGhGaXJzdEFuZExhc3Q6IFByb3BUeXBlcy5ib29sLFxuICBhbHdheXNTaG93QWxsQnRuczogUHJvcFR5cGVzLmJvb2wsXG4gIGhpZGVTaXplUGVyUGFnZTogUHJvcFR5cGVzLmJvb2wsXG4gIGhpZGVQYWdlTGlzdE9ubHlPbmVQYWdlOiBQcm9wVHlwZXMuYm9vbCxcbiAgYm9vdHN0cmFwNDogUHJvcFR5cGVzLmJvb2xcbn07XG5cblBhZ2luYXRpb24uZGVmYXVsdFByb3BzID0ge1xuICBkaXNhYmxlUGFnZVRpdGxlOiBmYWxzZSxcbiAgYm9vdHN0cmFwNDogZmFsc2UsXG4gIHBhZ2VTdGFydEluZGV4OiBDb25zdC5QQUdFX1NUQVJUX0lOREVYLFxuICBwYWdpbmF0aW9uU2l6ZTogQ29uc3QuUEFHSU5BVElPTl9TSVpFLFxuICB3aXRoRmlyc3RBbmRMYXN0OiBDb25zdC5XaXRoX0ZJUlNUX0FORF9MQVNULFxuICBhbHdheXNTaG93QWxsQnRuczogQ29uc3QuU0hPV19BTExfUEFHRV9CVE5TLFxuICBzaG93VG90YWw6IENvbnN0LlNIT1dfVE9UQUwsXG4gIHBhZ2VMaXN0UmVuZGVyZXI6IG51bGwsXG4gIHBhZ2VCdXR0b25SZW5kZXJlcjogbnVsbCxcbiAgc2l6ZVBlclBhZ2VSZW5kZXJlcjogbnVsbCxcbiAgcGFnaW5hdGlvblRvdGFsUmVuZGVyZXI6IENvbnN0LlBBR0lOQVRJT05fVE9UQUwsXG4gIHNpemVQZXJQYWdlT3B0aW9uUmVuZGVyZXI6IG51bGwsXG4gIGZpcnN0UGFnZVRleHQ6IENvbnN0LkZJUlNUX1BBR0VfVEVYVCxcbiAgcHJlUGFnZVRleHQ6IENvbnN0LlBSRV9QQUdFX1RFWFQsXG4gIG5leHRQYWdlVGV4dDogQ29uc3QuTkVYVF9QQUdFX1RFWFQsXG4gIGxhc3RQYWdlVGV4dDogQ29uc3QuTEFTVF9QQUdFX1RFWFQsXG4gIHNpemVQZXJQYWdlTGlzdDogQ29uc3QuU0laRV9QRVJfUEFHRV9MSVNULFxuICBuZXh0UGFnZVRpdGxlOiBDb25zdC5ORVhUX1BBR0VfVElUTEUsXG4gIHByZVBhZ2VUaXRsZTogQ29uc3QuUFJFX1BBR0VfVElUTEUsXG4gIGZpcnN0UGFnZVRpdGxlOiBDb25zdC5GSVJTVF9QQUdFX1RJVExFLFxuICBsYXN0UGFnZVRpdGxlOiBDb25zdC5MQVNUX1BBR0VfVElUTEUsXG4gIGhpZGVTaXplUGVyUGFnZTogQ29uc3QuSElERV9TSVpFX1BFUl9QQUdFLFxuICBoaWRlUGFnZUxpc3RPbmx5T25lUGFnZTogQ29uc3QuSElERV9QQUdFX0xJU1RfT05MWV9PTkVfUEFHRVxufTtcblxuZXhwb3J0IGRlZmF1bHQgcGFnaW5hdGlvbkhhbmRsZXIoUGFnaW5hdGlvbik7XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvcGFnaW5hdGlvbi5qcyIsIi8qIGVzbGludCBqc3gtYTExeS9ocmVmLW5vLWhhc2g6IDAgKi9cbmltcG9ydCBSZWFjdCBmcm9tICdyZWFjdCc7XG5pbXBvcnQgUHJvcFR5cGVzIGZyb20gJ3Byb3AtdHlwZXMnO1xuXG5jb25zdCBTaXplUGVyUGFnZU9wdGlvbiA9ICh7XG4gIHRleHQsXG4gIHBhZ2UsXG4gIG9uU2l6ZVBlclBhZ2VDaGFuZ2UsXG4gIGJvb3RzdHJhcDRcbn0pID0+IChib290c3RyYXA0ID8gKFxuICA8YVxuICAgIGhyZWY9XCIjXCJcbiAgICB0YWJJbmRleD1cIi0xXCJcbiAgICByb2xlPVwibWVudWl0ZW1cIlxuICAgIGNsYXNzTmFtZT1cImRyb3Bkb3duLWl0ZW1cIlxuICAgIGRhdGEtcGFnZT17IHBhZ2UgfVxuICAgIG9uTW91c2VEb3duPXsgKGUpID0+IHtcbiAgICAgIGUucHJldmVudERlZmF1bHQoKTtcbiAgICAgIG9uU2l6ZVBlclBhZ2VDaGFuZ2UocGFnZSk7XG4gICAgfSB9XG4gID5cbiAgICB7IHRleHQgfVxuICA8L2E+XG4pIDogKFxuICA8bGlcbiAgICBrZXk9eyB0ZXh0IH1cbiAgICByb2xlPVwicHJlc2VudGF0aW9uXCJcbiAgICBjbGFzc05hbWU9XCJkcm9wZG93bi1pdGVtXCJcbiAgPlxuICAgIDxhXG4gICAgICBocmVmPVwiI1wiXG4gICAgICB0YWJJbmRleD1cIi0xXCJcbiAgICAgIHJvbGU9XCJtZW51aXRlbVwiXG4gICAgICBkYXRhLXBhZ2U9eyBwYWdlIH1cbiAgICAgIG9uTW91c2VEb3duPXsgKGUpID0+IHtcbiAgICAgICAgZS5wcmV2ZW50RGVmYXVsdCgpO1xuICAgICAgICBvblNpemVQZXJQYWdlQ2hhbmdlKHBhZ2UpO1xuICAgICAgfSB9XG4gICAgPlxuICAgICAgeyB0ZXh0IH1cbiAgICA8L2E+XG4gIDwvbGk+XG4pKTtcblxuU2l6ZVBlclBhZ2VPcHRpb24ucHJvcFR5cGVzID0ge1xuICB0ZXh0OiBQcm9wVHlwZXMuc3RyaW5nLmlzUmVxdWlyZWQsXG4gIHBhZ2U6IFByb3BUeXBlcy5udW1iZXIuaXNSZXF1aXJlZCxcbiAgb25TaXplUGVyUGFnZUNoYW5nZTogUHJvcFR5cGVzLmZ1bmMuaXNSZXF1aXJlZCxcbiAgYm9vdHN0cmFwNDogUHJvcFR5cGVzLmJvb2xcbn07XG5cblNpemVQZXJQYWdlT3B0aW9uLmRlZmF1bHRQcm9wcyA9IHtcbiAgYm9vdHN0cmFwNDogZmFsc2Vcbn07XG5cbmV4cG9ydCBkZWZhdWx0IFNpemVQZXJQYWdlT3B0aW9uO1xuXG5cblxuLy8gV0VCUEFDSyBGT09URVIgLy9cbi8vIC4vcGFja2FnZXMvcmVhY3QtYm9vdHN0cmFwLXRhYmxlMi1wYWdpbmF0b3Ivc3JjL3NpemUtcGVyLXBhZ2Utb3B0aW9uLmpzIiwiLyogZXNsaW50IHJlYWN0L3JlcXVpcmUtZGVmYXVsdC1wcm9wczogMCAqL1xuLyogZXNsaW50IGpzeC1hMTF5L2hyZWYtbm8taGFzaDogMCAqL1xuaW1wb3J0IGNzIGZyb20gJ2NsYXNzbmFtZXMnO1xuaW1wb3J0IFJlYWN0LCB7IENvbXBvbmVudCB9IGZyb20gJ3JlYWN0JztcbmltcG9ydCBQcm9wVHlwZXMgZnJvbSAncHJvcC10eXBlcyc7XG5cbmNsYXNzIFBhZ2VCdXR0b24gZXh0ZW5kcyBDb21wb25lbnQge1xuICBjb25zdHJ1Y3Rvcihwcm9wcykge1xuICAgIHN1cGVyKHByb3BzKTtcbiAgICB0aGlzLmhhbmRsZUNsaWNrID0gdGhpcy5oYW5kbGVDbGljay5iaW5kKHRoaXMpO1xuICB9XG5cbiAgaGFuZGxlQ2xpY2soZSkge1xuICAgIGUucHJldmVudERlZmF1bHQoKTtcbiAgICB0aGlzLnByb3BzLm9uUGFnZUNoYW5nZSh0aGlzLnByb3BzLnBhZ2UpO1xuICB9XG5cbiAgcmVuZGVyKCkge1xuICAgIGNvbnN0IHtcbiAgICAgIHBhZ2UsXG4gICAgICB0aXRsZSxcbiAgICAgIGFjdGl2ZSxcbiAgICAgIGRpc2FibGVkLFxuICAgICAgY2xhc3NOYW1lXG4gICAgfSA9IHRoaXMucHJvcHM7XG4gICAgY29uc3QgY2xhc3NlcyA9IGNzKHtcbiAgICAgIGFjdGl2ZSxcbiAgICAgIGRpc2FibGVkLFxuICAgICAgJ3BhZ2UtaXRlbSc6IHRydWVcbiAgICB9LCBjbGFzc05hbWUpO1xuXG4gICAgcmV0dXJuIChcbiAgICAgIDxsaSBjbGFzc05hbWU9eyBjbGFzc2VzIH0gdGl0bGU9eyB0aXRsZSB9PlxuICAgICAgICA8YSBocmVmPVwiI1wiIG9uQ2xpY2s9eyB0aGlzLmhhbmRsZUNsaWNrIH0gY2xhc3NOYW1lPVwicGFnZS1saW5rXCI+eyBwYWdlIH08L2E+XG4gICAgICA8L2xpPlxuICAgICk7XG4gIH1cbn1cblxuUGFnZUJ1dHRvbi5wcm9wVHlwZXMgPSB7XG4gIG9uUGFnZUNoYW5nZTogUHJvcFR5cGVzLmZ1bmMuaXNSZXF1aXJlZCxcbiAgcGFnZTogUHJvcFR5cGVzLm9uZU9mVHlwZShbXG4gICAgUHJvcFR5cGVzLm5vZGUsXG4gICAgUHJvcFR5cGVzLm51bWJlcixcbiAgICBQcm9wVHlwZXMuc3RyaW5nXG4gIF0pLmlzUmVxdWlyZWQsXG4gIGFjdGl2ZTogUHJvcFR5cGVzLmJvb2wuaXNSZXF1aXJlZCxcbiAgZGlzYWJsZWQ6IFByb3BUeXBlcy5ib29sLmlzUmVxdWlyZWQsXG4gIGNsYXNzTmFtZTogUHJvcFR5cGVzLnN0cmluZyxcbiAgdGl0bGU6IFByb3BUeXBlcy5zdHJpbmdcbn07XG5cbmV4cG9ydCBkZWZhdWx0IFBhZ2VCdXR0b247XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvcGFnZS1idXR0b24uanMiLCJpbXBvcnQgUmVhY3QgZnJvbSAncmVhY3QnO1xuaW1wb3J0IFBhZ2luYXRpb25MaXN0IGZyb20gJy4vcGFnaW5hdGlvbi1saXN0JztcbmltcG9ydCBzdGFuZGFsb25lQWRhcHRlciBmcm9tICcuL3N0YW5kYWxvbmUtYWRhcHRlcic7XG5pbXBvcnQgUGFnaW5hdGlvbkhhbmRsZXIgZnJvbSAnLi9wYWdpbmF0aW9uLWhhbmRsZXInO1xuaW1wb3J0IHBhZ2luYXRpb25MaXN0QWRhcHRlciBmcm9tICcuL3BhZ2luYXRpb24tbGlzdC1hZGFwdGVyJztcblxuY29uc3QgUGFnaW5hdGlvbkxpc3RTdGFuZGFsb25lID0gcHJvcHMgPT4gKFxuICA8UGFnaW5hdGlvbkxpc3QgeyAuLi5wcm9wcyB9IC8+XG4pO1xuXG5leHBvcnQgZGVmYXVsdFxuc3RhbmRhbG9uZUFkYXB0ZXIoUGFnaW5hdGlvbkhhbmRsZXIocGFnaW5hdGlvbkxpc3RBZGFwdGVyKFBhZ2luYXRpb25MaXN0U3RhbmRhbG9uZSkpKTtcblxuXG5cbi8vIFdFQlBBQ0sgRk9PVEVSIC8vXG4vLyAuL3BhY2thZ2VzL3JlYWN0LWJvb3RzdHJhcC10YWJsZTItcGFnaW5hdG9yL3NyYy9wYWdpbmF0aW9uLWxpc3Qtc3RhbmRhbG9uZS5qcyIsImltcG9ydCBSZWFjdCBmcm9tICdyZWFjdCc7XG5pbXBvcnQgU2l6ZVBlclBhZ2VEcm9wZG93biBmcm9tICcuL3NpemUtcGVyLXBhZ2UtZHJvcGRvd24nO1xuaW1wb3J0IHN0YW5kYWxvbmVBZGFwdGVyIGZyb20gJy4vc3RhbmRhbG9uZS1hZGFwdGVyJztcbmltcG9ydCBwYWdpbmF0aW9uSGFuZGxlciBmcm9tICcuL3BhZ2luYXRpb24taGFuZGxlcic7XG5pbXBvcnQgc2l6ZVBlclBhZ2VEcm9wZG93bkFkYXB0ZXIgZnJvbSAnLi9zaXplLXBlci1wYWdlLWRyb3Bkb3duLWFkYXB0ZXInO1xuXG5jb25zdCBTaXplUGVyUGFnZURyb3Bkb3duU3RhbmRhbG9uZSA9IHByb3BzID0+IChcbiAgPFNpemVQZXJQYWdlRHJvcGRvd24geyAuLi5wcm9wcyB9IC8+XG4pO1xuXG5leHBvcnQgZGVmYXVsdFxuc3RhbmRhbG9uZUFkYXB0ZXIocGFnaW5hdGlvbkhhbmRsZXIoc2l6ZVBlclBhZ2VEcm9wZG93bkFkYXB0ZXIoU2l6ZVBlclBhZ2VEcm9wZG93blN0YW5kYWxvbmUpKSk7XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvc2l6ZS1wZXItcGFnZS1kcm9wZG93bi1zdGFuZGFsb25lLmpzIiwiaW1wb3J0IFJlYWN0IGZyb20gJ3JlYWN0JztcbmltcG9ydCBQYWdpbmF0aW9uVG90YWwgZnJvbSAnLi9wYWdpbmF0aW9uLXRvdGFsJztcbmltcG9ydCBzdGFuZGFsb25lQWRhcHRlciBmcm9tICcuL3N0YW5kYWxvbmUtYWRhcHRlcic7XG5pbXBvcnQgcGFnaW5hdGlvblRvdGFsQWRhcHRlciBmcm9tICcuL3BhZ2luYXRpb24tdG90YWwtYWRhcHRlcic7XG5cbmNvbnN0IFBhZ2luYXRpb25Ub3RhbFN0YW5kYWxvbmUgPSBwcm9wcyA9PiAoXG4gIDxQYWdpbmF0aW9uVG90YWwgeyAuLi5wcm9wcyB9IC8+XG4pO1xuXG5leHBvcnQgZGVmYXVsdFxuc3RhbmRhbG9uZUFkYXB0ZXIocGFnaW5hdGlvblRvdGFsQWRhcHRlcihQYWdpbmF0aW9uVG90YWxTdGFuZGFsb25lKSk7XG5cblxuXG4vLyBXRUJQQUNLIEZPT1RFUiAvL1xuLy8gLi9wYWNrYWdlcy9yZWFjdC1ib290c3RyYXAtdGFibGUyLXBhZ2luYXRvci9zcmMvcGFnaW5hdGlvbi10b3RhbC1zdGFuZGFsb25lLmpzIl0sInNvdXJjZVJvb3QiOiIifQ==
//# sourceMappingURL=react-bootstrap-table2-paginator.js.map