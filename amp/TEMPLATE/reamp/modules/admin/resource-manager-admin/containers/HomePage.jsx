// @flow
import { bindActionCreators } from "redux";
import { connect } from "react-redux";
import Home from "../components/HomePage/Home";
import * as HomePageActions from "../actions/HomeActions.jsx";

function mapStateToProps(state) {
    console.log('mapStateToProps');

    return state;
}

function mapDispatchToProps(dispatch, ownProps) {
    console.log('mapDispatchToPropsHomePageActions');
    return bindActionCreators(HomePageActions, dispatch, ownProps);
}

export default connect(mapStateToProps, mapDispatchToProps)(Home);
