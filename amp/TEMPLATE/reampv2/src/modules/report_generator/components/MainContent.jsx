import React, { Component } from 'react';
import { Grid, GridColumn } from 'semantic-ui-react';
import MainMenu from './MainMenu';
import ColumnsSection from './sections/ColumnsSection';
import ReportingDetailSection from './sections/ReportingDetailsSection';
import MeasuresSection from './sections/MeasuresSection';

export default class MainContent extends Component {
  constructor() {
    super();
    this.state = { visibleTab: 0 };
  }

  handleMenuClick = (index) => {
    this.setState({ visibleTab: index });
  }

  render() {
    const { visibleTab } = this.state;
    return (
      <>
        <Grid>
          <GridColumn width="4">
            <MainMenu onClick={this.handleMenuClick} />
          </GridColumn>
          <GridColumn width="12">
            <ReportingDetailSection visible={visibleTab === 0} />
            <ColumnsSection visible={visibleTab === 1} />
            <MeasuresSection visible={visibleTab === 2} />
          </GridColumn>
        </Grid>
      </>
    );
  }
}
