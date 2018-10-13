package net.ghyrosoft;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/buylist")
public class BuyListService
{
    private final DataBaseHelper dataBaseHelper = new DataBaseHelper();

    @GET
    @Path("/test")
    @Produces(MediaType.TEXT_HTML)
    public String test()
    {
        return "<html><body>Hello, this is test method!</body></html>";
    }

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_XML)
    public String login(@FormParam("login") String login, @FormParam("password") String password)
    {
        String session;
        try
        {
            session = dataBaseHelper.getSession(login, password);
        } catch (Exception e)
        {
            return String.format("<response><error value='%s' /></response>", e.getMessage());
        }

        if (session == null)
        {
            return "<response><error value='Login failed'></response>";
        }

        return String.format("<response><session value='%s'/></response>", session);
    }

    @POST
    @Path("/getAllLists")
    @Produces(MediaType.APPLICATION_XML)
    public String getAllLists(@FormParam("login") String login, @FormParam("session") String session)
    {
        int userId;
        try
        {
            userId = dataBaseHelper.getUserIdByLogin(login, session);
        } catch (Exception e)
        {
            return String.format("<response><error value='%s' /></response>", e.getMessage());
        }

        if (userId == -1)
        {
            return String.format("<response><error value='invalid session for user %s' /></response>", login);
        }

        List<String> lists;
        try
        {
            lists = dataBaseHelper.getAllLists(userId);
        } catch (Exception e)
        {
            return String.format("<response><error value='%s' /></response>", e.getMessage());
        }

        String result = "<response>";
        for (String list : lists)
        {
            result += String.format("<list value='%s' />", list);
        }

        result += "</response>";

        return result;
    }

    @POST
    @Path("/getListByName")
    @Produces(MediaType.APPLICATION_XML)
    public String getListByName(@FormParam("listName") String listName, @FormParam("login") String login, @FormParam("session") String session)
    {
        int userId;
        try
        {
            userId = dataBaseHelper.getUserIdByLogin(login, session);
        } catch (Exception e)
        {
            return String.format("<response><error value='%s' /></response>", e.getMessage());
        }

        if (userId == -1)
        {
            return String.format("<response><error value='invalid session for user %s' /></response>", login);
        }

        String result;
        try
        {
            result = dataBaseHelper.getListByName(listName, userId);
        } catch (Exception e)
        {
            return String.format("<response><error value='%s' /></response>", e.getMessage());
        }

        return String.format("<response>%s</response>", result);
    }

    @POST
    @Path("/checkLogin")
    @Produces(MediaType.APPLICATION_XML)
    public String checkLogin(@FormParam("login") String login)
    {
        boolean res;
        try
        {
            res = dataBaseHelper.checkUserName(login);
        } catch (Exception e)
        {
            return String.format("<response><error value='%s' /></response>", e.getMessage());
        }

        if (res)
        {
            return "<response><true /></response>";
        }

        return "<response><false /></response>";
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_XML)
    public String register(@FormParam("login") String login, @FormParam("password") String password)
    {
        boolean res;
        try
        {
            res = dataBaseHelper.register(login, password);
        } catch (Exception e)
        {
            return String.format("<response><error value='%s' /></response>", e.getMessage());
        }

        if (!res)
        {
            return "<response><error value='Unable to register user'/></response>";
        }

        return login(login, password);
    }

    @POST
    @Path("/unregister")
    @Produces(MediaType.APPLICATION_XML)
    public void unregister(@FormParam("login") String login, @FormParam("session") String session)
    {
        try
        {
            dataBaseHelper.unregister(login, session);
        }
        catch (Exception e)
        {
        }
    }
}