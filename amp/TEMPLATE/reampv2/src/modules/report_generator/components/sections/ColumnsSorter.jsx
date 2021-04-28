import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Accordion, Checkbox, Form, Icon, Image, Label
} from 'semantic-ui-react';

export default class ColumnSorter extends Component {
  handleHeaderClick = (e, titleProps) => {
    const { index } = titleProps;
    const { activeIndex } = this.state;
    const newIndex = activeIndex === index ? -1 : index;
    this.setState({ activeIndex: newIndex });
  }

  onItemClick = (obj) => {
    const { onColumnSelectionChange } = this.props;
    const id = Number.parseInt(obj.target.value, 10);
    onColumnSelectionChange(id);
  }

  render() {
    const { columns, selected } = this.props;
    if (selected.length > 0) {
      return (
        <Form>
          <>
            {selected.map(col => (
              <div className="sort-column-item">
                <span>{columns.find(i => i.id === col).label}</span>
              </div>
            ))}
          </>
        </Form>
      );
    } else {
      return null;
    }
  }
}

ColumnSorter.propTypes = {
  columns: PropTypes.array,
  selected: PropTypes.array,
  onColumnSelectionChange: PropTypes.func.isRequired,
};

ColumnSorter.defaultProps = {
  columns: [],
  selected: [],
};
