import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Checkbox, Form, FormField
} from 'semantic-ui-react';

export default class OptionsContent extends Component {
  render() {
    const {
      radioList, checkList, changeRadioList, changeCheckList, selectedRadio, selectedCheckboxes
    } = this.props;
    return (
      <>
        <Form>
          <Form.Group className="fields-vertical">
            {radioList.map((item) => (
              <Form.Radio
                label={item}
                value={item}
                checked={item === selectedRadio}
                onChange={changeRadioList}
              />
            ))}
          </Form.Group>
          {checkList.map((item) => (
            <FormField>
              <Checkbox
                toggle
                onChange={changeCheckList}
                checked={selectedCheckboxes && selectedCheckboxes.item} />
              <span>{item}</span>
            </FormField>
          ))}
        </Form>
      </>
    );
  }
}

OptionsContent.propTypes = {
  radioList: PropTypes.array,
  checkList: PropTypes.array,
  selectedRadio: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  selectedCheckboxes: PropTypes.object,
  changeRadioList: PropTypes.func.isRequired,
  changeCheckList: PropTypes.func.isRequired,
};

OptionsContent.defaultProps = {
  radioList: [],
  checkList: [],
  selectedRadio: undefined,
  selectedCheckboxes: undefined,
};
