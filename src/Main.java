import com.sun.source.tree.WhileLoopTree;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

    public static void main(String [] args)
    {
        BplusTree tree = new BplusTree();
        Readfile file = new Readfile();
        file.loadFile();
        tree.buildTree(file.keyList);
        menu(tree);

    }
    public static void menu(BplusTree tree) {
        Scanner input = new Scanner(System.in);
        System.out.println("");
        System.out.println("Welcome to the Parts Log");
        System.out.println("Please select one of the following options");
        System.out.println("1. Search for a part");
        System.out.println("2. Add a part");
        System.out.println("3. Update a part");
        System.out.println("4. Delete a part");
        System.out.println("5. Exit");
        int option = input.nextInt();
        switch (option) {
            case 1: {
                System.out.println("please enter part id");
                String id = input.next();
                InternalNode result = tree.findNodeByIntID(id, tree.root);
                /*DONT FORGET TO CHECK IF KEY DOES NOT EXIST IN RETURNED NODE*/
                Boolean found = false;
                for (int i = 0; i < result.keyArrayList.size(); i++) {
                    if (result.keyArrayList.get(i).getId().equals(id)) {
                        found = true;
                        System.out.println("Part Information");
                        System.out.println(result.keyArrayList.get(i).getId() + " " + result.keyArrayList.get(i).getDescription());
                    }
                }
                if (found) {
                    System.out.println("Next 10 parts");
                    int index = tree.leafList.indexOf(result);
                    printNextTen(index,id, tree);
                } else {
                    System.out.println("Part not found");
                }

                menu(tree);
            }
            case 2: {
                System.out.println("Please enter part id");
                String id = input.next();
                System.out.println("Please enter part description");
                String description = input.next();

                Key key = new Key(id, description);
                tree.insert(tree.root, key);
                System.out.println("part has been inserted");
                menu(tree);
            }
            case 3: {
                System.out.println("Please enter part id");
                String id = input.next();
                InternalNode result = tree.findNodeByIntID(id, tree.root);
                for (int i = 0; i < result.keyArrayList.size(); i++) {
                    if (result.keyArrayList.get(i).getId().equals(id)) {
                        System.out.println("Part Information");
                        System.out.println(result.keyArrayList.get(i).getId() + " " + result.keyArrayList.get(i).getDescription());
                        System.out.println("Please enter updated part description");
                        String description2 = input.next();
                        System.out.println("Part information will be updated to");
                        result.keyArrayList.get(i).setDescription(description2);
                        System.out.println(id + " " + description2);
                    }
                }
                menu(tree);
            }
            case 4: {
                System.out.println("Please enter part id");
                String id = input.next();
                tree.delete(id, null, null, false);
                menu(tree);
            }
            case 5: {
                System.out.println("Would you like to save changes? (Y/N)");
                String choice = input.next();
                if (choice.equals("Y")) {
                    //write to leaf list to file
                    saveChanges(tree);
                }
                else
                {
                    System.exit(0);
                }
            }
        }
    }
    private static void printNextTen(int index, String id,BplusTree tree)
    {
        ArrayList<Key> partList = new ArrayList<>();

        for(int i=index;i<tree.leafList.size();i++)
        {
            for (int j=0;j<tree.leafList.get(i).keyArrayList.size();j++)
            {
                partList.add(tree.leafList.get(i).keyArrayList.get(j));
            }
        }

        //now that there is a list of all parts
        int startKey = 0;
        for (int i = 0; i < partList.size(); i++) {
            if (partList.get(i).getId().equals(id))
            {
                startKey = i +1;
            }
        }

        //finally print
        try {
            int count = 0;
            while(count !=10)
            {
                Key key = partList.get(startKey + count);
                System.out.println(key.getId() + " " + key.getDescription());
                count++;
            }
        }
        catch (Exception e)
        {

        }

    }

    private static void saveChanges(BplusTree tree)
    {
        String filename = System.getProperty("user.dir")+"\\"+String.valueOf(Paths.get(".","src","Resources", "test1.txt").normalize().toFile());
        ArrayList<Key> partList = new ArrayList<>();

        for(int i=0;i<tree.leafList.size();i++)
        {
            for (int j=0;j<tree.leafList.get(i).keyArrayList.size();j++)
            {
                partList.add(tree.leafList.get(i).keyArrayList.get(j));
            }
        }

        try{
            FileWriter writer = new FileWriter(filename);
            for (int i=0;i<partList.size();i++)
            {
                writer.append(partList.get(i).getId() + "        " + partList.get(i).getDescription() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

