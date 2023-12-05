import React, { Component } from 'react';
import PropTypes from 'prop-types';
import ReactTooltip from 'react-tooltip';
import VisibilitySensor from 'react-visibility-sensor';
import './filters.css';
import { SSCTranslationContext } from '../../StartUp';
import { compareArrayNumber, calculateUpdatedValuesForDropDowns } from '../../../utils/Utils';

class MultiSelectionDropDown extends Component {
  constructor(props) {
    super(props);
    this.state = {
      searchText: ''
    };
  }

  handleClearText() {
    this.setState({ searchText: '' });
  }

  onSearchBoxChange(e) {
    this.setState({ searchText: e.target.value });
  }

  onChange(e) {
    const ipSelectedFilter = parseInt(e.target.value, 10);
    const { selectedOptions, onChange, selectionLimit } = this.props;
    if (selectionLimit === 0 || selectedOptions.length < selectionLimit || selectedOptions.includes(ipSelectedFilter)) {
      onChange(calculateUpdatedValuesForDropDowns(ipSelectedFilter, selectedOptions));
    }
  }

  onDropdownVisible(isVisible) {
    if (isVisible) {
      this.searchBox.focus();
    }
  }

  getCategoryOptions(categoryId) {
    const { options, categoryFetcher } = this.props;

    return options.filter(o => categoryFetcher(o) === categoryId).map(of => of.id);
  }

  getSelectedCount() {
    const { selectedOptions = [] } = this.props;
    return selectedOptions.length;
  }

  getOptionsCount() {
    const { options = [] } = this.props;
    return options.length;
  }

  getOptions(selected) {
    const {
      options = [], selectedOptions = [], sortData, filterId
    } = this.props;
    const { searchText } = this.state;
    const optionsFilteredByText = options.filter(p => (searchText === '' ? true
      : p.name.toString().toUpperCase().indexOf(searchText.toUpperCase()) >= 0));

    const optionsFiltered = optionsFilteredByText.filter(of => {
      if (selected) {
        return selectedOptions.includes(of.id);
      } else {
        return !selectedOptions.includes(of.id);
      }
    });
    const optionsSorted = optionsFiltered.sort((a, b) => {
      if (sortData) {
        if (a.name < b.name) {
          return -1;
        } else if (a.name > b.name) {
          return 1;
        } else {
          return 0;
        }
      } else {
        return 0;
      }
    });
    const elements = optionsSorted.map((o) => (
      <li key={`${o.id + filterId}_li`}>
        <input
          key={`${o.id + filterId}_input`}
          type="checkbox"
          name={`y${o.id}${filterId}`}
          id={o.id + filterId}
          value={o.id}
          checked={selected}
          onChange={(e) => {
            this.onChange(e);
          }}
        />
        <label htmlFor={o.id + filterId}>{o.name}</label>
      </li>
    ));
    return (
      <div className="row" key={`filter-row ${filterId}`}>
        <div className="filter-content col-md-12">
          <ul className="elements-list">{elements}</ul>
        </div>
      </div>
    );
  }

  isCategorySelected(categoryId) {
    const { selectedOptions = [] } = this.props;
    return compareArrayNumber(this.getCategoryOptions(categoryId), selectedOptions);
  }

  selectCategory(categoryId) {
    const { onChange } = this.props;
    onChange(this.getCategoryOptions(categoryId));
  }

  selectNone() {
    const { onChange } = this.props;
    onChange([]);
  }

  selectAll() {
    const { options, onChange } = this.props;
    onChange(options.map(o => o.id));
  }

  showCollapse() {
    const { chartName, chartSelected, parentId } = this.props;

    return chartName && chartSelected
    && chartName === chartSelected ? true : !parentId;
  }

  getActualWidth(inputText) {
    const font = '14px Montserrat \'Open Sans\'';
    const canvas = document.createElement('canvas');
    const context = canvas.getContext('2d');
    context.font = font;
    const { width } = context.measureText(inputText);
    const formattedWidth = Math.ceil(width);
    return formattedWidth;
  }

  render() {
    const { translations } = this.context;
    const onChangeNew = this.onChange.bind(this);
    const { searchText } = this.state;
    const {
      categoriesSelection, categoryFetcher, chartName, chartSelected, onChangeChartSelected, parentId, filterId,
      filterName, label, disabled, selectionLimit
    } = this.props;
    const textWidth = this.getActualWidth(`${translations['amp.ssc.dashboard:select-all']
    } | ${translations['amp.ssc.dashboard:select-none']}`);
    const SelectAll = () => (
      <>
        <span className="select-all all">
          <input
            type="radio"
            value="1"
            name={`radio-${filterId}`}
            id={`select-all-${filterId}`}
            checked={this.getSelectedCount() === this.getOptionsCount()}
          />
          <label
            htmlFor="select-all"
            onClick={() => this.selectAll()}>
            {translations['amp.ssc.dashboard:select-all']}
          </label>
        </span>
      </>
    );
    const SelectNone = () => (
      <>
        <span className="select-all all">
          <input
            type="radio"
            value="2"
            name={`radio-${filterId}`}
            id={`select-none-${filterId}`}
            checked={this.getSelectedCount() === 0}
          />
          <label
            htmlFor={`select-none-${filterId}`}
            onClick={() => this.selectNone()}>
            {translations['amp.ssc.dashboard:select-none']}
          </label>
        </span>
      </>
    );
    const showQuickSelectionLinks = parentId !== null;
    const showSelectAll = selectionLimit === 0;
    return (
      <div className={`horizontal-filter dropdown panel ${disabled ? ' disable-filter' : ''}`}>
        {parentId
        && (
          <button
            className={`btn btn-primary${chartName ? ` ${chartName}` : ''}${chartName
            && chartSelected && chartName === chartSelected ? ' selected' : ''}`}
            type="button"
            data-toggle="collapse"
            data-parent={`#${parentId}`}
            href={`#${filterId}`}
            aria-controls={filterId}
            onClick={() => (onChangeChartSelected && chartName
            !== chartSelected ? onChangeChartSelected(chartName) : false)}>
            <span className="filterName">
            {translations[filterName]}
              </span>
            {' '}
            {(!label) && (
              <span
                className="select-count">
                {`(${this.getSelectedCount()}/${this.getOptionsCount()})`}
              </span>
            )}
          </button>
        )}
        <VisibilitySensor onChange={this.onDropdownVisible.bind(this)}>
          <div
            className={`filter-list collapse${this.showCollapse() ? 'in' : ''}`}
            id={filterId}>
            <div className="well">
              <div className="row autocomplete">
                {!showQuickSelectionLinks && (
                  <div className={`col-md-${textWidth < 110 ? '3' : '5'}`}>
                    <div className="select-all-none">
                      <SelectAll />
                      <SelectNone />
                    </div>
                  </div>
                )}
                <div className={`col-md-${showQuickSelectionLinks ? '12' : '6'} autocomplete-row`}>
                  <div className="autocomplete-box">
                    <input
                      onChange={this.onSearchBoxChange.bind(this)}
                      value={searchText}
                      placeholder={`${translations['amp.ssc.dashboard:search']}...`}
                      ref={input => this.searchBox = input}
                    />
                    <span className="clear" onClick={this.handleClearText.bind(this)}>×</span>
                  </div>
                </div>
                {!showQuickSelectionLinks && (
                  <div className={`col-md-${textWidth < 110 ? '3' : '1'}`} />
                )}
              </div>
              {showQuickSelectionLinks
              && (
                <div className="select-all-none">
                  {showSelectAll
                  && (
                    <SelectAll />
                  )}
                  {showSelectAll
                  && (
                    <SelectNone />
                  )}
                  {categoryFetcher && categoriesSelection && categoriesSelection.map((category, idx) => {
                    const { id, name, tooltip } = category;
                    return (
                      <span
                        className="select-all all"
                        onClick={() => this.selectCategory(id)}
                        key={`all-none-${id}${filterId}`}>
                        <input
                          type="radio"
                          value={idx + 4}
                          name={`radio-${filterId}`}
                          id={name}
                          checked={this.isCategorySelected(id)}
                        />
                        <label
                          htmlFor={name}
                          id={`${name}-label`}
                          data-tip={tooltip}
                          className={this.isCategorySelected(id) ? ' label-bold' : ''}>
                          {name}
                        </label>
                      </span>
                    );
                  })}
                </div>
              )}
              <div className="selected">
                <div
                  className="title">
                  {translations['amp.ssc.dashboard:selected']}
                  {' '}
                  {label && parentId
                  && (
                    <span
                      className="select-count">
                      {`${translations[label]} (${this.getSelectedCount()}/${this.getOptionsCount()})`}
                    </span>
                  )}
                </div>

                <div className="well-inner filter-list-inner">
                  {this.getOptions(true, onChangeNew)}
                </div>
              </div>
              <div className="unselected">
                <div
                  className="title">
                  {translations['amp.ssc.dashboard:un-selected']}
                  {' '}
                  {label && parentId
                  && (
                    <span
                      className="select-count">
                      {`${translations[label]} (${this.getOptionsCount() - this.getSelectedCount()})`}
                    </span>
                  )}
                </div>
                <div className="well-inner filter-list-inner">
                  {this.getOptions(false, onChangeNew)}
                </div>
              </div>

            </div>
          </div>
        </VisibilitySensor>
        <ReactTooltip />
      </div>
    );
  }
}

MultiSelectionDropDown.contextType = SSCTranslationContext;

MultiSelectionDropDown.propTypes = {
  sortData: PropTypes.bool,
  options: PropTypes.array.isRequired,
  selectedOptions: PropTypes.array,
  label: PropTypes.string,
  onChange: PropTypes.func.isRequired,
  categoryFetcher: PropTypes.func,
  categoriesSelection: PropTypes.array,
  filterId: PropTypes.string.isRequired,
  parentId: PropTypes.string,
  onChangeChartSelected: PropTypes.func,
  chartSelected: PropTypes.string,
  chartName: PropTypes.string,
  filterName: PropTypes.string.isRequired,
  disabled: PropTypes.bool,
  selectionLimit: PropTypes.number
};

MultiSelectionDropDown.defaultProps = {
  selectedOptions: [],
  categoriesSelection: [],
  sortData: null,
  parentId: null,
  categoryFetcher: null,
  chartName: null,
  onChangeChartSelected: null,
  chartSelected: null,
  label: null,
  disabled: false,
  selectionLimit: 0
};
export default MultiSelectionDropDown;
