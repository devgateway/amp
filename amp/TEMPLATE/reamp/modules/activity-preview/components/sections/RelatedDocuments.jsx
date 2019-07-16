import React, { Component } from 'react';
import Section from './Section';
import * as AC from '../../utils/ActivityConstants';
import { createFormattedDate } from '../../utils/DateUtils';
import TopIcon from '../fields/TopIcon';
import SimpleField from '../fields/SimpleField';

class RelatedDocuments extends Component {

    constructor(props) {
      super(props);
    }

    renderDocument(document) {
        const { settings, translations } = this.props.params;
        const url = document[AC.DOC_WEB_LINK] ? document[AC.DOC_WEB_LINK] : document[AC.DOC_URL];
        const icon = document[AC.DOC_WEB_LINK] ? '/TEMPLATE/reamp/modules/activity-preview/styles/images/goto_url.svg' :
            '/TEMPLATE/reamp/modules/activity-preview/styles/images/download.svg';
        const docName = document[AC.DOC_WEB_LINK] ? document[AC.DOC_WEB_LINK] + '...' : document[AC.FILE_NAME];
        const date = createFormattedDate(document[AC.DOC_DATE], settings);
        return (
            <div key={document.uuid} className={'box_table'}>
                <div key="doc-title" className={'document_title'}>
                    <span key="header" className={'section_subtitle_class'}>
                        <span><b>{document[AC.DOC_TITLE]}</b></span>
                        <span>&nbsp;&nbsp;-&nbsp;&nbsp;</span>
                        <span className={'linkDocument'} title={docName}>{docName.substring(0, 45)}</span>
                    </span>
                    {url &&
                        <span key="download" className={'iconDocument'}>
                            <TopIcon link={url} img={icon} 
                                tooltip={translations['clickToDownload']} 
                                target={'_blank'} />
                        </span>
                    }
                </div>
                <div key="content">
                    {<SimpleField key={'DocDesc_' + document.uuid} 
                        title={translations['description']}
                        value={document[AC.DOC_DESC]}  separator={false}
                        fieldNameClass={this.props.styles.fieldNameClass || ''}
                        fieldValueClass={this.props.styles.fieldValueClass || ''} />
                    }
                    {<SimpleField key={'DocDate_' + document.uuid} 
                        title={translations['adding_date']}
                        value={date} separator={false}
                        fieldNameClass={this.props.styles.fieldNameClass || ''}
                        fieldValueClass={this.props.styles.fieldValueClass || ''} />
                    }
                </div>
            </div>
        );
    }
  
    render() {
      const { activity, translations, settings } = this.props.params;
      let content = [];
      const docs = activity[AC.ACTIVITY_DOCUMENTS];
      if (docs && docs.value && docs.value.length > 0) {
        for(var docId in docs.value) {
          content.push(this.renderDocument(docs.value[docId]));
        }
      } else {
        content.push(
            <SimpleField key={'Documents'} 
                value={translations['amp.activity-preview:noData']}  separator={false}
                fieldNameClass={this.props.styles.fieldNameClass || ''}
                fieldValueClass={this.props.styles.fieldValueClass || ''} />
        );
      }
      return (<div>
          {content}
      </div>);
  }

}

export default Section(RelatedDocuments, 'RelatedDocuments', true, 'AcRelatedDocuments');