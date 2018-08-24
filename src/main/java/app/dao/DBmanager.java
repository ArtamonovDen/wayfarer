package app.dao;

import app.entity.*;
import app.statistics.ExtendedWeekStatistics;
import app.statistics.StatisticsPerWeek;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.*;

public class DBmanager {

    private EntityManagerFactory managerFactory;

    public DBmanager(){
        //default constructor with connecting to DB and initializing EntityManagerFactory
        this.managerFactory = Persistence.createEntityManagerFactory("taskManager.jpa");
    }

    public void insertEntity(EntityObject entity){
        //Gets some Entity-class as argument and inserts it in DB inside the transaction

        EntityManager eManager = managerFactory.createEntityManager();
        eManager.getTransaction().begin();
        eManager.persist(entity);
        eManager.getTransaction().commit();

        eManager.close();
    }
    public void insertLEntities(ArrayList<EntityObject> enities){
        EntityManager eManager = managerFactory.createEntityManager();
        eManager.getTransaction().begin();
        enities.forEach((e)->eManager.persist(e));
        eManager.getTransaction().commit();
        eManager.close();
    }

//    public List<EntityObject>  obtainAllFromTable(String eo){
//        //Select and Return the whole list of eo Entities  from its table
//
//        EntityManager eManager = managerFactory.createEntityManager();
//        eManager.getTransaction().begin();
//
//        List<EntityObject> le =  eManager.createQuery("from "+eo, EntityObject.class).getResultList();
//
//        eManager.getTransaction().commit();
//        eManager.close();
//        return le;
//    }

    public <T extends EntityObject> T getById(Class<T> cls, Object id){
        EntityManager eManager = managerFactory.createEntityManager();
        eManager.getTransaction().begin();
        T entity = (T) eManager.find(cls,id);

        eManager.getTransaction().commit();
        eManager.close();
        return entity;
    }

    public <T extends EntityObject> List<T>  obtainAllFromTable(Class<T> c){
        //Select and Return the whole list of eo Entities  from its table

        EntityManager eManager = managerFactory.createEntityManager();
        eManager.getTransaction().begin();

        List<T> le =  eManager.createQuery("from "+c.getSimpleName(), c).getResultList();

        eManager.getTransaction().commit();
        eManager.close();
        return le;
    }

    public void removeEntity (Class entityClass, int id){
        //Remove one entityClass by id

        EntityManager eManager = managerFactory.createEntityManager();
        eManager.getTransaction().begin();

        eManager.remove(eManager.find(entityClass, (Integer)id));

        eManager.getTransaction().commit();

        eManager.close();
    }

    public void updateEntity(EntityObject entity){
        //Update got entity
        EntityManager eManager = managerFactory.createEntityManager();
        eManager.getTransaction().begin();

        eManager.merge(entity);
        eManager.getTransaction().commit();
        eManager.close();

    }

//    public ArrayList<prExtendedTask> getExtendedTasks(){
//        EntityManager eManager = managerFactory.createEntityManager();
//        eManager.getTransaction().begin();
//
//        List<Object[]> list = null;
//
//        try {
//            list = eManager.createNativeQuery(
//                    "SELECT t.task_id, t.task_name, t.description, t.priority, t.customer_id, " +
//                            "t.depart_id,t.status, t.createdate, t.duedate, t.assignee, d.depart_name," +
//                            "c.customer_name, ss.first_name, ss.middle_name, ss.last_name " +
//                            "FROM tasks t JOIN depart d  ON t.depart_id = d.depart_id " +
//                            "JOIN customers c ON t.customer_id = c.customer_id " +
//                            "JOIN support_staff as ss ON t.assignee = ss.sup_id").getResultList();
//        }catch(NoResultException nre){}
//
//        eManager.getTransaction().commit();
//        eManager.close();
//
//        ArrayList<prExtendedTask> eTaskList =  new ArrayList<>();
//
//        prExtendedTask eTask = new prExtendedTask();
//
//        for(Object[] om : list){
//
//            eTask.task.setId((Integer)om[0]);
//            eTask.task.setName((String)om[1]);
//            eTask.task.setDescription((String)om[2]);
//            eTask.task.setPriority(Priority.getByString( (String)om[3]));
//            eTask.task.setCustomer_id((Integer)om[4]);
//            eTask.task.setDepart_id((Integer)om[5]);
//            eTask.task.setStatus(Status.getByString( (String)om[6]));
//            eTask.task.setCreatedate((Date)om[7]);
//            eTask.task.setDuedate((Date)om[8]);
//            eTask.task.setAssignee((Integer)om[9]);
//            eTask.depart.setName((String)om[10]);
//            eTask.customer.setName((String)om[11]);
//            Name name =  new Name();
//            name.setFirstName((String)om[12]);
//            name.setMiddleName((String)om[13]);
//            name.setLastName((String)om[14]);
//            eTask.assignee.setName(name);
//
//            eTask.depart.setId(eTask.task.getDepart_id());
//            eTask.customer.setId(eTask.task.getCustomer_id());
//            eTask.assignee.setId(eTask.task.getCustomer_id());
//
//            eTaskList.add(eTask);
//        }
//
//        return eTaskList;
//    }


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
//         eManager.getTransaction().commit();
//
//
//        return stat;
//
//    }
//
//
//    public ArrayList<StatisticsPerWeek> getTasksOverPeriod(Calendar from, Calendar to){
//        //на вход подаются данные Calendar типа Year, Week of Year, 1st Day of Week !
//        EntityManager eManager = managerFactory.createEntityManager();
//        ArrayList<StatisticsPerWeek>statList = new ArrayList<>();
//        Calendar cur = (Calendar)from.clone();
//       // Calendar cur = from;
//        cur.add(Calendar.WEEK_OF_YEAR,1);
//        cur.add(Calendar.DAY_OF_WEEK,-1);
//        while(cur.before(to)){
//
//            statList.add(getTaskStatisticsPerWeek(from, cur,eManager));
//            from.add(Calendar.WEEK_OF_YEAR,1);
//            cur.add(Calendar.WEEK_OF_YEAR,1);
//        }
//        eManager.close();
//        return statList;
//
//    }

    private StatisticsPerWeek  getTaskStatisticsPerWeek(Calendar CalFrom,Calendar CalTo, EntityManager eManager){

        StatisticsPerWeek stat = new StatisticsPerWeek();
        stat.week = CalFrom.get(Calendar.WEEK_OF_YEAR);
        stat.year = CalFrom.get(Calendar.YEAR);

        Date from = CalFrom.getTime();
        Date to  = CalTo.getTime();
        BigInteger buff = null;

        eManager.getTransaction().begin();


        Query qPriority = eManager.createNativeQuery("select count(priority) from tasks where priority = :prior and createDate between :from and :to group by priority")
                .setParameter("from",from)
                .setParameter("to",to);
        try{
            buff = (BigInteger)qPriority.setParameter("prior","critical")
                    .getSingleResult();
        }catch (NoResultException nre){}
        stat.critical =  buff==null ? BigInteger.ZERO : buff;



        try{
            buff= (BigInteger) eManager.createNativeQuery("select count(*) from tasks where createDate between :from and :to")
                    .setParameter("from",from)
                    .setParameter("to",to)
                    .getSingleResult();

        }catch (NoResultException nre){}
        stat.total =  buff==null ? BigInteger.ZERO : buff;



        //Make Map frim the table of tasks group by status and their number
        Query qStatus = eManager.createNativeQuery("select status, count(*) as num from tasks where createDate between :from and :to group by status");
        List<Object[]> statusList = null;
        try{
            statusList = qStatus.
                    setParameter("from",from)
                    .setParameter("to",to).getResultList();
        }
        catch (NoResultException nre){}


        if (statusList!=null) {
            Map statusMap = new HashMap<String, BigInteger>();

            for (Object[] s : statusList) {
                statusMap.put(s[0], s[1]);
            }

            stat.created =   statusMap.get("Created")==null ? BigInteger.ZERO : (BigInteger) statusMap.get("Created");
            stat.move =   statusMap.get("Move")==null ? BigInteger.ZERO : (BigInteger) statusMap.get("Move");
            stat.reopen =   statusMap.get("Reopen")==null ? BigInteger.ZERO : (BigInteger) statusMap.get("Reopen");
            stat.resolved =   statusMap.get("Resolved")==null ? BigInteger.ZERO : (BigInteger) statusMap.get("Resolved");
        }
        else
        {
            stat.created = BigInteger.ZERO;
            stat.move = BigInteger.ZERO;
            stat.reopen = BigInteger.ZERO;
            stat.resolved = BigInteger.ZERO;

        }
        eManager.getTransaction().commit();
        return stat;
    }


    public ArrayList<StatisticsPerWeek> getTasksOverPeriod(Calendar from, Calendar to) {
        //на вход подаются данные Calendar типа Year, Week of Year, 1st Day of Week !
        EntityManager eManager = managerFactory.createEntityManager();
        ArrayList<StatisticsPerWeek> statList = new ArrayList<>();
        Calendar cur = (Calendar) from.clone();
        // Calendar cur = from;
        cur.add(Calendar.WEEK_OF_YEAR, 1);
        cur.add(Calendar.DAY_OF_WEEK, -1);
        while (cur.before(to)) {

            statList.add(getTaskStatisticsPerWeek(from, cur, eManager));
            from.add(Calendar.WEEK_OF_YEAR, 1);
            cur.add(Calendar.WEEK_OF_YEAR, 1);
        }
        eManager.close();
        return statList;
    }

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
            from.set(Calendar.DAY_OF_WEEK,1);
            from.set(Calendar.DAY_OF_WEEK,7);

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

    public void close(){
        managerFactory.close();
    }


}






