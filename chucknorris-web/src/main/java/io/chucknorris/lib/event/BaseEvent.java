package io.chucknorris.lib.event;

import java.io.Serializable;
import lombok.Getter;

@Getter
public class BaseEvent implements Event, Serializable {

    private String name;
    private Object payload;

    public BaseEvent(String name, Object payload) {
        this.name = name;
        this.payload = payload;
    }
}
