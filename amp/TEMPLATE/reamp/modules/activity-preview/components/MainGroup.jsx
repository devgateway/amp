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
    
    return (<div className={'main_group_container'}>
      <Identification activity={activity} translations={translations} 
        fieldNameClass={'section_field_name'}
        fieldValueClass={'section_field_value'} 
        titleClass={'section_title_class'} 
        groupClass={'section_group_class'}/>
      
    </div>);
  }
}
