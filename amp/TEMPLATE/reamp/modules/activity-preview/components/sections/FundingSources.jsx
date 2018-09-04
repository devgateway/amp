import React, { Component, PropTypes } from 'react';
import SimpleField from '../fields/SimpleField';
import * as AC from '../../utils/ActivityConstants';


/**
 * Total Number of Funding Sources section
 * @author Daniel Oliva
 */
export default class FundingSources extends Component {

  constructor(props) {
    super(props);
  }

  render() {
    const { activity, translations } = this.props.params;
    const title = translations['FundingSources'];
    let value_ = activity[AC.FUNDINGS] && activity[AC.FUNDINGS].length ? activity[AC.FUNDINGS].length : 0;
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

