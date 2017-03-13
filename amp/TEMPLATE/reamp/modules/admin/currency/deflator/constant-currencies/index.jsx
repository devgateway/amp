import * as AMP from 'amp/architecture';
import * as NewConstantCurrency from './new.jsx';
import * as Entry from './entry.jsx';
import { DEFLATOR_SETTINGS, CONSTANT_CURRENCIES } from 'amp/config/endpoints';
import { fetchJson, postJson } from 'amp/tools';
import React from 'react';
import { showSave, RequestStatus } from '../tools';
import { Alert } from 'react-bootstrap';
require("./style.less");

class Model extends AMP.Model {
    get(key) {
        var target = super.get(key);
        if ("newConstantCurrency" == key) return target.steal(this, 'constantCurrencies');
        return target;
    }
}

var parseCalendars = (calendars, calendar) => calendars.set(calendar.id, new AMP.Model({
    id: parseInt(calendar.id),
    name: calendar.name
}));

var makeKey = (...args) => args.join('-');

function parseConstantCurrencies(blueprint, data) {
    var accum = new AMP.Model([]);
    for (var calendar in data) {
        var currencies = data[calendar];
        for (var currency in currencies) {
            var periods = currencies[currency].split(', ');
            for (var period in periods) {
                var [from, to] = periods[period].split('-');
                if (!to) to = from;
                accum = accum.set(makeKey(calendar, currency, from, to), blueprint
                    .calendar(calendar)
                    .currency(currency)
                    .from(parseInt(from))
                    .to(parseInt(to))
                )
            }
        }
    }
    return accum;
}


export var init = (currenciesPromise, translationsPromise) => {
    var calendarsPromise = fetchJson(DEFLATOR_SETTINGS).then(settings =>
        settings[2].value.options.reduce(parseCalendars, new AMP.Model())
    );
    var entryPromise = Entry.init(calendarsPromise, currenciesPromise, translationsPromise);
    //constant currencies depend on entryPromise model blueprint
    var constantCurrenciesPromise = Promise.all([entryPromise, fetchJson(CONSTANT_CURRENCIES)])
        .then(([blueprint, data]) => parseConstantCurrencies(blueprint, data));
    return Promise.all([
        currenciesPromise,
        calendarsPromise,
        translationsPromise,
        NewConstantCurrency.init(constantCurrenciesPromise, currenciesPromise, calendarsPromise, translationsPromise),
        entryPromise,
        constantCurrenciesPromise
    ]).then(([currencies, calendars, translations, newConstantCurrency, entryModel, constantCurrencies]) => new Model({
        currencies: currencies,
        calendars: calendars,
        translations: translations,
        newConstantCurrency: newConstantCurrency,
        entryModel: entryModel,
        constantCurrencies: constantCurrencies,
        saveStatus: RequestStatus.INITIAL,
        trash: false
    }))
};

export var actions = AMP.actions({
    save: null,
    saveSuccess: null,
    saveFail: null,
    resetSaveStatus: null,
    cleanTrash: "number",
    undelete: Entry.Model,
    newConstantCurrency: NewConstantCurrency.actions,
    entry: ["string", Entry.actions]
});

export var view = AMP.view(({ __, constantCurrencies, newConstantCurrency, saveStatus, trash }, actions) => (
    <table className="table table-striped constant-currencies">
        <caption><h2>{__('amp.deflator:constantCurrencies')}</h2></caption>
        <thead>
        <tr className="constant-currency-entry">
            <th>{__('amp.deflator:currency')}</th>
            <th>{__('amp.deflator:calendar')}</th>
            <th>{__('amp.deflator:from')}</th>
            <th>{__('amp.deflator:to')}</th>
            <th>{__('amp.deflator:actions')}</th>
        </tr>
        </thead>
        <tbody>
        {constantCurrencies().mapEntries((constantCurrency) => {
            var { calendar, currency, from, to } = constantCurrency;
            var key = makeKey(calendar(), currency(), from(), to());
            return <Entry.view
                key={key}
                model={constantCurrency.checkMerging(newConstantCurrency())}
                actions={actions.entry(key)}
            />
        })}
        </tbody>
        <tfoot>
        <tr onMouseLeave={actions.newConstantCurrency().maybeClose}>
            <NewConstantCurrency.view
                model={newConstantCurrency()}
                actions={actions.newConstantCurrency()}
            />
            <td className="text-right">
                {maybeUndoPopup(__, trash(), actions)}
                {showSave(__)(actions.save)(saveStatus())}
            </td>
        </tr>
        </tfoot>
    </table>
));

var maybeUndoPopup = (__, maybeConstantCurrency, { cleanTrash, undelete }) => maybeConstantCurrency ? (
        <Alert className="undo-popup" bsStyle="info" onDismiss={e => cleanTrash(maybeConstantCurrency.deletedAt())}>
            {maybeConstantCurrency.currency()} {maybeConstantCurrency.from()}&ndash;{maybeConstantCurrency.to()}&nbsp;
            {__('amp.deflator:ccDeleted')}&nbsp;
            <a href="javascript:void(0);" className="alert-link" onClick={e => undelete(maybeConstantCurrency)}>
                {__('amp.deflator:undo')}
            </a>
        </Alert>
    ) : null;

var save = constantCurrencies => actions => postJson(CONSTANT_CURRENCIES,
    constantCurrencies.reduce((result, { calendar, currency, from, to }) => {
        if ("undefined" == typeof result[calendar()]) result[calendar()] = {};
        if ("undefined" == typeof result[calendar()][currency()]) result[calendar()][currency()] = "";
        var string = from() == to() ? from() : [from(), to()].join('-');
        if (result[calendar()][currency()].length) string = ", " + string;
        result[calendar()][currency()] += string;
        return result;
    }, {})
).then(response => response.status >= 200 && response.status < 300 ?
    actions.saveSuccess() :
    actions.saveFail()
).catch(() => actions.saveFail());

var resetSaveStatus = actions => setTimeout(actions.resetSaveStatus, 3000);

var cleanTrashEffect = timestamp => ({ cleanTrash }) => setTimeout(
    () => document.querySelector(".undo-popup:hover") ? null : cleanTrash(timestamp)
    , 5000
);

export var update = (action, model) => actions.match(action, {
    save: () => [
        model.saveStatus(RequestStatus.RUNNING),
        save(model.constantCurrencies())
    ],

    saveSuccess: () => [
        model.saveStatus(RequestStatus.SUCCESS),
        resetSaveStatus
    ],

    saveFail: () => [
        model.saveStatus(RequestStatus.FAIL),
        resetSaveStatus
    ],

    resetSaveStatus: () => model.saveStatus(RequestStatus.INITIAL),

    newConstantCurrency: newConstantCurrencyAction => {
        var updateSubmodel =
            AMP.updateSubmodel.bind(null, ['newConstantCurrency'], NewConstantCurrency.update, newConstantCurrencyAction);
        return NewConstantCurrency.actions.match(newConstantCurrencyAction, {
            add: (calendar, currency, from, to) => updateSubmodel(
                model.constantCurrencies(constantCurrencies =>
                    constantCurrencies.filter(constantCurrency =>
                        !constantCurrency.isIntersectedOrTouched(calendar, currency, from, to)
                    )
                ).setIn(['constantCurrencies', makeKey(calendar, currency, from, to)], model.entryModel()
                    .calendar(calendar)
                    .currency(currency)
                    .from(from)
                    .to(to)
                )
            ),
            maybeClose: () => {
                var [submodel, sideEffect] = updateSubmodel(model);
                return [submodel, actions => sideEffect(actions.newConstantCurrency())]
            },
            _: () => updateSubmodel(model)
        })
    },

    entry: (id, entryAction) => {
        var updateSubmodel = AMP.updateSubmodel.bind(null, ['constantCurrencies', id], Entry.update, entryAction);
        return Entry.actions.match(entryAction, {
            remove: () => {
                var timestampedCC = model.getIn(['constantCurrencies', id]).deletedAt(new Date().getTime());
                return [
                    model.trash(timestampedCC).unsetIn(['constantCurrencies', id]),
                    cleanTrashEffect(timestampedCC.deletedAt())
                ]
            },
            _: () => updateSubmodel(model)
        })
    },

    cleanTrash: timestamp => false !== model.trash() && timestamp == model.trash().deletedAt() ?
        model.trash(false) :
        model,

    undelete: target => model.setIn(
        ['constantCurrencies', makeKey(target.calendar(), target.currency(), target.from(), target.to())]
        , target.deletedAt(null)).trash(false)
});

export var translations = {
    ...NewConstantCurrency.translations,
    ...Entry.translations,
    "amp.deflator:constantCurrencies": "Constant currencies",
    "amp.deflator:calendar": "Calendar",
    "amp.deflator:currency": "Currency",
    "amp.deflator:from": "From",
    "amp.deflator:to": "To",
    "amp.deflator:actions": "Actions",
    "amp.deflator:success": "Success",
    "amp.deflator:failed": "Failed",
    "amp.deflator:ccDeleted": "constant currency deleted.",
    "amp.deflator:undo": "Undo?"
};