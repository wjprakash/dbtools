/*
 * Async.fx
 *
 * Created on Jan 09, 2010, 2:44:57 PM
 */

package com.jfxtools.database.util;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;

/**
 * Asyn execution utility
 * @author Winston Prakash
 */
public class Async {
    private Body body;
    private Listener listener;

    public Async(Body body, Listener listener) {
        this.body = body;
        this.listener = listener;
    }
    
    public void start() {
        Thread th = new Thread(new Runnable() {
            public void run() {
                try {
                    if(listener != null) {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                listener.onStarted();
                            }
                        });
                    }
                    try {
                        body.run();
                    } catch (Exception ex) {
                        Util.log(ex);
                    }
                    //Util.log("continuing. listener = "+listener);
                    if(listener != null) {
                        //Util.log("doing a post invoke invoke");
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                //Util.log("doing the real invoke");
                                listener.onFinished();
                            }
                        });
                        //Util.log("done with post invoke invoke");
                    }
                } catch (InterruptedException ex) {
                    Util.log(ex);
                } catch (InvocationTargetException ex) {
                    Util.log(ex);
                }
            }
        });
        th.start();
    }
    

    public static interface Body {
        public void run();
    }
    public static interface Listener {
        public void onStarted();
        public void onFinished();
    }
    
}
