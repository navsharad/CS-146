// Models an N-by-N percolation system.

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF unionFind;
    private WeightedQuickUnionUF backwashFix;
    private int numOfSites;
    private int numOfOpenSites;
    private int source, sink;

    // Create an N-by-N grid, with all sites blocked.
    public Percolation(int N) {
        if (N <= 0)
            throw new java.lang.IllegalArgumentException();
        numOfSites = N;
        grid = new boolean[numOfSites][numOfSites];
        unionFind = new WeightedQuickUnionUF(numOfSites * numOfSites + 2);
        backwashFix = new WeightedQuickUnionUF(numOfSites * numOfSites + 2);
        source = numOfSites * numOfSites;
        sink = numOfSites * numOfSites + 1;
        numOfOpenSites = 0;
    }

    // Open site (row, col) if it is not open already.
    public void open(int row, int col) {
        if (row < 0 || row > numOfSites - 1 || col < 0 || col > numOfSites - 1)
            throw new java.lang.IndexOutOfBoundsException();
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            numOfOpenSites++;

            if (row == 0) {
                unionFind.union(encode(row, col), source);
                backwashFix.union(encode(row, col), source);
            }
            if (row == numOfSites - 1)
                unionFind.union(encode(row, col), sink);
            if (col - 1 >= 0 && isOpen(row, col - 1)) {
                unionFind.union(encode(row, col), encode(row, col - 1));
                backwashFix.union(encode(row, col), encode(row, col - 1));
            }
            if (col + 1 < numOfSites && isOpen(row, col + 1)) {
                unionFind.union(encode(row, col), encode(row, col + 1));
                backwashFix.union(encode(row, col), encode(row, col + 1));
            }
            if (row + 1 < numOfSites && isOpen(row + 1, col)) {
                unionFind.union(encode(row, col), encode(row + 1, col));
                backwashFix.union(encode(row, col), encode(row + 1, col));

            }
            if (row - 1 >= 0 && isOpen(row - 1, col)) {
                unionFind.union(encode(row, col), encode(row - 1, col));
                backwashFix.union(encode(row, col), encode(row - 1, col));
            }
        }

    }

    // Is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || row > numOfSites - 1 || col < 0 || col > numOfSites - 1)
            throw new java.lang.IndexOutOfBoundsException();
        return grid[row][col];
    }

    // Is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 0 || row > numOfSites - 1 || col < 0 || col > numOfSites - 1)
            throw new java.lang.IndexOutOfBoundsException();
        return backwashFix.connected(source, encode(row, col));
    }

    // Number of open sites.
    public int numberOfOpenSites() {
        return numOfOpenSites;
    }

    // Does the system percolate?
    public boolean percolates() {
        return unionFind.connected(source, sink);
    }

    // An integer ID (1...N) for site (row, col).
    private int encode(int row, int col) {
        if (row < 0 || row > numOfSites - 1 || col < 0 || col > numOfSites - 1)
            throw new java.lang.IndexOutOfBoundsException();
        return (numOfSites * row + col);
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Percolation perc = new Percolation(N);
        while (!in.isEmpty()) {
            int i = in.readInt();
            int j = in.readInt();
            perc.open(i, j);
        }
        StdOut.println(perc.numberOfOpenSites() + " open sites");
        if (perc.percolates()) {
            StdOut.println("percolates");
        } else {
            StdOut.println("does not percolate");
        }

        // Check if site (i, j) optionally specified on the command line
        // is full.
        if (args.length == 3) {
            int i = Integer.parseInt(args[1]);
            int j = Integer.parseInt(args[2]);
            StdOut.println(perc.isFull(i, j));
        }
    }
}

/*
// Models an N-by-N percolation system.

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF unionFind;
    private int numOfSites;
    private int numOfOpenSites;
    private int source, sink;

    // Create an N-by-N grid, with all sites blocked.
    //...
    public Percolation(int N) {
        if (N <= 0)
            throw new java.lang.IllegalArgumentException();
        numOfSites = N;
        grid = new boolean[numOfSites][numOfSites];
        unionFind = new WeightedQuickUnionUF(numOfSites * numOfSites + 2);
        source = 0;
        sink = numOfSites * numOfSites + 1;
        numOfOpenSites = 0;
    }

    // Open site (row, col) if it is not open already.
    public void open(int row, int col) {
        if (row < 1 || row > numOfSites || col < 1 || col > numOfSites)
            throw new java.lang.IndexOutOfBoundsException();
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true;
            numOfOpenSites++;

            if (row == 1)
                unionFind.union(encode(row, col), source);
            if (row == numOfOpenSites)
                unionFind.union(encode(row, col), sink);
            if (col > 1 && isOpen(row, col - 1))
                unionFind.union(encode(row, col), encode(row, col - 1));
            if (col < numOfSites && isOpen(row, col + 1))
                unionFind.union(encode(row, col), encode(row, col + 1));
            if (row < numOfSites && isOpen(row + 1, col))
                unionFind.union(encode(row, col), encode(row + 1, col));
            if (row > numOfSites && isOpen(row - 1, col))
                unionFind.union(encode(row, col), encode(row - 1, col));
        }

    }

    // Is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > numOfSites || col < 1 || col > numOfSites)
            throw new java.lang.IndexOutOfBoundsException();
        return grid[row - 1][col - 1];
    }

    // Is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > numOfSites || col < 1 || col > numOfSites)
            throw new java.lang.IndexOutOfBoundsException();
        return unionFind.connected(source, encode(row, col)); //maybe use numOfSites * numOfSites as first arg
    }

    // Number of open sites.
    public int numberOfOpenSites() {
        return numOfOpenSites;
    }

    // Does the system percolate?
    public boolean percolates() {
        return unionFind.connected(source, sink);
    }

    // An integer ID (1...N) for site (row, col).
    private int encode(int row, int col) {
        if (row < 1 || row > numOfSites || col < 1 || col > numOfSites)
            throw new java.lang.IndexOutOfBoundsException();
        return numOfSites * (row - 1) + col;//(numOfSites * row) + col + 1; // maybe numOfSites * (row - 1) + col
    }
 */
