/***********/
/* PACKAGE */
/***********/
package TEMP;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class TEMP_LIST
{
    public TEMP head;
    public TEMP_LIST tail;

    public TEMP_LIST(TEMP head, TEMP_LIST tail) {
        this.head = head;
        this.tail = tail;
    }

    public void AddToTEMPList(TEMP temp) {
        TEMP_LIST tmp = this;
        if (tmp.head == null) {
            tmp.head = temp;
            return;
        }
        while(tmp.tail != null) {
            tmp = tmp.tail;
        }
        tmp.tail = new TEMP_LIST(temp, null);
    }
}