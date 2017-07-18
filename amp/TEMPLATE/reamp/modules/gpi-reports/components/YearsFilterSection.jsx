import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as startUp from '../actions/StartUpAction.jsx';
export default class YearsFilterSection extends Component {
    constructor( props, context ) {
        super( props, context );        
         this.onYearClick = this.onYearClick.bind( this );
        
    }

    componentDidMount() {        
    }    
    
    onYearClick( event ) {
        this.props.onYearClick($( event.target ).data( "year" ));
    }
    
    showSelectedDates() {
        var displayDates = '';
        if(this.props.filter){
            var filters = this.props.filter.serialize().filters;            
            if (filters[this.props.dateField]) {
                filters[this.props.dateField].start = filters[this.props.dateField].start || '';
                filters[this.props.dateField].end = filters[this.props.dateField].end || '';
                var startDatePrefix = ( filters[this.props.dateField].start.length > 0 && filters[this.props.dateField].end.length === 0 ) ? this.props.translations['amp.gpi-reports:from'] : '';
                var endDatePrefix = ( filters[this.props.dateField].start.length === 0 && filters[this.props.dateField].end.length > 0 ) ? this.props.translations['amp.gpi-reports:until'] : '';
                if ( filters[this.props.dateField].start.length > 0 ) {
                    displayDates = startDatePrefix + " " + this.props.filter.formatDate( filters[this.props.dateField].start );
                }

                if ( filters[this.props.dateField].end.length > 0 ) {
                    if ( filters[this.props.dateField].start.length > 0 ) {
                        displayDates += " - ";
                    }
                    displayDates += endDatePrefix + " " + this.props.filter.formatDate( filters[this.props.dateField].end );
                }
            } 
        }
        return displayDates;
    }

    render() {
        if ( this.props.mainReport && this.props.mainReport.page ) {
                var years = this.props.years.slice();
                console.log(years);
                return (
                           <div>
                           <div className="container-fluid no-padding">
                           <ul className="year-nav">
                               <li className={this.props.selectedYear ? '' : 'active'}>
                                   <a onClick={this.onYearClick}>{this.props.translations['amp.gpi-reports:all-years']}</a>
                               </li>
                               {( ( years.length > 3 ) ? years.splice( years.length - 3, 3 ).reverse() : years.reverse() ).map( year =>
                                   <li className={this.props.selectedYear == year ? 'active' : ''} key={year}><a data-year={year} onClick={this.onYearClick}>{year}</a></li>
                               )}
                               <li >
                                   <div className="dropdown">
                                       <a className={years.includes( this.props.selectedYear ) ? 'btn dropdown-toggle btn-years btn-years-active' : 'btn dropdown-toggle btn-years'} type="button" id="years" data-toggle="dropdown">
                                           {this.props.translations['amp.gpi-reports:other-years']}
                                           <span className="caret"></span></a>
                                       <ul className="dropdown-menu dropdown-years" role="menu">
                                           {years.reverse().map( year =>
                                               <li role="presentation" className={this.props.selectedYear == year ? 'active' : ''} key={year}><a data-year={year} onClick={this.onYearClick}>{year}</a></li>
                                           )}

                                       </ul>
                                   </div>
                               </li>
                           </ul>
                       </div>
                       <div className="selection-legend">
                           <div className="pull-right">{this.showSelectedDates()}</div>
                       </div>   
                      </div>
                           
            );
        }

        return ( <div></div> );
    }

}

function mapStateToProps( state, ownProps ) {
    return {
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps( dispatch ) {
    return {actions: bindActionCreators({}, dispatch)}
}

export default connect( mapStateToProps, mapDispatchToProps )( YearsFilterSection );
