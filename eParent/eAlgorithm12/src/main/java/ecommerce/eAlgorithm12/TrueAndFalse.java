package ecommerce.eAlgorithm12;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ecommerce.base.Context;
import ecommerce.base.ITrueAndFalse;
import ecommerce.patterns.trueandfalse.gonext.IGoNext;
import ecommerce.patterns.trueandfalse.stop.IStop;

public class TrueAndFalse implements ITrueAndFalse {
	
	private static final Logger logger = LoggerFactory.getLogger(TrueAndFalse.class);
	/*private static int[] metaData = new int[] { 1, 2, 3, 5, 8, 13, 21, 34,
		55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946,
		17711, 28657, 46368 };*/
	private static int[] metaData = new int[] {1,3,7,15,31,63,127,255,511,1023,2047,4095,8191,16383,32767};
	
	private List<Boolean> result;
	private int resultIndex=0, metaIndex=0;
	private List<Integer> process;
	private boolean toBePrint = true;
	private int offset;
	@Override public int getOffset(){return offset;}
	
	public TrueAndFalse(List<Boolean> result){
		this.result = result;
	}
	public TrueAndFalse(){
	}
	
	@Override
	public Context getContext() {
		
		Context context = new Context();
		context.put("TAF_SHOULD_STOP", this.toBePrint);
		StringBuilder sb = new StringBuilder();
		String stastic = String.format(" = %d [MAX:%d, x:%d (%f%%), o:%d (%f%%)]\r\n", 
				sum, max, 
				countFalse, ((float)countFalse*100/(float)(countFalse+countTrue)),
				countTrue, ((float)countTrue*100/(float)(countFalse+countTrue)));
		
		sb.append("\t");
		for(int val:this.process){
			sb.append(val>=0?"+"+val:val);
		}
		sb.append(stastic);
		context.put("TAF_ROW", sb.toString());
		return context;
	}
	
	public void print(){

		//if(this.toBePrint){
			logger.info("[ x:{} ({}%), o:{} ({}%) ]\r\n", 
					countFalse, ((float)countFalse*100/(float)(countFalse+countTrue)), 
					countTrue, ((float)countTrue*100/(float)(countFalse+countTrue)));
			
			for(int val:this.process){
				if(val>=0)
					logger.warn("+");
				logger.warn("{}", val);
			}
			logger.warn(" = {} [ MAX: {} ]\r\n", sum, max);
		//}
	}
	
	public void run(int offset){
		
		this.countTrue = 0;
		this.countFalse = 0;
		this.max = 0;
		this.sum = 0;
		metaIndex = 0;
		this.offset = offset;
		boolean shouldStop = false;
		this.process = new ArrayList<Integer>();
		for (this.resultIndex = offset; !shouldStop && this.resultIndex < result.size(); this.resultIndex++) {
			
			int current = metaData[this.metaIndex];
			if(max < current)//记录最大值
				max = current;

			if (result.get(this.resultIndex)) {	this.countTrue++;
				sum += metaData[this.metaIndex];
				this.process.add(metaData[this.metaIndex]);
				this.current = metaData[this.metaIndex];
				//logger.info("+{}", metaData[this.metaIndex]);
				if (this.metaIndex != 0)
					this.metaIndex--;
			} else {	this.countFalse++;
				sum -= metaData[this.metaIndex];
				this.process.add(-metaData[this.metaIndex]);
				this.current = -metaData[this.metaIndex];
				//logger.info("-{}", metaData[this.metaIndex]);
				this.metaIndex++;
			}

			this.metaIndex = TrueAndFalse.goNext.GetNext(this);
			
			shouldStop = TrueAndFalse.stop.match(this);
		}
		this.toBePrint = shouldStop;
		//logger.info(" = {} [ MAX: {} ]\r\n", sum, max);
	}

	static private IStop stop;
	public void setStop(IStop stop){
		TrueAndFalse.stop = stop;
	}
	
	static private IGoNext goNext;
	public void setGoNext(IGoNext next){
		TrueAndFalse.goNext = next;
	}
	
	private int sum, max, current;
	private int countTrue, countFalse;
	
	public int getSum(){return this.sum;}
	public int getMax(){return this.max;}
	public int getCurrent(){return this.current;}
	public int getResultPos(){return this.resultIndex+1;}
	public int getMetaPos() { return this.metaIndex; }
	public int getCountTrue(){return this.countTrue;}
	public int getCountFalse(){return this.countFalse;}
	public List<Boolean> getResult(){return this.result;}
	@Override public List<Integer> getProcess() {return this.process;}
	@Override public boolean isValid() {return this.toBePrint;}
}
