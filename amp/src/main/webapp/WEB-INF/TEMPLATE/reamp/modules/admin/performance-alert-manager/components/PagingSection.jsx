import React, { Component, PropTypes } from 'react';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as startUp from '../actions/StartUpAction';
import Utils from '../common/Utils';
class PagingSection extends Component {
    constructor( props, context ) {
        super( props, context );
        this.state = { recordsPerPage: this.props.page.recordsPerPage};
        this.goToClickedPage = this.goToClickedPage.bind( this );
        this.goToNextPage = this.goToNextPage.bind( this );
        this.goToLastPage = this.goToLastPage.bind( this );
        this.updateRecordsPerPage = this.updateRecordsPerPage.bind( this );
        this.onRecordsPerPageChange= this.onRecordsPerPageChange.bind(this);
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
        const paginationLinks = [];
        for ( let i = 1; i <= this.props.page.totalPageCount; i++ ) {
            const classes = ( i === this.props.page.currentPageNumber ) ? 'active page-item' : 'page-item';
            paginationLinks.push( <li className={classes} key={i}><a data-page={i} className="page-link" onClick={this.goToClickedPage}>{i}</a></li> );
        }
        return paginationLinks;
    }

    updateRecordsPerPage() {
        this.props.updateRecordsPerPage(this.state.recordsPerPage);
    }

    displayPagingInfo() {
        const transParams = {};
        if (this.props.translate) {
            transParams.fromRecord = ( ( this.props.page.currentPageNumber - 1 ) * this.props.page.recordsPerPage ) + 1;
            transParams.toRecord = Math.min(( this.props.page.currentPageNumber * this.props.page.recordsPerPage ), this.props.page.totalRecords );
            transParams.totalRecords = this.props.page.totalRecords;
            transParams.currentPageNumber = this.props.page.currentPageNumber;
            transParams.totalPageCount = this.props.page.totalPageCount;
            return ( <div className="col-md-2 pull-right record-number">
                <div>{this.props.translate( 'amp.performance-rule:records-displayed', transParams )}</div>
                <div>{this.props.translate( 'amp.performance-rule:page-info', transParams )}</div>
             </div> )
            }
    }

    onRecordsPerPageChange(event) {
      if (Utils.isNumber(event.target.value) && parseInt(event.target.value) > 0 ) {
          this.setState( { recordsPerPage: parseInt( this.refs.recordsPerPage.value ) });
      }
    }

    render() {
        if (this.props.page ) {
                   return (
                        <div >
                            <div className="pagination-wrapper row">
                                    <div className="col-md-8">
                                        <ul className="pagination pull-right">
                                            {this.generatePaginationLinks()}
                                            <li className="next"><a onClick={this.goToNextPage}><span aria-hidden="true">&rarr;</span></a></li>
                                            <li className="page-item"><a onClick={this.goToLastPage} className="page-link">&raquo;</a></li>
                                        </ul>
                                    </div>
                                <div className="col-md-2">
                                    <div className="input-group pull-right">
                                        <span className="input-group-addon" id="basic-addon1">
                                            <span className="glyphicon glyphicon-arrow-right" onClick={this.updateRecordsPerPage}></span>
                                        </span>
                                            <input type="text" className="form-control performance-input" ref="recordsPerPage" placeholder="" value={this.state.recordsPerPage} onChange={this.onRecordsPerPageChange}/>
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
    return { actions: bindActionCreators({}, dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( PagingSection );
