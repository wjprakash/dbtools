/*
 * AsyncTask.fx
 *
 * Created on Mar 17, 2009, 7:23:27 PM
 */

package com.jfxtools.database.util;

import java.lang.Runnable;
import java.lang.Void;

import javafx.async.JavaTaskBase;
import javafx.async.RunnableFuture;

import com.sun.javafx.runtime.Entry;

import java.lang.Throwable;

/**
 * Async task that uses Java Async task
 * @author Winston Prakash
 */

public class AsyncTask {
    public-init var action:function():Object;
    public-init var onStarted:function():Void;
    public-init var onFinished:function():Void;

    var javaTaskBase;

    public function start():Void {
        var runnableProxy = Runnable{
           override function run() {
               try {
                    action();
               } catch (thr:Throwable) {
                    thr.printStackTrace();
               }

               Entry.deferAction(Runnable {
                    public override function run() {
                       onFinished();
                    }
               });
           }
        }

        javaTaskBase = JavaTaskBase{
           protected override function create() : RunnableFuture {
                return new AsyncJavaTask(runnableProxy);
           }
        }
        onStarted();
        javaTaskBase.start();
    }

    public function cancel():Void{
        if ((javaTaskBase != null) and javaTaskBase.started){
           javaTaskBase.stop();
        }
    }

}
