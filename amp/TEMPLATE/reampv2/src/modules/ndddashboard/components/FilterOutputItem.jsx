import React, { Component } from 'react';
import PropTypes from 'prop-types';

export default class FilterOutputItem extends Component {
  generateChildren = (items, i) => {
    const ret = [];
    if (items.length > 0) {
      items.forEach(item => {
        ret.push(<li>{item.get('name')}</li>);
      });
    }
    return <ul>{ret}</ul>;
  }

  render() {
    const { filters, i } = this.props;
    const ret = [];
    if (filters && filters[i]) {
      if (filters[i].filterName) {
        const parent = <span>{filters[i].filterName}</span>;
        ret.push(parent);
      } else {
        const parent = <span>{i}</span>;
        ret.push(parent);
      }
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
