import * as log from 'loglevel';

export default class LoggerManager {

    constructor(module) {
        this.logger = log.getLogger(module);
        if (process.env.NODE_ENV && process.env.NODE_ENV === 'production') {
            this.logger.setLevel(log.levels.WARN);
        }
    }

    log(message) {
        this.logger.info(message);
    }

    info(message) {
        this.logger.info(message);
    }

    debug(message) {
        this.logger.debug(message);
    }

    warn(message) {
        this.logger.warn(message);
    }

    error(message) {
        this.logger.error(message);
    }
}
