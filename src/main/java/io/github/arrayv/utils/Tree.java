package io.github.arrayv.utils;

public class Tree {
    private Node root;

    private int leftCount = 0;
    private int rightCount = 0;

    public Tree() {
    }

    public Tree(Node root) {
        this.root = root;
    }

    public Tree(Integer rootValue, int index) {
        this.root = new Node(rootValue, index);
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public int getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(int leftCount) {
        this.leftCount = leftCount;
    }

    public int getRightCount() {
        return rightCount;
    }

    public void setRightCount(int rightCount) {
        this.rightCount = rightCount;
    }

    public void increRightCount() {
        rightCount++;
    }

    public void increLeftCount() {
        leftCount++;
    }

    public void decreRightCount() {
        rightCount--;
    }

    public void decreLeftCount() {
        leftCount--;
    }

    public static class Node {
        private Integer value;
        private int index;


        private Node parent;
        private Node left;
        private Node right;

        public Node(Integer value, int index) {
            this.value = value;
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" +
                    "value=" + value +
                    (left == null ? "" : (", left=" + left)) +
                    (right == null ? "" : (", right=" + right)) +
                    "}";
        }
    }

    @Override
    public String toString() {
        return "Tree{" +
                "root=" + root +
                '}';
    }
}
