package com.ghyrosoft.client.lists.buylist.model;

import com.ghyrosoft.client.common.KeyValue;
import com.ghyrosoft.client.common.StringUtil;
import com.google.gwt.xml.client.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: sckomoroh
 * Date: 1/27/13
 * Time: 7:31 PM
 */
public class TreeModel
{
    private TreeModelListener listener;
    private final List<KeyValue<String, List<String>>> items = new ArrayList<KeyValue<String, List<String>>>();
    private String listName;

    public void buildModelFromXml(Document doc)
    {
        items.clear();

        listName = doc.getDocumentElement().getAttribute("name");

        NodeList nodeList = doc.getDocumentElement().getChildNodes();
        int nodeCount = nodeList.getLength();

        for(int i=0; i<nodeCount; i++)
        {
            Node categoryNode = nodeList.item(i);
            if (categoryNode.getNodeType() != Node.ELEMENT_NODE)
            {
                continue;
            }

            Element categoryElement = (Element)categoryNode;
            KeyValue<String, List<String>> item = new KeyValue<String, List<String>>(
                    categoryElement.getAttribute("name"),
                    new ArrayList<String>());

            items.add(item);

            NodeList subItems = categoryElement.getChildNodes();
            int subItemsCount = subItems.getLength();

            for (int j=0; j<subItemsCount; j++)
            {
                Node subItemNode = subItems.item(j);
                if (subItemNode.getNodeType() != Node.ELEMENT_NODE)
                {
                    continue;
                }

                Element subItemElement = (Element)subItemNode;
                item.getValue().add(subItemElement.getAttribute("name"));
            }
        }

        fireModelClear();
    }

    public Document buildDocumentFromModel()
    {
        String xmlContent = buildXmlContentFromModel();
        return XMLParser.parse(xmlContent);
    }

    public String buildXmlContentFromModel()
    {
        String stringXml = StringUtil.format("<categories name='{0}'>", listName);

        for(KeyValue<String, List<String>> item : items)
        {
            stringXml += StringUtil.format("<category name='{0}'>", item.getKey());

            for (String subItem : item.getValue())
            {
                stringXml += StringUtil.format("<item name='{0}' />", subItem);
            }

            stringXml += "</category>";
        }

        stringXml += "</categories>";

        return stringXml;
    }

    public void setListName(String listName)
    {
        this.listName = listName;

        fireListNameChanged();
    }

    public String getListName()
    {
        return listName;
    }

    public void setListener(TreeModelListener listener)
    {
        this.listener = listener;
    }

    public void clear()
    {
        items.clear();

        fireModelClear();
    }

    public void addItem(String value)
    {
        KeyValue<String, List<String>> item = new KeyValue<String, List<String>>(value, new ArrayList<String>());
        items.add(item);

        int index = items.size() - 1;

        fireAddItem(index);
    }

    public void addSubItem(int index, String value)
    {
        items.get(index).getValue().add(value);

        int index2 = items.get(index).getValue().size() - 1;

        fireAddSubItem(index, index2);
    }

    public String getItem(int index)
    {
        return items.get(index).getKey();
    }

    public String getSubItem(int index1, int index2)
    {
        return items.get(index1).getValue().get(index2);
    }

    public void removeItem(int index)
    {
        items.remove(index);
        fireRemoveItem(index);
    }

    public void removeSubItem(int parentIndex, int index)
    {
        items.get(parentIndex).getValue().remove(index);
        fireRemoveSubItem(parentIndex, index);
    }

    public void setItemText(int index, String text)
    {
        items.get(index).setKey(text);

        fireRenameItem(index, text);
    }

    public void setSubItemText(int index1, int index2, String text)
    {
        items.get(index1).getValue().set(index2, text);

        fireRenameSubItem(index1, index2, text);
    }

    public int getItemsCount()
    {
        return items.size();
    }

    public int getSubItemsCount(int index)
    {
        return items.get(index).getValue().size();
    }

    private void fireModelClear()
    {
        if (listener != null)
        {
            listener.onModelClear();
        }
    }

    private void fireAddItem(int index)
    {
        if (listener != null)
        {
            listener.onAddItem(index);
        }
    }

    private void fireAddSubItem(int index1, int index2)
    {
        if (listener != null)
        {
            listener.onAddSubItem(index1, index2);
        }
    }

    private void fireRemoveItem(int index)
    {
        if (listener != null)
        {
            listener.onRemoveItem(index);
        }
    }

    private void fireRemoveSubItem(int index1, int index2)
    {
        if (listener != null)
        {
            listener.onRemoveSubItem(index1, index2);
        }
    }

    private void fireRenameItem(int index, String text)
    {
        if (listener != null)
        {
            listener.onRenameItem(index, text);
        }
    }

    private void fireRenameSubItem(int index1, int index2, String text)
    {
        if (listener != null)
        {
            listener.onRenameSubItem(index1, index2, text);
        }
    }

    private void fireListNameChanged()
    {
        if (listener != null)
        {
            listener.onListNameChanged(listName);
        }
    }
}
