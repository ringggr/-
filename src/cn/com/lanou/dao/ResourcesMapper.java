package cn.com.lanou.dao;

import cn.com.lanou.domain.Resources;
import cn.com.lanou.domain.ResourcesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ResourcesMapper {
    
	List<Resources> getResourcesByUserName(String username);
}