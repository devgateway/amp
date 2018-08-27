import React, { Component } from 'react';
import * as AC from '../utils/ActivityConstants';
import Identification from './sections/Identification';
require('../styles/ActivityView.css');

/**
 * @author Daniel Oliva
 */
export default class MainGroup extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const activity = this.props.activity;
    const translations = this.props.translations;
    const fieldNameClass = this.props.fieldNameClass;
    const fieldValueClass = this.props.fieldValueClass;
    const titleClass = this.props.titleClass;
    const groupClass = this.props.groupClass;
    
    return (<div className={'main_group_container'}>
      <Identification activity={activity} translations={translations} fieldNameClass={fieldNameClass}
        fieldValueClass={fieldValueClass} titleClass={titleClass} groupClass={groupClass}/>
      
    </div>);
  }
}
