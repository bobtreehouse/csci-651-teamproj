import java.util.Comparator;

public class Key
{
    /*This is the basic node class which defines the nodes, or keys which will be stored in an internal node.
    Each of these nodes can have two links one represents the internal node which is smaller than its key (left)
    and one that represents the greater than internal node (right)
     */
    private String id;
    private String description;
    private InternalNode left;
    private InternalNode right;

    //basic constructor for creating a node object with predefined parameters
    public Key(String id, String description){
        this.id = id;
        this.description = description;
        //this.left = left;
        //this.right = right;
    }

    //default constructor
    public Key()
    {
        id = null;
        left = null;
        right = null;
    }

    //Bellow are the three setters for different node parameters for editing a node object
    public void setId(String id)
    {
        this.id = id;
    }
    public void setDescription(String description)
    {
        this.description = description;
    }
    public void setLeft(InternalNode left)
    {
        this.left = left;
    }
    public void setRight(InternalNode right)
    {
        this.right = right;
    }
    public void removeLinks()
    {
        left = null;
        right = null;
    }
    //Bellow are the three getters for different node parameters for editing a node object
    public String getId()
    {return id;}
    public String getDescription()
    {return description;}
    public InternalNode getLeft()
    {return left;}
    public InternalNode getRight()
    {return right;}

}
