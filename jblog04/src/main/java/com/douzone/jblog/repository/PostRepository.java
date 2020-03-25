package com.douzone.jblog.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.PostVo;


@Repository
public class PostRepository {
	
	@Autowired
	SqlSession sqlSession;

	public List<PostVo> findAll(String id, Long categoryNo, Long postNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("categoryNo", categoryNo);
		map.put("postNo", postNo);
		List<PostVo> list = sqlSession.selectList("post.findAll", map);
		return list;
	}

	public PostVo findById(String id, Long categoryNo, Long postNo) {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("categoryNo", categoryNo);
		map.put("postNo", postNo);
		PostVo vo = sqlSession.selectOne("post.findById", map);
		return vo;
	}

	public void insert(PostVo postVo) {
		sqlSession.insert("post.insert", postVo);
		
	}

}
