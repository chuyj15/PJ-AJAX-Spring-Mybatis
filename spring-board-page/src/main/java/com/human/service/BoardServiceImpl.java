package com.human.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.human.domain.Board;
import com.human.domain.Comment;
import com.human.domain.Files;
import com.human.domain.Likes;
import com.human.domain.Page;
import com.human.domain.PageInfo;
import com.human.mapper.BoardMapper;
import com.human.mapper.FileMapper;
import com.human.utils.FileUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service // 컨테이너의 빈으로 등록, 비즈니스 로직을 처리하는 서비스로 구분
public class BoardServiceImpl implements BoardService {

	// 업로드 경로
	@Value("${upload.path}") // application.properties 의 속성값을 지정
	private String uploadPath;

	@Autowired
	private BoardMapper mapper;


	@Autowired
	private FileUtil fileUtil;

	@Override
	public Board read(int boardNo) throws Exception {

		Board board = mapper.read(boardNo);
		Likes likes = new Likes();
		likes.setParentTable("board");
		likes.setParentNo(boardNo);
		board.setLikes(mapper.likes(likes));

		return board;
	}

	@Override
	public int insert(Board board) throws Exception {

		int result = mapper.insert(board);

		return result;
	}

	@Override
	public int update(Board board) throws Exception {

		return mapper.update(board);
	}

	@Override
	public int delete(int boardNo) throws Exception {

		return mapper.delete(boardNo);
	}

	@Override
	public List<Board> list(Board board) throws Exception {

		board.setPageNo((board.getPageNo() - 1) * board.getRows());

		List<Board> boardList = mapper.pageList(board);

		return boardList;
	}

	@Override
	public PageInfo pageInfo(Board board) throws Exception {

		board.setTotalCount(mapper.count());
		PageInfo pageInfo = mapper.pageInfo(board);

		return pageInfo;
	}

	@Override
	public List<Comment> commentList(Comment comment) throws Exception {

		comment.setParentTable("board");
		List<Comment> commentList =  mapper.commentList(comment);


		return commentList;
	}

	@Override
	public int commentInsert(Comment comment) throws Exception {
		comment.setParentTable("board");

		int result = 0;

		result = mapper.commentInsert(comment);

		return result;
	}

	@Override
	public int commentUpdate(Comment comment) throws Exception {

		int result = 0;

		result = mapper.commentUpdate(comment);


		return result;
	}

	@Override
	public int commentDelete(Comment comment) throws Exception {

		int result = 0;

		result = mapper.commentDelete(comment);

		return result;
	}

	@Override
	public int likeEvent(Likes likes) throws Exception {

		int isLikeResult = 0;
		isLikeResult = mapper.isLike(likes);

		if(isLikeResult>0){
			mapper.likeDown(likes);
			return -1;
		}else{
			mapper.likeUp(likes);
			return 1;
		}


	}

}
