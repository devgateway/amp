/* eslint-disable max-len */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  Accordion, Checkbox, Form, Icon
} from 'semantic-ui-react';
import { OverlayTrigger, Tooltip } from 'react-bootstrap';
import InfoIcon from '../../static/images/icon-information.svg';

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

  handleCategoryTitleClick = (i, event) => {
    const { columns } = this.props;
    const categories = this.extractCategories(columns);
    const ids = columns.filter(j => j.category === categories[i]);
    ids.forEach(j => {
      this.onItemClick(j.id);
    });
    event.stopPropagation();
    event.preventDefault();
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
              {categories.map((cat, i) => {
                const subList = columns.filter(col => (col.category === cat));
                const subSelectedList = selected.filter(j => subList.find(k => k.id === j));
                return (
                  <div key={Math.random()}>
                    <Accordion.Title index={i} active={activeIndex.includes(i)} onClick={this.handleHeaderClick}>
                      <div className="ui checkbox general-checkbox" style={{ zIndex: 9999 }}>
                        <input
                          className="hidden"
                          id={`${i}_cat`}
                          type="checkbox"
                          checked={subSelectedList.length === subList.length} />
                        <label
                          className="general-label"
                          htmlFor={`${i}_cat`}
                          onClick={this.handleCategoryTitleClick.bind(null, i)} />
                      </div>
                      <Icon name="dropdown" className="dropdown-right" />
                      <span className="category-title">
                        {`${cat} (${subSelectedList.length}/${subList.length})`}
                      </span>
                    </Accordion.Title>
                    <Accordion.Content active={activeIndex.includes(i)}>
                      {subList.map(col => (
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
                );
              })}
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
                      <img className="info-icon" src={InfoIcon} alt="info-icon" />
                    </OverlayTrigger>
                  ) : null}
                </div>
              );
            })}
          </Form>
        );
      }
    } else if (showLoadingWhenEmpty) {
      return (
        <Form loading />
      );
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
