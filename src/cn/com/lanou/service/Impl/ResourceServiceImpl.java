package cn.com.lanou.service.Impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.com.lanou.dao.ResourcesMapper;
import cn.com.lanou.domain.Resources;
import cn.com.lanou.service.ResourceService;

@Service
public class ResourceServiceImpl implements ResourceService {
	
	@Resource
	private ResourcesMapper resourcesMapper;

	@Override
	public List<Resources> getResourcesByUserName(String username) {
		// TODO Auto-generated method stub
		return resourcesMapper.getResourcesByUserName(username);
	}
}
