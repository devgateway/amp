import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';
import SimpleField from '../fields/SimpleField';

/**
 *    
 */
class StatusBar extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    const { buildSimpleField } = this.props;
    const { activity, translations, settings } = this.props.params;
    const noDataValue = translations['amp.activity-preview:noData'];
    const fieldPaths = [AC.AMP_ID, AC.ACTIVITY_STATUS, AC.ACTIVITY_BUDGET];
    const inline = this.props.styles.inline;
    let unitLabel;
    switch (settings[AC.NUMBER_DIVIDER]) {
      case 1000:
        unitLabel = translations['amount_thousands'];
        break;
      case 1000000:
        unitLabel = translations['amount_millions'];
        break;
      default:
        unitLabel = translations['amount_units'];
        break;
    }
    return (
      <div>
        {fieldPaths.map(fieldPath => buildSimpleField(activity, fieldPath, settings, true, noDataValue, inline))}
        <SimpleField key={'UnitKey'} 
        title={translations['unit']} value={unitLabel} 
        useInnerHTML={false}
        inline={inline}
        separator={false}
        fieldNameClass={this.props.styles.fieldNameClass || ''}
        fieldValueClass={this.props.styles.fieldValueClass || ''} />
      </div>
    );
  }
}

export default Section(StatusBar);
