import React, {
    Component,
    PropTypes
} from 'react';
import {
    connect
} from 'react-redux';
import {
    bindActionCreators
} from 'redux';
import * as Constants from '../common/Constants';
require('../styles/less/main.less');
class AppliedFilters extends Component {
    constructor(props, context) {
        super(props, context);
        this.state = {

        };
        this.showAppliedFilters = this.showAppliedFilters.bind(this);
        this.filtersToHtml = this.filtersToHtml.bind(this);
    }

    showAppliedFilters() {
        var html = [];
        this.props.filter.reset({
            silent: true
        });
        if (this.props.dataFreezeEvent.filters) {
            let filters = JSON.parse(this.props.dataFreezeEvent.filters || '{}');
            this.props.filter.deserialize(filters, {
                silent: true
            });
            let modelFilters = this.props.filter.serializeToModels();
            html = this.filtersToHtml(modelFilters.filters);
        }
        return html;
    }

    filtersToHtml(filters) {
        var html = [];
        if (filters != undefined) {
            for (var propertyName in filters) {
                var auxProperty = filters[propertyName];
                var content = [];
                if (auxProperty.modelType === 'YEAR-SINGLE-VALUE' || auxProperty.modelType === 'DATE-RANGE-VALUES') {
                    var filter = this.createDateFilterObject(filters, propertyName);
                    if (filter && filter.values.length > 0) {
                        html.push(<div className='round-filter-group'><b className='i18n'>{ filter.trnName.replace(/-/g, " ") }</b><br/> { this.filterContentToHtml(filter.values) }</div>);
                    }

                } else {
                    auxProperty.forEach(function (item) {
                        var auxItem = {};
                        if (item.get !== undefined) {
                            auxItem.id = item.get('id');
                            auxItem.name = item.get('name');

                            if (item.get('name') === "true" || item.get('name') === "false") {
                                auxItem.trnName = TranslationManager.getTranslated(item.get('name'));
                            } else {
                                auxItem.trnName = item.get('name');
                            }
                            content.push(auxItem);
                        }
                    });

                    var trnName = auxProperty.filterName || propertyName;
                    html.push(<div className='round-filter-group'><b className='i18n'>{ trnName.replace(/-/g, " ") }</b><br/>{this.filterContentToHtml(content)}</div>);
                }

            }
        }
        return html;
    }

    filterContentToHtml(content) {
        var html = [];
        content.forEach(function (item) {
            html.push(<div className='round-filter'>{item.trnName}</div>);
        });
        return html;
    }

    createDateFilterObject(filters, propertyName) {
        var auxProperty = filters[propertyName];
        var filter;
        if (auxProperty != undefined) {
            filter = {
                trnName: TranslationManager.getTranslated(propertyName),
                name: propertyName,
                values: []
            };
            if (auxProperty.modelType === 'DATE-RANGE-VALUES') {
                auxProperty.start = auxProperty.start || "";
                auxProperty.end = auxProperty.end || "";

                var startDatePrefix = TranslationManager.getTranslated((auxProperty.start.length > 0 && auxProperty.end.length === 0) ? "From" : "");
                var endDatePrefix = TranslationManager.getTranslated((auxProperty.start.length === 0 && auxProperty.end.length > 0) ? "Until" : "");
                var trnName = "";

                if (auxProperty.start.length > 0) {
                    trnName = startDatePrefix + " " + window.currentFilter.formatDate(auxProperty.start);
                }

                if (auxProperty.end.length > 0) {
                    if (auxProperty.start.length > 0) {
                        trnName += " - ";
                    }
                    trnName += endDatePrefix + " " + window.currentFilter.formatDate(auxProperty.end);
                }

                filter.values.push({
                    id: auxProperty.end + auxProperty.start,
                    name: auxProperty.end + auxProperty.start,
                    trnName: trnName
                });

            } else if (auxProperty.modelType === 'YEAR-SINGLE-VALUE') {
                if (auxProperty.year) {
                    filter.values.push({
                        id: auxProperty.year,
                        name: auxProperty.year,
                        trnName: auxProperty.year
                    });
                }
                filter.trnName = auxProperty.displayName;
            }
        }
        return filter;
    };

    render() {
        return (
            <div className="applied-filters-outer">
                    <div className="applied-filters-inner">
                      {this.showAppliedFilters()}
                    </div>
                  </div>
        );
    }
}

function mapStateToProps(state, ownProps) {
    return {

    }
}

function mapDispatchToProps(dispatch) {
    return {
        actions: bindActionCreators({}, dispatch)
    }
}

export default connect(mapStateToProps, mapDispatchToProps)(AppliedFilters);
