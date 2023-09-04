package javax.swing.burst.util;

import java.util.ArrayList;

public class JBurstDestroyUtil
{
    /**
     * Checks if an object is not null before destroying it, <i>always returns null.</i>
     * 
     * @param object    Object to be destroyed
     * @return  a null pointer
     */
    public static <T extends IBurstDestroyable> T destroy(IBurstDestroyable object)
    {
        if(object != null) object.destroy();

        return null;
    }

    /**
	 * Destroys every element of an array of IBurstDestroyables.
	 *
	 * @param	array	An array of IFlxDestroyable objects
	 * @return	a null pointer
	 */
    public static <T extends IBurstDestroyable> T destroyArray(T[] array)
    {
        if(array != null)
            for(T object : array)
                destroy(object);
            
        return null;
    }

    /**
	 * Destroys every element of an ArrayList of IBurstDestroyables.
	 *
	 * @param	array	An ArrayList of IFlxDestroyable objects
	 * @return	a null pointer
	 */
    public static <T extends IBurstDestroyable> ArrayList<T> destroyArrayList(ArrayList<T> array)
    {
        if(array != null)
            for(T object : array)
                destroy(object);
            
        return null;
    }

    public static interface IBurstDestroyable
    {
        public void destroy();
    }
}
