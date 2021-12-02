package TYPES;

public class TYPE_ARRAY extends TYPE
{
    /*********************************************************************/
    /* If this class does not extend a father class this should be null  */
    /*********************************************************************/
    public TYPE type;

    /****************/
    /* CTROR(S) ... */
    /****************/
    public TYPE_ARRAY(TYPE type)
    {
        this.type = type;
    }

    public TYPE getArrayType(){
        return this.type;
    }

    public boolean isArray(){ return true;}
}
