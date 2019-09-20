package sql.common

import vanillax.framework.core.db.orm.Insert
import vanillax.framework.core.db.orm.Repository
import vanillax.framework.core.db.orm.Select
import vanillax.framework.core.db.script.Velocity

@Repository
interface FileDAO {

    @Select('''
        SELECT
              id
            , fileName
            , fileExt
            , filePath
            , regUser
            , regDate
            , modUser
            , modDate 
        FROM File
        WHERE id = :id
    ''')
    Map selectFile(Map x)

    @Insert('''
        INSERT INTO File(fileName, fileExt, filePath, regUser, regDate, modUser, modDate) 
        VALUES( :fileName, :fileExt, :filePath, :_userId, fn_curr_date14(), :_userId, fn_curr_date14())
    ''')
    def insertFile(Map x)


}