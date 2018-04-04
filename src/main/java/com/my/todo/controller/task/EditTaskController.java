package com.my.todo.controller.task;

import com.my.todo.controller.main.MainController;
import com.my.todo.model.GlobalContext;
import com.my.todo.model.Task;
import com.my.todo.service.task.TaskService;
import com.my.todo.service.task.TaskServiceImpl;
import com.my.todo.util.Priority;
import com.my.todo.util.TaskStatus;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import jfxtras.scene.control.LocalDateTimeTextField;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.FlowException;
import org.datafx.controller.flow.action.ActionMethod;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.flow.context.ActionHandler;
import org.datafx.controller.flow.context.FlowActionHandler;
import org.datafx.controller.util.VetoException;

import java.net.URL;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by marcin on 05.01.16.
 */
@FXMLController(title = "Edit task", value = "/view/task/editTask.fxml")
public class EditTaskController implements Initializable {

    private GlobalContext globalContext;

    private TaskService taskService;

    @FXML
    private TextField name;

    @FXML
    private TextField categoryName;

    @FXML
    private ComboBox<Priority> prioritySelect;

    @FXML
    private LocalDateTimeTextField dueDate;

    @FXML
    private TextArea description;

    @FXML
    private CheckBox alarm;

    @FXML
    private Label result;

    @FXML
    @ActionTrigger("edit")
    private Button saveButton;

    @FXML
    @ActionTrigger("back")
    private Button back;

    @ActionHandler
    FlowActionHandler handler;

    @ActionMethod("edit")
    public void editTask() {
        List<String> errors = validate();
        if (!errors.isEmpty()) {
            String errorMessage = "";
            for (String error : errors) {
                errorMessage += error + "\n";
            }
            onErrorDialog(errorMessage);
            return;
        }
        Task task = globalContext.getTaskToEdit();
        task.setCategory(null);
        task.setCreationDate(new Timestamp(new Date().getTime()));
        Timestamp endDate = Timestamp.from(dueDate.getLocalDateTime().toInstant(ZoneOffset.ofHours(1)));
        task.setEndDate(endDate);
        task.setDesc(description.getText());
        task.setTaskStatus(TaskStatus.NEW);
        task.setPriority(prioritySelect.getValue());
        task.setAlarm(alarm.isSelected());
        taskService.edit(task);
        createInformationDialog();
    }


    private List<String> validate() {
        LocalDateTime current = LocalDateTime.now();
        LocalDateTime taskDueDate = dueDate.getLocalDateTime();
        List<String> result = new ArrayList<>();
        if (dueDate.getLocalDateTime() == null) {
            result.add("Date cannot be empty.");
        }
        Duration duration = Duration.between(current, taskDueDate);
        if (duration.isNegative()) {
            result.add("Due date should not be before current date.");
        }
        return result;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        globalContext = GlobalContext.getInstance();
        result.setVisible(false);
        prioritySelect.setItems(FXCollections.observableArrayList(Priority.values()));
        setTaskDetails(globalContext.getTaskToEdit());
    }

    private void setTaskDetails(Task task) {
        name.setText(task.getName());
        dueDate.setLocalDateTime(task.getEndDate().toLocalDateTime());
        description.setText(task.getDesc());
        prioritySelect.getSelectionModel().select(task.getPriority());
        alarm.setSelected(task.getAlarm());
    }

    public EditTaskController() {
        taskService = new TaskServiceImpl();
    }

    private void createInformationDialog() {
        Alert dlg = new Alert(Alert.AlertType.INFORMATION,"");
        dlg.setTitle("Edit");
        dlg.getDialogPane().setContentText("Succesfully edited.");

        dlg.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                try {
                    handler.navigate(MainController.class);
                } catch (VetoException e) {
                    e.printStackTrace();
                } catch (FlowException e) {
                    e.printStackTrace();
                }
            }
        });

        showDialog(dlg);
    }

    private void onSuccessDialog() {
        Alert dlg = new Alert(Alert.AlertType.INFORMATION,"");
        dlg.setTitle("Create");
        dlg.getDialogPane().setContentText("Succesfully created.");

        dlg.setOnCloseRequest(new EventHandler<DialogEvent>() {
            @Override
            public void handle(DialogEvent event) {
                try {
                    handler.navigate(MainController.class);
                } catch (VetoException e) {
                    e.printStackTrace();
                } catch (FlowException e) {
                    e.printStackTrace();
                }
            }
        });
        showDialog(dlg);
    }

    private void onErrorDialog(String errors) {
        Alert dlg = new Alert(Alert.AlertType.ERROR,"");
        dlg.setTitle("Create");
        dlg.getDialogPane().setContentText("Creation error! \n" + errors);
        showDialog(dlg);
    }

    private void showDialog(Dialog<?> dlg) {
        dlg.showAndWait().ifPresent(result -> System.out.println("Result is " + result));
    }

}
