package mytunes.GUI;

import java.net.URL;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mytunes.BE.Songs;
/**
 *
 * @author Abdilk
 */
public class MyTunes extends Application
{
    
    @Override
    public void start(Stage pStage) throws Exception
    {
        pStage.setTitle("DankTunes");
        pStage.centerOnScreen();
        
        
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("/mytunes/GUI/view/MainWindow.fxml")); //getClassLoader added to avoid NullPointerException

        
        Scene scene = new Scene(root);
        
        pStage.setScene(scene);
        pStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
        
     
    }
    
}