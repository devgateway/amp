import React, { Component } from 'react';
import {
  Map, TileLayer, CircleMarker, Popup
} from 'react-leaflet';
import * as L from 'leaflet';
import Control from 'react-leaflet-control';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import CenterIcon from '../../images/icons/centermap.png';
import '../../../../App.css';
import ConnectionLayer from './d3Layer/ConnectionLayer';
import {
  NON_SELECTED_LINE_COLOR,
  SELECTED_BUBBLE_COLOR,
  NON_SELECTED_BUBBLE_COLOR,
  SELECTED_LINE_COLOR
} from '../../utils/constants';
import '../layout/map/map.css';
import HomePopup from '../layout/popups/homepopup/HomePopup';
import { EXTRA_INFO, CENTRO_ID } from '../../utils/FieldsConstants';
import { SSCTranslationContext } from '../StartUp';

class MapHome extends Component {
  // TODO map config should come from configuration

  constructor(props) {
    super(props);
    this.state = {
      zoom: 3,
      lat: -6.227933930268672,
      lng: 48.33984375,
      selectedCountries: [],
      showSector: true,
      mapCentered: false

    };
    this.theMap = null;
  }

  handleChangeDataToShow(checked) {
    this.setState({ showSector: checked });
  }

  componentDidUpdate(prevProps: Readonly<P>, prevState: Readonly<S>): void {
    if (prevProps.filteredProjects !== this.props.filteredProjects) {
      this.setState(previousState => {
        const availableCountries = this.props.filteredProjects.map(p => p.id);
        const selectedCountries = previousState.selectedCountries.filter(c => availableCountries.includes(c));
        return { selectedCountries };
      });
    }
  }

  getPoints(dataFiltered, center) {
    const result = [];
    const { selectedCountries } = this.state;
    dataFiltered.forEach(data => {
      const countryCenter = new L.LatLng(data.latitude, data.longitude);
      // TODO make circleMarker returned by a parametrized method
      const bubbleColor = selectedCountries.includes(data.objectData.id)
        ? SELECTED_BUBBLE_COLOR : NON_SELECTED_BUBBLE_COLOR;
      result.push(
        <CircleMarker
          center={countryCenter}
          radius={8}
          stroke
          color={bubbleColor}
          opacity={0.7}
          weight={2}
          fill
          fillColor={bubbleColor}
          fillOpacity={0.9}
          key={data.objectData.id}
          onClick={e => this.onBubbleClick(e)}
          onPopupClose={e => this.popUpClosed(e)}
          dataPoint={data}
                >
          {' '}
          <Popup>
            <HomePopup
              data={data}
              showSector={this.state.showSector}
              handleChangeDataToShow={this.handleChangeDataToShow.bind(this)} />
          </Popup>
        </CircleMarker>
      );
    });
    const centerCountry = new L.LatLng(center.latitude, center.longitude);
    result.push(
      <CircleMarker
        dashArray="10 10"
        center={centerCountry}
        radius={20}
        stroke
        color="#006600"
        opacity={0.3}
        weight={2}
        fill
        fillColor="#006600"
        fillOpacity={0.3}
        key="centralPoint"
             />
    );
    return result;
  }

  onLineClick(feature, position) {
    // TODO add the popin on the line click
    console.log(feature);
    console.log(position);
  }

  popUpClosed(e) {
    this.setState({ showSector: true });
    this.setState(previousState => {
      const selectedCountries = previousState.selectedCountries.filter(c => c !== e.target.options.dataPoint.objectData.id);
      return { selectedCountries };
    });
  }

  onBubbleClick(e) {
    this.setState(previousState => {
      const selectedCountries = [...previousState.selectedCountries, e.target.options.dataPoint.objectData.id];
      return { selectedCountries };
    });
  }

  // TODO see if we can avoid iterating the points twice (one to generate it and another one to
  // create the circle marker.
  _generateDataPoints(points) {
    const { selectedCountries } = this.state;
    // at this point selected countries can contain countries that are no longer in the result list
    // we need to filter the out so if they appear again in the result they are drawn orange instead of yellow
    this.props.filteredProjects.forEach(fp => {
      const countryFound = this.props.countries.find(element => element.id === fp.id);
      if (countryFound) {
        if (countryFound[EXTRA_INFO] && countryFound[EXTRA_INFO][CENTRO_ID]) {
          const centroId = countryFound[EXTRA_INFO][CENTRO_ID];
          const dataPoint = {};
          dataPoint.color = selectedCountries.includes(fp.id) ? SELECTED_LINE_COLOR : NON_SELECTED_LINE_COLOR;
          dataPoint.latitude = centroId.lat;
          dataPoint.longitude = centroId.lon;
          dataPoint.objectData = fp;
          dataPoint.objectName = countryFound.name;
          points.push(dataPoint);
        }
      }
    });
  }

  handleCenterClick() {
    this.setState({ mapCentered: false }, () => this.centerMap(this.theMap));
  }

  centerMap(theMap) {
    if (theMap && !this.state.mapCentered && this.props.filteredProjects && this.props.filteredProjects.length > 0) {
      const points = [];
      this._generateDataPoints(points);
      const arrayOfLatLngs = [];
      points.forEach(data => {
        const countryCenter = new L.LatLng(data.latitude, data.longitude);
        arrayOfLatLngs.push(countryCenter);
      });
      const bounds = new L.LatLngBounds(arrayOfLatLngs);
      if (bounds.isValid()) {
        theMap.leafletElement.fitBounds(bounds);
        this.setState({ mapCentered: true });
      }
    }
  }

  render() {
    const mapCenter = [this.state.lat, this.state.lng];
    const { translations } = this.context;
    // TODO country centroID comes from config
    const center = { latitude: 18.567634, longitude: -72.315361 };
    const points = [];
    if (this.props.filteredProjects && this.props.filteredProjects.length > 0
            && this.props.countries && this.props.countries.length > 0) {
      this._generateDataPoints(points);
    }
    return (
      <Map
        className="map-container"
        center={mapCenter}
        zoom={this.state.zoom}
        maxZoom={4}
        minZoom={1}
        ref={ref => {
          this.theMap = ref;
          this.centerMap(ref);
        }}
            >
        <TileLayer
          attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
        {' '}
        <Control position="topleft" className="leaflet-control-zoom leaflet-bar">
          <a onClick={this.handleCenterClick.bind(this)}>
            <img
              src={CenterIcon}
              className="map-control"
              alt={translations['amp.ssc.dashboard:center-map']}
              title={translations['amp.ssc.dashboard:center-map']} />
          </a>
        </Control>
        <ConnectionLayer
          points={points}
          nodePoint={center}
          onClick={(feature, position) => this.onLineClick(feature, position)} />
        {this.getPoints(points, center)}
      </Map>
    );
  }
}

const mapStateToProps = state => ({
  filters: {
    sectors: {
      sectors: state.filtersReducer.sectors,
      sectorsLoaded: state.filtersReducer.sectorsLoaded
    },
    countries: {
      countries: state.filtersReducer.countries,
      countriesLoaded: state.filtersReducer.countriesLoaded
    }

  }
});
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);
MapHome.contextType = SSCTranslationContext;

export default connect(mapStateToProps, mapDispatchToProps)(MapHome);
