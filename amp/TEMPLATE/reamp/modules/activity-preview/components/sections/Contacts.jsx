import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';
import SimpleField from '../fields/SimpleField';


/**
 * @author Daniel Oliva
 */
const CONTACT_INFORMATION_SOURCE = [AC.DONOR_CONTACT_INFORMATION, AC.SECTOR_MINISTRY_CONTACT_INFORMATION, 
  AC.IMPLEMENTING_AGENCY_CONTACT_INFORMATION];

class Contacts extends Component {

  constructor(props) {
    super(props);
  }

  _createColumns(contactInformation, title){
    if (contactInformation && contactInformation.length > 0 ) {
      let primaryContact = (<div></div>);
      let secondaryContact = (<div></div>);
      contactInformation.forEach(contact => {
        if (contact[AC.MARK_AS_PRIMARY].value === true) {
          primaryContact = (<SimpleField
              key={'contact1-' + title} title={this.props.params.translations['amp.activity-preview:primaryContact']} 
              value={contact[AC.CONTACT].value} separator={false} inline={false}
              fieldNameClass={this.props.styles.fieldNameClass} fieldValueClass={this.props.styles.fieldValueClass} />
          );
        } else {
          secondaryContact = (<SimpleField
              key={'contact2-' + title} title={this.props.params.translations['amp.activity-preview:secondaryContact']}
              value={contact[AC.CONTACT].value} separator={false} inline={false}
              fieldNameClass={this.props.styles.fieldNameClass} fieldValueClass={this.props.styles.fieldValueClass} />
          );
        }
      });
      return (<div>
        <div>
          <div className={'contact_title'}>{title}</div>
        </div>
        <div>
          <div className={'primary_sector'}>{primaryContact}</div>
          <div className={'secondary_sector'}>{secondaryContact}</div>
        </div>
      </div>);
    } else {
      return (<div></div>);
    }
  }

  _getTitle(field){
    return field && field.field_label ? field.field_label.en : '';
  }

  _buildContacts() {
    const { activity, translations } = this.props.params;

    const contacts = [];
    CONTACT_INFORMATION_SOURCE.forEach(source => {
      let contactInfo = activity[source] && activity[source].value ? activity[source].value : undefined;
      if (contactInfo && contactInfo.length > 0) {
        contacts.push(this._createColumns(contactInfo, this._getTitle(activity[source])));
      }
    });
    if (contacts.length < 1) {
      contacts.push(<div key={contactNodata} className={'nodata'}>{translations['amp.activity-preview:noData']}</div>);
    }
    return contacts;
  }

  render() {
    return <div>{this._buildContacts()}</div>;
  }
}

export default Section(Contacts, 'Contacts', true, 'AcContacts');
