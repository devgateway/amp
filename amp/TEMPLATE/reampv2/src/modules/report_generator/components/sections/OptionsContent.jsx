import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Checkbox, Form, FormField, Label
} from 'semantic-ui-react';

export default class OptionsContent extends Component {
  render() {
    const fakeReportTypes = ['Summary Report', 'Annual Report', 'Quarterly Report', 'Monthly Report'];
    const fakeExtraOptions = ['Totals Only'];
    return (
      <>
        <Form>
          <Form.Group className="fields-vertical">
            {fakeReportTypes.map((item, i) => (
              <Form.Radio
                label={item}
                value={item}
                checked={i === 1}
              />
            ))}
          </Form.Group>
          {fakeExtraOptions.map(item => (
            <FormField>
              <Checkbox toggle />
              <span>{item}</span>
            </FormField>
          ))}
        </Form>
      </>
    );
  }
}

OptionsContent.propTypes = {
};

OptionsContent.defaultProps = {
};
