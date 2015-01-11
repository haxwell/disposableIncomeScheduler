package com.haxwell.disposableIncomeScheduler;

import static org.junit.Assert.assertTrue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProcessorTest extends JSONDataBasedTest {

	@Before
	public void setup() {
		createDataAndStateObjects();
	}

	@After
	public void teardown() {
		
	}
	
	@Test
	public void testFoo() {
		Processor sut = new Processor();
		
		InputGetter mockedInputGetter = mock(InputGetter.class);
		when(mockedInputGetter.readInput()).thenReturn("quit");
		
		sut.setInputGetter(mockedInputGetter);
		
		boolean rtn = sut.process();
		
		assertTrue(rtn == false);
	}
}
