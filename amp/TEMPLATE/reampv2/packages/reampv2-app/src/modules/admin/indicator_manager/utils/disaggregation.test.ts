import { DisaggregationFormValue } from '../types';
import { buildThreeDisaggregationValues } from './disaggregation';

const empty = () => ({ originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' });
const legacy = (id: number, originalValue = 7): DisaggregationFormValue => ({
    id, parentCategoryId: 10, childCategoryId: 20,
    parentDisaggregationId: 1, childDisaggregationId: 2,
    base: { ...empty(), originalValue }, target: empty()
});
const options = {
    1: [{ id: 10, value: 'A' }],
    2: [{ id: 20, value: 'B' }],
    3: [{ id: 30, value: 'C' }, { id: 31, value: 'D' }]
};

test('preserves unallocated values while creating independent combinations', () => {
    const old = legacy(100);
    const result = buildThreeDisaggregationValues([3, 2, 1], options, [old], [old]);
    expect(result).toHaveLength(3);
    expect(result[0]).toBe(old);
    expect(result.map(item => item.thirdCategoryId ?? null)).toEqual([null, 30, 31]);
    expect(result[1].base.originalValue).toBe('');
    expect(result[1].parentCategoryId).toBe(10);
    expect(result[1].childCategoryId).toBe(20);
});

test('restores persisted values if an intermediate edit removed them', () => {
    const old = legacy(100);
    const result = buildThreeDisaggregationValues([1, 2, 3], options, [], [old]);
    expect(result[0]).toBe(old);
});

test('does not replace modified entries or regenerate the same tuples', () => {
    const modified = legacy(100, 8);
    const first = buildThreeDisaggregationValues([1, 2, 3], options, [modified], [legacy(100)]);
    const second = buildThreeDisaggregationValues([1, 2, 3], options, first, [legacy(100)]);
    expect(second).toHaveLength(3);
    expect(second[0].base.originalValue).toBe(8);
});

test('does not create unallocated rows from untouched intermediate selections', () => {
    const blank = { ...legacy(1), id: undefined, base: empty() };
    const result = buildThreeDisaggregationValues([1, 2, 3], options, [blank]);
    expect(result.map(item => item.thirdCategoryId)).toEqual([30, 31]);
});

test('waits for all option lists rather than clearing existing rows', () => {
    const old = legacy(100);
    expect(buildThreeDisaggregationValues([1, 2, 3], { 1: options[1] }, [old])).toEqual([old]);
});