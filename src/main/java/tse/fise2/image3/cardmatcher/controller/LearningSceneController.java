package tse.fise2.image3.cardmatcher.controller;




import java.awt.Label;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.event.ActionEvent;

import javafx.scene.image.ImageView;

import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import tse.fise2.image3.cardmatcher.model.Camera;
import tse.fise2.image3.cardmatcher.model.CameraLearning;
import tse.fise2.image3.cardmatcher.util.FileUtil;
import tse.fise2.image3.cardmatcher.util.MsgUtil;


public class LearningSceneController {
    @FXML
    private Button start_btn;
    @FXML
    private Button back_btn;

    @FXML
    private ImageView learningFrame;
    @FXML
    private ImageView menuFrame;
    @FXML
    private Label lab;

    public Camera capture1 = new CameraLearning();

    // Event Listener on Button[#start_btn].onAction
    @FXML
    public void startCamera(ActionEvent event) throws InterruptedException {
        boolean learningmode = true;
        capture1.setLearningmode(learningmode);
        capture1.openCamera(learningFrame,start_btn);
    }


    public void back(ActionEvent actionEvent)  throws IOException{
        capture1.setCameraActive(false);
        // stop the timer
        capture1.stopAcquisition();
        System.out.println(getClass().getResource("view/Menu.fxml"));
        Parent backLoader = FXMLLoader.load(getClass().getResource("view/Menu.fxml"));

        back_btn.getScene().setRoot(backLoader);


    }

    public void gotest(ActionEvent actionEvent)  throws IOException{
        capture1.setCameraActive(false);
        // stop the timer
        capture1.stopAcquisition();

        Parent backLoader = FXMLLoader.load(getClass().getResource("view/TestScene.fxml"));

        back_btn.getScene().setRoot(backLoader);
    }
    public void importdatabase(ActionEvent actionEvent){
        try {

            Stage primaryStage = new Stage();
            DirectoryChooser directoryChooser = new DirectoryChooser();

            directoryChooser.setInitialDirectory(new File("src"));


            File selectedDirectory = directoryChooser.showDialog(primaryStage);
//            System.out.println(selectedDirectory.getAbsolutePath());
            FileUtil.copyfolder(selectedDirectory.getAbsolutePath());
            MsgUtil.DisplayMsg("Import success !");
        }
        catch (Exception e)
        {
            MsgUtil.DisplayMsg("Import failed !");
        }
    }
    public void about(ActionEvent actionEvent){       
    	Stage stage = new Stage();
    	
    	//Creating a Text object 
    	Text text = new Text();      

    	//Setting the text to be added. 
    	text.setText("Hello how are you"); 

    	//setting the position of the text 
    	text.setX(50); 
    	text.setY(50); 

    	//Creating a Group object  
    	Group root = new Group(text);   

    	//Creating a scene object 
    	Scene scene = new Scene(root, 600, 300);  

    	//Setting title to the Stage 
    	stage.setTitle("About Learning Mode"); 

    	//Adding scene to the stage 
    	stage.setScene(scene); 

    	//Displaying the contents of the stage 
    	stage.show(); 
    }    
}
