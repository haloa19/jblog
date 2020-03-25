package com.douzone.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.douzone.jblog.vo.UserVo;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Object handler) throws Exception {

		// 1. handler 종류 확인
		if(handler instanceof HandlerMethod == false) {
			// DefaultServletHandler가 처리하는 경우 (보통, assets의 정적 자원 접근)
			return true;
		}
		
		// 2. casting (목적)
		HandlerMethod handlerMethod = (HandlerMethod)handler;
		
		// 3. Method의 @Auth 받아오기
		Auth auth = handlerMethod.getMethodAnnotation(Auth.class);
		
		// 4. Method에 @Auth가 없으면...Type(class)에 붙어 있는지 확인
		if(auth == null) {
			auth = handlerMethod.getMethod().getDeclaringClass().getAnnotation(Auth.class);
		}
		
		// 5. Type이나 Method 둘 다 @Auth가 적용이 안되어 있는 경우,
		if(auth == null) {
			return true;
		}
		
		// 6. @Auth가 붙어있기 때문에 Authentification여뷰 학인
		HttpSession session = request.getSession();
		if(session == null) {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}
		
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			response.sendRedirect(request.getContextPath() + "/main");
			return false;
		}
		
		
		// 7. 권한(Authorization) 체크를 위해서 @Auth의 ID값, URL 가져오기		
		String role = authUser.getId();
		String url = request.getRequestURI();
		String[] url2 = url.split("/");
		
		
		if(url2[2].equals(role)) {
			return true;
		} else {
			response.sendRedirect(request.getContextPath() + "/user/login");
			return false;
		}

	}

}
