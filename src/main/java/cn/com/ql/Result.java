package cn.com.ql;

import java.util.List;

public class Result<T> {
    private List<T> t;

    protected Result(List<T> t) {
        this.t = t;
    }

    public List<T> list() {
        return t;
    }

    public T unique() {
        if (t.size() > 1) {
            throw new RuntimeException("这是唯一对象，不是数组");
        }
        return t.get(0);
    }
}