import React, { Component } from 'react';
import { Grid, GridColumn } from 'semantic-ui-react';
import MainMenu from './MainMenu';

export default class MainContent extends Component {
  render() {
    return (
      <>
        <Grid>
          <GridColumn width="4">
            <MainMenu />
          </GridColumn>
          <GridColumn width="12">
            content
          </GridColumn>
        </Grid>
      </>
    );
  }
}
