package org.daesub.mapper;

import java.util.List;

import org.daesub.domain.BoardAttachVO;

public interface BoardAttachMapper {

	public void insert(BoardAttachVO vo);
	
	//DB에 삭제 불포함
	public void delete(String uuid);

	public List<BoardAttachVO> findByBno(Long bno);
	
	//DB 삭제 포함
	public void deleteAll(Long bno);
	
	public List<BoardAttachVO> getOldFiles();
	
	
}
