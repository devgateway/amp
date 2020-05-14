import React, {Component} from 'react';
import {Route} from 'react-router-dom';
import './../../../App.css';
import SectorsHome from './sectors/SectorsHome';
import MapContainer from "./layout/map/map-content";
import MapWithD3Figures from './map/MapWithD3Figures';


class SSCDashboardRouter extends Component {
    render() {
        return (
            <div>
                <Route path="/ssc/sectors" component={SectorsHome}/>
                <Route path="/ssc/home" component={MapContainer}/>
                <Route path="/ssc/figures" component={MapWithD3Figures}/>
            </div>
        );
    }
}

export default SSCDashboardRouter;
