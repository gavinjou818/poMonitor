package pomonitor.entity;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * A data access object (DAO) providing persistence and search support for
 * Briefing entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 * 
 * @see pomonitor.entity.Briefing
 * @author MyEclipse Persistence Tools
 */
public class BriefingDAO implements IBriefingDAO {
	// property constants
	public static final String BASEPATH = "basepath";
	public static final String NAME = "name";
	public static final String ENTITYURL = "entityurl";
	public static final String DOCPATH = "docpath";
	public static final String PDFPATH = "pdfpath";
	public static final String USERID="userid";
	public static final String VIRTUALNAME="virtualname";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved Briefing entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * BriefingDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            Briefing entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Briefing entity) {
		EntityManagerHelper.log("saving Briefing instance", Level.INFO, null);
		try {
			EntityManager em=getEntityManager();
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
			em.close();
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent Briefing entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * BriefingDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            Briefing entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Briefing entity) {
		EntityManagerHelper.log("deleting Briefing instance", Level.INFO, null);
		try {
			
			
			EntityManager em=getEntityManager();
			em.getTransaction().begin();
			entity = em.getReference(Briefing.class,
					entity.getId());
			em.remove(entity);
			em.getTransaction().commit();
			em.close();

			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved Briefing entity and return it or a copy of it
	 * to the sender. A copy of the Briefing entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = BriefingDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            Briefing entity to update
	 * @return Briefing the persisted Briefing entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public Briefing update(Briefing entity) {
		EntityManagerHelper.log("updating Briefing instance", Level.INFO, null);
		try {
			
			EntityManager em=getEntityManager();
			em.getTransaction().begin();
			Briefing result = getEntityManager().merge(entity);
			em.getTransaction().commit();
			em.close();
			
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public Briefing findById(Integer id) {
		EntityManagerHelper.log("finding Briefing instance with id: " + id,
				Level.INFO, null);
		try {
			Briefing instance = getEntityManager().find(Briefing.class, id);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all Briefing entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Briefing property to query
	 * @param value
	 *            the property value to match
	 * @return List<Briefing> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<Briefing> findByProperty(String propertyName, final Object value) {
		EntityManagerHelper.log("finding Briefing instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from Briefing model where model."
					+ propertyName + "= :propertyValue";
			Query query = getEntityManager().createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find by property name failed",
					Level.SEVERE, re);
			throw re;
		}
	}

	public List<Briefing> findByBasepath(Object basepath) {
		return findByProperty(BASEPATH, basepath);
	}

	public List<Briefing> findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List<Briefing> findByEntityurl(Object entityurl) {
		return findByProperty(ENTITYURL, entityurl);
	}

	public List<Briefing> findByDocpath(Object docpath) {
		return findByProperty(DOCPATH, docpath);
	}

	public List<Briefing> findByPdfpath(Object pdfpath) {
		return findByProperty(PDFPATH, pdfpath);
	}
   
	public List<Briefing> findByUserid(Object userid) {
		return findByProperty(USERID, userid);
	}
	public List<Briefing> findByVirtualname(Object virtualname) {
		return findByProperty(VIRTUALNAME, virtualname);
	}
	/**
	 * Find all Briefing entities.
	 * 
	 * @return List<Briefing> all Briefing entities
	 */
	@SuppressWarnings("unchecked")
	public List<Briefing> findAll() {
		EntityManagerHelper.log("finding all Briefing instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from Briefing model";
			Query query = getEntityManager().createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}