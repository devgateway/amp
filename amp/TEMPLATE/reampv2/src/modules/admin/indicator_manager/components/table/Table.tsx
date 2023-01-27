import React from 'react';
import { Col, Row, Button } from 'react-bootstrap';
import BootstrapTable, { PaginationOptions } from 'react-bootstrap-table-next';
import paginationFactory from 'react-bootstrap-table2-paginator';
import filterFactory, { FilterFactoryProps } from 'react-bootstrap-table2-filter';
import 'react-bootstrap-table2-filter/dist/react-bootstrap-table2-filter.min.css';
import ToolkitProvider, { Search, CSVExport, ToolkitContextType } from 'react-bootstrap-table2-toolkit';
// eslint-disable-next-line import/no-unresolved
import styles from './Table.module.css';

const SkeletonTable = ({ columns, data, title }: any) => {
  const { SearchBar } = Search;
  const { ExportCSVButton } = CSVExport;

  // create a pagination factory
  const paginationOptions: PaginationOptions = {
    paginationSize: 4,
    pageStartIndex: 1,
    alwaysShowAllBtns: true,
    sizePerPageList: [
      {
        text: '10',
        value: 10,
      },
      {
        text: '25',
        value: 25,
      },
      {
        text: '50',
        value: 50,
      },
      {
        text: '100',
        value: 100,
      },
      {
        text: 'All',
        value: data.length,
      }
    ]
  };

  const filterOptions: FilterFactoryProps = {
    afterFilter: (result: any, column: any) => {
      console.log('result', result);
      console.log('column', column);
    }
  };

  return (
    <Col sm={12}>
      <ToolkitProvider
        keyField="id"
        data={data}
        columns={columns}
        search
        exportCSV
      >
        {
          (props: ToolkitContextType) => (
            <div>
              <Col
                sm={12}
                style={{
                  paddingBottom: 15
                }}>
                <Row className={styles.table_header}>
                  <Col sm={6}>
                    <h3>{title}</h3>
                  </Col>
                  <Col sm={6}>
                    <hr />
                  </Col>
                </Row>

                <Row sm={12} className={styles.table_header_bottom}>
                  <Col sm={6}>
                    <Button type="primary">
                      <i className="fa fa-plus" />
                      {' '}
                      <span>Add New</span>
                    </Button>
                  </Col>
                  {/* searchbar should be far right on the col */}
                  <Col sm={6}>
                    <div>
                      <div className={styles.search_container}>
                        <SearchBar
                          {...props.searchProps}
                          placeholder="Search"
                        />
                        <ExportCSVButton
                          {...props.csvProps}
                          className={styles.export_button}
                        >
                          Export
                        </ExportCSVButton>
                      </div>
                    </div>

                  </Col>
                </Row>
              </Col>
              <hr />
              <BootstrapTable
                {...props.baseProps}
                pagination={paginationFactory(paginationOptions)}
                selectRow={{
                  mode: 'checkbox',
                  clickToSelect: false,
                }}
                filter={filterFactory(filterOptions)}
              />
            </div>
          )
        }
      </ToolkitProvider>

    </Col>

  );
};

export default SkeletonTable;
