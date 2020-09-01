import React, { Component } from 'react';
import { Map, TileLayer, Marker, Popup, withLeaflet } from 'react-leaflet';
import '../../../../App.css';
import D3AddShapeToLeafLet from './d3Layer/D3AddShapeToLeafLet' ;

export default class MapWithD3Figures extends Component {
    state = {
        lat: 0,
        lng: 0,
        zoom: 2,
    };

    render() {
        const position = [this.state.lat, this.state.lng];
        const WrappedD3AddShapeToLeafLet = withLeaflet(D3AddShapeToLeafLet);
        return (
            <Map classname='map-container' center={position} zoom={this.state.zoom}>
                <TileLayer
                    attribution='&amp;copy <a href="http://osm.org/copyright">OpenStreetMap</a> contributors'
                    url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                />
                <Marker position={position}>
                    <Popup>
                        A pretty CSS3 popup. <br/> Easily customizable.
                    </Popup>
                </Marker>
                {<WrappedD3AddShapeToLeafLet/>}
            </Map>);
    }
}
