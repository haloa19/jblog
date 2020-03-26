package com.douzone.jblog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.douzone.jblog.repository.PostRepository;
import com.douzone.jblog.vo.PostVo;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;
	
	public List<PostVo> getPostInfo(String id, Long categoryNo, Long postNo) {
		List<PostVo> list = postRepository.findAll(id, categoryNo, postNo);
		return list;
	}

	public PostVo getPostOne(String id, Long categoryNo, Long postNo) {
		PostVo vo = postRepository.findById(id, categoryNo, postNo);
		return vo;
	}

	public void insert(PostVo postVo) {
		postRepository.insert(postVo);
		
	}

}
