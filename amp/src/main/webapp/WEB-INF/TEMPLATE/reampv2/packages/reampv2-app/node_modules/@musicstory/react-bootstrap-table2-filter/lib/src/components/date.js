'use strict';

Object.defineProperty(exports, "__esModule", {
  value: true
});

var _createClass = function () { function defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } } return function (Constructor, protoProps, staticProps) { if (protoProps) defineProperties(Constructor.prototype, protoProps); if (staticProps) defineProperties(Constructor, staticProps); return Constructor; }; }();

var _react = require('react');

var _react2 = _interopRequireDefault(_react);

var _propTypes = require('prop-types');

var _comparison = require('../comparison');

var Comparator = _interopRequireWildcard(_comparison);

var _const = require('../const');

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