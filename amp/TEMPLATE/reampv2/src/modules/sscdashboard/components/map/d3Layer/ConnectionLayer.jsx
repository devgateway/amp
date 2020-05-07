import React from "react";
import L from 'leaflet';
// eslint-disable-next-line no-unused-vars
import { curve } from 'leaflet-curve';

export default class ConnectionLayer extends React.Component {
    constructor(props) {
        super(props);
        this.lines = [];
    }

    componentDidMount() {
        this.map = this.props.leaflet.map;
        const {points} = this.props;
        if (points) {
            this.update()
        }
    }

    compareProps(prevProps, newProps) {
        if (prevProps.points.length !== newProps.points.length) {
            return true;
        }
        for (let i = 0; i < prevProps.points.length; i++) {
            if (prevProps.points[i].latitude !== newProps.points[i].latitude
                || prevProps.points[i].longitude !== newProps.points[i].longitude) {
                return true;
            }
        }
        return false;
    }

    componentDidUpdate(prevProps) {
        if (this.compareProps(prevProps, this.props)) {
            this.update();
        }
    }

    cleanLines() {
        if (this.lines.length > 0) {
            this.lines.forEach(l => {
                l.removeFrom(this.map)
            });
            this.lines = [];
        }
    }

    drawLine(point1, point2) {
        const {lineColor, lineWeight, breaks, maxWeigth = 20, onClick} = this.props;

        const latlng1 = [point1.latitude, point1.longitude],
            latlng2 = [point2.latitude, point2.longitude];

        const offsetX = latlng2[1] - latlng1[1],
            offsetY = latlng2[0] - latlng1[0];

        const r = Math.sqrt(Math.pow(offsetX, 2) + Math.pow(offsetY, 2)),
            theta = Math.atan2(offsetY, offsetX);

        const thetaOffset = (3.14 / 10);

        const r2 = (r / 2) / (Math.cos(thetaOffset)),
            theta2 = theta + thetaOffset;

        const midpointX = (r2 * Math.cos(theta2)) + latlng1[1],
            midpointY = (r2 * Math.sin(theta2)) + latlng1[0];

        const midpointLatLng = [midpointY, midpointX];

        let weight = 5;
        if (breaks && breaks.length) {
            const breakUnit = maxWeigth / breaks.length;
            for (let i = 0; i < breaks.length - 1; i++) {
                if (point2.value > breaks[i] && point2.value < breaks[i + 1]) {
                    weight = breakUnit * (i + 1);
                }
            }
            ;
        }

        const pathOptions = {
            color: lineColor || 'rgba(255,140,0,0.5)',
            weight: lineWeight || weight,
            pointData: point2,
            dashOffset: '51',
            dashArray: '10 10',
            stroke: 'white'
        }

        const line = new L.curve(
            [
                'M', latlng1,
                'Q', midpointLatLng,
                latlng2
            ], pathOptions);
        line.addTo(this.map);
        if (onClick) {
            line.on('click', f => onClick(f.target.options.pointData, f.latlng));
        }
        this.lines.push(line);
    }

    update() {
        const {points, nodePoint} = this.props;
        this.cleanLines();
        points.forEach(d => {
            this.drawLine(nodePoint, d);
        })
    }

    render() {
        return (<div/>)
    }
}
