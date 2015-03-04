package ecommerce.eAlgorithm12.element;

import java.lang.reflect.Constructor;

import ecommerce.eAlgorithm12.IExpect;

public class Element5Builder implements IElementBuilder{
	
	private String typeOfElement;
	public void setTypeOfElement5(String type) { this.typeOfElement = type; }
	
	@Override
	public IElement createElement(String source, int startOff) {
		
		IElement rtn = null;
		int length = startOff+5<source.length()?5:source.length()-startOff;
		char[] data = source.substring(startOff, startOff+length).toCharArray();
		
		try {
			Constructor constructor = Class.forName(this.typeOfElement).getConstructor(char[].class, IExpect.class);
			rtn = (IElement)constructor.newInstance(data, this.expect);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//return new Element5(data, expect);
		return rtn;
	}
	
	@Override public int getLength(){ return 5; }
	
	private IExpect expect;
	@Override public IExpect getExpect(){ return this.expect; }
	@Override public void setExpect(IExpect expect){ this.expect = expect; }
}