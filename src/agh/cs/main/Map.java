package agh.cs.main;

import java.util.*;

public class Map implements IWorldMap, IPositionChangeObserver {
    private int width;
    private int height;

    private Vector2d jungleStartPoint;
    private Vector2d jungleEndPoint;

    private java.util.Map<Vector2d, LinkedList<Animal>> mapAnimals = new HashMap<>();
    private java.util.Map<Vector2d, Plant> mapPlants = new LinkedHashMap<>();
    private List<Animal> animalList = new ArrayList<>();

    public Map(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Map(int width, int height, double jungleRatio) {
        this.width = width;
        this.height = height;

        this.jungleStartPoint = new Vector2d((width - (int) (width*jungleRatio))/2, (height - (int) (height*jungleRatio))/2);
        this.jungleEndPoint = new Vector2d((width - (int) (width*jungleRatio))/2 + (int) (width*jungleRatio),
                (height - (int) (height*jungleRatio))/2 + (int) (height*jungleRatio));
    }

    public void nextDay() {
        removeDeadBodies();
        moveAnimals();
        animalsEat();
        breeding();
        spawnPlants();
    }

    public void removeDeadBodies() {
        for (Iterator<Animal> iter = animalList.listIterator(); iter.hasNext(); ) {
            Animal animal = iter.next();
            if (animal.isDead()) {
                mapAnimals.get(animal.getPosition()).remove(animal);
                iter.remove();
            }
        }
    }

    public void moveAnimals() {
        for(Animal animal: animalList) {
            animal.move();
        }
    }

    public void animalsEat() {
        for(Animal animal: animalList) {
            if (mapPlants.containsKey(animal.getPosition())) {
                List<Animal> elementList = mapAnimals.get(animal.getPosition());
                if (elementList.size() == 1) {
                    mapPlants.remove(animal.getPosition());
                    animal.eat(Plant.PLANT_ENERGY);
                }
                else {
                    List<Animal> strongestAnimals = new ArrayList<>();
                    strongestAnimals.add(animal);

                    for (Animal candidateAnimal: elementList) {
                        if(candidateAnimal.getEnergy() > strongestAnimals.get(0).getEnergy()) {
                            strongestAnimals = new ArrayList<>();
                            strongestAnimals.add(candidateAnimal);
                        }
                        else if (candidateAnimal.getEnergy() == strongestAnimals.get(0).getEnergy()) {
                            strongestAnimals.add(candidateAnimal);
                        }
                    }

                    int energyPerAnimal = Plant.PLANT_ENERGY/strongestAnimals.size();

                    for (Animal strongestAnimal: strongestAnimals)
                        strongestAnimal.eat(energyPerAnimal);

                    mapPlants.remove(animal.getPosition());
                }
            }
        }
    }

    public void breeding() {
        for (java.util.Map.Entry<Vector2d, LinkedList<Animal>> position_animals : mapAnimals.entrySet()) {
            Vector2d position = position_animals.getKey();
            if (!position_animals.getValue().isEmpty()) {

                List<Animal> elementList = position_animals.getValue();
                if (elementList.size() == 2 && !elementList.get(0).isDead() && !elementList.get(1).isDead()) {
                    if(Animal.canPopulate(elementList.get(0), elementList.get(1))) {
                        Vector2d childPosition = new Vector2d(position.getX() + (int) (Math.random() * 3) - 1,
                                position.getY() + (int) (Math.random() * 3) - 1);

                        Animal child = new Animal(this.inBoundaries(childPosition), elementList.get(0), elementList.get(1));
                        elementList.add(child);
                    }
                }
                else if (elementList.size() >= 2) {
                    Animal strongestAnimal = elementList.get(0);
                    Animal strongestAnimal2 = elementList.get(1);

                    if (elementList.get(1).getEnergy() > elementList.get(0).getEnergy()) {
                        strongestAnimal = elementList.get(1);
                        strongestAnimal2 = elementList.get(0);
                    }

                    for (Animal candidateAnimal: elementList) {
                        if(candidateAnimal.getEnergy() >= strongestAnimal.getEnergy()) {
                            strongestAnimal2 = strongestAnimal;
                            strongestAnimal = candidateAnimal;
                        }
                    }

                    if(Animal.canPopulate(strongestAnimal, strongestAnimal2)) {
                        Vector2d childPosition = new Vector2d(position.getX() + (int)(Math.random()*3) - 1,
                                position.getY() + (int)(Math.random()*3) - 1);

                        Animal child = new Animal(this.inBoundaries(childPosition), strongestAnimal, strongestAnimal2);
                        elementList.add(child);
                    }
                }
            }
        }
    }

    public void spawnPlants() {
        boolean plantedInJungle = false;
        boolean plantedInOutskirts = false;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Vector2d proposedPlantPosition = new Vector2d((int) (Math.random()*(width+1)), (int) (Math.random()*(height+1)));

                if ((proposedPlantPosition.getX() < this.jungleStartPoint.getX()
                        || proposedPlantPosition.getX() > this.jungleEndPoint.getX()
                        || proposedPlantPosition.getY() < this.jungleStartPoint.getY()
                        || proposedPlantPosition.getY() > this.jungleEndPoint.getY())
                        && !plantedInOutskirts) {
                    if(mapPlants.containsKey(proposedPlantPosition))
                        continue;

                    mapPlants.put(proposedPlantPosition, new Plant(proposedPlantPosition));
                    plantedInOutskirts = true;
                }
                else if (!(proposedPlantPosition.getX() < this.jungleStartPoint.getX()
                        || proposedPlantPosition.getX() > this.jungleEndPoint.getX()
                        || proposedPlantPosition.getY() < this.jungleStartPoint.getY()
                        || proposedPlantPosition.getY() > this.jungleEndPoint.getY())
                        && !plantedInJungle) {
                    if(mapPlants.containsKey(proposedPlantPosition))
                        continue;

                    mapPlants.put(proposedPlantPosition, new Plant(proposedPlantPosition));
                    plantedInJungle = true;
                }

                if(plantedInOutskirts && plantedInJungle)
                    break;
            }

            if(plantedInOutskirts && plantedInJungle)
                break;
        }
    }

    @Override
    public void spawnOrigin(int i) {
        for(int j = 0; j < i; j++)
            this.place(new Animal(this, new Vector2d((int) (Math.random() * this.width), (int) (Math.random() * this.height))));
    }

    @Override
    public String toString() {
        MapVisualizer mapVisualizer = new MapVisualizer(this);
        return mapVisualizer.draw(new Vector2d(0, 0), new Vector2d(width, height));
    }

    @Override
    public boolean place(Animal animal) {
        animalList.add(animal);
        if(mapAnimals.containsKey(animal.getPosition())) {
            mapAnimals.get(animal.getPosition()).add(animal);
        }
        else {
            LinkedList<Animal> listElements = new LinkedList<>();
            listElements.add(animal);
            mapAnimals.put(animal.getPosition(), listElements);
        }

        animal.addObserver(this);
        return true;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return (mapAnimals.containsKey(position) && !mapAnimals.get(position).isEmpty())
                || mapPlants.containsKey(position);
    }

    @Override
    public MapElement objectAt(Vector2d position) {
        if (mapAnimals.containsKey(position) && !mapAnimals.get(position).isEmpty())
            return (MapElement) mapAnimals.get(position).toArray()[0];
        else
            return (MapElement) mapPlants.get(position);
    }

    @Override
    public void positionChanged(Animal animal, Vector2d oldPosition, Vector2d newPosition) {
        mapAnimals.get(oldPosition).remove(animal);

        if(mapAnimals.containsKey(newPosition)) {
            mapAnimals.get(newPosition).add(animal);
        }
        else {
            LinkedList<Animal> listElements = new LinkedList<>();
            listElements.add(animal);
            mapAnimals.put(newPosition, listElements);
        }
    }

    public Vector2d inBoundaries(Vector2d proposedPos) {
        int x = proposedPos.getX();
        int y = proposedPos.getY();
        if (x > width)
            x -= width;
        else if (x < 0)
            x += width;

        if (y > height)
            y -= height;
        else if (y < 0)
            y += height;

        return new Vector2d(x, y);
    }
}
