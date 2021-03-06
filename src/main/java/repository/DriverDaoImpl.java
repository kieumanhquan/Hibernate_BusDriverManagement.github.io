package repository;

import dao.DriverDao;
import entity.Driver;
import util.DataUtil;
import util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class DriverDaoImpl implements DriverDao {

    @Override
    public void addNewDriver(Driver drivers) {
        if (DataUtil.isEmptyOrNull(drivers)) {
            return;
        }
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.save(drivers);
            session.getTransaction().commit();
        } catch (Exception exception) {
            exception.printStackTrace();
            assert session != null;
            session.getTransaction().rollback();
        }
    }

    @Override
    public List<Driver> getAll(){
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            session.beginTransaction();
            return (List<Driver>) session.createQuery("from Driver").list();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
