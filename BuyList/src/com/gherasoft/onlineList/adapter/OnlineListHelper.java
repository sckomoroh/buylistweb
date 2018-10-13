package com.gherasoft.onlineList.adapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;

/**
 * User: sckomoroh
 * Date: 1/15/13
 * Time: 10:45 PM
 */
public class OnlineListHelper
{
    private static String mUser;
    private static String mSession;
    private static String mErrorStr;
    private OnlineListAdapter mAdapter;

    private static final String SERVICE_HOST = "buylist.j.rsnx.ru";
    private static final String SERVICE_PATH = "/service/buylist";

    public OnlineListHelper(OnlineListAdapter adapter)
    {
        mAdapter = adapter;
    }

    public static boolean login(String login, String password)
    {
        try
        {
            String postParams = String.format("login=%s&password=%s",
                                              login,
                                              password);

            String responseContent = sendPostRequest("login", postParams);
            Document responseDoc = contentToXml(responseContent);

            getError(responseDoc);

            if (hasError())
            {
                return false;
            }

            mUser = login;
            mSession = getSession(responseDoc);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            mErrorStr = ex.getMessage();
            return false;
        }

        return mSession != null;
    }

    public static boolean register(String login, String password)
    {
        try
        {
            String postParams = String.format("login=%s&password=%s",
                                              login,
                                              password);

            String response = sendPostRequest("register", postParams);
            Document responseDoc = contentToXml(response);

            getError(responseDoc);

            if (hasError())
            {
                return false;
            }

            mUser = login;
            mSession = getSession(responseDoc);
        } catch (Exception ex)
        {
            ex.printStackTrace();
            mErrorStr = ex.getMessage();
            return false;
        }

        return true;
    }

    public boolean getLists()
    {
        try
        {
            String postParams = String.format("login=%s&session=%s",
                                              mUser,
                                              mSession);

            String response = sendPostRequest("getAllLists", postParams);
            Document responseDoc = contentToXml(response);

            getError(responseDoc);

            if (hasError())
            {
                return false;
            }

            NodeList listNodes = responseDoc.getDocumentElement().getChildNodes();
            for (int i = 0; i < listNodes.getLength(); i++)
            {
                Node node = listNodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE)
                {
                    continue;
                }

                Element listElement = (Element) node;
                if (listElement.hasAttribute("value"))
                {
                    mAdapter.addList(listElement.getAttribute("value"));
                }
            }

            mAdapter.notifyDataSetChanged();
        } catch (Exception ex)
        {
            ex.printStackTrace();
            mErrorStr = ex.getMessage();
            return false;
        }

        return true;
    }

    public Document getList(String listName)
    {
        try
        {
            String postParams = String.format("login=%s&session=%s&listName=%s",
                                              mUser,
                                              mSession,
                                              listName);

            String response = sendPostRequest("getListByName", postParams);
            Document responseDoc = contentToXml(response);

            getError(responseDoc);

            if (hasError())
            {
                return null;
            }

            return responseDoc;

        } catch (Exception ex)
        {
            ex.printStackTrace();
            mErrorStr = ex.getMessage();
        }

        return null;
    }

    public static boolean hasError()
    {
        return mErrorStr != null;
    }

    public String getLastError()
    {
        return mErrorStr;
    }

    private static String sendPostRequest(String path, String postParams) throws IOException
    {
        HttpPost httpPost = new HttpPost(buildUrl(path));
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setEntity(new StringEntity(postParams, "UTF-8"));

        HttpParams myParams = new BasicHttpParams();
        DefaultHttpClient httpClient = new DefaultHttpClient(myParams);
        BasicHttpContext localContext = new BasicHttpContext();

        HttpResponse response = httpClient.execute(httpPost, localContext);
        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }

    private static void getError(Document doc)
    {
        mErrorStr = null;

        Element root = doc.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("error");

        if (nodeList.getLength() > 0)
        {
            Element errorElement = (Element) nodeList.item(0);
            mErrorStr = errorElement.getAttribute("value");
        }
    }

    private static String getSession(Document doc)
    {
        Element root = doc.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName("session");

        if (nodeList.getLength() > 0)
        {
            Element sessionElement = (Element) nodeList.item(0);

            if (sessionElement.hasAttribute("value"))
            {
                return sessionElement.getAttribute("value");
            }
        }

        return null;
    }

    private static Document contentToXml(String xmlString)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try
        {
            builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    private static String buildUrl(String path)
    {
        return String.format("http://%s%s/%s",
                             SERVICE_HOST,
                             SERVICE_PATH,
                             path);
    }
}
