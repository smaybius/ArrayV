package io.github.arrayv.utils;

public class TreeUtil {

    private Reads Reads;
    private Writes Writes;
    private Delays Delays;
    private double sleep;

    public TreeUtil(Reads Reads, Writes Writes, Delays Delays, double sleep) {
        this.Reads = Reads;
        this.Writes = Writes;
        this.Delays = Delays;
        this.sleep = sleep;
    }

    public int countSelfAndChildren(io.github.arrayv.utils.Tree.Node node) {
        if (node == null) {
            return 0;
        }
        int count = 1;
        io.github.arrayv.utils.Tree.Node left = node.getLeft();
        if (left != null) {
            count += countSelfAndChildren(left);
        }
        io.github.arrayv.utils.Tree.Node right = node.getRight();
        if (right != null) {
            count += countSelfAndChildren(right);
        }
        return count;
    }

    public int countLeftChildren(io.github.arrayv.utils.Tree.Node node) {
        return countSelfAndChildren(node.getLeft());
    }

    public int countRightChildren(io.github.arrayv.utils.Tree.Node node) {
        return countSelfAndChildren(node.getRight());
    }

    public void addNodeToTree(Tree tree, final io.github.arrayv.utils.Tree.Node root, io.github.arrayv.utils.Tree.Node node) {
        if (root == null) {
            tree.setRoot(node);
            return;
        }
        addNodeByRoot(root, node);
        if (root.getValue() > node.getValue()) {
            tree.increLeftCount();
        } else {
            tree.increRightCount();
        }
        reconstructIfUnbalanced(tree);
    }

    public void addNodeByRoot(io.github.arrayv.utils.Tree.Node pointer, io.github.arrayv.utils.Tree.Node node) {
        Integer nodeValue = node.getValue();
        while (true) {
            Reads.addComparison();
            Delays.sleep(sleep);
            if (pointer.getValue() > nodeValue) {
                if (pointer.getLeft() == null) {
                    pointer.setLeft(node);
                    node.setParent(pointer);
                    Writes.changeAuxWrites(1);
                    break;
                } else {
                    pointer = pointer.getLeft();
                }
            } else {
                if (pointer.getRight() == null) {
                    pointer.setRight(node);
                    node.setParent(pointer);
                    Writes.changeAuxWrites(1);
                    break;
                } else {
                    pointer = pointer.getRight();
                }
            }
        }
    }

    private io.github.arrayv.utils.Tree.Node getExtremum(io.github.arrayv.utils.Tree.Node pointer, boolean isPickMax) {
        if (isPickMax) {
            while (pointer.getRight() != null) {
                pointer = pointer.getRight();
            }
        } else {
            while (pointer.getLeft() != null) {
                pointer = pointer.getLeft();
            }
        }
        return pointer;
    }

    private void delinkNode(io.github.arrayv.utils.Tree.Node node) {
        io.github.arrayv.utils.Tree.Node parent = node.getParent();
        io.github.arrayv.utils.Tree.Node left = node.getLeft();
        io.github.arrayv.utils.Tree.Node right = node.getRight();

        if (parent != null) {
            if (parent.getLeft() == node) {
                parent.setLeft(null);
            } else if (parent.getRight() == node) {
                parent.setRight(null);
            }
        }

        if (left != null) {
            left.setParent(null);
        }
        if (right != null) {
            right.setParent(null);
        }

        node.setParent(null);
        node.setLeft(null);
        node.setRight(null);
    }

    public void reconstruct(Tree tree, boolean isPickMax) {
        // 1. 将根节点摘出
        io.github.arrayv.utils.Tree.Node root = tree.getRoot();
        io.github.arrayv.utils.Tree.Node leftTree = root.getLeft();
        io.github.arrayv.utils.Tree.Node rightTree = root.getRight();
        delinkNode(root);

        io.github.arrayv.utils.Tree.Node moreNodeTree, lessNodeTree;
        if (isPickMax) {
            moreNodeTree = leftTree;
            lessNodeTree = rightTree;
        } else {
            moreNodeTree = rightTree;
            lessNodeTree = leftTree;
        }

        // 2. 将根节点加入到节点数较少的子树中
        if (lessNodeTree != null) {
            addNodeByRoot(lessNodeTree, root);
        } else {
            if (isPickMax) {
                rightTree = root;
            } else {
                leftTree = root;
            }
        }

        // 摘出最值
        io.github.arrayv.utils.Tree.Node extremum = getExtremum(moreNodeTree, isPickMax);
        io.github.arrayv.utils.Tree.Node remainderTree = isPickMax ? extremum.getLeft() : extremum.getRight();
        if (moreNodeTree == extremum) {
            moreNodeTree = null;
        }
        delinkNode(extremum);

        // 3. 合并被最值拆散的子树
        if (moreNodeTree != null && remainderTree != null) {
            addNodeByRoot(moreNodeTree, remainderTree);
        } else {
            if (isPickMax) {
                leftTree = moreNodeTree != null ? moreNodeTree : remainderTree;
            } else {
                rightTree = moreNodeTree != null ? moreNodeTree : remainderTree;
            }
        }

        // 将最值设置为新的根节点
        tree.setRoot(extremum);
        extremum.setLeft(leftTree);
        extremum.setRight(rightTree);

        // 更新树的左右子树的大小
        if (isPickMax) {
            tree.decreLeftCount();
            tree.increRightCount();
        } else {
            tree.decreRightCount();
            tree.increLeftCount();
        }
    }

    public void reconstructIfUnbalanced(Tree tree) {
//        int leftChildren = countLeftChildren(tree.getRoot());
//        int rightChildren = countRightChildren(tree.getRoot());
        int leftChildren = tree.getLeftCount();
        int rightChildren = tree.getRightCount();
        if (Math.abs(leftChildren - rightChildren) < 2) {
            return;
        }
        reconstruct(tree, leftChildren > rightChildren);
    }
}
