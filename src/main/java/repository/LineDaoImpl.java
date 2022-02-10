package repository;

import dao.LineDao;
import entity.Line;
import util.DataUtil;
import util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class LineDaoImpl implements LineDao {

    @Override
    public void addNewLine(Line line) {
        if (DataUtil.isEmptyOrNull(line)) {
            return;
        }
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(line);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            assert session != null;
            session.getTransaction().rollback();
        }
    }

    @Override
    public List<Line> getAll(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            return (List<Line>) session.createQuery("from Line ").list();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
