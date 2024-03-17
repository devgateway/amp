'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _propTypes = require('prop-types');

var _propTypes2 = _interopRequireDefault(_propTypes);

var _comparison = require('../comparison');

var Comparator = _interopRequireWildcard(_comparison);

var _const = require('../const');

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