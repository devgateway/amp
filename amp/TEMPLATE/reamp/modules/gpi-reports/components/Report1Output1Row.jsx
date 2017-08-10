import React, { Component, PropTypes } from 'react';
import ReactDOM from 'react-dom';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import * as startUp from '../actions/StartUpAction';
import * as Constants from '../common/Constants';
import SupportingEvidencePopup from  './SupportingEvidencePopup'
import { Overlay  } from 'react-bootstrap';
import { Tooltip   } from 'react-bootstrap';
export default class Report1Output1Row extends Component {
    constructor( props, context ) {
        super( props, context ); 
        this.state = {descriptionsExpanded:{Q6: false, Q10: false}, showSupportingEvidence: false};
        this.toggleDescription = this.toggleDescription.bind(this);
        this.toggleSupportingEvidence = this.toggleSupportingEvidence.bind(this);
    }
    
    componentDidMount() {
    }
    
    getLocalizedColumnName( originalColumnName ) {
        let name = originalColumnName;
        if ( this.props.reportData && this.props.reportData.page && this.props.reportData.page.headers ) {
            let header = this.props.reportData.page.headers.filter( header => header.originalColumnName === originalColumnName )[0]
            if ( header ) {
                name = header.columnName;
            }
        }
        return name;
    }       
    
    getListFromString(str){        
       let markup = []; 
       if (str) {
           let elements = str.split(Constants.DELIMETER);              
           elements.forEach((element, i) => {
               markup.push(<li key={i}>{element}</li>);
           });
       }       
       return markup;
    }
   
    toggleDescription(event) {
        let descriptionToggleState = this.state.descriptionsExpanded;
        let question = $(event.target).data("question");
        descriptionToggleState[question] = !descriptionToggleState[question];
        this.setState({descriptionsExpanded: descriptionToggleState})
    }
    
    toggleSupportingEvidence(event) {  
        this.setState({showSupportingEvidence: !this.state.showSupportingEvidence}) 
    }
    
    render() {          
             return (
                    <tr >
                    <td>{this.props.rowData[Constants.PROJECT_TITLE]}</td>
                    <td className="number-column">{this.props.rowData[Constants.Q1]}</td>
                    <td>{this.props.rowData[Constants.Q2]}</td>
                    <td>
                      <ul>
                        {this.getListFromString(this.props.rowData[Constants.Q3])}
                      </ul>
                    </td>
                    <td>
                      <ul>
                        {this.getListFromString(this.props.rowData[Constants.Q4])}
                      </ul>
                    </td>
                    <td>
                      <ul>
                        <ul>
                        {this.getListFromString(this.props.rowData[Constants.Q5])}
                      </ul>
                      </ul>
                    </td>
                    <td >{this.props.rowData[Constants.Q6]}
                        {this.props.rowData[Constants.Q6] == Constants.OPTION_YES &&
                           <span className="glyphicon glyphicon-chevron-down" onClick={this.toggleDescription} data-question="Q6"></span> 
                        }
                                             
                        {this.props.rowData[Constants.Q6] == Constants.OPTION_YES && this.state.descriptionsExpanded[Constants.Q6] &&
                            <div>{this.props.rowData[Constants.Q6_DESCRIPTION]}</div>
                        }                      
                    </td>
                    <td className="number-column">{this.props.rowData[Constants.Q7]}</td>
                    <td className="number-column">{this.props.rowData[Constants.Q8]}</td>
                    <td className="number-column">{this.props.rowData[Constants.Q9]}</td>
                    <td >{this.props.rowData[Constants.Q10]}
                    {this.props.rowData[Constants.Q10] == Constants.OPTION_YES &&
                        <span className="glyphicon glyphicon-chevron-down" onClick={this.toggleDescription} data-question="Q10"></span>
                    }                    
                    {this.props.rowData[Constants.Q10] == Constants.OPTION_YES && this.state.descriptionsExpanded[Constants.Q10] &&
                       <div>{this.props.rowData[Constants.Q10_DESCRIPTION]}</div>
                    }
                    </td>
                    <td className="number-column">{this.props.rowData[Constants.RESULT]}</td>
                    <td className="number-column">{this.props.rowData[Constants.M_E]}</td>
                    <td>                   
                    <div style={{ position: 'relative' }}>
                        {this.state.showSupportingEvidence &&
                           <SupportingEvidencePopup code="1" requestData={{}} toggleSupportingEvidence={this.toggleSupportingEvidence} rowData={this.props.rowData}/> 
                        }
                        <img className="table-icon" ref="showSupportingEvidenceToggle" src="images/icon-download-blue.svg" onClick={this.toggleSupportingEvidence}/>
                    </div>                                          
                    </td>
                    </tr>
            );        
    }

}

function mapStateToProps( state, ownProps ) {
    return {           
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps( dispatch ) {
    return { actions: bindActionCreators({}, dispatch ) }
}

export default connect( mapStateToProps, mapDispatchToProps )( Report1Output1Row );