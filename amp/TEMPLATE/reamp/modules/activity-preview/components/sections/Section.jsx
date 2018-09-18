import React, { Component } from 'react';
import SimpleField from '../fields/SimpleField';
import * as AC from '../../utils/ActivityConstants';
import DateUtils from '../../utils/DateUtils';
import ActivityUtils from '../../utils/ActivityUtils';
require('../../styles/ActivityView.css');

/**
 * @author Daniel Oliva
 */
const Section = (ComposedSection, SectionTitle = null, useEncapsulateHeader = true, sID) => class extends Component {


  buildSimpleField(activity, fieldPath, settings, showIfNotAvailable, noDataValue, inline = false, title) {
    const field = activity[fieldPath];
    //TODO
    const title_ = title ? title : ActivityUtils.getTitle(field, settings);
    let value_ = field.value;
    if (field.field_type === 'date') {
      value_ = DateUtils.createFormattedDate(value_, settings);
    }
    if (value_ === '' || value_ === null) {
      value_ = noDataValue;
    }
    if (showIfNotAvailable === true || (value_ !== undefined && value_ !== null)) {
      const useInnerHTML = AC.RICH_TEXT_FIELDS.has(fieldPath);
      return (<SimpleField key={title_ + fieldPath} 
        title={title_} value={value_} 
        useInnerHTML={useInnerHTML}
        inline={inline}
        separator={false}
        fieldNameClass={this.props.styles.fieldNameClass || ''}
        fieldValueClass={this.props.styles.fieldValueClass || ''} />);
    }
    
  }

  render() {
    const translations = this.props.params.translations;
    const sectionKey = SectionTitle + '-Section';
    const composedSection = (<ComposedSection key={sectionKey}
      {...this.props} {...this.state} {...this.context} buildSimpleField={this.buildSimpleField.bind(this)} />);

    return (<div key={sectionKey} className={this.props.styles.groupClass} id={sID}>
      <div className={this.props.styles.titleClass}>
        <span>{translations[SectionTitle]} </span><span>{this.props.styles.titleDetails}</span>
      </div>
      <div className={this.props.styles.composedClass}>
        {composedSection}
      </div>
    </div>);
  }
};



export default Section;
