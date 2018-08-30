import React, { Component } from 'react';
import * as AC from '../utils/ActivityConstants';
import Identification from './sections/Identification';
import InternalIds from './sections/InternalIds';
import Planning from './sections/Planning';
import Location from './sections/Location'
import NationalPlanObjective from './sections/NationalPlanObjective'
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

      <Planning params={params} styles={this._getBoxStyles()} />

      <Location params={params} tablify styles={this._getBoxStyles()} />

      <NationalPlanObjective params={params} styles={styles} />
      
    </div>);
  }

  _getBoxStyles() {
    return {
      inline : false,
      fieldNameClass : 'box_field_name',
      fieldValueClass : 'box_field_value',
      titleClass : 'section_title_class', 
      groupClass : 'section_group_class'
    }
  }
}
