class EmptyQueueException extends Exception {
	public EmptyQueueException (){
		super();
	}
	public EmptyQueueException (String s){
		super(s);
	}
}