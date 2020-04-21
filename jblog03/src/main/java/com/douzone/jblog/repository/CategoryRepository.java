package com.douzone.jblog.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.douzone.jblog.vo.CategoryVo;
import com.douzone.jblog.vo.UserVo;

@Repository
public class CategoryRepository {
	
	@Autowired
	private SqlSession sqlSession;

	public void insert(UserVo vo) {
		sqlSession.insert("category.insert", vo);
	}

	public List<CategoryVo> selectById(String id) {		
		return sqlSession.selectList("category.selectById", id);
	}

	public List<CategoryVo> selectByIdAll(String id) {
		return sqlSession.selectList("category.selectByIdAll", id);
	}

	public void categoryAdd(CategoryVo categoryVo) {
		sqlSession.insert("category.categoryAdd", categoryVo);
		
	}

	public void delete(Long no) {
		sqlSession.delete("category.delete", no);		
	}

	public Long selectNo(String name, String id) {
		Map<String, Object> map = new HashMap<>();
		map.put("name", name);
		map.put("id", id);
		return sqlSession.selectOne("category.selectNo", map);
	}

	public int selectPostCnt(Long no) {
		return sqlSession.selectOne("category.selectPostCnt", no);
	}

}
