package service

import groovy.util.logging.Log
import sql.common.FileDAO
import vanillax.framework.core.db.Transactional
import vanillax.framework.core.object.Autowired
import vanillax.framework.webmvc.service.ServiceBase

@Log
class fileUpload extends ServiceBase{
    @Autowired
    FileDAO fileDAO

    @Transactional(autoCommit = false)
    def insert(data){

        def list = data.uploadFileList;
        def fileList = []
        list.each{
            def id = fileDAO.insertFile(it)
            it.id = id
            it.url = "/file/fileDownload/$id"
            fileList << it
        }
        return fileList
    }

}