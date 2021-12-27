package TYPES;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;

	public int class_offset;

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}

	/*************/
	/* isFunction() */
	/*************/
	public boolean isFunction(){return false;}

	/*************/
	/* isTypeId() */
	/*************/
	public boolean isTypeId(){return false;}
}
