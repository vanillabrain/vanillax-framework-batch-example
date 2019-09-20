package common

import groovy.util.logging.Log
import vanillax.framework.core.util.StringUtil
import vanillax.framework.webmvc.exception.BaseException

@Log
class Validator {

    def hasParam(data, requiredItem){
        return hasItem(data._param, requiredItem)
    }

    /**
     * Object 내에 특정 항목이 있는지 확인한다.
     * @param inputData 검사대상의 객체 (Map)
     * @param requiredItem 필수 항목명 혹은 항목 리스트
     * @return 필수 항목이 모두있는 경우 true 반환
     */
    def hasItem(inputData, requiredItem){
        def list = null
        if(inputData == null)
            return false

        if(requiredItem instanceof List){
            list = requiredItem
        }else if(requiredItem instanceof String || requiredItem instanceof GString) {
            list = []
            list << requiredItem
        }else{
            //Error
            throw new IllegalArgumentException("비교 항목은 문자열이거나 문자열의 배열이어야 합니다")
        }
        list.each {it ->
            if(!inputData[it]){
                if(inputData instanceof Map && inputData.containsKey(it) && inputData[it] != null){
                    //숫자형이고 property가 존재하면 유효한 것으로 판단
                    if(inputData[it] instanceof Integer || inputData[it] instanceof Float || inputData[it] instanceof Long || inputData[it] instanceof Double){
                        return true //list.each에서 벗어나는 게 아니다.
                    }
                }
                throw new BaseException("ERR1201", "필수 파라미터가 제시되지않았습니다","필수 항목 : $it in [" + arrayToCommaString(requiredItem) +"]",null)
            }
        }
        return true
    }

    def hasItemInList(inputDataList, requiredItem){
        inputDataList.each{ it ->
            hasItem(it, requiredItem)
        }
        return true
    }

    /**
     * URL에 ID값이 정상적으로 입력되는지 확인한다.
     * 예) /my/url/1 --> 정상
     *     /my/url/gaga --> 비정상
     *     /my/url --> 비정상
     * @param data
     */
    def isProperId(data){
        if(!data._path || !isNumber(data._path)){
            throw new BaseException("ERR1202", "잘못된 형식의 데이터입니다 : $data._path",null,null)
        }
    }

    def isNumber(str){
        String s = str as String
        if(!s)
            return false
        for(int i; i < s.length(); i++){
            char c = s.charAt(i)
            if (c >= '0' && c <= '9'){
                //OK
            }else{
                return false
            }
        }
        return true
    }

    def private arrayToCommaString(item){
        if(!item || item instanceof String){
            return item
        }
        return item.join(',')
    }

}
