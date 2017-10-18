import React from "react";
import Link from "react-router";
import ReactDOM from "react-dom";
import platform from "platform";
import { fetchJson } from "amp/tools";
require('./style.less');

const WINDOWS = 'windows';
const MAC = 'mac';
const DEBIAN_LINUX = 'debian';
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
                name = `Windows Vista/7/8/10 - ${arch} ${this.props.translations['amp.offline:bits']}`;
                break;
            case DEBIAN_LINUX:
                name = `Ubuntu Linux (.deb) - ${arch} ${this.props.translations['amp.offline:bits']}`;
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
        let osName = '';
        if ( family.indexOf( WINDOWS ) > -1 ) {
            osName = WINDOWS;
        } else if ( family.indexOf( MACINTOSH ) > -1 ) {
            osName = MAC;
        } else if ( family.indexOf( LINUX ) > -1 ) {
            osName = DEBIAN_LINUX;
        } else {
            return null;
        }
        const installer = this.state.data.filter( i => ( i.os === osName && i.arch === arch.toString() ) );
        if ( installer.length > 0 ) {
            const message = this.props.translations['amp.offline:best-version-message'];
            const installerName = this._getInstallerName( installer[0].os, installer[0].arch );
            const link = <div className="link"><a href={`${this.props.url}/${installer[0].id}`} >{this.props.translations['amp.offline:download']} {installer[0].version} - {installerName}</a></div>;
            return ( <div className="alert alert-info" role="alert"><span className="info-text">{message}</span>{link}</div> );
        }
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