import React, {Component} from "react";
import * as StartupActions from '../actions/startupAction';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';

class HomeButton extends Component {
    render() {
        return (
          <div>
          <button class="btn btn-home" type="button">
            Page d’Accueil
          </button>
          </div>
        );
    }
}

export default HomeButton;
