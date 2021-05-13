import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Checkbox, Form, FormField
} from 'semantic-ui-react';

export default class OptionsContent extends Component {
  render() {
    const {
      radioList, checkList, changeRadioList, changeCheckList, selectedRadio, selectedCheckboxes, loading
    } = this.props;
    return (
      <>
        <Form loading={loading}>
          <Form.Group className="fields-vertical">
            {radioList.map((item) => (
              <Form.Radio
                label={item.label}
                value={item.name}
                key={item.name}
                checked={item.name === selectedRadio}
                onChange={changeRadioList}
              />
            ))}
          </Form.Group>
          {checkList.map((item, i) => (
            <FormField key={`form_field_${item}`}>
              <Checkbox
                toggle
                key={item.name}
                onChange={changeCheckList ? changeCheckList[i] : {}}
                checked={selectedCheckboxes && selectedCheckboxes.item}
                label={item.label} />
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
  selectedCheckboxes: PropTypes.array,
  changeRadioList: PropTypes.func.isRequired,
  changeCheckList: PropTypes.array.isRequired,
  loading: PropTypes.bool,
};

OptionsContent.defaultProps = {
  radioList: [],
  checkList: [],
  selectedRadio: undefined,
  selectedCheckboxes: [],
  loading: false,
};
