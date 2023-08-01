import React, {Component} from 'react';
import PrintDummy from './PrintDummy';
import './print.css';

export default class PrinterFriendly extends Component {
  componentDidMount() {
    window.parent.postMessage({ action: 'chart-loaded' });
  }

  render() {
    return (<PrintDummy friendly />);
  }
}
