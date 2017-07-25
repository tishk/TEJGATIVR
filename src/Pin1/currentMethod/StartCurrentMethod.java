package Pin1.currentMethod;


import Pin1.currentMethod.Avacas.AvaCas;
import Pin1.currentMethod.ORM.ORM;

import java.util.concurrent.*;

/**
 * Created by Administrator on 25/05/2015.
 */
public class StartCurrentMethod {
    public StartCurrentMethod() throws Exception {
        initialize();
    }
    public  Boolean StartedPin1ORM=false;
    public  Boolean StartedPin1AvaCas=false;
    private ORM     orm=null;
    private AvaCas  avacas=null;

    public void    initialize() throws Exception {
         if (StartedORM())     StartedPin1ORM=true;

        //if  (StartedAvaCas()) StatusOfApplication.StartedPin1Avacas=true;
    }
    public boolean StartedORM() throws ExecutionException, InterruptedException {
        boolean ResultOfCreateORMThread=false;

      //  ExecutorService executorService = Executors.newSingleThreadExecutor();
      //  Future future = executorService.submit(new Runnable() {
            //public void run() {
                try {
                    orm=new ORM();
                } catch (Exception e) {
                    e.printStackTrace();
                }
           // }
      //  });

     //   if  (future.get()!= null) ResultOfCreateORMThread=false; else ResultOfCreateORMThread=true;
        return ResultOfCreateORMThread && orm.ResultOfRunning;
    }
    public boolean StartedAvaCas() throws ExecutionException, InterruptedException {
        boolean ResultOfCreateAvaCasThread=false;

      //  ExecutorService executorService = Executors.newSingleThreadExecutor();
       // Future future = executorService.submit(new Runnable() {
          //  public void run() {
                try {
                    avacas=new AvaCas();
                } catch (Exception e) {
                    e.printStackTrace();
                }
           // }
      //  });

     //   if  (future.get()!= null) ResultOfCreateAvaCasThread=false; else ResultOfCreateAvaCasThread=true;
        return ResultOfCreateAvaCasThread && avacas.ResultOfRunning;
    }

}
