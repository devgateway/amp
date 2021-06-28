import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Tab } from 'semantic-ui-react';

export default class MultiLingualInputText extends Component {
  render() {
    const {
      languages, values, onChange, isMultiLanguage
    } = this.props;
    if (isMultiLanguage) {
      const panes = [];
      languages.forEach((item, i) => {
        panes.push({
          menuItem: item.id,
          render: () => {
            const value = values[i];
            return (
              <Tab.Pane>
                <input
                  type="text"
                  value={value}
                  key={item.id}
                  onChange={(event) => onChange(item.id, event.target.value)}
                  placeholder={`(${item.id})`} />
              </Tab.Pane>
            );
          }
        });
      });
      return (<Tab panes={panes} />);
    } else {
      return (
        <input
          type="text"
          value={values[0]}
          onChange={(event) => onChange(null, event.target.value)} />
      );
    }
  }
}

MultiLingualInputText.propTypes = {
  values: PropTypes.array.isRequired,
  languages: PropTypes.array.isRequired,
  onChange: PropTypes.func.isRequired,
  isMultiLanguage: PropTypes.bool.isRequired
};

MultiLingualInputText.defaultProps = {
};
