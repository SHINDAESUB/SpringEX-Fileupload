package org.daesub.controller;

import org.daesub.domain.Criteria;
import org.daesub.domain.ReplyVO;
import org.daesub.service.ReplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.daesub.domain.ReplyPageDTO;

@RestController
@RequestMapping(value = "/reply/")
@Log4j
@AllArgsConstructor
public class ReplyController {

	private ReplyService service;
	 //@PostMapping / 생성 할 경우 
	 //consumes 속성을 이용하면 @RequestBody 담는  타입을 제한 할 수 있다. 현재는 json 타입만 받아올수 있다.
	 //produces 데이터 타입을 반환하는 값 지정 , 현재는 문자열을 반환하도록 
	@PostMapping(value = "/new", consumes = "application/json" , produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> create(@RequestBody ReplyVO vo){
		
		log.info("ReplyVO: " + vo);
		
		int insertCount = service.register(vo); //정상적으로 들어오면 '1'
		
		log.info("Reply insert count  :" + insertCount );
		
		//chrome-extension://aejoelaoggembcahagimdiliamlcdmfm/index.html 에서  http://localhost:8080/reply/new  /{"bno":323,"reply":"POST 테스트 입니다","replyer":"신대섭"}
		// 데이터가 정상적으로 들어오면 "sucess" 라는 메시지를 보낸다.
		return insertCount ==1 ? new ResponseEntity<>("sucess", HttpStatus.OK) : new ResponseEntity<> (HttpStatus.INTERNAL_SERVER_ERROR);
	}
	//리스트 조회
	//test http://localhost:8080/reply/pages/323/1 
	//xml 형식으로  JSON_UTF8 값으로 반환 
//	@GetMapping(value="/pages/{bno}/{page}" ,produces= {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public ResponseEntity<List<ReplyVO>> getList(@PathVariable("page")int page ,@PathVariable("bno") Long bno){
//		
//		log.info("getList....");
//		
//		Criteria cri =new Criteria(page,10);
//		
//		log.info(cri);
//		
//		return new ResponseEntity<>(service.getList(cri, bno),HttpStatus.OK);
//	}
	
	//리스트 조회
	//test http://localhost:8080/reply/pages/323/1 
	//xml 형식으로  JSON_UTF8 값으로 반환 
	@GetMapping(value = "/pages/{bno}/{page}", 
			produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_UTF8_VALUE })
	public ResponseEntity<ReplyPageDTO> getList(@PathVariable("page") int page, @PathVariable("bno") Long bno) {

		Criteria cri = new Criteria(page, 10);
		
		log.info("get Reply List bno: " + bno);

		log.info("cri:" + cri);

		return new ResponseEntity<>(service.getListPage(cri, bno), HttpStatus.OK);
	}
	
	
	
	//일반 조회
	//xml 형식으로  JSON_UTF8 값으로 반환 
	@GetMapping(value="/{rno}",produces = {MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<ReplyVO> get(@PathVariable("rno") Long rno){
		
		log.info("get....");
		
		return new ResponseEntity<>(service.get(rno),HttpStatus.OK);
	}
	
	//삭제
	@DeleteMapping(value="/{rno}",produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String> remove(@PathVariable("rno")Long rno){
		
		log.info("remove....");
		
		return service.remove(rno)==1 ? new ResponseEntity<>("삭제 완료", HttpStatus.OK) : new ResponseEntity<> (HttpStatus.INTERNAL_SERVER_ERROR);
				
	}
	
	//수정
	@RequestMapping(method = {RequestMethod.PATCH,RequestMethod.PUT}, value="/{rno}",consumes = "application/json" , produces = {MediaType.TEXT_PLAIN_VALUE})
	public ResponseEntity<String>modify(@RequestBody ReplyVO vo, @PathVariable("rno") Long rno){
		
		vo.setRno(rno);
		
		log.info("rno :" + rno);
		
		log.info("modify: " + vo);
		
		return service.modify(vo) ==1 ? new ResponseEntity<>("수정완료", HttpStatus.OK) : new ResponseEntity<> (HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
}
