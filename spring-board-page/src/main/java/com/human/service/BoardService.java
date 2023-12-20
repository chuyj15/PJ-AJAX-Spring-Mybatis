package com.human.service;

import java.util.List;

import com.human.domain.Board;
import com.human.domain.Comment;
import com.human.domain.Likes;
import com.human.domain.Page;
import com.human.domain.PageInfo;

public interface BoardService {

	
	// 게시글 조회
	public Board read(int boardNo) throws Exception;
	
	// 게시글 등록
	public int insert(Board board) throws Exception;
	
	// 게시글 수정
	public int update(Board board) throws Exception;
	
	// 게시글 삭제
	public int delete(int boardNo) throws Exception;

	// 페이징 처리
	public List<Board> list(Board board) throws Exception;

	public PageInfo pageInfo(Board board) throws Exception;
	
	public List<Comment> commentList (Comment comment) throws Exception;
	
	public int commentInsert (Comment comment) throws Exception;
	
	public int commentUpdate (Comment comment) throws Exception;
	
	public int commentDelete (Comment comment) throws Exception;
	
	public int likeEvent (Likes likes) throws Exception;

}



