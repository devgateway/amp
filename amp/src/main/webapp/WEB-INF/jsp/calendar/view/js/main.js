function get(field, arg) {
    if (arg != null && arg.match('^[0-9]{2}/[0-9]{2}/[0-9]{4}$')) {
        // parse date
        var tokens = arg.split('/');
        if (field == 'year') {
            return tokens[2];
        } else if (field == 'month') {
            return tokens[1];
        } else if (field == 'day') {
            return tokens[0];
        }
    } else if (arg != null && arg.match('^[0-9]{2}:[0-9]{2}$')) {
        // parse time
        var tokens = arg.split(':');
        if (field == 'hour') {
            return tokens[0];
        } else if (field == 'minute') {
            return tokens[1];
        }
    }
    return null;
}

function createYearCombo(combo, dateStr) {
    if (combo == null) {
        return;
    }
    var year = parseInt(get('year', dateStr));
    if (year == null) {
        return;
    }
    var minYear = year - 10;
    var maxYear = year + 10;
    for (var i = minYear; i <= maxYear; i++) {
        var option = document.createElement("OPTION");
        option.value = i;
        option.text = i;
        combo.options.add(option, i);
        if (i == year) {
            combo.options[i - minYear].selected = true;
        }
    }
}

function selectOptionByValue(combo, value) {
    if (combo == null) {
        return;
    }
    var options = combo.options;
    if (options == null) {
        return;
    }
    for (var i = 0; i < options.length; i++) {
        if (options[i].value == value) {
            options[i].selected = true;
            break;
        }
    }
}

function updateDate(hidden, field, value) {
    if (hidden == null) {
        return;
    }
    var year = get('year', hidden.value);
    var month = get('month', hidden.value);
    var day = get('day', hidden.value);
    if (field == 'year') {
        hidden.value = day + '/' + month + '/' + value;
    } else if (field == 'month') {
        hidden.value = day + '/' + value + '/' + year;
    } else if (field == 'day') {
        hidden.value = value + '/' + month + '/' + year;
    }
}

function updateTime(hidden, field, value) {
    if (hidden == null) {
        return;
    }
    var hour = get('hour', hidden.value);
    var minute = get('minute', hidden.value);
    if (field == 'hour') {
        hidden.value = value + ':' + minute;
    } else if (field == 'minute') {
        hidden.value = hour + ':' + value;
    }
}
