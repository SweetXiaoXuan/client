package cn.xxtui.basic.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.annotation.Resource;
import java.io.Serializable;

public abstract class BasicDao<T> {
    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;


    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public Serializable save(T obj) {
        Session session = sessionFactory.getCurrentSession();
        return session.save(obj);
    }

    public T get(Class<T> clazz, int id) {
        Session session = sessionFactory.getCurrentSession();
        return (T) session.get(clazz, id);
    }

    public T get(Class<T> clazz, String id) {
        Session session = sessionFactory.getCurrentSession();
        return (T) session.get(clazz, id);
    }

    public T get(Class<T> clazz, long id) {
        Session session = sessionFactory.getCurrentSession();
        return (T) session.get(clazz, id);
    }
}
