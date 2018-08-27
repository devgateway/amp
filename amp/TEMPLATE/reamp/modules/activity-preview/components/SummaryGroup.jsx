import React, { Component, PropTypes } from 'react';
import * as AC from '../utils/ActivityConstants';
require('../styles/ActivityView.css');

/**
 * @author Daniel Oliva
 */
export default class SummaryGroup extends Component {

    constructor(props) {
    super(props);
  }

  render() {
    return (
    <div className={'summary_container'}>
      <div className={'summary_section_group'}>
        <div className={'summary_section_title'}><span>Funding</span><span></span>
        </div>
        <div><div>
          <div className={'block'}>
            <div className={'summary_field_name block'}>Funding Name</div>
            <div className={'summary_field_value block'}>Funding Value</div>
          </div>
        </div></div>
      </div>
    </div>);
  }

}
