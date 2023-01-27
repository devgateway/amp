/* eslint-disable */
import React, { useState, useMemo, useEffect } from 'react';
import { Row } from 'react-bootstrap';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import SkeletonTable from './Table';
import { ColumnDescription } from 'react-bootstrap-table-next';
import { textFilter } from 'react-bootstrap-table2-filter';
import styles from './InidcatorTable.module.css';
import { DefaultComponentProps } from '../../types';
import sampleData from './test_data.json';


interface IndicatorTableProps extends DefaultComponentProps {
    setShowAddForm: any;
}


const IndicatorTable: React.FC<IndicatorTableProps> = ({ setShowAddForm, translations }) => {

    const columns: ColumnDescription<any, any>[] = useMemo(() => [
        {
            dataField: 'id',
            text: 'ID',
            sort: true,
            headerStyle: { width: '10%' },
        },
        {
            dataField: 'indicatorName',
            text: 'Indicator Name',
            sort: true,
            headerStyle: { width: '50%' }
        },
        {
            dataField: 'sector',
            text: 'Sector',
            sort: true,
            headerStyle: { width: '30%' },
        },
        {
            dataField: 'action',
            text: 'Action',
            headerStyle: { width: '30%' },
            csvExport: false,
            formatter: (cell: any, row: any) => {
                return (
                    <Row sm={8} className={styles.action_wrapper}>
                        <div className={styles.action_container}>
                            <i style={{ fontSize: 20, color: '#00D100' }} className="fa fa-pencil" aria-hidden="true" onClick={() => console.log('testing action edit button')}></i>
                        </div>
                        <div className={styles.action_container}>
                            <i style={{ fontSize: 20, color: '#da2a2a' }} className="fa fa-trash" aria-hidden="true"></i>
                        </div>
                    </Row>
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
                title={translations['amp.indicatormanager:table-title']}
                data={ordersData}
                columns={columns}
            />
        </>
    )
}

const InidcatorTableMemo = React.memo(IndicatorTable);

const mapStateToProps = (state: any) => ({
    translations: state.translationsReducer.translations
});

const mapDispatchToProps = (dispatch: any) => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(InidcatorTableMemo);