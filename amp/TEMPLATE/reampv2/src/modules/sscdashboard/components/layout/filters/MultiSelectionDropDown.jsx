import React, { Component } from 'react';
import PropTypes from 'prop-types';
import './filters.css';
import { SSCTranslationContext } from '../../StartUp';

class MultiSelectionDropDown extends Component {

    getOptions() {
        const {options = [], selectedOptions = [], sortData} = this.props;
        /*const filtered = options.filter(p => {
            if (selected) {
                return (selectedOptions.includes(p.id));
            } else {
                return !(selectedOptions.includes(p.id));
            }
        });*/

        /*const listItems = options.sort((a, b) => {
            if (!dataIsSorted) {
                let aName = a.name;
                let bName = b.name;
                if (aName < bName) return -1;
                if (aName > bName) return 1;
            }
            return 0;
        }) */
        return options.sort((a, b) => {
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
        }).map((o) => {
                const isChecked = selectedOptions.find(s => s === o.id) !== undefined;
                return (<li key={o.id}>
                    <input type="checkbox" name={`y${o.id}`} id={o.id} value={o.id}
                           defaultChecked={isChecked} onClick={this.onChange.bind(this)}
                    />
                    <label htmlFor={o.id}>{o.name}</label>
                </li>);
            }
        );
    }

    getOptionsCount() {
        const {options = []} = this.props;
        return options.length;
    }

    getSelectedCount() {
        const {selectedOptions = []} = this.props;
        return selectedOptions.length;
    }

    onChange(event) {
        this.props.onChange(event.target.id);
    }

    render() {
        const {translations} = this.context;
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
                        <div className="well-inner">
                            <ul>
                                {this.getOptions()}
                            </ul>
                        </div>

                    </div>
                </div>

            </div>
        );
    }
}

MultiSelectionDropDown.contextType = SSCTranslationContext;
MultiSelectionDropDown.propTypes = {
    sortData: PropTypes.bool.isRequired,
    selectedOptions: PropTypes.array.isRequired,
    options: PropTypes.array.isRequired
};
MultiSelectionDropDown.defaultProps = {
    sortData: false,
    selectedOptions: []
};
export default MultiSelectionDropDown;
