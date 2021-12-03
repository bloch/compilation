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
<<<<<<< HEAD
    public TYPE_ARRAY(TYPE type)
    {
        this.type = type;
=======
    public TYPE_ARRAY(TYPE type, String name)
    {
        this.type = type;
        this.name = name;
>>>>>>> eb5421fe539d304b63b583384b8e569c1d6b692e
    }

    public TYPE getArrayType(){
        return this.type;
    }

    public boolean isArray(){ return true;}
}
