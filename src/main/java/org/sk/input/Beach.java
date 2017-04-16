package org.sk.input;

public class Beach {

    private final String name;
    private final String city;
    private String msg;

    public Beach(final String name, final String city) {
        this.name = name;
        this.city = city;
        msg = null;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(final String msg) {
        this.msg = msg;
    }
}
