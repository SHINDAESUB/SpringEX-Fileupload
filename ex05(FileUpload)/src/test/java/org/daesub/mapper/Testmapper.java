package org.daesub.mapper;

import java.util.List;

import org.daesub.domain.BoardVO;
import org.daesub.domain.Criteria;
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
public class Testmapper {
	
	@Setter(onMethod = @__({@Autowired}))
	private BoardMapper mapper;
	

//	@Test
//	public void testInsert() {
//		
//		BoardVO board=new BoardVO();
//		board.setTitle("새로 작성");
//		board.setContent("새로작성하는 글");
//		board.setWriter("운영자");
//		
//		mapper.insert(board);
//		
//		log.info(board);
//					
//	}
//	@Test
//	public void testlist() {
//		mapper.getList().forEach(board -> log.info(board));
//	}

//	@Test
//	public void read() {
//		BoardVO board =mapper.read(8L);
//		log.info(board);
//	}
//	
	
//	@Test
//	public void testdelete() {
//		
//		log.info("log info :" + mapper.delete(8L));
//	}
	
//	@Test
//	public void update() {
//		BoardVO board =new BoardVO();
//		board.setContent("수정했어용");
//		board.setTitle("수정했어용 재목");
//		board.setWriter("운영자");
//		board.setBno(5L);
//		
//		mapper.update(board);
//
//		log.info(mapper);
//	}
//	
//	@Test
//	public void testlist() {
//		mapper.getList().forEach(board -> log.info(board));
//	}
//
	@Test 
	public void testPaging() {
		
		Criteria cri =new Criteria();
		
		cri.setAmount(10);
		cri.setPageNum(3);
		
		List<BoardVO> list= mapper.getListWithPaging(cri); 
		
		list.forEach(board -> log.info(board));
		
		
	}
	
}
