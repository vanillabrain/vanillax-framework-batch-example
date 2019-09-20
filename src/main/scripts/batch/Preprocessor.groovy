package batch

import groovy.util.logging.Log
import vanillax.framework.batch.action.ActionBase

@Log
class Preprocessor extends ActionBase{
    def process(obj){
       log.info "Preprocessing"
        return null
    }
}
