import React, { Component } from 'react';
import { Header, Segment } from 'semantic-ui-react';

export default class Filters extends Component {
  render() {
    return (
      <>
        <Segment placeholder textAlign="left" className="filters_segment">
          <Header size="small">Filters</Header>
        </Segment>
      </>
    );
  }
}
