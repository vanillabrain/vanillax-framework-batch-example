package common

import groovy.util.logging.Log
import vanillax.framework.webmvc.exception.BaseException

@Log
class ObjectHelper {

    /**
     * 세셩상의 사용자 ID를 param에 넣어준다. DELETE 방식처럼 data._input을 참조하지 않는 경우 강제 세션값 세팅을 위해 사용.
     * @param data
     * @param param
     * @return
     */
    def putUserId(data, param){
        param['_userId'] = data._input._userId
    }

    /**
     * param에 속성중 fields에 명시된 필드의 값만 복제하여 새로운 인스턴스를 생성 반환한다.
     * @param param
     * @param fields
     * @return
     */
    def cloneWithFields(param, fields){
        if(!param || !fields || ! param instanceof Map || ! fields instanceof List)
            return null
        def result = [:]
        fields.each{
            if(! param.containsKey(it)){
                result[it] = null
            }else{
                result[it] = param[it]
            }
        }
        return result
    }

}
