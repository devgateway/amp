import React, {Component} from 'react';
import {Route} from 'react-router-dom';
import MapHome from './map/MapHome';
import './../../../App.css';
import SectorsHome from './sectors/SectorsHome';
import MapWithD3Figures from './map/MapWithD3Figures';
class DashboardRouter extends Component {
    render() {
        return (
            <div className='dashboard-container'>
                <Route path="/sscdashboard/map" component={MapHome}/>
                <Route path="/sscdashboard/sectors" component={SectorsHome}/>
                <Route path="/sscdashboard/figures" component={MapWithD3Figures}/>
            </div>
        );
    }
}
export default DashboardRouter;
