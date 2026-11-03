import React, { useState } from 'react';
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

interface ValueEntry {
    entry: DisaggregationFormValue;
    index: number;
}

interface CollapsibleSectionProps {
    label: string;
    count?: string;
    children: React.ReactNode;
}

const CollapsibleSection: React.FC<CollapsibleSectionProps> = ({ label, count, children }) => {
    const [expanded, setExpanded] = useState(false);

    return (
        <Card className={styles.disaggregationValueCard}>
            <button type="button" aria-expanded={expanded}
                className={`${styles.accordionHeader} ${styles.disaggregationToggle} ${expanded ? styles.accordionHeaderActive : ''}`}
                onClick={() => setExpanded(!expanded)}>
                <span className={styles.accordionHeaderTitle}>
                    <span className={styles.accordionChevron} aria-hidden="true">
                        <i className={`fa ${expanded ? 'fa-chevron-down' : 'fa-chevron-right'}`} />
                    </span>
                    <span className={styles.accordionHeaderText}>{label}</span>
                </span>
                {count && <span className={styles.accordionMeta}>{count}</span>}
            </button>
            {expanded && <Card.Body>{children}</Card.Body>}
        </Card>
    );
};

const DisaggregationCombinationValues: React.FC<Props> = ({ values, options, translations, onChange }) => {
    const t = (key: string): string =>
        translations[key] ?? initialTranslations[key as keyof typeof initialTranslations] ?? key;
    const label = (id: number | null | undefined, unallocatedKey: string) => {
        if (id == null) return t(unallocatedKey);
        const option = Object.values(options).reduce<DisaggregationOption[]>(
            (all, group) => all.concat(group), []).find(item => item.id === id);
        return option ? option.value : String(id);
    };
    const groups = new Map<number | null, Map<number | null, ValueEntry[]>>();
    values.forEach((entry, index) => {
        const parentId = entry.parentCategoryId ?? null;
        const childId = entry.childCategoryId ?? null;
        if (!groups.has(parentId)) groups.set(parentId, new Map());
        const children = groups.get(parentId)!;
        if (!children.has(childId)) children.set(childId, []);
        children.get(childId)!.push({ entry, index });
    });
    const countLabel = (count: number) => `${count} ${t('amp.indicatormanager:options-count')}`;

    return (
        <div className={styles.disaggregationCombinationSection}>
            <h6>{t('amp.indicatormanager:disaggregation-values')}</h6>
            <div className={styles.disaggregationValuesPanel}>
                {groups.size === 0 && (
                    <div className={styles.emptyDisaggregationState}>{t('amp.indicatormanager:no-disaggregation-values')}</div>
                )}
                {Array.from(groups.entries()).map(([parentId, children]) => (
                    <CollapsibleSection key={parentId ?? 'unallocated'} count={countLabel(children.size)}
                        label={label(parentId, 'amp.indicatormanager:unallocated')}>
                        {Array.from(children.entries()).map(([childId, entries]) => (
                            <CollapsibleSection key={childId ?? 'unallocated'} count={countLabel(entries.length)}
                                label={label(childId, 'amp.indicatormanager:unallocated')}>
                                {entries.map(({ entry, index }) => (
                                    <CollapsibleSection key={entry.id ?? `${entry.thirdCategoryId ?? 'unallocated'}-${index}`}
                                        label={label(entry.thirdCategoryId, 'amp.indicatormanager:unallocated-third')}>
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
                                                                        className={styles.input_field}
                                                                        aria-label={t(`amp.indicatormanager:${kind}-${version}-value`)}
                                                                        onChange={event => onChange(index, [kind, valueKey], event.target.value)} />
                                                                </Form.Group>
                                                                <Form.Group>
                                                                    <Form.Label>{t(`amp.indicatormanager:${version}-value-date`)}</Form.Label>
                                                                    <DateInput translations={translations} value={entry[kind]?.[dateKey] || ''}
                                                                        className={styles.input_field}
                                                                        onChange={value => onChange(index, [kind, dateKey], value)} />
                                                                </Form.Group>
                                                            </React.Fragment>
                                                        );
                                                    })}
                                                </div>
                                            ))}
                                        </div>
                                    </CollapsibleSection>
                                ))}
                            </CollapsibleSection>
                        ))}
                    </CollapsibleSection>
                ))}
            </div>
        </div>
    );
};

export default DisaggregationCombinationValues;