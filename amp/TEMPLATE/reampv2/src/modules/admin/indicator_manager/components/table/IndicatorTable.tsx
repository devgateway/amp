/* eslint-disable */
import React, { useState, useMemo, useEffect } from 'react';

import SkeletonTable from './Table';
import { ColumnDescription } from 'react-bootstrap-table-next';
import { textFilter } from 'react-bootstrap-table2-filter';
// import ActionMenu from '../../components/tables/ActionMenu';

const sampleData = [
  {
    "id": 1,
    "name": "Leanne Graham",
    "email": "test@test.com",
    "city": "test city",
    "company": "test company"
  },
  {
    "id": 2,
    "name": "Aeanne Graham",
    "email": "abc@test.com",
    "city": "test city",
    "company": "test company"
  }
]


const FetchPolicy = ({ setShowAddForm }: { setShowAddForm: any }) => {

    const columns: ColumnDescription<any, any>[] = useMemo(() => [
        {
            dataField: 'id',
            text: 'ID',
            sort: true,
            headerStyle: { width: '10%' },
        },
        {
            dataField: 'name',
            text: 'Name',
            sort: true,
            headerStyle: { width: '20%' }
        },
        {
            dataField: 'email',
            text: 'Email',
            sort: true,
            headerStyle: { width: '20%' },
        },
        {
            dataField: 'city',
            text: 'City',
            sort: true,
            headerStyle: { width: '20%' },
        },
        {
            dataField: 'action',
            text: 'Action',
            headerStyle: { width: '30%' },
            csvExport: false,
            formatter: (cell: any, row: any) => {
                return (
                    <div className="d-flex justify-content-center">
                        <div className="mr-2">
                            <i className="fa fa-pencil" aria-hidden="true" onClick={() => setShowAddForm(true)}></i>
                        </div>
                        <div>
                            <i className="fa fa-trash" aria-hidden="true"></i>
                        </div>
                    </div>
                )
            },
        }
    // eslint-disable-next-line react-hooks/exhaustive-deps
    ], []);

    const [ordersData, setOrdersData] = useState<any>(useMemo(() => [], []));

    const fetchData = async () => {
        setOrdersData(sampleData);
    }

    useEffect(() => {
        fetchData();
    }, []);

    return (
        <>
            <SkeletonTable
            title="Inidcation Management" 
            data={ordersData} 
            columns={columns}
            />
        </>
    )
}

export default React.memo(FetchPolicy);
