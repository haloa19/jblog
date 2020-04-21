package com.douzone.jblog.controller;

import java.util.List;
import java.util.Locale.Category;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.douzone.jblog.service.BlogService;
import com.douzone.jblog.service.CategoryService;
import com.douzone.jblog.service.PostService;
import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.PostVo;
import com.douzone.jblog.vo.UserVo;
import com.douzone.security.Auth;
import com.douzone.security.AuthUser;


@Controller
@RequestMapping("/{id:(?!assets).*}")
public class BlogController {
	
	@Autowired
	private BlogService blogService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping( {"", "/{pathNo1}", "/{pathNo1}/{pathNo2}" } )
	public String main(Model model,
					   @PathVariable("id") String id,
					   @PathVariable Optional<Long> pathNo1,
					   @PathVariable Optional<Long> pathNo2) {

		Long categoryNo = 0L;
		Long postNo = 0L;
		int chk = -1;
		
		if( pathNo2.isPresent() ) {
			if(pathNo1.get() != 0) {
				chk = 0;
			}
			categoryNo = pathNo1.get();
			postNo = pathNo2.get();			
		} else if( pathNo1.isPresent() ){
			if(pathNo1.get() != 0) {
				chk = 0;
			}
			categoryNo = pathNo1.get();
		}	
		
		BlogVo blogVo = blogService.getBlogInfo(id);
		PostVo postVo = postService.getPostOne(id, categoryNo, postNo);  // 상단 1개나오는 전체 포스트
		List<PostVo> postList = postService.getPostInfo(id, categoryNo, postNo); // 포스트 목록만 나오는 부분
		List<CategoryVo> categoryList = categoryService.getCategoryList(id);  // 우측 카테고리 목록
		
		model.addAttribute("blogVo", blogVo);
		model.addAttribute("postList", postList);
		model.addAttribute("postOne", postVo);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("chk", chk);
		
		return "blog/blog-main";
	}	
	
	@Auth
	@RequestMapping(value="/admin/basic", method=RequestMethod.GET)
	public String basic(Model model,
						@PathVariable("id") String id,
						@AuthUser UserVo authUser) {
		
		if(!id.equals(authUser.getId())){
			return "id:" + id;
		}
		
		BlogVo blogVo = blogService.getBlogInfo(id);
		model.addAttribute("blogVo", blogVo);
		return "blog/blog-admin-basic";
	}
	
	@Auth
	@RequestMapping(value="/admin/basic/update", method=RequestMethod.POST)
	public String update(BlogVo blogVo,
						 @AuthUser UserVo authUser,
						 @PathVariable("id") String id,
						 @RequestParam(value="file") MultipartFile multipartFile) {
		
		if(!id.equals(authUser.getId())){
			return "id:" + id;
		}
		
		if(multipartFile.isEmpty()) {
			BlogVo vo = blogService.getBlogInfo(id);
			blogVo.setLogo((vo.getLogo()));
		} else {
			String url = blogService.restore(multipartFile);
			blogVo.setLogo(url);
		}
		blogService.updateBlogInfo(blogVo);
		
		return "redirect:/{id}/admin/basic";
	}
	
	@Auth
	@RequestMapping(value="/admin/category", method=RequestMethod.GET)
	public String category(Model model,
						  @AuthUser UserVo authUser,
						  @PathVariable("id") String id) {
		
		if(!id.equals(authUser.getId())){
			return "id:" + id;
		}
		
		BlogVo blogVo = blogService.getBlogInfo(id);
		List<CategoryVo> categoryList = categoryService.getCategoryInfo(id);
		
		model.addAttribute("blogVo", blogVo);
		model.addAttribute("categoryList", categoryList);

		return "blog/blog-admin-category";
	}
	
	@Auth
	@RequestMapping(value="/admin/category", method=RequestMethod.POST)
	public String category(Model model,
						   CategoryVo categoryVo,
						   @PathVariable("id") String id) {
		categoryVo.setId(id);
		categoryService.categoryAdd(categoryVo);

		return "redirect:/{id}/admin/category";
	}
	
	@Auth
	@RequestMapping(value="/admin/category/delete/{no}", method=RequestMethod.GET)
	public String category(Model model,
						   @AuthUser UserVo authUser,
						   CategoryVo categoryVo,
						   @PathVariable("no") Long no,
						   @PathVariable("id") String id) {
		
		if(!id.equals(authUser.getId())){
			return "id:" + id;
		}
		
		List<CategoryVo> categoryList = categoryService.getCategoryInfo(id);
		int postCnt = categoryService.getPostCnt(no);
		
		if(categoryList.size() > 1 && postCnt == 0) {
			categoryService.delete(no);
		}		
		
		return "redirect:/{id}/admin/category";
	}
	
	@Auth
	@RequestMapping(value="/admin/write", method=RequestMethod.GET)
	public String write(Model model,
						@AuthUser UserVo authUser,
						@PathVariable("id") String id) {
		
		if(!id.equals(authUser.getId())){
			return "id:" + id;
		}
		
		BlogVo blogVo = blogService.getBlogInfo(id);		
		List<CategoryVo> categoryList = categoryService.getCategoryList(id);		
		
		model.addAttribute("blogVo", blogVo);
		model.addAttribute("id", id);
		model.addAttribute("categoryList", categoryList);

		return "blog/blog-admin-write";
	}
	
	@Auth
	@RequestMapping(value="/admin/write", method=RequestMethod.POST)
	public String write(Model model,
						PostVo postVo,
						@RequestParam(value="category") String name,
					    @PathVariable("id") String id) {
		
		BlogVo blogVo = blogService.getBlogInfo(id);	
		model.addAttribute("blogVo", blogVo);
		
		Long no = categoryService.getNo(name, id);
		postVo.setCategoryNo(no);
		postService.insert(postVo);

		return "redirect:/{id}";
	}
	
	@Auth
	@RequestMapping(value="/admin/category/ajax", method=RequestMethod.GET)
	public String categoryAjax(Model model,
						  @AuthUser UserVo authUser,
						  @PathVariable("id") String id) {
		
		BlogVo blogVo = blogService.getBlogInfo(id);		
		model.addAttribute("blogVo", blogVo);
		
		if(!id.equals(authUser.getId())){
			return "id:" + id;
		}

		return "blog/blog-admin-category-ajax";
	}
	
}
