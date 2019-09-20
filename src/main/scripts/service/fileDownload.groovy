package service

import common.Validator
import groovy.util.logging.Log
import sql.common.FileDAO
import vanillax.framework.core.db.Transactional
import vanillax.framework.core.object.Autowired
import vanillax.framework.webmvc.config.ConfigHelper
import vanillax.framework.webmvc.service.ServiceBase

@Log
class fileDownload extends ServiceBase{
    @Autowired
    FileDAO fileDAO

    @Autowired
    Validator validator

    @Transactional
    def find(data){
        validator.isProperId(data)
        def file = fileDAO.selectFile([id:data._path])
        if(!file){
            throw new Exception("파일이 없습니다.");
        }
        String uploadPath = ConfigHelper.get("cda.file.upload");
        String fileFullPath = uploadPath + file.filePath;
        File downloadFile = new File(fileFullPath);
        if(!downloadFile.exists() || downloadFile.length() == 0 || downloadFile.isDirectory()){
            throw new Exception("정상적인 파일이 아닙니다 : "+fileFullPath);
        }
        file.downloadFile = downloadFile
        return file
    }

}