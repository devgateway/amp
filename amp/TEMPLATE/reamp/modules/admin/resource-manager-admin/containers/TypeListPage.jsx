// @flow
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import TypeList from '../components/TypeManager/TypeList.jxs';
import * as TypeListAction from '../actions/TypeListAction.jsx';

function mapStateToProps(state) {
    console.log('mapStateToProps');

    return state;
}

function mapDispatchToProps(dispatch, ownProps) {
    console.log('mapDispatchToPropsHomePageActions');
    return bindActionCreators(TypeListAction, dispatch, ownProps);
}

export default connect(mapStateToProps, mapDispatchToProps)(TypeList);
