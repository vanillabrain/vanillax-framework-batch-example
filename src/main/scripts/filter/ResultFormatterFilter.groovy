package filter

import groovy.util.logging.Log
import vanillax.framework.webmvc.service.FilterBase

import javax.servlet.http.HttpServletRequest

/**
 * 결과데이터 공통 형식으로 변환
 */
@Log
class ResultFormatterFilter extends FilterBase {

    @Override
    Map<String, Object> preprocess(Map<String, Object> param) throws Exception {
        return param
    }

    @Override
    public Object postprocess(Object result) throws Exception {
        def resultForm = [:]

        if(result instanceof Map){
            Map m = (Map)result
            m.remove('_response')
            m.remove('_input')
            m.remove('_request')
        }

        if(result instanceof List){
            resultForm['resultList'] = result
        }else{
            resultForm['resultObject'] = result
        }
        return resultForm;
    }
}