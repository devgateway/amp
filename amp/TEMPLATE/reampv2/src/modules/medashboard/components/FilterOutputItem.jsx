import React from 'react';
import PropTypes from 'prop-types';
import dateformat from 'dateformat';
import { TRN_PREFIX } from '../utils/constants';

const FilterOutputItem = (props) => {
  const {
    filters,
    i,
    translations,
    globalSettings
  } = props;

  const generateChildren = (items) => {
    const ret = [];
    if (items.length > 0) {
      items.forEach(item => {
        ret.push(<li key={Math.random()}>{item.get('name')}</li>);
      });
    } else if (items.modelType === 'DATE-RANGE-VALUES') {
      ret.push(
        <li key={Math.random()}>
          {items.start ? formatDate(items.start) : translations[`${TRN_PREFIX}no-data-short`]}
          {' --- '}
          {items.end ? formatDate(items.end) : translations[`${TRN_PREFIX}no-data-short`]}
        </li>
      );
    } else if (items.modelType === 'YEAR-SINGLE-VALUE') {
      ret.push(<li key={Math.random()}>{items.year}</li>);
    }
    return <ul key={Math.random()}>{ret}</ul>;
  };

  const formatDate = (dateString) => {
    const date = new Date(`${dateString}T00:00`);
    const format = globalSettings.dateFormat.replace(/M{2}/gm, 'mm');
    return dateformat(date, format);
  };

  const ret = [];
  if (filters && filters[i]) {
    let parent = null;
    if (filters[i].filterName) {
      parent = <h5 key={Math.random()}>{filters[i].filterName}</h5>;
    } else if (filters[i].displayName) {
      parent = <h5 key={Math.random()}>{filters[i].displayName}</h5>;
    } else {
      parent = <h5 key={Math.random()}>{i.replace(/[-]+/g, ' ')}</h5>;
    }
    ret.push(parent);
    const children = generateChildren(filters[i], i);
    ret.push(children);
  }

  return <div>{ret}</div>;
};

FilterOutputItem.propTypes = {
  filters: PropTypes.object.isRequired,
  i: PropTypes.string.isRequired,
  translations: PropTypes.object.isRequired,
  globalSettings: PropTypes.object.isRequired
};

export default FilterOutputItem;
