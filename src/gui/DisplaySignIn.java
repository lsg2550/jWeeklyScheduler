package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import utils.io.IO;
import utils.io.Save;

/**
 *
 * @author Luis
 */
class DisplaySignIn {

    private static Scene scene;

    protected static void init() {
        //Root
        VBox root = new VBox(5);
        root.setAlignment(Pos.CENTER);

        //Form
        GridPane userForm = new GridPane();
        userForm.setHgap(2.0);
        userForm.setHgap(2.0);
        userForm.setAlignment(Pos.CENTER);

        //TextField
        TextField userIDInput = new TextField();
        PasswordField userPassInput = new PasswordField();

        //Button
        Button userSubmit = new Button("Sign-In");
        userSubmit.setOnAction((ActionEvent e) -> {
            if (new File(IO.getDOCS().toString() + "/" + userIDInput.getText() + ".txt").exists()) {
                File userFile = new File(IO.getDOCS().toString() + "/" + userIDInput.getText() + ".txt");

                try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
                    boolean userCheck = false, passCheck = false;

                    for (String line = br.readLine(); line != null; line = br.readLine()) {
                        if (line.contains("ID: ")) {
                            line = line.replace("ID: ", " ").trim();
                            if (line.equals(userIDInput.getText())) {
                                userCheck = true;
                            }

                            //System.out.println("User ID: " + line); //Logging
                        }
                        if (line.contains("PW: ")) {
                            line = line.replace("PW: ", " ").trim();
                            if (line.equals(userPassInput.getText())) {
                                passCheck = true;
                            }

                            //System.out.println("Password: " + line); //Logging
                        }
                    }

                    br.close(); //Reading is done.
                    if (userCheck && passCheck) {
                        Save.loadInfo(userFile);
                        DisplayTableView.addData();
                        System.out.printf("Welcome, %s.", Save.currentUser.getFirstName()); //Logging

                        DisplayStage.close();
                    } else {
                        DisplayWarning.show("Incorrect Login Information!");
                        //System.out.println("Incorrect Login Information!");
                    }
                } catch (IOException ex) {
                    DisplayWarning.show("Could Not Read From File!");
                    //System.out.println("Could Not Read From File!");
                }
            } else {
                DisplayWarning.show("User doesn't exist or password was entered incorrectly.");
                //System.out.println("User doesn't exist or password was entered incorrectly.");
            }

            //Clear Text Fields
            userIDInput.setText("");
            userPassInput.setText("");
        });

        //Warning Label
        Label userWarning = new Label("If you're already signed in, you will be logged out without saving!");
        userWarning.setTextFill(Color.RED);
        userWarning.setTextAlignment(TextAlignment.CENTER);
        userWarning.setWrapText(true);
        userWarning.setFont(Font.font(null, FontWeight.BOLD, 10));

        //Children
        userForm.addColumn(0, new Label("Student ID: "), new Label("Password: "));
        userForm.addColumn(1, userIDInput, userPassInput);
        root.getChildren().addAll(userForm, userSubmit, userWarning);
        scene = new Scene(root, 250, 175);
    }

    protected static Scene getScene() {
        return scene;
    }

}
