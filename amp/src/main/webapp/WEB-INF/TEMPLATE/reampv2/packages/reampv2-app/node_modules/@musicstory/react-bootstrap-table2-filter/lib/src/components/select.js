'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _extends = Object.assign || function (target) { for (var i = 1; i < arguments.length; i++) { var source = arguments[i]; for (var key in source) { if (Object.prototype.hasOwnProperty.call(source, key)) { target[key] = source[key]; } } } return target; };

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _propTypes = require('prop-types');

var _propTypes2 = _interopRequireDefault(_propTypes);

var _comparison = require('../comparison');

var _const = require('../const');

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