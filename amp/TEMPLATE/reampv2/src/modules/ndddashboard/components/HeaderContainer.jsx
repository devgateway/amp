import React, { Component } from 'react';
import Filters from './Filters';
import Settings from './Settings';
import Share from './Share';

export default class HeaderContainer extends Component {

  render() {
    return (
      <div>
        <Filters/>
        <Settings/>
        <Share/>
      </div>
    );
  }
}
