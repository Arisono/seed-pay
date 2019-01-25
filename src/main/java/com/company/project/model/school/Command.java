package com.company.project.model.school;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class Command<T> {
    private String cmd;
    private T data = null;//返回数据

    public Command(String cmd) {
        this.cmd = cmd;
    }

    public Command(String cmd, T data) {
        this.cmd = cmd;
        this.data = data;
    }

    /**
     * List装换Clist
     *
     * @param cmd
     * @param list
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
	public static List<Command> toCommandList(String cmd, List list) {
        List<Command> commandList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            commandList.add(new Command(cmd, list.get(i)));
        }
        return commandList;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toJsonStr() {
        return JSONObject.toJSONString(this);
    }
}
