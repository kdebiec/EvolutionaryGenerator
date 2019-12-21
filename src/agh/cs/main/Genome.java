package agh.cs.main;

import java.util.*;

public class Genome {
    private static int GENE_SIZE = 32;
    private List<Integer> genes = new ArrayList<>();

    public List<Integer> getGenes() {
        return genes;
    }

    // Random genes constructor
    public Genome() {
        for(int i = 0; i < Genome.GENE_SIZE; i++) {
            int gene = (int) (Math.random()*8);
            this.genes.add(gene);
        }
        Collections.sort(this.genes);
    }

    // Genes inherited from parents
    public Genome(Genome g1, Genome g2) {
        List<Integer> genes1 = g1.getGenes();
        List<Integer> genes2 = g2.getGenes();

        int p1 = (int) (Math.random()*Genome.GENE_SIZE);
        int p2 = (int) (Math.random()*Genome.GENE_SIZE);
        if (p1 == p2) {
            p2++;
            p2 = p2 % Genome.GENE_SIZE;
        }

        List<Integer> resultingGenes = new ArrayList<>();

        HashMap<Integer, Integer> countGenes = new HashMap<>();
        for(int i = 0; i < 8; i++)
            countGenes.put(i, 0);

        int i;
        for(i = 0; i <= p2; i++) {
            int gene = genes1.get(i);
            resultingGenes.add(gene);
            countGenes.put(gene, countGenes.get(gene) + 1);
        }
        for(;i<=p1; i++) {
            int gene = genes2.get(i);
            resultingGenes.add(gene);
            countGenes.put(gene, countGenes.get(gene) + 1);
        }
        for(;i<Genome.GENE_SIZE; i++) {
            int gene = genes1.get(i);
            resultingGenes.add(gene);
            countGenes.put(gene, countGenes.get(gene) + 1);
        }

        // Checking if there is at least 1 gene of every type
        for(int j = 0; j<8; j++) {
            if(countGenes.get(j) == 0){
                int pos = (int) (Math.random()*Genome.GENE_SIZE);
                while(countGenes.get(resultingGenes.get(pos)) < 2){
                    pos = (int) (Math.random()*Genome.GENE_SIZE);
                }
                countGenes.put(resultingGenes.get(pos), countGenes.get(resultingGenes.get(pos)) - 1);
                resultingGenes.set(pos, j);
            }
        }

        Collections.sort(resultingGenes);
        this.genes = resultingGenes;
    }

    // Return random direction of animal based on genes
    public MoveDirection chooseDirection() {
        return MoveDirection.values()[genes.get((int) (Math.random()*Genome.GENE_SIZE))];
    }
}
