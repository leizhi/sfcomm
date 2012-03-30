/**
 * @author Xabier Echevarría Espinosa
 *
 * The CommandsMicrochip class contains the commands of Microchip's Kit.
 */

package es.deusto.smartlab.rfid.microchip;

import java.io.*;
import es.deusto.smartlab.rfid.RFIDException;

class CommandsMicrochip
{
    private static byte sync_char= 0x40;
    private byte command;
    private byte data[];
    private byte checksum1;
    private byte checksum2;
    private static byte CR = 0x0D;
    private static byte LF = 0x0A;
    
    private CommandsMicrochip()
    {       
        command = 0;
        data = new byte[0];
        checksum1 = 0;
        checksum2 = 0;
    }

    CommandsMicrochip(byte command, byte data[])
    {
        this();
        setCommand(command);
        setData(data);
        calcChecksum();
    }

    static CommandsMicrochip nopMessage()
    {
        return new CommandsMicrochip((byte)78, new byte[0]);        
    }

    static CommandsMicrochip writeMCRF450(byte [] id, int blockNumber, byte [] data)
        throws RFIDException
    {
        String start = Integer.toHexString(blockNumber).toUpperCase();                
        if(start.length() < 2)
            start = "0" + start;

        ByteArrayOutputStream bytes = new ByteArrayOutputStream(4+id.length+(data.length)+((data.length)/8));
        bytes.write((byte)73);
        try {
            bytes.write(id);
        } catch (IOException ex) { }
        bytes.write((byte)44);
        bytes.write(start.charAt(0));
        bytes.write(start.charAt(1));        
        for(int i=0; i<data.length; )
        {
            if(i%8==0)
                bytes.write((byte)44);
            bytes.write(data[i++]);
        }    
        return new CommandsMicrochip((byte)87, bytes.toByteArray());
    }

    static CommandsMicrochip readMCRF450(byte [] id, int startBlockNumber, int endBlockNumber)
        throws RFIDException
    {
        if(id == null)
            throw new RFIDException("The Tag ID is null");
        
        if(startBlockNumber == endBlockNumber)
        {            
            if(startBlockNumber < 6)
            {
                startBlockNumber -= 6;
                throw new RFIDException("The block number must be positive (" + startBlockNumber + ")");
            }                
            if(startBlockNumber > 31)
            {                
                startBlockNumber -= 6;
                throw new RFIDException("The block number must be less or equal to 25, but it is " + startBlockNumber);
            }                
        } else {
            if(startBlockNumber < 6)
            {
                startBlockNumber -= 6;
                throw new RFIDException("The start block number must be positive (" + startBlockNumber + ")");
            }
            if(endBlockNumber <= 6)
            {
                endBlockNumber -= 6;
                throw new RFIDException("The end block number must be greater then zero, but it is " + endBlockNumber);
            }                
            if(endBlockNumber > 31)
            {
                endBlockNumber -= 6;
                throw new RFIDException("The end block number must be less or equal to 25, but it is " + endBlockNumber);            
            }                
        }            
        
        
        String start = Integer.toHexString(startBlockNumber).toUpperCase();                
        if(start.length() < 2)
            start = "0" + start;
        String end = Integer.toHexString(endBlockNumber).toUpperCase();                
        if(end.length() < 2)
            end = "0" + end;
        
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(id.length+4);
        try {
            bytes.write(id);
        } catch (IOException ex) { }
        bytes.write((byte)44);        
        bytes.write(start.charAt(0));
        bytes.write(start.charAt(1));        
        bytes.write((byte)44);
        bytes.write(end.charAt(0));
        bytes.write(end.charAt(1));
        return new CommandsMicrochip((byte)86, bytes.toByteArray());
    }
        
    static CommandsMicrochip resetMessage()    
    {
        return new CommandsMicrochip((byte)82, new byte[0]);
    }        
    
    static CommandsMicrochip stayQuiet()
    {
        return new CommandsMicrochip((byte)77, new byte[] { 73 });
    }
    
    private int getCommand()
    {
        return command;
    }

    private byte[] getData()
    {
        return data;
    }
    
    private byte getChecksum1()
    {
        return checksum1;
    }
        
    private byte getChecksum2()
    {
        return checksum2;
    }

    private void setCommand(byte command)
    {
        this.command = command;
    }

    private void setData(byte data[])
    {
        this.data = data;
    }
    
    private void setChecksum1(byte checksum1)
    {
        this.checksum1 = checksum1;
    }

    private void setChecksum2(byte checksum2)
    {
        this.checksum2 = checksum2;
    }
        
    public byte[] getBytes()
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(6 + getData().length);
        bytes.write(this.sync_char);
        bytes.write(getCommand());
        if(getData().length!=0) 
        {
            try {
                bytes.write(getData());
            } catch (IOException ex) { }
        }            
        bytes.write(getChecksum1());
        bytes.write(getChecksum2());
        bytes.write(this.CR);
        bytes.write(this.LF);
        return bytes.toByteArray();
    }
    
    private void calcChecksum() 
    {                
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(2 + getData().length);
        bytes.write(this.sync_char);        
        bytes.write(getCommand());
        if(getData().length!=0) 
        {
            try {
                bytes.write(getData());
            } catch (IOException ex) { }
        }
        
        byte [] data = bytes.toByteArray();
        
        int checksum = 0;
        for(int i=0; i<data.length; i++) 
        {
            checksum += (int) data[i] & 0xFF;
        }    

        checksum = (byte)(checksum ^ 0xFF) + 1 ;
                               
        String oneByte = Integer.toHexString(checksum & 0xff).toUpperCase();
        if(oneByte.length() < 2)
            oneByte = "0" + oneByte;
       
        setChecksum1((byte)(oneByte.charAt(0)));
        setChecksum2((byte)(oneByte.charAt(1)));
    }
      
}
