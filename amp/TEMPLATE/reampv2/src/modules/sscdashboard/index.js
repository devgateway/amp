import React, {Component} from 'react';
import {createStore, applyMiddleware, compose} from 'redux';
import thunk from 'redux-thunk';
import rootReducer from './reducers/rootReducer';
import {Provider} from 'react-redux'
import {Link} from "react-router-dom";
import DashboardRouter from "./components/SSCDashboard.router";

const composeEnhancer = window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;

class SSCDashboardApp extends Component {

    constructor(props) {
        super(props)
        this.store = createStore(rootReducer, composeEnhancer(applyMiddleware(thunk)));
        this.state = {
            currentTab: ''
        };
    }
    render() {
        const listGroupItem = 'list-group-item';
        const active = 'active';
        const modules = [];
        // modules need to be loaded dynamically
        modules.push({name: 'Map', link: '/sscdashboard/map'});
        modules.push({name: 'Sectors', link: '/sscdashboard/sectors'});
        modules.push({name: 'MapWithFigures', link: '/sscdashboard/figures'});
        const navItem = 'nav-item';
        const classActive = 'active';
        const styleMargin = {'marginLeft': '0px'};
        const classHeight = {height: '700px'};
        return (<Provider store={this.store}>
            <div style={styleMargin}>
                <div style={styleMargin}>
                    <div>INNER HEADER</div>
                </div>
                <div className="row" style={styleMargin}>
                    <div className="col-xs-12 col-sm-2 col-lg-2">
                        <ul className="nav-item">
                            {modules.map(module => ( // with a name, and routes
                                <li key={module.name}
                                    className={[navItem, this.state.currentTab === module.name ? classActive : ''].join('')}>
                                    <Link to={module.link}
                                          onClick={() => this.setState({currentState: module.name})}>{module.name}</Link>
                                </li>
                            ))}
                        </ul>
                    </div>
                    <div className="col-sm-10 col-lg-10" style={classHeight}><DashboardRouter/></div>
                </div>
            </div>
        </Provider>);
    }
}
export default SSCDashboardApp;

