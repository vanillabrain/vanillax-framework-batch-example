package filter

import groovy.util.logging.Log
import sql.common.UsersDAO
import vanillax.framework.core.db.Transactional
import vanillax.framework.core.object.Autowired
import vanillax.framework.webmvc.config.ConfigHelper
import vanillax.framework.webmvc.exception.BaseException
import vanillax.framework.webmvc.service.FilterBase

import javax.servlet.http.HttpServletRequest

/**
 * 인증정보 필터
 */
@Log
class LoginFilter extends FilterBase {
    @Autowired
    UsersDAO usersDAO
    private def ignoreSessionUrlList = ['/user/login', '/user/logout', '/fileDownload', '/fileUpload','/sample','/admin/scheduleLog','/admin/worker']

    @Override
    Map<String, Object> preprocess(Map<String, Object> param) throws Exception {
        HttpServletRequest request = (HttpServletRequest)param.get("_request")
        if(request != null){
            def ignoreUrl = ignoreUrl(request.getPathInfo())
            def userinfo = null;
            def token = request.getHeader("X-Token")
            if(ignoreUrl){
                //Do Nothing
            }else if(token == 'developer-user1-token001'){
                //개발모드일경우 정해진 사용자를 강제로 입력한다.
                userinfo = [userId:'user1', email:'hong@mycompany.com', userName:'홍길동']
            }else{
                int tokenTimeout = ConfigHelper.getInt("token.timeout", 300)
                def one = selectUserByToken([token:token, tokenTimeout:tokenTimeout])
                if(one){
                    userinfo = one
                    if(one.tokenValidYn == 'Y' && one.token && one.token == token){
                        //OK. 정상적인 세션인경우
                        updateUsersTokenUpdateDateByToken([token:token, _userId:userinfo.userId]) // 토큰갱신시간을 변경해준다
                    }else{
                        throw new BaseException("ERR1100","로그인 오류","유효하지않은 토큰입니다",null)
                    }
                }else{
                    throw new BaseException("ERR1100","로그인 오류","유효하지않은 토큰입니다",null)
                }
            }
            //입력데이터에 사용자정보를 추가해준다. SQL에서 사용하기 위함이다
            if(userinfo){
                if(param._input instanceof Map){
                    param._input['_userId'] = userinfo.userId
                    param._input['_token'] = token
                }else if(param._input instanceof List){
                    param._input.each {it ->
                        if(it instanceof Map){
                            it._userId  = userinfo.userId
                        }
                    }
                }
            }
        }
        return param
    }

    @Transactional
    def selectUserByToken(param){
        return usersDAO.selectUserByToken(param)
    }

    @Transactional
    def updateUsersTokenUpdateDateByToken(param){
        usersDAO.updateUsersTokenUpdateDateByToken(param) // 토큰갱신시간을 변경해준다
    }

    private boolean ignoreUrl(path){
        def result = false
        ignoreSessionUrlList.find { it ->
            if(path.startsWith(it)){
                result = true
                return true
            }
        }
        return result
    }

}