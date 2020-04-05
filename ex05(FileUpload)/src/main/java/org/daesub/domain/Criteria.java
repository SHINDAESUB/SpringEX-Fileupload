package org.daesub.domain;


import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {
	
	private int pageNum;  //페이지 번호
	private int amount;   //데이터 갯수
	private boolean prev,next; //1일 경우에 존재하는 것	
	
	private String type;   //검색 조건
	private String keyword; //검색어 
	
	public Criteria() {
		this(1,10);      //인스턴스 변수 생성
	}

	public Criteria(int pageNum, int amount) {
		this.amount =amount;
		this.pageNum= pageNum;
	}
	//검색을 하기 위함
	public String[] getTypeArr() { 
		return type== null? new String[] {} : type.split("");		
	}
	
	//링크를 생성하기 위합
	public String getListLink() {
		//UriComponentsBuilder 여러개의 파라미터들을 연결해서 URL 형태로 만들어줌
		UriComponentsBuilder builder=UriComponentsBuilder.fromPath("")
				.queryParam("pageNum",this.pageNum)
				.queryParam("amount", this.getAmount())
				.queryParam("type", this.getType())
				.queryParam("keyword", this.getKeyword());
		
		return builder.toUriString();
	}
	
	
}
