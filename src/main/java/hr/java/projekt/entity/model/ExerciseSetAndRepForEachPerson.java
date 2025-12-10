package hr.java.projekt.entity.model;

public class ExerciseSetAndRepForEachPerson {
    private Exercise exercise;
    private Integer set;
    private Integer rep;

    private ExerciseSetAndRepForEachPerson(){}

    public static class Builder {
        private Exercise exercise;
        private Integer set;
        private Integer rep;

        public Builder(){}

        public Builder setExercise(Exercise exercise) {
            this.exercise = exercise;
            return this;
        }

        public Builder setSet(Integer set) {
            this.set = set;
            return this;
        }

        public Builder setRep(Integer rep) {
            this.rep = rep;
            return this;
        }

        public ExerciseSetAndRepForEachPerson build(){
            ExerciseSetAndRepForEachPerson exerciseSetAndRepForEachPerson = new ExerciseSetAndRepForEachPerson();
            exerciseSetAndRepForEachPerson.exercise = exercise;
            exerciseSetAndRepForEachPerson.set = set;
            exerciseSetAndRepForEachPerson.rep = rep;
            return exerciseSetAndRepForEachPerson;
        }
    }

    public Integer getSet() {
        return set;
    }

    public Integer getRep() {
        return rep;
    }

    public Exercise getExercise() {
        return exercise;
    }

    @Override
    public String toString() {
        return "Exercise name:" + exercise.getName() + ", reps:" + rep + ", sets:" + set;
    }



}

