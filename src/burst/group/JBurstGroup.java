package burst.group;

import java.util.ArrayList;

import burst.JBurstBasic;
import burst.util.JBurstDestroyUtil;
import burst.util.JBurstDestroyUtil.IBurstDestroyable;

public class JBurstGroup<T extends JBurstBasic> implements IBurstDestroyable
{
    public ArrayList<T> members;

    private int maxSize;

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

    public void repaint()
    {
        if(members == null) return;

        for(int i = 0; i < members.size(); i++)
        {
            JBurstBasic basic = members.get(i);
            if(basic != null && basic.exists && basic.active)
            {
                basic.repaint();
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
            return object;
        }
        
        return add(-1, object);
    }

    public T add(int index, T object)
    {
        if(object == null || contains(object)) return object;

        if(maxSize > 0 && members.size() >= maxSize)
            return object;

        if(index > -1)
            members.add(index, object);
        else
            members.add(object);
        
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
            members.remove(object);
        else
            members.set(index, null);

        return object;
    }

    public boolean contains(T object)
    {
        return members.indexOf(object) > -1;
    }
    
    public int size()
    {
        return members.size();
    }
    
    @Override
    public void destroy()
    {
        members = JBurstDestroyUtil.destroyArrayList(members);
    }
}
