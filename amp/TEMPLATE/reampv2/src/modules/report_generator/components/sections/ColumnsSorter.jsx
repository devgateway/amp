/* eslint-disable react/jsx-props-no-spreading */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';
import { Checkbox, Icon } from 'semantic-ui-react';
import { TRN_PREFIX } from '../../utils/constants';

export default class ColumnSorter extends Component {
  constructor(props) {
    super(props);
    this.onDragEnd = this.onDragEnd.bind(this);
  }

  reorder = (list, startIndex, endIndex) => {
    const result = Array.from(list);
    const [removed] = result.splice(startIndex, 1);
    result.splice(endIndex, 0, removed);
    return result;
  };

  moveUp = (index) => {
    const { order, onColumnSortChange } = this.props;
    const items = this.reorder(order, index, index - 1);
    onColumnSortChange(items);
  }

  moveDown = (index) => {
    const { order, onColumnSortChange } = this.props;
    const items = this.reorder(order, index, index + 1);
    onColumnSortChange(items);
  }

  getListStyle = isDraggingOver => ({
    background: isDraggingOver ? 'transparent' : 'transparent',
    padding: '5px',
  });

  getItemStyle = (isDragging, draggableStyle) => ({
    userSelect: 'none',
    padding: '5px',
    margin: '0 0 6px 0',
    background: isDragging ? 'transparent' : 'transparent',
    // styles we need to apply on draggables
    ...draggableStyle
  });

  onDragEnd = (result) => {
    const { order, onColumnSortChange } = this.props;
    // dropped outside the list
    if (!result.destination) {
      return;
    }
    const items = this.reorder(
      order,
      result.source.index,
      result.destination.index
    );
    onColumnSortChange(items);
  }

  handleHeaderClick = (e, titleProps) => {
    const { index } = titleProps;
    const { activeIndex } = this.state;
    const newIndex = activeIndex === index ? -1 : index;
    this.setState({ activeIndex: newIndex });
  }

  onItemClick = (id) => {
    const { onColumnSelectionChange } = this.props;
    onColumnSelectionChange(id);
  }

  render() {
    const {
      columns, order, checkbox, keyPrefix, translations, selected
    } = this.props;
    if (columns.length > 0) {
      let sortedColumns = columns;
      if (order.length > 0) {
        sortedColumns = [];
        order.forEach(i => {
          sortedColumns.push(columns.find(j => j.id === i));
        });
      }
      return (
        <>
          <DragDropContext onDragEnd={this.onDragEnd}>
            <Droppable droppableId={`${keyPrefix}_droppable`}>
              {(provided, snapshot) => (
                <table
                  {...provided.droppableProps}
                  ref={provided.innerRef}
                  style={this.getListStyle(snapshot.isDraggingOver)}
                >
                  <tbody>
                    {sortedColumns.map((item, index) => (
                      <Draggable key={item.id} draggableId={`${keyPrefix + item.id}`} index={index}>
                        {(provided_, snapshot_) => (
                          <tr
                            className="sortable-tr"
                            ref={provided_.innerRef}
                            {...provided_.draggableProps}
                            {...provided_.dragHandleProps}
                            style={this.getItemStyle(
                              snapshot_.isDragging,
                              provided_.draggableProps.style
                            )}
                        >
                            <td className="sortable-td">
                              {checkbox
                                ? (
                                  <Checkbox
                                    color="green"
                                    id={keyPrefix + item.id}
                                    label={item.label ? item.label : item.name}
                                    checked={selected.find(j => j === item.id) !== undefined}
                                    onChange={this.onItemClick.bind(null, item.id)} />
                                )
                                : <span>{item.label ? item.label : item.name}</span> }
                            </td>
                            <td className="sortable-td td-arrows">
                              <span
                                className={index === 0 ? 'disabled' : ''}
                                title={translations[`${TRN_PREFIX}moveUp`]}
                                onClick={this.moveUp.bind(null, index)}>
                                <Icon name="arrow up" color="blue" />
                              </span>
                              <span
                                className={index === sortedColumns.length - 1 ? 'disabled' : ''}
                                title={translations[`${TRN_PREFIX}moveDown`]}
                                onClick={this.moveDown.bind(null, index)}>
                                <Icon name="arrow down" color="blue" />
                              </span>
                            </td>
                          </tr>
                        )}
                      </Draggable>
                    ))}
                    {provided.placeholder}
                  </tbody>
                </table>
              )}
            </Droppable>
          </DragDropContext>
        </>
      );
    } else {
      return null;
    }
  }
}

ColumnSorter.propTypes = {
  columns: PropTypes.array,
  order: PropTypes.array,
  onColumnSortChange: PropTypes.func.isRequired,
  checkbox: PropTypes.bool,
  onColumnSelectionChange: PropTypes.func,
  keyPrefix: PropTypes.string.isRequired,
  translations: PropTypes.object.isRequired,
  selected: PropTypes.array,
};

ColumnSorter.defaultProps = {
  columns: [],
  order: [],
  checkbox: false,
  onColumnSelectionChange: undefined,
  selected: [],
};
