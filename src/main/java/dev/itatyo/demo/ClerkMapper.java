package dev.itatyo.demo;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ClerkMapper {
    @Select("select * from clerk where id = #{id}")
    Clerk findByID(int id);
}
