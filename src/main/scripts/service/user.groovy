package service

import common.Validator
import groovy.util.logging.Log
import vanillax.batch.example.common.Constants
import vanillax.batch.example.common.StringUtils
import sql.common.UsersDAO
import vanillax.framework.core.db.Transactional
import vanillax.framework.core.object.Autowired
import vanillax.framework.webmvc.config.ConfigHelper
import vanillax.framework.webmvc.exception.BaseException
import vanillax.framework.webmvc.service.ServiceBase

@Log
class user extends ServiceBase{
    @Autowired
    UsersDAO usersDAO
    @Autowired
    Validator validator

    @Transactional
    def find(data){
        if(data._path == 'info'){
            def user = usersDAO.selectUser([userId:data._input._userId])
            user.remove('passwd')
            user.roles = ['admin']
            user.picture = '/file/fileDownload/1'
            return user
        }else if(data._path == 'many'){
            def param = [:]
            if(data._param.userId){
                param.userId = data._param.userId
            }
            return usersDAO.selectUsers(param)
        }
    }

    @Transactional(autoCommit = false)
    def post(data){
        if(data._path == 'login'){
            validator.hasItem(data._input, ['username','password'])
            def user = usersDAO.selectUser([userId:data._input.username])
            def loginFlag = false
            if(user){
                def passwordHash = StringUtils.hash(Constants.HASH_PREFIX, data._input.password)
                if(user.passwd == passwordHash){
                    //정상적인 ID, 비번인경우
                    //기존 Token은 무효화한다
                    if(data._input._token){
                        usersDAO.updateUsersTokenUpdateAsInvalidate([token:data._input._token])
                    }
                    //오래된 토큰을 삭제한다
                    int tokenTimeout = ConfigHelper.getInt("token.timeout", 300)
                    def param = [userId:user.userId, _userId:user.userId, tokenTimeout:tokenTimeout]
                    usersDAO.deleteUsersTokenOldByUserId(param)
                    //새 토큰을 생성한다.
                    def token = StringUtils.makeToken()
                    param.token = token
                    usersDAO.insertUsersToken(param)
                    //Element UI에서 사용하는 기본인증 데이터 구조로 결과를 반환한다.
                    return [code:200000, data:[token:token]]
                }
            }
            if(!loginFlag){
                throw new BaseException("ERR1101", "로그인 오류","ID가 존재하지 않거나 잘못된 비번입니다",null)
            }
        }else if(data._path == 'logout'){
            usersDAO.updateUsersTokenUpdateAsInvalidate([token:data._input._token])
            return null
        }
    }

}