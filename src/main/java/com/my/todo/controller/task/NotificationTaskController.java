package com.my.todo.controller.task;

import com.my.todo.model.GlobalContext;
import com.my.todo.model.Task;
import com.my.todo.service.task.TaskService;
import com.my.todo.service.task.TaskServiceImpl;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionMethod;
import org.datafx.controller.flow.action.ActionTrigger;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ResourceBundle;

/**
 * Created by marcin on 17.01.16.
 */
@FXMLController(title = "Remind", value = "/view/task/taskNotification.fxml")
public class NotificationTaskController implements Initializable {

    private GlobalContext globalContext;

    private TaskService taskService;

    private Task task;

    @FXML
    @ActionTrigger("dontRemind")
    Button dontRemind;

    @FXML
    @ActionTrigger("remindForOneHour")
    Button remindForOneHour;

    @FXML
    @ActionTrigger("remindTommorow")
    Button remindTommorow;

    @FXML
    TextField name;

    @FXML
    TextArea desc;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        globalContext = GlobalContext.getInstance();
        task = globalContext.getNotifiedTask();
        taskService = new TaskServiceImpl();
        setTaskDetails();
    }

    public void setTaskDetails() {
        name.setText(task.getName());
        desc.setText(task.getDesc());
        name.setEditable(false);
        desc.setEditable(false);
    }

    @ActionMethod("dontRemind")
    public void onDontRemindClicked() {
        task.setAlarm(false);
        taskService.edit(task);
        closeWindow();
        taskService.getEntityManager().clear();
    }

    private void closeWindow() {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }

    @ActionMethod("remindForOneHour")
    public void onRemindForOneHourClicked() {
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime newDueDateTime = current.plusHours(1);
        task.setEndDate(Timestamp.from(newDueDateTime.toInstant(ZoneOffset.ofHours(1))));
        taskService.edit(task);
        taskService.getEntityManager();
        closeWindow();
        taskService.getEntityManager().clear();
    }

    @ActionMethod("remindTommorow")
    public void onRemindTommorowClicked() {
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime newDueDateTime = current.plusDays(1);
        task.setEndDate(Timestamp.from(newDueDateTime.toInstant(ZoneOffset.ofHours(1))));
        taskService.edit(task);
        closeWindow();
        taskService.getEntityManager().clear();
    }
}
