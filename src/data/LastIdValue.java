package data;
import java.io.Serializable;

public class LastIdValue implements Serializable {


	private static final long serialVersionUID = 1L;
	
	private String id = "1";
	
	public LastIdValue() {
		
	}

	public LastIdValue(String currentId) {
		this.id = currentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "LastIdValue [id=" + id + "]";
	}
	
}
