package net.ghyrosoft.test;

import net.ghyrosoft.DataBaseHelper;
import org.junit.Assert;
import org.junit.Before;

import java.util.List;

/**
 * User: sckomoroh
 * Date: 2/4/13
 * Time: 9:52 AM
 */
public class DataBaseHelperTest
{
    DataBaseHelper helper;

    @Before
    public void setUp() throws Exception
    {
        helper = new DataBaseHelper();
    }

    @org.junit.Test
    public void testCheckLogin_valid() throws Exception
    {
        Assert.assertTrue(helper.checkLogin("abrakadabra"));
    }

    @org.junit.Test
    public void testCheckLogin_invalid() throws Exception
    {
        Assert.assertFalse(helper.checkLogin("user"));
    }

    @org.junit.Test
    public void testRegister_valid() throws Exception
    {
        Assert.assertFalse(helper.register("user2", "asdqwead"));

        String session = helper.getSession("user2", "asdqwead");
        Assert.assertNotNull(session);

        int userId = helper.getUserIdByLogin("user2", session);
        Assert.assertTrue(userId != -1);

        helper.unregister("user2", session);
    }

    @org.junit.Test
    public void testGetSession() throws Exception
    {
        Assert.assertNotNull(helper.getSession("user", "password"));
        Assert.assertNull(helper.getSession("user", "password2"));
    }

    @org.junit.Test
    public void testGetAllLists() throws Exception
    {
        String session = helper.getSession("user", "password");
        int userId = helper.getUserIdByLogin("user", session);
        List<String> lists = helper.getAllLists(userId);

        Assert.assertTrue(lists.size() > 0);
    }

    @org.junit.Test
    public void testGetListByName() throws Exception
    {
        String session = helper.getSession("user", "password");
        int userId = helper.getUserIdByLogin("user", session);
        String listContent = helper.getListByName("Лист", userId);
        Assert.assertNotNull(listContent);
    }

    @org.junit.Test
    public void testGetUserIdByLogin() throws Exception
    {
        String session = helper.getSession("user", "password");
        int userId = helper.getUserIdByLogin("user", session);
        Assert.assertTrue(userId != -1);

        session = helper.getSession("user2", "password");
        userId = helper.getUserIdByLogin("user", session);
        Assert.assertTrue(userId == -1);
    }
}
