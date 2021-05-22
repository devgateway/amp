import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { Container, Segment } from 'semantic-ui-react';
import { ReportGeneratorContext } from './StartUp';
import MainHeader from './MainHeader';
import MainContent from './MainContent';
import FiltersAndSettings from './FiltersAndSettings';
import {
  getMetadata, fetchReport, updateProfile, updateId
} from '../actions/stateUIActions';
import { getProfileFromReport } from '../utils/Utils';
import ErrorMessage from './ErrorMessage';
import { TRN_PREFIX } from '../utils/constants';

class ReportGeneratorHome extends Component {
  constructor() {
    super();
    this.state = { showChildren: false, error: false };
  }

  componentDidMount() {
    const {
      _getMetadata, _fetchReport, location, _updateProfile, _updateId
    } = this.props;
    // eslint-disable-next-line react/destructuring-assignment,react/prop-types
    const { id } = this.props.match.params;
    const typeFromURL = new URLSearchParams(location.search).get('type');
    const profileFromURL = new URLSearchParams(location.search).get('profile');
    // If this is a saved report then ignore type and profile params from the URL.
    if (id) {
      _updateId(id);
      return _fetchReport(id).then((action) => {
        const profile = getProfileFromReport(action.payload);
        _updateProfile(profile);
        if (action.payload) {
          return _getMetadata(action.payload.type, profile).then(() => this.setState({ showChildren: true }));
        } else {
          return this.setState({ error: true });
        }
      });
    } else {
      _updateProfile(profileFromURL);
      _getMetadata(typeFromURL, profileFromURL);
      // eslint-disable-next-line react/no-did-mount-set-state
      this.setState({ showChildren: true });
    }
  }

  render() {
    const { showChildren: canLoadChildren, error } = this.state;
    const { translations } = this.props;
    return (
      <Container>
        <MainHeader />
        <FiltersAndSettings loading={!canLoadChildren} />
        {error ? (
          <Segment>
            <ErrorMessage visible message={translations[`${TRN_PREFIX}errorLoadingReport`]} />
          </Segment>
        ) : null}
        <MainContent />
      </Container>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({
  _getMetadata: (type, profile) => getMetadata(type, profile),
  _fetchReport: (id) => fetchReport(id),
  _updateProfile: (data) => updateProfile(data),
  _updateId: (data) => updateId(data),
}, dispatch);

ReportGeneratorHome.propTypes = {
  _getMetadata: PropTypes.func.isRequired,
  _fetchReport: PropTypes.func.isRequired,
  _updateProfile: PropTypes.func.isRequired,
  _updateId: PropTypes.func.isRequired,
  location: PropTypes.object.isRequired,
  translations: PropTypes.object.isRequired,
};

ReportGeneratorHome.defaultProps = {};

ReportGeneratorHome.contextType = ReportGeneratorContext;

export default connect(mapStateToProps, mapDispatchToProps)(ReportGeneratorHome);
