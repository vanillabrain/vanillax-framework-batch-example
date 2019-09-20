package sql.common

import vanillax.framework.core.db.orm.Delete
import vanillax.framework.core.db.orm.Insert
import vanillax.framework.core.db.orm.Repository
import vanillax.framework.core.db.orm.Select
import vanillax.framework.core.db.orm.Update
import vanillax.framework.core.db.script.Velocity

@Repository
interface UsersDAO {

    @Velocity
    @Select('''
        SELECT
              userId
            , userName
            , passwd
            , email
            , permissionLevel
            , regUser
            , regDate
            , modUser
            , modDate 
        FROM Users 
        WHERE 1=1
        #if($userId) 
            AND userId = :userId 
        #end
    ''')
    List selectUsers(Map x)

    @Select('''
        SELECT
              userId
            , userName
            , passwd
            , email
            , permissionLevel
            , regUser
            , regDate
            , modUser
            , modDate 
        FROM Users
        WHERE userId = :userId
    ''')
    Map selectUser(Map x)

    @Select('''
        SELECT
            A.userId, A.userName, A.passwd, A.email, A.permissionLevel, B.token, B.tokenUpdateDate,
            TIMESTAMPDIFF(MINUTE, coalesce( B.tokenUpdateDate, '20010101010101'), NOW()) as  tokenUpdateDateDiff,
            CASE
                WHEN TIMESTAMPDIFF(MINUTE, coalesce( B.tokenUpdateDate, '20010101010101'), NOW()) < coalesce(:tokenTimeout, 300) THEN 'Y'
                ELSE 'N' 
            END as tokenValidYn, 
            B.regUser, B.regDate, B.modUser, B.modDate
        FROM Users A, UsersToken B
        WHERE A.userId = B.userId
          AND B.token = coalesce(:token,'AAA')
    ''')
    Map selectUserByToken(Map x)

    @Velocity
    @Update('''
        UPDATE Users
           SET
                #if($token)
                    token = :token,
                #end
                #if($tokenUpdateDate)
                    tokenUpdateDate = :tokenUpdateDate
                #else
                    tokenUpdateDate = fn_curr_date14()
                #end
        WHERE userId = :userId
    ''')
    int updateUserToken(Map x)

    @Update('''
        UPDATE UsersToken
           SET
                modUser = :_userId,
                modDate = fn_curr_date14(), 
                tokenUpdateDate = fn_curr_date14()
        WHERE token = :token
    ''')
    int updateUsersTokenUpdateDateByToken(Map x)

    @Insert('''
        INSERT INTO UsersToken(userId, token, tokenUpdateDate, regUser, regDate, modUser, modDate) 
        VALUES(:userId, :token, fn_curr_date14(), :_userId, fn_curr_date14(), :_userId, fn_curr_date14())
    ''')
    def insertUsersToken(Map x)

    @Delete('''
        DELETE 
          FROM UsersToken
         WHERE userId = :userId
           AND TIMESTAMPDIFF(MINUTE, tokenUpdateDate, NOW()) > coalesce(:tokenTimeout, 300)
    ''')
    boolean deleteUsersTokenOldByUserId(Map x)

    @Update('''
        UPDATE UsersToken
           SET
                modUser = :_userId,
                modDate = fn_curr_date14(), 
                tokenUpdateDate = '20010101010101'
        WHERE token = :token
    ''')
    int updateUsersTokenUpdateAsInvalidate(Map x)

}