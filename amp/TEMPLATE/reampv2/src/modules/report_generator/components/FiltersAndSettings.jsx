import React, { Component } from 'react';
import { Segment } from 'semantic-ui-react';
import Filters from './Filters';
import Settings from './Settings';

export default class FiltersAndSettings extends Component {
  render() {
    return (
      <>
        <Segment placeholder textAlign="left" className="filters_segment">
          <Filters />
          <Settings />
        </Segment>
      </>
    );
  }
}
