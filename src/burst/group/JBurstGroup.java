package burst.group;

import java.util.ArrayList;

import burst.JBurstBasic;
import burst.util.JBurstDestroyUtil;
import burst.util.JBurstDestroyUtil.IBurstDestroyable;

public class JBurstGroup<T extends JBurstBasic> implements IBurstDestroyable
{
    public ArrayList<T> members;

    private int maxSize;
    private int length;

    public JBurstGroup()
    {
        super();

        members = new ArrayList<>();
        maxSize = 0;
    }

    public JBurstGroup(int maxSize)
    {
        super();

        this.members = new ArrayList<>(maxSize);
        this.maxSize = maxSize;
    }

    public void update(double elasped)
    {
        for(int i = 0; i < members.size(); i++)
        {
            JBurstBasic basic = members.get(i);
            if(basic != null && basic.exists && basic.active)
            {
                basic.update(elasped);
            }
        }
    }

    public T add(T object)
    {
        if(object == null || contains(object)) return object;

        int index = findFirstNull();
        if(index > -1)
        {
            members.set(index, object);
            if(index >= length)
                length = index + 1;

            return object;
        }
        
        return add(-1, object);
    }

    public T add(int index, T object)
    {
        if(object == null || contains(object)) return object;

        if(maxSize > 0 && length >= maxSize)
            return object;

        if(index > -1)
            members.add(index, object);
        else
            members.add(object);
        
        length++;

        return object;
    }

    private int findFirstNull()
    {
        for(int i = 0; i < members.size(); i++)
        {
            if(members.get(i) == null) return i;
        }

        return -1;
    }

    public T remove(T object)
    {
        return remove(object, false);
    }

    public T remove(T object, boolean splice)
    {
        int index = members.indexOf(object);
        if(index < 0)
            return null;

        if(splice)
        {
            members.remove(object);
            length--;
        }
        else
            members.set(index, null);

        return object;
    }

    public boolean contains(T object)
    {
        return members.indexOf(object) > -1;
    }

    // Size is currently taken by JComponent
    /*
        public int size()
        {
            return length;
        }
    */

    @Override
    public void destroy()
    {
        members = JBurstDestroyUtil.destroyArrayList(members);
    }
}
