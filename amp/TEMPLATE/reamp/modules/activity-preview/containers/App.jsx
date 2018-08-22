import React, { Component, PropTypes } from 'react';
import Home from "./Home.jsx";

/**
 * @author Daniel Oliva
 */
export default class App extends Component {

    constructor(props, context) {
        super(props, context);
    }
  
    render() {       
        return (
              <Home />
        );
    }
}