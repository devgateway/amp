import React, {Component} from 'react';
import platform from 'platform';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import {getReleases, getReleasesError, getReleasesPending} from '../reducers/startupReducer';
import fetchReleases from '../actions/fetchReleases';
import {
    AMP_OFFLINE_INSTALLERS,
    DEBIAN_LINUX,
    LINUX,
    MAC,
    MACINTOSH,
    OS_X,
    REDHAT_LINUX,
    WINDOWS
} from '../constants/Constants';

import {TranslationContext} from './Startup';

class DownloadLinks extends Component {
  constructor(props) {
    super(props);
    this.shouldComponentRender = this.shouldComponentRender.bind(this);
  }

  componentDidMount() {
    const { fetchReleases } = this.props;
    fetchReleases();
  }

  shouldComponentRender() {
    return !this.props.pending;
  }

  _buildLinksTable() {
    const links = [];
    if (this.props.releases) {
      this.props.releases.sort((i, j) => i.os > j.os).map(i => links.push(<a
        href={`${AMP_OFFLINE_INSTALLERS}/${i.id}`}>
        {this._getInstallerName(i.os, i.arch)}
      </a>));
      return (
        <ul>
          {links.map((l, j) => (<li key={j}>{l}</li>))}
        </ul>
      );
    } else {
      return null;
    }
  }

  _getInstallerName(os, arch) {
    let name = '';
    switch (os) {
      case WINDOWS:
        name = `Windows Vista/7/8/10 - ${arch} ${this.context.translations['amp.offline:bits']}`;
        break;
      case DEBIAN_LINUX:
        name = `Ubuntu Linux (.deb) - ${arch} ${this.context.translations['amp.offline:bits']}`;
        break;
      case REDHAT_LINUX:
        name = `RedHat Linux (.rpm) - ${arch} ${this.context.translations['amp.offline:bits']}`;
        break;
      case MAC:
        name = `Mac OS - ${arch} ${this.context.translations['amp.offline:bits']}`;
        break;
      default:
        name = `${this.context.translations['amp.offline:unknown']}`;
    }
    return name;
  }

  _detectBestInstaller() {
    const { os } = platform;
    const arch = os.architecture;
    const family = os.family.toLowerCase();
    let osNames = null;
    if (family.indexOf(WINDOWS) > -1) {
      osNames = [WINDOWS];
    } else if (family.indexOf(MACINTOSH) > -1 || family.indexOf(OS_X) > -1) {
      osNames = [MAC];
    } else if (family.indexOf(LINUX) > -1) {
      osNames = [DEBIAN_LINUX, REDHAT_LINUX];
    } else {
      return [];
    }
    const installer = this.props.releases.filter(i => (osNames.filter(os => os === i.os).length > 0 && i.arch === arch.toString()));
    const links = installer.map(i => {
      const installerName = this._getInstallerName(i.os, i.arch);
      return (
        <div key={i.id} className="link">
          <a
            href={`${AMP_OFFLINE_INSTALLERS}/${i.id}`}>
            {this.context.translations['amp.offline:download']}
            {' '}
            {i.version}
            {' '}
            -
            {' '}
            {installerName}
          </a>
        </div>
      );
    });
    const message = this.context.translations['amp.offline:best-version-message'];
    return (
      links.length > 0
            && (
            <div className="alert alert-info" role="alert">
              <span className="info-text">{message}</span>
              {links}
            </div>
            ));
  }

  render() {
    if (!this.shouldComponentRender()) {
      return <div>loading</div>;
    } else {
      return (
        <div>
          <div>
            {this._detectBestInstaller()}
          </div>
          <h4>{this.context.translations['amp.offline:all-versions']}</h4>
          <div>
            {this._buildLinksTable()}
          </div>
        </div>
      );
    }
  }
}

DownloadLinks.contextType = TranslationContext;

const mapStateToProps = state => ({
  error: getReleasesError(state.startupReducer),
  releases: getReleases(state.startupReducer),
  pending: getReleasesPending(state.startupReducer),
  translations: state.translationsReducer.translations
});
const mapDispatchToProps = dispatch => bindActionCreators({ fetchReleases }, dispatch);
export default connect(mapStateToProps, mapDispatchToProps)(DownloadLinks);
