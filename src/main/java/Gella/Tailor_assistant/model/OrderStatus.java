package Gella.Tailor_assistant.model;

public enum OrderStatus {
	
	WAITING_FOR_EXECUTION("waiting for execution"),
	WAITING_FOR__FITTING("waiting for fitting"),
	READY("ready"),
	CLOSED("closed");
	String title;
	
	OrderStatus(String title){
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public static String[] titles() {
		String[] s= new String[4];
		s[0]= OrderStatus.WAITING_FOR_EXECUTION.title;
		s[1]= OrderStatus.WAITING_FOR__FITTING.title;
		s[2]= OrderStatus.READY.title;
		s[3]= OrderStatus.CLOSED.title;
		return s;
	}
	
	public static OrderStatus valueOfTitle(String s) {
		OrderStatus[] val = values();
		for (int i=0; i<val.length;i++)
			if (val[i].title==s) return val[i];
		return null;
	}
	
	
}
