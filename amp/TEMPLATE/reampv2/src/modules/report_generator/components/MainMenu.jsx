import React, { Component } from 'react';
import { Menu } from 'semantic-ui-react';
import './MainMenu.css';

export default class MainMenu extends Component {
  constructor() {
    super();
    this.state = { activeItem: 'Reporting Details' };
  }

  handleItemClick = (e, { name }) => this.setState({ activeItem: name })

  render() {
    const { activeItem } = this.state;
    return (
      <>
        <Menu fluid vertical>
          <Menu.Item
            name="Reporting Details"
            active={activeItem === 'Reporting Details'}
            onClick={this.handleItemClick}
              />
          <Menu.Item
            name="Columns"
            active={activeItem === 'Columns'}
            onClick={this.handleItemClick}
              />
          <Menu.Item
            name="Measures"
            active={activeItem === 'Measures'}
            onClick={this.handleItemClick}
              />
        </Menu>
      </>
    );
  }
}
