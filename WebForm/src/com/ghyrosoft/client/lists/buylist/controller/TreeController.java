package com.ghyrosoft.client.lists.buylist.controller;

import com.ghyrosoft.client.dialogs.error.ErrorDialog;
import com.ghyrosoft.client.dialogs.newname.NewNameDialog;
import com.ghyrosoft.client.dialogs.newname.NewNameFormListener;
import com.ghyrosoft.client.dialogs.rename.RenameDialog;
import com.ghyrosoft.client.dialogs.rename.RenameFormListener;
import com.ghyrosoft.client.lists.buylist.controller.ui.ItemWidget;
import com.ghyrosoft.client.lists.buylist.controller.ui.ItemWidgetListener;
import com.ghyrosoft.client.lists.buylist.controller.ui.SubItemWidget;
import com.ghyrosoft.client.lists.buylist.controller.ui.SubItemWidgetListener;
import com.ghyrosoft.client.lists.buylist.model.TreeModel;
import com.ghyrosoft.client.lists.buylist.model.TreeModelListener;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

/**
 * User: sckomoroh
 * Date: 1/27/13
 * Time: 7:34 PM
 */
public class TreeController implements TreeModelListener {
    private final Tree widget;
    private final Label listNameLabel;
    private final TreeModel model;

    public TreeController(Tree widget, Label nameWidget) {
        this.widget = widget;
        this.listNameLabel = nameWidget;
        this.model = new TreeModel();

        model.setListener(this);
    }

    public TreeController(Tree widget) {
        this(widget, new Label());
    }

    public TreeController() {
        this(new Tree());
    }

    public Tree getView() {
        return this.widget;
    }

    public TreeModel getModel() {
        return model;
    }

    public Label getNameView() {
        return listNameLabel;
    }

    @Override
    public void onModelClear() {
        rebuildTree();
    }

    private void rebuildTree() {
        widget.removeItems();

        listNameLabel.setText(model.getListName());

        int itemCount = model.getItemsCount();
        for (int i = 0; i < itemCount; i++) {
            String itemName = model.getItem(i);
            ItemWidget itemWidget = new ItemWidget(itemName, i);
            TreeItem item = widget.addItem(itemWidget);

            itemWidget.addListener(new OnItemWidgetListener());

            int subItemCount = model.getSubItemsCount(i);

            for (int j = 0; j < subItemCount; j++) {
                String subItemName = model.getSubItem(i, j);
                SubItemWidget subItemWidget = new SubItemWidget(subItemName, j, i);
                widget.getItem(i).addItem(subItemWidget);

                subItemWidget.addListener(new OnSubItemListener());

                // According to GWT Issue 5119
                Element div = item.getElement();
                Element table = DOM.getFirstChild(div);
                Element tableBody = DOM.getFirstChild(table);
                Element tr = DOM.getFirstChild(tableBody);
                Element td = DOM.getChild(tr, 1);
                td.getStyle().setWidth(100, Style.Unit.PCT);
            }

            item.setState(true);
        }
    }

    @Override
    public void onAddItem(int index) {
        ItemWidget itemWidget = new ItemWidget(model.getItem(index), index);
        widget.add(itemWidget);

        itemWidget.addListener(new OnItemWidgetListener());
    }

    @Override
    public void onAddSubItem(int index1, int index2) {
        SubItemWidget subItemWidget = new SubItemWidget(model.getSubItem(index1, index2), index2, index1);

        TreeItem treeItem = widget.getItem(index1);
        treeItem.addItem(subItemWidget);
        treeItem.setState(true);

        // According to GWT Issue 5119
        Element div = treeItem.getElement();
        Element table = DOM.getFirstChild(div);
        Element tableBody = DOM.getFirstChild(table);
        Element tr = DOM.getFirstChild(tableBody);
        Element td = DOM.getChild(tr, 1);
        td.getStyle().setWidth(100, Style.Unit.PCT);

        subItemWidget.addListener(new OnSubItemListener());
    }

    @Override
    public void onRemoveItem(int index) {
        TreeItem treeItem = widget.getItem(index);
        widget.removeItem(treeItem);

        rebuildIndexes();
    }

    @Override
    public void onRemoveSubItem(int index1, int index2) {
        TreeItem treeItem = widget.getItem(index1).getChild(index2);
        widget.getItem(index1).removeItem(treeItem);

        rebuildSubIndexes(index1);
    }

    @Override
    public void onRenameItem(int index, String text) {
        ItemWidget itemWidget = (ItemWidget) widget.getItem(index).getWidget();
        itemWidget.setText(text);
    }

    @Override
    public void onRenameSubItem(int index1, int index2, String text) {
        SubItemWidget itemWidget = (SubItemWidget) widget.getItem(index1).getChild(index2).getWidget();
        itemWidget.setSubItemName(text);
    }

    @Override
    public void onListNameChanged(String listName) {
        listNameLabel.setText(listName);
    }

    private void rebuildIndexes() {
        int itemCount = widget.getItemCount();

        for (int i = 0; i < itemCount; i++) {
            TreeItem treeItem = widget.getItem(i);
            ItemWidget itemWidget = (ItemWidget) treeItem.getWidget();
            itemWidget.setModelIndex(i);

            int subItemCount = treeItem.getChildCount();

            for (int j = 0; j < subItemCount; j++) {
                SubItemWidget subItemWidget = (SubItemWidget) treeItem.getChild(j).getWidget();
                subItemWidget.setParentIndex(i);
            }
        }
    }

    private void rebuildSubIndexes(int index) {
        TreeItem treeItem = widget.getItem(index);
        int itemCount = treeItem.getChildCount();

        for (int i = 0; i < itemCount; i++) {
            SubItemWidget subItemWidget = (SubItemWidget) treeItem.getChild(i).getWidget();
            subItemWidget.setIndex(i);
        }
    }

    private class OnItemRenameFormListener implements RenameFormListener {
        private final int index;

        public OnItemRenameFormListener(int index) {
            this.index = index;
        }

        @Override
        public void onRenameClicked(String text) {
            if (text.length() > 0) {
                if (text.contains("\"") || text.contains("'")) {
                    new ErrorDialog("The name cannot contain the characters ' or \"");
                } else {
                    model.setItemText(index, text);
                }
            }
        }
    }

    private class OnSubItemRenameFormListener implements RenameFormListener {
        private final int index;
        private final int parentIndex;

        public OnSubItemRenameFormListener(int parentIndex, int index) {
            this.index = index;
            this.parentIndex = parentIndex;
        }

        @Override
        public void onRenameClicked(String text) {
            if (text.length() > 0) {
                if (text.contains("\"") || text.contains("'")) {
                    new ErrorDialog("The name cannot contain the characters ' or \"");
                } else {
                    model.setSubItemText(parentIndex, index, text);
                }
            }
        }
    }

    private class OnItemWidgetListener implements ItemWidgetListener {
        @Override
        public void onAddButtonClick(int index) {
            final int itemIndex = index;
            new NewNameDialog(new NewNameFormListener() {
                @Override
                public void onNewName(String name) {
                    if (name.length() > 0) {
                        if (name.contains("'") || name.contains("\"")) {
                            new ErrorDialog("The name cannot contains characters ' or \"");
                        } else {
                            model.addSubItem(itemIndex, name);
                        }
                    }
                }
            });
        }

        @Override
        public void onEditButtonClick(String text, int index) {
            new RenameDialog(text, new OnItemRenameFormListener(index));
        }

        @Override
        public void onRemoveButtonClick(int index) {
            model.removeItem(index);
        }
    }

    private class OnSubItemListener implements SubItemWidgetListener {
        @Override
        public void onEditButtonClick(String text, int parentIndex, int index) {
            new RenameDialog(text, new OnSubItemRenameFormListener(parentIndex, index));
        }

        @Override
        public void onRemoveButtonClick(int parentIndex, int index) {
            model.removeSubItem(parentIndex, index);
        }
    }
}
