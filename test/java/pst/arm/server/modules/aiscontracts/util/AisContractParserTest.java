/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pst.arm.server.modules.aiscontracts.util;

import java.io.InputStream;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pst.arm.client.modules.aiscontracts.domain.AisContract;

/**
 *
 * @author Alexandr Kozhin <alexandr.kozhin@gmail.com>
 */
public class AisContractParserTest {

    public AisContractParserTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getContractsFromXml method, of class AisContractParser.
     */
    @Test
    public void testGetContractsFromXml() {
        System.out.println("getContractsFromXml");
        InputStream is = null;
        List<AisContract> expResult = null;
        List<AisContract> result = AisContractParser.getContractsFromXml(getClass().getResourceAsStream("dogovor.xml"));
        Assert.assertNotNull(result);

    }

}
