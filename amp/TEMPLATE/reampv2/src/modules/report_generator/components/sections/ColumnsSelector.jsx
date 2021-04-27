import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Accordion, Checkbox, Form, Icon, Image
} from 'semantic-ui-react';

export default class ColumnsSelector extends Component {
  constructor() {
    super();
    this.state = { activeIndex: -1 };
  }

  handleClick = (e, titleProps) => {
    const { index } = titleProps;
    const { activeIndex } = this.state;
    const newIndex = activeIndex === index ? -1 : index;
    this.setState({ activeIndex: newIndex });
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
    const { columns } = this.props;
    const { activeIndex } = this.state;
    if (columns.length > 0) {
      const categories = this.extractCategories(columns);
      return (
        <Form>
          <Accordion fluid styled>
            {categories.map((cat, i) => (
              <>
                <Accordion.Title index={i} active={activeIndex === i} onClick={this.handleClick}>
                  <Icon name="dropdown" />
                  {cat}
                </Accordion.Title>
                <Accordion.Content active={activeIndex === i}>
                  {columns.filter(col => (col.category === cat)).map(col => (
                    <>
                      <Checkbox />
                      <span>{col.label}</span>
                    </>
                  ))}
                </Accordion.Content>
              </>
            ))}
          </Accordion>
        </Form>
      );
    } else {
      return <Image src="https://react.semantic-ui.com/images/wireframe/media-paragraph.png" />;
    }
  }
}

ColumnsSelector.propTypes = {
  columns: PropTypes.array,
  onColumnChange: PropTypes.func.isRequired,
};

ColumnsSelector.defaultProps = {
  columns: []
};
