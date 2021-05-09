import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Readfile {
    public ArrayList<Key> keyList = new ArrayList<>();
    public void loadFile() {

        String filename = System.getProperty("user.dir")+"\\"+String.valueOf(Paths.get(".","src","Resources", "test.txt").normalize().toFile());

        try {
            List partsList = readPartsList(filename);
            //partsList.forEach(System.out::println);
            //System.out.println(keyList.get(2).getId());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private List readPartsList(String fileName) throws IOException {

        List<String> partsInventory = new ArrayList<>();

        BufferedReader br = null;

        try {

            br = new BufferedReader(new FileReader(fileName));

            String line;

            while ((line = br.readLine()) != null) {
                Key key = new Key();
                //String partID = ("PartID: ") + line.substring(0,8);
                String partID = line.substring(0,7);
                key.setId(partID);
                String partDesc =line.substring(15);
                key.setDescription(partDesc);
                //partsInventory.add(partID + partDesc );
                partsInventory.add(partID);
                keyList.add(key);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }

        return partsInventory;
    }
}
