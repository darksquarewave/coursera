package alg;

public class UnionFindFactory {

    public static UnionFind getUnionFindInstance(Class<? extends UnionFind> clazz, int n) {
        if (clazz == EagerUnionFind.class) {
            return new EagerUnionFind(n);
        }
        else if (clazz == QuickUnionFindLazy.class) {
            return new QuickUnionFindLazy(n);
        }
        else if (clazz == QuickUnionFindWeighted.class) {
            return new QuickUnionFindWeighted(n);
        }
        else if (clazz == QuickUnionFindWeightedPathCompression.class) {
            return new QuickUnionFindWeightedPathCompression(n);
        }
        else if (clazz == QuickUnionFindWeightedPathCompressionTwoPass.class) {
            return new QuickUnionFindWeightedPathCompressionTwoPass(n);
        }
        else if (clazz == StdQuickFind.class) {
            return new StdQuickFind(n);
        }
        else if (clazz == StdWeightedQuickFind.class) {
            return new StdWeightedQuickFind(n);
        }
        else {
            throw new IllegalArgumentException("unknown union find class");
        }
    }
}
