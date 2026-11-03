import { DisaggregationFormValue } from '../types';

export interface DisaggregationOption {
    id: number;
    value: string;
}

export function buildThreeDisaggregationValues(
    selected: number[],
    options: Record<number, DisaggregationOption[]>,
    current: DisaggregationFormValue[],
    original: DisaggregationFormValue[] = []
): DisaggregationFormValue[] {
    const savedOrientation = [...original, ...current].find(value =>
        value.parentDisaggregationId && value.childDisaggregationId
        && selected.includes(value.parentDisaggregationId)
        && selected.includes(value.childDisaggregationId));
    const savedParent = [...original, ...current].find(value =>
        value.parentDisaggregationId && selected.includes(value.parentDisaggregationId));
    const parent = savedOrientation?.parentDisaggregationId || savedParent?.parentDisaggregationId || selected[0];
    const child = savedOrientation?.childDisaggregationId || selected.find(id => id !== parent);
    const third = selected.find(id => id !== parent && id !== child);
    if (!parent || !child || !third || !options[parent] || !options[child] || !options[third]) {
        return current;
    }

    const result = current.filter(value => value.id != null || value.thirdCategoryId != null
        || hasEnteredDisaggregationValue(value));
    original.forEach(value => {
        if (!result.some(item => item.id === value.id && value.id != null)
            && !result.some(item => sameTuple(item, value))) {
            result.push(value);
        }
    });
    options[parent].forEach(p => options[child].forEach(c => options[third].forEach(t => {
        if (!result.some(item => item.parentCategoryId === p.id && item.childCategoryId === c.id
            && item.thirdCategoryId === t.id)) {
            const emptyValue = () => ({
                originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: ''
            });
            result.push({
                parentCategoryId: p.id, childCategoryId: c.id, thirdCategoryId: t.id,
                parentDisaggregationId: parent, childDisaggregationId: child, thirdDisaggregationId: third,
                base: emptyValue(), target: emptyValue()
            });
        }
    })));
    return result;
}

export function hasEnteredDisaggregationValue(value: DisaggregationFormValue): boolean {
    return [value.base, value.target].some(group => group && Object.values(group)
        .some(item => item !== '' && item != null));
}

function sameTuple(left: DisaggregationFormValue, right: DisaggregationFormValue): boolean {
    return left.parentCategoryId === right.parentCategoryId
        && left.childCategoryId === right.childCategoryId
        && (left.thirdCategoryId ?? null) === (right.thirdCategoryId ?? null);
}