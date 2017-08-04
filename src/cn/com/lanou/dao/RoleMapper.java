package cn.com.lanou.dao;

import cn.com.lanou.domain.Role;
import cn.com.lanou.domain.RoleExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {

	List<Role> getAll();
}