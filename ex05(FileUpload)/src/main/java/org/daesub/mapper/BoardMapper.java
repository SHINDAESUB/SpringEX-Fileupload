package org.daesub.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.daesub.domain.BoardVO;
import org.daesub.domain.Criteria;

public interface BoardMapper {
	//@Select("select * from tb1_board where bno >0 ")
	public List<BoardVO> getList();
	
	public List<BoardVO> getListWithPaging(Criteria cri);
	
	public void insert(BoardVO board);
	
	public void insertSelectKey(BoardVO board);
	
	public BoardVO read(Long bno);
	
	public int delete (Long bno);  //정상적으로 삭제되면 1이라는 값이 나오도록
	
	public int update(BoardVO board);
	
	public int getTotalCount(Criteria cri);
	
	public void updateReplyCnt(@Param("bno")Long bno ,@Param("amount")int amount);
	
}
