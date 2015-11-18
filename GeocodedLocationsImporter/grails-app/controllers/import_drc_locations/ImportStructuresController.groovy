package import_drc_locations

class ImportStructuresController {

    def importStructuresService

    def index() {
        log.info("index")
    }

    def importFileStructures() {
        log.info("importFile")
        def result = importStructuresService.processStructures(params)
        render(view: "result", model: [generatedSQL: result.generatedSQL, errors: result.errors])
    }
}
