package com.godEShop.Interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.godEShop.Service.CategoryService;
import com.godEShop.Service.SubCategoryService;

@Component
public class GlobalInterceptor implements HandlerInterceptor {
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    SubCategoryService subCategoryService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
	    ModelAndView modelAndView) throws Exception {
	// TODO Auto-generated method stub
	request.setAttribute("lstCategory", categoryService.findAll());
	request.setAttribute("lstSubCategory", subCategoryService.findAll());
	if(request.getRequestURL().toString().contains("/index")) {
	    request.setAttribute("isHomePage", true);
	}else {
	    request.setAttribute("isHomePage", false);
	}
    }
}
