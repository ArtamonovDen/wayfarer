package app.generator;

import app.dao.DBmanager;
import app.entity.*;

import java.util.*;

public class Generator{
    final static int depNum = 4;
    final static int custNum = 8;
    final static int statusNum = Status.values().length;
    final static int priorNum = Priority.values().length;
    final static int minWeek = 31;
    final static int maxWeek = 35;
    final static int year = 2018;

    static private DBmanager Lui = null;
    private static HashMap<Integer, WeekStatistics> statMap;
    private static HashMap<Integer, AssigneeStatistics> aStatMap;
    private static ArrayList<Task>  tasks;

    private static HashMap<Integer, Integer> pgHash;

    public static int getPizzaGuy(int depart){
        if (pgHash==null){
            pgHash = new HashMap<>();
            List<PizzaGuy> pgList = Lui.obtainAllFromTable(PizzaGuy.class);
            for(PizzaGuy pg : pgList)
                pgHash.put(pg.getDepart_id(), pg.getPizzaguy_id());
        }

        for(Integer key : pgHash.keySet()){
            System.out.println(key+": "+pgHash.get(key).toString());
        }

        return pgHash.get(depart);

    }



    public static void main(String[] args) {
        int n=40;
        Lui = new DBmanager();
        tasks = new ArrayList<Task>();
        statMap = new HashMap<>();
        aStatMap = new HashMap<>();


         for(int i=1;i<=n;i++){
            tasks.add(generateTask());
        }

        tasks.sort(Comparator.comparing(Task::getCreatedate));
         //ArrayList<EntityObject> eoList = new ArrayList<>(tasks);
        for(Integer key : statMap.keySet()){
            System.out.println(key+": "+statMap.get(key).getKey().toString()+" "+statMap.get(key).getCreated());
        }

        try{
            Lui.insertLEntities(new ArrayList<>(tasks));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        try{
            Lui.insertLEntities(new ArrayList<>(statMap.values()));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        try{
            Lui.insertLEntities(new ArrayList<>(aStatMap.values()));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

       Lui.close();
    }


    public static int randBetween(int start, int end,Random random) {
        return start + random.nextInt(end-start);
    }

    public static Task generateTask(){
        Task task = new Task();
        Random random = new Random();
        String name = "Task ";
        Calendar cal = Calendar.getInstance();

        task.setName(name + (char)(random.nextInt(25)+65)+(char)(random.nextInt(25)+65)+(char)(random.nextInt(10)+48));
        task.setDepart_id(random.nextInt(depNum)+1);
        task.setCustomer_id(random.nextInt(custNum)+1);
        task.setDescription("Test Description");
        Integer r = getPizzaGuy(task.getDepart_id() );
        if (r!=null)
            task.setAssignee(r );
        task.setStatus(Status.getByOrder(random.nextInt(statusNum)+1));
        task.setPriority(Priority.getByOrder(random.nextInt(priorNum)+1));



//        cal.set(Calendar.YEAR, 2017);
//        cal.set(Calendar.MONTH,random.nextInt(12)+1 );
//        cal.set(Calendar.DATE,random.nextInt(29)+1 );
        cal.set(Calendar.YEAR,year);
        cal.set(Calendar.WEEK_OF_YEAR,randBetween(minWeek,maxWeek,random));
        cal.set(Calendar.DAY_OF_WEEK,random.nextInt(29)+1);


        //Statistics
        int key = cal.get(Calendar.YEAR)*100  + cal.get(Calendar.WEEK_OF_YEAR);
        if(statMap.get(key)==null){
            WeekStatistics ws = new WeekStatistics();
            ws.setKey(new WeekStatisticsKey(cal.get(Calendar.YEAR),cal.get(Calendar.WEEK_OF_YEAR)));
            ws.inc(task.getStatus());
            statMap.put(key,ws);
        }
        else{
            statMap.get(key).inc(task.getStatus());
        }
        //----------

        //Assignee statistics
        key = task.getAssignee();
        if(aStatMap.get(key)==null){
            AssigneeStatistics as = new AssigneeStatistics();
            as.setId(key);
            aStatMap.put(key,as);
        }
        aStatMap.get(key).incIncome();
        switch (task.getStatus()){
            case Resolved: aStatMap.get(key).incResolved();break;
            case Move:aStatMap.get(key).incMoved();break;
            case Closed:aStatMap.get(key).incResolved();break;
            default:break;
        }

        //-------------

        task.setCreatedate(cal.getTime());
        switch (task.getPriority()){
            case normal: cal.add(Calendar.DAY_OF_YEAR, 15); break;
            case major: cal.add(Calendar.DAY_OF_YEAR,8 ); break;
            case minor: cal.add(Calendar.DAY_OF_YEAR, 40); break;
            case blocker: cal.add(Calendar.DAY_OF_YEAR,3); break;
            case critical: cal.add(Calendar.DAY_OF_YEAR, 5); break;
            default:cal.add(Calendar.DAY_OF_YEAR, 15); break;

        }
        task.setDuedate(cal.getTime());
        return task;
    }

}