package import_drc_locations

class ImportLocationsController {

    def importLocationsService

    def index() {
        log.info("index")
    }

    def importFileADM1_ADM2() {
        log.info("importFile")
        def result = importLocationsService.processADM1(params)
        render(view: "result", model: [generatedSQL: result.generatedSQL, errors: result.errors])
    }

}
