package service


import common.Validator
import groovy.util.logging.Log

import vanillax.framework.core.db.Transactional
import vanillax.framework.core.object.Autowired
import vanillax.framework.webmvc.service.ServiceBase

@Log
class sample extends ServiceBase{
    @Autowired // scripts 디렉토리내에 정의된 모든 Groovy객체를 참조하는 방식
    Validator validator

    /*
    *----------------------------------------------------
    *  정의할 수 있는 함수
    *----------------------------------------------------
    *  def find(data) : findOne(), findMany()함수가 정의되어있지 않으면 GET 방식에서 기본 호출
    *  def findOne(data) : URI 마지막에 ID 값이 정의되어있는 경우 호출. 예) /rest/myService/1234
    *  def findMany(data) : URI 마지막에 ID 값이 정의되어있지 않은 경우 호출. 예) /rest/myService
    *  def insert(data) : POST 방식에서 기본 호출
    *  def post(data) : insert() 함수가 정의되어있지 않은 경우 POST 방식에서 호출.
    *  def update(data) : PUT 방식에서 기본 호출. 결과를 반환하지 않음.
    *  def put(data) : update() 함수가 정의되어있지 않은 경우 PUT 방식에서 호출. 결과를 반환하지 않음.
    *  def delete(data) : DELETE 방식에서 기본 호출. 결과를 반환하지 않음.
    */

    /*
    *----------------------------------------------------
    *  함수 인자값 data의 내용
    *----------------------------------------------------
    *  data._request : HttpServletRequest
    *  data._response : HttpServletResponse
    *  data._param : HttpServletRequest.getParameters() 결과를 맵형태로 제공
    *  data._path : 현재 서비스를 기준으로 뒷 URI. 예) http://cda.3hand.io/rest/user/info --> user라는 서비스에서 data._path는 "info"
    *  data._input : POST, PUT 방식의 입력데이터. GET방식일때는 비어있는 Map이 들어온다.
    *  data._input._userId : 현재 로그인한 사용자의 사용자ID
    */

    /*
    *  findOne(), findMany()함수가 정의되어있지 않으면 GET방식에 호출된다.
    *  예) GET /my/url/1213
    */
    @Transactional
    def find(data){
        log.info("find()")
        log.info("data._path : $data._path")
        log.info("data._param : $data._param")
    }

    /*
    *  ID값이 부여된 경우 GET 방식에 기본호출된다.
    *  예) GET /my/url/1213
    */
    @Transactional
    def findOne(data){
        validator.isProperId(data)
        def id = data._path

    }

    /*
    *  ID값이 부여되지않은 경우 GET 방식에 기본호출된다.
    *  예) GET /my/url
    */
    @Transactional
    def findMany(data){
//        return LineExtractor.extractLine("D:/3Hand-CDA/fileupload/2019/04/30/aaa.tif")
        return LineExtractor.extractLine("D:/3Hand-CDA/fileupload/2019/04/30/ddd.jpg")
        //return ocrHelper.doOCRFromImage("D:/3Hand-CDA/fileupload/2019/04/30/bbb.jpg")
    }

    /**
     * POST 방식에서 기본호출
     */
    @Transactional(autoCommit = false)
    def insert(data){
        log.info("data._input : $data._input")
    }

    /**
     * insert() 함수가 정의되어있지 않은 경우 POST 방식에서 호출
     */
    @Transactional(autoCommit = false)
    def post(data){
        log.info("data._input : $data._input")
    }

    /**
     * PUT 방식에서 기본호출
     */
    @Transactional(autoCommit = false)
    def update(data){
        log.info("data._input : $data._input")
    }

    /**
     * update() 함수가 정의되어있지 않은 경우 PUT 방식에서 호출
     */
    @Transactional(autoCommit = false)
    def put(data){
        log.info("data._input : $data._input")
    }

    /**
     * DELETE 방식에서 기본호출
     */
    @Transactional(autoCommit = false)
    def delete(data){
        log.info("data._input : $data._input")
    }
}