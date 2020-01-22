package community.tina.demo.mapper;

import community.tina.demo.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
/**
 * 数据库MySQL的操作接口
 * **/
@Repository
@Mapper
public interface UserMapper {
    //插入user用户实际信息
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
    //通过token的值（这里是cookie传入的token值），查找user表中的用户信息，
    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);
}
