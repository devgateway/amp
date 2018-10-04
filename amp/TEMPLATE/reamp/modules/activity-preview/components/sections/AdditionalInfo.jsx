import React, { Component } from 'react';
import Section from './Section';
import SimpleField from '../fields/SimpleField';
import * as AC from '../../utils/ActivityConstants';


/**
 * Additional Info summary section
 *    
 */
class AdditionalInfo extends Component {

  constructor(props) {
    super(props);
  }

  _buildAdditionalInfo() {
    const { activity, translations, settings, activityInfo }  = this.props.params;
    const additionalInfo = [];
   
    additionalInfo.push(this.props.buildSimpleField(activity, AC.CREATED_BY, settings, true));
    additionalInfo.push(this.props.buildSimpleField(activity, AC.CREATION_DATE, settings, true));
    additionalInfo.push(this.props.buildSimpleField(activity, AC.MODIFIED_BY, settings, true));
    additionalInfo.push(this.props.buildSimpleField(activity, AC.UPDATE_DATE, settings, true));
    additionalInfo.push(this.props.buildSimpleField(activity, AC.TEAM, settings, true));
    additionalInfo.push(<SimpleField key={'computation'}
      title={translations['computation']} value={activityInfo[AC.INFO_ACTIVITY_TEAM][AC.INFO_IS_COMPUTED]
        ? translations['yes']:translations['no'] }
      useInnerHTML={true}
      inline={false}
      separator={false}
      fieldNameClass={this.props.styles.fieldNameClass || ''}
      fieldValueClass={this.props.styles.fieldValueClass || ''} />);

    return additionalInfo;
  }

  render() {
    return <div>{this._buildAdditionalInfo()}</div>;
  }

}

export default Section(AdditionalInfo, 'AdditionalInfo', true, 'AcAdditionalInfo');
