package data;
import java.io.Serializable;

public class Message implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;
	private String msg;
	
	public Message() {
		
	}
	
	public Message(String msg) {
		this.msg = msg;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", msg=" + msg + "]";
	}
	

}
