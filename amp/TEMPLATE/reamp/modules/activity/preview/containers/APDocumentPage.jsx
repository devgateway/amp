import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { APDocument } from 'amp-ui';
import * as ResourceAction from '../actions/ResourceAction';
import RepositoryManager from '../utils/RepositoryManager';

const mapStateToProps = (state) => {
    return {
        resourceReducer: state.resourceReducer,
        openExternal: window.open,
        RepositoryManager
    };
};
function mapDispatchToProps(dispatch, ownProps) {
    return bindActionCreators(ResourceAction, dispatch, ownProps);
}
export const APDocumentPage = connect(mapStateToProps, mapDispatchToProps)(APDocument);
