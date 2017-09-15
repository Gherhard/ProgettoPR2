import java.util.*;
class User {

	private String name; //user name
        private int password; //password
	public List<DigitalDoc> docs; // lista documenti
	public Queue<String> queue;
	public User () throws IllegalArgumentException {
	//EFFECTS: lancia IllegalArgumentException
		throw new IllegalArgumentException ("Nome non e' accettabile");
	}
		
	
	public User (String a,int password) throws IllegalArgumentException {
		if (a==null)
			throw new IllegalArgumentException ("UserName null");
		name=a;
		this.password=password;
		docs= new ArrayList<DigitalDoc>();
                this.queue = new LinkedList();
	}

	public int GetPassword(){
		return password;
	}
        public String getIdUtente(){
		return name;
	}
        public void setIdUtente(String name){
		this.name=name;
	}
        public void setPassword(String Password){
		this.password=password;
	}
}
