package TYPES;

public class TYPE_ID extends TYPE
{
    /*********************************************************************/
    /* If this class does not extend a father class this should be null  */
    /*********************************************************************/
    public TYPE type;
    public float int_value;
    public String string_value;

    /****************/
    /* CTROR(S) ... */
    /****************/
    public TYPE_ID(TYPE type, String name)
    {
        this.type = type;
        this.name = name;
        this.int_value = 0.5f;
        this.string_value = "null";
    }

    public TYPE_ID(TYPE type, String name , float int_value , String string_value)
    {
        this.type = type;
        this.name = name;
        this.int_value = int_value;
        this.string_value = string_value;
    }

    public TYPE getType(){
        return this.type;
    }

    public boolean isTypeId(){return true;}
}