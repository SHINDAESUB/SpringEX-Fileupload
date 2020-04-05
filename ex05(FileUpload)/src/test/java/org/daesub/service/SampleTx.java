package org.daesub.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@Log4j
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
public class SampleTx {
	
	@Setter(onMethod = @__({@Autowired}))
	private SampleTxSerivce serivce;
	
	@Test
	public void testLong() {
		
		String str="abcdeofjfgldjdjhdkdcichdkdodldfljwerkl;wejr;klwerjhwehrweuiqhruiweqhruihweqruihwqeruiposdiopcvuxciouvxciovuxciovuxc";
		
		log.info( "�Ŵ뼷�Դϴ�.,,,"+str.getBytes().length);
		
		serivce.addData(str);
		
	}
	
}
