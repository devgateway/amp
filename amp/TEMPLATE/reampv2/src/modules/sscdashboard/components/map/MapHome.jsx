import React, { Component } from 'react';
import { Map, TileLayer, CircleMarker, Marker, Popup } from 'react-leaflet';
import * as L from 'leaflet';
import '../../../../App.css';
import ConnectionLayer from "./d3Layer/ConnectionLayer";
import {
    NON_SELECTED_LINE_COLOR,
    SELECTED_BUBBLE_COLOR,
    NON_SELECTED_BUBBLE_COLOR,
    SELECTED_LINE_COLOR
} from '../../utils/constants';
import '../layout/map/map.css';
import HomePopup from '../layout/popups/homepopup/HomePopup';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { EXTRA_INFO, CENTRO_ID } from '../../utils/FieldsConstants';


class MapHome extends Component {
    //TODO map config should come from configuration
    state = {
        zoom: 3,
        lat: -6.227933930268672,
        lng: 48.33984375,
        selectedCountries: [],
        showSector: true
    };

    handleChangeDataToShow(checked) {
        this.setState({showSector: checked});
    }

    getPoints(dataFiltered, center) {
        let result = [];
        const {selectedCountries} = this.state;
        dataFiltered.forEach(data => {
            const countryCenter = new L.LatLng(data.latitude, data.longitude);
            //TODO make circleMarker returned by a parametrized method
            const bubbleColor = selectedCountries.includes(data.objectData.id)
                ? SELECTED_BUBBLE_COLOR : NON_SELECTED_BUBBLE_COLOR;
            result.push(
                <CircleMarker
                    center={countryCenter}
                    radius={8}
                    stroke={true}
                    color={bubbleColor}
                    opacity={0.7}
                    weight={2}
                    fill={true}
                    fillColor={bubbleColor}
                    fillOpacity={0.9}
                    key={data.objectData.id}
                    onClick={e => this.onBubbleClick(e)}
                    onPopupClose={e => this.popUpClosed(e)}
                    dataPoint={data}
                > <Popup><HomePopup data={data} showSector={this.state.showSector}
                                    handleChangeDataToShow={this.handleChangeDataToShow.bind(this)}/></Popup>
                </CircleMarker>
            )
        });
        const centerCountry = new L.LatLng(center.latitude, center.longitude);
        result.push(
            <CircleMarker
                dashArray={'10 10'}
                center={centerCountry}
                radius={20}
                stroke={true}
                color={'#006600'}
                opacity={0.3}
                weight={2}
                fill={true}
                fillColor={'#006600'}
                fillOpacity={0.3}
                key="centralPoint"
            >
            </CircleMarker>
        );
        return result;
    }

    onLineClick(feature, position) {
        //TODO add the popin on the line click
        console.log(feature);
        console.log(position);
    }

    popUpClosed(e) {
        this.setState({showSector: true});
        this.setState(previousState => {
            const selectedCountries = previousState.selectedCountries.filter(c => c !== e.target.options.dataPoint.objectData.id)
            return {selectedCountries};
        })
    }

    onBubbleClick(e) {
        this.setState(previousState => {
                const selectedCountries = [...previousState.selectedCountries, e.target.options.dataPoint.objectData.id];
                return {selectedCountries};
            }
        );
    }

    //TODO see if we can avoid iterating the points twice (one to generate it and another one to
    // create the circle marker.
    _generateDataPoints(points) {
        const {selectedCountries} = this.state;
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

    render() {
        const mapCenter = [this.state.lat, this.state.lng];
        //TODO country centroID comes from config
        const center = {latitude: 18.567634, longitude: -72.315361};
        const points = [];
        if (this.props.filteredProjects && this.props.filteredProjects.length > 0
            && this.props.countries && this.props.countries.length > 0) {
            this._generateDataPoints(points);
        }
        return (
            <Map className={'map-container'} center={mapCenter} zoom={this.state.zoom} maxZoom={4}
                 minZoom={2}
            >
                <TileLayer
                    attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <ConnectionLayer points={points} nodePoint={center}
                                 onClick={(feature, position) => this.onLineClick(feature, position)}/>
                {this.getPoints(points, center)}
            </Map>
        );
    }

}

const mapStateToProps = state => {
    return {
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
    };
};
const mapDispatchToProps = dispatch => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(MapHome);
