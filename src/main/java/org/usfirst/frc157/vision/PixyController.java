/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc157.vision;
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

    public PixyController(Port port, int address, int signatures)
    {
        this.buffer = new byte[signatures * 30];
        this.cam = new I2C(port, address);
        this.signatures = signatures;
    }
    public int unsign(byte n)
    {
        return n<0?256+n:n;
    }
    private int convertToShort(byte a, byte b)
    {
        return (unsign(b)<<8)|unsign(a);
    }
    public target read()
    {
        target retval = new target(); 
        this.cam.readOnly(this.buffer, this.buffer.length);
        boolean foundFirst = false;
        int previous = 0;
        boolean running = true;
        int i = 0;
        while (running)
        {
            if (i > this.buffer.length-4)
            {
                retval.found = false;
                running = false;
                return null;
            }
            else if (foundFirst)
            {
                if (i+9 > this.buffer.length-1)
                {
                    retval.found = false;
                    return null;
                }
                else
                {
                    retval.sig = convertToShort(this.buffer[i+0], this.buffer[i+1]);
                    retval.x = convertToShort(this.buffer[i+2], this.buffer[i+3]);
                    retval.y = convertToShort(this.buffer[i+4], this.buffer[i+5]);
                    retval.width = convertToShort(this.buffer[i+6], this.buffer[i+7]);
                    retval.height = convertToShort(this.buffer[i+8], this.buffer[i+9]);
                    retval.found = true;
                }
                running = false;
            }
            else
            {
                int word = convertToShort(this.buffer[i], this.buffer[i+1]);
                if (word == 0xaa55 && word == previous)
                {
                    foundFirst = true;
                    i += 4;
                }
                else
                {
                    i += 2;
                }
                previous = word;
            }
        }
        return retval;

    }
    public class target{
        public boolean found;
        public double x;
        public double y;
        public double sig;
        public double width;
        public double height;
    }
}

