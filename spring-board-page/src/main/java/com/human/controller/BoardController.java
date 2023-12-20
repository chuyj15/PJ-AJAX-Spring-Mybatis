package com.human.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.human.domain.Board;
import com.human.domain.Comment;
import com.human.domain.Files;
import com.human.domain.Likes;
import com.human.domain.PageInfo;
import com.human.service.BoardService;
import com.human.service.FileService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
public class BoardController {

	// 게시판 프로젝트
	// Controller
	// - 요청경로 매핑
	// 게시글 목록 - 화면 GET /board/list

	// 게시글 쓰기 - 화면 GET /board/insert
	// 게시글 쓰기 - 처리 POST /board/insert

	// 게시글 읽기 - 화면 GET /board/read

	// 게시글 수정 - 화면 GET /board/update
	// 게시글 수정 - 처리 PUT /board/update

	// 게시글 삭제 - 처리 DELETE /board/delete

	@Autowired
	private BoardService service;

	@Autowired
	private FileService fileService;

	@GetMapping("/")
	public String mainn() {
		return "/board/list";
	}
	

	/**
	 * 게시글 목록 - 화면 GET /board/list
	 * - 전체 게시글 목록 조회
	 * - 게시글 목록을 모델에 등록
	 * - /board/list.html 뷰를 응답
	 * 
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@GetMapping("/api/board/pagination")
	public ResponseEntity<PageInfo> getPageInfo(Board board) {
		PageInfo pageInfo = new PageInfo();

		try {
			pageInfo = service.pageInfo(board);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<PageInfo>(pageInfo, HttpStatus.OK);
	}

	@ResponseBody
	@GetMapping("/api/board/page")
	public ResponseEntity<List<Board>> getPageList(Board board) {

		List<Board> boardList = new ArrayList<>();

		try {
			boardList = service.list(board);
			System.out.println(boardList.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<Board>>(boardList, HttpStatus.OK);
	}

	@GetMapping("/board/list")
	public String boardMain() {

		return "/board/List";
	}

	@GetMapping("/index")
	public String main() {

		return "/index";
	}

	/**
	 * 게시글 쓰기 - 화면 GET /board/insert
	 * - /board/insert.html 뷰를 응답
	 * 
	 * @return
	 */
	@GetMapping("/board/insert")
	public String insertForm() {

		return "/board/insert"; // board/insert.html
	}

	/**
	 * 게시글 쓰기 - 처리 POST /board/insert
	 * - (제목/작성자/내용)을 요청파라미터로 전달 받음
	 * - 게시글을 등록 처리
	 * - 게시글 목록으로 리다이렉트 → /board/list.html 뷰를 응답
	 * 
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/board/insert")
	public String insert(Board board) throws Exception {
		// MultipartFile : 전송된 파일데이터를 다루는 인터페이스

		MultipartFile[] files = board.getFiles();

		if (files != null || files.length == 0) {
			// 요청된 첨부파일 확인
			for (MultipartFile file : files) {
				String fileName = file.getOriginalFilename(); // 파일 명
				String contentType = file.getContentType(); // 파일 타입(확장자)
				long size = file.getSize(); // 용량
				byte[] fileData = file.getBytes(); // 파일 데이터

				log.info("fileName : " + fileName);
				log.info("contentType : " + contentType);
				log.info("size : " + size);
			}
		}

		int result = service.insert(board);

		return "redirect:/board/list"; // 게시글 쓰기 처리 후, 게시글 목록으로 리다이렉트
	}

	@GetMapping("/board/read")
	public String readWarning() {

		return "/board/warning";
	}

	/**
	 * 게시글 읽기 - 화면 GET /board/read
	 * - 게시글 번호를 요청파라미터로 전달 받음
	 * - 게시글 번호로 게시글 정보 조회
	 * - 게시글 정보를 모델에 등록
	 * - /board/read.html 뷰를 응답
	 * 
	 * @param model
	 * @param boardNo
	 * @return
	 * @throws Exception
	 */
	@GetMapping(path = "/board/read", params = "boardNo")
	public String read(Model model, int boardNo, Files file) throws Exception {
		// 게시글 번호로 게시글 정보를 조회
		System.out.println("게시글 조회...");
		System.out.println("boardNo : " + boardNo);

		// 게시글 조회
		Board board = service.read(boardNo);

		// 파일 목록 조회
		file.setParentTable("board");
		file.setParentNo(boardNo);
		List<Files> fileList = fileService.listByParentNo(file);

		model.addAttribute("boardNo", boardNo);
		model.addAttribute("board", board);
		model.addAttribute("fileList", fileList);
		return "/board/read";
	}

	@GetMapping("/board/update")
	public String updateWarning() {

		return "/board/warning";
	}

	/**
	 * 게시글 수정 - 화면 GET /board/update
	 * - 게시글 번호를 요청파라미터로 전달 받음
	 * - 게시글 번호로 게시글 정보 조회
	 * - 게시글 정보를 모델에 등록
	 * - /board/update.html 뷰를 응답
	 * 
	 * @param model
	 * @param boardNo
	 * @return
	 * @throws Exception
	 */
	@GetMapping(path = "/board/update", params = "boardNo")
	public String updateForm(Model model, int boardNo) throws Exception {
		// 게시글 번호로 게시글 정보를 조회
		System.out.println("게시글 조회(수정화면)...");
		System.out.println("boardNo : " + boardNo);

		Board board = service.read(boardNo);

		model.addAttribute("boardNo", boardNo);
		model.addAttribute("board", board);
		return "/board/update";
	}

	/**
	 * 게시글 수정 - 처리 PUT /board/update
	 * - 수정할 (게시글번호/제목/작성자/내용) 요청파라미터로 전달 받음
	 * - 게시글 번호를 조건으로 게시글 정보 수정 처리
	 * - 게시글 읽기로 리다이렉트 → /board/read.html 응답
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PutMapping("/board/update")
	public String update(HttpServletRequest request) throws Exception {
		// HttpServletRequest
		// - 클라이언트의 요청과 관련한 기능을 사용할 수 있는 요청객체
		// - getParameter("파라미터명") : 요청 파라미터의 값을 가져온다
		// * 파라미터명
		// - form요청 (input 태그의 name 속성 지정한 파라미터명)
		// - URL의 쿼리스트링의 파라미터 localhost:8080/~~~?title=제목&writer=작성자&content=내용
		String title = request.getParameter("title");
		String writer = request.getParameter("writer");
		String content = request.getParameter("content");
		int boardNo = Integer.parseInt(request.getParameter("boardNo"));

		Board board = new Board();
		board.setBoardNo(boardNo);
		board.setTitle(title);
		board.setWriter(writer);
		board.setContent(content);

		int result = service.update(board);

		System.out.println("title : " + title);
		System.out.println("writer : " + writer);
		System.out.println("content : " + content);
		System.out.println("boardNo : " + boardNo);
		return "redirect:/board/read?boardNo=" + boardNo;
	}

	/**
	 * 게시글 수정 - 처리 POST /board/update
	 * - 수정할 (게시글번호/제목/작성자/내용) 요청파라미터로 전달 받음
	 * - 게시글 번호를 조건으로 게시글 정보 수정 처리
	 * - 게시글 읽기로 리다이렉트 → /board/read.html 응답
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/board/update")
	public String updatePost(Board board) throws Exception {
		// 요청 파라미터명과 일치하는 변수명을 가지고 있는 객체를 사용하여 여러 파라미터를 가져올 수 있다.
		int boardNo = board.getBoardNo();
		String title = board.getTitle();
		String writer = board.getWriter();
		String content = board.getContent();

		int result = service.update(board);

		System.out.println("title : " + title);
		System.out.println("writer : " + writer);
		System.out.println("content : " + content);
		System.out.println("boardNo : " + boardNo);
		return "redirect:/board/read?boardNo=" + boardNo;
	}

	/**
	 * 게시글 삭제 - 처리 DELETE /board/delete
	 * - 게시글 번호를 요청파라미터로 전달 받음
	 * - 게시글 번호를 조건으로 게시글 정보를 삭제 처리
	 * - 게시글 목록으로 리다이렉트 → /board/list.html 응답
	 * 
	 * @param boardNo
	 * @return
	 * @throws Exception
	 */
	@DeleteMapping("/board/delete")
	public String delete(int boardNo) throws Exception {
		// 게시글 번호로 게시글 삭제
		System.out.println("boardNo : " + boardNo);

		int result = service.delete(boardNo);

		return "redirect:/board/list";
	}

	/**
	 * 게시글 삭제 - 처리 POST /board/delete
	 * - 게시글 번호를 요청파라미터로 전달 받음
	 * - 게시글 번호를 조건으로 게시글 정보를 삭제 처리
	 * - 게시글 목록으로 리다이렉트 → /board/list.html 응답
	 * 
	 * @param boardNo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/board/delete")
	public String deletePost(int boardNo) throws Exception {
		// 게시글 번호로 게시글 삭제
		System.out.println("boardNo : " + boardNo);

		int result = service.delete(boardNo);

		return "redirect:/board/list";
	}

	@ResponseBody
	@GetMapping("/api/comment")
	public ResponseEntity<List<Comment>> getCommentList(Comment comment) {
		System.out.println("댓글목록 요청데이터 : " + comment);
		List<Comment> commentList = new ArrayList<Comment>();

		try {
			commentList = service.commentList(comment);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<List<Comment>>(commentList, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping("/api/comment")
	public String insertComment(Comment comment) {

		try {
			int result = service.commentInsert(comment);
			if (result > 0) {
				return "SUCCESS";
			} else {
				return "FAILED";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILED";
		}
	}

	@ResponseBody
	@PutMapping("/api/comment")
	public String updateComment(Comment comment) {

		try {
			int result = service.commentUpdate(comment);
			if (result > 0) {
				return "SUCCESS";
			} else {
				return "FAILED";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILED";
		}
	}

	@ResponseBody
	@DeleteMapping("/api/comment")
	public String deleteComment(Comment comment) {

		try {
			int result = service.commentDelete(comment);
			if (result > 0) {
				return "SUCCESS";
			} else {
				return "FAILED";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILED";
		}
	}

	@ResponseBody
	@GetMapping("/api/board")
	public ResponseEntity<Board> getBoard(int boardNo) {

		Board board = new Board();
		try {
			board = service.read(boardNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<Board>(board, HttpStatus.OK);
	}

	@ResponseBody
	@PostMapping("/api/likeEvent")
	public String likeEvent(Likes likes) {

		int result = 0;

		try {

			result = service.likeEvent(likes);

			if (result == 1) {
				return "UP";
			} else if (result == 0) {
				return "FAILED";

			} else if (result == -1) {
				return "DOWN";

			} else {
				return "FAILED";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILED";
		}

	}

	@ResponseBody
	@PostMapping("/api/board")
	public String insertBoard(Board board) {

		int result = 0;
		try {
			result = service.insert(board);
			if (result > 0) {
				return "SUCCESS";
			} else {
				return "FAILED";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILED";
		}
	}

	@ResponseBody
	@PutMapping("/api/board")
	public String updateBoard(Board board) {

		System.out.println("게시글 수정 요청데이터 : "+board);
		int result = 0;
		try {
			result = service.update(board);
			if (result > 0) {
				return "SUCCESS";
			} else {
				return "FAILED";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILED";
		}
	}

	@ResponseBody
	@DeleteMapping("/api/board")
	public String deleteBoard(Board board) {

		System.out.println("게시글 삭제 요청데이터 : "+board);
		int result = 0;
		try {
			result = service.delete(board.getBoardNo());
			if (result > 0) {
				return "SUCCESS";
			} else {
				return "FAILED";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "FAILED";
		}
	}


}