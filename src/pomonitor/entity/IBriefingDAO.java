package pomonitor.entity;

import java.util.Date;
import java.util.List;

/**
 * Interface for BriefingDAO.
 * 
 * @author MyEclipse Persistence Tools
 */

public interface IBriefingDAO {
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
	 * IBriefingDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            Briefing entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(Briefing entity);

	/**
	 * Delete a persistent Briefing entity. This operation must be performed
	 * within the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * IBriefingDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            Briefing entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(Briefing entity);

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
	 * entity = IBriefingDAO.update(entity);
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
	public Briefing update(Briefing entity);

	public Briefing findById(Integer id);

	/**
	 * Find all Briefing entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the Briefing property to query
	 * @param value
	 *            the property value to match
	 * @return List<Briefing> found by query
	 */
	public List<Briefing> findByProperty(String propertyName, Object value);

	public List<Briefing> findByBasepath(Object basepath);

	public List<Briefing> findByName(Object name);

	public List<Briefing> findByEntityurl(Object entityurl);

	public List<Briefing> findByDocpath(Object docpath);

	public List<Briefing> findByPdfpath(Object pdfpath);

	/**
	 * Find all Briefing entities.
	 * 
	 * @return List<Briefing> all Briefing entities
	 */
	public List<Briefing> findAll();
}