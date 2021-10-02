package org.pietroscuttari.managers;


import org.pietroscuttari.data.Lock;

/**
 * lockManager
 */
public class LockManager {
    private LockManager(){
        
    }

    public static boolean blockLock(Lock lock) {
        return true; // Simulation
    }

    public static boolean unlockLock(Lock lock) {
        return true; // Simulation
    }
}