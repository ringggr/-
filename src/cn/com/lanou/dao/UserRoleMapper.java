package cn.com.lanou.dao;

import cn.com.lanou.domain.UserRoleExample;
import cn.com.lanou.domain.UserRoleKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserRoleMapper {

	void addUserRoleKey(UserRoleKey userRoleKey);

	void deleteUserRoleKey(UserRoleKey userRole);

	void deleteAllByUserId(int[] userids);
    
}