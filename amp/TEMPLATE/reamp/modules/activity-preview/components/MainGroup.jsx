import React, { Component } from 'react';
import * as AC from '../utils/ActivityConstants';
import Identification from './sections/Identification';
import InternalIds from './sections/InternalIds';
require('../styles/ActivityView.css');

/**
 * @author Daniel Oliva
 */
export default class MainGroup extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const params = {
      activity: this.props.params.activity,
      translations : this.props.params.translations
    }
    const styles = {
      fieldNameClass : 'section_field_name',
      fieldValueClass : 'section_field_value',
      titleClass : 'section_title_class', 
      groupClass : 'section_group_class'
    }
    
    return (<div className={'main_group_container'}>
      <Identification params={params} styles={styles} />

      <InternalIds params={params} styles={styles}/>
      
    </div>);
  }
}
