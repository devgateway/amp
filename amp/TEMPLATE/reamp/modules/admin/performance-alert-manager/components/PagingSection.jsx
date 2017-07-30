import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as startUp from '../actions/StartUpAction';
import * as commonListsActions from '../actions/CommonListsActions';
export default class PagingSection extends Component {
    constructor( props, context ) {
        super( props, context );     
        this.state = { recordsPerPage: 10};
        this.goToClickedPage = this.goToClickedPage.bind( this );
        this.goToNextPage = this.goToNextPage.bind( this );
        this.goToLastPage = this.goToLastPage.bind( this );
        this.updateRecordsPerPage = this.updateRecordsPerPage.bind( this );        
    }

    componentDidMount() { 
        
    }    
    
    goToClickedPage( event ) {
        const pageNumber = parseInt( event.target.getAttribute( 'data-page' ) );
        this.props.goToPage( pageNumber );
    }

    goToNextPage() {
        const pageNumber = this.props.page.currentPageNumber + 1;
        if ( pageNumber <= this.props.page.totalPageCount ) {
            this.props.goToPage( pageNumber );
        }
    }

    goToLastPage() {
        this.props.goToPage( this.props.page.totalPageCount );
    }
    
    
    
    generatePaginationLinks() {
        var paginationLinks = [];
        for ( var i = 1; i <= this.props.page.totalPageCount; i++ ) {
            var classes = ( i === this.props.page.currentPageNumber ) ? 'active page-item' : 'page-item';
            paginationLinks.push( <li className={classes} key={i}><a data-page={i} className="page-link" onClick={this.goToClickedPage}>{i}</a></li> );
        }
        return paginationLinks;
    }
    
    updateRecordsPerPage() {
        if ( this.refs.recordsPerPage && this.refs.recordsPerPage.value ) {
            this.setState( { recordsPerPage: parseInt( this.refs.recordsPerPage.value ) }, function() {
                this.props.updateRecordsPerPage(this.state.recordsPerPage);
            }.bind( this ) );
        }
    }
    
    displayPagingInfo() {
        var transParams = {};
        if (this.props.translate) {
            transParams.fromRecord = ( ( this.props.page.currentPageNumber - 1 ) * this.props.page.recordsPerPage ) + 1;
            transParams.toRecord = Math.min(( this.props.page.currentPageNumber * this.props.page.recordsPerPage ), this.props.page.totalRecords );
            transParams.totalRecords = this.props.page.totalRecords;
            transParams.currentPageNumber = this.props.page.currentPageNumber;
            transParams.totalPageCount = this.props.page.totalPageCount;
            return ( <div className="col-md-3 pull-right record-number">
                <div>{this.props.translate( 'amp.performance-rule:records-displayed', transParams )}</div>
                <div>{this.props.translate( 'amp.performance-rule:page-info', transParams )}</div>
             </div> )
            } 
    }    

    render() {
        if (this.props.page ) {
                   return (               
                        <div className="row">
                            <div className="col-md-8 pull-right pagination-wrapper">
                                
                                    <div className="col-md-4">
                                        <ul className="pagination">
                                            {this.generatePaginationLinks()}
                                            <li className="next"><a onClick={this.goToNextPage}><span aria-hidden="true">&rarr;</span></a></li>
                                            <li className="page-item"><a onClick={this.goToLastPage} className="page-link">&raquo;</a></li>
                                        </ul>
                                    </div>
                                
                                <div className="col-md-3">
                                </div>
                                <div className="col-md-2">
                                    <div className="input-group pull-right">
                                        <span className="input-group-addon" id="basic-addon1">
                                            <span className="glyphicon glyphicon-arrow-right" onClick={this.updateRecordsPerPage}></span>
                                        </span>
                                        <input type="text" className="form-control" ref="recordsPerPage" placeholder="" defaultValue={this.props.page.recordsPerPage} />
                                    </div>
                                </div>
                                {this.displayPagingInfo()}
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
        translate: state.startUp.translate,       
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators( Object.assign( {}, commonListsActions ), dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( PagingSection );