package com.example.se330_pharmacy.Controllers;

import com.example.se330_pharmacy.DataAccessObject.EmployeeDAO;
import com.example.se330_pharmacy.Models.ConnectDB;
import com.example.se330_pharmacy.Models.Model;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public HBox container;
    public TextField tf_username_forgot;
    public PasswordField pfPassword1_change;
    public PasswordField pfPassword2_change;
    public RadioButton radioHideShowChange;
    public RadioButton radioHideShow;
    public TextField tfShowPasswordCP1,tfShowPasswordCP2,tfShowPasswordLogin;
    private int index ;
    public Text loginMessageLabel;
    public TextField tfUsername_Login;
    public PasswordField pfPassword_Login;
    @FXML
    private Button btnChange;
    @FXML
    public Text lbl_send_otp;
    @FXML
    private Button btnConfirm;

    @FXML
    private Button btnLogin;

    @FXML
    private Pane changePane;

    @FXML
    private Pane forgetPane;

    @FXML
    private Pane loginPane;

    @FXML
    private TextField textFieldOTP;

    @FXML
    private Text textWelcome;
    @FXML
    private Pane paneProgress;
    String sentEmail = null;
    private String storedOTP;
    private int time_remaining = 50;
    private Timeline timeline;
    private double xOffset = 0;
    private double yOffset =0;
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    public ConnectDB connectDB = ConnectDB.getInstance();
    @FXML
    void backToLogin(MouseEvent event) {
        index =0;
        ResetTextField();
        forgetPane.toBack();
        changePane.toBack();
        radioHideShow.setSelected(false);
    }

    @FXML
    void forgotPasswordOnclick(MouseEvent event) {
        index = 1;
        loginPane.toBack();
        changePane.toBack();
    }

    @FXML
    void sendOTP(MouseEvent event) {
        if (tf_username_forgot.getText().isBlank()) {
            showAlert("Warning","You must fill the username!");
            return;
        }
        paneProgress.toFront();
        paneProgress.setVisible(true);
        new Thread(() -> {
            String username_result;
            username_result = employeeDAO.getUsername(tf_username_forgot.getText());
            if (username_result == null) {
            Platform.runLater(()->{
                paneProgress.setVisible(false);
                showAlert("Warning","Không tồn tại username: " + tf_username_forgot.getText());
            });
                return;
            }
            storedOTP = generateOTP();
            String fromEmail = "kiseryouta2003@gmail.com";
            String password = "qcqa slmu vkbr edha";
            String subject = "OTP code";
            String body = "Your OTP code is " + storedOTP;

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(fromEmail, password);
                }
            });

            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(fromEmail, "Green Pharmacy"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(employeeDAO.getEmail(tf_username_forgot.getText())));
                message.setSubject(subject);
                message.setText(body);

                Transport.send(message);
                sentEmail = storedOTP;
                Platform.runLater(() -> {
                    if (sentEmail != null) {
                        paneProgress.setVisible(false);
                        showAlert("Warning","OTP is now sent to mail!");
                        countDown(); // Start countdown here
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                sentEmail = null;
                showAlert("Warning","Failed to send OTP: " + e.getMessage());
            }
        }).start();

    }

    private void countDown() {
        lbl_send_otp.setDisable(true);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                time_remaining--;
                lbl_send_otp.setText("Gửi lại sau " + time_remaining + " s");
                // lbl_send_otp.setX();
                if (time_remaining <= 0) {
                    timeline.stop();
                    lbl_send_otp.setText("Gửi OTP");
                    lbl_send_otp.setDisable(false);
                    sentEmail = null; // đặt sent email về null để huỷ otp
                    time_remaining = 50; // khởi động lại timer
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private String generateOTP() {
        Random random = new Random();
        int otp = random.nextInt(100000, 999999);
        return String.valueOf(otp);
    }

    @FXML
    void btnContinue_clicked(MouseEvent event) throws InterruptedException {
        if (CheckForFill()) {
            if (!textFieldOTP.getText().equals(sentEmail)) {
                showAlert("Warning","OTP is wrong!");
                return;
            }
            index = 2;
            changePane.toFront();
            pfPassword_Login.setText("");
            sentEmail = null;
        }
    }
    public void btnConfirm_clicked(MouseEvent mouseEvent) throws SQLException {
        if(isLess6characters(pfPassword1_change)) return;
        if(isLess6characters(pfPassword2_change)) return;// kiem tra co duoi 6 character hay khong ?
        if(!CheckForFill()) return;
        paneProgress.setVisible(true);
        paneProgress.toFront();
        new Thread(() -> {
            try {
                if (CheckForFill()) {
                    boolean updateSuccess = UpdatePassword(index);
                    Platform.runLater(() -> {
                        if (updateSuccess) {
                            showAlert("Notification", "Password is now changed!");
                            index = 0;
                            ResetTextField();
                            changePane.toBack();
                            forgetPane.toBack();
                            radioHideShow.setSelected(false);
                        } else {
                            showAlert("Warning", "Failed to update password. Please try again.");
                        }
                        paneProgress.setVisible(false);
                    });
                } else {
                    Platform.runLater(() -> {
                        showAlert("Warning", "Error! Please check the input fields.");
                        paneProgress.setVisible(false);
                    });
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    showAlert("Error Connection", "Can't connect to the server!");
                    paneProgress.setVisible(false);
                });
            }
        }).start();
    }
    private boolean UpdatePassword(int index) throws SQLException{
        if(index==3)// yêu cầu đổi mật khẩu mặc định
        {
            return employeeDAO.UpdatePassword(tfUsername_Login.getText(),pfPassword2_change.getText(),index);
        }
        return employeeDAO.UpdatePassword(tf_username_forgot.getText(),pfPassword2_change.getText(),index);
    }

    @FXML
    void close(MouseEvent event) {
        Stage s = (Stage) ((Node)event.getSource()).getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(s);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //set initialize
        radioHideShow.setOnAction(event -> showPassword());
        radioHideShowChange.setOnAction(event -> showPassword());
        btnLogin.setOnAction(event -> loginButtonOnAction());
        forgetPane.toBack();
        changePane.toBack();
        index=0;
        tfShowPasswordLogin.textProperty().addListener((observable,oldValue, newValue )-> {
            pfPassword_Login.setText(newValue);
        } );
        pfPassword_Login.textProperty().addListener((observable,oldValue, newValue )-> {
            tfShowPasswordLogin.setText(newValue);
        } );
        tfShowPasswordCP1.textProperty().addListener((observable,oldValue, newValue )-> {
            pfPassword1_change.setText(newValue);
        } );
        tfShowPasswordCP2.textProperty().addListener((observable,oldValue, newValue )-> {
            pfPassword2_change.setText(newValue);
        } );
        pfPassword1_change.textProperty().addListener((observable,oldValue, newValue )-> {
            tfShowPasswordCP1.setText(newValue);
        } );
        pfPassword2_change.textProperty().addListener((observable,oldValue, newValue )-> {
            tfShowPasswordCP2.setText(newValue);
        } );
        container.setOnMousePressed(mouseEvent -> {
            xOffset = mouseEvent.getSceneX();
            yOffset = mouseEvent.getSceneY();
        });

        container.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) container.getScene().getWindow();
            stage.setX(mouseEvent.getScreenX()-xOffset);
            stage.setY(mouseEvent.getScreenY()-yOffset);
        });
    }

    private void showPassword() {
        if (index == 0 && radioHideShow.isSelected()) {
            //show password va an password field
            tfShowPasswordLogin.setVisible(true);
            pfPassword_Login.setVisible(false);
        } else if (index == 0 && !radioHideShow.isSelected()) {
            pfPassword_Login.setVisible(true);
            tfShowPasswordLogin.setVisible(false);
        } else if ((index == 2 ||index ==3 )&& radioHideShowChange.isSelected()) {
            tfShowPasswordCP1.setVisible(true);
            pfPassword1_change.setVisible(false);
            tfShowPasswordCP2.setVisible(true);
            pfPassword2_change.setVisible(false);
        } else if ((index == 2 ||index ==3)&& !radioHideShowChange.isSelected()) {
            //an textfield va show lai passwordfield
            tfShowPasswordCP1.setVisible(false);
            pfPassword1_change.setVisible(true);
            tfShowPasswordCP2.setVisible(false);
            pfPassword2_change.setVisible(true);
        }
    }

    private void loginButtonOnAction()  {
      if(CheckForFill()) Login(tfUsername_Login.getText(),pfPassword_Login.getText());
    }

    private void Login(String username,String password) {
        int valid = employeeDAO.CheckValidate(username, password);
        Platform.runLater(()-> {
            paneProgress.setVisible(false);
            if (valid == 1) {
                showAlert("Notification","Welcome! Please change your password");
                index = 3 ;
                loginPane.toBack();
                forgetPane.toBack();
            } else if (valid == 2) {
                showAlert("Notification","Login successfully!");
                pfPassword_Login.setText("");
                Stage stage = (Stage) btnLogin.getScene().getWindow(); //get login-screen
                Model.getInstance().getViewFactory().closeStage(stage);//close login-screen
                try {
                    Model.getInstance().getViewFactory().showMenuWindow(employeeDAO.getEmployee());//mở menu-screen
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else
                showAlert("Warning","Fail to login! Check your Username and Password again");
        });
    }

    public void passwordFieldKeyTyped(KeyEvent keyEvent) {;
        checkValidate20characters(keyEvent);
    }

    private Boolean CheckForFill ()
    {
        if (index == 0) // đang ở màn login
        {
            if ((tfUsername_Login.getText().isBlank()) || pfPassword_Login.getText().isBlank()) {
                showAlert("Warning","Please enter username and password");
                tfUsername_Login.setText("");
                pfPassword_Login.setText("");
                return false;
            }
            if(isLess6characters(pfPassword_Login)) return false;
        } else if (index == 1) // đang ở màn forgotpassword
        {
            if (tf_username_forgot.getText().isBlank()) {
                showAlert("Warning","You must fill the username!");
                return false;
            } else if (textFieldOTP.getText().isBlank()) {
                showAlert("Warning","You must fill the OTP!");
                return false;
            }
        } else // đang ở màn change
        {
            if (pfPassword1_change.getText().isBlank()) {
                showAlert("Warning","You must fill new password");
                return false;
            }
            if (pfPassword2_change.getText().isBlank()) {
                showAlert("Warning","You must fill confirm new password");
                return false;
            }
            if (!pfPassword2_change.getText().equals(pfPassword1_change.getText())) {
                showAlert("Warning",
                        "Wrong password re-entered, please check again");
                return false;
            }
        }
        return true;
    }
    private void checkValidate20characters(KeyEvent keyEvent) {
        if (pfPassword_Login.getText().length() >= 15) {
            // Hiển thị thông báo khi độ dài vượt quá 15 ký tự
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Password must be 15 characters or less.");
            alert.showAndWait();
            keyEvent.consume(); // Hủy sự kiện

            // Xoá ký tự vừa nhập dư
            int caretPos = pfPassword_Login.getCaretPosition();
            pfPassword_Login.deleteText(caretPos - 1, caretPos);
        }
    }
    private boolean isLess6characters(TextField tf) {
        if (tf.getText().length() <6) {
            // Hiển thị thông báo khi độ dài ít hơn 6 ký tự
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Password must be 6 character or more!");
            alert.showAndWait();
            return true;
        }
        return false;
    }
    private void showAlert(String tilte,String string) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(tilte);
        alert.setHeaderText(null);
        alert.setContentText(string);
        alert.showAndWait();
    }
    private void ResetTextField()
    {
        tfUsername_Login.setText("");
        pfPassword_Login.setText("");
        pfPassword1_change.setText("");
        pfPassword2_change.setText("");
        tf_username_forgot.setText("");
        textFieldOTP.setText("");
    }
}
