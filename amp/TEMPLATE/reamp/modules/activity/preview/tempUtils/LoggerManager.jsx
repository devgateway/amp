export default class LoggerManager {
    constructor(module) {

    }
    log(message) {
        console.log(message);
    }

    info(message) {
        console.info(message);
    }

    debug(message) {
        console.debug(message);

    }

    warn(message) {
        console.warn(message);
    }

    error(message) {
        console.error(message);
    }
}
