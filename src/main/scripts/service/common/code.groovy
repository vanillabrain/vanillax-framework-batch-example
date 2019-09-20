package service.common

import groovy.util.logging.Log

import sql.common.CommonCodeDAO
import vanillax.framework.core.db.Transactional
import vanillax.framework.core.object.Autowired
import vanillax.framework.webmvc.service.ServiceBase

@Log
class code extends ServiceBase{
    @Autowired
    CommonCodeDAO commonCodeDAO

    @Transactional
    def find(data){
        return commonCodeDAO.selectCommonCodeList(data._param)
    }

}