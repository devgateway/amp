/* eslint-disable max-len */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Accordion, Checkbox, Form, Icon, Image
} from 'semantic-ui-react';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';

export default class ColumnsSelector extends Component {
  constructor() {
    super();
    this.state = { activeIndex: [] }; // No need to keep this in Redux's store.
  }

  handleHeaderClick = (e, titleProps) => {
    const { index } = titleProps;
    const { activeIndex } = this.state;
    if (activeIndex.includes(index)) {
      activeIndex.splice(activeIndex.findIndex(i => i === index));
    } else {
      activeIndex.push(index);
    }
    this.setState({ activeIndex });
  }

  onItemClick = (id) => {
    const { onColumnSelectionChange } = this.props;
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
      columns, showLoadingWhenEmpty, selected, radio, noCategories
    } = this.props;
    const { activeIndex } = this.state;
    if (columns.length > 0) {
      if (!noCategories) {
        const categories = this.extractCategories(columns);
        return (
          <Form>
            <Accordion fluid styled exclusive={false}>
              {categories.map((cat, i) => (
                <div key={Math.random()}>
                  <Accordion.Title index={i} active={activeIndex.includes(i)} onClick={this.handleHeaderClick}>
                    <Icon name="dropdown" />
                    {cat}
                  </Accordion.Title>
                  <Accordion.Content active={activeIndex.includes(i)}>
                    {columns.filter(col => (col.category === cat))
                      .map(col => (
                        <div className="column-item" key={Math.random()}>
                          <Checkbox
                            key={Math.random()}
                            radio={radio}
                            onClick={this.onItemClick.bind(null, col.id)}
                            id={col.id}
                            value={col.id}
                            checked={selected.find(j => j === col.id) !== undefined}
                            label={col.label ? col.label : col.name} />
                        </div>
                      ))}
                  </Accordion.Content>
                </div>
              ))}
            </Accordion>
          </Form>
        );
      } else {
        return (
          <Form>
            {columns.map((col) => {
              const tooltipText = col.description ? (
                <Tooltip id={col.description}>
                  {col.description}
                </Tooltip>
              ) : null;
              return (
                <div className="column-item" key={Math.random()}>
                  <Checkbox
                    color="green"
                    id={Math.random()}
                    label={col.label ? col.label : col.name}
                    checked={selected.find(j => j === col.id) !== undefined}
                    onChange={this.onItemClick.bind(null, col.id)} />
                  {col.description ? (
                    <OverlayTrigger trigger={['hover', 'focus']} overlay={tooltipText}>
                      <img
                        alt="info-icon"
                        className="info-icon"
                        src="/TEMPLATE/reamp/modules/admin/data-freeze-manager/styles/images/icon-information.svg" />
                    </OverlayTrigger>
                  ) : null}
                </div>
              );
            })}
          </Form>
        );
      }
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
  noCategories: PropTypes.bool,
};

ColumnsSelector.defaultProps = {
  columns: [],
  selected: [],
  showLoadingWhenEmpty: false,
  radio: false,
  noCategories: false
};
