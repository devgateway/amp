import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { DragDropContext, Draggable, Droppable } from 'react-beautiful-dnd';

// a little function to help us with reordering the result
const reorder = (list, startIndex, endIndex) => {
  const result = Array.from(list);
  const [removed] = result.splice(startIndex, 1);
  result.splice(endIndex, 0, removed);

  return result;
};

const grid = 8;

const getListStyle = isDraggingOver => ({
  background: isDraggingOver ? 'lightblue' : 'lightgrey',
  padding: grid,
  width: 250
});

const getItemStyle = (isDragging, draggableStyle) => ({
  // some basic styles to make the items look a bit nicer
  userSelect: 'none',
  padding: grid * 2,
  margin: `0 0 ${grid}px 0`,

  // change background colour if dragging
  background: isDragging ? 'lightgreen' : 'grey',

  // styles we need to apply on draggables
  ...draggableStyle
});

export default class ColumnSorter extends Component {
  constructor(props) {
    super(props);
    this.onDragEnd = this.onDragEnd.bind(this);
  }

  onDragEnd = (result) => {
    const { selected, onColumnSortChange } = this.props;
    // dropped outside the list
    if (!result.destination) {
      return;
    }
    const items = reorder(
      selected,
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

  onItemClick = (obj) => {
    const { onColumnSortChange } = this.props;
    const id = Number.parseInt(obj.target.value, 10);
    onColumnSortChange(id);
  }

  render() {
    const { columns, selected } = this.props;
    if (selected.length > 0) {
      return (
        <>
          <DragDropContext onDragEnd={this.onDragEnd}>
            <Droppable droppableId="droppable">
              {(provided, snapshot) => (
                <div
                  {...provided.droppableProps}
                  ref={provided.innerRef}
                  style={getListStyle(snapshot.isDraggingOver)}
                >
                  {selected.map((item, index) => (
                    <Draggable key={item} draggableId={`${item}`} index={index}>
                      {(provided, snapshot) => (
                        <div
                          ref={provided.innerRef}
                          {...provided.draggableProps}
                          {...provided.dragHandleProps}
                          style={getItemStyle(
                            snapshot.isDragging,
                            provided.draggableProps.style
                          )}
                        >
                          {columns.find(i => i.id === item).label}
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
  selected: PropTypes.array,
  onColumnSortChange: PropTypes.func.isRequired,
};

ColumnSorter.defaultProps = {
  columns: [],
  selected: [],
};
