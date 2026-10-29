import React from 'react';
import ReactDOM from 'react-dom';
import { act, Simulate } from 'react-dom/test-utils';
import DisaggregationCombinationValues from './DisaggregationCombinationValues';
import { DisaggregationFormValue } from '../../types';
import initialTranslations from '../../config/initialTranslations.json';

jest.mock('../DateInput', () => (props: { value: string; onChange: (value: string) => void }) => (
    <input aria-label="date" value={props.value} onChange={event => props.onChange(event.target.value)} />
));

const empty = { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' };
const options = {
    1: [{ id: 10, value: 'Rural' }, { id: 11, value: 'Urban' }],
    2: [{ id: 20, value: 'Female' }, { id: 21, value: 'Male' }],
    3: [{ id: 30, value: 'Youth' }, { id: 31, value: 'Adult' }]
};
const makeValues = (): DisaggregationFormValue[] => [
    { id: 1, parentCategoryId: 10, childCategoryId: 20, thirdCategoryId: null,
        base: { ...empty, originalValue: 0 }, target: { ...empty } },
    { id: 2, parentCategoryId: 11, childCategoryId: 21, thirdCategoryId: 31,
        base: { ...empty }, target: { ...empty } },
    { id: 3, parentCategoryId: 10, childCategoryId: 20, thirdCategoryId: 30,
        base: { ...empty }, target: { ...empty } },
    { id: 4, parentCategoryId: 10, childCategoryId: null, thirdCategoryId: null,
        base: { ...empty, originalValue: 7 }, target: { ...empty } }
];

let container: HTMLDivElement;
beforeEach(() => {
    container = document.createElement('div');
    document.body.appendChild(container);
});
afterEach(() => {
    act(() => { ReactDOM.unmountComponentAtNode(container); });
    container.remove();
});

const button = (text: string): HTMLButtonElement => {
    const result = Array.from(container.querySelectorAll('button'))
        .find(item => item.textContent?.startsWith(text));
    if (!result) throw new Error(`Button not found: ${text}`);
    return result;
};
const click = (text: string) => act(() => Simulate.click(button(text)));

test('nests independently collapsible categories and retains distinct values and IDs', () => {
    const onChange = jest.fn();
    let currentValues = makeValues();
    const render = () => {
        ReactDOM.render(<DisaggregationCombinationValues values={currentValues}
            options={options} translations={initialTranslations} onChange={onChange} />, container);
    };
    act(render);

    expect(button('Rural').getAttribute('aria-expanded')).toBe('false');
    expect(container.textContent).not.toContain('Female');
    click('Rural');
    expect(button('Rural').getAttribute('aria-expanded')).toBe('true');
    expect(container.textContent).toContain('Female');
    expect(container.textContent).not.toContain('Youth');
    click('Female');
    expect(container.textContent).toContain('Unallocated in third dimension');
    expect(container.textContent).toContain('Youth');
    expect(container.querySelectorAll('input')).toHaveLength(0);

    click('Unallocated in third dimension');
    expect((container.querySelector('input[type="number"]') as HTMLInputElement).value).toBe('0');
    click('Youth');
    const youthCard = button('Youth').parentElement!;
    const input = youthCard.querySelector('input[type="number"]') as HTMLInputElement;
    act(() => Simulate.change(input, { target: { value: '12' } } as React.ChangeEvent<HTMLInputElement>));
    expect(onChange).toHaveBeenLastCalledWith(2, ['base', 'originalValue'], '12');
    const date = youthCard.querySelector('input[aria-label="date"]') as HTMLInputElement;
    act(() => Simulate.change(date, { target: { value: '2025-01-02' } } as React.ChangeEvent<HTMLInputElement>));
    expect(onChange).toHaveBeenLastCalledWith(2, ['base', 'originalValueDate'], '2025-01-02');
    currentValues = currentValues.map((value, index) => index === 2
        ? { ...value, base: { ...value.base, originalValue: '12', originalValueDate: '2025-01-02' } }
        : value);
    act(render);

    click('Youth');
    expect(button('Youth').getAttribute('aria-expanded')).toBe('false');
    click('Youth');
    expect((button('Youth').parentElement!.querySelector('input[type="number"]') as HTMLInputElement).value).toBe('12');
    expect((button('Youth').parentElement!.querySelector('input[aria-label="date"]') as HTMLInputElement).value).toBe('2025-01-02');
    click('Female');
    click('Female');
    click('Youth');
    expect((button('Youth').parentElement!.querySelector('input[type="number"]') as HTMLInputElement).value).toBe('12');
    click('Rural');
    click('Rural');
    click('Female');
    click('Youth');
    expect((button('Youth').parentElement!.querySelector('input[type="number"]') as HTMLInputElement).value).toBe('12');
    expect(currentValues.map(value => value.id)).toEqual([1, 2, 3, 4]);
    expect(onChange).toHaveBeenCalledTimes(2);
});

test('keeps one-dimensional legacy values accessible separately from two- and three-dimensional values', () => {
    const onChange = jest.fn();
    act(() => {
        ReactDOM.render(<DisaggregationCombinationValues values={makeValues()}
            options={options} translations={initialTranslations} onChange={onChange} />, container);
    });
    click('Rural');
    click('Unallocated');
    click('Unallocated in third dimension');
    const input = container.querySelector('input[type="number"]') as HTMLInputElement;
    expect(input.value).toBe('7');
    act(() => Simulate.change(input, { target: { value: '9' } } as React.ChangeEvent<HTMLInputElement>));
    expect(onChange).toHaveBeenCalledWith(3, ['base', 'originalValue'], '9');
});

test('shows an empty state when no combinations are available', () => {
    act(() => {
        ReactDOM.render(<DisaggregationCombinationValues values={[]}
            options={options} translations={initialTranslations} onChange={jest.fn()} />, container);
    });
    expect(container.textContent).toContain('No Disaggregation Values');
});
