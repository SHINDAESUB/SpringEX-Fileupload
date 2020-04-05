package org.daesub.mapper;

import org.apache.ibatis.annotations.Insert;

public interface SampleMapper1 {
	
	@Insert("insert into tb1_sample1 (col1) values (#{data})")
	public int insertCol1(String data);

}
