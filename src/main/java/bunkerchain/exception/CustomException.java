package bunkerchain.exception;

public class CustomException extends RuntimeException {
	  public CustomException(String msg){
	        super(msg);
	    }

	    public CustomException() {
	        super();
	    }
}
