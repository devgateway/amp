import React, { Component, PropTypes } from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import * as startUp from '../actions/StartUpAction.jsx';
require('../styles/main.less');
require('bootstrap');
import Report9b from "../components/Report9b";

export default class App extends Component {

    constructor(props, context) {      
        super(props, context);
       
    }
    
    componentWillMount() {       
    }
    
    render() {       
        
      return (
            <div >             
             <div className="title-bar">
             <div className="container">
               <h2>{this.props.translations['amp.gpi-reports:title']}</h2>
             </div>
           </div>
             <div >
             <div>
              <ul className="nav nav-tabs indicator-tabs" role="tablist">                  
             <li role="presentation" className="active"><a href="#indicator1" aria-controls="indicator1" role="tab" data-toggle="tab">{this.props.translations['amp.gpi-reports:indicator1-title']}</a></li>                
             <li role="presentation" ><a href="#indicator5a" aria-controls="indicator5a" role="tab" data-toggle="tab">{this.props.translations['amp.gpi-reports:indicator5a-title']}</a></li>
             <li role="presentation" ><a href="#indicator5b" aria-controls="indicator5b" role="tab" data-toggle="tab">{this.props.translations['amp.gpi-reports:indicator5b-title']}</a></li>
             <li role="presentation"  ><a href="#indicator6" aria-controls="indicator6" role="tab" data-toggle="tab">{this.props.translations['amp.gpi-reports:indicator6-title']}</a></li>
             <li role="presentation" ><a href="#indicator9b" aria-controls="indicator9b" role="tab" data-toggle="tab">{this.props.translations['amp.gpi-reports:indicator9b-title']}</a></li>
             </ul>
             <div className="tab-content panel">
               <div role="tabpanel" className="tab-pane active" id="indicator1">indicator 1
               Lorem ipsum dolor sit amet, ne putent possit sit. Et fabulas voluptua delicata usu. Sit volumus omittam ut. Id fuisset indoctum vis, sit ei mentitum probatus, duo ei error fierent reprehendunt. Nec at sonet mnesarchum appellantur. Congue nostro id est.

               Mel nisl definiebas ne. Nam equidem nostrum cu, laudem laoreet ancillae an vel. Invenire assentior ea sed, iusto aliquip lobortis ad sea. Eu per probo eruditi pericula. Rebum meliore democritum pro ea. Per in utinam diceret laboramus, cum cibo legimus id.

               Sed no enim hendrerit interpretaris, et eos utamur nusquam omittantur. Voluptua pertinax in sea, no eos aeque partem commodo. Eu sit munere atomorum, populo adipisci id vix. Ludus option aperiam per ex, mel meis timeam ea, minimum tacimates mei ea. Inani intellegebat te cum, repudiare gubergren has cu, ut fabulas partiendo comprehensam sed. Pro ignota referrentur id, mea te ullum eleifend repudiare.

               In vero possim petentium ius, id sale augue repudiandae pri. No odio regione officiis quo, id justo oporteat complectitur vix. Sea at legere suavitate voluptatum, sit impetus legimus an, eam omnium commodo convenire id. Ius menandri consequuntur ad, te eum odio erat comprehensam. Te velit postea quo.

               Duo soluta interpretaris ut, sea iisque constituto an, albucius sententiae ea vel. Cu vel magna zril suscipit, ad quot eripuit tibique sed. Vix option interpretaris ut, ferri vocent indoctum ex mei. An dolorum perfecto abhorreant eum, per an dicat mundi.
               </div>
               <div role="tabpanel" className = 'tab-pane' id="indicator5a"> Indicator 5a 
               Lorem ipsum dolor sit amet, ne putent possit sit. Et fabulas voluptua delicata usu. Sit volumus omittam ut. Id fuisset indoctum vis, sit ei mentitum probatus, duo ei error fierent reprehendunt. Nec at sonet mnesarchum appellantur. Congue nostro id est.

               Mel nisl definiebas ne. Nam equidem nostrum cu, laudem laoreet ancillae an vel. Invenire assentior ea sed, iusto aliquip lobortis ad sea. Eu per probo eruditi pericula. Rebum meliore democritum pro ea. Per in utinam diceret laboramus, cum cibo legimus id.

               Sed no enim hendrerit interpretaris, et eos utamur nusquam omittantur. Voluptua pertinax in sea, no eos aeque partem commodo. Eu sit munere atomorum, populo adipisci id vix. Ludus option aperiam per ex, mel meis timeam ea, minimum tacimates mei ea. Inani intellegebat te cum, repudiare gubergren has cu, ut fabulas partiendo comprehensam sed. Pro ignota referrentur id, mea te ullum eleifend repudiare.

               In vero possim petentium ius, id sale augue repudiandae pri. No odio regione officiis quo, id justo oporteat complectitur vix. Sea at legere suavitate voluptatum, sit impetus legimus an, eam omnium commodo convenire id. Ius menandri consequuntur ad, te eum odio erat comprehensam. Te velit postea quo.

               Duo soluta interpretaris ut, sea iisque constituto an, albucius sententiae ea vel. Cu vel magna zril suscipit, ad quot eripuit tibique sed. Vix option interpretaris ut, ferri vocent indoctum ex mei. An dolorum perfecto abhorreant eum, per an dicat mundi.
               </div>              
               <div role="tabpanel" className = 'tab-pane' id="indicator5b"> Indicator 5b </div>
               <div role="tabpanel" className = 'tab-pane' id="indicator6"> Indicator 6 </div>
               <div role="tabpanel" className = 'tab-pane fade in' id="indicator9b"> <Report9b/> </div>
             </div>

           </div>
              
         </div>
            </div>
        );
    }
}

function mapStateToProps(state, ownProps) { 
    return {
        translations: state.startUp.translations,
        translate: state.startUp.translate
    }
}

function mapDispatchToProps(dispatch) {
    return {actions: bindActionCreators({}, dispatch)}
}

export default connect(mapStateToProps, mapDispatchToProps)(App);

