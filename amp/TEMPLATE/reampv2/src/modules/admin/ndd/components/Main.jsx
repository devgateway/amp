import React, {Component} from 'react';
import './css/style.css';
import {TranslationContext} from './Startup';
import * as Constants from "../constants/Constants";
import ProgramSelectGroup from "./ProgramSelectGroup";

export default class Main extends Component {

    render() {
        const {translations} = this.context;

        return (<div>
            <div className='col-md-12'>
                <div>
                    <h2>{translations[Constants.TRN_PREFIX + 'title']}</h2>
                </div>
                <div>
                    <ProgramSelectGroup/>
                </div>
            </div>
        </div>);
    }
}
Main.contextType = TranslationContext;
