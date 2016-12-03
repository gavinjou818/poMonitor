package pomonitor.entity;

import java.util.Date;
import java.util.List;

/**
 * Interface for NewsDAO.
 * 
 * @author MyEclipse Persistence Tools
 */

/*
 *  zhouzhifeng 
 *  2016年12月2日
 *  标记注释
 */
public interface INewsDAO {
	/**
	 * Perform an initial save of a previously unsaved News entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#persist(Object)
	 * EntityManager#persist} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * INewsDAO.save(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            News entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(News entity);//保存

	/**
	 * Delete a persistent News entity. This operation must be performed within
	 * the a database transaction context for the entity's data to be
	 * permanently deleted from the persistence store, i.e., database. This
	 * method uses the {@link javax.persistence.EntityManager#remove(Object)
	 * EntityManager#delete} operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * INewsDAO.delete(entity);
	 * EntityManagerHelper.commit();
	 * entity = null;
	 * </pre>
	 * 
	 * @param entity
	 *            News entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(News entity);//删除

	/**
	 * Persist a previously saved News entity and return it or a copy of it to
	 * the sender. A copy of the News entity parameter is returned when the JPA
	 * persistence mechanism has not previously been tracking the updated
	 * entity. This operation must be performed within the a database
	 * transaction context for the entity's data to be permanently saved to the
	 * persistence store, i.e., database. This method uses the
	 * {@link javax.persistence.EntityManager#merge(Object) EntityManager#merge}
	 * operation.
	 * 
	 * <pre>
	 * EntityManagerHelper.beginTransaction();
	 * entity = INewsDAO.update(entity);
	 * EntityManagerHelper.commit();
	 * </pre>
	 * 
	 * @param entity
	 *            News entity to update
	 * @return News the persisted News entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public News update(News entity);//更新

	public News findById(Integer id);//通过Id找新闻

	/**
	 * Find all News entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the News property to query
	 * @param value
	 *            the property value to match
	 * @return List<News> found by query
	 */
	public List<News> findByProperty(String propertyName, Object value);//找新闻通过属性

	public List<News> findById(Object id);//找新闻集通过id

	public List<News> findByTitle(Object title);//通过标题找新闻

	public List<News> findByUrl(Object url);//通过Url找新闻

	public List<News> findByContent(Object content);//通过内容找新闻

	public List<News> findByWeb(Object web);//通过web找新闻集

	public List<News> findByAllContent(Object allContent);//通过allContent找内容

	public List<News> findByKeyWords(Object keyWords);//通过关键词找新闻

	public List<News> findByContentPath(Object contentPath);//通过contentPath找新闻集

	public List<News> findByFailedCount(Object failedCount);//通过FailedCount找新闻集
	
	public List<News> findByIsFinsh(Object isFinsh);//通过isFinish找新闻集

	public List<News> findByIsFailed(Object isFailed);//通过isFailed找新闻集

	public List<News> findByIsWorking(Object isWorking);//通过isworking找新闻集

	/**
	 * Find all News entities.
	 * 
	 * @return List<News> all News entities
	 */
	public List<News> findAll();//找到所有实体
}