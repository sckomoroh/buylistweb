package net.ghyrosoft.test;

import net.ghyrosoft.BuyListService;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: sckomoroh
 * Date: 2/4/13
 * Time: 9:53 AM
 * To change this template use File | Settings | File Templates.
 */
public class BuyListServiceTest
{
    private BuyListService service = new BuyListService();

    @Test
    public void testLogin() throws Exception
    {
        String result = service.login("user", "password");
        result = service.login("user", "password2");
    }

    @Test
    public void testGetAllLists() throws Exception
    {
        String lists = service.getAllLists("user", "46fee63c1e70d3c783d0d3f1f3635ce2");
    }

    @Test
    public void testGetListByName() throws Exception
    {
        String listContent = service.getListByName("Лист", "user", "46fee63c1e70d3c783d0d3f1f3635ce2");
    }

    @Test
    public void testCheckLogin() throws Exception
    {
        String checkLogin = service.checkLogin("user");
        checkLogin = service.checkLogin("user2");
    }

    @Test
    public void testRegister() throws Exception
    {

    }
}
