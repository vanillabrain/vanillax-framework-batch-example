package sql

import vanillax.framework.core.db.orm.Delete
import vanillax.framework.core.db.orm.Insert
import vanillax.framework.core.db.orm.Repository
import vanillax.framework.core.db.orm.Select
import vanillax.framework.core.db.orm.Update
import vanillax.framework.core.db.script.Velocity

@Repository
interface SampleDAO {

    /*
    * IF 예제
    * 결과)
    *   AND a = 'haha'
    *   B_STRING --사용자가 입력한 문자가 그대로 치환됨
    * */
    @Velocity //Velocity 문법처리
    @Select('''
        SELECT a,b,c, '하하하' as d
        FROM Student
        WHERE 1=1
        #if($a) AND a = :a #end
        #if($b) $b #end
        LIMIT 0,1
        ''')
    List selectStudentList(Map x)// IN () 예제

    /*
    * IN() 예제
    * 결과)
    *   AND IN my_name ('gaga', 'haha', '고고')
    * */
    @Velocity //Velocity 문법처리
    @Select('''
        SELECT a,b,c, '하하하' as d
        FROM Student
        WHERE 1=1
        #if($a) AND a = :a #end
        #in($names $it "my_name") '$it.l' #end
        #if($b) $b #end
        LIMIT 0,1
        ''')
    List selectList(Map x)// IN () 예제

    /*
    * NOT IN() 예제
    * 결과)
    *   AND NOT IN my_name ('gaga', 'haha', '고고')
    * */
    @Velocity //Velocity 문법처리
    @Select('''
        SELECT a,b,c, '하하하' as d
        FROM Student
        WHERE 1=1
        #not_in($names $it "my_name") '$it.name' #end
        LIMIT 0,1
        ''')
    List selectListNotIn(Map x)// NOT IN() 예제

    @Select('''
        SELECT a,b,c
        FROM Student
        WHERE 1=1
        AND a = :a
        ''')
    Map selectOne(Map x) // 단건조회

    @Insert('''
        INSERT INTO Student
        (a,b,c) VALUES (:a, :b, :c)
        ''')
    def insertOne(Map x) //단건입력, 자동생성 PK가 있을 경우 그 값을 반환한다.

    @Insert('''
        INSERT INTO Student
        (a,b,c) VALUES (:a, :b, :c)
        ''')
    List insertList(List list) //다건입력, 자동생성 PK가 있을 경우 그 값을 List에 담아 반환한다.

    @Update(''' 
        UPDATE Student 
        SET 
            studentName = :studentName, 
            email = :email, 
            userPhoto = :userPhoto, 
            providerType = :providerType, 
            visitCnt = :visitCnt, 
            modDate = NOW()
        WHERE id = :id
    ''')
    int updateOne(Map x) // 단건 갱신. 변경된 건수가 반환된다.

    @Update(''' 
        UPDATE Student  
        SET 
            studentName = :studentName, 
            email = :email, 
            userPhoto = :userPhoto, 
            providerType = :providerType, 
            visitCnt = :visitCnt, 
            modDate = NOW()
        WHERE id = :id
    ''')
    List updateList(List list)// 다건 갱신. 각 SQL의 실행에 따른 변경 건수가 List에 담아 반환된다.

    @Delete(''' 
        DELETE FROM Student 
        WHERE id = :id
    ''')
    boolean deleteOne(Map x)// 단건 삭제. 반환값은 의미가 없다.

    @Delete(''' 
        DELETE FROM zStudent 
        WHERE id = :id
    ''')
    List deleteList(List list)//다건 삭제. 반환값은 의미가 없다.

}