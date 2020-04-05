package org.daesub.domain;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data  // 알아서 getter/setter tostring() 생성
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
