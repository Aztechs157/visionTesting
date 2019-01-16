/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc157.vision;
import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

/**
 * without this comment there are 20 new errors
 */
public class PixyController extends Thread {
    I2C cam;
    byte[] buffer;
    int signatures;
    int START = 43605; //0xaa55
    target[] targets;
    

    public PixyController(Port port, int address, int signatures)
    {
        this.buffer = new byte[2];
        this.cam = new I2C(port, address);
        this.signatures = signatures;
        this.targets = new target[signatures];
    }
    public void run()
    {
        byte[] dump = new byte[10];
        try
        {
            byte[] targetBytes = new byte[12];
            int w, lastw;
            lastw = 0xffff;
            while (!Thread.interrupted())
            {
                boolean testing = this.cam.readOnly(this.buffer, 2);
                w = convertToShort(this.buffer[0], this.buffer[1]);
                if (w == 0xaa55 && lastw == 0xaa55)
                {
                    this.cam.readOnly(targetBytes, 12);
                    this.targets = new target[this.signatures];
                    //read frame
                    target temp = new target();
                    temp.sig = convertToShort(targetBytes[2], targetBytes[3]);
                    temp.x = convertToShort(targetBytes[4], targetBytes[5]);
                    temp.y = convertToShort(targetBytes[6], targetBytes[7]);
                    temp.width = convertToShort(targetBytes[8], targetBytes[9]);
                    temp.height = convertToShort(targetBytes[10], targetBytes[11]);
                    synchronized(targets){this.targets[0] = temp;}
                    w = 0;
                    lastw = 0;
                }
                else if (w == 0x55aa)
                {
                    this.cam.readOnly(dump, 1);
                }
                else if (w == 0 && lastw == 0)
                {
                    TimeUnit.MICROSECONDS.sleep(10);
                }
                lastw = w;
                
            }
        }
        catch(InterruptedException ie)
        {
            Thread.currentThread().interrupt();
        }
    }
    public int unsign(byte n)
    {
        return n<0?256+n:n;
    }
    private int convertToShort(byte a, byte b)
    {
        return (unsign(b)<<8)|unsign(a);
    }
    public target[] read()
    {
        return this.targets;

    }
    public class target{
        public double x;
        public double y;
        public int sig;
        public double width;
        public double height;

    }
}

