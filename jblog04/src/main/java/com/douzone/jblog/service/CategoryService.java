package com.douzone.jblog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.CategoryRepository;
import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.UserVo;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;

	public void insert(UserVo vo) {
		categoryRepository.insert(vo);
		
	}

	public List<CategoryVo> getCategoryList(String id) {
		
		return categoryRepository.selectById(id);
	}

	public List<CategoryVo> getCategoryInfo(String id) {
		
		return categoryRepository.selectByIdAll(id);
	}

	public void categoryAdd(CategoryVo categoryVo) {
		categoryRepository.categoryAdd(categoryVo);
				
	}

	public void delete(Long no) {
		categoryRepository.delete(no);
		
	}

	public Long getNo(String name, String id) {
		
		return categoryRepository.selectNo(name, id);
	}

	public int getPostCnt(Long no) {
		
		return categoryRepository.selectPostCnt(no);
	}

}
