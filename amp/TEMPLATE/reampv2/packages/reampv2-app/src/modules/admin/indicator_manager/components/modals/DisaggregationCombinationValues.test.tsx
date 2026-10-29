import React from 'react';
import ReactDOM from 'react-dom';
import { act, Simulate } from 'react-dom/test-utils';
import DisaggregationCombinationValues from './DisaggregationCombinationValues';
import initialTranslations from '../../config/initialTranslations.json';

jest.mock('../DateInput', () => () => null);

test('renders unallocated and third-dimension rows separately and edits the correct row', () => {
    const empty = { originalValue: '', originalValueDate: '', revisedValue: '', revisedValueDate: '' };
    const values = [
        { id: 1, parentCategoryId: 10, childCategoryId: 20, thirdCategoryId: null,
            base: { ...empty, originalValue: 5 }, target: { ...empty } },
        { parentCategoryId: 10, childCategoryId: 20, thirdCategoryId: 30,
            base: { ...empty }, target: { ...empty } }
    ];
    const onChange = jest.fn();
    const container = document.createElement('div');
    document.body.appendChild(container);
    act(() => {
        ReactDOM.render(<DisaggregationCombinationValues values={values} options={{
            1: [{ id: 10, value: 'Rural' }],
            2: [{ id: 20, value: 'Female' }],
            3: [{ id: 30, value: 'Youth' }]
        }} translations={initialTranslations} onChange={onChange} />, container);
    });
    expect(container.textContent).toContain('Rural / Female / Unallocated in third dimension');
    expect(container.textContent).toContain('Rural / Female / Youth');
    const input = container.querySelectorAll('input[type="number"]')[4] as HTMLInputElement;
    act(() => Simulate.change(input, { target: { value: '12' } } as React.ChangeEvent<HTMLInputElement>));
    expect(onChange).toHaveBeenCalledWith(1, ['base', 'originalValue'], '12');
    ReactDOM.unmountComponentAtNode(container);
    container.remove();
});