import React, { Component } from 'react';
import Section from './Section';
import SimpleField from '../fields/SimpleField';
import Tablify from '../fields/Tablify';
import * as AC from '../../utils/ActivityConstants';
import * as FMC from '../../utils/FeatureManagerConstants';
require('../../styles/ActivityView.css');
import ActivityUtils from '../../utils/ActivityUtils';

/**
 *    
 */
class Planning extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const columnNumber = 3;
    const { activity, translations, settings, activityFieldsManager, featureManager } = this.props.params;
    const inline = this.props.styles.inline;
    let content = [];
    const fieldPaths = [
      AC.PROPOSED_PROJECT_LIFE,
      AC.PROPOSED_APPROVAL_DATE, 
      AC.PROPOSED_COMPLETION_DATE, 
      AC.ACTUAL_COMPLETION_DATE, 
      AC.ACTUAL_APPROVAL_DATE, 
      AC.PROPOSED_START_DATE,
      AC.ACTUAL_START_DATE];
      const __ret = ActivityUtils.calculateDurationOfProjects(activityFieldsManager, activity, settings, translations);
      const showIfNotAvailable = __ret.showIfNotAvailable;
      let duration = __ret.duration;

      if(featureManager.isFMSettingEnabled(FMC.ACTIVITY_DURATION_OF_PROJECT)) {
          content.push(
              <SimpleField key={'Duration'}
                           title={translations['Duration']} value={duration} inline={inline} separator={false}
                           fieldNameClass={this.props.styles.fieldNameClass || ''}
                           fieldValueClass={this.props.styles.fieldValueClass || ''}/>
          );
      }
    
    content = content.concat(fieldPaths.map(fieldPath =>
      this.props.buildSimpleField(activity, fieldPath, settings, showIfNotAvailable.has(fieldPath), inline, false, null,
          activityFieldsManager, featureManager)
    ).filter(data => data !== undefined));

    const tableContent = Tablify.addRows('Planning', content, columnNumber);
    return <div><table className={'box_table'}><tbody>{tableContent}</tbody></table></div>;
  }

}

export default Section(Planning, 'Planning', true, 'AcPlanning');
