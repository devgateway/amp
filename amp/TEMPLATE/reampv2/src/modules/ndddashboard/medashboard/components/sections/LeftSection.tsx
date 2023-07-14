import React, { useEffect, useState } from 'react';
import IndicatorByProgram from './IndicatorByProgram';
import ProgramGroupedByIndicator from './ProgramGroupedByIndicator';
import { Button, Row } from 'react-bootstrap';
import { DefaultTranslations, ProgramConfigChild } from '../../types';
import { Dispatch, bindActionCreators } from 'redux';
import { connect, useSelector } from 'react-redux';
import styles from './css/Styles.module.css';
import { findProgramConfig, extractLv1Children } from '../../utils/data';

interface LeftSectionProps {
  translations: DefaultTranslations,
  filters: any
}

const LeftSection: React.FC<LeftSectionProps> = (props) => {
  const { translations, filters } = props;

  const programConfigurationReducer = useSelector((state: any) => state.programConfigurationReducer);

  const [selectedConfiguration, setSelectedConfiguration] = useState<number | null>(null);
  const [level1Children, setLevel1Children] = useState<ProgramConfigChild[]>([]);
  const [level1Child, setLevel1Child] = useState<number | null>(null);

  if (!selectedConfiguration && programConfigurationReducer.data) {
    setSelectedConfiguration(programConfigurationReducer.data[0].ampProgramSettingsId);
  }

  useEffect(() => {
    if (selectedConfiguration && programConfigurationReducer.data) {
      const foundProgram = findProgramConfig(selectedConfiguration, programConfigurationReducer.data);

      if (foundProgram) {
        const children = extractLv1Children(foundProgram);
        setLevel1Children(children);
        setLevel1Child(children[0].id);
      }
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedConfiguration]);

  return (
    <div>
      {programConfigurationReducer.loading ? <div className="loading">Loading...</div> :
        <IndicatorByProgram
          translations={translations}
          programConfiguration={programConfigurationReducer.data}
          setLevel1Child={setLevel1Child}
          selectedConfiguration={selectedConfiguration}
          setSelectedConfiguration={setSelectedConfiguration}
          level1Children={level1Children}
          setLevel1Children={setLevel1Children}
          level1Child={level1Child}
          filters={filters}
        />
      }

      {(!level1Child) ? <div className="loading">Loading...</div> :
        <ProgramGroupedByIndicator
          translations={translations}
          level1Child={level1Child}
          filters={filters}
        />
      }

      <Row md={12} style={{
        display: 'flex',
        marginLeft: 0,
        alignItems: 'center',
        justifyContent: 'center',
        paddingTop: '10%',
        paddingBottom: '10%'

      }}>
        <Button className={styles.button}>
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" className="bi bi-database-add" viewBox="0 0 16 16">
            <path d="M12.5 16a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7Zm.5-5v1h1a.5.5 0 0 1 0 1h-1v1a.5.5 0 0 1-1 0v-1h-1a.5.5 0 0 1 0-1h1v-1a.5.5 0 0 1 1 0Z" />
            <path d="M12.096 6.223A4.92 4.92 0 0 0 13 5.698V7c0 .289-.213.654-.753 1.007a4.493 4.493 0 0 1 1.753.25V4c0-1.007-.875-1.755-1.904-2.223C11.022 1.289 9.573 1 8 1s-3.022.289-4.096.777C2.875 2.245 2 2.993 2 4v9c0 1.007.875 1.755 1.904 2.223C4.978 15.71 6.427 16 8 16c.536 0 1.058-.034 1.555-.097a4.525 4.525 0 0 1-.813-.927C8.5 14.992 8.252 15 8 15c-1.464 0-2.766-.27-3.682-.687C3.356 13.875 3 13.373 3 13v-1.302c.271.202.58.378.904.525C4.978 12.71 6.427 13 8 13h.027a4.552 4.552 0 0 1 0-1H8c-1.464 0-2.766-.27-3.682-.687C3.356 10.875 3 10.373 3 10V8.698c.271.202.58.378.904.525C4.978 9.71 6.427 10 8 10c.262 0 .52-.008.774-.024a4.525 4.525 0 0 1 1.102-1.132C9.298 8.944 8.666 9 8 9c-1.464 0-2.766-.27-3.682-.687C3.356 7.875 3 7.373 3 7V5.698c.271.202.58.378.904.525C4.978 6.711 6.427 7 8 7s3.022-.289 4.096-.777ZM3 4c0-.374.356-.875 1.318-1.313C5.234 2.271 6.536 2 8 2s2.766.27 3.682.687C12.644 3.125 13 3.627 13 4c0 .374-.356.875-1.318 1.313C10.766 5.729 9.464 6 8 6s-2.766-.27-3.682-.687C3.356 4.875 3 4.373 3 4Z" />
          </svg>
          <span className={styles.button_text}>{translations['amp.ndd.dashboard:me-add-indicator']}</span>
        </Button>
      </Row>
    </div>
  )
}

const mapStateToProps = (state: any) => ({
  translations: state.translationsReducer.translations,
});
const mapDispatchToProps = (dispatch: Dispatch) => bindActionCreators({}, dispatch);

export default connect(mapStateToProps, mapDispatchToProps)(LeftSection);
