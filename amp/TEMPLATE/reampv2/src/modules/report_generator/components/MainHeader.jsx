import React, { Component } from 'react';
import {
  Divider, Grid, GridColumn, Header, Segment
} from 'semantic-ui-react';

export default class MainHeader extends Component {
  handleResetAll = () => {
    alert('to be implemented');
  }

  handleCancel = () => {
    alert('to be implemented');
  }

  render() {
    return (
      <>
        <Grid>
          <GridColumn width="6" textAlign="left">
            <Header textAlign="left" size="medium">
              Report Generator
            </Header>
          </GridColumn>
          <GridColumn width="4" textAlign="left" />
          <GridColumn width="6" textAlign="right">
            <span className="green_text bold pointer" onClick={this.handleResetAll}>Reset All</span>
            {/* eslint-disable-next-line jsx-a11y/alt-text */}
            <img
              className="info-icon"
              src="/TEMPLATE/reamp/modules/admin/data-freeze-manager/styles/images/icon-information.svg" />
&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
            <span className="red_text bold pointer" onClick={this.handleCancel}>Cancel</span>
          </GridColumn>
        </Grid>
        <Divider />
        {/* eslint-disable-next-line max-len */}
        <Segment basic>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</Segment>
      </>
    );
  }
}
