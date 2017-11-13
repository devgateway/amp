import * as AMP from "amp/architecture";
import React from "react";
import { OFFLINE_INSTALLERS } from "amp/config/endpoints";
import { loadTranslations } from "amp/modules/translate";
import DownloadLinks from "./components/DownloadLinks.jsx";

var AMPOfflineDownload = React.createClass( {

    displayName: 'Download AMPOffline Client',

    getInitialState: function() {
        return { translations: [] };
    },

    componentDidMount: function() {
        let toTranslate = new AMP.Model( translations ).toJS();
        loadTranslations( translations ).then( trns => this.updateTranslations( trns ) );
    },

    render: function() {
        return (
            <div>
                <div className='col-md-5'>
                    <img src='/TEMPLATE/reamp/modules/ampoffline/download/images/monitor.png' />
                </div>
                <div className='col-md-7'>
                    <div>
                        <h2>{this.state.translations['amp.offline:download-title']}</h2>
                        <span>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vitae dapibus lectus. Donec sed ipsum neque. Sed orci nunc, fringilla vitae urna sed, porttitor tempor odio. Donec pulvinar accumsan mollis. Phasellus quis tellus interdum, consectetur lacus eget, mollis ante. Quisque turpis est, lobortis vitae ex at, porttitor dapibus neque. Phasellus finibus dui ut sem pellentesque, a tincidunt elit elementum. Sed suscipit, quam in pretium tincidunt, sem nisl pulvinar tortor, sit amet sagittis nibh risus eu erat. Proin ultricies gravida odio at molestie. Vestibulum eros quam, congue dignissim luctus quis, vulputate at risus. Duis feugiat, enim sit amet iaculis mattis, magna neque rhoncus nunc, sed scelerisque libero massa at lectus.</span>
                    </div>
                    <div>
                        <DownloadLinks url={OFFLINE_INSTALLERS} translations={this.state.translations} />
                    </div>
                </div>
            </div>
        );
    },

    updateTranslations: function( translations ) {
        this.setState( { translations: translations } )
    }
} );

var translations = {
    ...DownloadLinks.translations,
    "amp.offline:page-title": "Aid Management Platform - Download Offline Client",
    "amp.offline:download-title": "Download the Offline Client"
}

module.exports = AMPOfflineDownload;