import {MapLayer} from "react-leaflet";
import * as L from 'leaflet';
import * as d3 from "d3";

export default class D3Shape extends MapLayer {

    createLeafletElement(props) {
        return L.geoJson();
    }


    onZoom = (feature,map) => e => {
        debugger;
        this.update(feature, map);
    };

    update(feature, map){
        feature.attr("transform",
            function (d) {
                return "translate(" +
                    map.latLngToLayerPoint(d.LatLng).x + "," +
                    map.latLngToLayerPoint(d.LatLng).y + ")";
            }
        )
    }
    componentDidMount() {
        super.componentDidMount();
        const {map} = this.props.leaflet;

        this.leafletElement = L.geoJSON();
        this.svg = d3.select(map.getPanes().overlayPane).append('svg');
        this.svg.attr("style", "width:" + map._size.x + "; height:" + map._size.x);
        this.g = this.svg.append('g');//.attr('class', 'leaflet-zoom-hide');

        //this needs to be configurable if needed
        const feature = this.g.selectAll("circle")
            .data(this.getJsonPoints().objects)
            .enter().append("circle")
            .style("stroke", "black")
            .style("opacity", .6)
            .style("fill", "red")
            .attr("r", 20);

        map.on("viewreset", this.onZoom(feature,map));

        this.update(feature, map);
    }
    getJsonPoints() {
        const circles =
            {
                "objects": [
                    {"circle": {"coordinates": [-101.404398, 40.649782]}},
                    {"circle": {"coordinates": [25.767555, -80.197188]}},
                ]
            };
        circles.objects.forEach(d => {
            d.LatLng = new L.LatLng(d.circle.coordinates[0],
                d.circle.coordinates[1])
        });
        return circles;
    }
}
