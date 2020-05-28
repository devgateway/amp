import React, { Component } from 'react';
import PropTypes from 'prop-types';
import ReactTooltip from 'react-tooltip';
import './filters.css';
import { SSCTranslationContext } from '../../StartUp';
import { splitArray, compareArrayNumber } from '../../../utils/Utils';

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
        return (<div className={`col-md-${width}`} key={idx}>
            <ul>{e}</ul>
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
            return (searchText === '' ? true : p.name.toUpperCase().startsWith(searchText.toUpperCase()));
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
        let updatedSelectedOptions;
        if (selectedOptions.includes(ipSelectedFilter)) {
            updatedSelectedOptions = selectedOptions.filter(sc => sc !== ipSelectedFilter);
        } else {
            updatedSelectedOptions = [...selectedOptions];
            updatedSelectedOptions.push(ipSelectedFilter);
        }
        this.props.onChange(updatedSelectedOptions);
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

    render() {
        const {translations} = this.context;
        const showQuickSelectionLinks = true;
        const {categoriesSelection, categoryFetcher} = this.props;

        const showSelectAll = true;
        return (
            <div className="horizontal-filter dropdown panel">
                <button className="btn btn-primary" type="button" data-toggle="collapse"
                        data-parent="#accordion-filter" href={`#${this.props.filterId}`}
                        aria-controls={this.props.filterId}>
                    {translations[this.props.filterName]} <span
                    className="select-count">{`${this.getSelectedCount()}/${this.getOptionsCount()}`}</span>
                </button>
                <div className="filter-list collapse" id={this.props.filterId}>
                    <div className="well">
                        <div className="autocomplete-box">
                            <input onChange={this.onSearchBoxChange.bind(this)} value={this.state.searchText}/>
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
                        <div className="well-inner filter-list-inner">
                            <div className="selected">
                                <div className="title">selected</div>
                                {this.getOptions(true)}
                            </div>
                        </div>
                        <div className="well-inner filter-list-inner">
                            <div className="unselected">
                                <div className="title">un selected</div>
                                {this.getOptions(false)}
                            </div>
                        </div>

                    </div>
                </div>
                <ReactTooltip/>
            </div>
        );
    }
}

MultiSelectionDropDown
    .contextType = SSCTranslationContext;
MultiSelectionDropDown
    .propTypes = {
    sortData: PropTypes.bool.isRequired,
    selectedOptions: PropTypes.array.isRequired,
    options: PropTypes.array.isRequired,
    columnsCount: PropTypes.number.isRequired
};
MultiSelectionDropDown
    .defaultProps = {
    sortData: false,
    selectedOptions: [],
    columnsCount: 2
};
export default MultiSelectionDropDown;
