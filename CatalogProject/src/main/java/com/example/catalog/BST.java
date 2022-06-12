package com.example.catalog;

import java.util.ArrayList;

class TreeNode {
    private Object object;
    private TreeNode left;
    private TreeNode right;

    public TreeNode(Object object, TreeNode left, TreeNode right){
        this.object = object;
        this.left = left;
        this.right = right;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public TreeNode getRight() {
        return right;
    }
    
    public void setObject(Object object){
        this.object = object;
    }

    public Object getObject() {
        return object;
    }
}

public class BST {
    private TreeNode root;

    public void insert(Object object){ root = insert(object,root); }
    public void remove(Object object){ root = remove(object, root); }
    public ArrayList<Object> find(String string){ return find(new ArrayList<>(), string, root); }
    public ArrayList<Object> inOrder(){ return inOrder(new ArrayList<>(),root); }

    public TreeNode insert(Object o, TreeNode n){
        if (n == null) 
            n = new TreeNode(o, null, null);
        else {
            int compare = o.toString().compareToIgnoreCase(n.getObject().toString());
            if (compare < 0)
                n.setLeft(insert(o,n.getLeft()));
            else if (compare > 0)
                n.setRight(insert(o,n.getRight()));
        }
        return n;		
    }
    
    public TreeNode remove(Object o, TreeNode r) {
        if (r != null){
            int compare = o.toString().compareToIgnoreCase(r.getObject().toString());

            if (compare < 0)
                r.setLeft(remove(o,r.getLeft()));
            else if (compare > 0)
                r.setRight(remove(o,r.getRight()));
            
            else { // compare == 0
                if (r.getLeft() != null && r.getRight() != null) {
                    TreeNode pointer = r.getRight();
                    while (pointer.getLeft() != null)
                        pointer = pointer.getLeft();

                    r.setObject(pointer.getObject());
                    r.setRight(remove(r.getObject(), r.getRight()));
                }
                else if (r.getLeft() != null)
                    r = r.getLeft();
                else // r.getRight() != null
                    r = r.getRight();
            }
        }
        return r;
    }

    public ArrayList<Object> find(ArrayList<Object> list , String s, TreeNode f){
        if (f != null){
            int compare;

            if (s.length() <= f.getObject().toString().length())
                compare = s.compareToIgnoreCase(
                        f.getObject().toString().substring(0,s.length()));
            else // if the word we are looking for is longer than Node's object's name
                compare = s.substring(0,f.getObject().toString().length())
                        .compareToIgnoreCase(f.getObject().toString());

            if (compare < 0)
                find(list, s, f.getLeft());
            else if (compare > 0)
                find(list, s, f.getRight());
            else { // if compare == 0
                list.add(f.getObject());
                find(list, s, f.getLeft());
                find(list, s, f.getRight());
            }
        }
        return list;
    }

    public ArrayList<Object> inOrder(ArrayList<Object> arrayList, TreeNode i){
        if (i != null){
            inOrder(arrayList, i.getLeft());
            arrayList.add(i.getObject());
            inOrder(arrayList, i.getRight());
        }
        return arrayList;
    }
}
