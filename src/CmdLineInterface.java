import filestore.FileStore;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
        while(true)
        {
            System.out.println("\n-------------------\n" +
                    "[r]equest file, [a]dd file, [s]how all files in network, [l]eave chord network");

            char i = scanner.next().charAt(0);
            switch (i)
            {
                case 'r':
                    System.out.println("--- Request file ---");
                    System.out.println("Input filename to request:");
                    String reqFilename = scanner.next();
                    Map<String, List<String>> fileLocations = filestore.requestFile(reqFilename);
                    // TODO print
                    break;
                case 'a':
                    System.out.println("--- Add file ---");
                    System.out.println("Input filename to add:");
                    String addFilename = scanner.next();
                    System.out.println("Input filepath:");
                    String filepath = scanner.next();
                    filestore.addFile(addFilename, filepath);
                    break;
                case 's':
                    System.out.println("--- Show all files ---");
                    Map<String, Integer> fileList = filestore.listFiles();
                    // TODO print
                    break;
                case 'l':
                    System.out.println("--- Leave network ---");
                    // leave network
                    System.out.println("Shutting down node.");
                    return;
                default:
                    System.out.println("!! Invalid input !!");
                    break;
            }

        }
    }
}
