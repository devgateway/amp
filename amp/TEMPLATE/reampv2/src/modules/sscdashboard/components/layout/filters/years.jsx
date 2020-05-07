import React, { Component } from 'react';
import './filters.css';
import { SSCTranslationContext } from '../../StartUp';

class MultiSelectionDropDown extends Component {


    getOptions(selected) {
        const {options = [], selectedOptions = [], maxNumberOfSelections, dataIsSorted} = this.props;
        const filtered = options.filter(p => {
            if (selected) {
                return (selectedOptions.includes(p.id));
            } else {
                return !(selectedOptions.includes(p.id));
            }
        });

        /*const listItems = filtered.sort((a, b) => {
            if (!dataIsSorted) {
                let aName =  a.name;
                let bName =  b.name;
                if (aName < bName) return -1;
                if (aName > bName) return 1;
            }
            return 0;
        })*/
        return options.map((o) =>
            <li key={o.id}>
                <input type="checkbox" name={`y${o.id}`} id={o.id} value={o.id}/>
                <label htmlFor={o.id}>{o.name}</label>
            </li>
        );
    }

    getOptionsCount() {
        const {options = []} = this.props;
        return options.length;
    }

    getSelectedCount() {
        const {selected = []} = this.props;
        return selected.length;
    }

    render() {
        const {translations} = this.context;
        return (
            <div className="horizontal-filter dropdown panel">
                <button className="btn btn-primary" type="button" type="button" data-toggle="collapse"
                        data-parent="#accordion-filter" href={`#${this.props.filterId}`}
                        aria-controls={this.props.filterId}>
                    {translations[this.props.filterName]} <span
                    className="select-count">{`${this.getSelectedCount()}/${this.getOptionsCount()}`}</span>
                </button>
                <div className="filter-list collapse" id={this.props.filterId}>
                    <div className="well">
                        <div className="well-inner">
                            <ul>
                                {this.getOptions([])}
                            </ul>
                        </div>

                    </div>
                </div>

            </div>
        );
    }
}

MultiSelectionDropDown.contextType = SSCTranslationContext;
export default MultiSelectionDropDown;
