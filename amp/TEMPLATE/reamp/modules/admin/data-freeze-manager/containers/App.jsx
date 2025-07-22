import React, { Component, PropTypes } from 'react';
import Home from "./Home";

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