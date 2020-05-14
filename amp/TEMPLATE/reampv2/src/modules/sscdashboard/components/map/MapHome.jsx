import React, { Component } from 'react';
import { Map, TileLayer, CircleMarker, Marker, Tooltip } from 'react-leaflet';
import * as L from 'leaflet';
import '../../../../App.css';
import ConnectionLayer from "./d3Layer/ConnectionLayer";
import '../layout/map/map.css';

export default class MapHome extends Component {
    //TODO map config should come from configuration
    state = {
        zoom: 3,
        lat: -6.227933930268672,
        lng: 48.33984375
    };
    getPoints(dataFiltered, center) {
        let result = [];
        dataFiltered.forEach(data => {
            const countryCenter = new L.LatLng(data.latitude, data.longitude);
            //TODO make circleMarker returned by a parametrized method
            result.push(
                <CircleMarker
                    center={countryCenter}
                    radius={8}
                    stroke={true}
                    color={'#FAB47D'}
                    opacity={0.7}
                    weight={2}
                    fill={true}
                    fillColor={'#FAB47D'}
                    fillOpacity={0.9}
                    key={data.objectId}
                >{/*TODO check if this tooltip is correct and if the desing needs to be adjusted*/}
                    <Tooltip direction="bottom" offset={[0, 20]} opacity={1}>
                        {data.objectName}
                    </Tooltip>
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

    _generateDataPoints(points) {
        this.props.filteredProjects.forEach(fp => {
            const countryFound = this.props.countries.find(element => element.id === fp.countryId);
            if (countryFound) {
                //TODO move fields to constants
                if (countryFound['extra_info'] && countryFound['extra_info']['centro-id']) {
                    const latitude = countryFound['extra_info']['centro-id'].lat;
                    const longitude = countryFound['extra_info']['centro-id'].lon;
                    const objectId = fp.countryId;
                    const objectName = countryFound.name;
                    points.push({objectId, latitude, longitude, objectName});
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
                <ConnectionLayer points={points} nodePoint={center}/>
                {this.getPoints(points, center)}
            </Map>
        );
    }

}
