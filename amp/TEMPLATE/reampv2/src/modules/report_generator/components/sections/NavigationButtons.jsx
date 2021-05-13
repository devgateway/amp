import React, { Component } from 'react';
import {
  Button, ButtonGroup, Grid, GridColumn, GridRow
} from 'semantic-ui-react';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { TRN_PREFIX } from '../../utils/constants';

class NavigationButtons extends Component {
  setEnabledOrDisabledButtons = () => {
    const { tab } = this.props;
    // eslint-disable-next-line default-case
    switch (tab) {
      case 0:
        return [false, true];
      case 1:
        return [true, true];
      case 2:
        return [true, false];
    }
  }

  render() {
    const { translations, backClick, nextClick } = this.props;
    const status = this.setEnabledOrDisabledButtons();
    return (
      <Grid>
        <GridRow>
          <GridColumn width="16">
            <ButtonGroup className="navigation-buttons" floated="right">
              <Button positive={status[0]} disabled={!status[0]} onClick={backClick}>
                {`<< ${translations[`${TRN_PREFIX}back`]}`}
              </Button>
              <div className="separator" />
              <Button positive={status[1]} disabled={!status[1]} onClick={nextClick}>
                {`${translations[`${TRN_PREFIX}next`]} >>`}
              </Button>
            </ButtonGroup>
          </GridColumn>
        </GridRow>
      </Grid>
    );
  }
}

const mapStateToProps = state => ({
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(NavigationButtons);

NavigationButtons.propTypes = {
  backClick: PropTypes.func.isRequired,
  nextClick: PropTypes.func.isRequired,
  translations: PropTypes.object.isRequired,
  tab: PropTypes.number.isRequired,
};
