package com.douzone.jblog.repository;


import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.BlogVo;
import com.douzone.jblog.vo.UserVo;

@Repository
public class BlogRepository {
	
	@Autowired
	private SqlSession sqlSession;

	public void insert(UserVo vo) {
		sqlSession.insert("blog.insert", vo);		
	}

	public BlogVo selectById(String id) {
		return sqlSession.selectOne("blog.selectById", id);
	}

	public void updateInfo(BlogVo vo) {
		sqlSession.update("blog.update", vo);
		
	}

}
