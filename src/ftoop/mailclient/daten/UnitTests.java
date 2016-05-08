package ftoop.mailclient.daten;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import junit.framework.TestCase;

public class UnitTests extends TestCase{

	@Test
	public void testEmailKontoControl() throws IOException {
		EmailKontoControl testControl = new EmailKontoControl();
		testControl.newKonto();
		
		if (System.getProperty("os.name").toLowerCase().indexOf("win")<0) {
            System.err.println("Sorry, Windows only!");
            System.exit(1);
        }
        File desktopDir = new File(System.getProperty("user.home"), "Desktop");
        System.out.println(desktopDir.getPath() + " " + desktopDir.exists());
   
		testControl.saveKonten(desktopDir.getPath() + "/kontos.xml");
		assertTrue(true);
	}

}
