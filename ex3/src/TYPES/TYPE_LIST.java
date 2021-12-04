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
}
