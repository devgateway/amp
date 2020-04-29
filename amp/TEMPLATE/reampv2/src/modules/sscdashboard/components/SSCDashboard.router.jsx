import React, {Component} from 'react';
import {Route} from 'react-router-dom';
import MapHome from './map/MapHome';
import './../../../App.css';
import SectorsHome from './sectors/SectorsHome';
import MapContainer from "./layout/map/map-content";
import MapWithD3Figures from './map/MapWithD3Figures';
class SSCDashboardRouter extends Component {
    render() {
        return (
            <div>
                <Route path="/sscdashboard/map" component={MapContainer}/>
                <Route path="/sscdashboard/sectors" component={SectorsHome}/>
                <Route path="/sscdashboard/figures" component={MapWithD3Figures}/>
            </div>
        );
    }
}
export default SSCDashboardRouter;
