import React, { Component } from 'react';
import PropTypes from 'prop-types';
import ReactTooltip from 'react-tooltip';
import VisibilitySensor from 'react-visibility-sensor'

import './filters.css';
import { SSCTranslationContext } from '../../StartUp';
import { splitArray, compareArrayNumber, calculateUpdatedValuesForDropDowns } from '../../../utils/Utils';
import { Util } from 'leaflet/dist/leaflet-src.esm';

const MultiSelectionDropDownContainer = (props) => {
    const {elements, columnsCount} = props;
    return (<div className="row" key='filter-row'>
        <MultiSelectionDropDownContainerRow elements={elements} columnsCount={columnsCount}/>
    </div>);
};

const MultiSelectionDropDownContainerRow = (props) => {
    const {elements = [], columnsCount = 0} = props;
    return splitArray(elements, columnsCount, true).map((e, idx) => {
        const width = Math.floor(12 / columnsCount);
        return (<div className={`filter-content col-md-${width}`} key={idx}>
            <ul className={'elements-list'}>{e}</ul>
        </div>);
    });
};

class MultiSelectionDropDown extends Component {

    constructor(props) {
        super(props);
        this.state =
            {
                searchText: ''
            };
    }

    getOptions(selected) {
        const {options = [], selectedOptions = [], sortData, columnsCount} = this.props;
        const {searchText} = this.state;
        const optionsFilteredByText = options.filter(p => {
            return (searchText === '' ? true :
                p.name.toString().toUpperCase().indexOf(searchText.toUpperCase()) >= 0);
        });

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
                } else {
                    if (a.name > b.name) {
                        return 1
                    } else {
                        return 0;
                    }
                }
            } else {
                return 0;
            }
        });
        const elements = optionsSorted.map((o) => {
                return (<li key={o.id}>
                    <input type="checkbox" name={`y${o.id}`} id={o.id} value={o.id}
                           checked={selected}
                           onChange={this.onChange.bind(this)}
                    />
                    <label htmlFor={o.id}>{o.name}</label>
                </li>);
            }
        );
        return (<MultiSelectionDropDownContainer elements={elements} columnsCount={columnsCount}/>);
    }

    getOptionsCount() {
        const {options = []} = this.props;
        return options.length;
    }

    getSelectedCount() {
        const {selectedOptions = []} = this.props;
        return selectedOptions.length;
    }

    onChange(e) {
        const ipSelectedFilter = parseInt(e.target.id);
        const {selectedOptions} = this.props;
        this.props.onChange(calculateUpdatedValuesForDropDowns(ipSelectedFilter, selectedOptions));
    }

    onSearchBoxChange(e) {
        this.setState({searchText: e.target.value});
    }

    handleClearText(e) {
        this.setState({searchText: ''});
    }

    selectAll() {
        const {options} = this.props;
        this.props.onChange(options.map(o => o.id));
    }


    selectNone() {
        this.props.onChange([]);
    }

    selectCategory(categoryId) {
        this.props.onChange(this.getCategoryOptions(categoryId));
    }

    getCategoryOptions(categoryId) {
        const {options, categoryFetcher} = this.props;

        return options.filter(o => categoryFetcher(o) === categoryId).map(of => of.id);
    }

    isCategorySelected(categoryId) {
        const {selectedOptions = []} = this.props;
        return compareArrayNumber(this.getCategoryOptions(categoryId), selectedOptions);
    }


    onDropdownVisible(isVisible) {
        if (isVisible) {
            this.searchBox.focus();
        }
    }

    render() {
        const {translations} = this.context;
        const showQuickSelectionLinks = true;
        const {categoriesSelection, categoryFetcher, chartName, chartSelected, onChangeChartSelected} = this.props;
        const showSelectAll = true;
        return (
            <div className="horizontal-filter dropdown panel">
                <button
                    className={`btn btn-primary${chartName ? ' ' + chartName : ''}${chartName
                    && chartSelected && chartName === chartSelected ? ' selected' : ''}`}
                    type="button" data-toggle="collapse"
                    data-parent={`#${this.props.parentId}`}
                    href={`#${this.props.filterId}`}
                    aria-control={this.props.filterId}
                    onClick={() =>
                        (onChangeChartSelected && chartName !== chartSelected ? onChangeChartSelected(chartName) : false)
                    }>
                    {translations[this.props.filterName]} {!this.props.label && <span
                    className="select-count">{`(${this.getSelectedCount()}/${this.getOptionsCount()})`}</span>}
                </button>
                <VisibilitySensor onChange={this.onDropdownVisible.bind(this)}>
                    <div
                        className={`filter-list collapse${chartName && chartSelected
                        && chartName === chartSelected ? ' in ' : ''}`}
                        id={this.props.filterId}>
                        <div className="well">
                            <div className="autocomplete-box">
                                <input onChange={this.onSearchBoxChange.bind(this)} value={this.state.searchText}
                                       placeholder={`${translations['amp.ssc.dashboard:search']}...`}
                                       ref={input => this.searchBox = input}
                                />
                                <span className="clear" onClick={this.handleClearText.bind(this)}>×</span>
                            </div>
                            {showQuickSelectionLinks &&
                            <div className="select-all-none">
                                {showSelectAll &&
                                <span className="select-all all">
                                <input type='radio' value='1' name={`radio-${this.props.filterId}`}
                                       id={`select-all-${this.props.filterId}`}
                                       checked={this.getSelectedCount() == this.getOptionsCount()}
                                />
                                <label htmlFor='select-all' onClick={e => this.selectAll()}>Select All</label>
                            </span>
                                }
                                <span className="select-all all">
                                <input type='radio' value='2' name={`radio-${this.props.filterId}`}
                                       id={`select-none-${this.props.filterId}`}
                                       checked={this.getSelectedCount() == 0}
                                />
                                <label htmlFor='select-none' onClick={e => this.selectNone()}>Select None</label>
                            </span>

                                {categoryFetcher && categoriesSelection && categoriesSelection.map((category, idx) => {
                                    const {id, name, tooltip} = category;
                                    return (
                                        <span className="select-all all" onClick={e => this.selectCategory(id)}
                                              key={`all-none-${id}`}>
                                <input type='radio' value={idx + 4} name={`radio-${this.props.filterId}`} id={name}
                                       checked={this.isCategorySelected(id)}
                                />
                                <label htmlFor={name} id={`${name}-label`}
                                       data-tip={tooltip}>{name}</label>
                            </span>
                                    );
                                })}
                            </div>
                            }
                            <div className="selected">
                                <div
                                    className="title">{translations['amp.ssc.dashboard:selected']} {this.props.label &&
                                <span
                                    className="select-count">{`${translations[this.props.label]} (${this.getSelectedCount()}/${this.getOptionsCount()})`}</span>}
                                </div>

                                <div className="well-inner filter-list-inner">
                                    {this.getOptions(true)}
                                </div>
                            </div>
                            <div className="unselected">
                                <div
                                    className="title">{translations['amp.ssc.dashboard:un-selected']} {this.props.label &&
                                <span
                                    className="select-count">{`${translations[this.props.label]} (${this.getOptionsCount()-this.getSelectedCount()})`}</span>}</div>
                                <div className="well-inner filter-list-inner">

                                    {this.getOptions(false)}
                                </div>
                            </div>

                        </div>
                    </div>
                </VisibilitySensor>
                <ReactTooltip/>
            </div>
        );
    }
}

MultiSelectionDropDown.contextType = SSCTranslationContext;

MultiSelectionDropDown.propTypes = {
    sortData: PropTypes.bool,
    options: PropTypes.array.isRequired,
    columnsCount: PropTypes.number
};

MultiSelectionDropDown.defaultProps = {
    selectedOptions: [],
    columnsCount: 1
};
export default MultiSelectionDropDown;
