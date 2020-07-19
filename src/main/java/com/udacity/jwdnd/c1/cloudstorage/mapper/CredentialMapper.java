package com.udacity.jwdnd.c1.cloudstorage.mapper;

import com.udacity.jwdnd.c1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid)" +
            "VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    public int insert(Credential credential);

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    public List<Credential> getAllCredentials(Integer userId);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    public void deleteCredential(Integer credentialId);


}
