package org.daesub.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PageDTO {
	
	private int startPage;
	private int endPage;
	private boolean prev,next; 
	
	private int total;
	private Criteria cri; 
	
	public PageDTO(Criteria cri, int total) { //생성자 정의 
		
		this.cri =cri;
		this.total= total;
		this.endPage= (int)(Math.ceil(cri.getPageNum()/10.0))*10; //페이지 계수를 가지고 마지막 값을 구함
		this.startPage=this.endPage -9; //화면에서 10개씩 보여줄 경우  
		
		int realEnd=(int)(Math.ceil(total * 1.0)/cri.getAmount());  // 전체 데이터 수를 이용해서 진짜 끝 페이지 계산 

		if(realEnd < this.endPage) {  //진짜 끝페이지 보다 구해둔 끝 번호보다 작으면  반환
			this.endPage = realEnd;
		}
		
		this.prev = this.startPage >1;  //1보다 작으면 false 1보다 작은 페이지는 없다.
		
		this.next = this.endPage <realEnd ; //진짜 끝 페이지 는 구해둔 끝 번호 보다 커야 한다.
		
	}
	
	
}
