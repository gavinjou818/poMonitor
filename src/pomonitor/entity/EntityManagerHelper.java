package pomonitor.entity;

// default package

import java.util.logging.Level;

import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * @author MyEclipse Persistence Tools
 */

/*
 * zhouzhifeng
 * 2016��12��2��
 * ���ע��
 */

//���ڴ���EntityManager��������ɾ���
public class EntityManagerHelper 
{

	private static final EntityManagerFactory emf;//JPA ʵ�����������
	private static final ThreadLocal<EntityManager> threadLocal;
	private static final Logger logger;

	static {
		emf = Persistence.createEntityManagerFactory("poMonitor");
		threadLocal = new ThreadLocal<EntityManager>();
		logger = Logger.getLogger("poMonitor");
		logger.setLevel(Level.ALL);
	}

	public static EntityManager getEntityManager()//��ȡʵ�������
	{
		EntityManager manager = threadLocal.get();
		if (manager == null || !manager.isOpen()) {
			manager = emf.createEntityManager();
			threadLocal.set(manager);
		}
		return manager;
	}

	public static void closeEntityManager()//ʵ������߹ر� 
	{
		EntityManager em = threadLocal.get();
		threadLocal.set(null);
		if (em != null)
			em.close();
	}

	public static void beginTransaction() {
		getEntityManager().getTransaction().begin();//��������
	}

	public static void commit() {
		getEntityManager().getTransaction().commit();//�����ύ
	}

	public static void rollback() {
		getEntityManager().getTransaction().rollback();//����ع�
	}

	public static Query createQuery(String query) { 
		return getEntityManager().createQuery(query);
	}

	public static void log(String info, Level level, Throwable ex) {
		logger.log(level, info, ex);//��־��¼
	}

}
