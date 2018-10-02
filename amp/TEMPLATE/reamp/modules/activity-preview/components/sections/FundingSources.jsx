import React, { Component } from 'react';
import SimpleField from '../fields/SimpleField';
import * as AC from '../../utils/ActivityConstants';


/**
 * Total Number of Funding Sources section
 *    
 */
export default class FundingSources extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const { activity, translations } = this.props.params;
    const title = translations['FundingSources'];
    var sources = new Set();
    if (activity[AC.FUNDINGS] && activity[AC.FUNDINGS].value.length) {
      activity[AC.FUNDINGS].value.forEach(v=> {sources.add('[' + v.donor_organization_id.value + '] ')});
    }
    let value_ = sources.size + ' ';
    sources.forEach(x => value_ += x); 
    const content = <SimpleField key={title}
    title={title} value={value_} 
    useInnerHTML={false}
    inline={false}
    separator={false}
    fieldNameClass={'section_title_class'}
    fieldValueClass={this.props.styles.fieldValueClass || ''} />
    return (<div key={'AcFundingSources'} id={'AcFundingSources'}>{content}</div>);
  }
}

