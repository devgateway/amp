import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Tab } from 'semantic-ui-react';

export default class MultiLingualInputText extends Component {
  render() {
    const { languages, values, onChange } = this.props;
    const panes = [];
    languages.forEach((item, i) => {
      panes.push({
        menuItem: item,
        render: () => {
          const value = values[i];
          return (
            <Tab.Pane>
              <input type="text" value={value} key={item} onChange={onChange.bind(null, item)} />
            </Tab.Pane>
          );
        }
      });
    });
    return (<Tab panes={panes} />);
  }
}

MultiLingualInputText.propTypes = {
  values: PropTypes.array.isRequired,
  languages: PropTypes.array.isRequired,
  onChange: PropTypes.func.isRequired,
};

MultiLingualInputText.defaultProps = {
};
