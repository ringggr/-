package cn.com.lanou.service;

import java.util.List;

import cn.com.lanou.domain.Resources;

public interface ResourceService {

	List<Resources> getResourcesByUserName(String username);

}
