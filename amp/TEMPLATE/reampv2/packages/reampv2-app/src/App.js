import React, { Component } from 'react';
import './App.css';
import AppRoute from './App.route';
import NavigationManager from "./NavigationManager";

class App extends Component {
  componentDidMount() {
    // Detect changes in params after the '#'.
    window.addEventListener('hashchange', () => {
      window.location.reload();
    }, false);
  }

  render() {
    return (
        <NavigationManager>
          <AppRoute />
        </NavigationManager>
    );
  }
}

export default App;
