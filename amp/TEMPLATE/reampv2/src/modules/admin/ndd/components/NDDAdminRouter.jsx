import React, {Component} from 'react';
import { Route } from 'react-router-dom';
import AdminNDDIndirectProgramApp from "../indirect";
import AdminNDDProgramApp from "../program";
import Switch from "react-bootstrap/Switch";

class NDDAdminRouter extends Component {
  render() {
    return (
      <>
          <Switch>
              <Route path="/ndd/indirect-programs" component={AdminNDDIndirectProgramApp} />
              <Route path="/ndd/programs" component={AdminNDDProgramApp} />
          </Switch>
      </>
    );
  }
}

export default NDDAdminRouter;
