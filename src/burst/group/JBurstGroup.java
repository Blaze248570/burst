package burst.group;

import java.util.ArrayList;

import burst.JBurstBasic;
import burst.JBurstSprite;
import burst.util.JBurstDestroyUtil;
import burst.util.JBurstDestroyUtil.IBurstDestroyable;

/**
 * A JBurstGroup is more or less just a glorified ArrayList that holds JBurstBasics.
 * <p>
 * It's main job is to make sure that all of its necessary members are updated and painted.
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/group/FlxGroup.html">FlxG</a>
 */
public class JBurstGroup<T extends JBurstBasic> implements IBurstDestroyable
{
    /**
     * The actual ArrayList that contains the elements within this group
     * <p> If there is an ArrayList function that this group does not have, 
     * it may be employed through {@code members}
     */
    public ArrayList<T> members;

    private int maxSize;

    /**
     * Constructs a new JBurstGroup with no maximum size
     */
    public JBurstGroup()
    {
        this.members = new ArrayList<>();
        this.maxSize = 0;
    }

    /**
     * Constructs a new JBurstGoup with a maximum size
     * <p> If {@code members.size()} is equal to or exceeds {@code maxSize}, 
     * no more elements will be accepted into it.
     * 
     * @param maxSize   the maximum amount of elements this group may hold
     */
    public JBurstGroup(int maxSize)
    {
        if(maxSize < 1)
        {
            this.members = new ArrayList<>();
            this.maxSize = 0;
        }
        else
        {
            this.members = new ArrayList<>(maxSize);
            this.maxSize = maxSize;
        }
    }

    /**
     * Updates every existent, active member of this group
     * 
     * @param elasped   time in milliseconds between each call to {@code update()}
     */
    public void update(int elasped)
    {
        if(members == null) return;

        for(int i = 0; i < members.size(); i++)
        {
            JBurstBasic basic = members.get(i);
            if(basic != null && basic.exists && basic.active)
            {
                basic.update(elasped);
            }
        }
    }

    /**
     * Updates every existent, visible member of this group
     */
    public void repaint()
    {
        if(members == null) return;

        for(int i = 0; i < members.size(); i++)
        {
            JBurstBasic basic = members.get(i);
            if(basic != null)
            {
                basic.repaint();
            }
        }
    }

    /**
     * Appends an element to the end of this group.
     * <p>
     * If {@code members.size()} equals or exceeds {@code maxSize},
     * this call to add will be ignored.
     * 
     * @param element   the element to be added to this group
     * @return  whether or not the element was successfully added
     */
    public boolean add(T element)
    {
        if(element == null || contains(element)) return false;

        int index = findFirstNull();
        if(index > -1)
        {
            members.set(index, element);
            return true;
        }
        
        return add(-1, element);
    }

    /**
     * Inserts an element at {@code index}.
     * <p>
     * If {@code members.size()} equals or exceeds {@code maxSize},
     * this call to add will be ignored.
     * 
     * @param element   the element to be added to this group
     * @return  whether or not the element was successfully added
     */
    public boolean add(int index, T element)
    {
        if(element == null || contains(element)) 
            return false;

        if(maxSize > 0 && members.size() >= maxSize)
            return false;

        if(index > -1)
            members.add(index, element);
        else
            members.add(element);
        
        return true;
    }

    /**
     * Replaces the element at {@code index} with {@code element}.
     * <p>
     * If {@code index} is less than zero or exceeds the length of {@code members}, 
     * the element will be appended to the end, if possible.
     * 
     * @param index     the index of the element to be replaced
     * @param element   the element to be set at {@code index}
     * @return  the element that was replaced
     */
    public T set(int index, T element)
    {
        if(index < 0 || index >= size())
        {
            add(element);
            return element;
        }
        
        return members.set(index, element);
    }

    private int findFirstNull()
    {
        for(int i = 0; i < members.size(); i++)
        {
            if(members.get(i) == null) return i;
        }

        return -1;
    }

    /**
     * Removes an element from this group, leaving the open space as {@code null}
     * 
     * @param element   the element to be removed
     * @return  whether or not the group contained {@code element}
     */
    public boolean remove(T element)
    {
        return remove(element, false);
    }

    /**
     * Removes an element from this group
     * 
     * @param element   the element to be removed
     * @param splice    whether to replace the element with null or not
     * @return  whether or not the group contained {@code element}
     */
    public boolean remove(T element, boolean splice)
    {
        int index = members.indexOf(element);
        if(index < 0)
            return false;

        if(splice)
            members.remove(element);
        else
            members.set(index, null);

        return true;
    }

    /**
     * @param element   the element to search for
     * @return  whether or not this group contains {@code element}
     */
    public boolean contains(T element)
    {
        return members.indexOf(element) > -1;
    }
    
    /**
     * <i>Convienence function for {@code members.size()}</i>
     * 
     * @return  the number of elements added to this group
     */
    public int size()
    {
        return members.size();
    }

    /**
     * Sets the maximum size for this group
     * <p> 
     * <i>If {@code size} is less than one, this call will be ignored.</i>
     * 
     * @param size  the amount of elements this group may hold
     */
    public void setMaxSize(int size)
    {
        if(size < 1)
            return;

        maxSize = size;
        trim();
    }

    /**
     * Cuts off any elements whose index exceeds {@code  maxSize - 1}
     * 
     * @return  an ArrayList of all the elements that were removed
     */
    public ArrayList<T> trim()
    {
        ArrayList<T> list = new ArrayList<>();

        while(size() > maxSize)
        {
            list.add(members.remove(members.size() - 1));
        }

        return list;
    }
    
    /**
     * Destroys this group.
     * <p>
     * <i>Warning: This will also call destroy on all subsequent elements!</i>
     * 
     * @see {@link JBurstSprite#destroy()}
     */
    @Override
    public void destroy()
    {
        members = JBurstDestroyUtil.destroyArrayList(members);
    }
}
