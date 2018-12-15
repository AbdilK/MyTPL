package mytunes.GUI;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javafx.application.Application.launch;
//import org.apache.tika.exception.TikaException;
//import org.xml.sax.ContentHandler;
//import org.apache.tika.parser.mp3.Mp3Parser;
//import org.apache.tika.parser.Parser;
//import org.apache.tika.parser.ParseContext;
//import org.xml.sax.helpers.DefaultHandler;
//import org.apache.tika.metadata.Metadata;
//import org.xml.sax.SAXException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Abdil-K
 */


public class Tester
{

    public static void main(String[] args)
    {
        /*String name = "C:\\Users\\Abdil-K\\Documents\\GitHub\\Music-Player\\Mytunes\\res\\songs\\1. A friend like me ft Dave wave - Electro.mp3";
        Path path = Paths.get(name);
        Path pathR = Paths.get("C:\\Users\\Abdil-K\\Documents\\");
        Path relativePath = pathR.relativize(path);
        System.out.println(relativePath);
        
        try 
        {
            InputStream input = new FileInputStream(new File(name));
            ContentHandler handler = new DefaultHandler();
            Metadata metadata = new Metadata();
            Parser parser = new Mp3Parser();
            ParseContext parseCtx = new ParseContext();
            parser.parse(input, handler, metadata, parseCtx);
            input.close();
            
            String[] metadataNames = metadata.names();
            
            for(String name1 : metadataNames)
            {
                System.out.println(name1 + ": " + metadata.get(name1));
            }

            System.out.println("-----------------------------------------------");
            System.out.println("Title: " + metadata.get("title"));
            System.out.println("SongId: " + metadata.get("xmpDM:songId"));
            System.out.println("Artist: " + metadata.get("xmpDM:artist"));
            System.out.println("Genre: " + metadata.get("xmpDM:genre"));
            System.out.println("Duration: " + metadata.get("xmpDM:duration"));
            System.out.println("ReleaseYear: " + metadata.get("xmpDM:releaseYear"));
            System.out.println("SongPath: " + metadata.get("xmpDM:SongPath"));
        }
        catch (FileNotFoundException ex) 
        {
            ex.printStackTrace();
        }
         catch (IOException ex) 
         {
            ex.printStackTrace();
         }
         catch (SAXException ex) 
         {
            ex.printStackTrace();
         }
         catch (TikaException ex) 
         {
            ex.printStackTrace();
         }
    }
         */
    }
}