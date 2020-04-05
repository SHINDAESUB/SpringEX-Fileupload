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

@RestController  // JSP �� �޸� ������ �����͸� ��ȯ�ϴ� ���� �̹Ƿ� �پ��� ������ �����͸� ���� ���� �ϴ�.  JSON �̳� XML �ַ� ���
@RequestMapping("/sample")
@Log4j
public class SampleController {
	//String �������� ������
	@GetMapping(value="/getText", produces = "text/plain; charset=UTF-8")
	public String getText() {
		
			log.info("MIME TYPE " + MediaType.TEXT_PLAIN_VALUE);
		
		return "�ȳ��ϼ���";   //@Contoller�� jsp ���� �̸����� ��ȯ������  @RestContoller �� ������ ������ ��ȯ���� 
	}
	
	//XML,JSON ����(getSample.json)���� ������  
	@GetMapping(value = "/getSample", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
	public SampleVO getSample() {
		
		return new SampleVO(112,"��Ÿ","�ε�"); 
	}
	
	@GetMapping(value="/check" , params= {"height","weight"})
	public ResponseEntity<SampleVO> check(Double height, Double weight){
		
		SampleVO vo =new SampleVO(0,""+height, "" + weight);
		
		ResponseEntity<SampleVO> result= null;
		
		if(height < 150) {
			result =ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);  //sample/check.json?height=140&weight=60 ���� ���  502 ���°�
		}else {
			result =ResponseEntity.status(HttpStatus.OK).body(vo);  // 150 ���� ���� ���  200 ���°�
			
		}
		return result;
	} 
	
	// '{}' �������� �Ķ���͸� ������ ���� @PathVariable ��� ����
	@GetMapping("/produce/{cat}/{pid}") //ex) sample/produce/�ȳ��ϼ���/150
	public String [] getPath(@PathVariable("cat") String cat,@PathVariable("pid") Integer pid) {
		return new String[] {"category:" +cat , "productid :" +pid };   //<Strings><item>category:�ȳ��ϼ���</item><item>productid150</item></Strings>     
	}
	
	//��û�� ������ ó���ϱ� ������ @PostMapping ��� 
	@PostMapping("/ticket")
	public Ticket convert(@RequestBody Ticket ticket) {
		
		log.info("convert.... ticket" + ticket);
		
		return ticket;
	}
		

}
