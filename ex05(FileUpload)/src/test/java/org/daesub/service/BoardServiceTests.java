package org.daesub.service;

import static org.junit.Assert.assertNotNull;
import org.daesub.domain.BoardVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j
public class BoardServiceTests {
	
	@Setter(onMethod = @__({@Autowired}))
	private BoardService serivce;
	
//	@Test
//	public void testExist() {
//		
//		log.info(serivce);
//		assertNotNull(serivce);
//		
//	}
//	@Test
//	public void testRegister() {
//	
//		BoardVO board=new BoardVO();
//		board.setWriter("신대섭이다.");
//		board.setTitle("Service Test 입니다.");
//		board.setContent("서비스 내용");
//		
//		serivce.register(board);
//		
//		log.info("생성된 번호는 ?:" + board.getBno());
//		
//	}
//	

	
//	@Test
//	public void testGet() {
//		
//		log.info(serivce.get(10L));
//				
//	}

//	@Test
//	public void testDelete() {
//		
//		log.info("remove :"+ serivce.remove(2L));
//	}
	
//	@Test
//	public void testUpdate() {
//		
//		BoardVO board= serivce.get(3L);
//		
//		if(board == null) {
//			return;
//		}
//		
//		board.setTitle("안녕하세요 수정 했는데용");
//		
//		serivce.modify(board);
//				
//	}
//	@Test
//	public void testList() {
//				
//		serivce.getList().forEach(board ->log.info(board));
//		
//	}
	
}
