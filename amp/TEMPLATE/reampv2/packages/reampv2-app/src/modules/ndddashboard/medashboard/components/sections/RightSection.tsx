import React from 'react';
import {DefaultTranslations} from "../../types";
import {Col} from "react-bootstrap";
import SectorProgress from "./SectorProgress";

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
                <SectorProgress filters={filters} settings={settings} />
            </div>
        </div>
    );
};

export default RightSection;
