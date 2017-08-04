package cn.com.lanou.dao;

import java.util.List;
import java.util.Map;

import cn.com.lanou.domain.User;

public interface UserMapper {

	Map getUserByUserName(String username);

	long getRowCount(User user);

	List getUsersByPage(Map map);

	void updateLastLogintime(String username);

	List getUsersPage(Map map);

	void deleteUser(int userId);

	void update(User user);

	void addUser(User user);

	void deleteAll(int[] userids);

	
	
}