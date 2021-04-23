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
            {radioList.map((item, i) => (
              <Form.Radio
                label={item}
                value={item}
                checked={i === selectedRadio}
                onChange={changeRadioList}
              />
            ))}
          </Form.Group>
          {checkList.map(item => (
            <FormField>
              <Checkbox toggle onChange={changeCheckList} />
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
  selectedRadio: PropTypes.number,
  selectedCheckboxes: PropTypes.array,
  changeRadioList: PropTypes.func.isRequired,
  changeCheckList: PropTypes.func.isRequired,
};

OptionsContent.defaultProps = {
  radioList: [],
  checkList: [],
  selectedRadio: -1,
  selectedCheckboxes: [],
};
