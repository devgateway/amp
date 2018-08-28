import React, { Component } from 'react';
import Home from "./Home";

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