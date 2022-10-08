import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Password extends Application {
	static Map<String, String> siteToPass = new HashMap<String, String>();
	String site = "";
	String pass = "";
	
	String strongerPass = "Make sure your password is longer than 12 characters, has capital and lowercase letters, and has numbers!"
			+ " Try a new one!";
	String getPassError = "No password with this website exists in the database! To enter a new password, refer to the section on the left.";

	String siteInstrDefault = "Enter the website for which you wanna save your password in; if you already entered a password in for this website, "
			+ "it will be overwritten: ";
	String passInstrDefault = "Enter a strong password: ";
	String getPassDefault = "Enter a website below to access the corresponding password: ";

	Text prompt1 = new Text("Enter a new password: ");
	Text siteInstr = new Text(siteInstrDefault);
	TextField siteField = new TextField();
	Button siteButton = new Button("Enter");

	Text passInstr = new Text("Enter the password for this website: ");
	TextField passField = new TextField();
	Button passButton = new Button("Enter");

	Text prompt2 = new Text("Get a password: ");
	Text getPassInstr = new Text(getPassDefault);
	TextField getPassField = new TextField();
	Button getPassButton = new Button("Enter");
	Text password = new Text();
	
	Button save = new Button("Save before closing the window!");

	Text inputPass = new Text();

	
	HBox hbox1 = new HBox(5);
	HBox hbox2 = new HBox(5);
	HBox hbox3 = new HBox(5);
	VBox vbox1 = new VBox(5);
	VBox vbox2 = new VBox(5);
	HBox total = new HBox(40);

	public void start(Stage primaryStage) throws Exception {
		passButton.setVisible(false);
		inputPass.setVisible(false);
		save.setStyle("-fx-background-color: #ff0000; ");
		
		siteInstr.setWrappingWidth(200);
		passInstr.setWrappingWidth(200);
		getPassInstr.setWrappingWidth(200);

		// load everything into map
		File input = new File("/Users/ludiyu/user_password.txt");
		
		if (input.exists()) {
			Scanner reader = new Scanner(input);

			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				site = line.substring(0, line.indexOf("-")).trim();
				pass = line.substring(line.indexOf("-")+ 1).trim();
				siteToPass.put(site, pass);
			}
			reader.close();
		}
		
		//will write websites and passwords to file
		FileWriter writer = new FileWriter(new File("/Users/ludiyu/user_password.txt"), new Boolean(false));
//		FileWriter writer = new FileWriter(new File("/Users/ludiyu/user_password.txt"), new Boolean(true));


		siteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				siteInstr.setText(siteInstrDefault);
				// update
				vbox1 = vbox1Display(vbox1, prompt1, siteInstr, hbox1, passInstr, hbox2, inputPass, save);
				total = totalDisplay(total, vbox1, vbox2);
			
				site = siteField.getText();

				siteField.clear();
				passButton.setVisible(true);
				siteButton.setVisible(false);
			}

		});

		passButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				siteInstr.setText(siteInstrDefault);
				passInstr.setText(passInstrDefault);
				// update pane
				vbox1 = vbox1Display(vbox1, prompt1, siteInstr, hbox1, passInstr, hbox2, inputPass, save);
				total = totalDisplay(total, vbox1, vbox2);

				// TODO Auto-generated method stub
				if (checkPass(passField.getText().trim())) {
					passInstr.setText(passInstrDefault);
					pass = passField.getText().trim();
					siteToPass.put(site, pass);
					siteButton.setVisible(true);
					
					//confirmation
					inputPass.setVisible(true);
//					try {
//						writer.write(site + " - " + pass);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
					inputPass.setText("Your password for " + site + " is " + pass);
					
					//update pane
					vbox1 = vbox1Display(vbox1, prompt1, siteInstr, hbox1, passInstr, hbox2, inputPass, save);
					total = totalDisplay(total, vbox1, vbox2);
				}

				else {
					passInstr.setText(strongerPass);
					// update pane
					vbox1 = vbox1Display(vbox1, prompt1, siteInstr, hbox1, passInstr, hbox2, inputPass, save);
					total = totalDisplay(total, vbox1, vbox2);

				}

				passField.clear();
				
			}

		});

		getPassButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				if (siteToPass.containsKey(getPassField.getText().trim())) {
					getPassInstr.setText(getPassDefault);
					password.setText("Your password is: " + siteToPass.get(getPassField.getText()));

					// update pane
					vbox2 = vbox2Display(vbox2, prompt2, getPassInstr, hbox3, password);
					total = totalDisplay(total, vbox1, vbox2);
				} else {
					getPassInstr.setText(getPassError);
					// update pane
					vbox2 = vbox2Display(vbox2, prompt2, getPassInstr, hbox3, password);
					total = totalDisplay(total, vbox1, vbox2);

				}

				getPassField.clear();
			}

		});

		
		
		//save the map to file when the user presses close button
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// TODO Auto-generated method stub
				for (Map.Entry<String, String> entry : siteToPass.entrySet()) {
				    try {
						writer.write(entry.getKey() + " - " + entry.getValue() + "\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} finally {
						try {
							writer.flush();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
			
		});
		
		
		// load things into the pane
		hbox1 = hboxDisplay(hbox1, siteField, siteButton);
		hbox2 = hboxDisplay(hbox2, passField, passButton);
		hbox3 = hboxDisplay(hbox3, getPassField, getPassButton);
		vbox1 = vbox1Display(vbox1, prompt1, siteInstr, hbox1, passInstr, hbox2, inputPass, save);
		vbox2 = vbox2Display(vbox2, prompt2, getPassInstr, hbox3, password);
		total = totalDisplay(total, vbox1, vbox2);

		Scene scene = new Scene(total);
		primaryStage.setTitle("Password Manager");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	
	public HBox hboxDisplay(HBox hbox, TextField field, Button button) {
		hbox.getChildren().clear();
		hbox.getChildren().addAll(field, button);
		return hbox;
	}


	public VBox vbox1Display(VBox vbox, Text prompt, Text instr1, HBox hbox1, Text instr2, HBox hbox2, Text inputPass, Button saveToFile) {
		vbox.getChildren().clear();
		vbox.getChildren().addAll(prompt, instr1, hbox1, instr2, hbox2, inputPass, saveToFile);
		return vbox;
	}

	public VBox vbox2Display(VBox vbox, Text prompt, Text instr, HBox hbox, Text password) {
		vbox.getChildren().clear();
		vbox.getChildren().addAll(prompt, instr, hbox, password);
		return vbox;
	}

	public HBox totalDisplay(HBox hbox, VBox vbox1, VBox vbox2) {
		hbox.getChildren().clear();
		hbox.getChildren().addAll(vbox1, vbox2);
		return hbox;
	}

	public boolean checkPass(String pass) {
		boolean hasCapital = false;
		boolean hasLower = false;
		boolean hasNum = false;

		for (int i = 0; i < pass.length(); i++) {
			char c = pass.charAt(i);
			if (Character.isDigit(c)) {
				hasNum = true;
			}
			if (Character.isUpperCase(c)) {
				hasCapital = true;
			}
			if (Character.isLowerCase(c)) {
				hasLower = true;
			}
		}

		return pass.length() >= 12 && hasCapital && hasLower && hasNum;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
