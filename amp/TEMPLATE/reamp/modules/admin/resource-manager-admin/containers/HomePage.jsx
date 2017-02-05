// @flow
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import HomePage from '../components/HomePage/HomePage';
import * as HomePageActions from '../actions/HomePageActions.jsx';

function mapStateToProps(state) {
    console.log('mapStateToProps');

    return state;
}

function mapDispatchToProps(dispatch, ownProps) {
    console.log('mapDispatchToPropsHomePageActions');
    return bindActionCreators(HomePageActions, dispatch, ownProps);
}

export default connect(mapStateToProps, mapDispatchToProps)(HomePage);
