package org.daesub.service;

import java.util.List;

import org.daesub.domain.Criteria;
import org.daesub.domain.ReplyVO;
import org.daesub.domain.ReplyPageDTO;

public interface ReplyService {
	public int register(ReplyVO vo);

	public ReplyVO get(Long rno);

	public int modify(ReplyVO vo);

	public int remove(Long rno);

	public List<ReplyVO> getList(Criteria cri, Long bno);
	
	public ReplyPageDTO getListPage(Criteria cri, Long bno);
	

}
