package com.my.todo.controller.main;

import com.my.todo.model.Category;
import com.my.todo.model.GlobalContext;
import com.my.todo.model.GlobalPropertiesSingleton;
import com.my.todo.model.Task;
import com.my.todo.service.category.CategoryService;
import com.my.todo.service.category.CategoryServiceImpl;
import com.my.todo.service.task.TaskService;
import com.my.todo.service.task.TaskServiceImpl;
import com.my.todo.util.Priority;
import com.my.todo.util.PrioritySelectType;
import com.my.todo.util.TaskStatus;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.datafx.controller.FXMLController;
import org.datafx.controller.flow.action.ActionMethod;
import org.datafx.controller.flow.action.ActionTrigger;
import org.datafx.controller.flow.context.ActionHandler;
import org.datafx.controller.flow.context.FlowActionHandler;

import javax.persistence.FlushModeType;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@FXMLController(title = "TodoApp", value = "/view/main.fxml")
public class MainController implements Initializable {

    /* SERVICES */
    private CategoryService categoryService;
    private TaskService taskService;
    private ObservableList<Task> taskList;
    private ObservableList<Category> categoryList;

    /* BUTTONS */
    @FXML
    @ActionTrigger("removeTask")
    private Button removeTaskButton;

    @FXML
    @ActionTrigger("taskCreationFormLink")
    private Button createNewTaskButton;

    @FXML
    @ActionTrigger("editTask")
    private Button editTaskButton;

    @FXML
    @ActionTrigger("markAsDone")
    private Button  doneTaskButton;

    @FXML
    @ActionTrigger("reopenTask")
    private Button taskReopenButton;

    @FXML
    @ActionTrigger("markAsDoing")
    private Button taskDoingButton;
    /*CONTAINER */
    @FXML
    private TextFlow taskDescriptionTextFlow;
    @FXML
    private VBox taskVBox;

    @FXML
    private ListView listView;

    @FXML
    private HBox headerHBox;

    /* LABELS */
    @FXML
    private Label taskNameLabel;
    @FXML
    private Label taskStatusLabel;
    @FXML
    private Label taskDateToLabel;
    @FXML
    private Label taskCreationDateLabel;
    @FXML
    private Label taskPriorityLabel;
    @FXML
    private Label footerLabel;

    /* OTHERS */
    @FXML
    private ComboBox<PrioritySelectType> tasksSelect;
    @FXML
    private TextField searchPanel;
    @FXML
    private MenuBar menuBar;

    @FXML
    private CheckBox taskAlarm;

    @FXML
    private TextArea taskDescriptionTextArea;

    @ActionHandler
    FlowActionHandler handler;

    private GlobalContext globalContext;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.print("DEBUG mainController initialize ");
        categoryList.setAll(categoryService.findAll());
        taskList.setAll(taskService.findAll());
        setAllTasksListView();
        setEventOnTaskSelection();
        taskVBox.setVisible(false);
        setQuickSearchEvent();
        taskService.getEntityManager().setFlushMode(FlushModeType.COMMIT);
        tasksSelect.setItems(FXCollections.observableArrayList(PrioritySelectType.values()));
        tasksSelect.valueProperty().addListener(new ChangeListener<PrioritySelectType>() {
            @Override
            public void changed(ObservableValue<? extends PrioritySelectType> observable, PrioritySelectType oldValue, PrioritySelectType newValue) {
                if (newValue.equals(PrioritySelectType.ALL)) {
                    taskList.setAll(taskService.findAll());
                }
                else if (newValue.equals(PrioritySelectType.CRITICAL)) {
                    taskList.setAll(taskService.findByPriority(Priority.CRITICAL));
                }
                else if (newValue.equals(PrioritySelectType.MAJOR)) {
                    taskList.setAll(taskService.findByPriority(Priority.MAJOR));
                    System.out.println(taskList.size());
                }
                else if (newValue.equals(PrioritySelectType.NEUTRAL)) {
                    taskList.setAll(taskService.findByPriority(Priority.NEUTRAL));
                    System.out.println(taskList.size());
                }
                else if (newValue.equals(PrioritySelectType.MINOR)) {
                    taskList.setAll(taskService.findByPriority(Priority.MINOR));
                }
                setTaskListView();
            }
        });

    }

    private void setQuickSearchEvent() {
        searchPanel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (listView.getItems().contains(searchPanel.getText())) {
                    listView.getSelectionModel().select(searchPanel.getText());
                }
            }
        });
    }


    public MainController() {
        System.out.print("DEBUG mainController constructor ");
        categoryService = new CategoryServiceImpl();
        taskService = new TaskServiceImpl();
        taskList = FXCollections.observableArrayList();
        categoryList = FXCollections.observableArrayList();
        startTimer();
        globalContext = GlobalContext.getInstance();
    }

    private void startTimer() {
        if (GlobalPropertiesSingleton.getInstance().isTimerStarted() == false) {
            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.ZERO,
                            actionEvent -> notifyIfDueDate()
                    ),
                    new KeyFrame(
                            Duration.seconds(60)
                    )
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            GlobalPropertiesSingleton.getInstance().setTimerStarted(true);
        }
    }

    private void notifyIfDueDate() {
        System.out.println("timer task");
        List<Task> dueDated = taskService.findExpired(new Timestamp(new Date().getTime()));
        if (dueDated == null || dueDated.isEmpty()) return;
        for (Task task : dueDated) {
            System.out.println("ALARM!" + task);
            StringBuilder notificationText = new StringBuilder("Remember about task: \n");
            notificationText.append(task.getName()).append("\n");
            notificationText.append(task.getDesc());
            notification(task, notificationText.toString(), "Reminder", Pos.BOTTOM_RIGHT);
        }
    }

    private void setTaskListView() {
        List<String> tasks = new ArrayList<>();
        for (Task task : taskList) {
            tasks.add(task.getName());
        }
        listView.setItems(FXCollections.observableArrayList(tasks));
    }

    private void setAllTasksListView() {
        List<String> tasks = new ArrayList<>();
        for (Task task : taskList) {
            tasks.add(task.getName());
        }
        listView.setItems(FXCollections.observableArrayList(tasks));
    }

    private void setEventOnTaskSelection() {
        listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
               String taskName = (String) newValue;
                System.out.println("Task: " + taskName);
                showTaskDetails(taskName);
            }
        });
    }

    private void showTaskDetails(String taskName) {
        taskService.getEntityManager().clear();
        List<Task> tasks = taskService.findByName(taskName);
        if (tasks.isEmpty()) return;

        taskVBox.setVisible(true);
        Task task = tasks.get(0);
        taskNameLabel.setText(task.getName());
        taskStatusLabel.setText(task.getTaskStatus().toString());
        taskDateToLabel.setText(task.getEndDate().toString());
        taskCreationDateLabel.setText(task.getCreationDate().toString());
        taskPriorityLabel.setText(task.getPriority().toString());
        taskDescriptionTextArea.setText(task.getDesc());
        taskAlarm.setSelected(task.getAlarm() == false ? false : true);

        TaskStatus taskStatus = task.getTaskStatus();
        if (taskStatus.equals(TaskStatus.NEW)) {
            doneTaskButton.setDisable(false);
            editTaskButton.setDisable(false);
            taskDoingButton.setDisable(false);
            taskReopenButton.setVisible(false);
        }
        else if (taskStatus.equals(TaskStatus.DONE)) {
            doneTaskButton.setDisable(true);
            editTaskButton.setDisable(true);
            taskReopenButton.setVisible(true);
            taskDoingButton.setDisable(true);
        }
        else if (taskStatus.equals(TaskStatus.DOING)) {
            doneTaskButton.setDisable(false);
            editTaskButton.setDisable(false);
            taskReopenButton.setVisible(false);
            taskDoingButton.setDisable(true);

        }
        globalContext.setTaskToEdit(task);
    }

    @ActionMethod("removeTask")
    public void removeTask() {
        String taskName = (String) listView.getSelectionModel().getSelectedItem();
        List<Task> tasks = taskService.findByName(taskName);
        Task task = tasks.get(0);
        listView.getSelectionModel().clearSelection();
        taskService.remove(task.getId());
        taskVBox.setVisible(false);
        refreshTasksLists();

        popUpInformationDialog("Deleted", "Task has been succesfully removed." , "");
    }

    private void popUp(String text) {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.NONE);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(new Text(text));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void popUpInformationDialog(String title, String content, String optionalMasthead) {
        Alert dlg = createAlert(Alert.AlertType.INFORMATION);
        dlg.setTitle(title);
        dlg.getDialogPane().setContentText(content);

        // lets get some output when events happen
        dlg.setOnShowing(evt -> System.out.println(evt));
        dlg.setOnShown(evt -> System.out.println(evt));
        dlg.setOnHiding(evt -> System.out.println(evt));
        dlg.setOnHidden(evt -> System.out.println(evt));
//              dlg.setOnCloseRequest(evt -> evt.consume());

        showDialog(dlg);
    }

    private Alert createAlert(Alert.AlertType type) {
        Alert dlg = new Alert(type, "");
        return dlg;
    }

    private void showDialog(Dialog<?> dlg) {
            dlg.showAndWait().ifPresent(result -> System.out.println("Result is " + result));
        }

    private void refreshTasksLists() {
        taskList.setAll(taskService.findAll());
        setAllTasksListView();
    }

    @ActionMethod("markAsDone")
    public void markAsDone() {
        String taskName = (String) listView.getSelectionModel().getSelectedItem();
        List<Task> tasks = taskService.findByName(taskName);
        Task task = tasks.get(0);
        task.setTaskStatus(TaskStatus.DONE);
        taskService.edit(task);
        showTaskDetails(task.getName());
    }

    @ActionMethod("reopenTask")
    public void reopenTask() {
        String taskName = (String) listView.getSelectionModel().getSelectedItem();
        List<Task> tasks = taskService.findByName(taskName);
        Task task = tasks.get(0);
        task.setTaskStatus(TaskStatus.NEW);
        showTaskDetails(task.getName());
    }

    @ActionMethod("markAsDoing")
    public void markAsDoing() {
        String taskName = (String) listView.getSelectionModel().getSelectedItem();
        List<Task> tasks = taskService.findByName(taskName);
        Task task = tasks.get(0);
        task.setTaskStatus(TaskStatus.DOING);
        taskService.edit(task);
        showTaskDetails(task.getName());
    }

    private void notification(Task task, String notificationText, String tittle, Pos pos) {
        globalContext.setNotifiedTask(task);
        Notifications notificationBuilder = Notifications.create()
                .title(tittle)
                .text(notificationText)
                .hideAfter(Duration.seconds(10))
                .graphic(null)
                .position(pos)
                .onAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent arg0) {
                        try {
                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/task/taskNotification.fxml"));
                            Parent root1 = (Parent) fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root1));
                            stage.getScene().getStylesheets().add("/view/style.css");
                            stage.getIcons().add(new Image("/view/ok-icon.png"));
                            stage.setTitle("Remind");
                            stage.show();
                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("Notification clicked on!");
                    }
                });

        notificationBuilder.showWarning();
        }


}
