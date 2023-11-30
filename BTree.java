package cse214hw3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class BTree<E extends Comparable<E>>{
    private final int min;
    private BTNode root;
    public class BTNode{
        private int num;
        private boolean leaf;
        private ArrayList<E> values;
        private ArrayList<BTNode> children;
        public BTNode(){
            this.leaf = true;
            this.num = 0;
            this.values = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        public String toString(){
            String s = "";
            s += "[" + values.get(0);
            for (int i = 1; i < num; i++){
                s += ", " + values.get(i);
            }
            s += "]";
            return s;
        }
    }
    public BTree(int minimumDegree){
        if(minimumDegree <= 1) throw new IllegalArgumentException("Illegal Argument.");
        else{
            this.min = minimumDegree;
            this.root = new BTNode();
        }
    }

    public void add(E element){
        if(root.num == min * 2 - 1) splitRoot();
        BTNode curr = root;
        while(!curr.leaf){
            BTNode node = curr;
            for(int i = 0; i < node.num; i++){
                int cmpr = curr.values.get(i).compareTo(element);
                if(cmpr > 0){
                    if(curr.children.get(i).num == min * 2 - 1) {
                        splitNode(curr, i);
                        if (curr.values.get(i).compareTo(element) > 0) i++;
                    }
                    curr = curr.children.get(i);
                    break;
                }else if(cmpr == 0) return;
                else if(i == curr.num - 1 && !curr.children.isEmpty()){
                    i++;
                    if(curr.children.get(i).num == min * 2 - 1){
                        splitNode(curr, i);
                        if(curr.values.get(i).compareTo(element) < 0) i++;
                    }
                    curr = curr.children.get(i);
                }
            }
        }
        for(int i = 0; i < curr.num; i++){
            int cmpr = curr.values.get(i).compareTo(element);
            if (cmpr == 0) return;
            else if(cmpr > 0){
                curr.values.add(i, element);
                curr.num++;
                return;
            }
        }
        curr.values.add(element);
        curr.num++;
    }

    private void splitNode(BTNode node, int index){
        BTNode child = node.children.get(index);
        BTNode newChild = new BTNode();
        node.children.add(index+1, newChild);
        for(int i = min - 1; i < min * 2 - 1; i++){
            if(i == min - 1){
                node.values.add(index, child.values.remove(i));
            }else{
                newChild.values.add(child.values.remove(min - 1));
            }
        }
        if(!child.leaf){
            newChild.leaf = false;
            for(int i = min; i < min * 2; i++){
                newChild.children.add(child.children.remove(min));
            }
        }
        node.num++;
        child.num = min - 1;
        newChild.num = min - 1;
    }

    private void splitRoot(){
        BTNode newRoot = new BTNode();
        newRoot.leaf = false;
        BTNode newRight = new BTNode();
        for(int i = min - 1; i < min * 2 - 1; i++){
            if(i == min - 1){
                newRoot.values.add(root.values.remove(i));
            }else{
                newRight.values.add(root.values.remove(min - 1));
            }
        }
        if(!root.leaf){
            newRight.leaf = false;
            for(int i = min; i < min * 2; i++){
                newRight.children.add(root.children.remove(min));
            }
        }
        newRoot.num = 1;
        root.num = min - 1;
        newRight.num = min - 1;
        newRoot.children.add(root);
        newRoot.children.add(newRight);
        this.root = newRoot;
    }

    public NodeAndIndex find(E element){
        BTNode node = root;
        while(!node.values.contains(element)){
            if(node.leaf) return null;
            BTNode curr = node;
            for(int i = 0; i < curr.num; i++){
                int cmpr = node.values.get(i).compareTo(element);
                if(cmpr > 0){
                    node = node.children.get(i);
                    break;
                }
                if (i == node.num - 1) node = node.children.get(node.num);
            }
        }
        return new NodeAndIndex(node, node.values.indexOf(element));
    }
    public void addAll(Collection<E> elements) throws UnsupportedOperationException{
        for(E e : elements) this.add(e);
    }

    public void show(){
        String nodesep = "    ";
        Queue<BTNode> queue1 = new LinkedList<>();
        Queue<BTNode> queue2 = new LinkedList<>();
        queue1.add(root);
        while(true){
            while(!queue1.isEmpty()){
                BTNode node = queue1.poll();
                System.out.printf("%s%s", node.toString(), nodesep);
                if (!node.children.isEmpty()){
                    queue2.addAll(node.children);
                }
            }
            System.out.printf("%n");
            if(queue2.isEmpty())break;
            else{
                queue1 = queue2;
                queue2 = new LinkedList<>();
            }
        }
    }

    public class NodeAndIndex {
        BTNode node;
        int index;
        public NodeAndIndex(BTNode node, int index){
            this.node = node;
            this.index = index;
        }
        public String toString() {
            return ("<" + node.toString() + ", " + index + ">");
        }
    }
}
