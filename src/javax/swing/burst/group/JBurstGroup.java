package javax.swing.burst.group;

import java.util.ArrayList;
import javax.swing.burst.JBurstBasic;
import javax.swing.burst.util.JBurstDestroyUtil;

public class JBurstGroup<T extends JBurstBasic> extends JBurstBasic
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

    @Override
    public void update(double elasped)
    {
        for(T object : members)
        {
            if(object != null && object.exists && object.active)
            {
                object.update(elasped);
            }
        }
    }

    public T add(T object)
    {
        return add(-1, object);
    }

    public T add(int position, T object)
    {
        if(object == null) return null;
        if(members.indexOf(object) > -1) return object;

        int index = findFirstNull();
        if(index > -1)
        {
            members.set(index, object);
            if(index >= length)
                length = index + 1;

            return object;
        }

        if(maxSize > 0 && length >= maxSize)
            return object;

        if(position > -1)
            members.add(position, object);
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
        super.destroy();

        members = JBurstDestroyUtil.destroyArrayList(members);
    }
}
