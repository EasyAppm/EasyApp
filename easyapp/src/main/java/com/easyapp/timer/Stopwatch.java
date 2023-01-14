package com.easyapp.timer;
import com.easyapp.task.SimpleTask;
import com.easyapp.timer.Stopwatch.Status;

public class Stopwatch{

    private Status status = Status.STOPPED;
    private long currentTime;
    private WorkTimer workTimer = new WorkTimer();


    private final OnUpdateTimeListener onUpdateTimeListener;

    public Stopwatch(OnUpdateTimeListener onUpdateTimeListener){
        this.onUpdateTimeListener = onUpdateTimeListener;
    }


    public void start(){
        if(!workTimer.isRunning()){
            workTimer = new WorkTimer();
            workTimer.execute(
                WorkTimer.PROGRESSIVE
                .setTickTime(1)
                .setStartTime(currentTime)
            );
            status = status.RUNNING;
        }
    }


    public void stop(){
        workTimer.destroyTask();
        currentTime = 0;
        status = status.STOPPED;
    }

    public void pause(){
        workTimer.destroyTask();
        status = status.PAUSED;
    }

    public Status getStatus(){
        return status;
    }

    public enum Status{
        RUNNING, STOPPED, PAUSED
        }

    private class WorkTimer extends TickNotify{

        @Override
        protected void onNotify(long time){
            currentTime = time;
            onUpdateTimeListener.onUpdateTime(time);
        }

        @Override
        protected void onFinish(long time){
            currentTime = time;
            onUpdateTimeListener.onUpdateTime(time);
        }
    }

}
