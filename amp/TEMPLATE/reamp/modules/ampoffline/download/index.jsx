import * as AMP from "amp/architecture";
import React from "react";
import {OFFLINE_INSTALLERS} from "amp/config/endpoints";
import {loadTranslations} from "amp/modules/translate";
import DownloadLinks from "./components/DownloadLinks.jsx";

require('./style.less');

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
                    <div className='c'>
                        <h2>{this.state.translations['amp.offline:download-title']}</h2>
                        <span>{this.state.translations['amp.offline:text']}</span>
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
    "amp.offline:download-title": "Download the Offline Client",
    "amp.offline:text": "The AMP Offline application allows you to edit and add activity information to the AMP without having an active internet connection. In order to use the application, you must download and install the compatible version of AMP Offline application from the list of the latest AMP Offline installers. When you run the application for the first time, you must have an active internet connection in order to sync your user data, activity data, and other critical data to the application. After that, you may work offline and sync your data periodically."
}

module.exports = AMPOfflineDownload;
