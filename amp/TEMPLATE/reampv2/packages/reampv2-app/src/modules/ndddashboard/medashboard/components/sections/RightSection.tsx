import React from 'react';
import {DefaultTranslations} from "../../types";
import {Col} from "react-bootstrap";
import SectorProgress from "./SectorProgress";
import {bindActionCreators, Dispatch} from "redux";
import {connect} from "react-redux";

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
                <SectorProgress filters={filters} settings={settings} translations={translations}/>
            </div>
        </div>
    );
};


const mapStateToProps = (state: any) => ({
    translations: state.translationsReducer.translations,
});
const mapDispatchToProps = (dispatch: Dispatch) => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(React.memo(RightSection));

