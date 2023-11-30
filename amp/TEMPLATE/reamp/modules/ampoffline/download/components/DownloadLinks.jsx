import React from "react";
import Link from "react-router";
import ReactDOM from "react-dom";
import platform from "platform";
import { fetchJson } from "amp/tools";
require('./style.less');

const WINDOWS = 'windows';
const MAC = 'osx';
const DEBIAN_LINUX = 'debian';
const REDHAT_LINUX = 'redhat';
const MACINTOSH = 'macintosh';
const LINUX = 'linux';

var DownloadLinks = React.createClass( {

    displayName: '',

    getInitialState: function() {
        return { data: [] };
    },

    componentDidMount: function() {
        fetchJson( this.props.url ).then( installers => this.setState( { data: installers } ) );
    },

    _buildLinksTable() {
        const links = [];
        if ( this.state.data ) {
            this.state.data.sort(( i, j ) => i.os > j.os ).map( i => {
                links.push( <a href={`${this.props.url}/${i.id}`} >{this._getInstallerName( i.os, i.arch )}</a> );
            } );
            return ( <ul>
                {links.map( (l, j) => ( <li key={j}>{l}</li> ) )}
            </ul> );
        } else {
            return null;
        }
    },

    _getInstallerName( os, arch ) {
        let name = '';
        switch ( os ) {
            case WINDOWS:
                name = `Windows 10/11 - ${arch} ${this.props.translations['amp.offline:bits']}`;
                break;
            case DEBIAN_LINUX:
                name = `Ubuntu Linux (.deb) - ${arch} ${this.props.translations['amp.offline:bits']}`;
                break;
            case REDHAT_LINUX:
                name = `RedHat Linux (.rpm) - ${arch} ${this.props.translations['amp.offline:bits']}`;
                break;
            case MAC:
                name = `Mac OS - ${arch} ${this.props.translations['amp.offline:bits']}`;
                break;
        }
        return name;
    },

    _detectBestInstaller() {
        const os = platform.os;
        const arch = os.architecture;
        const family = os.family.toLowerCase();
        let osNames = null;
        if ( family.indexOf( WINDOWS ) > -1 ) {
            osNames = [WINDOWS];
        } else if ( family.indexOf( MACINTOSH ) > -1 ) {
            osNames = [MAC];
        } else if ( family.indexOf( LINUX ) > -1 ) {
            osNames = [DEBIAN_LINUX, REDHAT_LINUX];
        } else {
            return [];
        }
        const installer = this.state.data.filter( i => ( osNames.filter(os => os === i.os).length > 0 && i.arch === arch.toString() ) );        
        const links = installer.map(i => {            
            const installerName = this._getInstallerName( i.os, i.arch );
            return (<div key={i.id} className="link"><a href={`${this.props.url}/${i.id}`} >{this.props.translations['amp.offline:download']} {i.version} - {installerName}</a></div>);            
        });
        const message = this.props.translations['amp.offline:best-version-message'];
        return ( <div className="alert alert-info" role="alert"><span className="info-text">{message}</span>{links}</div> );
    },

    render: function() {
        return (
            <div>
                <div>
                    {this._detectBestInstaller()}
                </div>
                <h4>{this.props.translations['amp.offline:all-versions']}</h4>
                <div>
                    {this._buildLinksTable()}
                </div>
            </div>
        );
    }
} );

DownloadLinks.translations = {
    "amp.offline:download": "Download AMP Offline",
    "amp.offline:all-versions": "All installer versions",
    "amp.offline:best-version-message": "We have automatically detected which version of the application meets your operating system requirements. Other versions are available below.",
    "amp.offline:bits": "bits"
};

module.exports = DownloadLinks;