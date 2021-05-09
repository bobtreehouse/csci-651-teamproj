import java.util.ArrayList;

public class InternalNode
{
    /*The internal node object is a node which is designed to hold at least 2-4 default nodes which represent the keys*/
    public ArrayList<Key> keyArrayList = new ArrayList<>();
    public int id =0;
    public InternalNode()
    {
        ArrayList<Key> keyArrayList = null;
    }
    public InternalNode(int id)
    {
        ArrayList<Key> keyArrayList = null;
        this.id = id;
    }

    public Boolean isLeaf()
    {
        boolean flag = true;
        for(int i=0; i< keyArrayList.size();i++)
        {
            if (keyArrayList.get(i).getLeft() !=null || keyArrayList.get(i).getRight() !=null)
            {
                flag = false;
                break;
            }
        }
        return flag;
    }
    public void setNodeArrayList(ArrayList<Key> keyArrayList) {
        this.keyArrayList = keyArrayList;
    }
    public ArrayList<Key> getNodeArrayList()
    {
        return keyArrayList;
    }

    public void addKey(Key key){ keyArrayList.add(key);}

}

