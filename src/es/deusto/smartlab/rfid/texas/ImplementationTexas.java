package es.deusto.smartlab.rfid.texas;

import java.io.*;
import java.util.*;
import es.deusto.smartlab.rfid.*;

/**
 * @author Xabier Echevarría Espinosa
 */
public class ImplementationTexas implements InterfaceRFID
{    
    private String port;
    private SerialManager sm;
    private static Hashtable errorCodes;
    static
    {
        errorCodes = new Hashtable();
        errorCodes.put(new Integer(0), "ERROR_NONE");
        errorCodes.put(new Integer(1), "ERROR_TOKEN_NOT_PRESENT");
        errorCodes.put(new Integer(5), "ERROR_INVALID_RF_FORMAT");
        errorCodes.put(new Integer(14), "ERROR_DEVICE_ID_INVALID");
        errorCodes.put(new Integer(16), "ERROR_ILLEGAL_ACTION");
        errorCodes.put(new Integer(17), "ERROR_WRONG_DOWNLOAD_STATE");
        errorCodes.put(new Integer(18), "ERROR_WRITE_FAILED");
        errorCodes.put(new Integer(19), "ERROR_INVALID_ADDRESS");
        errorCodes.put(new Integer(20), "ERROR_INVALID_BAUD");
        errorCodes.put(new Integer(21), "ERROR_INVALID_CHECK_DIGITS");
        errorCodes.put(new Integer(22), "ERROR_NO_TIMER_AVAILABLE");
        errorCodes.put(new Integer(23), "ERROR_INVALID_ENTITY_ID");
        errorCodes.put(new Integer(24), "ERROR_DATA_TRUNCATED");
        errorCodes.put(new Integer(25), "ERROR_NO_DATA_READ");
        errorCodes.put(new Integer(26), "ERROR_INVALID_START_BYTE");
        errorCodes.put(new Integer(27), "ERROR_INVALID_CRC");
        errorCodes.put(new Integer(28), "ERROR_CMD_REPLY_MISMATCH");
        errorCodes.put(new Integer(32), "ERROR_14443_A_DATA_INCORRECT");
        errorCodes.put(new Integer(33), "ERROR_14443_B_DATA_INCORRECT");
        errorCodes.put(new Integer(34), "ERROR_14443_B_TOKEN_NOT_FOUND");
        errorCodes.put(new Integer(35), "ERROR_HF_ASIC_RECEIVE_TIMEOUT");
        errorCodes.put(new Integer(36), "ERROR_HF_ASIC_ABORTED");
        errorCodes.put(new Integer(37), "ERROR_HF_ASIC_ATQB_ERR1");
        errorCodes.put(new Integer(38), "ERROR_HF_ASIC_ATQB_PROT_TYPE");
        errorCodes.put(new Integer(39), "ERROR_HF_ASIC_INVALID_CID");
        errorCodes.put(new Integer(40), "ERROR_HF_ASIC_INVALID_NAD");
        errorCodes.put(new Integer(41), "ERROR_HF_ASIC_CID_LOW_POWER");
        errorCodes.put(new Integer(42), "ERROR_14443_B_HLTB_ERROR");
        errorCodes.put(new Integer(43), "ERROR_14443_B_INVALID_BLK_TYPE");
        errorCodes.put(new Integer(44), "ERROR_HF_ASIC_NOT_IBLOCK");
        errorCodes.put(new Integer(45), "ERROR_HF_ASIC_NOT_RBLOCK");
        errorCodes.put(new Integer(46), "ERROR_14443_B_SDESELECT");
        errorCodes.put(new Integer(47), "ERROR_14443_DATA_INCORRECT");
        errorCodes.put(new Integer(48), "ERROR_MANY_CID_NO_SUPRT_TRANSPONDERS");
        errorCodes.put(new Integer(49), "ERROR_COLISN_BPSK_AND_OR_CID");
        errorCodes.put(new Integer(50), "ERROR_COLISN_BPSK_DECODE");
        errorCodes.put(new Integer(51), "ERROR_14443_ABORTED");
        errorCodes.put(new Integer(52), "ERROR_TOKEN_BUFFER_NULL");
        errorCodes.put(new Integer(53), "ERROR_14443_A_UPLINK_PARITY");
        errorCodes.put(new Integer(54), "ERROR_14443_A_ATS");
        errorCodes.put(new Integer(55), "ERROR_14443_A_PPS");
        errorCodes.put(new Integer(56), "ERROR_14443_A_CASCADE_LEVEL");
        errorCodes.put(new Integer(57), "ERROR_14443_A_SAK_CRC");
        errorCodes.put(new Integer(64), "ERROR_BPSK_NO_ERROR");
        errorCodes.put(new Integer(65), "ERROR_BPSK_BAD_FRAME_WAIT");
        errorCodes.put(new Integer(66), "ERROR_BPSK_BAD_VAL_BAUD");
        errorCodes.put(new Integer(67), "ERROR_BPSK_CANCELLED");
        errorCodes.put(new Integer(68), "ERROR_BPSK_SUBCARRIER");
        errorCodes.put(new Integer(69), "ERROR_BPSK_TR0_TIMEOUT");
        errorCodes.put(new Integer(70), "ERROR_BPSK_RCV_OVERFLOW");
        errorCodes.put(new Integer(71), "ERROR_BPSK_NO_SOF");
        errorCodes.put(new Integer(72), "ERROR_BPSK_NO_EOF");
        errorCodes.put(new Integer(73), "ERROR_BPSK_TR1_TIMEOUT");
        errorCodes.put(new Integer(74), "ERROR_BPSK_CRC_ERROR");
        errorCodes.put(new Integer(75), "ERROR_BPSK_FRAME");
        errorCodes.put(new Integer(76), "ERROR_MODULE_ABORTED");
        errorCodes.put(new Integer(77), "ERROR_PARAMETER_ERROR");
        errorCodes.put(new Integer(87), "ERROR_COLLISION_DETECTED");
        errorCodes.put(new Integer(96), "ERROR_APOLLO_LIFE_CYCLE");
        errorCodes.put(new Integer(97), "ERROR_APOLLO_DATA_INCORRECT");
    }

    /**
     *Constructor.
     */
    public ImplementationTexas()
    {       
        sm = new SerialManager();
    }

    /**
    *Initializes the Texas Instruments's kit, testing the connection with the reader/interrogator and opening the serial port.
    *@return Returns success if a RFID kit was successfully initialized.
    */
    public boolean init()        
    {
        boolean opened = false;
        if(port!=null) {
            if(sm.openPort(port,9600)) 
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
                if(sm.openPort(portsAvailable.get(i).toString(),9600)) 
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
        Tag[] Tagit = null;
        Tag[] ISO15693 = null;
        byte[] response = null;

        sm.send(CommandsTexas.findTokenTagitRequest().getBytes());
        response = sm.read();
        if(response.length>0)
            if(response[6]==0)
                Tagit = getTokens(response);        
        
        sm.send(CommandsTexas.findTokenISO15693Request().getBytes());
        response = sm.read();
        if(response.length>0)
            if(response[6]==0)
                ISO15693 = getTokens(response);
        
        return allTags(Tagit, ISO15693);        
    }

    public Tag readSingleBlockMemory(byte[] tagID, int blockNumber)
        throws RFIDException
    {
        if(blockNumber < 0)
            throw new RFIDException("The block number must be positive (" + blockNumber + ")");        
        
        Tag[] Tagit = null;
        Tag[] ISO15693 = null;
        byte[] response = null;
        boolean present = false;
        
        sm.send(CommandsTexas.findTokenTagitRequest().getBytes());
        response = sm.read();
        if(response.length>0)
        {            
            if(response[6]==0)
            {
                if(blockNumber > 7)
                    throw new RFIDException("The block number must be less or equal to 7, but it is " + blockNumber);
                Tag[] id = new Tag[1];
                byte entityID = response[7];
                Tag[] ids = getTokens(response);
                
                for(int i=0; i<ids.length; i++)
                {                    
                    if(Arrays.equals(tagID,ids[i].getTagID()))
                    {                        
                        present = true;
                        id[0] = ids[i];
                    }                        
                }
                
                if(present)
                    Tagit = readTokens(id, blockNumber, blockNumber, entityID);
            }                
        }

        if(!present)
        {
            sm.send(CommandsTexas.findTokenISO15693Request().getBytes());
            response = sm.read();
            if(response.length>0)
            {            
                if(response[6]==0)
                {
                    if(blockNumber > 63)
                        throw new RFIDException("The block number must be less or equal to 63, but it is " + blockNumber);
                    Tag[] id = new Tag[1];
                    byte entityID = response[7];
                    Tag[] ids = getTokens(response);

                    for(int i=0; i<ids.length; i++)
                    {                    
                        if(Arrays.equals(tagID,ids[i].getTagID()))
                        {                        
                            present = true;                        
                            id[0] = ids[i];
                        }                        
                    }

                    if(present)                                               
                        ISO15693 = readTokens(id, blockNumber, blockNumber, entityID);
                }
            }
        }
        
        if(!present)
        {
            throw new RFIDException("The tag is not present");
        } else {
            Tag[] tags = allTags(Tagit, ISO15693);
            return tags[0];
        }        
    }            
    
    public Tag readMultipleBlocksMemory(byte[] tagID, int startBlockNumber, int endBlockNumber) 
        throws RFIDException
    {
        if(startBlockNumber > endBlockNumber)
            throw new RFIDException("The end block number must be greater than the start block number");
        if(startBlockNumber == endBlockNumber)
            throw new RFIDException("The start and end block number must be differents");
        
        Tag[] Tagit = null;
        Tag[] ISO15693 = null;
        byte[] response = null;
        boolean present = false;
        
        sm.send(CommandsTexas.findTokenTagitRequest().getBytes());
        response = sm.read();
        if(response.length>0)
        {            
            if(response[6]==0)
            {
                Tag[] id = new Tag[1];
                byte entityID = response[7];
                Tag[] ids = getTokens(response);
                
                for(int i=0; i<ids.length; i++)
                {                    
                    if(Arrays.equals(tagID,ids[i].getTagID()))
                    {                        
                        present = true;
                        id[0] = ids[i];
                    }                        
                }
                
                if(present)
                    Tagit = readTokens(id, startBlockNumber, endBlockNumber, entityID);
            }                
        }

        if(!present)
        {
            sm.send(CommandsTexas.findTokenISO15693Request().getBytes());
            response = sm.read();
            if(response.length>0)
            {            
                if(response[6]==0)
                {
                    Tag[] id = new Tag[1];
                    byte entityID = response[7];
                    Tag[] ids = getTokens(response);

                    for(int i=0; i<ids.length; i++)
                    {                    
                        if(Arrays.equals(tagID,ids[i].getTagID()))
                        {                        
                            present = true;                        
                            id[0] = ids[i];
                        }                        
                    }

                    if(present)                                               
                        ISO15693 = readTokens(id, startBlockNumber, endBlockNumber, entityID);
                }
            }
        }
        
        if(!present)
        {
            throw new RFIDException("The tag is not present");
        } else {
            Tag[] tags = allTags(Tagit, ISO15693);
            return tags[0];
        }
    }
    
    public Tag[] readAllBlocksMemory()
        throws RFIDException
    {
        Tag[] Tagit = null;
        Tag[] ISO15693 = null;
        byte[] response = null;
        
        sm.send(CommandsTexas.findTokenTagitRequest().getBytes());
        response = sm.read();
        if(response.length>0)
        {            
            if(response[6]==0)
            {
                byte entityID = response[7];
                Tag[] ids = getTokens(response);
                Tagit = readTokens(ids, 0, 7, entityID);
            }                
        }

        sm.send(CommandsTexas.findTokenISO15693Request().getBytes());
        response = sm.read();
        if(response.length>0)
        {            
            if(response[6]==0)
            {
                byte entityID = response[7];
                Tag[] ids = getTokens(response);
                ISO15693 = readTokens(ids, 0, 63, entityID);
            }
        }
        
        return allTags(Tagit, ISO15693);
    }    
    
    public Tag readAllBlocksMemory(byte[] tagID)
        throws RFIDException
    {
        Tag[] Tagit = null;
        Tag[] ISO15693 = null;
        byte[] response = null;
        boolean present = false;
        
        sm.send(CommandsTexas.findTokenTagitRequest().getBytes());
        response = sm.read();
        if(response.length>0)
        {            
            if(response[6]==0)
            {
                Tag[] id = new Tag[1];        
                byte entityID = response[7];
                Tag[] ids = getTokens(response);
                
                for(int i=0; i<ids.length; i++)
                {                    
                    if(Arrays.equals(tagID,ids[i].getTagID()))
                    {                        
                        present = true;
                        id[0] = ids[i];
                    }                        
                }
                
                if(present)
                    Tagit = readTokens(id, 0, 7, entityID);
            }                
        }

        if(!present)
        {
            sm.send(CommandsTexas.findTokenISO15693Request().getBytes());
            response = sm.read();
            if(response.length>0)
            {            
                if(response[6]==0)
                {
                    Tag[] id = new Tag[1];
                    byte entityID = response[7];
                    Tag[] ids = getTokens(response);

                    for(int i=0; i<ids.length; i++)
                    {                    
                        if(Arrays.equals(tagID,ids[i].getTagID()))
                        {                        
                            present = true;                        
                            id[0] = ids[i];
                        }                        
                    }

                    if(present)                                               
                        ISO15693 = readTokens(id, 0, 63, entityID);
                }
            }
        }
        
        if(!present)
        {
            throw new RFIDException("The tag is not present");
        } else {
            Tag[] tags = allTags(Tagit, ISO15693);
            return tags[0];
        }

    }

    public void writeTokens(byte [] tagID, int startBlockNumber, byte [] dataTemp)
        throws RFIDException
    {
        byte [] data = null;
        
        if(tagID == null)
            throw new RFIDException("The tag ID is null");

        if(startBlockNumber < 0)
        {
            throw new RFIDException("The start block number must be positive (" + startBlockNumber + ")");
        }
        
        if(dataTemp == null)
            throw new RFIDException("The data is null");
        else if(dataTemp.length%4!=0)
        {
            data = new byte[((dataTemp.length/4)+1)*4];
            System.arraycopy(dataTemp,0,data,0,dataTemp.length);
        } else {
            data = dataTemp;
        }

        int endBlockNumber = ((data.length/4)-1)+startBlockNumber;
        
        byte[] response = null;
        boolean present = false;

        sm.send(CommandsTexas.findTokenTagitRequest().getBytes());
        response = sm.read();
        if(response.length>0)
        {
            if(response[6]==0)
            {
                if(endBlockNumber > 7)
                {            
                    throw new RFIDException("The end block number must be less or equal to 7, but it is " + endBlockNumber);
                }
                Tag[] id = new Tag[1];
                Tag[] ids = getTokens(response);
                
                for(int i=0; i<ids.length; i++)
                {                    
                    if(Arrays.equals(tagID,ids[i].getTagID()))
                    {
                        present = true;
                        id[0] = ids[i];
                    }
                }
                    if(present)
                    {
                        int counter = 0;
                        for(int i=startBlockNumber;i<=endBlockNumber; i++)
                        {
                            byte[] bytes = new byte[4];
                            for(int j=3; j>=0; j--)
                            {
                                bytes[j] = data[counter++];
                            }
                            byte[] command = CommandsTexas.writeSingleBlockTagitRequest(id[0].getTagID(),i,bytes).getBytes();
                            sm.send(CommandsTexas.writeSingleBlockTagitRequest(id[0].getTagID(),i,bytes).getBytes());
                            response = sm.read();
                            System.out.println();
                        }
                }
            }
        }

        if(!present)
        {
            sm.send(CommandsTexas.findTokenISO15693Request().getBytes());
            response = sm.read();
            if(response.length>0)
            {
                if(response[6]==0)
                {
                    if(endBlockNumber > 63)
                    {            
                        throw new RFIDException("The end block number must be less or equal to 63, but it is " + endBlockNumber);
                    }
                    
                    Tag[] id = new Tag[1];
                    Tag[] ids = getTokens(response);

                    for(int i=0; i<ids.length; i++)
                    {
                        if(Arrays.equals(tagID,ids[i].getTagID()))
                        {
                            present = true;
                            id[0] = ids[i];
                        }                        
                    }

                    if(present)
                    {
                        int counter = 0;
                        for(int i=startBlockNumber;i<=endBlockNumber; i++)
                        {
                            byte[] bytes = new byte[4];
                            for(int j=3; j>=0; j--)
                            {
                                bytes[j] = data[counter++];
                            }
                            byte[] command = CommandsTexas.writeSingleBlockISO15693Request(id[0].getTagID(),i,bytes).getBytes();
                            sm.send(CommandsTexas.writeSingleBlockISO15693Request(id[0].getTagID(),i,bytes).getBytes());
                            response = sm.read();
                        }
                    }
                }
            }
        }
        
        if(!present)
        {
            throw new RFIDException("The tag is not present");
        }
    }

    public void destroy()
    {
        sm.closePort();
    }
    
    private Tag[] allTags(Tag[] Tagit, Tag[] ISO15693)
    {
        int size = 0;
        Tag [] tags = null;

        if(Tagit!=null)            
            size = size + Tagit.length;        
        
        if(ISO15693!=null)
            size = size + ISO15693.length;
    
        if(size !=0)
        {
            int tagNumber = 0;
            tags = new Tag[size];
            
            if(Tagit!=null)
                for(int i=0; i<Tagit.length; i++,tagNumber++)
                    tags[tagNumber] = Tagit[i];
            
            if(ISO15693!=null)
                for(int i=0; i<ISO15693.length; i++,tagNumber++)
                    tags[tagNumber] = ISO15693[i];
        }
        
        return tags;
    }

    private Tag[] getTokens(byte[] response)
    {        
        Tag[] tags = null;
        byte entityID = response[7];
        switch(entityID)
        {    
            case 4: // Entity 15693 Module
            {                
                int tagNumber = 0;
                tags = new Tag[(response.length-12)/8];
                for(int i=8; i<response.length-2; tagNumber++)
                {
                    byte inventoryFlag = response[i++];
                    byte dataStorageFormatID = response[i++];
                    byte[] uid = new byte[8];
                    for(int j=0; j<8; j++)
                        uid[j] = response[i++];
                    Tag tag = new Tag("[Texas Instruments] S4100 Multi-Function Reader Evaluation Kit", "ISO 15693");
                    tag.setTagID(uid);
                    tags[tagNumber] = tag;
                }
                break;
            }
            case 5: // Entity Tag-it Module
            {                
                int tagNumber = 0;
                tags = new Tag[(response.length-10)/4];
                for(int i=8; i<response.length-2; tagNumber++)
                {
                    byte[] sid = new byte[4];
                    for(int j=0; j<4; j++)
                        sid[j] = response[i++];
                    Tag tag = new Tag("[Texas Instruments] S4100 Multi-Function Reader Evaluation Kit", "Tag-it");
                    tag.setTagID(sid);
                    tags[tagNumber] = tag;
                }
                break;
            }                   
        }
        return tags;
    }

    private byte[] readSingleBlockOfTagitTransponder(byte [] id, int blockNumber)
        throws RFIDException
    {
        if(id == null)
            throw new RFIDException("The tag ID is null");
        if(blockNumber < 0)
            throw new RFIDException("The block number must be positive (" + blockNumber + ")");
        if(blockNumber > 7)
            throw new RFIDException("The block number must be less or equal to 7, but it is " + blockNumber);

        sm.send(CommandsTexas.readSingleBlockTagitRequest(id, blockNumber).getBytes());
        byte [] response = sm.read();
                
        if(response.length != 21)
            throw new RFIDException("Malformed response received. The expected data size is 21 bytes, but it is " + response.length);
        
        byte result[] = new byte[4];
        for(int index = 0; index < 4; index++)
            result[index] = response[15 + index];

        return result;
    }
    
    private byte[] readSingleBlockOfISO15693Transponder(byte [] id, int blockNumber)
        throws RFIDException
    {
        if(id == null)
            throw new RFIDException("The tag ID is null");
        if(blockNumber < 0)
            throw new RFIDException("The block number must be positive (" + blockNumber + ")");
        if(blockNumber > 63)
            throw new RFIDException("The block number must be less or equal to 63, but it is " + blockNumber);

        sm.send(CommandsTexas.readSingleBlockISO15693Request(id, blockNumber).getBytes());
        byte [] response = sm.read();
                
        if(response.length != 15)
            throw new RFIDException("Malformed response received. The expected data size is 15 bytes, but it is " + response.length);
        
        byte result[] = new byte[4];
        for(int index = 0; index < 4; index++)
            result[index] = response[9 + index];

        return result;
    }    

    private byte[] readMultipleBlocksOfISO15693Transponder(byte [] id, int startBlockNumber, int numberOfBlocks)
        throws RFIDException
    {
        if(id == null)
            throw new RFIDException("The tag ID is null");
        if(startBlockNumber < 0)
            throw new RFIDException("The start block number must be positive (" + startBlockNumber + ")");
        if(startBlockNumber == 0 && numberOfBlocks <= 1)
        {
            numberOfBlocks = numberOfBlocks - 1;
            throw new RFIDException("The end block number must be greater then zero, but it is " + numberOfBlocks);
        }            
        if(startBlockNumber != 0 && numberOfBlocks <= 0)
        {
            throw new RFIDException("The end block number must be greater then zero, but it is " + numberOfBlocks);
        }            
        if(startBlockNumber == 0 && numberOfBlocks > 64)
        {
            numberOfBlocks = numberOfBlocks - 1;
            throw new RFIDException("The end block number must be less or equal to 63, but it is " + numberOfBlocks);
        }            
        if(startBlockNumber == 1 && numberOfBlocks > 63)
            throw new RFIDException("The end block number must be less or equal to 63, but it is " + numberOfBlocks);
        
        int reminder = 0;        
        numberOfBlocks = numberOfBlocks - 1;
        if(numberOfBlocks > 28)
        {
            reminder = numberOfBlocks - 28;
            numberOfBlocks = 28;
        }
        
        sm.send(CommandsTexas.readMultipleBlocksISO15693Request(id, startBlockNumber, numberOfBlocks).getBytes());
        byte [] response = sm.read();
        byte responseFlags = response[7];
        if((responseFlags & 1) > 0)
        {
            int errorCode = response[8] & 0xff;                        
            throw new RFIDException((String)errorCodes.get(new Integer(errorCode)));
        }
        
        int len = response.length - 8 - 2;
        ByteArrayOutputStream result = new ByteArrayOutputStream(len);
        result.write(response, 8, len);
        if(reminder > 0)
        {
            byte reminderBytes[] = readMultipleBlocksOfISO15693Transponder(id, startBlockNumber + numberOfBlocks, reminder);
            result.write(reminderBytes, 0, reminderBytes.length);
        }
        
        byte resultAsBytes[] = result.toByteArray();
        return resultAsBytes;
    }

    private Tag[] readTokens(Tag[] ids, int startBlockNumber, int endBlockNumber, byte entityID) 
        throws RFIDException
    {
        Tag[] tags = null;
        switch(entityID)
        {    
            case 4: // Entity 15693 Module
            {                
                byte [] blockMemory = null;                
                tags = new Tag[ids.length];
                for(int i=0; i<ids.length; i++)
                {
                    Tag tag = new Tag("[Texas Instruments] S4100 Multi-Function Reader Evaluation Kit", "ISO 15693");
                    tag.setTagID(ids[i].getTagID());
                    
                    if(startBlockNumber == endBlockNumber) {
                        byte[][] blocks = new byte[1][4];
                        blockMemory = readSingleBlockOfISO15693Transponder(ids[i].getTagID(), startBlockNumber);
                        blocks[0] = blockMemory;
                        tag.setTagData(blocks);
                        tags[i] = tag;
                    } else {
                        int numberOfBlocks = 0;
                        if(startBlockNumber == 0)
                            numberOfBlocks = endBlockNumber + 1;
                        else
                            numberOfBlocks = endBlockNumber - startBlockNumber + 1;

                        blockMemory = readMultipleBlocksOfISO15693Transponder(ids[i].getTagID(), startBlockNumber, numberOfBlocks);
                        int blockNumber = 0;
                        byte[][] blocks = new byte[blockMemory.length/4][4];
                        for(int j=0; j<blockMemory.length; blockNumber++)
                        {
                            byte [] dataBlock = new byte[4];
                            System.arraycopy(blockMemory,j,dataBlock,0,4);
                            blocks[blockNumber] = dataBlock;
                            j = j+4;
                        }
                        tag.setTagData(blocks);
                        tags[i] = tag;
                    }                        
                }
                break;
            }
            case 5: // Entity Tag-it Module
            {                
                byte [] blockMemory = null;
                tags = new Tag[ids.length];
                for(int i=0; i<ids.length; i++)
                {
                    Tag tag = new Tag("[Texas Instruments] S4100 Multi-Function Reader Evaluation Kit", "Tag-it");
                    tag.setTagID(ids[i].getTagID());
                    
                    if(startBlockNumber == endBlockNumber) {
                        byte[][] blocks = new byte[1][4];
                        blockMemory = readSingleBlockOfTagitTransponder(ids[i].getTagID(), startBlockNumber);
                        blocks[0] = blockMemory;
                        tag.setTagData(blocks);
                        tags[i] = tag;
                    } else {
                        if(startBlockNumber < 0)
                            throw new RFIDException("The start block number must be positive (" + startBlockNumber + ")");
                        if(endBlockNumber > 7)
                            throw new RFIDException("The end block number must be less or equal to 7, but it is " + endBlockNumber);
                        
                        int numberOfBlocks = 0;
                        if(startBlockNumber == 0)
                            numberOfBlocks = endBlockNumber + 1;
                        else
                            numberOfBlocks = endBlockNumber;

                        int blockNumber = 0;
                        byte[][] blocks = new byte[numberOfBlocks][4];
                        for(int k=startBlockNumber; k<=endBlockNumber; k++)
                        {
                            blockMemory = readSingleBlockOfTagitTransponder(ids[i].getTagID(), k);
                            blocks[blockNumber] = blockMemory;
                            blockNumber ++;    
                        }
                        tag.setTagData(blocks);
                        tags[i] = tag;
                    }
                }
                break;
            }                   
        } 

    return tags;
    }    

    private boolean testConnection()
    {
        sm.send(CommandsTexas.versionRequest().getBytes());
        byte [] response = sm.read();
        byte [] compar = {0x01,0x03,0x01,0x40,0x00};
        if(response.length==0) {
            return false;
        } else if(response[0]==compar[0] && response[3]==compar[1] && response[4]==compar[2] && response[5]==compar[3] && response[6]==compar[4]) {
            return true;    
        } else {
            return false;                
        }   
    }    
    
}
