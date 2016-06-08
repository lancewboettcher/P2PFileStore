import filestore.FileStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by Simon on 6/6/16.
 */
public class CmdLineInterface {
    public static void main(String[] args) {
        if(args.length < 2)
        {
            System.out.println("Expect at least 2 arguments.");
            System.out.println("Run as bootstrap: [myAddress] [myPort]");
            System.out.println("Joing existing network: [myAddress] [myPort] [hostAddress] [hostPort]");
            return;
        }
        String myAddress = args[0];
        int myPort = Integer.parseInt(args[1]);
        boolean isBootstrap = args.length == 2;

        FileStore filestore;

        if(isBootstrap)
        {
            // Start new chord network
            filestore = new FileStore(myAddress, myPort, "", null);
            System.out.printf("Create a new chord network with bootstrap node on %s:%d",
                    myAddress, myPort);
        }
        else
        {
            String hostAddress = args[2];
            int hostPort = Integer.parseInt(args[3]);
            // Join existing chord network
            filestore = new FileStore(myAddress, myPort, hostAddress, hostPort);
            System.out.printf("Create node on %s:%d and join network with bootstrap node on %s:%d",
                    myAddress, myPort, hostAddress, hostPort);
        }

        Scanner scanner = new Scanner(System.in);
        boolean shutDown = false;
        while(!shutDown)
        {
            System.out.println("\n-------------------\n" +
                    "[r]equest file, [a]dd file, [s]how all files in network, [l]eave chord network");

            char i = scanner.next().charAt(0);
            switch (i)
            {
                //request file
                case 'r':
                    System.out.println("--- Request file ---");
                    System.out.println("Input filename to request:");
                    String reqFilename = scanner.next();
                    List<String> fileLocations = filestore.requestFile(reqFilename);
                    if(fileLocations.size() > 0)
                    {
                        System.out.println("Locations for " + reqFilename + ":");
                        for(String s: fileLocations)
                            System.out.println("    " + s);
                    }
                    break;
                //add file
                case 'a':
                    System.out.println("--- Add file ---");
                    System.out.println("Input filename to add:");
                    String addFilename = scanner.next();
                    filestore.addFile(addFilename);
                    break;
                //show all files in network
                case 's':
                    System.out.println("--- Show all files ---");
                    Map<String, Integer> fileList = filestore.listFiles();

                    Set<String> keySet = fileList.keySet();

                    final ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();

                    if(keySet.isEmpty())
                        System.out.println("No files in network.");
                    else {
	                    for (String s: keySet) {
	                    	ArrayList<String> row = new ArrayList<String>();
	                    	row.add(s);
	                    	row.add(fileList.get(s).toString());

	                    	table.add(row);
	                    }
	                        //System.out.println("File: " + s + " Number of nodes: " + fileList.get(s));

	                    System.out.format("%-30s%-15s\n", "Filename", "Number of Nodes");
	                    System.out.println("------------------------------");
	                    for (ArrayList<String> row : table) {
	                    	Object[] rowArr = row.toArray();
	                    	System.out.format("%-15s%-15s\n", rowArr);
	                    }
                    }
                    //

                    break;
                //leave chord network
                case 'l':
                    System.out.println("--- Leave network ---");
                    filestore.leaveChordRing();
                    System.out.println("Shutting down node.");
                    shutDown = true;
                    break;
                default:
                    System.out.println("!! Invalid input !!");
                    break;
            }

        }
    }
}
