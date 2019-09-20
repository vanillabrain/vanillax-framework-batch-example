package sql.common

import vanillax.framework.core.db.orm.Repository
import vanillax.framework.core.db.orm.Select
import vanillax.framework.core.db.script.Velocity

@Repository
interface CommonCodeDAO {
    @Velocity
    @Select('''
    #if($allYn)
        SELECT
            'ELEMENT_REL_TYPE' as codeGroup
            , '' as codeGroupDescription
            , 'ALL' as codeDetail
            , '전체' as codeName
            , 0 as sort
            , null as regUser
            , null as regDate
            , null as modUser
            , null as modDate 
        
        UNION ALL
    #end
        SELECT
              A.codeGroup
            , B.description as codeGroupDescription
            , A.codeDetail
            , A.codeName
            , A.sort
            , A.regUser
            , A.regDate
            , A.modUser
            , A.modDate 
        FROM CommonCodeDetail A, CommonCode B
        WHERE A.codeGroup = :codeGroup
          AND A.codeGroup = B.codeGroup
        ORDER BY sort
    ''')
    List selectCommonCodeList(Map x)

}