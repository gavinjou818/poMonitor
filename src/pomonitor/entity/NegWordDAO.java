package pomonitor.entity;

// default package

import java.util.List;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import pomonitor.entity.EntityManagerHelper;

/**
 * A data access object (DAO) providing persistence and search support for
 * NegWord entities. Transaction control of the save(), update() and delete()
 * operations must be handled externally by senders of these methods or must be
 * manually added to each of these methods for data to be persisted to the JPA
 * datastore.
 * 
 * @see .NegWord
 * @author MyEclipse Persistence Tools
 */
public class NegWordDAO implements INegWordDAO
{
	// property constants
	public static final String WORD = "word";

	private EntityManager getEntityManager() {
		return EntityManagerHelper.getEntityManager();
	}

	/**
	 * Perform an initial save of a previously unsaved NegWord entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * NegWordDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            NegWord entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(NegWord entity) {
		EntityManagerHelper.log("saving NegWord instance", Level.INFO, null);
		try {
			getEntityManager().persist(entity);
			EntityManagerHelper.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent NegWord entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * NegWordDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            NegWord entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(NegWord entity) {
		EntityManagerHelper.log("deleting NegWord instance", Level.INFO, null);
		try {
			entity = getEntityManager().getReference(NegWord.class,
					entity.getId());
			getEntityManager().remove(entity);
			EntityManagerHelper.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			EntityManagerHelper.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved NegWord entity and return it or a copy of it
	 * to the sender. A copy of the NegWord entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = NegWordDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            NegWord entity to update
	 * @return NegWord the persisted NegWord entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public NegWord update(NegWord entity) {
		EntityManagerHelper.log("updating NegWord instance", Level.INFO, null);
		try {
			NegWord result = getEntityManager().merge(entity);
			EntityManagerHelper.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public NegWord findById(Integer id) {
		EntityManagerHelper.log("finding NegWord instance with id: " + id,
				Level.INFO, null);
		try {
			NegWord instance = getEntityManager().find(NegWord.class, id);
			return instance;
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all NegWord entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the NegWord property to query
	 * @param value
	 *            the property value to match
	 * @return List<NegWord> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<NegWord> findByProperty(String propertyName, final Object value) {
		EntityManagerHelper.log("finding NegWord instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from NegWord model where model."
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

	public List<NegWord> findByWord(Object word) {
		return findByProperty(WORD, word);
	}

	/**
	 * Find all NegWord entities.
	 * 
	 * @return List<NegWord> all NegWord entities
	 */
	@SuppressWarnings("unchecked")
	public List<NegWord> findAll() {
		EntityManagerHelper.log("finding all NegWord instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from NegWord model";
			Query query = getEntityManager().createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			EntityManagerHelper.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}