package cn.com.lanou.service;

import java.util.Map;

import cn.com.lanou.domain.PageView;
import cn.com.lanou.domain.User;

public interface UserService {

	Map getUserByUserName(String username);

	PageView getUserByPageNow(PageView pageView, User user);

	void updateLastLogintime(String username);

	void deleteUser(String userId);

	void update(User user);

	void addUser(User user);

	void deleteAll(int[] userids);



}
