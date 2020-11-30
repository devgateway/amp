import React, { Component } from 'react';
import PropTypes from 'prop-types';

export default class FilterOutputItem extends Component {
  generateChildren = (items, i) => {
    const ret = [];
    if (items.length > 0) {
      items.forEach(item => {
        ret.push(<li key={Math.random()}>{item.get('name')}</li>);
      });
    } else if (items.modelType === 'DATE-RANGE-VALUES') {
      ret.push(
        <li key={Math.random()}>
          {items.start ? items.start : 'No Data'}
          {' --- '}
          {items.end ? items.end : 'No Data'}
        </li>
      );
    } else if (items.modelType === 'YEAR-SINGLE-VALUE') {
      ret.push(<li key={Math.random()}>{items.year}</li>);
    }
    return <ul>{ret}</ul>;
  }

  render() {
    const { filters, i } = this.props;
    const ret = [];
    if (filters && filters[i]) {
      let parent = null;
      if (filters[i].filterName) {
        parent = <h5>{filters[i].filterName}</h5>;
      } else if (filters[i].displayName) {
        parent = <h5>{filters[i].displayName}</h5>;
      } else {
        parent = <h5>{i.replaceAll('-', ' ')}</h5>;
      }
      ret.push(parent);
      const children = this.generateChildren(filters[i], i);
      ret.push(children);
    }
    return <div>{ret}</div>;
  }
}

FilterOutputItem.propTypes = {
  filters: PropTypes.object.isRequired,
  i: PropTypes.string.isRequired
};
