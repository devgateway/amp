import React, { Component } from 'react';
import {
  Button, Header, Item, Menu
} from 'semantic-ui-react';
import './MainMenu.css';

export default class MainMenu extends Component {
  constructor() {
    super();
    this.state = { activeItem: 'details' };
  }

  handleItemClick = (e, { name }) => this.setState({ activeItem: name })

  render() {
    const { activeItem } = this.state;
    return (
      <>
        <Menu fluid vertical>
          <Menu.Item
            name="details"
            content="Reporting Details"
            active={activeItem === 'details'}
            onClick={this.handleItemClick}
              />
          <Menu.Item
            name="columns"
            content="Columns"
            active={activeItem === 'columns'}
            onClick={this.handleItemClick}
              />
          <Menu.Item
            name="measures"
            content="Measures"
            active={activeItem === 'measures'}
            onClick={this.handleItemClick}
              />
          <Item className="save_buttons_item">
            <Button color="green">Save</Button>
            <Button color="orange">Save As</Button>
          </Item>
        </Menu>
        <Item>
          <Button disabled size="huge" fluid color="grey">+ Run Report</Button>
        </Item>
      </>
    );
  }
}
