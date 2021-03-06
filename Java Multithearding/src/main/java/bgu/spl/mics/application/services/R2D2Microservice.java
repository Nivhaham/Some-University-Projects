package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.Main;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadCast;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    long duration;
    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration=duration;
    }

    @Override
    protected void initialize() {
        bus.register(this);
        subscribeEvent(DeactivationEvent.class,e->{
            try
            {
                Thread.sleep(duration);
            }
            catch (InterruptedException ex)
            {

            }
            complete(e,true);
            diary.setR2D2Deactivate(System.currentTimeMillis());
            sendEvent(new BombDestroyerEvent());
        });

        subscribeBroadcast(TerminationBroadCast.class, e->{this.terminate();
        diary.setR2D2Terminate(System.currentTimeMillis());
          //  System.out.println("r2 terminated");
        });
        Main.latch.countDown();

    }
}
