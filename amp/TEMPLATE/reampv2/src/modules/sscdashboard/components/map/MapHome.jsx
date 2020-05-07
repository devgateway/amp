import React, {Component} from 'react';
import {Map, TileLayer, withLeaflet, CircleMarker, Marker} from 'react-leaflet';
import * as L from 'leaflet';
import '../../../../App.css';
import ConnectionLayer from "./d3Layer/ConnectionLayer";
import '../layout/map/map.css';

export default class MapHome extends Component {
    state = {
        lat: -38.41,
        lng: -63.61,
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
                color={'#006600'}
                opacity={0.3}
                weight={2}
                fill={true}
                fillColor={'#006600'}
                fillOpacity={0.3}
            >
            </CircleMarker>
        );
        return result;
    }

    render() {
        const mapCenter = [this.state.lat, this.state.lng];
        const WrappedConnectionLayer = withLeaflet(ConnectionLayer);
        const center = {latitude: 18.567634, longitude: -72.315361};
        const points = [];
        //this will come from the api
        points.push({latitude: -14.235004, longitude: -51.92528});
        points.push({latitude: 35.86166, longitude: 104.195397});
        points.push({latitude: 4.570868, longitude: -74.297333});
        points.push({latitude: 14.058324, longitude: 108.277199});
        points.push({latitude: 4.535277, longitude: 114.727669});
        points.push({latitude: -9.189967, longitude: -75.015152});
        points.push({latitude: -38.416097, longitude: -63.616672});
        points.push({latitude: -35.675147, longitude: -71.542969});
        points.push({latitude: -4.038333, longitude: 21.758664});
        points.push({latitude: 20.593684, longitude: 78.96288});
        return (
            <Map className={'map-container'} center={mapCenter} zoom={this.state.zoom} >
                <Marker position={mapCenter}/>
                <TileLayer
                    attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <WrappedConnectionLayer points={points} nodePoint={center}/>
                {this.getPoints(points, center)}

            </Map>
        );
    }

}
