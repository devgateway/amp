import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Tab, Menu } from 'semantic-ui-react';

export default class MultiLingualInputText extends Component {
  render() {
    const {
      languages, values, onChange, isMultiLanguage, mandatory
    } = this.props;
    if (isMultiLanguage) {
      const panes = [];
      let _languages = [];
      if (mandatory && mandatory.length === 1) {
        _languages.push(languages.find(i => i.id === mandatory[0]));
        languages.forEach(i => {
          if (!_languages.find(j => j.id === i.id)) {
            _languages.push(i);
          }
        });
      } else {
        _languages = languages;
      }
      _languages.forEach((item, i) => {
        panes.push({
          menuItem: (
            mandatory.find(j => j === item.id) ? (
              <Menu.Item key="messages">
                <div className="red_text" style={{ float: 'left', paddingRight: '5px' }}>* </div>
                {item.id}
              </Menu.Item>
            ) : item.id
          ),
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
  isMultiLanguage: PropTypes.bool.isRequired,
  mandatory: PropTypes.array,
};

MultiLingualInputText.defaultProps = {
  mandatory: [],
};
