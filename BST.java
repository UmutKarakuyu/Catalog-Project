import java.util.ArrayList;

class Node {
    private Object object;
    private Node left;
    private Node right;

    public Node(Object object, Node left, Node right){
        this.object = object;
        this.left = left;
        this.right = right;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getLeft() {
        return left;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public Node getRight() {
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
    private Node root;

    public void insert(Object object){ root = insert(object,root); }
    public void remove(Object object){ root = remove(object, root); }
    
    public Node insert(Object o, Node n){
        
        if (n == null) 
            n = new Node(o, null, null);
            
        else {
            int compare = o.toString().compareToIgnoreCase(n.getObject().toString());
            if (compare < 0)
                n.setLeft(insert(o,n.getLeft()));
            else if (compare > 0)
                n.setRight(insert(o,n.getRight()));
        }
        return n;		
    }
    
    public Node remove(Object o, Node r) {
        if (r != null){
            int compare = o.toString().compareToIgnoreCase(r.getObject().toString());

            if (compare < 0)
                r.setLeft(remove(o,r.getLeft()));
            else if (compare > 0)
                r.setRight(remove(o,r.getRight()));
            
            else { // compare == 0
                if (r.getLeft() != null && r.getRight() != null) {
                    Node pointer = r.getRight();
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
