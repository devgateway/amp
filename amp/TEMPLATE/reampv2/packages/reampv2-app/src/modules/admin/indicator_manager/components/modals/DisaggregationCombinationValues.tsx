import React from 'react';
import { Card, Form } from 'react-bootstrap';
import { DefaultTranslationPackTypes, DisaggregationFormValue } from '../../types';
import { DisaggregationOption } from '../../utils/disaggregation';
import DateInput from '../DateInput';
import initialTranslations from '../../config/initialTranslations.json';
import styles from './css/IndicatorModal.module.css';

interface Props {
    values: DisaggregationFormValue[];
    options: Record<number, DisaggregationOption[]>;
    translations: DefaultTranslationPackTypes;
    onChange: (index: number, path: string[], value: unknown) => void;
}

const DisaggregationCombinationValues: React.FC<Props> = ({ values, options, translations, onChange }) => {
    const t = (key: string): string =>
        translations[key] ?? initialTranslations[key as keyof typeof initialTranslations] ?? key;
    const label = (id: number | null | undefined, unallocatedKey: string) => {
        if (id == null) return t(unallocatedKey);
        const option = Object.values(options).reduce<DisaggregationOption[]>(
            (all, group) => all.concat(group), []).find(item => item.id === id);
        return option ? option.value : String(id);
    };

    return (
        <div className={styles.disaggregationValuesPanel}>
            <p>{t('amp.indicatormanager:third-values-description')}</p>
            {values.map((entry, index) => (
                <Card key={entry.id ?? `${entry.parentCategoryId}-${entry.childCategoryId}-${entry.thirdCategoryId}`}>
                    <Card.Body>
                        <Card.Title>
                            {label(entry.parentCategoryId, 'amp.indicatormanager:unallocated')}
                            {' / '}{label(entry.childCategoryId, 'amp.indicatormanager:unallocated')}
                            {' / '}{label(entry.thirdCategoryId, 'amp.indicatormanager:unallocated-third')}
                        </Card.Title>
                        <div className={styles.disaggregationValueFields}>
                            {(['base', 'target'] as const).map(kind => (
                                <div className={styles.disaggregationValueColumn} key={kind}>
                                    <h6>{t(`amp.indicatormanager:${kind}-values`)}</h6>
                                    {(['original', 'revised'] as const).map(version => {
                                        const valueKey = `${version}Value` as 'originalValue' | 'revisedValue';
                                        const dateKey = `${version}ValueDate` as 'originalValueDate' | 'revisedValueDate';
                                        return (
                                            <React.Fragment key={version}>
                                                <Form.Group>
                                                    <Form.Label>{t(`amp.indicatormanager:${version}-value`)}</Form.Label>
                                                    <Form.Control type="number" value={entry[kind]?.[valueKey] ?? ''}
                                                        onChange={event => onChange(index, [kind, valueKey], event.target.value)} />
                                                </Form.Group>
                                                <Form.Group>
                                                    <Form.Label>{t(`amp.indicatormanager:${version}-value-date`)}</Form.Label>
                                                    <DateInput translations={translations} value={entry[kind]?.[dateKey] || ''}
                                                        onChange={value => onChange(index, [kind, dateKey], value)} />
                                                </Form.Group>
                                            </React.Fragment>
                                        );
                                    })}
                                </div>
                            ))}
                        </div>
                    </Card.Body>
                </Card>
            ))}
        </div>
    );
};

export default DisaggregationCombinationValues;