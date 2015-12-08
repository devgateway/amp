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
        if(result.excel != null) {
            response.setContentType("application/octet-stream") // or or image/JPEG or text/xml or whatever type the file is
            response.setHeader("Content-disposition", "attachment;filename=\"test.xls\"")
            response.outputStream << result.excel.storeLocation.bytes
        }
    }
}
