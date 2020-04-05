package org.daesub.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data  // �˾Ƽ� getter/setter tostring() ����
public class BoardVO {

	private Long bno;
	private String title;
	private String content;
	private String writer;
	private Date regdate;
	private Date updateDate;
	
	private int replyCnt;
	
	private List<BoardAttachVO> attachList;
	
}
