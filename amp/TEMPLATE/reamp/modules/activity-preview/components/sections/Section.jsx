import React, { Component } from 'react';
import SimpleField from '../fields/SimpleField';
import * as AC from '../../utils/ActivityConstants';
import DateUtils from '../../utils/DateUtils';
import ActivityUtils from '../../utils/ActivityUtils';
require('../../styles/ActivityView.css');

/**
 *    
 */
const Section = (ComposedSection, SectionTitle = null ,useEncapsulateHeader = true, sID) => class extends Component {


  buildSimpleField(activity, fieldPath, settings, showIfNotAvailable, noDataValue, inline = false, title) {
    let title_ = title;
    let value_ = '';
    if (!Array.isArray(activity)) {
      const field = activity[fieldPath];   
      title_ = title ? title : ActivityUtils.getTitle(field, settings);
      value_ = field.value;
      if (field.field_type === 'date') {
        value_ = DateUtils.createFormattedDate(value_, settings);
      }
      if (value_ === '' || value_ === null) {
        value_ = noDataValue;
      }
    } else {
      for (var id in activity) {
        value_+= '<li>' + activity[id][fieldPath].value + '</li>';
      }
    }

    if (showIfNotAvailable === true || (value_ !== undefined && value_ !== null)) {
      const useInnerHTML = AC.RICH_TEXT_FIELDS.has(fieldPath);
      return (<SimpleField key={title_ + Math.random()} 
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
    const sectionTitle = SectionTitle ? translations[SectionTitle] + (this.props.titleExtra ? this.props.titleExtra : '') : '';
    return (<div key={sectionKey} className={this.props.styles.groupClass} id={sID}>
      <div className={this.props.styles.titleClass}>
        <span>{sectionTitle}</span>
        <span>{this.props.styles.titleDetails}</span>
      </div>
      <div className={this.props.styles.composedClass}>
        {composedSection}
      </div>
    </div>);
  }
};



export default Section;
