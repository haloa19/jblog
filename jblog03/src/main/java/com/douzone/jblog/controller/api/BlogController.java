package com.douzone.jblog.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.douzone.jblog.dto.JsonResult;
import com.douzone.jblog.service.BlogService;
import com.douzone.jblog.service.CategoryService;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.UserVo;
import com.douzone.security.Auth;
import com.douzone.security.AuthUser;


@RestController("BlogApiController")
@RequestMapping("/{id:(?!assets).*}/api/blog")
public class BlogController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Auth
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public JsonResult categoryList(Model model,
						  @AuthUser UserVo authUser,
						  @PathVariable("id") String id) {
		
		List<CategoryVo> categoryList = categoryService.getCategoryInfo(id);

		return JsonResult.success(categoryList);
	}
	
	@Auth
	@DeleteMapping("/delete/{no}")
	public JsonResult delete(
			CategoryVo categoryVo,
			@PathVariable("no") Long no, 
			@PathVariable("id") String id) {
		
		List<CategoryVo> categoryList = categoryService.getCategoryInfo(id);
		int postCnt = categoryService.getPostCnt(no);
		
		if(categoryList.size() > 1 && postCnt == 0) {
			categoryService.delete(no);
			
		} else {
			JsonResult.fail("No Delete!!");
		}		
		return JsonResult.success(no);
	}
	
	@Auth
	@PostMapping("/add")
	public JsonResult add (
			@RequestBody CategoryVo vo) {	
		categoryService.categoryAdd(vo);	
		return JsonResult.success(vo);
	}

}
