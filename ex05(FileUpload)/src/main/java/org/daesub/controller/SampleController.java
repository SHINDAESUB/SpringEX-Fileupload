package org.daesub.controller;

import org.daesub.domain.SampleVO;
import org.daesub.domain.Ticket;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j;

@RestController  // JSP 와 달리 순수한 데이터를 반환하는 형태 이므로 다양한 형태의 데이터를 전송 가능 하다.  JSON 이나 XML 주로 사용
@RequestMapping("/sample")
@Log4j
public class SampleController {
	//String 형식으로 보내기
	@GetMapping(value="/getText", produces = "text/plain; charset=UTF-8")
	public String getText() {
		
			log.info("MIME TYPE " + MediaType.TEXT_PLAIN_VALUE);
		
		return "안녕하세요";   //@Contoller는 jsp 파일 이름으로 반환되지만  @RestContoller 는 순수한 데이터 반환가능 
	}
	
	//XML,JSON 형식(getSample.json)으로 보내기  
	@GetMapping(value = "/getSample", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
	public SampleVO getSample() {
		
		return new SampleVO(112,"스타","로드"); 
	}
	
	@GetMapping(value="/check" , params= {"height","weight"})
	public ResponseEntity<SampleVO> check(Double height, Double weight){
		
		SampleVO vo =new SampleVO(0,""+height, "" + weight);
		
		ResponseEntity<SampleVO> result= null;
		
		if(height < 150) {
			result =ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);  //sample/check.json?height=140&weight=60 보낼 경우  502 상태값
		}else {
			result =ResponseEntity.status(HttpStatus.OK).body(vo);  // 150 보다 작을 경우  200 상태값
			
		}
		return result;
	} 
	
	// '{}' 형식으로 파라미터를 보내기 위해 @PathVariable 사용 예제
	@GetMapping("/produce/{cat}/{pid}") //ex) sample/produce/안녕하세요/150
	public String [] getPath(@PathVariable("cat") String cat,@PathVariable("pid") Integer pid) {
		return new String[] {"category:" +cat , "productid :" +pid };   //<Strings><item>category:안녕하세요</item><item>productid150</item></Strings>     
	}
	
	//요청한 내용을 처리하기 떄문에 @PostMapping 사용 
	@PostMapping("/ticket")
	public Ticket convert(@RequestBody Ticket ticket) {
		
		log.info("convert.... ticket" + ticket);
		
		return ticket;
	}
		

}
