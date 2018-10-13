package com.gherasoft.buyList.adapter;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sckomoroh
 * Date: 1/15/13
 * Time: 10:32 AM
 */
public class Model {
    private List<GroupModel> mGroups = new ArrayList<GroupModel>();
    private ModelListener mListener;

    public void setListener(ModelListener listener)
    {
        mListener = listener;
    }

    public int groupCount()
    {
        return mGroups.size();
    }

    public GroupModel getGroup(int index)
    {
        return mGroups.get(index);
    }

    public List<GroupModel> getGroups()
    {
        return mGroups;
    }

    public void buildModel(Element rootElement)
    {
        mGroups.clear();
        int itemCount = 0;

        NodeList nodeList = rootElement.getChildNodes();

        String listName = rootElement.getAttribute("name");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tempNode = nodeList.item(i);

            if (tempNode.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            Element item = (Element) tempNode;
            GroupModel groupItem = new GroupModel(item.getAttribute("name"));

            NodeList subItems = item.getChildNodes();

            for (int j = 0; j < subItems.getLength(); j++) {
                Node tempSubNode = subItems.item(j);

                if (tempSubNode.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }

                Element subItem = (Element) tempSubNode;
                groupItem.addItem(subItem.getAttribute("name"));

                itemCount++;
            }

            mGroups.add(groupItem);
        }

        if (mListener != null)
        {
            mListener.modelRebuilded(itemCount, listName);
        }
    }
}
