/* eslint-disable react/jsx-props-no-spreading */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';
import { Checkbox, Icon } from 'semantic-ui-react';

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
      columns, order, checkbox, onColumnSelectionChange, keyPrefix
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
                <div
                  {...provided.droppableProps}
                  ref={provided.innerRef}
                  style={this.getListStyle(snapshot.isDraggingOver)}
                >
                  {sortedColumns.map((item, index) => (
                    <Draggable key={item.id} draggableId={`${item.id}`} index={index}>
                      {(provided_, snapshot_) => (
                        <div
                          className="sortable-item"
                          ref={provided_.innerRef}
                          {...provided_.draggableProps}
                          {...provided_.dragHandleProps}
                          style={this.getItemStyle(
                            snapshot_.isDragging,
                            provided_.draggableProps.style
                          )}
                        >
                          {checkbox
                            ? (
                              <Checkbox
                                color="green"
                                id={keyPrefix + item.id}
                                label={item.label}
                                onChange={this.onItemClick.bind(null, item.id)} />
                            )
                            : item.label }
                          <Icon name="sort alternate vertical right" color="blue" />
                        </div>
                      )}
                    </Draggable>
                  ))}
                  {provided.placeholder}
                </div>
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
};

ColumnSorter.defaultProps = {
  columns: [],
  order: [],
  checkbox: false,
  onColumnSelectionChange: undefined,
};
