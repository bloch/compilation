package TYPES;

public class TYPE_ID extends TYPE
{
    /*********************************************************************/
    /* If this class does not extend a father class this should be null  */
    /*********************************************************************/
    public TYPE type;

    /****************/
    /* CTROR(S) ... */
    /****************/
    public TYPE_ID(TYPE type, String name)
    {
        this.type = type;
        this.name = name;
    }

    public TYPE getType(){
        return this.type;
    }
}