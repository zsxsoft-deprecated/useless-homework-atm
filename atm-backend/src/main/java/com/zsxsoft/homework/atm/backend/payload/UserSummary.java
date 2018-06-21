package com.zsxsoft.homework.atm.backend.payload;

import com.zsxsoft.homework.atm.backend.model.User;

public class UserSummary {
    private Long id;
    private String username;
    private String name;

    UserSummary(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
