package ecommerce.eAlgorithm10;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ecommerce.base.ITrueAndFalse;
import ecommerce.patterns.trueandfalse.gonext.IGoNext;
import ecommerce.patterns.trueandfalse.stop.IStop;

public class TrueAndFalse implements ITrueAndFalse {
	
	private static final Logger logger = LoggerFactory.getLogger(TrueAndFalse.class);
	private static int[] metaData = new int[] { 1, 2, 3, 5, 8, 13, 21, 34,
		55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946,
		17711, 28657, 46368 };
	
	private List<Boolean> result;
	private int resultIndex=0, metaIndex=0;
	public TrueAndFalse(List<Boolean> result){
		this.result = result;
	}
	public TrueAndFalse(){
	}
	
	public void print(){
		
		StringBuilder sBuild = new StringBuilder();
		this.countTrue = 0;
		this.countFalse = 0;
		for(Boolean o:this.result){
			if(o){
				countTrue ++;
				sBuild.append("o");
			} else {
				countFalse ++;
				sBuild.append("x");
			}
		}
		
		logger.info(" [ x:{} ({}%), o:{} ({}%) ]\r\n", 
				countFalse, ((float)countFalse*100/(float)(countFalse+countTrue)), 
				countTrue, ((float)countTrue*100/(float)(countFalse+countTrue)));
	}
	
	public void run(int offset){
		
		this.max = 0;
		this.sum = 0;
		metaIndex = 0;
		boolean shouldStop = false;
		for (this.resultIndex = offset; !shouldStop && this.resultIndex < result.size(); this.resultIndex++) {
			
			int current = metaData[this.metaIndex];
			if(max < current)//记录最大值
				max = current;

			if (result.get(this.resultIndex)) {
				sum += metaData[this.metaIndex];
				this.current = metaData[this.metaIndex];
				logger.info("+{}", metaData[this.metaIndex]);
				if (this.metaIndex != 0)
					this.metaIndex--;
			} else {
				sum -= metaData[this.metaIndex];
				this.current = -metaData[this.metaIndex];
				logger.info("-{}", metaData[this.metaIndex]);
				this.metaIndex++;
			}
			
			//if(TrueAndFalse.next.go2First(result, this.resultIndex+1, current))
			//	this.metaIndex = 0;
			this.metaIndex = TrueAndFalse.goNext.GetNext(this);
			
			shouldStop = TrueAndFalse.stop.match(this);
		}
		logger.info(" = {} [ MAX: {} ]\r\n", sum, max);

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
}
