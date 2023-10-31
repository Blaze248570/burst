package com.github.jbb248.jburst.util;

import java.util.ArrayList;

/**
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/util/FlxDestroyUtil.html">FlxDestroyUtil</a>
 */
public class JBurstDestroyUtil
{
    /**
     * Checks if an object is not null before destroying it.
     * <p><i>Always returns null</i>
     * 
     * @param object    object to be destroyed
     * @return  a null pointer
     */
    public static <T extends IBurstDestroyable> T destroy(IBurstDestroyable object)
    {
        if(object != null) object.destroy();

        return null;
    }

    /**
	 * Destroys every element of an array of IBurstDestroyables.
     * <p><i>Always returns null</i>
	 *
	 * @param	array	an array of IFlxDestroyable objects
	 * @return	a null pointer
	 */
    public static <T extends IBurstDestroyable> T destroyArray(T[] array)
    {
        if(array != null)
            for(int i = 0; i < array.length; i++)
            {
                array[i] = destroy(array[i]);
            }
            
        return null;
    }

    /**
	 * Destroys every element of an ArrayList of IBurstDestroyables.
     * <p><i>Always returns null</i>
	 *
	 * @param	array	an ArrayList of IFlxDestroyable objects
	 * @return	a null pointer
	 */
    public static <T extends IBurstDestroyable> ArrayList<T> destroyArrayList(ArrayList<T> array)
    {
        if(array != null)
        {
            for(int i = 0; i < array.size(); i++)
            {
                destroy(array.set(i, null));
            }
        }
            
        return null;
    }

    public static interface IBurstDestroyable
    {
        public void destroy();
    }
}
