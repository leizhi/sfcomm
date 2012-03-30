package es.deusto.smartlab.rfid.microchip;

import es.deusto.smartlab.rfid.*;
import java.io.*;
import java.util.*;

/**
 * @author Xabier Echevarría Espinosa
 */
public class ImplementationMicrochip implements InterfaceRFID
{    
    private String port;    
    private SerialManager sm;

    /**
     *Constructor.
     */
    public ImplementationMicrochip()
    {       
        sm = new SerialManager();
    }

    /**
    *Initializes the Microchip's kit, testing the connection with the reader/interrogator and opening the serial port.
    *@return Returns success if a RFID kit was successfully initialized.
    */
    public boolean init()
    {
        boolean opened = false;
        if(port!=null) {
            if(sm.openPort(port,19200)) 
            {
                if(testConnection())                
                    opened = true;
                else
                    sm.closePort();
            }
        } else {
            ArrayList portsAvailable = new ArrayList();
            portsAvailable = sm.getPorts();
            for(int i=0; i<portsAvailable.size(); i++)
            {            
                if(sm.openPort(portsAvailable.get(i).toString(),19200)) 
                {
                    if(testConnection())
                    {
                        opened = true;
                        break;
                    } 
                    else 
                        sm.closePort();
                }
            }               
        }
        return opened;
    }
        
    public void setPort(String port)
    {
        this.port = port;
    }    

    public Tag[] findTokens()
    {
        Tag[] tags = null;
        try {
            tags = readTokens(null,0,0);
            if(tags!=null)
            {
                for(int i=0; i<tags.length; i++)
                {
                    tags[i].setTagData(null);
                }
            }                
        } catch (RFIDException ex) { }
        return tags;
    }    
    
    public Tag readSingleBlockMemory(byte[] tagID, int blockNumber)
        throws RFIDException
    {                
        boolean present = false;
        Tag[] id = new Tag[1];
        Tag[] ids = findTokens();
        
        for(int i=0; i<ids.length; i++)
        {                    
            if(Arrays.equals(tagID,ids[i].getTagID()))
            {                        
                present = true;
                id[0] = ids[i];
            }                        
        }
        
        if(!present)
        {
            throw new RFIDException("The tag is not present");
        } else {
            Tag[] tags = readTokens(tagID,blockNumber,blockNumber);
            return tags[0];
        }
    }
    
    public Tag readAllBlocksMemory(byte[] tagID)
        throws RFIDException
    {
        boolean present = false;
        Tag[] id = new Tag[1];
        Tag[] ids = findTokens();
        
        for(int i=0; i<ids.length; i++)
        {                    
            if(Arrays.equals(tagID,ids[i].getTagID()))
            {                        
                present = true;
                id[0] = ids[i];
            }                        
        }
        
        if(!present)
        {
            throw new RFIDException("The tag is not present");
        } else {
            Tag[] tags = readTokens(tagID,0,25);
            return tags[0];
        }
    }
    
    public Tag[] readAllBlocksMemory()
        throws RFIDException
    {
        return readTokens(null,0,25);
    }
    
    public Tag readMultipleBlocksMemory(byte[] tagID, int startBlockNumber, int endBlockNumber)
        throws RFIDException
    {
        if(startBlockNumber > endBlockNumber)
            throw new RFIDException("The end block number must be greater than the start block number");
        if(startBlockNumber == endBlockNumber)
            throw new RFIDException("The start and end block number must be differents");
                
        boolean present = false;
        Tag[] id = new Tag[1];
        Tag[] ids = findTokens();
        
        for(int i=0; i<ids.length; i++)
        {                    
            if(Arrays.equals(tagID,ids[i].getTagID()))
            {                        
                present = true;
                id[0] = ids[i];
            }                        
        }
        
        if(!present)
        {
            throw new RFIDException("The tag is not present");
        } else {
            Tag[] tags = readTokens(tagID,startBlockNumber,endBlockNumber);
            return tags[0];
        }
    }
    
    public void writeTokens(byte [] id, int startBlockNumber, byte [] dataTemp)
        throws RFIDException
    {
        byte[] data = null;

        if(id == null)
            throw new RFIDException("The Tag ID is null");
        
        startBlockNumber +=6;
        if(startBlockNumber < 6)
        {
            startBlockNumber -= 6;
            throw new RFIDException("The start block number must be positive (" + startBlockNumber + ")");
        }
        
        if(dataTemp == null)
            throw new RFIDException("The data is null");
        else if(dataTemp.length%4!=0)
        {     
            byte [] temp1 = new byte[((dataTemp.length/4)+1)*4];
            System.arraycopy(dataTemp,0,temp1,0,dataTemp.length);
            data = new byte[temp1.length*2];            
            for(int i=0; i<data.length/8; i++)
            {
                byte[] temp2 = new byte[4];
                System.arraycopy(temp1,i*4,temp2,0,4);
                byte[] temp3 = convertDataBlock(temp2);
                System.arraycopy(temp3,0,data,i*8,8);
            }
        } else {
            data = convertDataBlock(dataTemp);
        }

        if(startBlockNumber+(data.length/8) > 31)
        {
            int endBlockNumber = (startBlockNumber+(data.length/8)) - 6;
            throw new RFIDException("The end block number must be less or equal to 25, but it is " + endBlockNumber);
        }
        
        id = convertDataBlock(id);
        byte [] command = CommandsMicrochip.writeMCRF450(id,startBlockNumber,data).getBytes();
        sm.send(command);
        byte [] response = sm.read();        
    }

    public void destroy()
    {
        byte [] command = CommandsMicrochip.resetMessage().getBytes();
        sm.send(CommandsMicrochip.resetMessage().getBytes());       
        sm.closePort();
    }    
    
    private Tag[] readTokens(byte[] tagID, int startBlockNumber, int endBlockNumber)
        throws RFIDException
    {
        Tag[] tags = null;
        byte [] response = null;
        startBlockNumber +=6;
        endBlockNumber +=6;
        int size = (((endBlockNumber - startBlockNumber + 1)*12)+16)-1;
        
        if(tagID!=null)
        {
            tagID = convertDataBlock(tagID);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(tagID.length+1);
            bytes.write((byte)73);
            try {
                bytes.write(tagID);            
            } catch (IOException ex) { }
            sm.send(CommandsMicrochip.readMCRF450(bytes.toByteArray(), (byte)startBlockNumber, (byte)endBlockNumber).getBytes());
            response = sm.read();            
        } else {
            sm.send(CommandsMicrochip.readMCRF450(new byte[] { 42 }, (byte)startBlockNumber, (byte)endBlockNumber).getBytes());
            response = sm.read();    
        }
                        
        if(response.length>8)
        {
            tags = new Tag[(response.length-8)/size];
            for(int i=0; i<(response.length-8)/size; i++)
            {
                Tag tag = new Tag("[Microchip] 13.56 MHz Anti-Collision microID Developer's Kit for MCRF355 & MCRF45x", "MCRF450");
                byte[] id = new byte[8];
                System.arraycopy(response,11+(i*size),id,0,8);
                tag.setTagID(convertDataBlock(id));
                
                int blockNumber = 0;
                byte[] temp = new byte[size-16];
                byte[][] blocks = new byte[(endBlockNumber - startBlockNumber + 1)][4];
                System.arraycopy(response,20+(i*size),temp,0,size-16);

                for(int k=20+(i*size)+3; k<((i+1)*size)+8; blockNumber++)
                {
                    byte [] dataBlock = new byte[8];
                    System.arraycopy(response,k,dataBlock,0,8);
                    blocks[blockNumber] = convertDataBlock(dataBlock);
                    k = k+12;
                }

                tag.setTagData(blocks);
                tags[i] = tag;
            }
        }
        return tags;
    }

    private byte[] convertDataBlock(byte[] dataBlockTemp)
    {
        if(dataBlockTemp.length==8)
        {            
            byte [] dataBlock = new byte[4];

            for(int i=0; i<dataBlockTemp.length/2; i++)
            {                        
                String highHexNumber = new String(dataBlockTemp,i*2,1);
                String lowHexNumber = new String(dataBlockTemp,i*2 + 1,1);

                dataBlock[i] = Byte.valueOf(highHexNumber,16);
                dataBlock[i] <<= 4;
                dataBlock[i] += Byte.valueOf(lowHexNumber,16);                        
            }        
            return dataBlock;
        } else {
            byte [] dataBlock = new byte[8];

            for(int i=0; i<dataBlockTemp.length; i++)
            {                        
                String byteStr = Integer.toHexString(dataBlockTemp[i] & 0xff).toUpperCase();
                if(byteStr.length() < 2)
                    byteStr = "0" + byteStr;
                dataBlock[i*2] = (byte)byteStr.charAt(0);
                dataBlock[(i*2)+1] = (byte)byteStr.charAt(1);
            }        
            return dataBlock;            
        }
    }
    
    private boolean testConnection()
    {
        byte [] sync = {0x42};
        byte [] response = null;
        
        sm.send(sync);
        response = sm.read();
    
        sm.send(CommandsMicrochip.nopMessage().getBytes());
        response = sm.read();

        byte [] compar = {0x40, 0x52, 0x30, 0x30, 0x30, 0x45, 0x0D, 0x0A};
        if(response.length==0) {
            return false;
        } else if(Arrays.equals(response,compar)) {
            return true;    
        } else {
            return false;                
        }   
    }
    
}
