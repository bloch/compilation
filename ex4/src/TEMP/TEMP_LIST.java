package TEMP;

public class TEMP_LIST
{
    /****************/
    /* DATA MEMBERS */
    /****************/
    public TEMP head;
    public TEMP_LIST tail;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public TEMP_LIST(TEMP head,TEMP_LIST tail)
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

    public void PrintTempList() {
        TEMP_LIST tmp = this;
        System.out.format("\nTEMP_LIST HOLDS: ");
        while(tmp != null) {
            System.out.format("%s\t", tmp.head.name);
            tmp = tmp.tail;
        }
        System.out.format("\n");
    }

    public void AddToTempList(TYPE t) {
        TEMP_LIST tmp = this;
        if (tmp.head == null) {
            tmp.head = t;
            return;
        }
        while(tmp.tail != null) {
            //System.out.format("%s\t", tmp.head.name);
            tmp = tmp.tail;
        }

        tmp.tail = new TEMP_LIST(t, null);
    }
}
