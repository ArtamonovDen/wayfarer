package app.dao;

import app.entity.*;
import app.statistics.ExtendedWeekStatistics;
import app.statistics.StatisticsPerWeek;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.*;

public class DBmanager {

    private EntityManagerFactory managerFactory;

    /**
     * Default constructor with connecting to DB and initializing EntityManagerFactory
     */
    public DBmanager(){
        this.managerFactory = Persistence.createEntityManagerFactory("taskManager.jpa");
    }


    /**
     * Insert any entity into DB
     */
    public void insertEntity(EntityObject entity){
        //Gets some Entity-class as argument and inserts it in DB inside the transaction

        EntityManager eManager = managerFactory.createEntityManager();
        eManager.getTransaction().begin();
        eManager.persist(entity);
        eManager.getTransaction().commit();

        eManager.close();
    }


    /**
     * Insert the list of entities into DB
     */
    public void insertLEntities(ArrayList<EntityObject> enities){
        EntityManager eManager = managerFactory.createEntityManager();
        eManager.getTransaction().begin();
        enities.forEach((e)->eManager.persist(e));
        eManager.getTransaction().commit();
        eManager.close();
    }


    /**
     * Get entity by id from the table
     */
    public <T extends EntityObject> T getById(Class<T> cls, Object id){
        EntityManager eManager = managerFactory.createEntityManager();
        eManager.getTransaction().begin();
        T entity = (T) eManager.find(cls,id);

        eManager.getTransaction().commit();
        eManager.close();
        return entity;
    }


    /**
     * Returns the list of all entities from the table
     */
    public <T extends EntityObject> List<T>  obtainAllFromTable(Class<T> c){
        //Select and Return the whole list of eo Entities  from its table

        EntityManager eManager = managerFactory.createEntityManager();
        eManager.getTransaction().begin();

        List<T> le =  eManager.createQuery("from "+c.getSimpleName(), c).getResultList();

        eManager.getTransaction().commit();
        eManager.close();
        return le;
    }


    /**
     * Removes entity by id
     */
    public void removeEntity (Class entityClass, int id){
        //Remove one entityClass by id

        EntityManager eManager = managerFactory.createEntityManager();
        eManager.getTransaction().begin();

        eManager.remove(eManager.find(entityClass, (Integer)id));

        eManager.getTransaction().commit();

        eManager.close();
    }

    /**
     * Updates entity.
     * @param entity Entity object as a parameter
     */
    public void updateEntity(EntityObject entity){
        //Update got entity
        EntityManager eManager = managerFactory.createEntityManager();
        eManager.getTransaction().begin();

        eManager.merge(entity);
        eManager.getTransaction().commit();
        eManager.close();

    }



//    private StatisticsPerWeek  getTaskStatisticsPerWeek(Calendar CalFrom,Calendar CalTo, EntityManager eManager){
//
//        StatisticsPerWeek stat = new StatisticsPerWeek();
//        stat.week = CalFrom.get(Calendar.WEEK_OF_YEAR);
//        stat.year = CalFrom.get(Calendar.YEAR);
//
//        Date from = CalFrom.getTime();
//        Date to  = CalTo.getTime();
//        BigInteger buff = null;
//
//        eManager.getTransaction().begin();
//
//
//        Query qPriority = eManager.createNativeQuery("select count(priority) from tasks where priority = :prior and createDate between :from and :to group by priority")
//                .setParameter("from",from)
//                .setParameter("to",to);
//        try{
//            buff = (BigInteger)qPriority.setParameter("prior","critical")
//                    .getSingleResult();
//        }catch (NoResultException nre){}
//        stat.critical =  buff==null ? BigInteger.ZERO : buff;
//
//
//
//        try{
//            buff= (BigInteger) eManager.createNativeQuery("select count(*) from tasks where createDate between :from and :to")
//                    .setParameter("from",from)
//                    .setParameter("to",to)
//                    .getSingleResult();
//
//        }catch (NoResultException nre){}
//        stat.total =  buff==null ? BigInteger.ZERO : buff;
//
//
//
//        //Make Map frim the table of tasks group by status and their number
//        Query qStatus = eManager.createNativeQuery("select status, count(*) as num from tasks where createDate between :from and :to group by status");
//        List<Object[]> statusList = null;
//        try{
//            statusList = qStatus.
//                    setParameter("from",from)
//                    .setParameter("to",to).getResultList();
//        }
//        catch (NoResultException nre){}
//
//
//        if (statusList!=null) {
//            Map statusMap = new HashMap<String, BigInteger>();
//
//            for (Object[] s : statusList) {
//                statusMap.put(s[0], s[1]);
//            }
//
//            stat.created =   statusMap.get("Created")==null ? BigInteger.ZERO : (BigInteger) statusMap.get("Created");
//            stat.move =   statusMap.get("Move")==null ? BigInteger.ZERO : (BigInteger) statusMap.get("Move");
//            stat.reopen =   statusMap.get("Reopen")==null ? BigInteger.ZERO : (BigInteger) statusMap.get("Reopen");
//            stat.resolved =   statusMap.get("Resolved")==null ? BigInteger.ZERO : (BigInteger) statusMap.get("Resolved");
//        }
//        else
//        {
//            stat.created = BigInteger.ZERO;
//            stat.move = BigInteger.ZERO;
//            stat.reopen = BigInteger.ZERO;
//            stat.resolved = BigInteger.ZERO;
//
//        }
//        eManager.getTransaction().commit();
//        return stat;
//    }


//    public ArrayList<StatisticsPerWeek> getTasksOverPeriod(Calendar from, Calendar to) {
//        //на вход подаются данные Calendar типа Year, Week of Year, 1st Day of Week !
//        EntityManager eManager = managerFactory.createEntityManager();
//        ArrayList<StatisticsPerWeek> statList = new ArrayList<>();
//        Calendar cur = (Calendar) from.clone();
//        // Calendar cur = from;
//        cur.add(Calendar.WEEK_OF_YEAR, 1);
//        cur.add(Calendar.DAY_OF_WEEK, -1);
//        while (cur.before(to)) {
//
//            statList.add(getTaskStatisticsPerWeek(from, cur, eManager));
//            from.add(Calendar.WEEK_OF_YEAR, 1);
//            cur.add(Calendar.WEEK_OF_YEAR, 1);
//        }
//        eManager.close();
//        return statList;
//    }

    /**
     * Create new ArrayList based on List with WeekStatistics,
     * which contains weekStatistics, total number of task
     * and number of critical tasks per week
     * @param wsList
     * @return ArrayList with ExtendedWeekStatistics objects
     */
    public ArrayList<ExtendedWeekStatistics> getExtendedStatistics(List<WeekStatistics> wsList){

        ArrayList<ExtendedWeekStatistics> ewsList = new ArrayList<>();
        Calendar from = Calendar.getInstance();
        Calendar to;
        BigInteger buff = null;

        EntityManager eManager = managerFactory.createEntityManager();
        eManager.getTransaction().begin();

        for (WeekStatistics ws : wsList){

            from.set(Calendar.YEAR, ws.getKey().getYear());
            from.set(Calendar.WEEK_OF_YEAR, ws.getKey().getWeek());
            to = (Calendar) from.clone();
            from.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            to.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
            to.add(Calendar.DAY_OF_YEAR,1);

            ExtendedWeekStatistics ews = new ExtendedWeekStatistics();
            ews.weekStat = ws;

            //get Critical
            Query qPriority = eManager.createNativeQuery("select count(priority) from tasks where priority = :prior and (:from,:to) OVERLAPS (createDate,dueDate) group by priority")
                    .setParameter("from",from.getTime())
                    .setParameter("to",to.getTime());
            try{
                buff = (BigInteger)qPriority.setParameter("prior","critical")
                        .getSingleResult();
            }catch (NoResultException nre){}
            ews.critical =  buff==null ? BigInteger.ZERO : buff;

            //get Total
            try{
                buff= (BigInteger) eManager.createNativeQuery("select count(*) from tasks where (:from,:to) OVERLAPS (createDate,dueDate)")
                        .setParameter("from",from.getTime())
                        .setParameter("to",to.getTime())
                        .getSingleResult();

            }catch (NoResultException nre){}
            ews.total =  buff==null ? BigInteger.ZERO : buff;

            ewsList.add(ews);
        }
        eManager.getTransaction().commit();
        eManager.close();
        return ewsList;
    }


    /**
     * Disconnects DB and cleans up resources
     */
    public void close(){
        managerFactory.close();
    }


}






