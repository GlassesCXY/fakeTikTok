package com.qiu.apifinal.mapper;



import com.qiu.apifinal.entity.Account;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface AccountMapper {
    @Insert("INSERT INTO account(username, email, password) VALUES(#{username}, #{email}, #{password})")
    @Options(useGeneratedKeys = true, keyProperty = "uid")
    int insert(Account account);

    @Select("SELECT * FROM account WHERE username = #{username}")
    Account select(int username);

    @Update("UPDATE account SET username = #{username}, email = #{email}, password = #{password} WHERE uid = #{uid}")
    void update(Account account);

    @Select("SELECT * FROM account WHERE username = #{username} AND password = #{password}")
    Account selectByUsernameAndPassword(@Param("username") String username, @Param("password")
    String password);

    @Delete("DELETE FROM account WHERE uid = #{uid}")
    void delete(int uid);


    @Select("SELECT * FROM account WHERE username = #{username}")
    Account selectByUsername(String username);

    @Select("SELECT * FROM account WHERE email = #{email}")
    List<Account> selectByEmail(String email);

    @Select("SELECT * FROM account WHERE username = #{username} AND email = #{email}")
    Account selectByUsernameAndEmail(@Param("username") String username, @Param("email") String email);
}