package cn.com.lanou.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class VerifyFilter implements Filter{

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		Map user = (Map) req.getSession().getAttribute("userSession");
		String[] strs = {"login.do", "loginCheck.do", "list.do"};
		
		
		boolean isExist = false;
		for (String str :strs) {
			if (req.getRequestURI().indexOf(str) > -1) {
				isExist = true;
				break;
			}
		}
		//过滤不需要过滤的链接
		if(!isExist) {
			//判断是否登录，没有登录返回登录页
			if (user == null){
				res.sendRedirect(req.getContextPath() + "/background/login.do");
			}
		}
		
		
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}

}
