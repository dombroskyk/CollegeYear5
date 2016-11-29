package aop.cruisectl;

import aop.cruisectl.UserControls.Control;

public aspect CCStateMgmt {

	private UserControls myControls;
	private State currState = null;
	
	/**
	 * Pointcut to catch when a new instance of UserControls is created.
	 */
	pointcut newUserControls() :
		call(public UserControls.new(..));
	
	/**
	 * Pointcut to catch when the "enable" transition is issued.
	 */
	pointcut enableTransition() :
		execution(public void UserControls.enable(..));
	
	/**
	 * Pointcut to catch when the "disable" transition is issued. Will not
	 * trigger when called from inside of the UserControls class, like in the
	 * constructor.
	 */
	pointcut disableTransition() :
		!withincode(UserControls.new(..)) && execution(public void UserControls.disable(..));
		
	/**
	 * Pointcut to catch when the "resume" transition is issued.
	 */
	pointcut resumeTransition() :
		execution(public void UserControls.resume(..));
		
	/**
	 * Pointcut to catch when the "set" transition is issued.
	 */
	pointcut setTransition():
		execution(public void UserControls.set(..));
		
	/**
	 * Pointcut to catch when the "cancel" transition is issued.
	 */
	pointcut cancelTransition():
		execution(public void UserControls.cancel(..));
	
	after() returning(UserControls newControls): newUserControls(){
		this.myControls = newControls;
		this.currState = State.disabled;
	}
	
	void around(): enableTransition(){
		if(this.currState == State.disabled){
			proceed();
			this.currState = State.enabled;
		} else {
			myControls.disallowed(Control.enable);
		}
	}
	
	void around(): disableTransition(){
		System.out.println("Made it");
		if(this.currState == State.enabled ||
		   this.currState == State.enabledWMem ||
		   this.currState == State.active){
			proceed();
			this.currState = State.disabled;
		} else {
			myControls.disallowed(Control.disable);
		}
	}
	
	void around(): resumeTransition(){
		if(this.currState == State.enabledWMem){
			proceed();
			this.currState = State.active;
		} else {
			myControls.disallowed(Control.resume);
		}
	}
	
	void around(): setTransition(){
		if(this.currState == State.enabled ||
		   this.currState == State.enabledWMem ||
		   this.currState == State.active){
			proceed();
			this.currState = State.active;
		} else {
			myControls.disallowed(Control.set);
		}
	}
	
	void around(): cancelTransition(){
		if(this.currState == State.active){
			proceed();
			this.currState = State.enabledWMem;
		} else {
			myControls.disallowed(Control.cancel);
		}
	}
		
	
}
