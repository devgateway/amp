import React, { Component } from 'react';
import PropTypes from 'prop-types';
import './ToogleSwitch.css';

/*
Toggle Switch Component
Note: id and text are is required for ToggleSwitch component to function. Name, currentValue, defaultChecked, big, s
mall and onChange are optional.
If uses Small then no text is used and only shows the
Usage: <ToggleSwitch id="id" onChange={function (e) { console.log("Checkbox Current State: " + e.target.checked); }} />
*/
class ToggleSwitch extends Component {
  constructor(props) {
    super(props);
    const { defaultChecked } = props;
    this.state = {
      checked: defaultChecked
    };
  }

  onChange(e) {
    const { onChange } = this.props;
    this.setState({
      checked: e.target.checked
    });
    if (typeof onChange === 'function') onChange(e.target.checked);
  }

  render() {
    const {
      id, name, big, currentValue, defaultChecked, disabled, text, small
    } = this.props;
    return (
      <div className={`toggle-switch${big ? ' big-switch' : small ? 'small-switch' : ''}`}>
        <input
          type="checkbox"
          name={name}
          className="toggle-switch-checkbox"
          id={id}
          checked={currentValue}
          defaultChecked={defaultChecked}
          onChange={this.onChange.bind(this)}
          disabled={disabled}
                />
        {id ? (
          <label className="toggle-switch-label" htmlFor={id}>
            <span
              className={
                    disabled
                      ? 'toggle-switch-inner toggle-switch-disabled'
                      : 'toggle-switch-inner'
                }
              data-yes={text[0]}
              data-no={text[1]}
            />
            <span
              className={
                                disabled
                                  ? 'toggle-switch-switch toggle-switch-disabled'
                                  : 'toggle-switch-switch'
                            }
                        />
          </label>
        ) : null}
      </div>
    );
  }
}

ToggleSwitch.propTypes = {
  id: PropTypes.string.isRequired,
  text: PropTypes.array.isRequired,
  name: PropTypes.string,
  onChange: PropTypes.func,
  defaultChecked: PropTypes.bool,
  big: PropTypes.bool,
  small: PropTypes.bool,
  currentValue: PropTypes.bool,
  disabled: PropTypes.bool
};

export default ToggleSwitch;
