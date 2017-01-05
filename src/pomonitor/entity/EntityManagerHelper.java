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
 * 2016年12月2日
 * 标记注释
 */

//用于创建EntityManager并进行增删查改
public class EntityManagerHelper 
{

	private static final EntityManagerFactory emf;//JPA 实体管理器工厂
	private static final ThreadLocal<EntityManager> threadLocal;
	private static final Logger logger;

	static {
		emf = Persistence.createEntityManagerFactory("poMonitor");
		threadLocal = new ThreadLocal<EntityManager>();
		logger = Logger.getLogger("poMonitor");
		logger.setLevel(Level.ALL);
	}

	public static EntityManager getEntityManager()//获取实体管理者
	{
		EntityManager manager = threadLocal.get();
		if (manager == null || !manager.isOpen()) {
			manager = emf.createEntityManager();
			threadLocal.set(manager);
		}
		return manager;
	}

	public static void closeEntityManager()//实体管理者关闭 
	{
		EntityManager em = threadLocal.get();
		threadLocal.set(null);
		if (em != null)
			em.close();
	}

	public static void beginTransaction() {
		getEntityManager().getTransaction().begin();//开启事务
	}

	public static void commit() {
		getEntityManager().getTransaction().commit();//事务提交
	}

	public static void rollback() {
		getEntityManager().getTransaction().rollback();//事务回滚
	}

	public static Query createQuery(String query) { 
		return getEntityManager().createQuery(query);
	}

	public static void log(String info, Level level, Throwable ex) {
		logger.log(level, info, ex);//日志记录
	}

}
