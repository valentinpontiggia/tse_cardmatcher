package tse.fise2.image3.cardmatcher.controller;




import java.awt.Label;

import java.io.File;
import java.io.IOException;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;

import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.event.ActionEvent;

import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import javafx.scene.text.TextFlow;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tse.fise2.image3.cardmatcher.model.Camera;
import tse.fise2.image3.cardmatcher.model.CameraLearning;
import tse.fise2.image3.cardmatcher.util.FileUtil;
import tse.fise2.image3.cardmatcher.util.MsgUtil;
import tse.fise2.image3.cardmatcher.sift.DatabaseDescriptors;
import tse.fise2.image3.cardmatcher.sift.Sift;


public class LearningSceneController {
    @FXML
    private Button start_btn;
    @FXML
    private Button back_btn;
    @FXML
    private MenuItem btn_nav_menu;
    @FXML
    private MenuItem btn_nav_test;
    @FXML
    private ImageView learningFrame;
    @FXML
    private ImageView menuFrame;
    @FXML
    private Label lab;


    public Camera capture1 = new CameraLearning();

    // Event Listener on Button[#start_btn].onAction
    @FXML
    public void startCamera(ActionEvent event) throws InterruptedException, IOException {
        boolean learningmode = true;
        capture1.setLearningmode(learningmode);
        capture1.openCamera(learningFrame,start_btn);
    }


    public void back(ActionEvent actionEvent)  throws IOException{
        capture1.setCameraActive(false);
        // stop the timer
        capture1.stopAcquisition();
        Parent backLoader = FXMLLoader.load(getClass().getResource("view/Menu.fxml"));
        Stage stage = (Stage)((MenuItem) btn_nav_menu).getParentPopup().getOwnerWindow();
        stage.getScene().setRoot(backLoader);


    }

    public void gotest(ActionEvent actionEvent)  throws IOException{
        capture1.setCameraActive(false);
        // stop the timer
        capture1.stopAcquisition();
        Parent backLoader = FXMLLoader.load(getClass().getResource("view/TestScene.fxml"));
        Stage stage = (Stage)((MenuItem) btn_nav_test).getParentPopup().getOwnerWindow();
        stage.getScene().setRoot(backLoader);
    }
    public void importdatabase(ActionEvent actionEvent){
        try {

            Stage primaryStage = new Stage();
            DirectoryChooser directoryChooser = new DirectoryChooser();

            directoryChooser.setInitialDirectory(new File("src"));

            File selectedDirectory = directoryChooser.showDialog(primaryStage);
            if (selectedDirectory!=null) {
                FileUtil.copyfolder(selectedDirectory.getAbsolutePath());
                DatabaseDescriptors.extractAndSaveDescriptors(selectedDirectory.getAbsolutePath());
                MsgUtil.DisplayMsg("Import and learning of the database success !");
            }
        }
        catch (IOException e)
        {
            MsgUtil.DisplayMsg("Import failed !");
        }
    }

    public void importpicture(ActionEvent actionEvent){
        try {

            Stage primaryStage = new Stage();
            FileChooser fileChooser = new FileChooser();

            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile!=null) {
                FileUtil.copyfile(selectedFile.getAbsolutePath());
                File file = new File(selectedFile.getAbsolutePath());
                Mat image = Imgcodecs.imread(file.getAbsolutePath());
                String name = file.getName();
                String updatedname = name.replaceAll(".png", "");
                Sift.saveDescriptor(Sift.getDescriptor(image, updatedname));
                MsgUtil.DisplayMsg("Import and learning of the picture success !");
            }
        }
        catch (IOException e)
        {
            MsgUtil.DisplayMsg("Import failed !");
        }
    }
    public void about(ActionEvent actionEvent) {       
    	Stage stage = new Stage();
    	
    	//Creating a Text object 
    	Text title = new Text(); 
    	Text title2 = new Text();
    	Text title3 = new Text();
    	Text presentationText = new Text();
    	Text grabText = new Text();
    	Text importText = new Text();
    	Group root = new Group();
        
        //Setting the text to be added. 
        title.setText("About learning mode"); 
        title.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title.setX(50); 
        title.setY(50);
        
        presentationText.setText("The CardMatcher learning mode is a mode where you can grab pictures of cards to fill a learning database. This database will be useful to recognize which card you show to the webcam in the test mode.");
        presentationText.setWrappingWidth(500);
        presentationText.setTextAlignment(TextAlignment.JUSTIFY);
        
        presentationText.setX(50); 
        presentationText.setY(90);
        
        title2.setText("Grabbing a picture"); 
        title2.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title2.setX(50); 
        title2.setY(160);
        
        grabText.setText("When you are ready to grab your card, just click on the red button to make your webcam start. Then, just place your card in the red rectangle, and click again on the red button. All that was inside the red rectangle is saved as a picture in your computer. Then a small window appears, to rename your .jpg file writing a new name for it.");
        grabText.setWrappingWidth(500);
        grabText.setTextAlignment(TextAlignment.JUSTIFY);
        
        grabText.setX(50); 
        grabText.setY(190);
        
        title3.setText("Importing files"); 
        title3.setFont(Font.font("arial", FontWeight.BOLD, FontPosture.REGULAR, 20)); 
        //setting the position of the text 
        title3.setX(50); 
        title3.setY(270);
        
        importText.setText("In learning mode, you can import either a learning database or just a picture of a card, instead of grabbing all the pictures of the card one by one. To do that, just click on the 'File' menu button, then 'Import' and finally 'Import picture' or 'Import database'. Choose the file or the folder you want to import and select it.");
        importText.setWrappingWidth(500);
        importText.setTextAlignment(TextAlignment.JUSTIFY);
        
        importText.setX(50); 
        importText.setY(300);
        
        root.getChildren().add(title);
        root.getChildren().add(presentationText);
        root.getChildren().add(title2);
        root.getChildren().add(grabText);
        root.getChildren().add(title3);
        root.getChildren().add(importText);
        //Creating a scene object
        Scene scene = new Scene(root, 600, 400);  
        
        //Setting title to the Stage 
        stage.setTitle("About Learning Mode"); 
           
        //Adding scene to the stage 
        stage.setScene(scene); 
           
        //Displaying the contents of the stage 
        stage.show(); 
     }    
}
