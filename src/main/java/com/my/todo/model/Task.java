package com.my.todo.model;

import com.my.todo.util.Priority;
import com.my.todo.util.TaskStatus;
import lombok.Data;
import javax.persistence.*;

import java.sql.Timestamp;

/**
 * Created by marcin on 30.11.15.
 */
@Data
@Entity
@Table(name = "TASK")
@NamedQueries({
        @NamedQuery(name="Task.findByName",
                query="SELECT t FROM Task t WHERE t.name = :name"),
        @NamedQuery(name="Task.findByPriority",
                query="SELECT t FROM Task t WHERE t.priority = :priority"),
        @NamedQuery(name="Task.findByStatus",
                query="SELECT t FROM Task t WHERE t.taskStatus = :status"),
        @NamedQuery(name="Task.findExpired",
                query="SELECT t FROM Task t WHERE t.endDate <= :date and t.alarm = true and t.taskStatus != :taskStatus"),
})

public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @Column(name = "DESCRIPTION")
    private String desc;

    @Column(name = "CREATION_DATE", nullable = false)
    private Timestamp creationDate;

    @Column(name = "END_DATE")
    private Timestamp endDate;

    @Enumerated(EnumType.STRING)
    @Column(name ="STATUS")
    private TaskStatus taskStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "PRIORITY")
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @Column(name = "ALARM")
    private Boolean alarm = new Boolean(false);

    public Task() {}

}
