import React, { Component } from 'react';
import SimpleField from '../fields/SimpleField';
import * as AC from '../../utils/ActivityConstants';
import DateUtils from '../../utils/DateUtils';
import ActivityUtils from '../../utils/ActivityUtils';
import { ACTIVITY_FIELDS_FM_PATH } from '../../utils/FieldPathConstants';
require('../../styles/ActivityView.css');

/**
 *    
 */
const Section = (ComposedSection, SectionTitle = null ,useEncapsulateHeader = true, sID) => class extends Component {


  buildSimpleField(activity, fieldPath, settings, showIfNotAvailable, noDataValue, inline = false, title = null,
                   activityFieldsManager, featureManager) {
      if (!featureManager) {
          console.log('feature manager is null ');
          debugger;
      }

      if (activityFieldsManager) {
          // TODO once completed move to only work if its found so we avoid returning null
          const fmPath = ACTIVITY_FIELDS_FM_PATH[fieldPath];
          const isFieldPathEnabled = activityFieldsManager.isFieldPathEnabled(fieldPath);
          const isFieldFmEnabled = !fmPath || featureManager.isFMSettingEnabled(fmPath);
          if (!isFieldPathEnabled || !isFieldFmEnabled) {
              return null;
          }
      }else {
          //TODO this will be moved, leaving only for debugging purposese during implementation
          console.log('Activity manager should not be null ' + fieldPath);
          debugger;
      }

    let title_ = title;
    let value_ = '';
    if (!Array.isArray(activity)) {
      const field = activity[fieldPath];
      if(!field){
          //TODO to remove debugger once fixed
        console.log(fieldPath + ' should not be nothing since its enabled check whats wrong');
        debugger;
        return null;
      }
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
      const { activityFieldsManager, featureManager } = this.props.params;
      if (this.props.sectionPath && !activityFieldsManager.isFieldPathEnabled(this.props.sectionPath)) {
          return null;
      }
      if (this.props.fmPath && !featureManager.isFMSettingEnabled(this.props.fmPath)) {
          return null;
      }
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
