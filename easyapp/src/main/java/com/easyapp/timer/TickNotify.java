package com.easyapp.timer;
import com.easyapp.core.TypeValidator;
import com.easyapp.task.Task;

public abstract class TickNotify extends Task<TickNotify.Adjust, Long, Long>{

    public final static Adjust PROGRESSIVE = new Adjust(Adjust.Mode.PROGRESSIVE);
    public final static Adjust REGRESSIVE = new Adjust(Adjust.Mode.REGRESSIVE);

    @Override
    protected final Long doTaskInBackground(TickNotify.Adjust[] params) throws Throwable{

        long value = params [0].getStartTime();
        final long tick = params [0].getTickTime();
        final long max = params [0].getMaxTime();
        final Adjust.Mode mode = params [0].getMode();

        if(mode == Adjust.Mode.REGRESSIVE && max > value){
            throw new IllegalArgumentException(
                "maxValue cannot be larger than startValue in mode " + mode.name()
            );
        }

        while(Math.abs(max - value) != 0 && !isTaskDestroyed()){
            postProgressTask(value);
            switch(mode){
                case PROGRESSIVE: 
                    value++; 
                    break;
                case REGRESSIVE: 
                    value--; 
                    break;
            }
            Thread.sleep(tick);
        }
        return value;
    }

    @Override
    protected final void onResultTask(Long result){
        onFinish(result);
    }

    @Override
    protected void onDestroyTask(Long result){
        onResultTask(result);
    }

    @Override
    protected void onPostProgressTask(Long[] post){
        onNotify(post [0]);
    }


    @Override
    protected final void onFailureTask(Throwable throwable){
        onError(throwable);
    }


    protected abstract void onNotify(long time);

    protected abstract void onFinish(long time);

    protected void onError(Throwable throwable){}

    public static class Adjust{
        private long startTime;
        private long maxTime;
        private long tickTime = 1000;
        private Mode mode;

        public Adjust(Mode mode){
            this.mode = TypeValidator.argumentNonNull(mode);
            this.maxTime = (mode == Mode.PROGRESSIVE) ? Long.MAX_VALUE : 0;
        }


        public Adjust setStartTime(long startTime){
            this.startTime = startTime;
            return this;
        }

        protected long getStartTime(){
            return startTime;
        }

        public Adjust setMaxTime(long maxTime){
            this.maxTime = maxTime;
            return this;
        }

        protected long getMaxTime(){
            return maxTime;
        }

        public Adjust setTickTime(long tickTime){
            this.tickTime = tickTime;
            return this;
        }

        protected long getTickTime(){
            return tickTime;
        }

        protected Mode getMode(){
            return mode;
        }


        public enum Mode{
            PROGRESSIVE,
            REGRESSIVE
            }
    }

}
