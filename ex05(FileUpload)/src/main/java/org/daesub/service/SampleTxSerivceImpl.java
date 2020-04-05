package org.daesub.service;

import org.daesub.mapper.SampleMapper1;
import org.daesub.mapper.SampleMapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class SampleTxSerivceImpl implements SampleTxSerivce {

	@Setter(onMethod = @__({@Autowired}))
	private SampleMapper1 sampMapper1;

	@Setter(onMethod = @__({@Autowired}))
	private SampleMapper2 sampMapper2;

    @Transactional	
	@Override
	public void addData(String value) {
		
		log.info("mapper1.............." + value);
		sampMapper1.insertCol1(value);
		log.info("mapper2.............."+ value);
		sampMapper2.insertCol2(value);
		log.info("end..............");

	}
	
	
}
