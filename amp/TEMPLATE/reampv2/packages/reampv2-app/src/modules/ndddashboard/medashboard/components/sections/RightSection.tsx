import React from 'react';
import {DefaultTranslations} from "../../types";
import {Col} from "react-bootstrap";
import SectorClassification from "./SectorClassification";
import {bindActionCreators, Dispatch} from "redux";
import {connect} from "react-redux";
import IndicatorBySector from "./IndicatorBySector";

interface RightSectionProps {
    translations?: DefaultTranslations,
    filters: any,
    settings: any
}

const RightSection: React.FC<RightSectionProps> = (props) => {
    const { translations, filters, settings } = props;

    return (
        <div>
            <div style={{
                minHeight: '350px'
            }}>
                <SectorClassification filters={filters} settings={settings} translations={translations}/>
            </div>

            <div>
                <IndicatorBySector translations={translations as DefaultTranslations} filters={filters} settings={settings} />
            </div>
        </div>
    );
};


const mapStateToProps = (state: any) => ({
    translations: state.translationsReducer.translations,
});
const mapDispatchToProps = (dispatch: Dispatch) => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(React.memo(RightSection));

