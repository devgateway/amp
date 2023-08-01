import React, {Component} from 'react';
import './App.css';
import AppRoute from './App.route';

class App extends Component {
  componentDidMount() {
    // Detect changes in params after the '#'.
    window.addEventListener('hashchange', () => {
      window.location.reload();
    }, false);
  }

  render() {
    return (
      <AppRoute />
    );
  }
}

export default App;
