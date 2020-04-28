import React, {Component} from 'react';
import {Map, TileLayer, withLeaflet, CircleMarker} from 'react-leaflet';
import * as L from 'leaflet';
import '../../../../App.css';
import ConnectionLayer from "./d3Layer/ConnectionLayer";

export default class MapHome extends Component {
    state = {
        lat: 0,
        lng: 0,
        zoom: 2.5,
    };

    getPoints(dataFiltered, center) {
        let result = [];
        dataFiltered.forEach(data => {
            const countryCenter = new L.LatLng(data.latitude, data.longitude);
            result.push(
                <CircleMarker
                    center={countryCenter}
                    radius={6}
                    stroke={true}
                    color={'#FAB47D'}
                    opacity={0.7}
                    weight={2}
                    fill={true}
                    fillColor={'#FAB47D'}
                    fillOpacity={0.9}
                >
                </CircleMarker>
            )
        });
        //This will come from configuration
        const centerCountry = new L.LatLng(center.latitude, center.longitude);
        result.push(
            <CircleMarker
                dashArray={'10 10'}
                center={centerCountry}
                radius={12}
                stroke={true}
                color={'#789D67'}
                opacity={0.7}
                weight={2}
                fill={true}
                fillColor={'transparent'}
                fillOpacity={0.9}
                dashArray
            >
            </CircleMarker>
        )
        return result;
    }

    render() {
        const mapCenter = [this.state.lat, this.state.lng];
        const WrappedConnectionLayer = withLeaflet(ConnectionLayer);
        const center = {latitude: 18.567634, longitude: -72.315361};
        const points = [];
        points.push({latitude: -14.235004, longitude: -51.92528});
        points.push({latitude: 35.86166, longitude: 104.195397});
        points.push({latitude: 4.570868, longitude: -74.297333});
        return (
            <Map classname='map-container' center={mapCenter} zoom={this.state.zoom}>
                <TileLayer
                    attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <WrappedConnectionLayer points={points} nodePoint={center}/>
                {this.getPoints(points, center)}
            </Map>);
    }

}
