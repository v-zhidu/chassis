package io.vzhidu.chassis.common.scapp;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class MockUser {

    private String id;

    private MockUserType type;

    @NotNull
    @Length(max = 10)
    private String name;

    private LocalDateTime birthTime;

    public MockUser() {
    }

    public MockUser(String id, MockUserType type, @NotNull String name, LocalDateTime birthTime) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.birthTime = birthTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MockUserType getType() {
        return type;
    }

    public void setType(MockUserType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getBirthTime() {
        return birthTime;
    }

    public void setBirthTime(LocalDateTime birthTime) {
        this.birthTime = birthTime;
    }

    /**
     * Enum for user type
     */
    public enum MockUserType {
        NORMAL,

        MOCK
    }
}
