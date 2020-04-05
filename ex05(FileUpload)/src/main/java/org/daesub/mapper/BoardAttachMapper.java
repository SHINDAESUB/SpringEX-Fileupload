package org.daesub.mapper;

import java.util.List;

import org.daesub.domain.BoardAttachVO;

public interface BoardAttachMapper {

	public void insert(BoardAttachVO vo);
	
	//DB�� ���� ������
	public void delete(String uuid);

	public List<BoardAttachVO> findByBno(Long bno);
	
	//DB ���� ����
	public void deleteAll(Long bno);
	
	public List<BoardAttachVO> getOldFiles();
	
	
}
