import React, {Component, Suspense} from 'react';
import Nav from "react-bootstrap/Nav";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {getRootUrl} from "../../../../utils/Utils";

class NDDAdminNavigator extends Component {
  render() {
      const {translations} = this.props;
      const trnPrefix = "amp.admin.ndd:";
      return (
      <>
          <Nav activeKey="#/ndd/indirect" as="ul" variant="pills">
              <Nav.Item as="li">
                  <Nav.Link href={`${getRootUrl()}/ndd/indirect-programs`}><b>{translations[trnPrefix + "menu-item-indirect-programs-mapping"]}</b></Nav.Link>
              </Nav.Item>
              <Nav.Item as="li">
                  <Nav.Link href={`${getRootUrl()}/ndd/programs`}><b>{translations[trnPrefix + "menu-item-programs-mapping"]}</b></Nav.Link>
              </Nav.Item>
          </Nav>
      </>
    );
  }
}

const mapStateToProps = state => ({
    translations: state.translationsReducer.translations,
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch)

export default connect(mapStateToProps, mapDispatchToProps)(NDDAdminNavigator);
