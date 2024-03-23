import React, { Component } from 'react';

// eslint-disable-next-line no-unused-vars
import styles from '../css/style.css';

export default class RequiredMark extends Component {
  render() {
    return <span className="required-fields"> *</span>;
  }
}
