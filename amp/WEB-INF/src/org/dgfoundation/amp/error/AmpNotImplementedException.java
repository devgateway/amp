package org.dgfoundation.amp.error;

public class AmpNotImplementedException extends AMPException {

	public AmpNotImplementedException() {
		super();
	}
	
	public AmpNotImplementedException(String message) {
		super(message);
	}
	
	public AmpNotImplementedException(AmpNotImplementedException ae){ 
		super(ae);
	}
	
	public AmpNotImplementedException(Throwable cause){
		super(cause);
	}
}
