import React, { useState, useEffect, useRef } from 'react';
import { Col } from 'react-bootstrap';
import PropTypes from 'prop-types';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import FilterOutputItem from './FilterOutputItem';
import { TRN_PREFIX } from '../utils/constants';

const Filter = require('../../../../../ampTemplate/node_modules/amp-filter/dist/amp-filter');

const filter = new Filter({
  draggable: true,
  caller: 'DASHBOARD'
});

const Filters = (props) => {
  const {
    onApplyFilters,
    dashboardId,
    translations,
    globalSettings,
    _sharedDataPending,
    _sharedDataLoaded,
    _sharedData,
  } = props;

  const [state, setState] = useState({
    show: false,
    filtersWithModels: null,
    showFiltersList: false,
    savedDashboardLoaded: false
  });

  useEffect(() => {
    filter.on('apply', applyFilters);
    filter.on('cancel', hideFilters);

    const { savedDashboardLoaded } = state;
    filter.loaded.done(() => {
      if (dashboardId && _sharedDataPending === false && _sharedDataLoaded === true && savedDashboardLoaded === false) {
        // eslint-disable-next-line react/no-did-update-set-state
        setState(prevState => ({ ...prevState, savedDashboardLoaded: true }));
        // Note: no need to explicitly call applyFilters when deserializing (unless you use silent: true).
        filter.deserialize(JSON.parse(_sharedData.stateBlob), { dontSetDefaultDates: true });
      } else {
        /* Notice we dont need to define this.state.filters here, we will get it from onApplyFilters. Apparently
        the filter widget takes date.start and date.end automatically from dashboard settings EP. */
        filter.deserialize({});
      }
    });
    return () => {
      window.removeEventListener('apply', applyFilters);
      window.removeEventListener('cancel', hideFilters);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const hideFilters = () => {
    setState(prevState => {
      return { ...prevState, show: false };
    })
  };

  const applyFilters = () => {
    hideFilters();
    const serializeWithModels = filter.serializeToModels();
    setState(prevState => ({
      ...prevState,
      filtersWithModels: serializeWithModels
    }))
    onApplyFilters(filter.serialize(), serializeWithModels);
  };

  const filterPopupRef = useRef(null);

  const showFilterWidget = () => {
    const { show } = state;
    if (filter && !show) {
      setState(prevState => {
        return { ...prevState, show: true };
      })

      return filter.loaded.then(() => {
        filter.setElement(filterPopupRef.current);
        return filter.showFilters();
      });
    }
  };

  const toggleAppliedFilters = () => {
    const { showFiltersList } = state;

    setState(prevState => {
      return { ...prevState, showFiltersList: !showFiltersList };
    });
  }


  const generateFilterOutput = () => {
    const { filtersWithModels } = state;

    const ret = [];
    if (filtersWithModels && filtersWithModels.filters) {
      Object.keys(filtersWithModels.filters)
        .forEach(i => {
          ret.push(<FilterOutputItem
            key={Math.random()}
            filters={filtersWithModels.filters}
            i={i}
            translations={translations}
            globalSettings={globalSettings} />);
        });
    }
    return <div style={{ paddingLeft: '10px' }}>{ret}</div>;
  }

  return (
    <Col md={5}>
      <div className="panel">
        <div className="panel-body">
          <h3 className="inline-heading" style={{ float: 'left' }}>{translations[`${TRN_PREFIX}filters`]}</h3>
          <button
            type="button"
            className="btn btn-sm btn-default filter-button"
            onClick={showFilterWidget}
            style={{ float: 'right' }}>
            <span className="glyphicon glyphicon-edit" />
            <span>
              {' '}
              {translations[`${TRN_PREFIX}edit-filters`]}
            </span>
          </button>
          <button
            type="button"
            className="btn btn-sm btn-default filter-button"
            onClick={toggleAppliedFilters}
            style={{ float: 'left' }}>
            <span className="glyphicon glyphicon-eye-open" />
            <span>
              {!state.showFiltersList
                ? ` ${translations[`${TRN_PREFIX}show-filter-settings`]} `
                : ` ${translations[`${TRN_PREFIX}hide-filter-settings`]} `}
            </span>
            (
            <b>{state.filtersWithModels ? Object.keys(state.filtersWithModels.filters).length : 0}</b>
            )
          </button>
        </div>
        <div className="applied-filters">
          {state.showFiltersList ? (
            <div>
              {generateFilterOutput()}
            </div>
          ) : null}
        </div>
      </div>

      <div id="filter-popup" ref={filterPopupRef} style={{ display: (!state.show ? 'none' : 'block') }} />
    </Col>
  );
}

const mapStateToProps = state => ({
  _sharedDataPending: state.sharedDataReducer.sharedDataPending,
  _sharedDataLoaded: state.sharedDataReducer.sharedDataLoaded,
  _sharedData: state.sharedDataReducer.sharedData,
  translations: state.translationsReducer.translations
});

const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(Filters);

Filters.propTypes = {
  onApplyFilters: PropTypes.func.isRequired,
  dashboardId: PropTypes.number,
  translations: PropTypes.object.isRequired,
  globalSettings: PropTypes.object.isRequired,
  _sharedDataPending: PropTypes.bool.isRequired,
  _sharedDataLoaded: PropTypes.bool.isRequired,
  _sharedData: PropTypes.object
};

Filters.defaultProps = {
  dashboardId: null,
  _sharedDataPending: false,
  _sharedDataLoaded: false,
  _sharedData: undefined
};
