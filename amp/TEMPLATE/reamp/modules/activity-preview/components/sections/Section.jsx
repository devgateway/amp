import React from 'react';
import SimpleField from '../fields/SimpleField';
require('../../styles/ActivityView.css');



export default function buildSimpleField(activity, fieldPath, showIfNotAvailable, noDataValue, inline = false, options) {
    const field = activity[0][fieldPath];
    const options_ = options || {};
    const title_ = (field.field_label.en ? field.field_label.en : '');
    let value_ = field.value;
    
    if (value_ === '' || value_ === null) {
      value_ = noDataValue;
    }
    if (showIfNotAvailable === true || (value_ !== undefined && value_ !== null)) {
      return (<SimpleField
        title={title_} value={value_} inline={inline}
        separator={false}
        fieldNameClass={options_.fieldNameClass || ''}
        fieldValueClass={options_.fieldValueClass || ''} />);
    }
    
}

