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