import React, { Component } from 'react';
import SimpleField from './SimpleField';
import PercentageField from './PercentageField';
import Tablify from './Tablify';
import ActivityUtils from '../../utils/ActivityUtils';
require('../../styles/ActivityView.css');


const PercentageList = (listField, valueField, percentageField, listTitle = null) => class extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const {activity, translations, settings} = this.props.params;
    const title = translations[listTitle] ? translations[listTitle] : listTitle;
    const items = activity[listField];
    let content = null;
    let isListEnabled = items && items.value && items.value.length;
    if (isListEnabled) {
      content = items.value.map(item => {
        const itemTitle = item[valueField].value ? item[valueField].value : ActivityUtils.getTitle(item[percentageField], settings);
        return (<PercentageField
          key={'IT_' + itemTitle} title={itemTitle} value={item[percentageField].value}
          titleClass={this.props.percentTitleClass} valueClass={this.props.percentValueClass} settings={settings} />);
      });
      if (this.props.tablify) {
        content = <Tablify content={content} columns={this.props.columns} />;
      }
      content = (<SimpleField
        key={listField} title={title} value={content} separator={false} inline={this.props.tablify === true}
        fieldNameClass={this.props.styles.fieldNameClass} fieldValueClass={this.props.styles.fieldValueClass} />);
    } else {
      content = (<SimpleField
        key={listField} title={title} value={this.props.params.translations['amp.activity-preview:noData']} 
        separator={false} inline={this.props.tablify === true}
        fieldNameClass={this.props.styles.fieldNameClass} fieldValueClass={this.props.styles.fieldValueClass} />);
    }
    
    return content;
  }
};

export default PercentageList;
