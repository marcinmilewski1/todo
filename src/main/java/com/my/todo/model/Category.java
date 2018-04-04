package com.my.todo.model;

import lombok.Data;


import javax.persistence.*;
import java.util.List;

/**
 * Created by marcin on 30.11.15.
 */
@Data
@Entity
@Table(name = "CATEGORY")
@NamedQueries({
        @NamedQuery(name="Category.findByName",
                query="SELECT t FROM Category t WHERE t.name = :name"),
        @NamedQuery(name="Category.findByTaskId",
                query="SELECT t.category FROM Task t where t.id = :id"),
        @NamedQuery(name="Category.findAllWithTasks",
                query="SELECT t FROM Category t"),
})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @OneToMany (mappedBy = "category", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Task> taskList;

    public Category() {}
}
