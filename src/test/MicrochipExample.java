package test;
import es.deusto.smartlab.rfid.*;

/**
 *@author Xabier Echevarría Espinosa
 */
public class MicrochipExample {
    
    public MicrochipExample() {

    }
    
    public static void main(String args[])
    {
        
        MicrochipExample demo = new MicrochipExample();
        InterfaceRFID iRFID = null;
        try {
            iRFID = LoaderRFID.load("es.deusto.smartlab.rfid.microchip.ImplementationMicrochip");
        } catch (InvalidDriverException ex) {
            ex.printStackTrace();
        }
        System.out.println("iRFID " + iRFID);

        if(iRFID!=null)
        {            
            Tag[] read = null;
            try {
                read = iRFID.readAllBlocksMemory();
            } catch (RFIDException ex) {
                ex.printStackTrace();
            }
            if(read!=null)
            {
                System.out.println("Found " + read.length  + " tag(s) in range");                
                System.out.println("Tag Type\tTag ID\t\tNo.Blocks");
                for(int counter=0; counter<read.length; counter++)
                {
                    System.out.println(read[counter].getTagType() + "\t\t" + read[counter].getTagIDAsString() + "\t" + read[counter].getTagData().length);
                }
                
            }            
            LoaderRFID.unload(iRFID);
        }

    }

}
