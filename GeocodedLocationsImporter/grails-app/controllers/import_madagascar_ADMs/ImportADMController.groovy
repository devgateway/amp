package import_madagascar_ADMs

import org.springframework.web.multipart.commons.CommonsMultipartFile

class ImportADMController {

    def importADMService

    def index() {}

    def importFiles() {
        log.info("importFile")
        def files = (LinkedList<CommonsMultipartFile>) request.getMultiFileMap().get("files[]")
        def result = importADMService.importADM3s(files)
        render(view: "result", model: [errors: result.errors])
    }
}
