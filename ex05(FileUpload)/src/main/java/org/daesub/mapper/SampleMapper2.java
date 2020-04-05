package org.daesub.mapper;

import org.apache.ibatis.annotations.Insert;

public interface SampleMapper2 {
	
	@Insert("insert into tb1_sample2 (col2) values (#{data})")
	public int insertCol2(String data);

}
