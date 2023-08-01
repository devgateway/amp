// @flow
import { bindActionCreators } from "redux";
import { connect } from "react-redux";
import App from "../components/Layout/App.jsx";
import * as AppActions from "../actions/AppActions";

function mapStateToProps(state) {
    console.log('mapStateToProps');

    return state;
}

function mapDispatchToProps(dispatch, ownProps) {
    console.log('mapDispatchToPropsHomePageActions');
    return bindActionCreators(AppActions, dispatch, ownProps);
}

export default connect(mapStateToProps, mapDispatchToProps)(App);
