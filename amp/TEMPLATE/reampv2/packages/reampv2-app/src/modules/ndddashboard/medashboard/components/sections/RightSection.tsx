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

    const [selectedClassification, setSelectedClassification] = React.useState<number | null>(null);

    return (
        <div>
            <div>
                <SectorClassification
                    filters={filters}
                    settings={settings}
                    translations={translations}
                    selectedClassification={selectedClassification}
                    setSelectedClassification={setSelectedClassification}
                />
            </div>

            <div>
                <IndicatorBySector translations={translations as DefaultTranslations} filters={filters} settings={settings} selectedClassification={selectedClassification} />
            </div>
        </div>
    );
};


const mapStateToProps = (state: any) => ({
    translations: state.translationsReducer.translations,
});
const mapDispatchToProps = (dispatch: Dispatch) => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(React.memo(RightSection));

