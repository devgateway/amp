import React, {Component} from 'react';
import { Route } from 'react-router-dom';
import AdminNDDIndirectProgramApp from "../indirect";
import AdminNDDSDGProgramApp from "../sdg";
import Switch from "react-bootstrap/Switch";

class NDDAdminRouter extends Component {
  render() {
    return (
      <>
          <Switch>
              <Route path="/ndd/indirect" component={AdminNDDIndirectProgramApp} />
              <Route path="/ndd/sdg" component={AdminNDDSDGProgramApp} />
          </Switch>
      </>
    );
  }
}

export default NDDAdminRouter;
