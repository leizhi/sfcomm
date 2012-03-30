/**
 * @author Xabier Echevarría Espinosa
 *
 * The CommandsTexas class contains the request packets of Texas Instruments's Kit.
 */

package es.deusto.smartlab.rfid.texas;

import java.io.*;
import es.deusto.smartlab.rfid.*;

class CommandsTexas
{
    public static final byte SOF = 1;
    private int length;
    private byte deviceId;
    private byte command1;
    private byte command2;
    private byte data[];
    private int lrc1;
    private int lrc2;    
    
    private CommandsTexas()
    {
        length = 0;
        deviceId = 3;
        command1 = 0;
        command2 = 0;
        data = new byte[0];
        lrc1 = 0;
        lrc2 = 0;
    }

    CommandsTexas(byte command1, byte command2, byte data[])
    {
        this();
        setFirstCommand(command1);
        setSecondCommand(command2);
        setData(data);
        calculateFields();
    }

    private void calculateFields()
    {
        setLength(6 + (getData() == null ? 0 : getData().length) + 2);
        calcCRC(getBytesWithoutCRC(), getLength() - 2);
    }

    static CommandsTexas setDriverRequest(byte deviceMask, byte state)
    {
        byte data[] = new byte[2];
        data[0] = deviceMask;
        data[1] = state;
        return new CommandsTexas((byte)1, (byte)67, data);
    }

    static CommandsTexas findTokenRequest()
    {
        return new CommandsTexas((byte)1, (byte)65, new byte[] { 10 });        
    }
    
    static CommandsTexas findTokenISO15693Request()
    {
        return new CommandsTexas((byte)4, (byte)65, new byte[] { 10 });
    }
    
    static CommandsTexas findTokenTagitRequest()
    {
        return new CommandsTexas((byte)5, (byte)65, new byte[] { 10 });
    }
    
    static CommandsTexas readSingleBlockISO15693Request(byte[] id, int blockNumber)
        throws RFIDException
    {
        if(id == null)
            throw new RFIDException("The tag ID is null");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(11);
        bytes.write(0);
        bytes.write(1);
        bytes.write((byte)(blockNumber & 0xff));
        try
        {
            bytes.write(id);
        }
        catch(IOException ex)
        {
            RFIDException nex = new RFIDException(ex.getMessage());
            nex.setStackTrace(ex.getStackTrace());
            throw nex;
        }
        return new CommandsTexas((byte)4, (byte)101, bytes.toByteArray());
    }
    
    static CommandsTexas readSingleBlockTagitRequest(byte[] id, int blockNumber)        
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(5);
        bytes.write((byte)(blockNumber & 0xff));
        try {
            bytes.write(id);
        } catch (IOException ex) { }
        return new CommandsTexas((byte)5, (byte)97, bytes.toByteArray());
    }    
    
    static CommandsTexas readMultipleBlocksISO15693Request(byte[] id, int startBlockNumber, int numberOfBlocks)        
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(12);
        bytes.write(0);
        bytes.write(0);
        bytes.write((byte)(startBlockNumber & 0xff));
        bytes.write((byte)(numberOfBlocks & 0xff));
        try
        {
            bytes.write(id);
        } catch(IOException ex) { }
        return new CommandsTexas((byte)4, (byte)104, bytes.toByteArray());
    }

    static CommandsTexas stayQuietISO15693Request(byte [] uid)
    {
        return new CommandsTexas((byte)4, (byte)100, uid);
    }

    static CommandsTexas versionRequest()
    {
        return new CommandsTexas((byte)1, (byte)64, null);
    }
    
    static CommandsTexas writeSingleBlockISO15693Request(byte[] id, int blockNumber, byte blockData[])
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(16);
        bytes.write(0);
        bytes.write(1);
        bytes.write((byte)(blockNumber & 0xff));
        bytes.write(4);
        try {
            bytes.write(encodeBytes(blockData, 4));
            bytes.write(id);
        } catch (IOException ex) { }
        return new CommandsTexas((byte)4, (byte)102, bytes.toByteArray());
    }

    static CommandsTexas writeSingleBlockTagitRequest(byte[] id, int blockNumber, byte blockData[])
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(10);
        bytes.write((byte)(blockNumber & 0xff));
        bytes.write((byte)31);
        try {
            bytes.write(encodeBytes(blockData, 4));
            bytes.write(id);
        } catch (IOException ex) { }
        return new CommandsTexas((byte)5, (byte)99, bytes.toByteArray());
    }    

    private int getFirstCommand()
    {
        return command1;
    }

    private int getSecondCommand()
    {
        return command2;
    }

    private byte[] getData()
    {
        return data;
    }

    private byte getDeviceId()
    {
        return deviceId;
    }

    private int getLength()
    {
        return length;
    }

    private int getLRC1()
    {
        return lrc1;
    }
        
    private int getLRC2()
    {
        return lrc2;
    }
            
    private void setFirstCommand(byte command)
    {
        command1 = command;
    }

    private void setSecondCommand(byte command)
    {
        command2 = command;
    }

    private void setData(byte data[])
    {
        this.data = data;
    }

    private void setLength(int length)
    {
        this.length = length;
    }

    private void setLRC1(int lrc1)
    {
        this.lrc1 = lrc1;
    }
    
    private void setLRC2(int lrc2)
    {
        this.lrc2 = lrc2;
    }
    
    private void calcCRC(byte bytes[], int length)
    {
        int lrc1 = 0;
        int lrc2 = 0;
        for(int i = 0; i < length; i++)
            lrc1 ^= bytes[i];

        lrc2 = lrc1 ^ 0xff;
        setLRC1(lrc1);
        setLRC2(lrc2);        
    }
    
    public byte[] getBytes()
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(getLength());
        bytes.write(1);
        bytes.write((byte)(getLength() & 0xff));
        bytes.write((byte)(getLength() / 256 & 0xff));
        bytes.write(getDeviceId());
        bytes.write(getFirstCommand());
        bytes.write(getSecondCommand());
        int dl = getData() == null ? 0 : getData().length;
        for(int counter = 0; counter < dl; counter++)
            bytes.write(getData()[counter]);

        bytes.write((byte)getLRC1());
        bytes.write((byte)getLRC2());
        return bytes.toByteArray();
    }

    private byte[] getBytesWithoutCRC()
    {
        byte bytes[] = getBytes();
        ByteArrayOutputStream result = new ByteArrayOutputStream(bytes.length - 2);
        result.write(bytes, 0, bytes.length - 2);
        return result.toByteArray();
    }
    
    private static byte[] encodeBytes(byte bytes[], int blockSize, int index, int length)
    {
        if(bytes == null)
            return null;
        int numberOfBlocks = length / blockSize;
        if(length % blockSize > 0)
            numberOfBlocks++;
        byte result[] = new byte[numberOfBlocks * blockSize];
        for(int blockIndex = 0; blockIndex < numberOfBlocks; blockIndex++)
        {
            for(int byteIndex = 0; byteIndex < blockSize; byteIndex++)
            {
                int srcIndex = blockIndex * blockSize + byteIndex + index;
                int dstIndex = blockIndex * blockSize + (blockSize - byteIndex - 1);
                if(srcIndex < bytes.length)
                    result[dstIndex] = bytes[srcIndex];
                else
                    result[dstIndex] = 0;
            }

        }

        return result;
    }

    private static byte[] encodeBytes(byte bytes[], int blockSize)
    {
        return encodeBytes(bytes, blockSize, 0, bytes.length);
    }
    
}
