import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Accordion, Checkbox, Form, Icon, Image
} from 'semantic-ui-react';

export default class ColumnsSelector extends Component {
  constructor() {
    super();
    this.state = { activeIndex: -1 }; // No need to keep this in Redux's store.
  }

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

  // eslint-disable-next-line class-methods-use-this
  extractCategories(columns) {
    const categories = new Set();
    columns.forEach(item => {
      categories.add(item.category);
    });
    return Array.from(categories);
  }

  render() {
    const {
      columns, showLoadingWhenEmpty, selected, radio
    } = this.props;
    const { activeIndex } = this.state;
    if (columns.length > 0) {
      const categories = this.extractCategories(columns);
      return (
        <Form>
          <Accordion fluid styled>
            {categories.map((cat, i) => (
              <>
                <Accordion.Title index={i} active={activeIndex === i} onClick={this.handleHeaderClick}>
                  <Icon name="dropdown" />
                  {cat}
                </Accordion.Title>
                <Accordion.Content active={activeIndex === i}>
                  {columns.filter(col => (col.category === cat)).map(col => (
                    <div className="column-item">
                      <Checkbox
                        radio={radio}
                        onClick={this.onItemClick}
                        id={col.id}
                        value={col.id}
                        checked={selected.find(j => j === col.id) !== undefined}
                        label={col.label} />
                    </div>
                  ))}
                </Accordion.Content>
              </>
            ))}
          </Accordion>
        </Form>
      );
    } else if (showLoadingWhenEmpty) {
      return <Image src="https://react.semantic-ui.com/images/wireframe/media-paragraph.png" />;
    } else {
      return null;
    }
  }
}

ColumnsSelector.propTypes = {
  columns: PropTypes.array,
  selected: PropTypes.array,
  onColumnSelectionChange: PropTypes.func.isRequired,
  showLoadingWhenEmpty: PropTypes.bool,
  radio: PropTypes.bool,
};

ColumnsSelector.defaultProps = {
  columns: [],
  selected: [],
  showLoadingWhenEmpty: false,
  radio: false,
};
