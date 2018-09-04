import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';


/**
 * Additional Info summary section
 * @author Daniel Oliva
 */
class AdditionalInfo extends Component {

  constructor(props) {
    super(props);
  }

  _buildAdditionalInfo() {
    const { activity }  = this.props.params;
    const additionalInfo = [];
   
    additionalInfo.push(this.props.buildSimpleField(activity, AC.CREATED_BY, true));
    additionalInfo.push(this.props.buildSimpleField(activity, AC.CREATION_DATE, true));
    additionalInfo.push(this.props.buildSimpleField(activity, AC.MODIFIED_BY, true));
    additionalInfo.push(this.props.buildSimpleField(activity, AC.UPDATE_DATE, true));
    additionalInfo.push(this.props.buildSimpleField(activity, AC.TEAM, true));

    return additionalInfo;
  }

  render() {
    return <div>{this._buildAdditionalInfo()}</div>;
  }

}

export default Section(AdditionalInfo, 'AdditionalInfo', true, 'AcAdditionalInfo');
