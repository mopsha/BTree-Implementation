package cse214hw3;

import javax.naming.OperationNotSupportedException;
import java.util.Arrays;

public class BTreeRunner {
    public static void main(String[] args) throws OperationNotSupportedException {
        BTree<Integer> tree = new BTree<>(3);
        tree.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12, 22, 19, 25, 100, 88, 64, 65, 16));
        tree.add(99);
        tree.show();
        System.out.println(tree.find(100));
    }
}
