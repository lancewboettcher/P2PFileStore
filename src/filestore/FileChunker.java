package filestore;
import java.util.*;
import java.io.*;

/**
 * Created by Mikhail Gundogdu on 6/7/16.
 *
 * FileChunk class for placing 8KB chunks of specified file into a chunk directory.
 */
public class FileChunker {
    //Source: http://stackoverflow.com/questions/19177994/java-read-file-and-split-into-multiple-files
    public static void Chunk(String toChunk) throws Exception {

        RandomAccessFile raf = new RandomAccessFile(toChunk, "r");

        String dir = "./chunks";

        File dirPath = new File(dir);
        if (!dirPath.exists()) {
         File directory = new File(dir);
         if(!directory.mkdir()) {
             System.out.println("Error creating chunk directory. Exiting.");
             return;
         }
     }

     long sourceSize = raf.length();
     long bytesPerSplit = 8 * 1024;
        long numSplits = sourceSize/bytesPerSplit;
        long remainingBytes = sourceSize;
        if(numSplits > 0) { remainingBytes = sourceSize % numSplits; }

     StringTokenizer strTok = new StringTokenizer(toChunk, ".");
     String path = strTok.nextToken();

     String extension = strTok.nextToken();
     StringTokenizer strTok2 = new StringTokenizer(path, "/");
     String fileName = path.substring(path.lastIndexOf("/")+1);

     if(sourceSize > bytesPerSplit) {
        int destIx;
        for(destIx=1; destIx <= numSplits; destIx++) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(dir + "/" + fileName +destIx + '.' + extension));
            readWrite(raf, bw, bytesPerSplit);
            bw.close();
        }

        if(remainingBytes > 0) {
            BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(dir + "/" + fileName + destIx + '.' + extension));
            readWrite(raf, bw, remainingBytes);
            bw.close();
        }


    } else {
        BufferedOutputStream bw = new BufferedOutputStream(new FileOutputStream(dir + "/" + fileName + '.' + extension));
        readWrite(raf, bw, sourceSize);
        bw.close();
    }
        raf.close();
}

private static void readWrite(RandomAccessFile raf, BufferedOutputStream bw, long numBytes) throws IOException {
    byte[] buf = new byte[(int) numBytes];
    int val = raf.read(buf);
    if(val != -1) bw.write(buf);
}
}
