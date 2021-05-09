import java.util.ArrayList;
import java.util.LinkedList;

public class BplusTree {
    ArrayList<InternalNode> nodeList = new ArrayList<>();
    public InternalNode root = null;
    public LinkedList<InternalNode> leafList = new LinkedList<>();

    /*To do
     * -add more stuff to the test file to make sure recursive parent splitting works
     * -start linking query function to UI*/
    //Linked list declaration will go here
    public BplusTree()
    {

    }

    public void buildTree(ArrayList<Key> keyList)
    {
        //Tree will be constructed on instance creation

        for(int i = 0; i<keyList.size();i++)
        {
            if(root == null)
            {
                InternalNode node = new InternalNode();
                node.addKey(keyList.get(i));
                root = node;
                nodeList.add(root);
                leafList.add(root);
            }
            else
            {
                insert(root, keyList.get(i));
            }
        }
        //treeToString();
        //printLeafList();
        //System.out.println( nodeList.get(nodeList.size()-3)+ " parent is: "+findParent(nodeList.get(nodeList.size()-2), 0));
        //Key key = nodeList.get(nodeList.size()-1).keyArrayList.get(0);
        //System.out.println("key: "+ key.getId() + "has link to: "+ key.getLeft());
    }

    public void printLeafList()
    {
        for(int i =0; i< leafList.size();i++)
        {
            System.out.println("");
            System.out.println("Node: " + leafList.get(i));
            for(int j =0; j< leafList.get(i).keyArrayList.size();j++)
            {
                System.out.print(leafList.get(i).keyArrayList.get(j).getId() + ", ");
            }
        }
    }

    public void treeToString()
    {
        for(int i =0; i< nodeList.size();i++)
        {
            System.out.println("");
            System.out.println("Node: " + nodeList.get(i));
            for(int j =0; j< nodeList.get(i).keyArrayList.size();j++)
            {
                System.out.print(nodeList.get(i).keyArrayList.get(j).getId() + ", ");
            }
            System.out.println( nodeList.get(i)+ " parent is: "+findParent(nodeList.get(i), 0));
        }
    }

    public void leafListToString()
    {
        System.out.println("");
        for (int i=0; i< leafList.size();i++)
        {
            System.out.println("");
            System.out.println("leaf: " + leafList.get(i));
            for(int j =0; j< leafList.get(i).keyArrayList.size();j++)
            {
                System.out.print(leafList.get(i).keyArrayList.get(j).getId() + ", ");

            }
        }
    }
    public void insert(InternalNode root,Key key)
    {
        //Step 1, call the findInsertPosition method, pass it the global root and the key
        InternalNode insertNode = findInsertNode(key, root);
        int nodeIndx = nodeList.indexOf(insertNode);
        //step2: once we have the node that we need to insert the key into, check if that node is empty
        //If node is empty, add that key into that node object's, keyarraylist
        if (insertNode.keyArrayList.size() != 4)
        {
            insertNode.addKey(key);
            sortKeys(insertNode);
            nodeList.set(nodeIndx,insertNode); //update the node in the nodelist
            System.out.println("inserting: " + key.getId());
        }
        // else, call split, pass it the node that is full and the key we need to insert.
        else
        {
            System.out.println("splitting for key " + key.getId());
            split(insertNode, key,false);
        }
    }

    public InternalNode sortKeys(InternalNode node)
    {
        //will eventually be using a modified version of selection sort to deal with alphanumerics
        int size = node.keyArrayList.size();

        for (int i=0;i< size-1;i++)
        {
            int mIndex = i;
            for (int j =i+1; j< size; j++)
            {
                Boolean result = compareIds(node.keyArrayList.get(j),node.keyArrayList.get(mIndex));
                if(result)
                {
                    mIndex = j;
                }
            }
            Key temp = node.keyArrayList.get(mIndex);
            node.keyArrayList.set(mIndex,node.keyArrayList.get(i));
            node.keyArrayList.set(i,temp);
        }
        return node;
    }


    //index here should start as the size of the nodelist array
    public InternalNode findParent(InternalNode node, int index)
    {
        if(index == nodeList.size())
        {
            return null;
        }
        InternalNode currentNode = nodeList.get(index);

        for(int i=0; i<currentNode.keyArrayList.size();i++)
        {
            InternalNode leftLink = currentNode.keyArrayList.get(i).getLeft();
            InternalNode rightLink = currentNode.keyArrayList.get(i).getRight();
            if( leftLink==node || rightLink == node)
            {
                return currentNode;
            }
        }

        return findParent(node,index + 1);
    }

    //returns true if key1 is smaller than key2
    public Boolean compareIds(Key key1,Key key2)
    {
        //get the id of key1 and split into alphabetic and numeric values
        String[] id1_splitResult = key1.getId().split("-");
        String id1_alphabetical = id1_splitResult[0];
        int id1_numeric = Integer.parseInt(id1_splitResult[1]);

        //get the id of key2 and split into alphabetic and numeric values
        String[] id2_splitResult = key2.getId().split("-");
        String id2_alphabetical = id2_splitResult[0];
        int id2_numeric = Integer.parseInt(id2_splitResult[1]);

        int result =id1_alphabetical.compareTo(id2_alphabetical);
        if(result == 0) //ids word portion have equal alphabetical ordering
        {
            //compare their numerical id portions
            if(id1_numeric < id2_numeric)
            {
                //id1 is less than id2
                return true;
            }
            else if(id1_numeric == id2_numeric)
            {
                //if they are also equal just assume id1 is first
                return false;
            }
            else {
                return false;
            }
        }
        else if (Integer.signum(result) == -1)//id1 is less than id2
        {
            return true;//no need to compare numericals
        }
        else //id2 is greater than id1
        {
            return false;
        }

    }

    public InternalNode findInsertNode(Key key, InternalNode node)
    {
        //this method return the node where the key needs to be inserted

        //if there is only one node in the tree, then return root
        if(nodeList.size() == 1)
        {
            System.out.println("just root exists");
            return node;
        }
        else
        {
            //get list of keys from node, check each key against the key we need to insert
            for(int i=0; i<node.keyArrayList.size();i++)
            {
                Key comparisonKey = node.keyArrayList.get(i);

                Boolean result = compareIds(key,comparisonKey);
                //if key is smaller than key inside the internal node

                if (result == true)
                {
                    //first check if that key has a left link to continue traversal
                    if (comparisonKey.getLeft() != null)
                    {
                        return findInsertNode(key, comparisonKey.getLeft()); //continue traversal down left branch
                    }
                    else
                    {
                        //no left links means that key belongs in the current internalnode
                        return node;
                    }
                }
                //if it is greater continue through the loop until we find any key it is less than
            }
            //if loop completes and there is nothing key is less than, see if the last key has a right link to continue traversal
            InternalNode nextNode = node.keyArrayList.get(node.keyArrayList.size()-1).getRight();
            if (nextNode != null)
            {
                return findInsertNode(key, nextNode);
            }
        }
        //if no right link to continue down right branch, than we must insert key into current node at end
        return node;
    }

    public InternalNode findNodeByIntID(String inputID, InternalNode node) {

        InternalNode currentNode = node;
        Key keytemp = new Key();
        keytemp.setId(inputID);//creating a temporary key to reuse the compareids method
        //this method return the node where the key needs to be inserted

        //if there is only one node in the tree, then return root
        if(nodeList.size() == 1)
        {
            System.out.println("just root exists");
            return node;
        }
        else
        {
            //get list of keys from node, check each key against the key we need to insert
            for(int i=0; i<node.keyArrayList.size();i++)
            {
                Key comparisonKey = node.keyArrayList.get(i);
                Boolean result = compareIds(keytemp,comparisonKey);
                //if key is smaller than key inside the internal node
                if (result)
                {
                    //first check if that key has a left link to continue traversal
                    if (comparisonKey.getLeft() != null)
                    {
                        return findNodeByIntID(inputID, comparisonKey.getLeft()); //continue traversal down left branch
                    }
                    else
                    {
                        //no left links means that key belongs in the current internalnode
                        return node;
                    }
                }
                //if it is greater continue through the loop until we find any key it is less than
            }
            //if loop completes and there is nothing key is less than, see if the last key has a right link to continue traversal
            InternalNode nextNode = node.keyArrayList.get(node.keyArrayList.size()-1).getRight();
            if (nextNode != null)
            {
                return findNodeByIntID(inputID, nextNode);
            }
        }
        //if no right link to continue down right branch, than we must insert key into current node at end
        return node;
    }

    public void split(InternalNode node,Key key, Boolean recurseSplit)
    {
        //int size = nodeList.size();
        InternalNode parent = findParent(node,0);
        System.out.println("parent:" + parent);

        if(parent == null)
        {
            System.out.println("here for " + key.getId());
            //construct right bias tree
            node.keyArrayList.add(key);
            sortKeys(node);

            //modify when tree is converted to max 4 keys per node instead
            Key middle = node.keyArrayList.get(2);
            InternalNode nodeleft = new InternalNode();
            InternalNode noderight = new InternalNode();
            int size = node.keyArrayList.size();

            for (int i = 0; i < size; i++)
            {
                System.out.println(node.keyArrayList.get(i).getId());
                Boolean result = compareIds(node.keyArrayList.get(i),middle);
                if (result)
                {
                    if (recurseSplit)
                    {
                        nodeleft.addKey(node.keyArrayList.get(i));
                    }
                    else
                    {
                        Key keytemp = node.keyArrayList.get(i);
                        keytemp.setId(node.keyArrayList.get(i).getId());
                        keytemp.setDescription(node.keyArrayList.get(i).getDescription());
                        nodeleft.addKey(keytemp);
                    }

                }
                else
                {
                    if (recurseSplit)
                    {
                        noderight.addKey(node.keyArrayList.get(i));
                    }
                    else
                    {
                        Key keytemp = new Key();
                        keytemp.setId(node.keyArrayList.get(i).getId());
                        keytemp.setDescription(node.keyArrayList.get(i).getDescription());
                        noderight.addKey(keytemp);
                    }
                }
            }
            node.keyArrayList.clear();
            node.addKey(middle);

            if(middle.getLeft() !=null && middle.getRight() !=null) //meaning we are currently in a recursive split
            {
                //noderight.keyArrayList.remove(0);
                nodeleft.keyArrayList.get(node.keyArrayList.size()-1).setRight(middle.getLeft());
                noderight.keyArrayList.get(0).setLeft(middle.getRight());
                //System.out.println("node left in right sub is: "+ middle.getLeft().keyArrayList.get(0).getRight());
            }
            noderight = sortKeys(noderight);
            node.keyArrayList.get(0).setLeft(nodeleft);
            node.keyArrayList.get(0).setRight(noderight);
            nodeList.add(1,nodeleft);
            nodeList.add(2,noderight);
            if (nodeleft.isLeaf())
            {
                if (!leafList.contains(nodeleft))
                {
                    leafList.add(nodeleft);
                }
            }
            if (noderight.isLeaf())
            {
                if (!leafList.contains(noderight)) {
                    leafList.add(noderight);
                }
            }
            if (!node.isLeaf())
            {
                leafList.remove(node);
            }
        }
        else
        {
            if(parent.keyArrayList.size() != 4)
            {
                //construct right bias tree
                node.keyArrayList.add(key);
                sortKeys(node);

                //modify when tree is converted to max 4 keys per node instead
                Key middle = new Key();
                if(recurseSplit)
                {
                    middle = node.keyArrayList.get(2);
                }
                else
                {
                    middle.setId(node.keyArrayList.get(2).getId());
                    middle.setDescription(node.keyArrayList.get(2).getDescription());
                }

                InternalNode nodeleft = new InternalNode();
                System.out.println("middle is: " + middle.getId());
                int size = node.keyArrayList.size();

                int count =0;
                for (int i = 0; i < size; i++)
                {
                    System.out.println(node.keyArrayList.get(i).getId());
                    Boolean result = compareIds(node.keyArrayList.get(i),middle);
                    if (result)
                    {
                        nodeleft.addKey(node.keyArrayList.get(i));
                        count =i;
                    }
                }
                node.keyArrayList.subList(0,count+1).clear();
                middle.setLeft(nodeleft);
                middle.setRight(node);
                //System.out.println("Key: "+ key.getId() + " right link is: " + node);
                parent.keyArrayList.add(middle);
                int location = parent.keyArrayList.indexOf(middle) -1;
                parent.keyArrayList.get(location).setRight(null);
                int index = nodeList.indexOf(node);
                nodeList.add(index,nodeleft);
                //System.out.println(parent.keyArrayList.get(0).getRight());
                if (nodeleft.isLeaf())
                {
                    if (!leafList.contains(nodeleft)) {
                        int insertPosition = leafList.indexOf(node);
                        leafList.add(insertPosition, nodeleft);
                        System.out.println("here1");
                    }
                }
                if (node.isLeaf())
                {
                    if (!leafList.contains(node)) {
                        leafList.add(node);
                        System.out.println("here2");
                    }
                }
                else
                {
                    //leafList.remove(node);
                }

            }
            else
            {
                //split parent continue up tree if needed
                //do initial abstract split, send to split parent
                //construct right bias tree
                node.keyArrayList.add(key);
                sortKeys(node);

                //modify when tree is converted to max 4 keys per node instead
                Key middle = new Key();
                if (recurseSplit)
                {
                    middle = node.keyArrayList.get(2);
                }
                else
                {
                    middle.setId(node.keyArrayList.get(2).getId());
                    middle.setDescription(node.keyArrayList.get(2).getDescription());
                }

                InternalNode nodeleft = new InternalNode();
                int size = node.keyArrayList.size();

                int count =0;
                for (int i = 0; i < size; i++)
                {
                    System.out.println(node.keyArrayList.get(i).getId());
                    Boolean result = compareIds(node.keyArrayList.get(i),middle);
                    if (result)
                    {
                        nodeleft.addKey(node.keyArrayList.get(i));
                        count =i;
                    }
                }
                node.keyArrayList.subList(0,count+1).clear();
                middle.setLeft(nodeleft);
                middle.setRight(node);
                //System.out.println("recursive split right: " + node);

                //nodeList.add(nodeleft);
                //nodeList.add(node);
                split(parent,middle,true);
                int index = nodeList.indexOf(node);
                nodeList.add(index,nodeleft);
                if (nodeleft.isLeaf())
                {
                    if (!leafList.contains(nodeleft)) {
                        int insertPosition = leafList.indexOf(node);
                        leafList.add(insertPosition,nodeleft);
                    }
                }
            }
        }

    }

    public int getIndex(String id, InternalNode node)
    {
        int index = -1;
        for(int i =0;i<node.keyArrayList.size();i++)
        {
            if (node.keyArrayList.get(i).getId().equals(id))
            {
                index = i;
                break;
            }
        }
        return index;
    }

    public String delete(String id, InternalNode recDelete, InternalNode child, boolean recursion)
    {
        InternalNode node =  new InternalNode();
        //Method handles if node only exists in leaf
        if (recursion == false)
        {
            node = findNodeByIntID(id,root); //traverse tree to find leaf node that contains string
        }
        else
        {
            node = recDelete; //traverse tree to find leaf node that contains string
        }
        int index = getIndex(id,node);//sanity check to make sure, contains key we are looking for.

        InternalNode parent = findParent(node, 0);
        if (index > -1 ) //node contains id the user has inputted.
        {
            if (node.keyArrayList.size() > 2)// if removal wont violate min key rules best case scenario
            {
                Key keyToRemove = node.keyArrayList.get(index);
                InternalNode rightLink = keyToRemove.getLeft();
                node.keyArrayList.remove(index);//remove key from node
                if(recursion)
                {
                    Key moveKey = new Key();
                    moveKey.setId(child.keyArrayList.get(0).getId());
                    moveKey.setLeft(rightLink);
                    moveKey.setRight(child);
                    node.keyArrayList.add(moveKey);
                    sortKeys(node);
                    int deleteIndex = nodeList.indexOf(child);
                    //child.keyArrayList.remove(0);
                    if (child.keyArrayList.size() == 0)
                    {
                        nodeList.remove(deleteIndex);
                    }
                }
                //find key anywhere else in the tree
                if (recursion != true)
                {
                    InternalNode internalLocation = keyExists(id,root);
                    Boolean keyExists =  false;
                    for (int i =0; i< internalLocation.keyArrayList.size();i++)
                    {
                        if(internalLocation.keyArrayList.get(i).getId().equals(id))//if there is a match
                        {
                            keyExists = true;
                        }
                    }
                    if(keyExists)
                    {
                        return delete(id, internalLocation, node, true);
                    }
                }
            }
            else
            {
                //call merge method
                merge(id, node,parent);
            }
        }
        return "deleted succesfully";
    }

    public void merge(String id, InternalNode node, InternalNode parent)
    {
        //get immediate siblings
        if (parent == null)
        {
            Key newRootKey = new Key();
            newRootKey.setId(findMinimumRightSub(node.keyArrayList.get(node.keyArrayList.size()-1).getRight()).keyArrayList.get(0).getId());//get minimum value in right sub tree
            InternalNode leftNode = node.keyArrayList.get(0).getLeft();//preserve left subtree link
            InternalNode rightNode = node.keyArrayList.get(0).getRight();//preserve right subtree link
            node.keyArrayList.remove(0); //remove old root key
            newRootKey.setLeft(leftNode);
            newRootKey.setRight(rightNode);
            node.keyArrayList.add(newRootKey);
        }
        else
        {
            ArrayList<InternalNode> siblings = getSiblingsList(node);
            //check if siblings can donate
            Boolean canDonate = false;
            int siblingindex = -1;
            for (int i =0; i< siblings.size(); i++)
            {
                if(siblings.get(i).keyArrayList.size() > 2)
                {
                    canDonate = true;
                    siblingindex = i;
                    break;
                }
            }

            if (canDonate)
            {

                //check what sibling is donating, front or back
                if(Integer.parseInt(siblings.get(siblingindex).keyArrayList.get(0).getId()) < Integer.parseInt(node.keyArrayList.get(0).getId()))
                {
                    //donating sibling is left
                    Key donatingkey = siblings.get(siblingindex).keyArrayList.get(siblings.get(siblingindex).keyArrayList.size()-1);//get maximum key
                    InternalNode right = node.keyArrayList.get(0).getRight();
                    node.keyArrayList.remove(0);
                    siblings.get(siblingindex).keyArrayList.remove(siblings.get(siblingindex).keyArrayList.size()-1);
                    node.keyArrayList.add(donatingkey);
                    System.out.println(parent.keyArrayList.get(0).getId());
                    int index = getIndex(id,parent);
                    System.out.println(index);
                    boolean recursive = false;
                    if(index == -1)
                    {
                        siblings.get(siblingindex).keyArrayList.get(siblings.get(siblingindex).keyArrayList.size()-1).setRight(donatingkey.getLeft());
                        donatingkey.setLeft(null);
                        donatingkey.setLeft(right);
                        String parentID = parent.keyArrayList.get(0).getId();
                        parent.keyArrayList.get(0).setId(donatingkey.getId());
                        int location = getIndex(donatingkey.getId(),node);
                        node.keyArrayList.get(location).setId(parentID);
                        recursive = true;
                    }
                    else
                    {
                        parent.keyArrayList.get(index).setId(donatingkey.getId());
                    }


                    InternalNode internalLocation = keyExists(id,root);
                    Boolean keyExists =  false;
                    for (int i =0; i< internalLocation.keyArrayList.size();i++)
                    {
                        if(internalLocation.keyArrayList.get(i).getId().equals(id))//if there is a match
                        {
                            keyExists = true;
                        }
                    }
                    if(keyExists)
                    {
                        delete(id, internalLocation, node, true);
                    }
                }
                else
                {
                    //donating sibling is right
                    Key donatingkey = siblings.get(siblingindex).keyArrayList.get(0);//get minimum key
                    node.keyArrayList.remove(0);
                    siblings.get(siblingindex).keyArrayList.remove(0);
                    node.keyArrayList.add(donatingkey);
                    int index = getIndex(donatingkey.getId(),parent);
                    parent.keyArrayList.get(index).setId(siblings.get(siblingindex).keyArrayList.get(0).getId());

                    InternalNode internalLocation = keyExists(id,root);
                    Boolean keyExists =  false;
                    for (int i =0; i< internalLocation.keyArrayList.size();i++)
                    {
                        if(internalLocation.keyArrayList.get(i).getId().equals(id))//if there is a match
                        {
                            keyExists = true;
                        }
                    }
                    if(keyExists)
                    {
                        delete(id, internalLocation, node, true);
                    }
                }
            }
            else//nothing can donate
            {
                //MAKE SURE TO MOVE REMAINING VALUES OVER FOR CONVERSION
                //check if we are merging with left or right
                if (Integer.parseInt(siblings.get(0).keyArrayList.get(0).getId()) < Integer.parseInt(id))
                {
                    //we are merging with the left sibling
                    nodeList.remove(node);//remove empty node
                    String idToDelete = siblings.get(0).keyArrayList.get(0).getId();
                    System.out.println("id to delete is: " + idToDelete);
                    InternalNode internalLocation = keyExists(id,root);
                    Boolean keyExists =  false;

                    int keyInParent = getIndex(id, parent);
                    parent.keyArrayList.get(keyInParent).setRight(siblings.get(0));
                    //parent.keyArrayList.remove(deleteLocation);
                    //parent.keyArrayList.get(0).getLeft()siblings.get(0)
                    //parent.keyArrayList.add(siblings.get(0).keyArrayList.get(0));
                    //internalLocation = keyExists(idToDelete,root);


                    for (int i =0; i< internalLocation.keyArrayList.size();i++)
                    {
                        if(internalLocation.keyArrayList.get(i).getId().equals(id))//if there is a match
                        {
                            keyExists = true;
                        }
                    }
                    if(keyExists)
                    {
                        delete(id, internalLocation, node, true);
                    }
                }
                else
                {
                    //we are merging with the right sibling
                    nodeList.remove(node);//remove empty node
                    String idToDelete = siblings.get(0).keyArrayList.get(0).getId();
                    int deleteindex = getIndex(idToDelete,parent);
                    parent.keyArrayList.remove(deleteindex);

                    InternalNode internalLocation = keyExists(id,root);
                    Boolean keyExists =  false;
                    for (int i =0; i< internalLocation.keyArrayList.size();i++)
                    {
                        if(internalLocation.keyArrayList.get(i).getId().equals(id))//if there is a match
                        {
                            keyExists = true;
                        }
                    }
                    if(keyExists)
                    {
                        delete(id, internalLocation, node, true);
                    }
                }
            }
        }
    }

    public InternalNode keyExists(String inputID, InternalNode node)//gets passed root initially
    {
        InternalNode currentNode = node;

        //if there is only one node in the tree, then return root

        if(nodeList.size() == 1)
        {
            return node;
        }
        else
        {
            //get list of keys from node, check each key against the key we need to insert
            for(int i=0; i<node.keyArrayList.size();i++)
            {
                Key comparisonKey = node.keyArrayList.get(i);
                int result = compareIds(inputID,comparisonKey);
                //if key are equal
                if(result == 0)
                {
                    return node;
                }
                if (result == -1)
                {
                    //first check if that key has a left link to continue traversal
                    if (comparisonKey.getLeft() != null)
                    {
                        return keyExists(inputID, comparisonKey.getLeft()); //continue traversal down left branch
                    }
                    else
                    {
                        //no left links means that key belongs in the current internalnode
                        return node;
                    }
                }
                //if it is greater continue through the loop until we find any key it is less than
            }
            //if loop completes and there is nothing key is less than, see if the last key has a right link to continue traversal
            InternalNode nextNode = node.keyArrayList.get(node.keyArrayList.size()-1).getRight();
            if (nextNode != null)
            {
                return keyExists(inputID, nextNode);
            }
        }
        //if no right link to continue down right branch, than we must insert key into current node at end
        return node;
    }

    public InternalNode findMinimumRightSub(InternalNode node)
    {
        //takes an internal node as a parameter and returns the minimum leaf in its right sub tree
        if(nodeList.size() == 1)
        {
            return node;
        }
        if(node.keyArrayList.get(0).getLeft() != null)//continue traversing
        {
            return node.keyArrayList.get(0).getLeft();
        }

        return node;
    }

    public ArrayList<InternalNode> getSiblingsList(InternalNode node)
    {
        ArrayList<InternalNode> temp = new ArrayList<>();
        ArrayList<InternalNode> siblings = new ArrayList<>();
        InternalNode parent = findParent(node, 0);
        Key Key = node.keyArrayList.get(0);
        for (int i=0;i<parent.keyArrayList.size();i++) {
            InternalNode leftlink = parent.keyArrayList.get(i).getLeft();
            InternalNode rightlink = parent.keyArrayList.get(i).getRight();
            if (leftlink != null)
            {
                temp.add(leftlink);
            }
            if (rightlink != null)
            {
                temp.add(rightlink);
            }
        }
        int index = temp.indexOf(node);
        if (index == 0) //if node we are trying to find siblings for is first, only has right sibling
        {
            if(temp.size() > 1)
            {
                siblings.add(temp.get(index +1));
            }

        }
        else if(index == temp.size()-1)//if node we are trying to find siblings for is last, only has left sibling
        {
            if(temp.size() > 1)
            {
                siblings.add(temp.get(index -1));
            }

        }
        else //if its in the middle
        {
            siblings.add(temp.get(index -1));
            siblings.add(temp.get(index +1));
        }
        return siblings;
    }

    public int compareIds(String id,Key key2)
    {
        //get the id of key1 and split into alphabetic and numeric values
        String[] id1_splitResult = id.split("-");
        String id1_alphabetical = id1_splitResult[0];
        int id1_numeric = Integer.parseInt(id1_splitResult[1]);

        //get the id of key2 and split into alphabetic and numeric values
        String[] id2_splitResult = key2.getId().split("-");
        String id2_alphabetical = id2_splitResult[0];
        int id2_numeric = Integer.parseInt(id2_splitResult[1]);

        int result =id1_alphabetical.compareTo(id2_alphabetical);
        if(result == 0) //ids word portion have equal alphabetical ordering
        {
            //compare their numerical id portions
            if(id1_numeric < id2_numeric)
            {
                //id1 is less than id2
                return -1;
            }
            else if(id1_numeric == id2_numeric)
            {
                //if they are also equal just assume id1 is first
                return 0;
            }
            else {
                return 1;
            }
        }
        else if (Integer.signum(result) == -1)//id1 is less than id2
        {
            return -1;//no need to compare numericals
        }
        else //id2 is greater than id1
        {
            return 1;
        }

    }
}
