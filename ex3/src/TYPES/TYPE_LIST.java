package TYPES;

public class TYPE_LIST
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public TYPE head;
	public TYPE_LIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public TYPE_LIST(TYPE head,TYPE_LIST tail)
	{
		this.head = head;
		this.tail = tail;
	}
	public int numOfParams(){
		if (head == null){
			return 0;
		}
		if (tail == null){
			return 1;
		}
		return 1 + tail.numOfParams();
	}

	public void PrintTypeList() {
        TYPE_LIST tmp = this;
		System.out.format("\nTYPE_LIST HOLDS: ");
        while(tmp != null) {
            System.out.format("%s\t", tmp.head.name);
            tmp = tmp.tail;
        }
		System.out.format("\n");
	}

	public void AddToTypeList(TYPE t) {
		TYPE_LIST tmp = this;
		if (tmp.head == null) {
			tmp.head = t;
			return;
		}
		while(tmp.tail != null) {
			//System.out.format("%s\t", tmp.head.name);
			tmp = tmp.tail;
		}

		tmp.tail = new TYPE_LIST(t, null);
	}
}
