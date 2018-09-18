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
    const { activity, settings }  = this.props.params;
    const additionalInfo = [];
   
    additionalInfo.push(this.props.buildSimpleField(activity, AC.CREATED_BY, settings, true));
    additionalInfo.push(this.props.buildSimpleField(activity, AC.CREATION_DATE, settings, true));
    additionalInfo.push(this.props.buildSimpleField(activity, AC.MODIFIED_BY, settings, true));
    additionalInfo.push(this.props.buildSimpleField(activity, AC.UPDATE_DATE, settings, true));
    additionalInfo.push(this.props.buildSimpleField(activity, AC.TEAM, settings, true));

    return additionalInfo;
  }

  render() {
    return <div>{this._buildAdditionalInfo()}</div>;
  }

}

export default Section(AdditionalInfo, 'AdditionalInfo', true, 'AcAdditionalInfo');
