import React, { Component } from 'react';
import { Route } from 'react-router-dom';
import Switch from 'react-bootstrap/Switch';
import AdminNDDIndirectProgramApp from '../indirect';
import AdminNDDProgramApp from '../program';

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
