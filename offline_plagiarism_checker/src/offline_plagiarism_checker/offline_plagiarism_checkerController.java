/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package offline_plagiarism_checker;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class offline_plagiarism_checkerController implements Initializable {

    private List<File> selectedFile;
    private Label label;
    @FXML
    private MenuBar menu_bar;
    private TextField file_location;
    @FXML
    private Button file_btn;
    @FXML
    private TextArea selected_files;
    private ProgressIndicator progress_tracker;
    @FXML
    private MenuItem closeBtn;
    @FXML
    private Button analyze;
    @FXML
    private BorderPane home;

    private int similarities[][];
    private int filesLength[];
//    Utilities
    @FXML
    private TextArea output_message;
    @FXML
    private Label output_heading;
    @FXML
    private Label output_error;
    private MenuItem help;
    @FXML
    private MenuItem docs;

//       Close platform or program
    @FXML
    public void closeProgram(ActionEvent e) {
        Platform.exit();
    }

//    All Read types
    //    Analyze
    public String readText(int i) throws FileNotFoundException {

        File file = new File(selectedFile.get(i).toString());
        byte[] data;
        String str = "";
        try (FileInputStream fis = new FileInputStream(file)) {
            data = new byte[(int) file.length()];
            fis.read(data);
            str = new String(data);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return str;

    }


//    compare text
    public void compareText(String s, int i, int j) {
        try {
            File file = new File(selectedFile.get(j).toString());
            Scanner myReader = new Scanner(file);
            int word_length = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (s.contains(data)) {
                    word_length += data.length();
                }
            }
            myReader.close();
            similarities[i][j] = word_length;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

//    print similarity
    public void printSimilarity() {
        if (selectedFile.size() > 1) {
            output_error.setVisible(false);
            output_message.setVisible(true);
            output_heading.setVisible(true);
            output_message.setText("");
            int similarity_level = 0;
            for (int i = 0; i < selectedFile.size(); i++) {
                for (int j = 0; j < selectedFile.size(); j++) {
                    if (j > i) {
                        System.out.println(similarities[i][j] + " " + filesLength[i]);
                        int per = (int) ((float) similarities[i][j] / filesLength[i] * 100);
                        if (per > 40) {
                            output_heading.setText("Files with more than 40% similarity");
                            Integer temp = per;
                            output_message.appendText(selectedFile.get(i).getName() + "   AND   " + selectedFile.get(j).getName() + "-----> " + temp.toString() + "%\n");
                            similarity_level = 1;
                        }
                    }

                }
            }
            if (similarity_level == 0) {
                output_error.setVisible(true);
                output_message.setVisible(false);
                output_heading.setVisible(false);
                output_error.setText("All files have less similiarity");
                output_error.setStyle("-fx-text-fill: green;");
                analyze.setDisable(true);
            }
        } else {
            output_error.setText("Please choose at least 2 files");
            output_error.setVisible(true);

            output_heading.setVisible(false);
            output_message.setVisible(false);
            analyze.setDisable(true);

        }
    }
    

    @FXML
    public void analyzeClicked() throws FileNotFoundException {
        /**
         * Read the content of the first file
         * Store it in a variable
         * Compare it with the rest of the files
         * Print the Similarity
         */
        String first_file = "";
        if (selectedFile.size() > 0) {
            for (int i = 0; i < selectedFile.size(); i++) {
               
                    first_file = readText(i);
                
                for (int j = 0; j < selectedFile.size(); j++) {
                    if (i != j) {
                         compareText(first_file, i, j);
                    }
                }
            }
            printSimilarity();
        } else {
            output_error.setText("Please choose at least 2 files");
            output_error.setVisible(true);
            output_heading.setVisible(false);
            output_message.setVisible(false);
            analyze.setDisable(true);

        }

    }

    /**
     * If file chooser button is clicked
     * @param e
     * @throws IOException 
     */
//    file chooser
    @FXML
    public void selectFiles(ActionEvent e) throws IOException {
        /**
         * FileChooser object allows the user to choose files
         */
        FileChooser fileChooser = new FileChooser();
        
        /**
         * The fileChooser object method getExtensonFilters 
         * specifies that allowed file extensions
         */
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*"),
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("HTML Files", "*.html"),
                new FileChooser.ExtensionFilter("Java Files", "*.java"),
                new FileChooser.ExtensionFilter("Python Files", "*.py"),
                new FileChooser.ExtensionFilter("Javascript Files", "*.js"),
                new FileChooser.ExtensionFilter("Cascading Style Sheet Files", "*.css")
        );
        
        /**
         * We open the file dialog to choose files
         * Also reset some UI components
         * Selected files will be stored in a file list
         * Finally the selected files is iterated and displayed on the UI
         */
        
        Stage stage = (Stage) file_btn.getScene().getWindow();
        selectedFile = fileChooser.showOpenMultipleDialog(stage);
        selected_files.setText("");
        int len = selectedFile.size();
        filesLength = new int[selectedFile.size()];
        for (int i = 0, num = 1; i < selectedFile.size(); i++, num++) {
            Integer n = num;
            selected_files.appendText(n.toString() + ". " + selectedFile.get(i).getName() + "\n");
            filesLength[i] = (int) selectedFile.get(i).length();
        }
        similarities = new int[selectedFile.size()][selectedFile.size()];

        for (int i = 0; i < selectedFile.size(); i++) {
            similarities[i][i] = 0;
        }

        if (len > 1) {
            analyze.setDisable(false);
            output_error.setVisible(false);

        } else {
            
            /**
             * If errors are encountered whilst choosing the files like
             * Choosing a one file instead of at least two files
             * Also disable the analyze button in the mean while
             */
            output_error.setText("You need at least 2 files");
            output_error.setVisible(true);
            output_heading.setVisible(false);
            output_message.setVisible(false);
            analyze.setDisable(true);

        }

    }

    @FXML
    public void openDocs() throws URISyntaxException, MalformedURLException, IOException {
       /**
        * This links the application to an external user documentation
        * That is hosted on netlify
        */
        Desktop.getDesktop().browse(new URL("https://festive-nightingale-d5269a.netlify.app/").toURI());

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        /**
         * If the application loads.
         * 1. Remove the visibility of the output heading and error message
         *
         */
        // TODO
        output_heading.setVisible(false);
        output_message.setVisible(false);
        output_message.setEditable(false);
        output_error.setVisible(false);
        selected_files.setEditable(false);

        //            menu item
        Hyperlink link = new Hyperlink("https://festive-nightingale-d5269a.netlify.app/");

    }
}
