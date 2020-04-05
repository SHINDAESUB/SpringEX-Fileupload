package org.daesub.domain;


import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {
	
	private int pageNum;  //������ ��ȣ
	private int amount;   //������ ����
	private boolean prev,next; //1�� ��쿡 �����ϴ� ��	
	
	private String type;   //�˻� ����
	private String keyword; //�˻��� 
	
	public Criteria() {
		this(1,10);      //�ν��Ͻ� ���� ����
	}

	public Criteria(int pageNum, int amount) {
		this.amount =amount;
		this.pageNum= pageNum;
	}
	//�˻��� �ϱ� ����
	public String[] getTypeArr() { 
		return type== null? new String[] {} : type.split("");		
	}
	
	//��ũ�� �����ϱ� ����
	public String getListLink() {
		//UriComponentsBuilder �������� �Ķ���͵��� �����ؼ� URL ���·� �������
		UriComponentsBuilder builder=UriComponentsBuilder.fromPath("")
				.queryParam("pageNum",this.pageNum)
				.queryParam("amount", this.getAmount())
				.queryParam("type", this.getType())
				.queryParam("keyword", this.getKeyword());
		
		return builder.toUriString();
	}
	
	
}
