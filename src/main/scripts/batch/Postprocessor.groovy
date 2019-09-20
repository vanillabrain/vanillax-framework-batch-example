package batch

import groovy.util.logging.Log
import vanillax.framework.batch.action.ActionBase

@Log
class Postprocessor extends ActionBase{
    def process(obj){
       log.info "Post-Processing"
        return null
    }
}
